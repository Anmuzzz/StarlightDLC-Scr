package com.isusdlc.systems.modules.modules.visuals;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ColorSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.render.Draw3DUtility;
import com.isusdlc.utility.render.RenderUtility;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "ESP",
   category = ModuleCategory.VISUALS,
   desc = "Подсвечивает сущностей сквозь стены"
)
public class ESP extends BaseModule {
   private final ModeSetting mode = new ModeSetting(this, "Mode");
   private final ModeSetting.Value box3d = new ModeSetting.Value(this.mode, "3D Box").select();
   private final ModeSetting.Value box2d = new ModeSetting.Value(this.mode, "2D Box");

   private final BooleanSetting players = new BooleanSetting(this, "Players").enable();
   private final BooleanSetting hostileMobs = new BooleanSetting(this, "Hostile Mobs").enable();
   private final BooleanSetting animals = new BooleanSetting(this, "Animals");
   private final BooleanSetting villagers = new BooleanSetting(this, "Villagers");
   private final BooleanSetting armorStands = new BooleanSetting(this, "Armor Stands");
   private final BooleanSetting showInvisible = new BooleanSetting(this, "Show Invisible");

   private final ColorSetting colorPlayers = new ColorSetting(this, "Color Players")
      .color(new ColorRGBA(255.0F, 0.0F, 0.0F, 255.0F));
   private final ColorSetting colorMobs = new ColorSetting(this, "Color Mobs")
      .color(new ColorRGBA(255.0F, 170.0F, 0.0F, 255.0F));
   private final ColorSetting colorAnimals = new ColorSetting(this, "Color Animals")
      .color(new ColorRGBA(0.0F, 255.0F, 0.0F, 255.0F));
   private final ColorSetting colorOthers = new ColorSetting(this, "Color Others")
      .color(new ColorRGBA(150.0F, 150.0F, 255.0F, 255.0F));

   private final SliderSetting lineWidth = new SliderSetting(this, "Line Width")
      .min(0.5F).max(5.0F).step(0.5F).currentValue(2.0F);
   private final SliderSetting opacity = new SliderSetting(this, "Opacity")
      .min(0.0F).max(1.0F).step(0.05F).currentValue(0.8F);

   private final EventListener<Render3DEvent> onRender3D = event -> {
      if (mc.world == null || mc.player == null) return;

      MatrixStack ms = event.getMatrices();
      Camera camera = mc.gameRenderer.getCamera();
      Vec3d cameraPos = camera.getPos();
      float tickDelta = mc.getRenderTickCounter().getTickDelta(false);

      for (Entity entity : mc.world.getEntities()) {
         if (entity == mc.player || !(entity instanceof LivingEntity living)) continue;
         if (!showInvisible.isEnabled() && living.isInvisible()) continue;
         if (!isValidTarget(living)) continue;

         ColorRGBA color = getColorForTarget(living);
          float alpha = opacity.getCurrentValue();
          color = color.withAlpha((int)(color.getAlpha() * alpha));

          double dx = MathHelper.lerp(tickDelta, living.prevX, living.getX()) - living.getX();
          double dy = MathHelper.lerp(tickDelta, living.prevY, living.getY()) - living.getY();
          double dz = MathHelper.lerp(tickDelta, living.prevZ, living.getZ()) - living.getZ();

          Box renderBox = living.getBoundingBox()
             .offset(dx, dy, dz)
             .offset(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

         renderESPBox(ms, renderBox, color);
      }
   };

   private void renderESPBox(MatrixStack ms, Box box, ColorRGBA color) {
      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.lineWidth(lineWidth.getCurrentValue());

      BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
      Draw3DUtility.renderOutlinedBox(ms, buffer, box, color);
      RenderUtility.buildBuffer(buffer);

      if (box2d.isSelected()) {
         BufferBuilder fillBuffer = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         Draw3DUtility.renderFilledBox(ms, fillBuffer, box, color.withAlpha(color.getAlpha() / 4));
         RenderUtility.buildBuffer(fillBuffer);
      }

      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   }

   private boolean isValidTarget(LivingEntity entity) {
      if (entity instanceof PlayerEntity player) return players.isEnabled();
      if (entity instanceof HostileEntity) return hostileMobs.isEnabled();
      if (entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) return animals.isEnabled();
      if (entity instanceof VillagerEntity) return villagers.isEnabled();
      if (entity instanceof ArmorStandEntity) return armorStands.isEnabled();
      return false;
   }

   private ColorRGBA getColorForTarget(LivingEntity entity) {
      if (entity instanceof PlayerEntity) return colorPlayers.getColor();
      if (entity instanceof HostileEntity) return colorMobs.getColor();
      if (entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) return colorAnimals.getColor();
      return colorOthers.getColor();
   }
}
