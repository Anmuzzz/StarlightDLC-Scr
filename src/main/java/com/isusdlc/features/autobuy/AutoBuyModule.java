package com.isusdlc.features.autobuy;

import com.isusdlc.display.screens.autobuy.AutoBuyScreen;
import com.isusdlc.display.screens.autobuy.history.HistoryRenderer;
import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.manager.AutoBuyManager;
import com.isusdlc.features.autobuy.originalitems.ItemRegistry;
import com.isusdlc.features.autobuy.holyworld.HolyWorldAuctionHandler;
import com.isusdlc.features.autobuy.parser.FunTimePriceParser;
import com.isusdlc.features.autobuy.parser.HolyWorldPriceParser;
import com.isusdlc.features.autobuy.parser.SpookyTimePriceParser;
import com.isusdlc.features.autobuy.spookytime.SpookyTimeAuctionHandler;
import com.isusdlc.systems.event.impl.network.ReceivePacketEvent;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.event.impl.render.ScreenRenderEvent;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.event.impl.window.MouseScrollEvent;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.systems.setting.SettingsContainer;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.ServerInfo.ServerType;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

public class AutoBuyModule implements SettingsContainer {
   protected final List<Setting> settings = new ArrayList<>();

   public List<Setting> getSettings() {
      return this.settings;
   }

   private static AutoBuyModule instance;
   private final MinecraftClient mc = MinecraftClient.getInstance();
   private final ModeSetting serverMode = new ModeSetting(this, "Сервер", "", () -> false);
   private final ModeSetting.Value serverModeFunTime = new ModeSetting.Value(this.serverMode, "FunTime").select();
   private final ModeSetting.Value serverModeSpookyTime = new ModeSetting.Value(this.serverMode, "SpookyTime");
   private final ModeSetting.Value serverModeHolyWorld = new ModeSetting.Value(this.serverMode, "HolyWorld");
   private final ModeSetting leaveType = new ModeSetting(this, "Тип обхода", "", () -> !this.serverMode.getValue().getName().equals("FunTime"));
   private final ModeSetting.Value leaveTypeBuyer = new ModeSetting.Value(this.leaveType, "Покупающий").select();
   private final ModeSetting.Value leaveTypeChecker = new ModeSetting.Value(this.leaveType, "Проверяющий");
   private final BindSetting autobuyGui = new BindSetting(this, "Открыть гуи");
   private final SliderSetting timer2 = new SliderSetting(this, "Таймер", "", () -> !this.serverMode.getValue().getName().equals("FunTime")).min(350.0F).max(750.0F).step(1.0F).currentValue(350.0F);
   private final ModeSetting versionSetting = new ModeSetting(this, "Версия", "", () -> !this.serverMode.getValue().getName().equals("FunTime"));
   private final ModeSetting.Value version165 = new ModeSetting.Value(this.versionSetting, "1.16.5").select();
   private final ModeSetting.Value version214 = new ModeSetting.Value(this.versionSetting, "1.21.4");
   private final BooleanSetting autoStorage = new BooleanSetting(this, "Автоскладирование", "", () -> this.serverMode.getValue().getName().equals("HolyWorld"));
   private final AutoBuyManager autoBuyManager = AutoBuyManager.getInstance();
   private final NetworkManager networkManager;
   private final ServerManager serverManager;
   private final StorageManager storageManager;
   private final AuctionHandler auctionHandler;
   private final HolyWorldAuctionHandler holyWorldAuctionHandler;
   private final SpookyTimeAuctionHandler spookyTimeAuctionHandler;
   private final AfkHandler afkHandler;
   private final Random random = new Random();
   private final TimerUtil openTimer = TimerUtil.create();
   private final TimerUtil updateTimer = TimerUtil.create();
   private final TimerUtil buyTimer = TimerUtil.create();
   private final TimerUtil switchTimer = TimerUtil.create();
   private final TimerUtil enterDelayTimer = TimerUtil.create();
   private final TimerUtil ahSpamTimer = TimerUtil.create();
   private final TimerUtil connectionCheckTimer = TimerUtil.create();
   private final TimerUtil auctionRequestTimer = TimerUtil.create();
   private final TimerUtil reconnectTimer = TimerUtil.create();
   private boolean open = false;
   private boolean serverInAuction = false;
   public static final long[] GOTO_DELAYS_MS = new long[]{120000L, 240000L, 180000L, 240000L};
   private static final double[][] GOTO_WAYPOINTS = new double[][]{{-11.0, 71.0, -46.0}, {23.0, 69.0, -25.0}, {-8.0, 69.0, 24.0}, {50.0, 69.0, 57.0}};
   private static final double GOTO_ARRIVAL_RADIUS = 2.0;
   private static final long GOTO_AFTER_ARRIVAL_MS = 1000L;
   private final TimerUtil gotoTimer = TimerUtil.create();
   private int gotoStepIndex = 0;
   private boolean waitingForGotoArrival = false;
   private long gotoArrivedAtMs = 0L;
   private double gotoTargetX;
   private double gotoTargetY;
   private double gotoTargetZ;
   private boolean pendingGotoAfterClose = false;
   private long lastGotoRemainingLogMs = 0L;
   private boolean justEntered = false;
   private boolean spammingAh = false;
   private boolean waitingForAuctionOpen = false;
   private boolean waitingForReconnect = false;
   private String lastServerAddress = "mc.funtime.su";
   private final List<AutoBuyableItem> cachedEnabledItems = new ArrayList<>();
   private long randomizedTimerValue = 350L;

