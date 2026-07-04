package com.isusdlc.ui.mainmenu;

import java.util.ArrayList;
import java.util.List;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomScreen;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.modules.other.Sounds;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.game.TextUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.math.MathUtility;
import com.isusdlc.utility.render.DrawUtility;
import com.isusdlc.utility.render.obj.Rect;
import com.isusdlc.utility.sounds.ClientSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import ru.kotopushka.compiler.sdk.annotations.Compile;
import ru.kotopushka.compiler.sdk.annotations.VMProtect;
import ru.kotopushka.compiler.sdk.enums.VMProtectType;

public class CustomTitleScreen extends CustomScreen implements IMinecraft {
   private static boolean once;
   private static final String[] BUTTON_LABELS = new String[]{
      "SinglePlayer",
      "MultiPlayer",
      "Alt Manager",
      "Options",
      "Quit"
   };
   private static final int HEADER_BTN_SIZE = 24;
   private static final int HEADER_BTN_GAP = 8;

   @Compile
   @VMProtect(
      type = VMProtectType.MUTATION
   )
   protected void init() {
      if (!once) {
         if (elegant.getInstance().getModuleManager().getModule(Sounds.class).isEnabled()) {
            ClientSounds.WELCOME.play(elegant.getInstance().getModuleManager().getModule(Sounds.class).getVolume().getCurrentValue());
         }
         once = true;
      }

      super.init();
   }

   @Override
   public void render(UIContext context) {
      if (Fonts.isInitialized()) {
         Font titleFont = Fonts.ROUND_BOLD.getFont(20.0F);
         Font subtitleFont = Fonts.MEDIUM.getFont(10.0F);
         Font buttonFont = Fonts.MEDIUM.getFont(9.0F);

         context.drawRoundedRect(0.0F, 0.0F, this.width, this.height, BorderRadius.ZERO, new ColorRGBA(0.0F, 0.0F, 0.0F, 140.0F));
         DrawUtility.blurProgram.draw();

         float buttonWidth = 120.0F;
         float buttonHeight = 16.0F;
         float buttonOffset = 4.5F;
         int buttonCount = BUTTON_LABELS.length;
         float totalButtonsHeight = buttonCount * buttonHeight + (buttonCount - 1) * buttonOffset;

         float iconSize = 22.0F;
         float spaceIconToGreet = 6.0F;
         float greetHeight = subtitleFont.height();
         float spaceGreetToButtons = 10.0F;
         float totalHeight = iconSize + spaceIconToGreet + greetHeight + spaceGreetToButtons + totalButtonsHeight;

         float topY = (this.height - totalHeight) / 2.0F;

         ColorRGBA logoColor = new ColorRGBA(139.0F, 0F, 255.0F);
         context.drawCenteredText(
            titleFont,
            "StarlightDLC",
            this.width / 2.0F,
            topY + iconSize / 4.0F,
            logoColor
         );

         String lang = mc.getLanguageManager().getLanguage();
         java.time.LocalTime now = java.time.LocalTime.now();
         String timeOfDay;
         if (now.isBefore(java.time.LocalTime.of(6, 0))) {
            timeOfDay = lang.equals("ru_ru") ? "Доброй ночи" : "Good night";
         } else if (now.isBefore(java.time.LocalTime.NOON)) {
            timeOfDay = lang.equals("ru_ru") ? "Доброе утро" : "Good morning";
         } else if (now.isBefore(java.time.LocalTime.of(18, 0))) {
            timeOfDay = lang.equals("ru_ru") ? "Добрый день" : "Good afternoon";
         } else {
            timeOfDay = lang.equals("ru_ru") ? "Добрый вечер" : "Good evening";
         }

         String username = mc.getSession() != null ? mc.getSession().getUsername() : "Player";
         String greet = timeOfDay + ", " + username + "!";

         float greetY = topY + iconSize + spaceIconToGreet;
         context.drawCenteredText(
            subtitleFont,
            greet,
            this.width / 2.0F,
            greetY,
            ColorRGBA.WHITE
         );

         float startY = greetY + greetHeight + spaceGreetToButtons;
         float x = this.width / 2.0F - buttonWidth / 2.0F;

         float mouseX = (float)context.getMouseX();
         float mouseY = (float)context.getMouseY();

         for (int i = 0; i < buttonCount; i++) {
            float y = startY + i * (buttonHeight + buttonOffset);
            boolean hovered = mouseX >= x && mouseX <= x + buttonWidth && mouseY >= y && mouseY <= y + buttonHeight;

            ColorRGBA baseButton = new ColorRGBA(15.0F, 15.0F, 15.0F, hovered ? 230.0F : 195.0F);
            context.drawRoundedRect(x, y, buttonWidth, buttonHeight, BorderRadius.all(7.0F), baseButton);

            context.drawCenteredText(
               buttonFont,
               BUTTON_LABELS[i],
               this.width / 2.0F,
               y + buttonHeight / 2.0F - buttonFont.height() / 2.0F + 0.5F,
               ColorRGBA.WHITE
            );
         }

         Font headerIconFont = Fonts.ROUND_BOLD.getFont(13.0F);
         float headerBtnY = 12.0F;
         float headerRightX = this.width - 12.0F;
         for (int i = 0; i < 3; i++) {
            float bx = headerRightX - (i + 1) * (HEADER_BTN_SIZE + HEADER_BTN_GAP) + HEADER_BTN_GAP;
            boolean hov = mouseX >= bx && mouseX <= bx + HEADER_BTN_SIZE && mouseY >= headerBtnY && mouseY <= headerBtnY + HEADER_BTN_SIZE;
            ColorRGBA btnBg = new ColorRGBA(15, 15, 15, hov ? 230 : 180);
            context.drawRoundedRect(bx, headerBtnY, HEADER_BTN_SIZE, HEADER_BTN_SIZE, BorderRadius.all(7), btnBg);
            String label = switch (i) {
               case 0 -> "Ч";
               case 1 -> "F";
               case 2 -> "⚙";
               default -> "";
            };
            context.drawCenteredText(headerIconFont, label, bx + HEADER_BTN_SIZE / 2, headerBtnY + HEADER_BTN_SIZE / 2 - headerIconFont.height() / 2 + 0.5F, ColorRGBA.WHITE);
         }
      }
   }

