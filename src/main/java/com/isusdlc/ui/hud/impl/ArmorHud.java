package com.isusdlc.ui.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.hud.HudEditorScreen;
import com.isusdlc.ui.hud.HudElement;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.screen.ChatScreen;

public class ArmorHud extends HudElement {
   private final BooleanSetting alwaysDisplay = new BooleanSetting(this, "hud.always_display").enabled(true);

   public ArmorHud() {
      super("hud.armor", "icons/hud/armor.png");
   }

   @Override
   public void update(UIContext context) {
      this.width = 81.0F;
      this.height = 15.0F;
      super.update(context);
   }

   @Override
   protected void renderComponent(UIContext context) {
      if (mc.player != null && mc.world != null) {
         Font font = Fonts.SEMIBOLD.getFont(5.5F);
         boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
         ColorRGBA bgColor = Colors.getBackgroundColor().withAlpha(255.0F * (dark ? 0.8F - 0.6F * Interface.glass() : 0.7F));
         float slotWidth = 18.0F;
         float slotHeight = 15.0F;
         float spacing = 3.0F;
         float startX = this.x;
         float slotY = this.y;
         float prev = RenderSystem.getShaderColor()[3];
         ItemStack[] armorItems = new ItemStack[4];

         for (int i = 0; i < 4; i++) {
            armorItems[i] = mc.player.getInventory().getArmorStack(3 - i);
         }

         for (int i = 0; i < 4; i++) {
            ItemStack stack = armorItems[i];
            float slotX = startX + i * (slotWidth + spacing);
            context.drawShadow(
               slotX - 5.0F,
               slotY - 5.0F,
               slotWidth + 10.0F,
               slotHeight + 10.0F,
               15.0F,
               BorderRadius.all(4.0F),
               ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.getValue())
            );
            if (Interface.showMinimalizm()) {
               context.drawBlurredRect(
                  slotX,
                  slotY,
                  slotWidth,
                  slotHeight,
                  11.25F,
                  7.0F,
                  BorderRadius.all(4.0F),
                  ColorRGBA.WHITE.withAlpha(255.0F * this.animation.getValue() * Interface.minimalizm())
               );
            }

            if (Interface.showGlass()) {
               context.drawLiquidGlass(
                  slotX,
                  slotY,
                  slotWidth,
                  slotHeight,
                  7.0F,
                  Interface.getDistortion() - 0.07F * this.dragAnim.getValue(),
                  BorderRadius.all(4.0F),
                  Colors.getLiquidGlassColor().withAlpha(255.0F * this.animation.getValue() * Interface.glass())
               );
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
            context.drawRoundedRect(slotX, slotY, slotWidth, slotHeight, BorderRadius.all(4.0F), bgColor);
            if (!stack.isEmpty()) {
               float itemSize = 0.75F;
               float itemWidth = 16.0F * itemSize;
               float itemOffsetX = (slotWidth - itemWidth) / 2.0F;
               float itemOffsetY = (slotHeight - itemWidth) / 2.0F;
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
               context.drawItem(stack, slotX + itemOffsetX, slotY + itemOffsetY, itemSize);
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
               int maxDamage = stack.getMaxDamage();
               if (maxDamage > 0) {
                  int damage = stack.getDamage();
                  int durability = maxDamage - damage;
                  int percentage = durability * 100 / maxDamage;
                  String percentText = percentage + "%";
                  float textWidth = font.width(percentText);
                  float textHeight = font.height();
                  float textX = slotX + (slotWidth - textWidth) / 2.0F;
                  float textY = slotY + (slotHeight - textHeight) / 2.0F;
                  context.drawText(font, percentText, textX + 0.5F, textY + 0.5F, ColorRGBA.BLACK.withAlpha(150.0F));
                  ColorRGBA textColor = percentage > 50
                     ? ColorRGBA.WHITE
                     : (percentage > 25 ? new ColorRGBA(255.0F, 200.0F, 0.0F, 255.0F) : new ColorRGBA(255.0F, 50.0F, 50.0F, 255.0F));
                  context.drawText(font, percentText, textX, textY, textColor);
               }
            }
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   public boolean show() {
      if (!Interface.showArmorHud()) {
         return false;
      }

      if (mc.player == null || mc.world == null) {
         return false;
      } else if (!(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof HudEditorScreen)) {
         boolean hasArmor = false;

         for (int i = 0; i < 4; i++) {
            if (!mc.player.getInventory().getArmorStack(i).isEmpty()) {
               hasArmor = true;
               break;
            }
         }

         return hasArmor || this.alwaysDisplay.isEnabled();
      } else {
         return true;
      }
   }
}