   public AutoBuyModule() {
      instance = this;
      this.networkManager = new NetworkManager();
      this.serverManager = new ServerManager(this.versionSetting);
      this.storageManager = new StorageManager(this.autoStorage);
      this.auctionHandler = new AuctionHandler(this.autoBuyManager);
      this.holyWorldAuctionHandler = new HolyWorldAuctionHandler(this.autoBuyManager);
      this.spookyTimeAuctionHandler = new SpookyTimeAuctionHandler(this.autoBuyManager);
      this.afkHandler = new AfkHandler();
   }

   public static AutoBuyModule getInstance() {
      return instance;
   }

   public NetworkManager getNetworkManager() {
      return this.networkManager;
   }

   public boolean isBuyerMode() {
      return this.leaveTypeBuyer.isSelected();
   }

   public AutoBuyServerMode getServerMode() {
      if (this.serverMode == null) {
         return AutoBuyServerMode.FUNTIME;
      } else {
         String var1 = this.serverMode.getValue().getName();
         if ("SpookyTime".equals(var1)) {
            return AutoBuyServerMode.SPOOKYTIME;
         } else {
            return "HolyWorld".equals(var1) ? AutoBuyServerMode.HOLYWORLD : AutoBuyServerMode.FUNTIME;
         }
      }
   }

   public boolean isFunTimeMode() {
      return this.getServerMode() == AutoBuyServerMode.FUNTIME;
   }

   public boolean isSpookyTimeMode() {
      return this.getServerMode() == AutoBuyServerMode.SPOOKYTIME;
   }

   public boolean isHolyWorldMode() {
      return this.getServerMode() == AutoBuyServerMode.HOLYWORLD;
   }

   private boolean isFunTimeServer() {
      return this.lastServerAddress != null && this.lastServerAddress.toLowerCase().contains("funtime");
   }

   private boolean isCopyTimeServer() {
      return this.lastServerAddress != null && this.lastServerAddress.toLowerCase().contains("copytime");
   }

   private boolean isSpookyTimeServer() {
      return this.lastServerAddress != null && this.lastServerAddress.toLowerCase().contains("spookytime");
   }

