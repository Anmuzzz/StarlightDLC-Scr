package com.isusdlc.ui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.systems.setting.SettingsContainer;
import com.isusdlc.ui.components.popup.Popup;
import com.isusdlc.ui.hud.impl.island.DynamicIsland;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.interfaces.IScaledResolution;
import com.isusdlc.utility.render.RenderUtility;

public abstract class HudElement implements SettingsContainer, IMinecraft {
   protected float x;
   protected float y;
   protected float width;
   protected float height;
   protected final Animation animation = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   protected final Animation visible = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   protected final Animation selecting = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   protected final Animation dragAnim = new Animation(300L, 0.0F, Easing.FIGMA_EASE_IN_OUT);
   private final Animation blurAnim = new Animation(300L, 0.0F, Easing.FIGMA_EASE_IN_OUT);
   private final Animation loadingAnim = new Animation(700L, 0.0F, Easing.SMOOTH_STEP);
   private final Animation widthAnim = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   private final Animation heightAnim = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   protected boolean showing;
   protected boolean select;
   private List<Setting> settings = new ArrayList<>();
   protected boolean dragging;
   private float dragX;
   private float dragY;
   protected float startDragX;
   protected float startDragY;
   protected final String name;
   protected final String icon;

   public HudElement(String name, String icon) {
      this.name = name;
      this.icon = icon;
   }

   private void saveClientData() {
      try {
         elegant.getInstance().getFileManager().saveClientFiles();
      } catch (Exception var2) {
         elegant.LOGGER.error("Failed to save HUD element data: {}", var2.getMessage());
      }
   }

