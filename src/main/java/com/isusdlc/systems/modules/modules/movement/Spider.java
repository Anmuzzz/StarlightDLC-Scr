package com.isusdlc.systems.modules.modules.movement;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Spider",
   category = ModuleCategory.MOVEMENT,
   desc = "Забирается на стены"
)
public class Spider extends BaseModule {
   private final ModeSetting mode = new ModeSetting(this, "Mode");
   private final ModeSetting.Value water = new ModeSetting.Value(this.mode, "Water").select();
   private final ModeSetting.Value blocks = new ModeSetting.Value(this.mode, "Blocks");

   private final SliderSetting speed = new SliderSetting(this, "Speed", () -> this.blocks.isSelected())
      .min(0.1F).max(1.0F).step(0.05F).currentValue(0.42F);

   private boolean isAgainstWall() {
      if (mc.player == null || mc.world == null) return false;
      if (mc.player.horizontalCollision) return true;
      Vec3d dir = Vec3d.fromPolar(0, mc.player.getYaw()).normalize();
      for (double r = 0.1; r <= 0.6; r += 0.1) {
         BlockPos pos = BlockPos.ofFloored(mc.player.getPos().add(dir.x * r, 0, dir.z * r));
         if (mc.world.getBlockState(pos).isFullCube(mc.world, pos)) return true;
      }
      return false;
   }

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null || mc.world == null) return;
      if (!isAgainstWall()) return;

      if (water.isSelected()) {
         if (mc.player.isTouchingWater()) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.3, mc.player.getVelocity().z);
         }
      } else if (blocks.isSelected()) {
         mc.player.setVelocity(mc.player.getVelocity().x, speed.getCurrentValue(), mc.player.getVelocity().z);
         mc.player.fallDistance = 0;
      }
   };
}
