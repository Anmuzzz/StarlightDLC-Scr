package com.isusdlc.systems.modules.modules.visuals;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.setting.settings.SliderSetting;

import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.HudRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import net.minecraft.util.hit.EntityHitResult;

@ModuleInfo(
   name = "Cross Hair",
   category = ModuleCategory.VISUALS,
   desc = "Улучшенный прицел"
)
public class CrossHair extends BaseModule {
   private final SliderSetting attackSize = new SliderSetting(this, "Размер при ударе")
      .min(0F).max(20F).step(1F).currentValue(6F);
   private final SliderSetting indent = new SliderSetting(this, "Сближенность")
      .min(0F).max(5F).step(1F).currentValue(2F);
   private final SliderSetting height = new SliderSetting(this, "Высота линий")
      .min(2F).max(10F).step(1F).currentValue(6F);
   private final SliderSetting thickness = new SliderSetting(this, "Толщина линий")
      .min(2F).max(4F).step(1F).currentValue(2F);

   private float red = 0F;

   private final EventListener<HudRenderEvent> onRender2D = event -> {
      if (mc.player == null) return;

      float target = mc.crosshairTarget instanceof EntityHitResult ? 5F : 1F;
      red += (target - red) * 0.5F * event.getTickDelta();

      int r = 255;
      int g = Math.round(Math.max(0F, 255F * (1F - red / 5F)));
      int b = g;
      ColorRGBA mainColor = new ColorRGBA(r, g, b, 255);
      ColorRGBA borderColor = new ColorRGBA(0, 0, 0, 255);
      BorderRadius zero = BorderRadius.all(0F);

      var ctx = event.getContext();
      float x = mc.getWindow().getScaledWidth() / 2F;
      float y = mc.getWindow().getScaledHeight() / 2F;
      float cd = attackSize.getCurrentValue()
         - attackSize.getCurrentValue() * mc.player.getAttackCooldownProgress(0F);
      float size = height.getCurrentValue();
      float size2 = thickness.getCurrentValue();
      float off = size2 / 2F;
      float ind = indent.getCurrentValue() + cd;

      renderParts(ctx, x, y, size, size2, 1F, ind, off, zero, borderColor);
      renderParts(ctx, x, y, size, size2, 0F, ind, off, zero, mainColor);
   };

   private void renderParts(
      CustomDrawContext ctx, float x, float y, float size, float size2,
      float padding, float ind, float off, BorderRadius radius, ColorRGBA color
   ) {
      ctx.drawRoundedRect(x - off - padding / 2F, y - size - ind - padding / 2F, size2 + padding, size + padding, radius, color);
      ctx.drawRoundedRect(x - off - padding / 2F, y + ind - padding / 2F, size2 + padding, size + padding, radius, color);
      ctx.drawRoundedRect(x - size - ind - padding / 2F, y - off - padding / 2F, size + padding, size2 + padding, radius, color);
      ctx.drawRoundedRect(x + ind - padding / 2F, y - off - padding / 2F, size + padding, size2 + padding, radius, color);
   }
}
