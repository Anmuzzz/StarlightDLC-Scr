package com.isusdlc.ui.menu.modern;

import java.util.List;
import lombok.Generated;
import com.isusdlc.ui.menu.api.MenuCategory;
import com.isusdlc.ui.menu.modern.components.ModernModule;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.render.penis.PenisPlayer;

public class ModernCategory {
   private final MenuCategory category;
   private final List<ModernModule> modules;
   private final Animation selected = new Animation(500L, Easing.BAKEK);
   private PenisPlayer penis;
   private float y;

   @Generated
   public MenuCategory getCategory() {
      return this.category;
   }

   @Generated
   public List<ModernModule> getModules() {
      return this.modules;
   }

   @Generated
   public Animation getSelected() {
      return this.selected;
   }

   @Generated
   public PenisPlayer getPenis() {
      return this.penis;
   }

   @Generated
   public float getY() {
      return this.y;
   }

   @Generated
   public ModernCategory(MenuCategory category, List<ModernModule> modules) {
      this.category = category;
      this.modules = modules;
   }

   @Generated
   public void setPenis(PenisPlayer penis) {
      this.penis = penis;
   }

   @Generated
   public void setY(float y) {
      this.y = y;
   }
}
