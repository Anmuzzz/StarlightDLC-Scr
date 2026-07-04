package com.isusdlc.systems.modules.constructions.swinganim;

import java.util.Collection;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomScreen;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.modules.constructions.swinganim.presets.PresetComponent;
import com.isusdlc.systems.modules.constructions.swinganim.presets.SwingPreset;
import com.isusdlc.systems.modules.constructions.swinganim.presets.SwingPresetFile;
import com.isusdlc.systems.modules.constructions.swinganim.presets.SwingPresetManager;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.ui.components.popup.Popup;
import com.isusdlc.ui.components.textfield.TextField;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.interfaces.IScaledResolution;
import net.minecraft.util.Hand;
import net.minecraft.client.gui.DrawContext;

public class SwingAnimScreen extends CustomScreen implements IScaledResolution, IMinecraft {
   private final Popup presets = new Popup(100.0F, 100.0F).title("presets");
   private final Popup shared = new Popup(100.0F, 100.0F).title("shared");
   private final Popup start = new Popup(300.0F, 100.0F).title("anim_from");
   private final Popup end = new Popup(500.0F, 100.0F).title("anim_to");

   public SwingAnimScreen() {
      SwingManager manager = elegant.getInstance().getSwingManager();
      elegant.getInstance().getSwingPresetManager().refresh();
      this.presets.add(new PresetComponent());
      this.applySettings(manager.getSharedSettings().settings, this.shared);
      this.applySettings(manager.getStartPhase().settings, this.start);
      this.applySettings(manager.getEndPhase().settings, this.end);
      SwingManager swingManager = elegant.getInstance().getSwingManager();
      SwingPresetManager presetManager = elegant.getInstance().getSwingPresetManager();
      String swing = swingManager.getCurrent();
      SwingPresetFile customPreset = presetManager.getPreset(swing);
      boolean isCustomPreset = customPreset != null;
      if (!isCustomPreset) {
         for (SwingPreset value : elegant.getInstance().getSwingManager().getPresets()) {
            if (value.getName().equals(swing)) {
               swingManager.getBezier().start(value.getBezierStart()).end(value.getBezierEnd());
               swingManager.getBack().enabled(value.isSwingBack());
               swingManager.getSpeed().setCurrentValue(value.getSpeed());
               SwingPhase start = swingManager.getStartPhase();
               start.getAnchorX().setCurrentValue(value.getFrom().getAnchorX());
               start.getAnchorY().setCurrentValue(value.getFrom().getAnchorY());
               start.getAnchorZ().setCurrentValue(value.getFrom().getAnchorZ());
               start.getMoveX().setCurrentValue(value.getFrom().getMoveX());
               start.getMoveY().setCurrentValue(value.getFrom().getMoveY());
               start.getMoveZ().setCurrentValue(value.getFrom().getMoveZ());
               start.getRotateX().setCurrentValue(value.getFrom().getRotateX());
               start.getRotateY().setCurrentValue(value.getFrom().getRotateY());
               start.getRotateZ().setCurrentValue(value.getFrom().getRotateZ());
               SwingPhase end = swingManager.getEndPhase();
               end.getAnchorX().setCurrentValue(value.getTo().getAnchorX());
               end.getAnchorY().setCurrentValue(value.getTo().getAnchorY());
               end.getAnchorZ().setCurrentValue(value.getTo().getAnchorZ());
               end.getMoveX().setCurrentValue(value.getTo().getMoveX());
               end.getMoveY().setCurrentValue(value.getTo().getMoveY());
               end.getMoveZ().setCurrentValue(value.getTo().getMoveZ());
               end.getRotateX().setCurrentValue(value.getTo().getRotateX());
               end.getRotateY().setCurrentValue(value.getTo().getRotateY());
               end.getRotateZ().setCurrentValue(value.getTo().getRotateZ());
               swingManager.setCurrent(swing);
               break;
            }
         }
      }
   }

   @Override
   public void render(UIContext context) {
      float startX = IScaledResolution.sr.getScaledWidth() / 2.0F - 360.0F + 4.0F;
      this.presets.setX(startX);
      startX += 180.0F;
      this.shared.setX(startX);
      startX += 180.0F;
      this.start.setX(startX);
      startX += 180.0F;
      this.end.setX(startX);
      this.popupEvent(popup -> popup.render(context));
      this.popupEvent(popup -> popup.setY(sr.getScaledHeight() / 2.0F - this.end.getHeight() / 2.0F));
      this.popupEvent(popup -> popup.setWidth(170.0F));
      if (mc.player.age % 20 == 0) {
         mc.player.swingHand(Hand.MAIN_HAND);
      }
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      this.popupEvent(popup -> popup.onMouseClicked(mouseX, mouseY, button));
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      this.popupEvent(popup -> popup.onMouseReleased(mouseX, mouseY, button));
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.popupEvent(popup -> popup.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount));
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      this.popupEvent(popup -> popup.onKeyPressed(keyCode, scanCode, modifiers));
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean charTyped(char chr, int modifiers) {
      this.popupEvent(popup -> popup.charTyped(chr, modifiers));
      return super.charTyped(chr, modifiers);
   }

   private void applySettings(Collection<Setting> settings, Popup target) {
      for (Setting setting : settings) {
         target.setting(setting);
      }
   }

   private void popupEvent(PopupEvent event) {
      event.call(this.presets);
      event.call(this.shared);
      event.call(this.start);
      event.call(this.end);
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public void close() {
      SwingPresetManager manager = elegant.getInstance().getSwingPresetManager();
      if (manager.getCurrent() != null) {
         manager.getCurrent().save();
      }

      if (TextField.LAST_FIELD != null) {
         TextField.LAST_FIELD.setFocused(false);
      }

      super.close();
      mc.setScreen(elegant.getInstance().getMenuScreen());
   }
}
