package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.utility.game.MessageUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.inventory.InventoryUtility;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.item.Items;

@ModuleInfo(
    name = "AutoPilot",
    category = ModuleCategory.PLAYER,
    desc = "Автопилот на элитрах с облётом препятствий"
)
public class AutoPilotModule extends BaseModule implements IMinecraft {
    private Vec3d target;
    private float lastYaw;
    private float lastPitch;
    private Vec3d lastPos;

    private final SliderSetting flightHeight = new SliderSetting(this, "Высота полёта")
        .min(64.0F).max(320.0F).step(1.0F).currentValue(160.0F);
    private final SliderSetting obstacleRange = new SliderSetting(this, "Дальность проверки")
        .min(20.0F).max(200.0F).step(5.0F).currentValue(80.0F);
    private final SliderSetting pitchUpSpeed = new SliderSetting(this, "Скорость подъёма")
        .min(5.0F).max(45.0F).step(1.0F).currentValue(15.0F);
    private final SliderSetting descendDist = new SliderSetting(this, "Дистанция снижения")
        .min(10.0F).max(100.0F).step(5.0F).currentValue(30.0F);
    private final SliderSetting yawSpeed = new SliderSetting(this, "Скорость поворота")
        .min(5.0F).max(45.0F).step(1.0F).currentValue(20.0F);
    private final BooleanSetting autoFirework = new BooleanSetting(this, "Авто-фейерверк").enable();

    private final EventListener<ClientPlayerTickEvent> onTick = event -> handleTick();

    @Override
    public void onEnable() {
        if (target == null) {
            MessageUtility.error(Text.of("Укажи цель: .autopilot <x> <y> <z>"));
            setEnabled(false, true);
            return;
        }
        if (mc.player == null) return;
        lastYaw = mc.player.getYaw();
        lastPitch = mc.player.getPitch();
        lastPos = mc.player.getPos();
        if (!mc.player.isGliding()) {
            MessageUtility.warn(Text.of("Надень элитры для полёта!"));
        }
    }

    @Override
    public void onDisable() {
        target = null;
        if (mc.options != null) {
            mc.options.useKey.setPressed(false);
        }
    }

    public void setTarget(Vec3d pos) {
        this.target = pos;
        if (isEnabled() && mc.player != null) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
        }
    }

    public Vec3d getTarget() { return target; }

    private void handleTick() {
        if (mc.player == null || mc.world == null || target == null) return;

        Vec3d playerPos = mc.player.getPos();
        double dist = playerPos.distanceTo(target);

        if (dist < 3.0) {
            MessageUtility.info(Text.of("§aАвтопилот: цель достигнута"));
            setEnabled(false, true);
            return;
        }

        double dx = target.getX() - playerPos.getX();
        double dy = target.getY() - (playerPos.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double dz = target.getZ() - playerPos.getZ();
        double horizDist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float)(Math.atan2(dz, dx) * 180.0 / Math.PI - 90.0);

        boolean obstacle = checkObstacle(playerPos, targetYaw, obstacleRange.getCurrentValue());

        float targetPitch;
        if (obstacle) {
            targetPitch = -pitchUpSpeed.getCurrentValue();
        } else if (dist < descendDist.getCurrentValue()) {
            targetPitch = (float)(-Math.atan2(dy, horizDist) * 180.0 / Math.PI);
            targetPitch = MathHelper.clamp(targetPitch, -30.0F, 30.0F);
        } else {
            float heightDiff = (float)(target.getY() - playerPos.getY());
            if (Math.abs(heightDiff) > 10.0F) {
                targetPitch = (float)(-Math.atan2(dy, horizDist) * 180.0 / Math.PI);
                targetPitch = MathHelper.clamp(targetPitch, -20.0F, 10.0F);
            } else {
                targetPitch = 0.0F;
            }
        }

        float yDiff = MathHelper.wrapDegrees(targetYaw - lastYaw);
        lastYaw += MathHelper.clamp(yDiff, -yawSpeed.getCurrentValue(), yawSpeed.getCurrentValue());

        float pDiff = targetPitch - lastPitch;
        lastPitch += MathHelper.clamp(pDiff, -10.0F, 10.0F);

        mc.player.setYaw(lastYaw);
        mc.player.setPitch(lastPitch);

        if (autoFirework.isEnabled() && mc.player.isGliding()) {
            Vec3d vel = mc.player.getVelocity();
            double speed = Math.sqrt(vel.x * vel.x + vel.y * vel.y + vel.z * vel.z);
            if (speed < 0.5 && mc.player.getPos().distanceTo(lastPos) < 0.3) {
                InventoryUtility.selectItemInHotbar(Items.FIREWORK_ROCKET);
                mc.options.useKey.setPressed(true);
            } else {
                mc.options.useKey.setPressed(false);
            }
        }

        lastPos = mc.player.getPos();
    }

    private boolean checkObstacle(Vec3d playerPos, float yaw, float range) {
        Vec3d eyePos = playerPos.add(0, mc.player.getEyeHeight(mc.player.getPose()), 0);
        Vec3d lookDir = Vec3d.fromPolar(0.0F, yaw).normalize();
        Vec3d end = eyePos.add(lookDir.multiply(range));

        BlockHitResult result = mc.world.raycast(
            new RaycastContext(eyePos, end, ShapeType.COLLIDER, FluidHandling.NONE, mc.player)
        );

        return result.getType() == HitResult.Type.BLOCK
            && eyePos.distanceTo(result.getPos()) < range - 3.0;
    }
}
