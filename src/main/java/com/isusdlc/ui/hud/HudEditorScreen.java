package com.isusdlc.ui.hud;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.systems.event.impl.render.ChatRenderEvent;
import com.isusdlc.systems.event.impl.window.ChatClickEvent;
import com.isusdlc.systems.event.impl.window.ChatKeyPressEvent;
import com.isusdlc.systems.event.impl.window.ChatReleaseEvent;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class HudEditorScreen extends Screen {
   public HudEditorScreen() {
      super(Text.literal("HUD Editor"));
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatRenderEvent(CustomDrawContext.of(context), delta));
      context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("HUD Editor - Перетащите элементы мышью"), context.getScaledWindowWidth() / 2, 10, 16777215);
      context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("ESC или Right Shift - выход"), context.getScaledWindowWidth() / 2, 22, 11184810);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatClickEvent((float)mouseX, (float)mouseY, button));
      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatReleaseEvent((float)mouseX, (float)mouseY, button));
      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatKeyPressEvent(keyCode, scanCode, modifiers));
      if (keyCode != 256 && keyCode != 340) {
         return super.keyPressed(keyCode, scanCode, modifiers);
      } else {
         this.close();
         return true;
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }
}
