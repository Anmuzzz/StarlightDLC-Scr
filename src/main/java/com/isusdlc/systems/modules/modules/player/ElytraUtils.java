package com.isusdlc.systems.modules.modules.player;

import java.util.function.Predicate;
import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.event.impl.window.MouseEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.utility.game.server.ServerUtility;
import com.isusdlc.utility.inventory.InventoryUtility;
import com.isusdlc.utility.inventory.ItemSlot;
import com.isusdlc.utility.inventory.group.SlotGroup;
import com.isusdlc.utility.inventory.group.SlotGroups;
import com.isusdlc.utility.mixins.ArmorItemAddition;
import com.isusdlc.utility.rotations.Rotation;
import com.isusdlc.utility.rotations.RotationHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;

@ModuleInfo(
   name = "Elytra Utils",
   category = ModuleCategory.PLAYER,
   desc = "Помощник с элитрами"
)
public class ElytraUtils extends BaseModule {
   private final BindSetting swapKey = new BindSetting(this, "Клавиша свапа");
   private final BindSetting fireworkKey = new BindSetting(this, "Клавиша фейерверка");
   private boolean wasFlying;
   private ElytraUtils.SwapTask swapTask;
   private final EventListener<ClientPlayerTickEvent> onUpdate = event -> {
      if (mc.player.isGliding()) {
         this.wasFlying = true;
      }

      ItemSlot chestplateSlot = InventoryUtility.getChestplateSlot();
      SlotGroup<ItemSlot> group = SlotGroups.hotbar().and(SlotGroups.inventory()).and(SlotGroups.offhand());
      ItemSlot chestplateItemSlot = group.findItem(
         (Predicate<ItemStack>)(itemStack -> itemStack.getItem() instanceof ArmorItem armorItem
            && ((ArmorItemAddition)armorItem).elegant$getType() == EquipmentType.CHESTPLATE)
      );
      ItemSlot slot = group.findItem(Items.FIREWORK_ROCKET);
      boolean isElytraEquipped = chestplateSlot.item() == Items.ELYTRA;
      if (this.swapTask != null) {
         if (this.swapTask.stage == 0 && this.swapTask.ticksWaited < 2) {
            this.swapTask.ticksWaited++;
            return;
         }

         if (this.swapTask.stage == 0 && mc.currentScreen == null) {
            mc.setScreen(new InventoryScreen(mc.player));
            this.swapTask.ticksWaited = 0;
            this.swapTask.stage++;
            return;
         }

         if (this.swapTask.stage == 1 && this.swapTask.ticksWaited < 2) {
            this.swapTask.ticksWaited++;
            return;
         }

         switch (this.swapTask.stage) {
            case 1:
            case 3:
               InventoryUtility.hotbarSwap(this.swapTask.from.getIdForServer(), 40);
               break;
            case 2:
               InventoryUtility.hotbarSwap(this.swapTask.chest.getIdForServer(), 40);
         }

         if (this.swapTask.stage++ >= 3) {
            mc.setScreen(null);
            this.swapTask = null;
         }
      }
   };
   private final EventListener<KeyPressEvent> onKeyPressEvent = event -> {
      if (this.swapKey.isKey(event.getKey()) && event.getAction() == 1 && mc.currentScreen == null) {
         this.swapElytraChestplate();
      }

      if (this.fireworkKey.isKey(event.getKey()) && event.getAction() == 1 && mc.currentScreen == null) {
         InventoryUtility.selectItemInHotbar(Items.FIREWORK_ROCKET);
      }
   };
   private final EventListener<MouseEvent> onMouseButtonPress = event -> {
      if (this.swapKey.isKey(event.getButton()) && event.getAction() == 1 && mc.currentScreen == null) {
         this.swapElytraChestplate();
      }

      if (this.fireworkKey.isKey(event.getButton()) && event.getAction() == 1 && mc.currentScreen == null) {
         InventoryUtility.selectItemInHotbar(Items.FIREWORK_ROCKET);
      }
   };

   private void swapElytraChestplate() {
      ItemSlot chestplateSlot = InventoryUtility.getChestplateSlot();
      SlotGroup<ItemSlot> slotsToSearch = SlotGroups.inventory().and(SlotGroups.hotbar());
      ItemSlot elytraItemSlot = slotsToSearch.findItem(
         (Predicate<ItemStack>)(itemStack -> itemStack.getItem() == Items.ELYTRA && !itemStack.willBreakNextUse())
      );
      ItemSlot chestplateItemSlot = slotsToSearch.findItem(
         (Predicate<ItemStack>)(itemStack -> itemStack.getItem() instanceof ArmorItem armorItem
            && ((ArmorItemAddition)armorItem).elegant$getType() == EquipmentType.CHESTPLATE)
      );
      boolean isElytraEquipped = chestplateSlot.item() == Items.ELYTRA;
      if (!isElytraEquipped && elytraItemSlot != null) {
         if (mc.player != null) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
         }

         this.swapTask = new ElytraUtils.SwapTask(elytraItemSlot, chestplateSlot);
      } else if (chestplateItemSlot != null) {
         if (mc.player != null) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
         }