   public void render(UIContext context) {
      this.update(context);
      float anim = this.animation.getValue() * this.visible.getValue();
      if (anim != 0.0F) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, anim));
         float scale = 0.5F + anim * 0.5F - 0.05F * this.selecting.getValue();
         RenderUtility.scale(context.getMatrices(), this.x + this.width / 2.0F, this.y + this.height / 2.0F, scale);
         this.renderComponent(context);
         RenderUtility.end(context.getMatrices());
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   protected abstract void renderComponent(UIContext var1);

   public void update(UIContext context) {
      float oldWidth = this.widthAnim.getValue();
      this.widthAnim.update(this.width);
      float newWidth = this.widthAnim.getValue();
      float widthDelta = newWidth - oldWidth;
      boolean isLeftSide = this.x + this.width / 2.0F < IScaledResolution.sr.getScaledWidth() / 2.0F;
      if (!isLeftSide && widthDelta != 0.0F && this.showing && this.visible.getValue() > 0.5F) {
         this.x -= widthDelta;
      }

      if (widthDelta != 0.0F && this.showing && this.visible.getValue() > 0.5F) {
         for (HudElement otherElement : elegant.getInstance().getHud().getElements()) {
            if (otherElement != this && otherElement.isShowing() && otherElement.visible.getValue() > 0.5F) {
               float verticalOverlap = Math.min(this.y + this.height, otherElement.y + otherElement.height) - Math.max(this.y, otherElement.y);
               if (!(verticalOverlap <= 0.0F)) {
                  if (isLeftSide) {
                     float rightEdge = this.x + newWidth;
                     float distanceToOther = otherElement.x - rightEdge;
                     if (distanceToOther >= -5.0F && distanceToOther <= 25.0F) {
                        otherElement.x += widthDelta;
                        otherElement.x = Math.max(0.0F, Math.min(otherElement.x, IScaledResolution.sr.getScaledWidth() - otherElement.width));
                     }
                  } else {
                     float leftEdge = this.x;
                     float distanceToOther = leftEdge - (otherElement.x + otherElement.width);
                     if (distanceToOther >= -5.0F && distanceToOther <= 25.0F) {
                        otherElement.x -= widthDelta;
                        otherElement.x = Math.max(0.0F, Math.min(otherElement.x, IScaledResolution.sr.getScaledWidth() - otherElement.width));
                     }
                  }
               }
            }
         }
      }

      this.width = newWidth;
      this.dragAnim.update(this.dragging);
      this.animation.setEasing(this.showing ? Easing.BAKEK : Easing.BAKEK_BACK);
      this.animation.update(this.showing);
      this.visible.setEasing(this.show() ? Easing.BAKEK : Easing.BAKEK_BACK);
      this.visible.update(this.show());
      this.selecting.update(this.select);
      this.blurAnim.update(this.animation.getValue() >= 0.6F);
      if (this.dragging) {
         this.x = Math.clamp(context.getMouseX() - this.dragX, 0.0F, IScaledResolution.sr.getScaledWidth() - this.width);
         this.y = Math.clamp(context.getMouseY() - this.dragY, 0.0F, IScaledResolution.sr.getScaledHeight() - this.height);
         if (!(this instanceof DynamicIsland)) {
            for (GridLine line : elegant.getInstance().getHud().getGrid().getLines()) {
               if (line.getType() == GridLine.Type.VERTICAL) {
                  this.x = this.snapToLine(line, this.x, List.of(0.0F, this.width, this.width / 2.0F), List.of(0.0F, -this.width, -this.width / 2.0F));
               } else {
                  this.y = this.snapToLine(line, this.y, List.of(0.0F, this.height), List.of(0.0F, -this.height));
               }
            }
         }
      }

      if (this.isHovered(context) && this.animation.getValue() >= 1.0F) {
         CursorUtility.set(CursorType.HAND);
      }
   }

   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (this.isHovered(mouseX, mouseY) && this.showing) {
         if (button == MouseButton.LEFT) {
            this.dragging = true;
            this.dragX = (float)(mouseX - this.x);
            this.dragY = (float)(mouseY - this.y);
            this.startDragX = this.x;
            this.startDragY = this.y;
         } else if (button == MouseButton.RIGHT) {
            this.select = true;
            this.loadingAnim.setValue(0.0F);
            Popup popup = new Popup((float)mouseX, (float)mouseY, 110.0F, 6.0F).title(this.settings.isEmpty() ? "actions" : "settings").separator();

            for (Setting setting : this.settings) {
               popup.setting(setting);
            }

            popup.button(Localizator.translate("remove"), "icons/hud/trash.png", popup1 -> {
               this.showing = false;
               popup1.setShowing(false);
               elegant.getInstance().getFileManager().writeFile("client");
            }).onClose(() -> this.select = false);
            elegant.getInstance().getHud().getPopups().add(popup);
         }
      }
   }

   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      if (this.dragging && button == MouseButton.LEFT) {
         this.dragging = false;
         if (this.x != this.startDragX || this.y != this.startDragY) {
            elegant.getInstance().getHud().getHistoryManager().registerMove(this, this.startDragX, this.startDragY, this.x, this.y);
         }

         elegant.getInstance().getFileManager().writeFile("client");
      }
   }

   private float snapToLine(GridLine line, float pos, List<Float> offsets, List<Float> adjustments) {
      for (int i = 0; i < offsets.size(); i++) {
         float distance = Math.abs(pos + offsets.get(i) - line.getPos());
         if (distance < 25.0F) {
            line.setActive(true);
         }

         if (distance < 5.0F) {
            pos = line.getPos() + adjustments.get(i);
         }
      }

      return pos;
   }

   public boolean show() {
      return true;
   }

   public boolean isHovered(float mouseX, float mouseY) {
      return GuiUtility.isHovered((double)this.x, (double)this.y, (double)this.width, (double)this.height, (double)mouseX, (double)mouseY);
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return GuiUtility.isHovered((double)this.x, (double)this.y, (double)this.width, (double)this.height, mouseX, mouseY);
   }

   public boolean isHovered(UIContext context) {
      return this.isHovered((float)context.getMouseX(), (float)context.getMouseY());
   }

   public void pos(float x, float y) {
      this.x = x;
      this.y = y;
   }

   @Generated
   public float getX() {
      return this.x;
   }

   @Generated
   public float getY() {
      return this.y;
   }

   @Generated
   public float getWidth() {
      return this.width;
   }

   @Generated
   public float getHeight() {
      return this.height;
   }

   @Generated
   public Animation getAnimation() {
      return this.animation;
   }

   @Generated
   public Animation getVisible() {
      return this.visible;
   }

   @Generated
   public Animation getSelecting() {
      return this.selecting;
   }

   @Generated
   public Animation getDragAnim() {
      return this.dragAnim;
   }

   @Generated
   public Animation getBlurAnim() {
      return this.blurAnim;
   }

   @Generated
   public Animation getLoadingAnim() {
      return this.loadingAnim;
   }

   @Generated
   public Animation getWidthAnim() {
      return this.widthAnim;
   }

   @Generated
   public Animation getHeightAnim() {
      return this.heightAnim;
   }

   @Generated
   public boolean isShowing() {
      return this.showing;
   }

   @Generated
   public boolean isSelect() {
      return this.select;
   }

   @Generated
   @Override
   public List<Setting> getSettings() {
      return this.settings;
   }

   @Generated
   public boolean isDragging() {
      return this.dragging;
   }

   @Generated
   public float getStartDragY() {
      return this.startDragY;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public String getIcon() {
      return this.icon;
   }

   @Generated
   public void setX(float x) {
      this.x = x;
   }

   @Generated
   public void setY(float y) {
      this.y = y;
   }

   @Generated
   public void setWidth(float width) {
      this.width = width;
   }

   @Generated
   public void setHeight(float height) {
      this.height = height;
   }

   public void setShowing(boolean showing) {
      if (this.showing != showing) {
         this.showing = showing;
         this.saveClientData();
      }
   }

   @Generated
   public void setSelect(boolean select) {
      this.select = select;
   }

   @Generated
   public void setSettings(List<Setting> settings) {
      this.settings = settings;
   }

}
