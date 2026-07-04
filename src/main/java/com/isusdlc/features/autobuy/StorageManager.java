package com.isusdlc.features.autobuy;

import com.isusdlc.features.autobuy.util.AuctionUtils;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;

public class StorageManager {
   private static final int MAX_SHULKERS = 3;
   private final BooleanSetting autoStorage;
   private final TimerUtil storageTimer = TimerUtil.create();
   private final TimerUtil storageActionTimer = TimerUtil.create();
   private final TimerUtil auctionEnterTimer = TimerUtil.create();
   private final TimerUtil postStorageTimer = TimerUtil.create();
   private boolean storageActive = false;
   private int storageStep = 0;
   private int storageAttempts = 0;
   private boolean waitingForAuctionClose = false;
   private boolean searchingShulker = false;
   private boolean buyingShulker = false;
   private int currentShulkerIndex = 0;
   private final List<Integer> shulkerSlots = new ArrayList<>();
   private boolean reachedMaxShulkers = false;
   private boolean canStartStorage = false;
   private boolean storageCompleted = false;

   public StorageManager(BooleanSetting var1) {
      this.autoStorage = var1;
   }

   public void resetTimers() {
      this.storageTimer.resetCounter();
      this.storageActionTimer.resetCounter();
      this.auctionEnterTimer.resetCounter();
      this.postStorageTimer.resetCounter();
   }

   public void reset() {
      this.storageActive = false;
      this.storageStep = 0;
      this.storageAttempts = 0;
      this.waitingForAuctionClose = false;
      this.searchingShulker = false;
      this.buyingShulker = false;
      this.currentShulkerIndex = 0;
      this.shulkerSlots.clear();
      this.reachedMaxShulkers = false;
      this.canStartStorage = false;
      this.storageCompleted = false;
   }

   public void handle(MinecraftClient var1, boolean var2) {
      if (this.autoStorage.isEnabled()) {
         if (!this.reachedMaxShulkers) {
            if (this.canStartStorage) {
               if (!this.storageActive) {
                  int var3 = this.getFreeInventorySlots(var1);
                  if (var3 <= 9 && this.hasResourcesInInventory(var1)) {
                     this.startStorage();
                  }
               } else if (this.storageActionTimer.hasTimeElapsed(300L)) {
                  this.processStorageStep(var1);
               }
            }
         }
      }
   }

   private void startStorage() {
      this.storageActive = true;
      this.storageStep = 0;
      this.storageAttempts = 0;
      this.waitingForAuctionClose = false;
      this.searchingShulker = false;
      this.buyingShulker = false;
      this.currentShulkerIndex = 0;
      this.shulkerSlots.clear();
      this.storageCompleted = false;
      this.storageTimer.resetCounter();
      this.storageActionTimer.resetCounter();
   }

   private void processStorageStep(MinecraftClient var1) {
      switch (this.storageStep) {
         case 0:
            this.handleStep0(var1);
            break;
         case 1:
            this.handleStep1(var1);
            break;
         case 2:
            this.handleStep2(var1);
            break;
         case 15:
            this.handleStep15();
            break;
         case 20:
            this.handleStep20(var1);
            break;
         case 21:
            this.handleStep21(var1);
            break;
         case 22:
            this.handleStep22(var1);
            break;
         case 23:
            this.handleStep23(var1);
            break;
         case 24:
            this.handleStep24(var1);
            break;
         case 25:
            this.handleStep25(var1);
            break;
         case 26:
            this.handleStep26(var1);
            break;
         case 100:
            this.handleStep100(var1);
            break;
         case 101:
            this.handleStep101(var1);
            break;
         case 102:
            this.handleStep102();
            break;
         case 103:
            this.handleStep103(var1);
            break;
         case 104:
            this.handleStep104(var1);
            break;
         case 105:
            this.handleStep105();
            break;
         case 201:
            this.handleStep201(var1);
      }
   }

