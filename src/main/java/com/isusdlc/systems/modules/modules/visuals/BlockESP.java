package com.isusdlc.systems.modules.modules.visuals;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.systems.setting.settings.BooleanSetting;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.render.Draw3DUtility;
import com.isusdlc.utility.render.RenderUtility;
import net.minecraft.block.Blocks;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Block ESP",
   category = ModuleCategory.VISUALS,
   desc = "Показывает сундуки, печки, спавнеры и др."
)
public class BlockESP extends BaseModule {
   private final SliderSetting radius = new SliderSetting(this, "Радиус")
      .min(1F).max(30F).step(1F).currentValue(20F);
   private final BooleanSetting chest = new BooleanSetting(this, "Сундуки").enable();
   private final BooleanSetting furnace = new BooleanSetting(this, "Печки").enable();
   private final BooleanSetting spawner = new BooleanSetting(this, "Спавнеры").enable();
   private final BooleanSetting brewingStand = new BooleanSetting(this, "Варочные").enable();
   private final BooleanSetting enderChest = new BooleanSetting(this, "Эндер сундуки").enable();
   private final BooleanSetting detectorRail = new BooleanSetting(this, "Детектор рельс").enable();

   private final EventListener<Render3DEvent> onRender3D = event -> {
      if (mc.world == null || mc.player == null) return;

      int r = (int) radius.getCurrentValue();
      BlockPos center = mc.player.getBlockPos();
      MatrixStack ms = event.getMatrices();
      Camera camera = event.getCamera();
      Vec3d cameraPos = camera.getPos();

      RenderSystem.enableBlend();
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

      BufferBuilder buffer = RenderSystem.renderThreadTesselator()
         .begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

      for (int x = center.getX() - r; x <= center.getX() + r; x++) {
         for (int y = center.getY() - r; y <= center.getY() + r; y++) {
            for (int z = center.getZ() - r; z <= center.getZ() + r; z++) {
               BlockPos pos = new BlockPos(x, y, z);
               var state = mc.world.getBlockState(pos);
               Box box = new Box(pos).contract(0.01)
                  .offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);
               ColorRGBA color = getColor(state);

               if (color != null) {
                  Draw3DUtility.renderOutlinedBox(ms, buffer, box, color);
               }
            }
         }
      }

      RenderUtility.buildBuffer(buffer);
      RenderSystem.defaultBlendFunc();
      RenderSystem.enableCull();
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
   };

   private ColorRGBA getColor(net.minecraft.block.BlockState state) {
      var block = state.getBlock();
      if (chest.isEnabled() && (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST))
         return new ColorRGBA(139, 69, 19, 150);
      if (furnace.isEnabled() && (block == Blocks.FURNACE || block == Blocks.BLAST_FURNACE || block == Blocks.SMOKER))
         return new ColorRGBA(128, 128, 128, 150);
      if (spawner.isEnabled() && block == Blocks.SPAWNER)
         return new ColorRGBA(255, 0, 255, 150);
      if (brewingStand.isEnabled() && block == Blocks.BREWING_STAND)
         return new ColorRGBA(0, 191, 255, 150);
      if (enderChest.isEnabled() && block == Blocks.ENDER_CHEST)
         return new ColorRGBA(75, 0, 130, 150);
      if (detectorRail.isEnabled() && block == Blocks.DETECTOR_RAIL)
         return new ColorRGBA(255, 165, 0, 150);
      return null;
   }
}
