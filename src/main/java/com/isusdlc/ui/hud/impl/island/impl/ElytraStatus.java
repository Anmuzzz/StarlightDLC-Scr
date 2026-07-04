package com.isusdlc.ui.hud.impl.island.impl;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.systems.setting.settings.SelectSetting;
import com.isusdlc.ui.hud.impl.island.DynamicIsland;
import com.isusdlc.ui.hud.impl.island.IslandStatus;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.item.Items;

public class ElytraStatus extends IslandStatus implements IMinecraft {
   public ElytraStatus(SelectSetting setting) {
      super(setting, "elytra");
   }

   @Override
   public void draw(CustomDrawContext context) {
      if (mc.player == null || !mc.player.isGliding()) return;

      DynamicIsland island = elegant.getInstance().getHud().getIsland();
      float x = sr.getScaledWidth() / 2.0F - island.getSize().width / 2.0F;
      float y = 7.0F;

      double velX = mc.player.getVelocity().x;
      double velZ = mc.player.getVelocity().z;
      double horizSpeed = Math.sqrt(velX * velX + velZ * velZ);
      int speedKmh = (int) Math.round(horizSpeed * 72.0);

      int fireworks = 0;
      for (int i = 0; i < mc.player.getInventory().size(); i++) {
         if (mc.player.getInventory().getStack(i).getItem() == Items.FIREWORK_ROCKET) {
            fireworks += mc.player.getInventory().getStack(i).getCount();
         }
      }

      String text = speedKmh + " km/h | " + fireworks + " ft";
      float textWidth = Fonts.MEDIUM.getFont(7.0F).width(text);
      this.size.width = 20.0F + textWidth;
      this.size.height = 15.0F;

      context.drawText(Fonts.MEDIUM.getFont(7.0F), text, x + 10.0F - 10.0F * this.animation.getValue(), y + 5.0F, Colors.getTextColor());
   }

   @Override
   public boolean canShow() {
      return mc.player != null && mc.player.isGliding();
   }
}