   private void handleStep0(MinecraftClient var1) {
      if (var1.currentScreen instanceof GenericContainerScreen) {
         var1.player.closeHandledScreen();
         this.waitingForAuctionClose = true;
         this.storageAttempts = 0;
         this.storageTimer.resetCounter();
         this.storageStep = 1;
      } else {
         this.storageStep = 2;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep1(MinecraftClient var1) {
      if (!(var1.currentScreen instanceof GenericContainerScreen)) {
         this.waitingForAuctionClose = false;
         this.storageTimer.resetCounter();
         this.storageStep = 15;
      } else if (this.storageTimer.hasTimeElapsed(5000L)) {
         this.waitingForAuctionClose = false;
         this.storageTimer.resetCounter();
         this.storageStep = 15;
      } else {
         this.storageAttempts++;
         if (this.storageAttempts > 3) {
            var1.player.closeHandledScreen();
            this.storageTimer.resetCounter();
         }
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep15() {
      if (this.storageTimer.hasTimeElapsed(500L)) {
         this.storageStep = 2;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep2(MinecraftClient var1) {
      this.currentShulkerIndex = 0;
      this.shulkerSlots.clear();

      for (int var2 = 0; var2 < 36; var2++) {
         if (this.isShulkerBox(var1.player.getInventory().getStack(var2))) {
            this.shulkerSlots.add(var2);
         }
      }

      if (this.shulkerSlots.isEmpty()) {
         this.storageStep = 100;
      } else {
         this.storageStep = 20;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep20(MinecraftClient var1) {
      if (var1.currentScreen == null) {
         var1.setScreen(new InventoryScreen(var1.player));
         this.storageTimer.resetCounter();
         this.storageStep = 21;
      } else if (var1.currentScreen instanceof InventoryScreen) {
         this.storageTimer.resetCounter();
         this.storageStep = 21;
      } else {
         var1.player.closeHandledScreen();
         this.storageTimer.resetCounter();
         this.storageStep = 201;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep201(MinecraftClient var1) {
      if (this.storageTimer.hasTimeElapsed(500L)) {
         var1.setScreen(new InventoryScreen(var1.player));
         this.storageTimer.resetCounter();
         this.storageStep = 21;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep21(MinecraftClient var1) {
      if (!(var1.currentScreen instanceof InventoryScreen)) {
         if (this.storageTimer.hasTimeElapsed(2000L)) {
            if (var1.currentScreen != null) {
               var1.player.closeHandledScreen();
            }

            this.storageTimer.resetCounter();
            this.storageStep = 201;
         }

         this.storageActionTimer.resetCounter();
      } else {
         if (this.storageTimer.hasTimeElapsed(800L)) {
            this.storageTimer.resetCounter();
            this.storageStep = 22;
         }

         this.storageActionTimer.resetCounter();
      }
   }

   private void handleStep22(MinecraftClient var1) {
      if (!(var1.currentScreen instanceof InventoryScreen)) {
         this.storageStep = 20;
         this.storageActionTimer.resetCounter();
      } else {
         if (this.storageTimer.hasTimeElapsed(300L)) {
            if (this.currentShulkerIndex >= this.shulkerSlots.size()) {
               if (!this.hasResourcesInInventory(var1)) {
                  this.finishStorage(var1);
               } else {
                  this.storageStep = 100;
               }
            } else {
               int var2 = this.shulkerSlots.get(this.currentShulkerIndex);
               int var3 = this.getSlotId(var2);
               if (var3 == -1) {
                  this.currentShulkerIndex++;
                  this.storageTimer.resetCounter();
                  return;
               }

               var1.interactionManager.clickSlot(var1.player.currentScreenHandler.syncId, var3, 1, SlotActionType.PICKUP, var1.player);
               this.storageTimer.resetCounter();
               this.storageStep = 23;
            }
         }

         this.storageActionTimer.resetCounter();
      }
   }

   private void handleStep23(MinecraftClient var1) {
      if (var1.currentScreen instanceof ShulkerBoxScreen) {
         this.storageTimer.resetCounter();
         this.storageStep = 24;
      } else if (this.storageTimer.hasTimeElapsed(2000L)) {
         this.currentShulkerIndex++;
         this.storageTimer.resetCounter();
         this.storageStep = 22;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep24(MinecraftClient var1) {
      if (!(var1.currentScreen instanceof ShulkerBoxScreen)) {
         this.currentShulkerIndex++;
         this.storageTimer.resetCounter();
         this.storageStep = 22;
         this.storageActionTimer.resetCounter();
      } else {
         if (this.storageTimer.hasTimeElapsed(500L)) {
            this.storageStep = 25;
         }

         this.storageActionTimer.resetCounter();
      }
   }

   private void handleStep25(MinecraftClient var1) {
      if (!(var1.currentScreen instanceof ShulkerBoxScreen var2)) {
         this.currentShulkerIndex++;
         this.storageTimer.resetCounter();
         this.storageStep = 22;
         this.storageActionTimer.resetCounter();
      } else {
         DefaultedList var9 = ((ShulkerBoxScreenHandler)var2.getScreenHandler()).slots;
         if (this.isShulkerFull(var9)) {
            this.currentShulkerIndex++;
            this.storageTimer.resetCounter();
            this.storageStep = 26;
            this.storageActionTimer.resetCounter();
         } else {
            boolean var4 = false;

            for (int var5 = 27; var5 < var9.size(); var5++) {
               Slot var6 = (Slot)var9.get(var5);
               ItemStack var7 = var6.getStack();
               if (!var7.isEmpty() && !this.isShulkerBox(var7) && !this.isBag(var7)) {
                  int var8 = ((ShulkerBoxScreenHandler)var2.getScreenHandler()).syncId;
                  var1.interactionManager.clickSlot(var8, var6.id, 0, SlotActionType.QUICK_MOVE, var1.player);
                  var4 = true;
                  this.storageTimer.resetCounter();
                  break;
               }
            }

            if (!var4) {
               this.currentShulkerIndex++;
               this.storageTimer.resetCounter();
               this.storageStep = 26;
            }

            this.storageActionTimer.resetCounter();
         }
      }
   }

   private void handleStep26(MinecraftClient var1) {
      if (this.storageTimer.hasTimeElapsed(300L)) {
         if (this.currentShulkerIndex >= this.shulkerSlots.size()) {
            if (!this.hasResourcesInInventory(var1)) {
               if (var1.currentScreen instanceof ShulkerBoxScreen) {
                  var1.player.closeHandledScreen();
               }

               this.finishStorage(var1);
            } else {
               if (var1.currentScreen instanceof ShulkerBoxScreen) {
                  var1.player.closeHandledScreen();
               }

               this.storageTimer.resetCounter();
               this.storageStep = 100;
            }
         } else {
            int var2 = this.shulkerSlots.get(this.currentShulkerIndex);
            int var3 = this.getSlotId(var2);
            if (var3 == -1) {
               this.currentShulkerIndex++;
               this.storageTimer.resetCounter();
               return;
            }

            var1.interactionManager.clickSlot(var1.player.currentScreenHandler.syncId, var3, 1, SlotActionType.PICKUP, var1.player);
            this.storageTimer.resetCounter();
            this.storageStep = 23;
         }
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep100(MinecraftClient var1) {
      int var2 = this.countTotalShulkers(var1);
      if (var2 >= 3) {
         if (var1.currentScreen != null) {
            var1.player.closeHandledScreen();
         }

         this.reachedMaxShulkers = true;
         this.finishStorage(var1);
      } else {
         if (var1.currentScreen != null) {
            var1.player.closeHandledScreen();
         }

         this.storageTimer.resetCounter();
         this.storageStep = 101;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep101(MinecraftClient var1) {
      if (this.storageTimer.hasTimeElapsed(500L)) {
         if (!this.searchingShulker) {
            CommandSender.sendCommand(var1.player, "/ah search Шалкер пустой");
            this.searchingShulker = true;
            this.storageTimer.resetCounter();
         }

         if (var1.currentScreen instanceof GenericContainerScreen) {
            this.storageTimer.resetCounter();
            this.storageStep = 102;
         } else if (this.storageTimer.hasTimeElapsed(6000L)) {
            this.searchingShulker = false;
            this.storageStep = 101;
         }
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep102() {
      if (this.storageTimer.hasTimeElapsed(3000L)) {
         this.storageStep = 103;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep103(MinecraftClient var1) {
      if (var1.currentScreen instanceof GenericContainerScreen var2) {
         DefaultedList var10 = ((GenericContainerScreenHandler)var2.getScreenHandler()).slots;
         Slot var4 = null;
         int var5 = 100001;

         for (int var6 = 0; var6 <= 44; var6++) {
            Slot var7 = (Slot)var10.get(var6);
            ItemStack var8 = var7.getStack();
            if (this.isShulkerBox(var8)) {
               int var9 = AuctionUtils.getPrice(var8);
               if (var9 > 0 && var9 <= 100000 && var9 < var5) {
                  var4 = var7;
                  var5 = var9;
               }
            }
         }

         if (var4 != null) {
            int var11 = ((GenericContainerScreenHandler)var2.getScreenHandler()).syncId;
            var1.interactionManager.clickSlot(var11, var4.id, 0, SlotActionType.QUICK_MOVE, var1.player);
            this.buyingShulker = true;
            this.storageTimer.resetCounter();
            this.storageStep = 104;
         } else {
            int var12 = ((GenericContainerScreenHandler)var2.getScreenHandler()).syncId;
            var1.interactionManager.clickSlot(var12, 49, 0, SlotActionType.QUICK_MOVE, var1.player);
            this.storageTimer.resetCounter();
         }
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep104(MinecraftClient var1) {
      if (this.storageTimer.hasTimeElapsed(2500L)) {
         if (var1.currentScreen instanceof GenericContainerScreen) {
            var1.player.closeHandledScreen();
         }

         this.storageTimer.resetCounter();
         this.storageStep = 105;
      }

      this.storageActionTimer.resetCounter();
   }

   private void handleStep105() {
      if (this.storageTimer.hasTimeElapsed(1000L)) {
         this.searchingShulker = false;
         this.buyingShulker = false;
         this.storageStep = 2;
      }

      this.storageActionTimer.resetCounter();
   }

   private void finishStorage(MinecraftClient var1) {
      this.storageActive = false;
      this.storageCompleted = true;
      this.postStorageTimer.resetCounter();
      this.canStartStorage = false;
      this.storageStep = 0;
   }

   private int getFreeInventorySlots(MinecraftClient var1) {
      int var2 = 0;

      for (int var3 = 9; var3 < 36; var3++) {
         if (var1.player.getInventory().getStack(var3).isEmpty()) {
            var2++;
         }
      }

      return var2;
   }

   private boolean isShulkerBox(ItemStack var1) {
      if (var1.isEmpty()) {
         return false;
      } else {
         return var1.getItem() instanceof BlockItem var2 ? var2.getBlock() instanceof ShulkerBoxBlock : false;
      }
   }

   private boolean isBag(ItemStack var1) {
      return var1.isEmpty() ? false : var1.getItem() instanceof BundleItem;
   }

   private int countTotalShulkers(MinecraftClient var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = var1.player.getInventory().getStack(var3);
         if (this.isShulkerBox(var4)) {
            var2++;
         }
      }

      return var2;
   }

   private boolean isShulkerFull(List<Slot> var1) {
      for (int var2 = 0; var2 < 27; var2++) {
         if (var2 < var1.size() && ((Slot)var1.get(var2)).getStack().isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private boolean hasResourcesInInventory(MinecraftClient var1) {
      for (int var2 = 9; var2 < 36; var2++) {
         ItemStack var3 = var1.player.getInventory().getStack(var2);
         if (!var3.isEmpty() && !this.isShulkerBox(var3) && !this.isBag(var3)) {
            return true;
         }
      }

      return false;
   }

   private int getSlotId(int var1) {
      if (var1 >= 0 && var1 < 9) {
         return var1 + 36;
      } else {
         return var1 >= 9 && var1 < 36 ? var1 : -1;
      }
   }

   public boolean isActive() {
      return this.storageActive;
   }

   public void notifyAuctionEnter() {
      this.auctionEnterTimer.resetCounter();
   }

   public void handleAuctionEnter() {
      if (this.autoStorage.isEnabled() && !this.canStartStorage && this.auctionEnterTimer.hasTimeElapsed(5000L)) {
         this.canStartStorage = true;
      }
   }

   public boolean handlePostStorage(MinecraftClient var1, TimerUtil var2, TimerUtil var3) {
      if (this.storageCompleted && this.postStorageTimer.hasTimeElapsed(5000L)) {
         this.storageCompleted = false;
         this.canStartStorage = false;
         var2.resetCounter();
         var3.resetCounter();
         if (!(var1.currentScreen instanceof GenericContainerScreen)) {
            CommandSender.sendCommand(var1.player, "/ah");
         }

         return true;
      } else {
         return false;
      }
   }

   public TimerUtil getPostStorageTimer() {
      return this.postStorageTimer;
   }

   public void clearStorageCompleted() {
      this.storageCompleted = false;
   }

   public void disableStartStorage() {
      this.canStartStorage = false;
   }

   public boolean hasReachedMaxShulkers() {
      return this.reachedMaxShulkers;
   }

   public void resetMaxShulkers() {
      this.reachedMaxShulkers = false;
      this.canStartStorage = false;
   }
}
