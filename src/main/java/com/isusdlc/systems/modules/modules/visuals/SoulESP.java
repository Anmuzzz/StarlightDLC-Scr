package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.network.ReceivePacketEvent;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(
   name = "Soul ESP",
   category = ModuleCategory.VISUALS,
   desc = "Показывает душу игрока после смерти"
)
public class SoulESP extends BaseModule {

   private final SliderSetting duration = new SliderSetting(this, "Duration")
      .min(1.0F).max(10.0F).step(0.5F).currentValue(3.0F);

   private final List<Ghost> ghosts = new ArrayList<>();

   private final EventListener<ReceivePacketEvent> onPacket = event -> {
      if (mc.world == null) return;
      if (!(event.getPacket() instanceof EntityStatusS2CPacket packet)) return;

      byte status = packet.getStatus();
      if (status == 3 || status == 35) {
         Entity entity = packet.getEntity(mc.world);
         if (entity instanceof PlayerEntity player && player != mc.player) {
            ghosts.add(new Ghost(
               player.getPos(),
               player.getBodyYaw(),
               player.isSneaking(),
               player.age,
               System.currentTimeMillis()
            ));
         }
      }
   };

   private final EventListener<Render3DEvent> onRender = event -> {
      if (ghosts.isEmpty()) return;

      long now = System.currentTimeMillis();
      float dur = duration.getCurrentValue() * 1000F;
      ghosts.removeIf(g -> (now - g.time) >= dur);

      Vec3d cam = event.getCamera().getPos();
      var matrices = event.getMatrices();
      ColorRGBA accent = Colors.getAccentColor();
      float r = accent.getRed();
      float g2 = accent.getGreen();
      float b2 = accent.getBlue();

      for (Ghost g : ghosts) {
         float t = (now - g.time) / dur;
         if (t >= 1F) continue;

         float alpha = (1F - t) * 0.6F;
         float rise = (float) (3.5F * (1F - Math.pow(1F - MathHelper.clamp(t, 0F, 1F), 3)));

         matrices.push();
         matrices.translate(g.pos.x - cam.x, g.pos.y - cam.y + rise, g.pos.z - cam.z);
         matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(180F - g.yaw));
         matrices.scale(-1F, -1F, 1F);
         matrices.translate(0, -1.5, 0);

         if (g.sneak) {
            matrices.translate(0, 0.2, 0);
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(28F));
         }

         float u = 1F / 16F;
         float swing = MathHelper.sin(g.phase * 0.6662F) * 0.6F;
         var mat = matrices.peek().getPositionMatrix();
         var buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

         box(buf, mat, -4*u, 0, -2*u, 8*u, 12*u, 4*u, r, g2, b2, alpha);
         box(buf, mat, -4*u, -8*u, -4*u, 8*u, 8*u, 8*u, r, g2, b2, alpha);

         matrices.push();
         matrices.translate(-6*u, 2*u, 0);
         matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotation(-swing));
         matrices.translate(6*u, -2*u, 0);
         box(buf, matrices.peek().getPositionMatrix(), -8*u, -2*u, -2*u, 4*u, 12*u, 4*u, r, g2, b2, alpha);
         matrices.pop();

         matrices.push();
         matrices.translate(6*u, 2*u, 0);
         matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotation(swing));
         matrices.translate(-6*u, -2*u, 0);
         box(buf, matrices.peek().getPositionMatrix(), 4*u, -2*u, -2*u, 4*u, 12*u, 4*u, r, g2, b2, alpha);
         matrices.pop();

         matrices.push();
         matrices.translate(-2*u, 12*u, 0);
         matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotation(swing));
         matrices.translate(2*u, -12*u, 0);
         box(buf, matrices.peek().getPositionMatrix(), -4*u, 12*u, -2*u, 4*u, 12*u, 4*u, r, g2, b2, alpha);
         matrices.pop();

         matrices.push();
         matrices.translate(2*u, 12*u, 0);
         matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotation(-swing));
         matrices.translate(-2*u, -12*u, 0);
         box(buf, matrices.peek().getPositionMatrix(), 0, 12*u, -2*u, 4*u, 12*u, 4*u, r, g2, b2, alpha);
         matrices.pop();

         BufferRenderer.drawWithGlobalProgram(buf.end());
         matrices.pop();
      }
   };

   private void box(BufferBuilder b, org.joml.Matrix4f m, float x, float y, float z, float sx, float sy, float sz, float r, float g, float bl, float a) {
      float x2 = x + sx, y2 = y + sy, z2 = z + sz;
      b.vertex(m, x, y, z2).color(r, g, bl, a);
      b.vertex(m, x2, y, z2).color(r, g, bl, a);
      b.vertex(m, x2, y2, z2).color(r, g, bl, a);
      b.vertex(m, x, y2, z2).color(r, g, bl, a);
      b.vertex(m, x2, y, z).color(r, g, bl, a);
      b.vertex(m, x, y, z).color(r, g, bl, a);
      b.vertex(m, x, y2, z).color(r, g, bl, a);
      b.vertex(m, x2, y2, z).color(r, g, bl, a);
      b.vertex(m, x, y, z).color(r, g, bl, a);
      b.vertex(m, x, y, z2).color(r, g, bl, a);
      b.vertex(m, x, y2, z2).color(r, g, bl, a);
      b.vertex(m, x, y2, z).color(r, g, bl, a);
      b.vertex(m, x2, y, z2).color(r, g, bl, a);
      b.vertex(m, x2, y, z).color(r, g, bl, a);
      b.vertex(m, x2, y2, z).color(r, g, bl, a);
      b.vertex(m, x2, y2, z2).color(r, g, bl, a);
      b.vertex(m, x, y2, z2).color(r, g, bl, a);
      b.vertex(m, x2, y2, z2).color(r, g, bl, a);
      b.vertex(m, x2, y2, z).color(r, g, bl, a);
      b.vertex(m, x, y2, z).color(r, g, bl, a);
      b.vertex(m, x, y, z).color(r, g, bl, a);
      b.vertex(m, x2, y, z).color(r, g, bl, a);
      b.vertex(m, x2, y, z2).color(r, g, bl, a);
      b.vertex(m, x, y, z2).color(r, g, bl, a);
   }

   @Override
   public void onDisable() {
      ghosts.clear();
      super.onDisable();
   }

   private record Ghost(Vec3d pos, float yaw, boolean sneak, float phase, long time) {}
}