   @Compile
   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      float buttonWidth = 120.0F;
      float buttonHeight = 16.0F;
      float buttonOffset = 4.5F;
      int buttonCount = BUTTON_LABELS.length;
      float totalButtonsHeight = buttonCount * buttonHeight + (buttonCount - 1) * buttonOffset;

      float iconSize = 22.0F;
      float spaceIconToGreet = 6.0F;
      float greetHeight = Fonts.MEDIUM.getFont(10.0F).height();
      float spaceGreetToButtons = 10.0F;
      float totalHeight = iconSize + spaceIconToGreet + greetHeight + spaceGreetToButtons + totalButtonsHeight;
      float topY = (this.height - totalHeight) / 2.0F;
      float greetY = topY + iconSize + spaceIconToGreet;
      float startY = greetY + greetHeight + spaceGreetToButtons;
      float x = this.width / 2.0F - buttonWidth / 2.0F;

      float headerBtnY = 12.0F;
      float headerRightX = this.width - 12.0F;
      for (int i = 0; i < 3; i++) {
         float bx = headerRightX - (i + 1) * (HEADER_BTN_SIZE + HEADER_BTN_GAP) + HEADER_BTN_GAP;
         boolean headerHovered = mouseX >= bx && mouseX <= bx + HEADER_BTN_SIZE && mouseY >= headerBtnY && mouseY <= headerBtnY + HEADER_BTN_SIZE;
         if (headerHovered && button == MouseButton.LEFT) {
            switch (i) {
               case 0 -> mc.setScreen(new ChangelogScreen(this));
               case 1 -> mc.setScreen(new FriendsScreen(this));
               case 2 -> mc.setScreen(elegant.getInstance().getMenuScreen());
            }
            return;
         }
      }

      for (int i = 0; i < buttonCount; i++) {
         float y = startY + i * (buttonHeight + buttonOffset);
         boolean hovered = mouseX >= x && mouseX <= x + buttonWidth && mouseY >= y && mouseY <= y + buttonHeight;
         if (hovered && button == MouseButton.LEFT) {
            switch (i) {
               case 0 -> mc.setScreen(new SelectWorldScreen(this));
               case 1 -> mc.setScreen(new MultiplayerScreen(this));
               case 2 -> mc.setScreen(new NicknameScreen(this));
               case 3 -> mc.setScreen(new OptionsScreen(this, mc.options));
               case 4 -> mc.stop();
            }
            return;
         }
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Compile
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 69) {
         elegant.getInstance().getThemeManager().switchTheme();
      }

      if (Screen.hasControlDown() && keyCode == 82) {
         MinecraftClient.getInstance().setScreen(new MultiplayerScreen(this));
      }

      if (Screen.hasControlDown() && keyCode == 84) {
         MinecraftClient.getInstance().setScreen(new SelectWorldScreen(this));
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   private boolean shouldShowIsland() {
      return elegant.getInstance().getMusicTracker().haveActiveSession();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }
}
