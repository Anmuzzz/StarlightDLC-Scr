package com.isusdlc.ui.mainmenu;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomScreen;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.mixin.minecraft.client.IMinecraftClient;
import com.isusdlc.systems.modules.modules.other.NameProtect;
import com.isusdlc.ui.components.textfield.TextField;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.TextUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.gui.GuiUtility;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.session.Session;

import java.util.Optional;
import java.util.UUID;

public class NicknameScreen extends CustomScreen implements IMinecraft {
   private final Screen parent;
   private final TextField nicknameField;

   public NicknameScreen(Screen parent) {
      this.parent = parent;
      Font fieldFont = Fonts.MEDIUM.getFont(8.0F);
      this.nicknameField = new TextField(fieldFont);
      this.nicknameField.setPreview("Введите имя аккаунта");
      this.nicknameField.setTextColor(Colors.getTextColor());

      NameProtect nameProtect = elegant.getInstance().getModuleManager().getModule(NameProtect.class);
      if (nameProtect != null && nameProtect.getFakeName().getText() != null) {
         String current = nameProtect.getFakeName().getText();
         if (!current.isEmpty()) {
            this.nicknameField.clear();
            this.nicknameField.paste(current);
         }
      }
   }

   @Override
   public void render(UIContext context) {
      context.drawRect(0.0F, 0.0F, this.width, this.height, ColorRGBA.BLACK.withAlpha(140.0F));

      float panelWidth = 260.0F;
      float panelHeight = 130.0F;
      float x = (this.width - panelWidth) / 2.0F;
      float y = (this.height - panelHeight) / 2.0F;
      context.drawShadow(x - 5.0F, y - 5.0F, panelWidth + 10.0F, panelHeight + 10.0F, 18.0F, BorderRadius.all(10.0F), ColorRGBA.BLACK.withAlpha(80.0F));
      context.drawSquircle(
         x,
         y,
         panelWidth,
         panelHeight,
         10.0F,
         BorderRadius.all(10.0F),
         Colors.getBackgroundColor().withAlpha(230.0F)
      );

      Font titleFont = Fonts.MEDIUM.getFont(9.0F);
      Font labelFont = Fonts.REGULAR.getFont(7.5F);
      Font buttonFont = Fonts.MEDIUM.getFont(7.5F);

      context.drawCenteredText(titleFont, "Смена ника", this.width / 2.0F, y + 10.0F, Colors.getTextColor());

      if (mc.player != null) {
         float headSize = 32.0F;
         float headX = this.width / 2.0F - headSize / 2.0F;
         float headY = y - headSize / 2.0F - 6.0F;
         context.drawHead(mc.player, headX, headY, headSize, BorderRadius.all(6.0F), ColorRGBA.WHITE);
      }

      float fieldTop = y + 32.0F;
      context.drawText(labelFont, "Новый ник:", x + 14.0F, fieldTop - 10.0F, Colors.getTextColor().withAlpha(220.0F));

      float fieldHeight = 18.0F;
      this.nicknameField.setX(x + 14.0F);
      this.nicknameField.setY(fieldTop);
      this.nicknameField.setWidth(panelWidth - 28.0F);
      this.nicknameField.setHeight(fieldHeight);
      ColorRGBA inputBg = Colors.getBackgroundColor().withAlpha(210.0F);
      context.drawRoundedRect(
         this.nicknameField.getX(),
         this.nicknameField.getY(),
         this.nicknameField.getWidth(),
         this.nicknameField.getHeight(),
         BorderRadius.all(6.0F),
         inputBg
      );
      if (this.nicknameField.isFocused()) {
         context.drawRoundedBorder(
            this.nicknameField.getX() - 1.0F,
            this.nicknameField.getY() - 1.0F,
            this.nicknameField.getWidth() + 2.0F,
            this.nicknameField.getHeight() + 2.0F,
            0.6F,
            BorderRadius.all(7.0F),
            Colors.getAccentColor().withAlpha(130.0F)
         );
      }
      this.nicknameField.render(context);


      float buttonsY = y + panelHeight - 36.0F;
      float buttonWidth = (panelWidth - 14.0F * 3.0F) / 2.0F;
      float randomX = x + 14.0F;
      float applyX = randomX + buttonWidth + 14.0F;
      float buttonHeight = 18.0F;

      drawButton(context, randomX, buttonsY, buttonWidth, buttonHeight, "Рандом", buttonFont);
      drawButton(context, applyX, buttonsY, buttonWidth, buttonHeight, "Сохранить", buttonFont);


      float backY = buttonsY + buttonHeight + 8.0F;
      float backWidth = 60.0F;
      float backX = x + panelWidth - backWidth - 14.0F;
      drawButton(context, backX, backY, backWidth, 14.0F, "Назад", buttonFont);
   }

