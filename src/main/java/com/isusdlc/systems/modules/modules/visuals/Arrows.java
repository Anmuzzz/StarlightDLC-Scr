package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.HudRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(
   name = "Arrows",
   category = ModuleCategory.VISUALS,
   desc = "modules.descriptions.arrows"
)
@Environment(EnvType.CLIENT)
public class Arrows extends BaseModule {

   private final SliderSetting radius = new SliderSetting(this, "Radius")
      .min(30.0F).max(150.0F).step(1.0F).currentValue(50.0F);

   private final BooleanSetting players = new BooleanSetting(this, "Players").enable();
   private final BooleanSetting mobs = new BooleanSetting(this, "Mobs");

   private final Animation radiusAnim = new Animation(400L, 0.0F, Easing.EXPO_OUT);

   private final EventListener<HudRenderEvent> onRender = event -> {
      if (mc.world == null || mc.player == null) return;

      List<LivingEntity> targets = findTargets();
      if (targets.isEmpty()) return;

      float targetR = calculateTargetRadius();
      radiusAnim.update(targetR);

      float cx = mc.getWindow().getScaledWidth() / 2.0F;
      float cy = mc.getWindow().getScaledHeight() / 2.0F;
      float r = radiusAnim.getValue();

      MatrixStack matrices = event.getContext().getMatrices();

      for (LivingEntity target : targets) {
         double dx = target.getX() - mc.player.getX();
         double dz = target.getZ() - mc.player.getZ();
         float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
         float yawDiff = MathHelper.wrapDegrees(angle - mc.player.getYaw());
         float arrowAngle = (float) Math.toRadians(yawDiff);

         float ax = cx + (float) Math.sin(arrowAngle) * r;
         float ay = cy - (float) Math.cos(arrowAngle) * r;

         matrices.push();
         matrices.translate(ax, ay, 0.0F);
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(yawDiff + 90.0F));
         drawArrow(matrices, event.getContext());
         matrices.pop();
      }
   };

   private float calculateTargetRadius() {
      if (mc.currentScreen instanceof HandledScreen) {
         return 160.0F;
      }
      float base = radius.getCurrentValue();
      if (mc.player != null && mc.player.isSneaking()) {
         base -= 10.0F;
      }
      return base;
   }

   private void drawArrow(MatrixStack matrices, CustomDrawContext context) {
      context.drawRect(-1.5F, -6.0F, 3.0F, 4.0F, ColorRGBA.WHITE);
      context.drawRect(-3.5F, -2.0F, 7.0F, 3.0F, ColorRGBA.WHITE);
   }

   private List<LivingEntity> findTargets() {
      if (mc.world == null || mc.player == null) return List.of();
      List<LivingEntity> list = new ArrayList<>();
      var box = mc.player.getBoundingBox().expand(128.0);
      for (LivingEntity e : mc.world.getEntitiesByClass(LivingEntity.class, box, e -> e != mc.player && e.isAlive())) {
         if (e instanceof PlayerEntity && players.isEnabled()) list.add(e);
         else if (!(e instanceof PlayerEntity) && mobs.isEnabled()) list.add(e);
      }
      return list;
   }
}
