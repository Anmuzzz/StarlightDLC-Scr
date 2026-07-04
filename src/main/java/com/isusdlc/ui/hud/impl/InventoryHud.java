package com.isusdlc.ui.hud.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.hud.HudEditorScreen;
import com.isusdlc.ui.hud.HudElement;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.screen.ChatScreen;

public class InventoryHud extends HudElement {
   private final BooleanSetting alwaysDisplay = new BooleanSetting(this, "hud.always_display");
   private final Animation content = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   private final Animation[][] itemAnims = new Animation[3][9];

   public InventoryHud() {
      super("hud.inventory", "icons/hud/inventory.png");

      for (int row = 0; row < 3; row++) {
         for (int col = 0; col < 9; col++) {
            this.itemAnims[row][col] = new Animation(300L, 0.0F, Easing.BAKEK);
         }
      }
   }

   @Override
   public void update(UIContext context) {
      super.update(context);
      int rows = 3;
      this.width = 147.0F;
      this.height = 18.0F + rows * 13.0F + (rows - 1) * 2.0F + 7.0F;
   }

   @Override
   protected void renderComponent(UIContext context) {
      if (mc.player != null) {
         Font font = Fonts.MEDIUM.getFont(7.0F);
         boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
         ColorRGBA bgColor = Colors.getBackgroundColor().withAlpha(255.0F * (dark ? 0.8F - 0.6F * Interface.glass() : 0.7F));
         this.content.update(this.animation.getValue() * this.visible.getValue() >= 1.0F);
         if (this.animation.getValue() != 0.0F) {
            float prev = RenderSystem.getShaderColor()[3];
            context.drawShadow(
               this.x - 5.0F,
               this.y - 5.0F,
               this.width + 10.0F,
               this.height + 10.0F,
               15.0F,
               BorderRadius.all(6.0F),
               ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.getValue())
            );
            if (Interface.showMinimalizm()) {
               context.drawBlurredRect(
                  this.x,
                  this.y,
                  this.width,
                  this.height,
                  11.25F,
                  7.0F,
                  BorderRadius.all(6.0F),
                  ColorRGBA.WHITE.withAlpha(255.0F * this.animation.getValue() * Interface.minimalizm())
               );
            }

            if (Interface.showGlass()) {
               context.drawLiquidGlass(
                  this.x,
                  this.y,
                  this.width,
                  this.height,
                  7.0F,
                  Interface.getDistortion() - 0.07F * this.dragAnim.getValue(),
                  BorderRadius.all(6.0F),
                  Colors.getLiquidGlassColor().withAlpha(255.0F * this.animation.getValue() * Interface.glass())
               );
            }

            context.drawSquircle(this.x, this.y, this.width, this.height, 7.0F, BorderRadius.all(6.0F), bgColor);
            float alpha = 255.0F * this.content.getValue();
            float headerHeight = 18.0F;
            float iconSize = 12.0F;
            context.drawTexture(elegant.id(this.icon), this.x + 7.0F, this.y + 4.0F, iconSize, iconSize, Colors.getTextColor().withAlpha(alpha));
            context.drawText(
               font,
               Localizator.translate("hud.inventory"),
               this.x + 7.0F + iconSize + 4.0F,
               this.y + GuiUtility.getMiddleOfBox(font.height(), headerHeight) + 0.5F,
               Colors.getTextColor().withAlpha(alpha)
            );
            context.drawRect(this.x, this.y + headerHeight, this.width, 0.5F, Colors.getTextColor().withAlpha(30.0F * this.content.getValue()));
            float startY = this.y + headerHeight + 4.5F;
            float startX = this.x + 7.0F;
            float itemSize = 13.0F;
            float gap = 2.0F;

            for (int row = 0; row < 3; row++) {
               for (int col = 0; col < 9; col++) {
                  int slotIndex = 9 + row * 9 + col;
                  ItemStack stack = mc.player.getInventory().getStack(slotIndex);
                  boolean hasItem = !stack.isEmpty();
                  this.itemAnims[row][col].update(true);
                  float itemAnim = this.content.getValue() * this.itemAnims[row][col].getValue();
                  if (itemAnim > 0.0F) {
                     float itemX = startX + col * (itemSize + gap);
                     float itemY = startY + row * (itemSize + gap);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev * itemAnim);
                     context.drawBlurredRect(
                        itemX, itemY, itemSize, itemSize, 1.25F, BorderRadius.all(1.5F), ColorRGBA.WHITE.withAlpha(255.0F * this.animation.getValue())
                     );
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
                     ColorRGBA slotBgColor = hasItem ? bgColor : bgColor.mix(ColorRGBA.BLACK, 0.4F);
                     context.drawRoundedRect(itemX, itemY, itemSize, itemSize, BorderRadius.all(1.5F), slotBgColor.withAlpha(slotBgColor.getAlpha() * itemAnim));
                     context.drawRoundedBorder(
                        itemX, itemY, itemSize, itemSize, 0.3F, BorderRadius.all(1.5F), Colors.getTextColor().withAlpha(50.0F * itemAnim)
                     );
                     if (hasItem) {
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev * itemAnim);
                        context.drawItem(stack, itemX - 11.0F + itemSize / 2.0F + 5.5F, itemY, 0.7F);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, prev);
                        if (stack.getCount() > 1) {
                           String count = String.valueOf(stack.getCount());
                           Font countFont = Fonts.REGULAR.getFont(6.0F);
                           context.drawRightText(
                              countFont, count, itemX + itemSize - 2.0F, itemY + itemSize - countFont.height() - 1.0F, Colors.WHITE.withAlpha(alpha * itemAnim)
                           );
                        }
                     }
                  }
               }
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }
   }

   @Override
   public boolean show() {
      if (!Interface.showInventoryHud()) {
         return false;
      }

      if (mc.player == null) {
         return false;
      } else if (!(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof HudEditorScreen)) {
         if (this.alwaysDisplay.isEnabled()) {
            return true;
         } else {
            for (int i = 9; i < 36; i++) {
               if (!mc.player.getInventory().getStack(i).isEmpty()) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }
}
