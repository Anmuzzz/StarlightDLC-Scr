package com.isusdlc.ui.menu;

import lombok.Generated;
import com.isusdlc.framework.base.CustomScreen;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;

public abstract class MenuScreen extends CustomScreen {
   protected final Animation menuAnimation = new Animation(500L, Easing.LINEAR);
   protected boolean closing = true;

   @Generated
   public Animation getMenuAnimation() {
      return this.menuAnimation;
   }

   @Generated
   public boolean isClosing() {
      return this.closing;
   }

   @Generated
   public void setClosing(boolean closing) {
      this.closing = closing;
   }
}
