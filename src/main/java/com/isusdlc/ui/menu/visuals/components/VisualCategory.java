package com.isusdlc.ui.menu.visuals.components;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.systems.modules.Module;
import com.isusdlc.ui.hud.HudElement;
import com.isusdlc.ui.menu.api.MenuCategory;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;

public class VisualCategory {
   private final String name;
   private final MenuCategory menuCategory;
   private final List<VisualToggle> toggles = new ArrayList<>();
   private final List<HudToggle> hudToggles = new ArrayList<>();
   private final Animation selectedAnimation = new Animation(300L, Easing.BAKEK);

   public VisualCategory(String name, MenuCategory menuCategory) {
      this.name = name;
      this.menuCategory = menuCategory;
   }

   public void init() {
      this.toggles.clear();
      this.hudToggles.clear();
      if (this.menuCategory != null) {
         for (Module module : elegant.getInstance().getModuleManager().getModules()) {
            if (module.getCategory() == this.menuCategory.getCategory() && !module.isHidden()) {
               this.toggles.add(new VisualToggle(module));
            }
         }
      } else if (this.name.equals("HUD")) {
         for (HudElement element : elegant.getInstance().getHud().getElements()) {
            this.hudToggles.add(new HudToggle(element));
         }
      } else if (this.name.equals("Utilities")) {
      }
   }

   public boolean isHudCategory() {
      return this.name.equals("HUD");
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public List<VisualToggle> getToggles() {
      return this.toggles;
   }

   @Generated
   public List<HudToggle> getHudToggles() {
      return this.hudToggles;
   }

   @Generated
   public Animation getSelectedAnimation() {
      return this.selectedAnimation;
   }

   @Generated
   public MenuCategory getMenuCategory() {
      return this.menuCategory;
   }
}