         this.swapTask = new ElytraUtils.SwapTask(chestplateItemSlot, chestplateSlot);
      }
   }

   private double getAdvancedBoost() {
      RotationHandler rotationHandler = elegant.getInstance().getRotationHandler();
      Rotation rot = rotationHandler.isIdling() ? rotationHandler.getPlayerRotation() : rotationHandler.getCurrentRotation();
      float playerYaw = rot.getYaw();
      float playerPitch = rot.getPitch();
      double A = 0.239037;
      double B = 4.489648;
      double C = 1.236087;
      double MAX_ACCELERATION_YAW = 1.47;
      double YAW_TOLERANCE = 7.9;
      double MAX_PITCH_BOOST = 1.01;
      double MAX_PITCH = -45.0;
      double MIN_PITCH = 10.0;
      double effectiveYaw = Math.abs(playerYaw) % 90.0;
      double yawAcceleration;
      if (Math.abs(effectiveYaw - 45.0) <= 7.9) {
         yawAcceleration = 1.47;
      } else {
         double argument = 4.489648 * (effectiveYaw - 45.0);
         yawAcceleration = 0.239037 * Math.cos(Math.toRadians(argument)) + 1.236087;
      }

      if (playerPitch >= 10.0F) {
         return Math.abs(effectiveYaw - 45.0) <= 5.0 ? 1.8 : yawAcceleration;
      } else if (playerPitch >= 0.0F) {
         return 1.0;
      } else if (playerPitch < -80.0F) {
         return 1.0;
      } else {
         double pitchRatio = Math.min(1.0, Math.abs(playerPitch) / Math.abs(-45.0));
         double pitchMultiplier = 1.0 + 0.010000000000000009 * pitchRatio;
         double totalAcceleration = yawAcceleration * pitchMultiplier;
         return Math.min(totalAcceleration, 1.49);
      }
   }

   private static int findClosestVector(float lastYaw, int[] vectors) {
      int index = 0;
      int minDistIndex = -1;
      float minDist = Float.MAX_VALUE;

      for (int vector : vectors) {
         float dist = Math.abs(MathHelper.wrapDegrees(lastYaw) - vector);
         if (dist < minDist) {
            minDist = dist;
            minDistIndex = index;
         }

         index++;
      }

      return minDistIndex;
   }

   private double calculateDynamicBoostPower(LivingEntity player) {
      float yaw = player.getYaw();
      float pitch = player.getPitch();
      double minSpeed = 1.4;
      double maxSpeed = 1.9;
      double yawFactor = this.calculateYawFactor(yaw);
      double pitchFactor = this.calculatePitchFactor(pitch);
      double combinedFactor = yawFactor * pitchFactor;
      double boostPower = minSpeed + (maxSpeed - minSpeed) * combinedFactor;
      return Math.max(minSpeed, Math.min(maxSpeed, boostPower));
   }

   private double calculateYawFactor(float yaw) {
      yaw = (yaw % 360.0F + 360.0F) % 360.0F;
      double[] diagonalAngles = new double[]{45.0, 135.0, 225.0, 315.0};
      double minDistanceToDiagonal = Double.MAX_VALUE;

      for (double diagonal : diagonalAngles) {
         double distance = Math.min(Math.abs(yaw - diagonal), Math.min(Math.abs(yaw - diagonal + 360.0), Math.abs(yaw - diagonal - 360.0)));
         minDistanceToDiagonal = Math.min(minDistanceToDiagonal, distance);
      }

      return minDistanceToDiagonal <= 45.0 ? 1.0 - minDistanceToDiagonal / 45.0 * 0.85 : 0.15;
   }

   private double calculatePitchFactor(float pitch) {
      float absPitch = Math.abs(pitch);
      if (absPitch <= 10.0F) {
         return 1.0;
      } else if (absPitch <= 30.0F) {
         return 1.0 - (absPitch - 10.0F) / 20.0 * 0.3;
      } else {
         return absPitch <= 60.0F ? 0.7 - (absPitch - 30.0F) / 30.0 * 0.4 : 0.3;
      }
   }

   @Override
   public void onDisable() {
      this.wasFlying = false;
   }

   @Override
   public boolean isHidden() {
      return super.isHidden() || ServerUtility.isHW() || ServerUtility.isRW();
   }

   @Override
   public void setEnabled(boolean newState, boolean silent) {
      if (!newState || !this.isHidden()) {
         super.setEnabled(newState, silent);
      }
   }

   @Override
   public void tick() {
      if (this.isHidden()) {
         super.setEnabled(false, true);
      }
   }

   @Override
   public void onEnable() {
      this.wasFlying = false;
   }

   private static class SwapTask {
      int stage;
      int ticksWaited = 0;
      final ItemSlot from;
      final ItemSlot chest;

      SwapTask(ItemSlot from, ItemSlot chest) {
         this.from = from;
         this.chest = chest;
      }
   }
}
