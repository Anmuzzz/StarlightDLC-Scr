package com.isusdlc.ui.hud.impl.island.impl;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.setting.settings.SelectSetting;
import com.isusdlc.ui.hud.impl.island.DynamicIsland;
import com.isusdlc.ui.hud.impl.island.IslandStatus;
import com.isusdlc.utility.colors.Colors;

public class DefaultStatus extends IslandStatus {
   public DefaultStatus(SelectSetting setting) {
      super(setting, "default");
   }

   @Override
   public void draw(CustomDrawContext context) {
      DynamicIsland island = elegant.getInstance().getHud().getIsland();
      float x = sr.getScaledWidth() / 2.0F - island.getSize().width / 2.0F;
      float y = 7.0F;
      float width = this.size.width = 20.0F + Fonts.MEDIUM.getFont(7.0F).width(elegant.NAME);
      float height = this.size.height = 15.0F;
      context.drawRoundedRect(x - 6.0F + 10.0F * this.animation.getValue(), y + 4.0F, 7.0F, 7.0F, BorderRadius.all(3.0F), Colors.getAccentColor());
      context.drawText(Fonts.MEDIUM.getFont(7.0F), elegant.NAME, x + 25.0F - 10.0F * this.animation.getValue(), y + 5.0F, Colors.getTextColor());
   }

   @Override
   public boolean canShow() {
      return true;
   }
}
