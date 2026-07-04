package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.event.impl.game.BlockBreakEvent;
import com.isusdlc.systems.event.impl.game.StartBreakBlockEvent;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BindSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.option.Perspective;
import org.lwjgl.glfw.GLFW;

@ModuleInfo(
   name = "Freelook",
   category = ModuleCategory.PLAYER,
   desc = "modules.descriptions.freelook"
)
public class Freelook extends BaseModule {
   public static boolean isActive = false;
   public static float x = 0.0F;
   public static float y = 0.0F;
   public static float prevX = 0.0F;
   public static float prevY = 0.0F;
   private float startYaw = 0.0F;
   private float startPitch = 0.0F;
   private Perspective originalPerspective = Perspective.FIRST_PERSON;
   private final BindSetting keybind = new BindSetting(this, "freelook.keybind").key(342);
   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null) {
         if (isActive) {
            isActive = false;
         }
      } else {
         boolean keyPressed = this.keybind.getKey() != -1 && GLFW.glfwGetKey(mc.getWindow().getHandle(), this.keybind.getKey()) == 1;
         if (keyPressed && !isActive) {
            this.activateFreelook();
         } else if (!keyPressed && isActive) {
            this.deactivateFreelook();
         }

         if (!isActive) {
            x = mc.player.getYaw();
            y = mc.player.getPitch();
            prevX = x;
            prevY = y;
         } else {
            mc.player.bodyYaw = this.startYaw;
            mc.player.prevBodyYaw = this.startYaw;
            mc.player.headYaw = this.startYaw;
            mc.player.prevHeadYaw = this.startYaw;
         }
      }
   };
   private final EventListener<AttackEvent> onAttack = event -> {
      if (isActive) {
         event.cancel();
      }
   };
   private final EventListener<StartBreakBlockEvent> onStartBreakBlock = event -> {
      if (isActive) {
         event.cancel();
      }
   };
   private final EventListener<BlockBreakEvent> onBlockBreak = event -> {
      if (isActive) {
         event.cancel();
      }
   };

   @Override
   public void onEnable() {
      super.onEnable();
      isActive = false;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (isActive) {
         this.deactivateFreelook();
      }
   }

   private void activateFreelook() {
      if (mc.player != null && !isActive) {
         this.startYaw = mc.player.getYaw();
         this.startPitch = mc.player.getPitch();
         this.originalPerspective = mc.options.getPerspective();
         isActive = true;
         x = this.startYaw;
         y = this.startPitch;
         prevX = x;
         prevY = y;
         mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
      }
   }

   private void deactivateFreelook() {
      if (mc.player != null && isActive) {
         mc.options.setPerspective(this.originalPerspective);
         mc.player.setYaw(this.startYaw);
         mc.player.setPitch(this.startPitch);
         mc.player.headYaw = this.startYaw;
         mc.player.prevHeadYaw = this.startYaw;
         isActive = false;
      }
   }

   private boolean isFacingTarget(Entity target) {
      if (mc.player == null) {
         return false;
      } else {
         Vec3d playerPos = mc.player.getEyePos();
         Vec3d targetPos = target.getPos().add(0.0, target.getHeight() / 2.0F, 0.0);
         Vec3d direction = targetPos.subtract(playerPos).normalize();
         Vec3d lookVec = getCurrentLookVector();
         double dot = lookVec.dotProduct(direction);
         double angle = Math.acos(MathHelper.clamp(dot, -1.0, 1.0)) * (180.0 / Math.PI);
         return angle < 90.0;
      }
   }

   public static float calculateCorrectYawOffset(float yaw) {
      if (mc.player == null) {
         return yaw;
      } else {
         double xDiff = mc.player.getX() - mc.player.prevX;
         double zDiff = mc.player.getZ() - mc.player.prevZ;
         float distSquared = (float)(xDiff * xDiff + zDiff * zDiff);
         float renderYawOffset = mc.player.prevBodyYaw;
         float offset = renderYawOffset;
         if (distSquared > 0.0025000002F) {
            offset = (float)MathHelper.atan2(zDiff, xDiff) * 180.0F / (float) Math.PI - 90.0F;
         }

         if (mc.player.handSwingProgress > 0.0F) {
            offset = yaw;
         }

         float yawOffsetDiff = MathHelper.wrapDegrees(yaw - (renderYawOffset + MathHelper.wrapDegrees(offset - renderYawOffset) * 0.3F));
         yawOffsetDiff = MathHelper.clamp(yawOffsetDiff, -75.0F, 75.0F);
         renderYawOffset = yaw - yawOffsetDiff;
         if (yawOffsetDiff * yawOffsetDiff > 2500.0F) {
            renderYawOffset += yawOffsetDiff * 0.2F;
         }

         return renderYawOffset;
      }
   }

   public float getRotYaw() {
      return x;
   }

   public void setRotYaw(float rotYaw) {
      prevX = x;
      x = rotYaw;
   }

   public float getRotPitch() {
      return y;
   }

   public void setRotPitch(float rotPitch) {
      prevY = y;
      y = rotPitch;
   }

   public static float getYaw(float partialTicks) {
      return partialTicks == 1.0F ? x : prevX + (x - prevX) * partialTicks;
   }

   public static float getPitch(float partialTicks) {
      return partialTicks == 1.0F ? y : prevY + (y - prevY) * partialTicks;
   }

   public static float getActualYaw(float partialTicks) {
      return isActive ? getYaw(partialTicks) : (mc.player != null ? mc.player.getYaw(partialTicks) : 0.0F);
   }

   public static float getActualPitch(float partialTicks) {
      return isActive ? getPitch(partialTicks) : (mc.player != null ? mc.player.getPitch(partialTicks) : 0.0F);
   }

   public static Vec3d getActualLookVector(float partialTicks) {
      if (!isActive && mc.player != null) {
         return mc.player.getRotationVec(partialTicks);
      } else if (!isActive) {
         return Vec3d.ZERO;
      } else {
         float yaw = getYaw(partialTicks);
         float pitch = getPitch(partialTicks);
         float f = pitch * (float) (Math.PI / 180.0);
         float f1 = -yaw * (float) (Math.PI / 180.0);
         float f2 = MathHelper.cos(f1);
         float f3 = MathHelper.sin(f1);
         float f4 = MathHelper.cos(f);
         float f5 = MathHelper.sin(f);
         return new Vec3d(f3 * f4, -f5, f2 * f4);
      }
   }

   public static Vec3d getCurrentLookVector() {
      if (!isActive) {
         return mc.player != null ? mc.player.getRotationVec(1.0F) : Vec3d.ZERO;
      } else {
         float yaw = x;
         float pitch = y;
         float f = pitch * (float) (Math.PI / 180.0);
         float f1 = -yaw * (float) (Math.PI / 180.0);
         float f2 = MathHelper.cos(f1);
         float f3 = MathHelper.sin(f1);
         float f4 = MathHelper.cos(f);
         float f5 = MathHelper.sin(f);
         return new Vec3d(f3 * f4, -f5, f2 * f4);
      }
   }
}