   private void drawButton(UIContext context, float x, float y, float w, float h, String text, Font font) {
      boolean hovered = isHovered(x, y, w, h, context);
      ColorRGBA bg = Colors.getBackgroundColor().mix(ColorRGBA.WHITE, hovered ? 0.15F : 0.0F).withAlpha(200.0F);
      context.drawRoundedRect(x, y, w, h, BorderRadius.all(6.0F), bg);
      context.drawCenteredText(font, text, x + w / 2.0F, y + GuiUtility.getMiddleOfBox(font.height(), h), Colors.getTextColor());
   }

   private boolean isHovered(float x, float y, float w, float h, UIContext context) {
      double mx = context.getMouseX();
      double my = context.getMouseY();
      return mx >= x && mx <= x + w && my >= y && my <= y + h;
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      this.nicknameField.onMouseClicked(mouseX, mouseY, button);
      if (button == MouseButton.LEFT) {
         float panelWidth = 260.0F;
         float panelHeight = 130.0F;
         float x = (this.width - panelWidth) / 2.0F;
         float y = (this.height - panelHeight) / 2.0F;
         float buttonsY = y + panelHeight - 36.0F;
         float buttonWidth = (panelWidth - 14.0F * 3.0F) / 2.0F;
         float randomX = x + 14.0F;
         float applyX = randomX + buttonWidth + 14.0F;
         float buttonHeight = 18.0F;


         if (mouseX >= randomX && mouseX <= randomX + buttonWidth && mouseY >= buttonsY && mouseY <= buttonsY + buttonHeight) {
            this.nicknameField.clear();
            this.nicknameField.paste(TextUtility.getRandomNick());
         }


          if (mouseX >= applyX && mouseX <= applyX + buttonWidth && mouseY >= buttonsY && mouseY <= buttonsY + buttonHeight) {
             String nick = this.nicknameField.getBuiltText().trim();
             if (!nick.isEmpty()) {
                NameProtect nameProtect = elegant.getInstance().getModuleManager().getModule(NameProtect.class);
                if (nameProtect != null) {
                   nameProtect.getFakeName().setText(nick);
                   if (!nameProtect.isEnabled()) {
                      nameProtect.enable();
                   }
                }

                ((IMinecraftClient) mc).setSession(
                   new Session(nick, UUID.randomUUID(), "", Optional.empty(), Optional.empty(), Session.AccountType.MOJANG)
                );

                elegant.getInstance().getFileManager().saveClientFiles();
             }

             this.close();
         }


         float backY = buttonsY + buttonHeight + 8.0F;
         float backWidth = 60.0F;
         float backX = x + panelWidth - backWidth - 14.0F;
         if (mouseX >= backX && mouseX <= backX + backWidth && mouseY >= backY && mouseY <= backY + 14.0F) {
            this.close();
         }
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      this.nicknameField.onMouseReleased(mouseX, mouseY, button);
      super.onMouseReleased(mouseX, mouseY, button);
   }

   public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
      this.nicknameField.onKeyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      this.nicknameField.charTyped(chr, modifiers);
      return super.charTyped(chr, modifiers);
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public void close() {
      if (TextField.LAST_FIELD != null) {
         TextField.LAST_FIELD.setFocused(false);
      }

      super.close();
      mc.setScreen(this.parent);
   }
}

