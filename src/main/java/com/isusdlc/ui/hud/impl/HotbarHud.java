package com.isusdlc.ui.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.hud.HudEditorScreen;
import com.isusdlc.ui.hud.HudElement;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.interfaces.IScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.screen.ChatScreen;

public class HotbarHud extends HudElement {
   public HotbarHud() {
      super("hud.hotbar", "icons/hud/hotbar.png");
   }

   @Override
   public void update(UIContext context) {
      boolean hasOffhand = mc.player != null && !mc.player.getOffHandStack().isEmpty();
      float offhandBlockWidth = hasOffhand ? 26.0F : 0.0F;
      float hotbarBlockWidth = 194.0F;
      float gapBetweenBlocks = hasOffhand ? 8.0F : 0.0F;
      this.width = offhandBlockWidth + gapBetweenBlocks + hotbarBlockWidth;
      this.height = 22.0F;
      if (!(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof HudEditorScreen)) {
         float hotbarCenterX = IScaledResolution.sr.getScaledWidth() / 2.0F;
         float hotbarBlockX = hotbarCenterX - hotbarBlockWidth / 2.0F;
         this.x = hasOffhand ? hotbarBlockX - gapBetweenBlocks - offhandBlockWidth : hotbarBlockX;
         this.y = IScaledResolution.sr.getScaledHeight() - this.height - 1.0F;
      }

      super.update(context);
   }

   @Override
   protected void renderComponent(UIContext context) {
      if (mc.player != null && mc.world != null) {
         Font font = Fonts.SEMIBOLD.getFont(5.5F);
         boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
         ColorRGBA bgColor = Colors.getBackgroundColor().withAlpha(255.0F * (dark ? 0.8F - 0.6F * Interface.glass() : 0.7F));
         float slotWidth = 20.0F;
         float slotHeight = 20.0F;
         float spacing = 1.0F;
         float paddingX = 3.0F;
         float paddingY = 1.5F;
         float offhandPaddingX = 3.0F;
         ItemStack offhandStack = mc.player.getOffHandStack();
         boolean hasOffhand = !offhandStack.isEmpty();
         float offhandBlockWidth = slotWidth + offhandPaddingX * 2.0F;
         float offhandBlockX = this.x;
         float hotbarBlockWidth = 9.0F * slotWidth + 8.0F * spacing + 6.0F;
         float hotbarBlockX = hasOffhand ? offhandBlockX + offhandBlockWidth + 8.0F : this.x;
         float offhandX = offhandBlockX + offhandPaddingX;
         float hotbarStartX = hotbarBlockX + paddingX;
         float slotY = this.y + paddingY;
         float prev = RenderSystem.getShaderColor()[3];
         if (hasOffhand) {
            context.drawShadow(
               offhandBlockX - 5.0F,
               this.y - 5.0F,
               offhandBlockWidth + 10.0F,
               this.height + 10.0F,
               15.0F,
               BorderRadius.all(6.0F),
               ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.getValue())
            );
            if (Interface.showMinimalizm()) {
               context.drawBlurredRect(
                  offhandBlockX,
                  this.y,
                  offhandBlockWidth,
                  this.height,
                  11.25F,
                  7.0F,
                  BorderRadius.all(6.0F),
                  ColorRGBA.WHITE.withAlpha(255.0F * this.animation.getValue() * Interface.minimalizm())
               );
            }

            if (Interface.showGlass()) {
               context.drawLiquidGlass(
                  offhandBlockX,
                  this.y,
                  offhandBlockWidth,
                  this.height,
                  7.0F,
                  Interface.getDistortion() - 0.07F * this.dragAnim.getValue(),
                  BorderRadius.all(6.0F),
                  Colors.getLiquidGlassColor().withAlpha(255.0F * this.animation.getValue() * Interface.glass())
               );
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
            context.drawRoundedRect(offhandBlockX, this.y, offhandBlockWidth, this.height, BorderRadius.all(6.0F), bgColor);
            float itemSize = 0.85F;
            float itemWidth = 16.0F * itemSize;
            float itemOffsetX = (slotWidth - itemWidth) / 2.0F;
            float itemOffsetY = (slotHeight - itemWidth) / 2.0F;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
            context.drawItem(offhandStack, offhandX + itemOffsetX, slotY + itemOffsetY, itemSize);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
            if (offhandStack.getCount() > 1) {
               String count = String.valueOf(offhandStack.getCount());
               Font countFont = Fonts.REGULAR.getFont(6.0F);
               float textX = offhandX + slotWidth - countFont.width(count) - 2.0F;
               float textY = slotY + slotHeight - countFont.height() - 1.0F;
               context.drawText(countFont, count, textX + 0.5F, textY + 0.5F, ColorRGBA.BLACK.withAlpha(150.0F));
               context.drawText(countFont, count, textX, textY, ColorRGBA.WHITE);
            }
         }

         context.drawShadow(
            hotbarBlockX - 5.0F,
            this.y - 5.0F,
            hotbarBlockWidth + 10.0F,
            this.height + 10.0F,
            15.0F,
            BorderRadius.all(6.0F),
            ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.getValue())
         );
         if (Interface.showMinimalizm()) {
            context.drawBlurredRect(
               hotbarBlockX,
               this.y,
               hotbarBlockWidth,
               this.height,
               11.25F,
               7.0F,
               BorderRadius.all(6.0F),
               ColorRGBA.WHITE.withAlpha(255.0F * this.animation.getValue() * Interface.minimalizm())
            );
         }

         if (Interface.showGlass()) {
            context.drawLiquidGlass(
               hotbarBlockX,
               this.y,
               hotbarBlockWidth,
               this.height,
               7.0F,
               Interface.getDistortion() - 0.07F * this.dragAnim.getValue(),
               BorderRadius.all(6.0F),
               Colors.getLiquidGlassColor().withAlpha(255.0F * this.animation.getValue() * Interface.glass())
            );
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
         context.drawRoundedRect(hotbarBlockX, this.y, hotbarBlockWidth, this.height, BorderRadius.all(6.0F), bgColor);
         int selectedSlot = mc.player.getInventory().selectedSlot;

         for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            float slotX = hotbarStartX + i * (slotWidth + spacing);
            boolean isSelected = i == selectedSlot;
            if (isSelected) {
               context.drawRoundedRect(slotX, slotY, slotWidth, slotHeight, BorderRadius.all(6.0F), ColorRGBA.BLACK.withAlpha(80.0F));
            }

            if (!stack.isEmpty()) {
               float itemSize = 0.85F;
               float itemWidth = 16.0F * itemSize;
               float itemOffsetX = (slotWidth - itemWidth) / 2.0F;
               float itemOffsetY = (slotHeight - itemWidth) / 2.0F;
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
               context.drawItem(stack, slotX + itemOffsetX, slotY + itemOffsetY, itemSize);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
               if (stack.getCount() > 1) {
                  String count = String.valueOf(stack.getCount());
                  Font countFont = Fonts.REGULAR.getFont(6.0F);
                  float textX = slotX + slotWidth - countFont.width(count) - 2.0F;
                  float textY = slotY + slotHeight - countFont.height() - 1.0F;
                  context.drawText(countFont, count, textX + 0.5F, textY + 0.5F, ColorRGBA.BLACK.withAlpha(150.0F));
                  context.drawText(countFont, count, textX, textY, ColorRGBA.WHITE);
               }
            }
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   public boolean show() {
      return false;
   }
}
