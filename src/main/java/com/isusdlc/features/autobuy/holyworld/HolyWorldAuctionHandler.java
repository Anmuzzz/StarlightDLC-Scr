package com.isusdlc.features.autobuy.holyworld;

import com.isusdlc.display.screens.autobuy.history.HistoryManager;
import com.isusdlc.features.autobuy.AutoBuyBanList;
import com.isusdlc.features.autobuy.CommandSender;
import com.isusdlc.features.autobuy.TimerUtil;
import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.manager.AutoBuyManager;
import com.isusdlc.features.autobuy.util.AuctionUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class HolyWorldAuctionHandler {
   private static final MinecraftClient mc = MinecraftClient.getInstance();
   private static final Pattern HOLYWORLD_PRICE_PATTERN = Pattern.compile("(?:\\$\\s*([\\d,.\\s]+)|([\\d,.\\s]+)\\s*\\$|(?:Цена|Price)[:\\s]*([\\d,.\\s]+))");
   private static final String BUY_CONFIRM_TITLE = "Покупка предмета";
   private static final int ITEMS_START_SLOT = 0;
   private static final int ITEMS_END_SLOT = 44;
   private static final int BUY_DELAY_MS = 0;
   private static final int CONFIRM_DELAY_MS = 0;
   private static final int POST_BUY_COOLDOWN_MS = 0;
   private final AutoBuyManager autoBuyManager;
   private final TimerUtil refreshTimer = TimerUtil.create();
   private final TimerUtil buyTimer = TimerUtil.create();
   private final TimerUtil refreshCooldown = TimerUtil.create();
   private final Random random = new Random();
   private int updateCounter = 0;
   private final TimerUtil reconnectTimer = TimerUtil.create();
   private boolean isReconnecting = false;
   private static final long REFRESH_COOLDOWN_MS = 3000L;
   private final Set<String> processedItems = ConcurrentHashMap.newKeySet();
   private final Map<String, Long> processedItemsTime = new ConcurrentHashMap<>();
   private final Map<String, HolyWorldAuctionHandler.PendingPurchase> pendingPurchases = new ConcurrentHashMap<>();
   private long lastProcessedClear = System.currentTimeMillis();
   private boolean waitingForConfirmation = false;
   private int pendingBuySyncId = -1;
   private int pendingBuySlotId = -1;
   private int pendingBuyButton = 0;
   private String pendingBuyItemKey = null;
   private Item pendingBuyExpectedItem = null;
   private int pendingBuyExpectedPrice = -1;
   private long pendingBuyTime = 0L;
   private int pendingBuyDelayMs = 0;
   private SlotActionType pendingBuyAction = SlotActionType.QUICK_MOVE;
   private static final int CONFIRM_SLOT_VERIFY_INDEX = 9;
   private int pendingConfirmSyncId = -1;
   private int pendingConfirmSlot = 0;
   private SlotActionType pendingConfirmAction = SlotActionType.QUICK_MOVE;
   private long pendingConfirmTime = 0L;
   private int pendingConfirmDelayMs = 0;
   private int pendingRefreshSyncId = -1;
   private long pendingRefreshTime = 0L;
   private int pendingRefreshDelayMs = 0;
   private long lastBuyClickTime = 0L;
   private long lastConfirmClickTime = 0L;
   private int postBuyCooldownMs = 0;
   private long lastRefreshTime = 0L;
   private int postRefreshCooldownMs = 0;
   private static final long NO_WALK_AFTER_PURCHASE_MS = 2500L;
   private static final long STALE_SLOTS_MS = 3000L;
   private static final long POST_PURCHASE_CLOSE_REOPEN_DELAY_MS = 100L;
   private long lastSlotFingerprint = 0L;
   private long lastSlotFingerprintTime = 0L;
   private boolean pendingCloseAndReopen = false;
   private long pendingCloseAndReopenTime = 0L;

   public long getRefreshDelayMs() {
      return this.random.nextLong(1000, 1300);
   }

   public HolyWorldAuctionHandler(AutoBuyManager var1) {
      this.autoBuyManager = var1;
   }

   public void clearPendingConfirm() {
      this.pendingConfirmSyncId = -1;
      this.waitingForConfirmation = false;
   }

   public void clearStaleConfirmIfOnAuction() {
      this.clearPendingConfirm();
   }

   public boolean hasPendingClick() {
      return this.pendingBuySyncId >= 0 || this.pendingConfirmSyncId >= 0 || this.pendingRefreshSyncId >= 0;
   }

   public boolean hasRecentPurchaseActivity() {
      long var1 = System.currentTimeMillis();
      return this.hasPendingClick()
         || this.lastBuyClickTime > 0L && var1 - this.lastBuyClickTime < 2500L
         || this.lastConfirmClickTime > 0L && var1 - this.lastConfirmClickTime < 2500L;
   }

   public void clear() {
      this.processedItems.clear();
      this.processedItemsTime.clear();
      this.pendingPurchases.clear();
      this.waitingForConfirmation = false;
      this.pendingBuySyncId = -1;
      this.pendingBuySlotId = -1;
      this.pendingBuyItemKey = null;
      this.pendingBuyExpectedItem = null;
      this.pendingBuyExpectedPrice = -1;
      this.pendingConfirmSyncId = -1;
      this.pendingRefreshSyncId = -1;
      this.lastSlotFingerprint = 0L;
      this.pendingCloseAndReopen = false;
      this.refreshTimer.resetCounter();
      this.buyTimer.resetCounter();
      this.reconnectTimer.resetCounter();
      this.updateCounter = 0;
      this.isReconnecting = false;
      this.lastProcessedClear = System.currentTimeMillis();
   }

   public void tickReconnect() {
      if (mc.player != null) {
         if (this.pendingCloseAndReopen && System.currentTimeMillis() - this.pendingCloseAndReopenTime >= 100L) {
            CommandSender.sendCommand(mc.player, "/ah");
            this.pendingCloseAndReopen = false;
         } else if (this.isReconnecting) {
            if (this.reconnectTimer.hasTimeElapsed(10000L)) {
               CommandSender.sendCommand(mc.player, "/ah");
               this.updateCounter = 0;
               this.isReconnecting = false;
               this.refreshTimer.resetCounter();
               this.buyTimer.resetCounter();
            }
         }
      }
   }

   public void tick(int var1, List<Slot> var2, List<AutoBuyableItem> var3, long var4, long var6) {
      if (mc.player != null && mc.interactionManager != null) {
         long var8 = System.currentTimeMillis();
         if (var2 != null && var2.size() > 36) {
            long var10 = this.computeSlotFingerprint(var2);
            if (var10 == this.lastSlotFingerprint && this.lastSlotFingerprint != 0L) {
               if (var8 - this.lastSlotFingerprintTime >= 3000L) {
                  if (mc.currentScreen != null && mc.player != null) {
                     mc.player.closeHandledScreen();
                     CommandSender.sendCommand(mc.player, "/ah");
                  }

                  this.lastSlotFingerprint = 0L;
                  return;
               }
            } else {
               this.lastSlotFingerprint = var10;
               this.lastSlotFingerprintTime = var8;
            }
         }

         if (this.pendingConfirmSyncId >= 0 && var8 - this.pendingConfirmTime >= this.pendingConfirmDelayMs) {
            boolean var18 = false;
            if (var2 != null && var2.size() > 9) {
               Slot var19 = (Slot)var2.get(9);
               ItemStack var21 = var19 != null ? var19.getStack() : null;
               HolyWorldAuctionHandler.PendingPurchase var13 = this.getMostRecentPendingPurchase();
               if (var21 != null && !var21.isEmpty() && var13 != null) {
                  boolean var14 = var21.getItem() == var13.itemStack.getItem();
                  int var15 = this.getHolyWorldPrice(var21, var2, 9);
                  if (!var14) {
                     var18 = true;
                  } else if (var15 > 0 && var15 != var13.price) {
                     var18 = true;
                  }
               } else if (var13 == null) {
                  var18 = true;
               }
            }

            if (var18) {
               mc.player.closeHandledScreen();
               this.waitingForConfirmation = false;
               this.pendingConfirmSyncId = -1;
            } else {
               int var20 = this.pendingConfirmSlot;
               if (var2 != null && this.pendingConfirmSlot >= 0 && this.pendingConfirmSlot < var2.size()) {
                  Slot var22 = (Slot)var2.get(this.pendingConfirmSlot);
                  if (var22 != null) {
                     var20 = var22.id;
                  }
               }

               mc.interactionManager.clickSlot(this.pendingConfirmSyncId, var20, 0, this.pendingConfirmAction, mc.player);
               this.lastConfirmClickTime = var8;
               this.waitingForConfirmation = false;
               this.buyTimer.resetCounter();
               this.pendingConfirmSyncId = -1;
               mc.player.closeHandledScreen();
               this.pendingCloseAndReopen = true;
               this.pendingCloseAndReopenTime = var8;
            }
         } else if (this.pendingBuySyncId >= 0 && var8 - this.pendingBuyTime >= this.pendingBuyDelayMs) {
            int var17 = this.findSlotIndexById(var2, this.pendingBuySlotId);
            if (var17 >= 0 && var2 != null && var17 < var2.size()) {
               Slot var11 = (Slot)var2.get(var17);
               ItemStack var12 = var11.getStack();
               if (!var12.isEmpty()
                  && var12.getItem() == this.pendingBuyExpectedItem
                  && this.getHolyWorldPrice(var12, var2, var17) == this.pendingBuyExpectedPrice) {
                  mc.interactionManager.clickSlot(this.pendingBuySyncId, this.pendingBuySlotId, this.pendingBuyButton, this.pendingBuyAction, mc.player);
                  if (this.pendingBuyItemKey != null) {
                     this.processedItems.add(this.pendingBuyItemKey);
                     this.processedItemsTime.put(this.pendingBuyItemKey, var8);
                  }

                  this.lastBuyClickTime = var8;
                  this.postBuyCooldownMs = 0;
                  this.pendingBuySyncId = -1;
                  this.pendingBuySlotId = -1;
                  this.pendingBuyItemKey = null;
                  this.pendingBuyExpectedItem = null;
                  this.pendingBuyExpectedPrice = -1;
                  this.buyTimer.resetCounter();
               } else {
                  this.pendingBuySyncId = -1;
                  this.pendingBuySlotId = -1;
                  this.pendingBuyItemKey = null;
                  this.pendingBuyExpectedItem = null;
                  this.pendingBuyExpectedPrice = -1;
               }
            } else {
               this.pendingBuySyncId = -1;
               this.pendingBuySlotId = -1;
               this.pendingBuyItemKey = null;
               this.pendingBuyExpectedItem = null;
               this.pendingBuyExpectedPrice = -1;
            }
         } else if (this.pendingRefreshSyncId >= 0 && var8 - this.pendingRefreshTime >= this.pendingRefreshDelayMs) {
            this.refreshAuction(this.pendingRefreshSyncId, var2);
            this.lastRefreshTime = var8;
            this.postRefreshCooldownMs = 0;
            this.updateCounter++;
            this.refreshTimer.resetCounter();
            this.refreshCooldown.resetCounter();
            this.pendingRefreshSyncId = -1;
         } else if (var3 != null && !var3.isEmpty()) {
            this.clearOldProcessedItems();
            this.clearOldPendingPurchases();
            if (!this.waitingForConfirmation) {
               if (var8 - this.lastBuyClickTime >= this.postBuyCooldownMs || this.lastBuyClickTime <= 0L) {
                  if (var8 - this.lastRefreshTime >= this.postRefreshCooldownMs || this.lastRefreshTime <= 0L) {
                     boolean var16 = this.scanAndBuy(var1, var2, var3);
                     if (var16) {
                        this.buyTimer.resetCounter();
                     }

                     if (this.updateCounter >= 800 && !this.isReconnecting) {
                        this.isReconnecting = true;
                        this.reconnectTimer.resetCounter();
                        if (mc.currentScreen instanceof GenericContainerScreen) {
                           mc.player.closeHandledScreen();
                           mc.player.networkHandler.sendChatMessage(".rct " + this.random.nextLong(6, 60));
                        }
                     } else if (this.isReconnecting && this.reconnectTimer.hasTimeElapsed(10000L)) {
                        CommandSender.sendCommand(mc.player, "/ah");
                        this.updateCounter = 0;
                        this.isReconnecting = false;
                        this.refreshTimer.resetCounter();
                        this.buyTimer.resetCounter();
                     } else if (!this.isReconnecting) {
                        if (this.refreshTimer.hasTimeElapsed(var4)) {
                           this.pendingRefreshSyncId = var1;
                           this.pendingRefreshTime = var8;
                           this.pendingRefreshDelayMs = 0;
                           this.refreshTimer.resetCounter();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void handleConfirmation(int var1, List<Slot> var2, String var3) {
      if (mc.player != null && mc.interactionManager != null) {
         if ((var3.contains("Покупка предмета") || var3.contains("Покупка") || var3.contains("Подтверд")) && this.pendingConfirmSyncId < 0) {
            this.pendingConfirmSyncId = var1;
            this.pendingConfirmSlot = 0;
            this.pendingConfirmAction = SlotActionType.PICKUP;
            this.pendingConfirmTime = System.currentTimeMillis();
            this.pendingConfirmDelayMs = 0;
         }
      }
   }

   public void handleConfirmation(int var1, List<Slot> var2) {
      this.handleConfirmation(var1, var2, "Покупка предмета");
   }

   public void refreshAuction(int var1, List<Slot> var2) {
      if (mc.player != null && mc.interactionManager != null) {
         int var3 = var2.size() - 36;
         int var4 = var3 - 7;
         if (var4 < 0) {
            var4 = 49;
         }

         mc.interactionManager.clickSlot(var1, var4, 0, SlotActionType.PICKUP, mc.player);
         this.processedItems.clear();
         this.processedItemsTime.clear();
      }
   }

   private boolean scanAndBuy(int var1, List<Slot> var2, List<AutoBuyableItem> var3) {
      HashMap<Item, List<AutoBuyableItem>> var4 = new HashMap<>();

      for (AutoBuyableItem var6 : var3) {
         if (var6 != null && var6.isEnabled()) {
            Item var7 = var6.getItem();
            var4.computeIfAbsent(var7, var0 -> new ArrayList<>()).add(var6);
         }
      }

      int var32 = Math.min(var2.size(), 45);

      for (int var33 = 0; var33 < var32; var33++) {
         Slot var34 = (Slot)var2.get(var33);
         if (var34 != null && !var34.getStack().isEmpty()) {
            ItemStack var8 = var34.getStack();
            String var9 = var8.getName().getString();
            if (!AuctionUtils.isArmorItem(var8) || !AuctionUtils.hasThornsEnchantment(var8)) {
               int var10 = this.getHolyWorldPrice(var8, var2, var33);
               if (var10 > 0) {
                  String var11 = var9 + "|" + var10 + "|" + var33;
                  if (!this.processedItems.contains(var11)) {
                     Item var12 = var8.getItem();
                     List<AutoBuyableItem> var13 = var4.get(var12);
                     if (var13 != null && !var13.isEmpty()) {
                        for (AutoBuyableItem var15 : var13) {
                           if (var15 != null && var15.isEnabled()) {
                              ItemStack var16 = var15.createItemStack();
                              boolean var17 = AuctionUtils.compareItem(var8, var16);
                              if (var17) {
                                 int var18 = var8.getCount();
                                 if (var18 <= 0) {
                                    var18 = 1;
                                 }

                                 int var19 = var10 / var18;
                                 int var20 = this.getMaxPrice(var15);
                                 if (var20 > 0 && var19 <= var20 && (!var15.getSettings().isCanHaveQuantity() || var18 >= var15.getSettings().getMinQuantity())
                                    )
                                  {
                                    String var21 = this.getSellerName(var8);
                                    if (var21 != null && !var21.isEmpty()) {
                                    }

                                    if (var21 != null && !var21.isEmpty()) {
                                       String var22 = mc.player != null ? mc.player.getName().getString() : null;
                                       if (var22 != null) {
                                          String var23 = var22.replaceAll("§[0-9a-fk-or]", "").replaceAll("§.", "").trim();
                                          String var24 = var21.replaceAll("§[0-9a-fk-or]", "").replaceAll("§.", "").trim();
                                          if (var23.equalsIgnoreCase(var24)) {
                                             continue;
                                          }
                                       }

                                       if (AutoBuyBanList.isBanned(var21)) {
                                          continue;
                                       }
                                    }

                                    boolean var35 = var3.stream()
                                       .anyMatch(
                                          var1x -> var1x != null && var1x.getDisplayName() != null && var1x.getDisplayName().equals(var15.getDisplayName())
                                       );
                                    if (var35) {
                                       NbtComponent var36 = (NbtComponent)var8.get(DataComponentTypes.CUSTOM_DATA);
                                       if (var36 != null) {
                                          var36.copyNbt().toString();
                                       } else {
                                          String var10000 = "null";
                                       }

                                       if (var33 < var2.size()) {
                                          Slot var37 = (Slot)var2.get(var33);
                                          if (var37 != null) {
                                             ItemStack var29 = var37.getStack();
                                             if (var29 != null && !var29.isEmpty() && AuctionUtils.compareItem(var29, var16)) {
                                                String var30 = var9.toLowerCase().trim();
                                                this.pendingPurchases.put(var30, new HolyWorldAuctionHandler.PendingPurchase(var29.copy(), var9, var10));
                                                System.out.println(
                                                   String.format(
                                                      "[AutoBuy HolyWorld] Покупка: предмет=\"%s\", кол-во=%d, цена=%d¤, слот=%d, продавец=%s, макс.за_шт=%d, pricePerItem=%d, matched_gui=\"%s\", itemType=%s",
                                                      var9,
                                                      var18,
                                                      var10,
                                                      var33,
                                                      var21 != null ? var21 : "—",
                                                      var20,
                                                      var19,
                                                      var15.getDisplayName() != null ? var15.getDisplayName() : "?",
                                                      var8.getItem().toString()
                                                   )
                                                );
                                                int var31 = var37.id;
                                                if (var37.id == 0 || var37.id != var33) {
                                                   var31 = var33;
                                                }

                                                this.pendingBuySyncId = var1;
                                                this.pendingBuySlotId = var31;
                                                this.pendingBuyItemKey = var11;
                                                this.pendingBuyExpectedItem = var8.getItem();
                                                this.pendingBuyExpectedPrice = var10;
                                                this.pendingBuyTime = System.currentTimeMillis();
                                                this.pendingBuyDelayMs = 0;
                                                this.pendingBuyAction = SlotActionType.PICKUP;
                                                this.pendingBuyButton = 0;
                                                return true;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private int getHolyWorldPrice(ItemStack var1, List<Slot> var2, int var3) {
      int var4 = AuctionUtils.getPrice(var1);
      if (var4 > 0) {
         return var4;
      }

      LoreComponent var5 = (LoreComponent)var1.get(DataComponentTypes.LORE);
      if (var5 != null && !var5.lines().isEmpty()) {
         for (Text var7 : var5.lines()) {
            String var8 = var7.getString();
            Matcher var9 = HOLYWORLD_PRICE_PATTERN.matcher(var8);
            if (var9.find()) {
               String var10 = null;

               for (int var11 = 1; var11 <= 3; var11++) {
                  if (var9.group(var11) != null) {
                     var10 = var9.group(var11);
                     break;
                  }
               }

               if (var10 != null) {
                  try {
                     var10 = var10.replaceAll("[\\s,.]", "");
                     var4 = Integer.parseInt(var10);
                     if (var4 > 0) {
                        return var4;
                     }
                  } catch (NumberFormatException var12) {
                  }
               }
            }
         }
      }

      var4 = AuctionUtils.getPriceFromNearbySlots(var2, var3);
      return var4 > 0 ? var4 : -1;
   }

   private int getMaxPrice(AutoBuyableItem var1) {
      int var7 = var1.getSettings().getBuyBelow();
      return var7 > 0 ? var7 : var1.getPrice();
   }

   private int findSlotIndexById(List<Slot> var1, int var2) {
      if (var1 == null) {
         return -1;
      }

      for (int var3 = 0; var3 < var1.size(); var3++) {
         Slot var4 = (Slot)var1.get(var3);
         if (var4 != null && var4.id == var2) {
            return var3;
         }
      }

      return -1;
   }

   private long computeSlotFingerprint(List<Slot> var1) {
      int var2 = var1.size() - 36;
      if (var2 <= 0) {
         return 0L;
      }

      long var3 = 0L;

      for (int var5 = 0; var5 < var2 && var5 < var1.size(); var5++) {
         ItemStack var6 = ((Slot)var1.get(var5)).getStack();
         long var7 = var6.isEmpty() ? 0L : var6.getItem().hashCode() * 31L + var6.getCount();
         var3 = var3 * 31L + var7;
      }

      return var3;
   }

   private void clearOldProcessedItems() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.lastProcessedClear > 30000L) {
         this.processedItems.removeIf(var3 -> {
            Long var4 = this.processedItemsTime.get(var3);
            if (var4 != null && var1 - var4 > 30000L) {
               this.processedItemsTime.remove(var3);
               return true;
            } else {
               return false;
            }
         });
         this.lastProcessedClear = var1;
      }
   }

   private void clearOldPendingPurchases() {
      long var1 = System.currentTimeMillis();
      this.pendingPurchases.entrySet().removeIf(var2 -> var1 - var2.getValue().timestamp > 10000L);
   }

   private HolyWorldAuctionHandler.PendingPurchase getMostRecentPendingPurchase() {
      return this.pendingPurchases.isEmpty()
         ? null
         : this.pendingPurchases.values().stream().max(Comparator.comparingLong(var0 -> var0.timestamp)).orElse(null);
   }

   private static String normalizeItemName(String var0) {
      return var0 == null ? "" : var0.replaceAll("§[0-9a-fk-or]", "").replaceAll("§.", "").trim();
   }

   public void confirmPurchase(String var1, int var2) {
      String var3 = normalizeItemName(var1);
      if (!var3.isEmpty()) {
         HolyWorldAuctionHandler.PendingPurchase var4 = null;
         String var5 = null;

         for (Entry var7 : this.pendingPurchases.entrySet()) {
            HolyWorldAuctionHandler.PendingPurchase var8 = (HolyWorldAuctionHandler.PendingPurchase)var7.getValue();
            if (var8.price == var2) {
               String var9 = normalizeItemName(var8.itemStack.getName().getString());
               if (!var9.isEmpty() && (var9.equalsIgnoreCase(var3) || var9.contains(var3) || var3.contains(var9))) {
                  var4 = var8;
                  var5 = (String)var7.getKey();
                  break;
               }
            }
         }

         if (var4 != null) {
            String var10 = normalizeItemName(var4.itemStack.getName().getString());
            if (var10.isEmpty()) {
               var10 = var4.itemStack.getName().getString();
            }

            HistoryManager.getInstance().addPurchaseWithItem(var4.itemStack, var10, var2);
            this.pendingPurchases.remove(var5);
         } else {
            HistoryManager.getInstance().addPurchaseFromMessage(var1, var2);
         }
      }
   }

   public void resetTimers() {
      this.refreshTimer.resetCounter();
      this.buyTimer.resetCounter();
      this.refreshCooldown.resetCounter();
   }

   public boolean isWaitingForConfirmation() {
      return this.waitingForConfirmation;
   }

   public void setWaitingForConfirmation(boolean var1) {
      this.waitingForConfirmation = var1;
   }

   private String getSellerName(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         LoreComponent var2 = (LoreComponent)var1.get(DataComponentTypes.LORE);
         if (var2 != null && !var2.lines().isEmpty()) {
            Pattern var3 = Pattern.compile("(?i)(?:Продавец|Продaвeц|Seller)[:\\s]+(.+)");

            for (Text var5 : var2.lines()) {
               String var6 = var5.getString();
               Matcher var7 = var3.matcher(var6);
               if (var7.find()) {
                  String var8 = var7.group(1).trim();
                  var8 = var8.replaceAll("§[0-9a-fk-or]", "");
                  var8 = var8.replaceAll("§.", "");
                  var8 = var8.replaceAll("[▍▶▎]", "");
                  var8 = var8.trim();
                  if (!var8.isEmpty()) {
                     return var8;
                  }
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static class PendingPurchase {
      public final ItemStack itemStack;
      public final String itemName;
      public final int price;
      public final long timestamp;

      public PendingPurchase(ItemStack var1, String var2, int var3) {
         this.itemStack = var1;
         this.itemName = var2;
         this.price = var3;
         this.timestamp = System.currentTimeMillis();
      }
   }
}
