package com.isusdlc.features.autobuy.parser;

import com.isusdlc.features.autobuy.CommandSender;
import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.manager.AutoBuyManager;
import com.isusdlc.features.autobuy.originalitems.ItemRegistry;
import com.isusdlc.features.autobuy.util.AuctionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class HolyWorldPriceParser {
   private static final Pattern HOLYWORLD_PRICE_PATTERN = Pattern.compile("(?:\\$\\s*([\\d,.\\s]+)|([\\d,.\\s]+)\\s*\\$|(?:Цена|Price)[:\\s]*([\\d,.\\s]+))");
   private static volatile boolean enabled = false;
   private static volatile int discountPercent = 0;
   private static HolyWorldPriceParser.Stage stage = HolyWorldPriceParser.Stage.IDLE;
   private static long stageUntilMs = 0L;
   private static int currentIndex = 0;
   private static List<AutoBuyableItem> items = new ArrayList<>();
   private static AutoBuyableItem currentItem = null;
   private static long lastEmptyItemsNotifyMs = 0L;
   private static int retryCount = 0;
   private static final int MAX_RETRIES = 3;
   private static int sortingClickCount = 0;
   private static final int MAX_SORTING_CLICKS = 5;
   private static int pageRetryCount = 0;
   private static final int MAX_PAGE_RETRIES = 3;
   private static boolean sortingComplete = false;
   private static final Pattern LEVEL_PATTERN = Pattern.compile("(.+?)\\s*\\[(\\d+)\\s*[уУ]р\\.?\\]");

   private HolyWorldPriceParser() {
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public static void setEnabled(boolean var0) {
      if (enabled != var0) {
         if (var0) {
            List var1 = resolveItemsToParse();
            if (var1 == null || var1.isEmpty()) {
               log("[Parser] Start requested, but item list is empty -> OFF");
               enabled = false;
               stage = HolyWorldPriceParser.Stage.IDLE;
               stageUntilMs = 0L;
               currentIndex = 0;
               currentItem = null;
               items = new ArrayList<>();
               return;
            }

            enabled = true;
            stage = HolyWorldPriceParser.Stage.OPEN_AH;
            stageUntilMs = 0L;
            currentIndex = 0;
            currentItem = null;
            items = var1;
            sortingClickCount = 0;
            pageRetryCount = 0;
            sortingComplete = false;
            log("[Parser] ON. Discount=" + discountPercent + "%, items=" + items.size() + ", stage=" + stage);
            AutoBuyManager.getInstance().setEnabled(false);
            log("[Parser] Открываем /ah для настройки сортировки");
            if (MinecraftClient.getInstance().player != null) {
               CommandSender.sendCommand(MinecraftClient.getInstance().player, "/ah");
            }

            stageUntilMs = System.currentTimeMillis() + 500L;
         } else {
            log("[Parser] OFF");
            enabled = false;
            stage = HolyWorldPriceParser.Stage.IDLE;
            stageUntilMs = 0L;
            currentIndex = 0;
            currentItem = null;
         }
      }
   }

   public static int getDiscountPercent() {
      return discountPercent;
   }

   public static String getCurrentItemName() {
      return currentItem != null ? currentItem.getDisplayName() : null;
   }

   public static int findCheapestPerItemStatic(List<Slot> var0, AutoBuyableItem var1) {
      return findCheapestPerItem(var0, var1);
   }

   public static void setDiscountPercent(int var0) {
      int var1 = discountPercent;
      discountPercent = Math.max(0, Math.min(100, var0));
      if (var1 != discountPercent) {
      }
   }

   public static void tickNoContainer(MinecraftClient var0) {
      if (!enabled) {
         log("[Parser] tickNoContainer: парсер выключен");
      } else if (var0 == null || var0.player == null) {
         log(Formatting.RED + "[Parser] tickNoContainer: mc или player null");
      } else if (stage == HolyWorldPriceParser.Stage.IDLE) {
         log("[Parser] tickNoContainer: stage=" + stage + ", пропускаем");
      } else {
         long var2 = System.currentTimeMillis();
         if (var2 >= stageUntilMs) {
            if (stage == HolyWorldPriceParser.Stage.OPEN_AH
               || stage == HolyWorldPriceParser.Stage.SETUP_SORTING
               || stage == HolyWorldPriceParser.Stage.RESTORE_SORTING
               || stage == HolyWorldPriceParser.Stage.CHECK_NEXT_PAGE
               || stage == HolyWorldPriceParser.Stage.FINISH_WAIT) {
               log("[Parser] tickNoContainer: контейнер не открыт в стадии " + stage + ", открываем /ah");
               CommandSender.sendCommand(var0.player, "/ah");
               stageUntilMs = var2 + 500L;
            } else if (stage != HolyWorldPriceParser.Stage.SEND_SEARCH) {
               if (currentItem != null) {
                  retryCount++;
                  if (retryCount < 3) {
                     String var5 = getSearchQuery(currentItem);
                     log("[Parser] Контейнер не открыт, повтор (" + retryCount + "/3): /ah search " + var5);
                     CommandSender.sendCommand(var0.player, "/ah search " + var5);
                     stageUntilMs = var2 + 1000L;
                  } else {
                     log("[Parser] " + currentItem.getDisplayName() + ": контейнер не открылся после 3 попыток, пропуск");
                     currentIndex++;
                     retryCount = 0;
                     stage = HolyWorldPriceParser.Stage.SEND_SEARCH;
                     stageUntilMs = 0L;
                  }
               }
            } else {
               if (items == null || items.isEmpty()) {
                  items = resolveItemsToParse();
                  currentIndex = 0;
               }

               if (items != null && !items.isEmpty()) {
                  if (currentIndex >= items.size()) {
                     stage = HolyWorldPriceParser.Stage.FINISH_WAIT;
                     stageUntilMs = var2 + 2000L;
                     log("[Parser] SEND_SEARCH: все предметы обработаны (" + items.size() + "), перехожу в FINISH_WAIT");
                  } else {
                     currentItem = items.get(currentIndex);
                     String var4 = getSearchQuery(currentItem);
                     retryCount = 0;
                     log("[Parser] (" + (currentIndex + 1) + "/" + items.size() + ") Поиск: " + currentItem.getDisplayName());
                     CommandSender.sendCommand(var0.player, "/ah search " + var4);
                     stage = HolyWorldPriceParser.Stage.WAIT_RESULTS;
                     stageUntilMs = var2 + 500L;
                  }
               } else {
                  setEnabled(false);
               }
            }
         }
      }
   }

   public static void tickAuction(MinecraftClient var0, List<Slot> var1) {
      if (!enabled) {
         log("[Parser] tickAuction: парсер выключен");
      } else if (var0 != null && var0.player != null && var0.interactionManager != null) {
         if (items == null || items.isEmpty()) {
            items = resolveItemsToParse();
            currentIndex = 0;
         }

         if (items != null && !items.isEmpty()) {
            long var12 = System.currentTimeMillis();
            int var6 = 0;
            if (var0.player.currentScreenHandler != null) {
               var6 = var0.player.currentScreenHandler.syncId;
            }

            int var7 = var1.size() - 36;
            int var8 = var7 - 2;
            int var9 = var7 - 4;
            if (var8 < 0) {
               var8 = 52;
            }

            if (var9 < 0) {
               var9 = 50;
            }

            switch (stage) {
               case OPEN_AH:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  log("[Parser] OPEN_AH: аукцион открыт, переходим к настройке сортировки");
                  stage = HolyWorldPriceParser.Stage.SETUP_SORTING;
                  sortingClickCount = 0;
                  stageUntilMs = var12 + 100L;
                  break;
               case SETUP_SORTING:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  boolean var15 = checkSortingState(var1, var8, "дешевые за ед", true);
                  if (!var15 && sortingClickCount < 5) {
                     var0.interactionManager.clickSlot(var6, var8, 0, SlotActionType.QUICK_MOVE, var0.player);
                     sortingClickCount++;
                     stageUntilMs = var12 + 700L;
                     break;
                  }

                  if (var15) {
                     log("[Parser] SETUP_SORTING: сортировка настроена (дешевые за ед.)");
                  } else {
                     log("[Parser] SETUP_SORTING: достигнут лимит кликов (5), продолжаем");
                  }

                  sortingComplete = true;
                  stage = HolyWorldPriceParser.Stage.SEND_SEARCH;
                  stageUntilMs = var12 + 300L;
                  return;
               case SEND_SEARCH:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  if (currentIndex >= items.size()) {
                     stage = HolyWorldPriceParser.Stage.FINISH_WAIT;
                     stageUntilMs = var12 + 2000L;
                     log("[Parser] SEND_SEARCH: все предметы обработаны (" + items.size() + "), перехожу в FINISH_WAIT");
                     log("[Parser] Завершено. Ожидание 3сек перед включением AutoBuy...");
                     return;
                  }

                  currentItem = items.get(currentIndex);
                  String var14 = getSearchQuery(currentItem);
                  retryCount = 0;
                  log("[Parser] (" + (currentIndex + 1) + "/" + items.size() + ") Поиск: " + currentItem.getDisplayName());
                  CommandSender.sendCommand(var0.player, "/ah search " + var14);
                  stage = HolyWorldPriceParser.Stage.WAIT_RESULTS;
                  stageUntilMs = var12 + 500L;
                  break;
               case WAIT_RESULTS:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  int var13 = findCheapestPerItem(var1, currentItem);
                  if (var13 > 0) {
                     int var11 = (int)Math.floor(var13 * (100.0 - discountPercent) / 100.0);
                     if (var11 < 0) {
                        var11 = 0;
                     }

                     log("[Parser] ✓ " + currentItem.getDisplayName() + ": найдена цена " + var13 + " -> установлена " + var11 + " (-" + discountPercent + "%)");
                     System.out.println("Цена для \"" + currentItem.getDisplayName() + "\" установлена: " + formatPrice(var11));
                     currentIndex++;
                     pageRetryCount = 0;
                     stage = HolyWorldPriceParser.Stage.WAIT_BETWEEN;
                     stageUntilMs = var12 + 2000L;
                     log("[Parser] Переход к следующему предмету, индекс: " + currentIndex + "/" + items.size());
                  } else {
                     boolean var16 = hasItemsInSlots(var1);
                     if (var16 && pageRetryCount < 3) {
                        log(
                           "[Parser] ✗ "
                              + currentItem.getDisplayName()
                              + ": не найдено на странице, переход на следующую (попытка "
                              + (pageRetryCount + 1)
                              + "/3)"
                        );
                        stage = HolyWorldPriceParser.Stage.CHECK_NEXT_PAGE;
                        stageUntilMs = var12 + 100L;
                     } else {
                        log("[Parser] ✗ " + currentItem.getDisplayName() + ": не найдено на аукционе, пропуск");
                        System.out.println("\"" + currentItem.getDisplayName() + "\" не найдено на аукционе");
                        currentIndex++;
                        pageRetryCount = 0;
                        retryCount = 0;
                        stage = HolyWorldPriceParser.Stage.WAIT_BETWEEN;
                        stageUntilMs = var12 + 1000L;
                        log("[Parser] Переход к следующему предмету, индекс: " + currentIndex + "/" + items.size());
                     }
                  }
                  break;
               case CHECK_NEXT_PAGE:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  log("[Parser] CHECK_NEXT_PAGE: клик по следующей странице, слот=" + var9);
                  var0.interactionManager.clickSlot(var6, var9, 0, SlotActionType.PICKUP, var0.player);
                  pageRetryCount++;
                  stage = HolyWorldPriceParser.Stage.WAIT_RESULTS;
                  stageUntilMs = var12 + 500L;
                  break;
               case WAIT_BETWEEN:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  stage = HolyWorldPriceParser.Stage.SEND_SEARCH;
                  break;
               case RESTORE_SORTING:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  boolean var10 = checkSortingState(var1, var8, "сначала новые", true);
                  if (!var10 && sortingClickCount < 5) {
                     log("[Parser] RESTORE_SORTING: клик по сортировке (" + (sortingClickCount + 1) + "/5), слот=" + var8);
                     var0.interactionManager.clickSlot(var6, var8, 0, SlotActionType.PICKUP, var0.player);
                     sortingClickCount++;
                     stageUntilMs = var12 + 100L;
                     break;
                  }

                  if (var10) {
                     log("[Parser] RESTORE_SORTING: сортировка «сначала новые» установлена");
                  } else {
                     log("[Parser] RESTORE_SORTING: достигнут лимит кликов, включаю AutoBuy");
                  }

                  log("[Parser] Готово! Включаю AutoBuy.");
                  AutoBuyManager.getInstance().setEnabled(true);
                  setEnabled(false);
                  return;
               case FINISH_WAIT:
                  if (var12 < stageUntilMs) {
                     return;
                  }

                  log("[Parser] Парсинг завершён, открываю /ah для переключения на «сначала новые»");
                  CommandSender.sendCommand(var0.player, "/ah");
                  stage = HolyWorldPriceParser.Stage.RESTORE_SORTING;
                  sortingClickCount = 0;
                  stageUntilMs = var12 + 500L;
            }
         } else {
            long var4 = System.currentTimeMillis();
            if (var4 - lastEmptyItemsNotifyMs > 2000L) {
               lastEmptyItemsNotifyMs = var4;
            }

            setEnabled(false);
         }
      } else {
         log("[Parser] tickAuction: mc или player null");
      }
   }

   private static void log(String var0) {
   }

   private static String getSearchQuery(AutoBuyableItem var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = var0.getSearchName();
      if (var1 == null || var1.isEmpty()) {
         var1 = var0.getDisplayName();
      }

      return cleanSearchName(var1);
   }

   private static int findCheapestPerItem(List<Slot> var0, AutoBuyableItem var1) {
      if (var0 != null && var1 != null) {
         ItemStack var2 = var1.createItemStack();
         if (var2 != null && !var2.isEmpty()) {
            NbtComponent var3 = (NbtComponent)var2.get(DataComponentTypes.CUSTOM_DATA);
            boolean var4 = false;
            if (var3 != null) {
               NbtCompound var5 = var3.copyNbt();
               if (var5 != null && var5.getBoolean("HolyWorldSphere")) {
                  var4 = true;
               }
            }

            Item var26 = var1.getItem();
            boolean var6 = var1.needsAdditionalCheck();
            log(
               "[Parser] findCheapestPerItem: ищу предмет типа "
                  + var26
                  + ", displayName='"
                  + var1.getDisplayName()
                  + "', needsCheck="
                  + var6
                  + ", targetHasSphereFlag="
                  + var4
            );
            int var7 = Integer.MAX_VALUE;
            int var8 = 0;
            int var9 = 0;
            int var10 = 0;

            for (int var11 = 0; var11 <= 44 && var11 < var0.size() && var10 < 5; var11++) {
               Slot var12 = (Slot)var0.get(var11);
               if (var12 != null && !var12.getStack().isEmpty()) {
                  ItemStack var13 = var12.getStack();
                  log("[Parser] findCheapestPerItem: слот " + var11 + ": тип=" + var13.getItem() + ", название='" + var13.getName().getString() + "'");
                  var10++;
               }
            }

            for (int var27 = 0; var27 <= 44 && var27 < var0.size(); var27++) {
               Slot var29 = (Slot)var0.get(var27);
               if (var29 != null && !var29.getStack().isEmpty()) {
                  var8++;
                  ItemStack var31 = var29.getStack();
                  if (!AuctionUtils.isArmorItem(var31) || !AuctionUtils.hasThornsEnchantment(var31)) {
                     NbtComponent var14 = (NbtComponent)var2.get(DataComponentTypes.CUSTOM_DATA);
                     boolean var15 = false;
                     if (var14 != null) {
                        NbtCompound var16 = var14.copyNbt();
                        if (var16 != null && var16.getBoolean("HolyWorldSphere")) {
                           var15 = true;
                           log("[Parser] findCheapestPerItem: targetStack - это сфера HolyWorld");
                        }
                     }

                     NbtComponent var33 = (NbtComponent)var31.get(DataComponentTypes.CUSTOM_DATA);
                     boolean var17 = false;
                     if (var33 != null) {
                        NbtCompound var18 = var33.copyNbt();
                        if (var18 != null && var18.getBoolean("HolyWorldSphere")) {
                           var17 = true;
                        }
                     }

                     boolean var35 = var31.getItem() == var26;
                     if (!var35) {
                        if (!var15 || var31.getItem() != Items.END_CRYSTAL && var31.getItem() != Items.PLAYER_HEAD) {
                           continue;
                        }

                        var35 = true;
                        log("[Parser] findCheapestPerItem: тип не совпадает, но это сфера HolyWorld, продолжаем");
                     }

                     boolean var19 = false;
                     String var20 = var31.getName().getString();
                     String var21 = var1.getDisplayName();
                     if (var15) {
                        log("[Parser] findCheapestPerItem: проверяю сферу в слоте " + var27 + ": '" + var20 + "' vs ожидаемая '" + var21 + "'");
                        var19 = AuctionUtils.compareItem(var31, var2);
                        if (!var19) {
                           log("[Parser] findCheapestPerItem: сфера НЕ совпала в слоте " + var27 + ": '" + var20 + "'");
                           if (var33 != null) {
                              NbtCompound var22 = var33.copyNbt();
                              if (var22 != null) {
                                 log("[Parser] findCheapestPerItem: NBT stack: " + var22.toString());
                                 if (var22.contains("AttributeModifiers", 9)) {
                                    NbtList var23 = var22.getList("AttributeModifiers", 9);
                                    log("[Parser] findCheapestPerItem: NBT AttributeModifiers count=" + var23.size());
                                 }

                                 if (var22.contains("sphereEffect", 10)) {
                                    NbtCompound var41 = var22.getCompound("sphereEffect");
                                    log("[Parser] findCheapestPerItem: sphereEffect: " + var41.toString());
                                 }
                              }
                           }

                           AttributeModifiersComponent var38 = (AttributeModifiersComponent)var31.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
                           if (var38 != null) {
                              log("[Parser] findCheapestPerItem: ATTRIBUTE_MODIFIERS component: " + var38.toString());
                              log("[Parser] findCheapestPerItem: modifiers count=" + var38.modifiers().size());
                           }

                           if (var14 != null) {
                              NbtCompound var42 = var14.copyNbt();
                              if (var42 != null) {
                                 log("[Parser] findCheapestPerItem: NBT target: " + var42.toString());
                                 if (var42.contains("AttributeModifiers", 9)) {
                                    NbtList var24 = var42.getList("AttributeModifiers", 9);
                                    log("[Parser] findCheapestPerItem: NBT target AttributeModifiers count=" + var24.size());
                                 }
                              }
                           }
                        } else {
                           log("[Parser] findCheapestPerItem: сфера СОВПАЛА в слоте " + var27 + ": '" + var20 + "'");
                        }
                     } else if (!var6) {
                        var19 = true;
                     } else {
                        var19 = AuctionUtils.compareItem(var31, var2);
                        if (!var19) {
                           boolean var39 = false;
                           if (var14 != null) {
                              NbtCompound var43 = var14.copyNbt();
                              if (var43 != null && var43.getBoolean("HolyWorldKringeEffect")) {
                                 var39 = true;
                              }
                           }

                           if (var31.getItem() == Items.TOTEM_OF_UNDYING && var26 == Items.TOTEM_OF_UNDYING) {
                              if (var33 != null) {
                                 NbtCompound var44 = var33.copyNbt();
                                 if (var44 != null) {
                                 }
                              }

                              if (var14 != null) {
                                 NbtCompound var45 = var14.copyNbt();
                                 if (var45 != null) {
                                 }
                              }
                           }

                           NbtComponent var46 = (NbtComponent)var31.get(DataComponentTypes.CUSTOM_DATA);
                           if (var46 != null) {
                              NbtCompound var48 = var46.copyNbt();
                              if (var48 != null && var48.getBoolean("HolyWorldTalisman")) {
                              }
                           }

                           if (var39 && var33 != null) {
                              NbtCompound var49 = var33.copyNbt();
                              if (var49 != null && var49.contains("kringeEffect", 10)) {
                                 NbtCompound var25 = var49.getCompound("kringeEffect");
                              }
                           }
                        } else {
                           log("[Parser] findCheapestPerItem: предмет СОВПАЛ в слоте " + var27 + ": '" + var20 + "'");
                        }
                     }

                     if (var19) {
                        var9++;
                        log("[Parser] findCheapestPerItem: найдено совпадение в слоте " + var27 + ": '" + var20 + "'");
                        int var40 = getHolyWorldPrice(var31, var0, var27);
                        if (var40 <= 0) {
                           log("[Parser] findCheapestPerItem: цена не найдена для '" + var20 + "'");
                        } else {
                           int var47 = var31.getCount();
                           if (var47 <= 0) {
                              var47 = 1;
                           }

                           int var50 = var40 / var47;
                           log("[Parser] findCheapestPerItem: '" + var20 + "' - цена: " + var40 + ", количество: " + var47 + ", за штуку: " + var50);
                           if (var50 > 0 && var50 < var7) {
                              var7 = var50;
                              log("[Parser] findCheapestPerItem: новая лучшая цена: " + var7);
                           }
                        }
                     }
                  }
               }
            }

            if (var9 == 0 && var8 > 0) {
               log("[Parser] findCheapestPerItem: не найдено совпадений. Все предметы в слотах:");

               for (int var28 = 0; var28 <= 44 && var28 < var0.size(); var28++) {
                  Slot var30 = (Slot)var0.get(var28);
                  if (var30 != null && !var30.getStack().isEmpty()) {
                     ItemStack var32 = var30.getStack();
                     log("[Parser]   Слот " + var28 + ": тип=" + var32.getItem() + ", название='" + var32.getName().getString() + "'");
                  }
               }
            }

            return var7 == Integer.MAX_VALUE ? -1 : var7;
         } else {
            log("[Parser] findCheapestPerItem: не удалось создать ItemStack для " + var1.getDisplayName());
            return -1;
         }
      } else {
         log("[Parser] findCheapestPerItem: slots или expectedItem null");
         return -1;
      }
   }

   private static List<AutoBuyableItem> resolveItemsToParse() {
      ArrayList var11 = new ArrayList();

      try {
         for (AutoBuyableItem var15 : ItemRegistry.getHolyWorld()) {
            if (var15 != null && var15.isEnabled()) {
               var11.add(var15);
            }
         }
      } catch (Exception var8) {
      }

      if (!var11.isEmpty()) {
         log("[Parser] Items source=HOLYWORLD_ENABLED size=" + var11.size());

         for (int var14 = 0; var14 < Math.min(30, var11.size()); var14++) {
            log("[Parser]   Item " + var14 + ": " + ((AutoBuyableItem)var11.get(var14)).getDisplayName());
         }
      } else {
         log("[Parser] Items source=HOLYWORLD_ENABLED size=0");
      }

      return var11;
   }

   private static int getHolyWorldPrice(ItemStack var0, List<Slot> var1, int var2) {
      int var3 = AuctionUtils.getPrice(var0);
      if (var3 > 0) {
         log("[Parser] getHolyWorldPrice: цена найдена через AuctionUtils.getPrice: " + var3);
         return var3;
      }

      LoreComponent var4 = (LoreComponent)var0.get(DataComponentTypes.LORE);
      if (var4 != null && !var4.lines().isEmpty()) {
         for (Text var6 : var4.lines()) {
            String var7 = var6.getString();
            if (var7.contains("Цена:") || var7.contains("Price:")) {
               try {
                  int var8 = Math.max(var7.indexOf("Цена:"), var7.indexOf("Price:"));
                  if (var8 != -1) {
                     String var9 = var7.substring(var8);
                     String var10 = var9.replaceAll("[^0-9.]", "").trim();

                     try {
                        var3 = (int)Double.parseDouble(var10);
                        if (var3 > 0) {
                           log("[Parser] getHolyWorldPrice: цена найдена через SpookyTime-стиль (Цена/Price): " + var3);
                           return var3;
                        }
                     } catch (NumberFormatException var15) {
                        try {
                           var3 = Integer.parseInt(var10);
                           if (var3 > 0) {
                              log("[Parser] getHolyWorldPrice: цена найдена через SpookyTime-стиль (Цена/Price): " + var3);
                              return var3;
                           }
                        } catch (NumberFormatException var14) {
                        }
                     }
                  }
               } catch (Exception var16) {
               }
            }
         }
      }

      if (var4 != null && !var4.lines().isEmpty()) {
         for (Text var21 : var4.lines()) {
            String var22 = var21.getString();
            Matcher var23 = HOLYWORLD_PRICE_PATTERN.matcher(var22);
            if (var23.find()) {
               String var24 = null;

               for (int var26 = 1; var26 <= 3; var26++) {
                  if (var23.group(var26) != null) {
                     var24 = var23.group(var26);
                     break;
                  }
               }

               if (var24 != null) {
                  try {
                     var24 = var24.replaceAll("[\\s,.]", "");
                     var3 = Integer.parseInt(var24);
                     if (var3 > 0) {
                        log("[Parser] getHolyWorldPrice: цена найдена через HOLYWORLD_PRICE_PATTERN из лора: " + var3);
                        return var3;
                     }
                  } catch (NumberFormatException var13) {
                  }
               }
            }
         }
      }

      log("[Parser] getHolyWorldPrice: цена не найдена");
      return -1;
   }

   private static String cleanSearchName(String var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = var0.replace("[★] ", "").replace("[★]", "").trim();
      Matcher var2 = LEVEL_PATTERN.matcher(var1);
      if (var2.find()) {
         String var3 = var2.group(1).trim();
         String var4 = var2.group(2);
         var3 = var3.replace(" опыта", "").replace(" Опыта", "").trim();
         var1 = var3 + " с уровнем " + var4;
      }

      return var1.trim();
   }

   private static String formatPrice(int var0) {
      StringBuilder var1 = new StringBuilder();
      String var2 = String.valueOf(var0);
      int var3 = 0;

      for (int var4 = var2.length() - 1; var4 >= 0; var4--) {
         if (var3 > 0 && var3 % 3 == 0) {
            var1.insert(0, '.');
         }

         var1.insert(0, var2.charAt(var4));
         var3++;
      }

      return var1.toString();
   }

   private static boolean checkSortingState(List<Slot> var0, int var1, String var2, boolean var3) {
      if (var0 != null && var1 >= 0 && var1 < var0.size()) {
         Slot var4 = (Slot)var0.get(var1);
         if (var4 != null && !var4.getStack().isEmpty()) {
            ItemStack var5 = var4.getStack();
            LoreComponent var6 = (LoreComponent)var5.get(DataComponentTypes.LORE);
            if (var6 != null && !var6.lines().isEmpty()) {
               for (Text var8 : var6.lines()) {
                  String var9 = var8.getString().toLowerCase();
                  if (var9.contains(var2.toLowerCase())) {
                     if (!var3) {
                        return true;
                     }

                     String var10 = var8.toString();
                     if (var10.contains("gold") || var10.contains("§6") || var10.contains("GOLD")) {
                        log("[Parser] checkSortingState: найден текст '" + var2 + "' с GOLD цветом");
                        return true;
                     }
                  }
               }
            }

            String var11 = var5.getName().getString().toLowerCase();
            if (var11.contains(var2.toLowerCase())) {
               if (!var3) {
                  return true;
               }

               String var12 = var5.getName().toString();
               if (var12.contains("gold") || var12.contains("§6") || var12.contains("GOLD")) {
                  log("[Parser] checkSortingState: найден текст в названии '" + var2 + "' с GOLD цветом");
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean hasItemsInSlots(List<Slot> var0) {
      if (var0 == null) {
         return false;
      }

      for (int var1 = 0; var1 <= 44 && var1 < var0.size(); var1++) {
         Slot var2 = (Slot)var0.get(var1);
         if (var2 != null && !var2.getStack().isEmpty()) {
            ItemStack var3 = var2.getStack();
            String var4 = var3.getName().getString();
            if (!var4.contains("Страница") && !var4.contains("Обновить") && !var4.contains("Сортировка") && !var4.contains("Назад")) {
               return true;
            }
         }
      }

      return false;
   }

   private enum Stage {
      IDLE,
      OPEN_AH,
      SETUP_SORTING,
      SEND_SEARCH,
      WAIT_RESULTS,
      CHECK_NEXT_PAGE,
      WAIT_BETWEEN,
      RESTORE_SORTING,
      FINISH_WAIT;
   }
}