   public void onEnable() {
      this.resetTimers();
      this.resetState();
      if (this.leaveTypeBuyer.isSelected() && (this.version165.isSelected() || this.version214.isSelected())) {
         mc.options.pauseOnLostFocus = false;
      }

      if (mc.getCurrentServerEntry() != null) {
         this.lastServerAddress = mc.getCurrentServerEntry().address;
      }

      this.cacheEnabledItems();
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         this.networkManager.start(this.leaveType.getValue().getName());
      }
   }

   public void onDisable() {
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         this.networkManager.stop();
      }

      this.serverManager.reset();
      this.storageManager.reset();
      this.afkHandler.resetMovementKeys(mc.options);
   }

   private void resetTimers() {
      this.openTimer.resetCounter();
      this.updateTimer.resetCounter();
      this.buyTimer.resetCounter();
      this.switchTimer.resetCounter();
      this.enterDelayTimer.resetCounter();
      this.ahSpamTimer.resetCounter();
      this.connectionCheckTimer.resetCounter();
      this.auctionRequestTimer.resetCounter();
      this.reconnectTimer.resetCounter();
      this.gotoTimer.resetCounter();
      this.serverManager.resetTimers();
      this.storageManager.resetTimers();
      this.afkHandler.resetTimers();
   }

   private void resetState() {
      this.open = false;
      this.serverInAuction = false;
      this.justEntered = false;
      this.spammingAh = false;
      this.waitingForAuctionOpen = false;
      this.waitingForReconnect = false;
      this.waitingForGotoArrival = false;
      this.gotoArrivedAtMs = 0L;
      this.pendingGotoAfterClose = false;
      this.cachedEnabledItems.clear();
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         this.networkManager.clearQueues();
      }

      this.auctionHandler.clear();
      this.holyWorldAuctionHandler.clear();
      this.spookyTimeAuctionHandler.clear();
   }

   private void generateRandomizedTimer() {
      float var1 = this.timer2.getCurrentValue();
      float var2 = 0.85F + (float)(Math.random() * 0.3F);
      this.randomizedTimerValue = (long)(var1 * var2);
   }

   private void cacheEnabledItems() {
      this.cachedEnabledItems.clear();
      AutoBuyServerMode var2 = this.getServerMode();

      for (AutoBuyableItem var4 : switch (var2) {
         default -> ItemRegistry.getFunTimeItems();
         case SPOOKYTIME -> ItemRegistry.getSpookyTime();
         case HOLYWORLD -> ItemRegistry.getHolyWorld();
      }) {
         if (var4 != null && var4.isEnabled()) {
            this.cachedEnabledItems.add(var4);
         }
      }
   }

   public void onKey(KeyPressEvent event) {
      if (event.getKey() == this.autobuyGui.getKey() && event.getAction() == 1) {
         AutoBuyScreen.INSTANCE.openGui();
      }
   }

   public void onHandledScreen(ScreenRenderEvent event) {
      HistoryRenderer.getInstance().render(event.getContext(), (int) mc.mouse.getX(), (int) mc.mouse.getY(), event.getTickDelta());
   }

   public void onGuiScroll(MouseScrollEvent event) {
      if (!(mc.currentScreen instanceof ChatScreen)) {
         double mouseX = mc.mouse.getX() / mc.getWindow().getScaleFactor();
         double mouseY = mc.mouse.getY() / mc.getWindow().getScaleFactor();
         HistoryRenderer.getInstance().mouseScrolled(mouseX, mouseY, event.getVerticalAmount());
      }
   }

   public void onPacket(ReceivePacketEvent event) {
      if (event.getPacket() instanceof GameMessageS2CPacket var2) {
         Text var6 = var2.content();
         String var4 = var6.getString();
         if (var4.contains("Вы уже подключены к этому серверу!")) {
            if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
               this.serverManager.switchToNextServer(mc.player);
            }

            return;
         }

         if (this.leaveTypeBuyer.isSelected() || this.isHolyWorldMode() || this.isSpookyTimeMode()) {
            PurchaseHandler.handlePurchaseMessage(var4, this.auctionHandler, this.holyWorldAuctionHandler, this.spookyTimeAuctionHandler);
         }
      }

      if (event.getPacket() instanceof DisconnectS2CPacket var5 && this.leaveTypeChecker.isSelected()) {
         String var8 = var5.reason().getString();
         this.waitingForReconnect = true;
         this.reconnectTimer.resetCounter();
      }
   }

   private void tickGotoCycle() {
      if (this.isHolyWorldMode() && this.autoBuyManager.isEnabled()) {
         if (mc.player != null) {
            if (this.pendingGotoAfterClose) {
               if (!(mc.currentScreen instanceof GenericContainerScreen)) {
                  double[] var10 = GOTO_WAYPOINTS[this.gotoStepIndex];
                  this.gotoTargetX = var10[0];
                  this.gotoTargetY = var10[1];
                  this.gotoTargetZ = var10[2];
                  String var2 = "#goto " + (int)this.gotoTargetX + " " + (int)this.gotoTargetY + " " + (int)this.gotoTargetZ;
                  mc.getNetworkHandler().sendChatMessage(var2);
                  this.waitingForGotoArrival = true;
                  this.gotoArrivedAtMs = 0L;
                  this.gotoTimer.resetCounter();
                  this.pendingGotoAfterClose = false;
               }
            } else if (this.waitingForGotoArrival) {
               double var9 = mc.player.getX() - this.gotoTargetX;
               double var11 = mc.player.getY() - this.gotoTargetY;
               double var12 = mc.player.getZ() - this.gotoTargetZ;
               double var14 = Math.sqrt(var9 * var9 + var11 * var11 + var12 * var12);
               if (var14 <= 2.0) {
                  if (this.gotoArrivedAtMs == 0L) {
                     this.gotoArrivedAtMs = System.currentTimeMillis();
                  }

                  if (System.currentTimeMillis() - this.gotoArrivedAtMs >= 1000L) {
                     this.waitingForGotoArrival = false;
                     this.gotoArrivedAtMs = 0L;
                     CommandSender.sendCommand(mc.player, "/ah");
                     this.gotoStepIndex = (this.gotoStepIndex + 1) % 4;
                     this.gotoTimer.resetCounter();
                  }
               } else {
                  this.gotoArrivedAtMs = 0L;
               }
            } else {
               long var1 = GOTO_DELAYS_MS[this.gotoStepIndex];
               long var3 = this.gotoTimer.getTime();
               long var5 = var1 - var3;
               if (!this.gotoTimer.hasTimeElapsed(var1)) {
                  long var13 = System.currentTimeMillis();
                  if (var13 - this.lastGotoRemainingLogMs >= 30000L) {
                     System.out.println(
                        String.format("[AutoBuy #goto] до след. #goto: %d мс (шаг %d, delay=%d мс, прошло=%d мс)", var5 / 1000L, this.gotoStepIndex, var1, var3)
                     );
                     this.lastGotoRemainingLogMs = var13;
                  }
               } else {
                  if (mc.currentScreen instanceof GenericContainerScreen) {
                     mc.player.closeHandledScreen();
                     this.pendingGotoAfterClose = true;
                  } else {
                     double[] var7 = GOTO_WAYPOINTS[this.gotoStepIndex];
                     this.gotoTargetX = var7[0];
                     this.gotoTargetY = var7[1];
                     this.gotoTargetZ = var7[2];
                     String var8 = "#goto " + (int)this.gotoTargetX + " " + (int)this.gotoTargetY + " " + (int)this.gotoTargetZ;
                     mc.getNetworkHandler().sendChatMessage(var8);
                     this.waitingForGotoArrival = true;
                     this.gotoArrivedAtMs = 0L;
                  }

                  this.gotoTimer.resetCounter();
               }
            }
         }
      }
   }

   public void onTick(ClientPlayerTickEvent event) {
      if (this.leaveTypeChecker.isSelected()) {
         this.handleCheckerReconnect();
      }

      if (mc.player != null && mc.world != null) {
         this.tickGotoCycle();
         boolean var2 = HolyWorldPriceParser.isEnabled() && this.getServerMode() == AutoBuyServerMode.HOLYWORLD;
         boolean var3 = SpookyTimePriceParser.isEnabled() && this.getServerMode() == AutoBuyServerMode.SPOOKYTIME;
         boolean var4 = FunTimePriceParser.isEnabled() && this.leaveTypeBuyer.isSelected();
         if (this.autoBuyManager.isEnabled() || var4 || var2 || var3) {
            if (this.autoBuyManager.isEnabled()) {
               this.handleConnectionStatus();
               this.afkHandler.handle(mc);
               this.storageManager.handle(mc, this.open);
               if (this.storageManager.isActive() && !var4) {
                  return;
               }

               if (this.storageManager.handlePostStorage(mc, this.enterDelayTimer, this.ahSpamTimer)) {
                  this.justEntered = true;
               }

               boolean var5 = this.serverManager.isInHub();
               this.serverManager.updateHubStatus(mc.world);
               if (this.getServerMode() == AutoBuyServerMode.FUNTIME && this.serverManager.shouldJoinAnarchy() && !FunTimePriceParser.isEnabled()) {
                  this.serverManager.joinAnarchyFromHub(mc.player);
               }

               if (var5 && !this.serverManager.isInHub()) {
                  this.handleServerSwitch();
               }

               if ((this.serverManager.isWaitingForServerLoad() || ServerSwitchHandler.isWaitingForServerLoad())
                  && (ServerSwitchHandler.hasTimedOut() || !var5 && !this.serverManager.isInHub())) {
                  this.serverManager.setWaitingForServerLoad(false);
                  ServerSwitchHandler.setWaitingForServerLoad(false);
                  this.handleServerSwitch();
               }

               this.handleAhSpam();
            }

            if (var2 && !(mc.currentScreen instanceof GenericContainerScreen)) {
               HolyWorldPriceParser.tickNoContainer(mc);
            }

            if (var3 && !(mc.currentScreen instanceof GenericContainerScreen)) {
               SpookyTimePriceParser.tickNoContainer(mc);
            }

            if (this.getServerMode() == AutoBuyServerMode.HOLYWORLD
               && this.autoBuyManager.isEnabled()
               && !(mc.currentScreen instanceof GenericContainerScreen)) {
               this.holyWorldAuctionHandler.tickReconnect();
            }

            this.handleAuction();
            if (var4 && !(mc.currentScreen instanceof GenericContainerScreen)) {
               FunTimePriceParser.tickNoContainer(mc);
            }

            this.handleServerAutoSwitch();
            this.handleCheckerAuctionRequest();
         }
      }
   }

   private void handleCheckerReconnect() {
      if (mc.currentScreen instanceof DisconnectedScreen && !this.waitingForReconnect) {
         this.waitingForReconnect = true;
         this.reconnectTimer.resetCounter();
      }

      if (this.waitingForReconnect
         && this.reconnectTimer.hasTimeElapsed(5000L)
         && (mc.currentScreen instanceof DisconnectedScreen || mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof TitleScreen)) {
         this.reconnectToServer();
         this.reconnectTimer.resetCounter();
      }

      if (this.waitingForReconnect && mc.player != null && mc.world != null) {
         this.waitingForReconnect = false;
      }
   }

   private void reconnectToServer() {
      try {
         mc.setScreen(new MultiplayerScreen(new TitleScreen()));
      } catch (Exception var3) {
      }
   }

   private void handleConnectionStatus() {
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         if (this.leaveTypeChecker.isSelected() && this.connectionCheckTimer.hasTimeElapsed(5000L)) {
            if (!this.networkManager.isConnectedToServer()) {
               this.networkManager.start(this.leaveType.getValue().getName());
            }

            this.connectionCheckTimer.resetCounter();
         }
      }
   }

   private void handleServerSwitch() {
      this.justEntered = true;
      this.enterDelayTimer.resetCounter();
      this.switchTimer.resetCounter();
      this.storageManager.resetMaxShulkers();
      this.waitingForAuctionOpen = false;
      this.auctionRequestTimer.resetCounter();
   }

   private void handleAhSpam() {
      if (this.getServerMode() == AutoBuyServerMode.HOLYWORLD) {
         if (this.spammingAh) {
            this.spammingAh = false;
         }
      } else if (this.leaveTypeBuyer.isSelected() && (this.version165.isSelected() || this.version214.isSelected())) {
         if (this.justEntered && this.enterDelayTimer.hasTimeElapsed(2000L) && !this.spammingAh) {
            this.spammingAh = true;
            this.ahSpamTimer.resetCounter();
         }

         if (this.spammingAh && !this.afkHandler.isPerformingAction() && this.ahSpamTimer.hasTimeElapsed(5000L)) {
            if (mc.player != null && mc.player.networkHandler != null) {
            }

            this.ahSpamTimer.resetCounter();
         }
      }
   }

   private void handleCheckerAuctionRequest() {
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         if (this.leaveTypeChecker.isSelected()) {
            if (!this.open && !this.waitingForAuctionOpen && this.auctionRequestTimer.hasTimeElapsed(3000L) && this.networkManager.isConnectedToServer()) {
               CommandSender.openAuction();
               this.waitingForAuctionOpen = true;
               this.auctionRequestTimer.resetCounter();
            }

            if (this.waitingForAuctionOpen && this.auctionRequestTimer.hasTimeElapsed(5000L)) {
               this.waitingForAuctionOpen = false;
               this.auctionRequestTimer.resetCounter();
            }
         }
      }
   }

   private void handleAuction() {
      this.handleCheckerAuctionRequest();
      if (mc.currentScreen instanceof GenericContainerScreen var1) {
         String var10 = var1.getTitle().getString();
         int var3 = ((GenericContainerScreenHandler)var1.getScreenHandler()).syncId;
         DefaultedList var4 = ((GenericContainerScreenHandler)var1.getScreenHandler()).slots;
         if (!var10.contains("Аукцион") && !var10.contains("Аукционы") && !var10.contains("Поиск")) {
            if (!var10.contains("Подозрительная цена") && !var10.contains("Подтверждение") && !var10.contains("Confirm") && !var10.contains("Покупка предмета")
            ) {
               this.exitAuction();
            } else {
               if (this.isHolyWorldMode()) {
                  this.holyWorldAuctionHandler.handleConfirmation(var3, var4, var10);
                  long var11 = this.holyWorldAuctionHandler.getRefreshDelayMs();
                  long var7 = 0L;
                  this.holyWorldAuctionHandler.tick(var3, var4, this.cachedEnabledItems, var11, var7);
               } else {
                  this.auctionHandler.handleSuspiciousPrice(mc, var3, var4);
               }

               this.openTimer.resetCounter();
               this.buyTimer.resetCounter();
            }
         } else {
            if (!this.open) {
               this.enterAuction();
               return;
            }

            this.storageManager.handleAuctionEnter();
            AutoBuyServerMode var5 = this.getServerMode();
            if (var5 == AutoBuyServerMode.SPOOKYTIME) {
               if (SpookyTimePriceParser.isEnabled()) {
                  SpookyTimePriceParser.tickAuction(mc, var4);
                  return;
               }

               if (this.autoBuyManager.isEnabled()) {
                  this.handleSpookyTimeMode(var3, var4);
               }

               return;
            }

            if (var5 == AutoBuyServerMode.HOLYWORLD) {
               if (HolyWorldPriceParser.isEnabled()) {
                  HolyWorldPriceParser.tickAuction(mc, var4);
                  return;
               }

               this.handleHolyWorldMode(var3, var4);
               return;
            }

            if (var5 == AutoBuyServerMode.FUNTIME
               && (isFunTimeServer() || isCopyTimeServer() && !isSpookyTimeServer())
               && FunTimePriceParser.isEnabled()) {
               FunTimePriceParser.tickAuction(mc, var4);
               return;
            }

            if (this.leaveTypeBuyer.isSelected()) {
               this.handleBuyerMode(var1, var3, var4);
            } else if (this.leaveTypeChecker.isSelected()) {
               if (!FunTimePriceParser.isEnabled() || !isFunTimeServer() && (!isCopyTimeServer() || isSpookyTimeServer())) {
                  try {
                     this.handleCheckerMode(var4);
                  } catch (Exception var9) {
                     var9.printStackTrace();
                  }
               } else {
                  this.handleCheckerParserMode(var3, var4, var10);
               }
            }
         }
      } else {
         this.exitAuction();
      }
   }

   private void enterAuction() {
      this.open = true;
      this.openTimer.resetCounter();
      this.updateTimer.resetCounter();
      this.buyTimer.resetCounter();
      this.generateRandomizedTimer();
      this.storageManager.notifyAuctionEnter();
      this.serverInAuction = true;
      this.auctionHandler.clear();
      this.justEntered = false;
      this.spammingAh = false;
      this.waitingForAuctionOpen = false;
      this.storageManager.clearStorageCompleted();
      if (!this.storageManager.getPostStorageTimer().hasTimeElapsed(2000L)) {
         this.storageManager.disableStartStorage();
      }

      this.cacheEnabledItems();
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         if (this.leaveTypeChecker.isSelected()) {
            this.networkManager.notifyAuctionEnter();
         }

         if (this.leaveTypeBuyer.isSelected()) {
            this.networkManager.requestAuctionOpen();
         }
      }
   }

   private void exitAuction() {
      if (this.open) {
         this.open = false;
         this.serverInAuction = false;
         this.auctionHandler.clear();
         if (this.getServerMode() == AutoBuyServerMode.FUNTIME && this.leaveTypeChecker.isSelected()) {
            this.networkManager.notifyAuctionLeave();
         }
      }
   }

   private void handleBuyerMode(GenericContainerScreen var1, int var2, List<Slot> var3) {
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         long var4 = this.networkManager.getClientInAuctionCount();
         if (this.networkManager.getQueueSize() > 30) {
            this.auctionHandler.updateAuction(mc, var2);
            this.networkManager.sendUpdateToClients();
            this.updateTimer.resetCounter();
            this.networkManager.clearQueues();
         } else {
            if (!this.storageManager.hasReachedMaxShulkers()) {
               BuyRequest var6 = this.networkManager.pollRequest();
               if (var6 != null) {
                  this.auctionHandler.handleBuyRequest(mc, var2, var3, var6, this.networkManager);
               }
            }

            if (this.auctionHandler.shouldUpdate()) {
               this.auctionHandler.updateAuction(mc, var2);
               this.networkManager.sendUpdateToClients();
               this.updateTimer.resetCounter();
               this.networkManager.clearQueues();
            }

            if (this.updateTimer.hasTimeElapsed(this.randomizedTimerValue) && this.serverInAuction && var4 > 0L && this.networkManager.isQueuesEmpty()) {
               this.auctionHandler.updateAuction(mc, var2);
               this.networkManager.sendUpdateToClients();
               this.updateTimer.resetCounter();
               this.generateRandomizedTimer();
            }
         }
      }
   }

   private void handleCheckerMode(List<Slot> var1) {
      if (!this.cachedEnabledItems.isEmpty()) {
         List var2 = this.auctionHandler.findMatchingSlots(var1, this.cachedEnabledItems);
         if (!var2.isEmpty()) {
            this.auctionHandler.processBestSlots(var2, this.networkManager);
            this.buyTimer.resetCounter();
         }
      }
   }

   private void handleCheckerParserMode(int var1, List<Slot> var2, String var3) {
      if (this.getServerMode() == AutoBuyServerMode.FUNTIME) {
         if (var2 != null && var3 != null) {
            String var4 = FunTimePriceParser.getCurrentItemName();
            if (var4 != null && !var4.isEmpty()) {
               int var5 = FunTimePriceParser.findCheapestPerItemStatic(var2, var4);
               if (var5 > 0) {
                  int var6 = FunTimePriceParser.getDiscountPercent();
                  int var7 = (int)Math.floor(var5 * (100.0 - var6) / 100.0);
                  if (var7 < 0) {
                     var7 = 0;
                  }

                  this.networkManager.sendParserResult(var4, var5, var7);
               } else {
                  this.networkManager.sendParserResult(var4, -1, -1);
               }

               this.auctionHandler.updateAuction(mc, var1);
            }
         }
      }
   }

   private void handleServerAutoSwitch() {
      if (!FunTimePriceParser.isEnabled()) {
         if (this.getServerMode() == AutoBuyServerMode.FUNTIME
            && this.leaveTypeBuyer.isSelected()
            && (this.version165.isSelected() || this.version214.isSelected())
            && !this.serverManager.isInHub()
            && this.switchTimer.hasTimeElapsed(60000L)) {
            this.serverManager.switchToNextServer(mc.player);
         }
      }
   }

   private void handleHolyWorldMode(int var1, List<Slot> var2) {
      this.holyWorldAuctionHandler.clearStaleConfirmIfOnAuction();
      if (!this.cachedEnabledItems.isEmpty()) {
         long var3 = this.holyWorldAuctionHandler.getRefreshDelayMs();
         long var5 = 0L;
         this.holyWorldAuctionHandler.tick(var1, var2, this.cachedEnabledItems, var3, var5);
      }
   }

   private void handleSpookyTimeMode(int var1, List<Slot> var2) {
      if (this.autoBuyManager.isEnabled()) {
         AutoBuyServerMode var3 = this.getServerMode();
         if (var3 != AutoBuyServerMode.SPOOKYTIME) {
            this.cacheEnabledItems();
         } else {
            if (this.cachedEnabledItems.isEmpty()) {
               this.cacheEnabledItems();
            }

            long var4 = 300L;
            long var6 = 0L;
            this.spookyTimeAuctionHandler.tick(var1, var2, this.cachedEnabledItems, var4, var6);
         }
      }
   }

   public HolyWorldAuctionHandler getHolyWorldAuctionHandler() {
      return this.holyWorldAuctionHandler;
   }

   public SpookyTimeAuctionHandler getSpookyTimeAuctionHandler() {
      return this.spookyTimeAuctionHandler;
   }

   public AuctionHandler getAuctionHandler() {
      return this.auctionHandler;
   }

   public enum AutoBuyServerMode {
      FUNTIME("FunTime"),
      SPOOKYTIME("SpookyTime"),
      HOLYWORLD("HolyWorld");

      private final String displayName;

      AutoBuyServerMode(String var3) {
         this.displayName = var3;
      }

      public String getDisplayName() {
         return this.displayName;
      }
   }
}
