package com.isusdlc.ui.hud.impl.island.impl;

import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.systems.setting.settings.SelectSetting;
import com.isusdlc.ui.hud.impl.island.TimerStatus;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.game.server.ServerUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.item.ItemStack;

public class PVPStatus extends TimerStatus implements IMinecraft {
   public PVPStatus(SelectSetting setting) {
      super(setting, "pvp");
   }

   @Override
   public void draw(CustomDrawContext context) {
      String text = "Вы в PVP режиме";
      String armorWarn = this.getArmorWarning();
      if (!armorWarn.isEmpty()) {
         text = armorWarn;
      }
      this.update("s", ServerUtility.ctTime, text, new ColorRGBA(185.0F, 28.0F, 28.0F));
      super.draw(context);
   }

   @Override
   public boolean canShow() {
      return ServerUtility.hasCT;
   }

   private String getArmorWarning() {
      if (mc.player == null) return "";

      String[] names = {"ботинки", "поножи", "нагрудник", "шлем"};
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < 4; i++) {
         ItemStack stack = mc.player.getInventory().getArmorStack(i);
         if (stack.isEmpty()) continue;

         int maxDamage = stack.getMaxDamage();
         if (maxDamage <= 0) continue;

         int damage = stack.getDamage();
         int durability = maxDamage - damage;
         int percent = durability * 100 / maxDamage;

         if (percent <= 5) {
            sb.append(names[i]).append(" 5% ");
         } else if (percent <= 10) {
            sb.append(names[i]).append(" 10% ");
         } else if (percent <= 25) {
            sb.append(names[i]).append(" 25% ");
         } else if (percent <= 50) {
            sb.append(names[i]).append(" 50% ");
         }
      }

      return sb.toString().trim();
   }
}
