package com.isusdlc.systems.event.impl.render;

import lombok.Generated;
import com.isusdlc.systems.event.EventCancellable;
import com.isusdlc.systems.modules.constructions.swinganim.SwingTransformations;
import com.isusdlc.systems.modules.constructions.viewmodel.ViewModelTransformations;
import net.minecraft.util.Hand;
import net.minecraft.util.Arm;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.math.MatrixStack;

public class HandRenderEvent extends EventCancellable {
   private final Arm arm;
   private final Hand hand;
   private final float swingProgress;
   private final ItemStack itemStack;
   private final float equipProgress;
   private final MatrixStack matrices;
   private SwingTransformations customTransformations;
   private ViewModelTransformations viewModelTransformations;

   @Generated
   public Arm getArm() {
      return this.arm;
   }

   @Generated
   public Hand getHand() {
      return this.hand;
   }

   @Generated
   public float getSwingProgress() {
      return this.swingProgress;
   }

   @Generated
   public ItemStack getItemStack() {
      return this.itemStack;
   }

   @Generated
   public float getEquipProgress() {
      return this.equipProgress;
   }

   @Generated
   public MatrixStack getMatrices() {
      return this.matrices;
   }

   @Generated
   public SwingTransformations getCustomTransformations() {
      return this.customTransformations;
   }

   @Generated
   public void setCustomTransformations(SwingTransformations customTransformations) {
      this.customTransformations = customTransformations;
   }

   @Generated
   public ViewModelTransformations getViewModelTransformations() {
      return this.viewModelTransformations;
   }

   @Generated
   public void setViewModelTransformations(ViewModelTransformations viewModelTransformations) {
      this.viewModelTransformations = viewModelTransformations;
   }

   @Generated
   public HandRenderEvent(Arm arm, Hand hand, float swingProgress, ItemStack itemStack, float equipProgress, MatrixStack matrices) {
      this.arm = arm;
      this.hand = hand;
      this.swingProgress = swingProgress;
      this.itemStack = itemStack;
      this.equipProgress = equipProgress;
      this.matrices = matrices;
      this.customTransformations = null;
      this.viewModelTransformations = null;
   }
}
