package com.isusdlc.ui.hud.inline.impl;

import com.isusdlc.framework.base.UIContext;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.ui.hud.inline.InlineElement;
import com.isusdlc.ui.hud.inline.InlineValue;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;

public class PlayerElement extends InlineElement {
   private final InlineValue fps = new InlineValue(this.elements, "FPS", "FPS");
   private final InlineValue speed = new InlineValue(this.elements, "speed", "BPS");
   private final BooleanSetting ySpeed = new BooleanSetting(this, "hud.player.speedY").enable();
   private final Animation animation = new Animation(300L, 0.0F, Easing.SMOOTH_STEP);

   public PlayerElement() {
      super("hud.player", "icons/hud/player.png");
   }

   @Override
   public void update(UIContext context) {
      super.update(context);
      double motion = !this.ySpeed.isEnabled()
         ? Math.hypot(mc.player.getX() - mc.player.prevX, mc.player.getZ() - mc.player.prevZ)
         : Math.hypot(
            mc.player.getY() - mc.player.prevY,
            Math.hypot(mc.player.getX() - mc.player.prevX, mc.player.getZ() - mc.player.prevZ)
         );
      this.speed.update(String.format("%.2f", motion * 20.0).replace(",", "."));
      this.fps.update(Math.round(this.animation.update(mc.getCurrentFps())) + "");
   }
}
