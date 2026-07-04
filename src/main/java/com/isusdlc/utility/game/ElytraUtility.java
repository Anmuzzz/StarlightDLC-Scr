package com.isusdlc.utility.game;

import java.util.List;
import java.util.function.Predicate;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.inventory.InventoryUtility;
import com.isusdlc.utility.inventory.group.SlotGroup;
import com.isusdlc.utility.inventory.group.SlotGroups;
import com.isusdlc.utility.inventory.slots.HotbarSlot;
import com.isusdlc.utility.inventory.slots.InventorySlot;
import com.isusdlc.utility.math.MathUtility;
import com.isusdlc.utility.mixins.ArmorItemAddition;
import com.isusdlc.utility.render.Draw3DUtility;
import com.isusdlc.utility.rotations.Rotation;
import com.isusdlc.utility.rotations.RotationMath;
import com.isusdlc.utility.time.Timer;
import net.minecraft.util.Hand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.equipment.EquipmentType;

public final class ElytraUtility implements IMinecraft {
   private static Vec3d lastVec;
   private static final Timer fireworkTimer = new Timer();

   public static void swapInHotbar(boolean chestplate) {
      SlotGroup<HotbarSlot> slotsToSearch = SlotGroups.hotbar();
      HotbarSlot slot = chestplate
         ? slotsToSearch.findItem(
            (Predicate<ItemStack>)(itemStack -> itemStack.getItem() instanceof ArmorItem armorItem
               && ((ArmorItemAddition)armorItem).elegant$getType() == EquipmentType.CHESTPLATE)
         )
         : slotsToSearch.findItem(Items.ELYTRA);
      if (slot != null) {
         HotbarSlot currentItem = InventoryUtility.getCurrentHotbarSlot();
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot.getSlotId()));
         InventoryUtility.selectHotbarSlot(slot);
         mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
         ((Slot)mc.player.currentScreenHandler.slots.get(6)).setStack(new ItemStack(chestplate ? Items.NETHERITE_CHESTPLATE : Items.ELYTRA));
         InventoryUtility.selectHotbarSlot(currentItem);
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
      }
   }

   public static void drawBoxes(MatrixStack matrices, BufferBuilder linesBuffer, Box box, ColorRGBA color) {
      Draw3DUtility.renderOutlinedBox(matrices, linesBuffer, box, color);
      Draw3DUtility.renderBoxInternalDiagonals(matrices, linesBuffer, box, color);
   }

   public static boolean leaving() {
      return false;
   }

   public static void useFirework(float selectedSlot) {
      SlotGroup<HotbarSlot> slotsToSearch = SlotGroups.hotbar();
      HotbarSlot slot = slotsToSearch.findItem(Items.FIREWORK_ROCKET);
      if (slot != null) {
         LivingEntity target = elegant.getInstance().getTargetManager().getLivingTarget();
         Rotation rot = target == null
            ? elegant.getInstance().getRotationHandler().getPlayerRotation()
            : RotationMath.getRotationTo(leaving() ? mc.player.getEyePos().add(leaveVec(target)) : targetPoint(target));
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot.getSlotId()));
         mc.interactionManager.sendSequencedPacket(mc.world, sequence -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence, rot.getYaw(), rot.getPitch()));
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
         fireworkTimer.reset();
      } else {
         SlotGroup<InventorySlot> search = SlotGroups.inventory();
         InventorySlot invSlot = search.findItem(Items.FIREWORK_ROCKET);
         if (invSlot != null) {
            InventoryUtility.hotbarSwap(invSlot.getIdForServer(), (int)(selectedSlot - 1.0F));
            fireworkTimer.reset();
         }
      }
   }

   public static Vec3d targetPoint(LivingEntity target) {
      return RotationMath.getNearestPoint(target, target.getPos());
   }

   public static Vec3d leaveVec(LivingEntity target) {
      List<Vec3d> leaveVectors = List.of(
         new Vec3d(0.0, 20.0, 0.0),
         new Vec3d(0.0, -20.0, 0.0),
         new Vec3d(20.0, 0.0, 0.0),
         new Vec3d(-20.0, 0.0, 0.0),
         new Vec3d(0.0, 0.0, 20.0),
         new Vec3d(0.0, 0.0, -20.0)
      );
      if (CombatUtility.getMace() != null) {
         leaveVectors = List.of(new Vec3d(0.0, 20.0, 0.0));
      }

      Vec3d leaveVec = Vec3d.ZERO;

      for (Vec3d vector : leaveVectors) {
         if (MathUtility.canSeen(target.getEyePos().add(vector)) && !vector.equals(lastVec)) {
            leaveVec = vector;
            break;
         }
      }

      return leaveVec;
   }

   public static float[] getLeftRightYaw45NotMultipleOf90(float yaw) {
      float baseYaw = yaw - yaw % 360.0F;
      float yawNormalized = yaw % 360.0F;
      if (yawNormalized < 0.0F) {
         yawNormalized += 360.0F;
         baseYaw -= 360.0F;
      }

      float nearest45 = Math.round(yawNormalized / 45.0F) * 45.0F;
      float left;
      float right;
      if (nearest45 % 90.0F == 0.0F) {
         float candidateLeft = (nearest45 - 45.0F) % 360.0F;
         float candidateRight = (nearest45 + 45.0F) % 360.0F;
         left = candidateLeft < yawNormalized ? candidateLeft : candidateLeft - 45.0F;
         right = candidateRight > yawNormalized ? candidateRight : candidateRight + 45.0F;
      } else if (nearest45 < yawNormalized) {
         left = nearest45;
         right = nearest45 + 90.0F;
      } else {
         left = nearest45 - 90.0F;
         right = nearest45;
      }

      left += baseYaw;
      right += baseYaw;
      return new float[]{left, right};
   }

   @Generated
   private ElytraUtility() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Generated
   public static void setLastVec(Vec3d lastVec) {
      ElytraUtility.lastVec = lastVec;
   }

   @Generated
   public static Timer getFireworkTimer() {
      return fireworkTimer;
   }
}
