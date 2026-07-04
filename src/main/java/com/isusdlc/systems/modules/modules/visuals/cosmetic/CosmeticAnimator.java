package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class CosmeticAnimator {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private final Map<String, PlaybackState> playback = new HashMap<>();

    public void clearState() {
        playback.clear();
    }

    private float walkCycle = 0f;
    private float prevWalkCycle = 0f;
    private float walkSpeed = 0f;
    private float petHoverPhase = 0f;

    public float getPetHoverOffset() {
        return MathHelper.sin(petHoverPhase) * 0.14f;
    }

    public void tick(PlayerEntity player) {
        if (player == null) return;
        prevWalkCycle = walkCycle;
        float speed = (float) Math.sqrt(
                (player.getX() - player.prevX) * (player.getX() - player.prevX) +
                (player.getZ() - player.prevZ) * (player.getZ() - player.prevZ)
        );
        walkSpeed = MathHelper.lerp(0.3f, walkSpeed, speed);
        walkCycle += walkSpeed * 2.5f;
        if (CosmeticManager.getInstance().getActivePet() != null) {
            petHoverPhase += 0.1f;
        }

        tickModel(CosmeticManager.getInstance().getActiveModel(), player);
        tickModel(CosmeticManager.getInstance().getActivePet(), player);
        tickModel(CosmeticManager.getInstance().getActiveAccessory(), player);
    }

    private void tickModel(CosmeticModel model, PlayerEntity player) {
        if (model == null || !model.hasBbAnimations()) return;

        PlaybackState state = playback.computeIfAbsent(model.getId(), id -> new PlaybackState());
        String wanted = model.getType() == CosmeticType.PET
                ? pickPetAnimationName(model)
                : pickAnimationName(player, model);
        if (!wanted.equals(state.currentName)) {
            state.previousName = state.currentName;
            state.currentName = wanted;
            state.blend = 0f;
        }

        CosmeticBbAnimation anim = model.findBbAnimation(wanted);
        if (anim == null) {
            anim = model.getFirstBbAnimation();
            if (anim == null) return;
        }

        state.time += 1f / 20f;
        if (anim.isLoop()) {
            state.time %= anim.getLength();
        } else if (state.time > anim.getLength()) {
            state.time = anim.getLength();
        }
        state.blend = Math.min(1f, state.blend + 0.2f);
    }

    public void apply(CosmeticModel model, float partialTicks, float swingProgress, float limbAngle, float limbDistance) {
        if (model == null) return;

        for (CosmeticBone root : model.getRootBones()) {
            root.resetAnim();
        }

        if (model.hasBbAnimations() && mc.player != null) {
            if (shouldUseBindPose(model, mc.player)) {
                applyBindPose(model);
                applyProcedural(model, partialTicks, swingProgress, limbAngle, limbDistance);
                return;
            }
            applyBb(model, partialTicks, swingProgress, limbAngle, limbDistance);
            return;
        }

        applyProcedural(model, partialTicks, swingProgress, limbAngle, limbDistance);
    }

    private boolean shouldUseBindPose(CosmeticModel model, PlayerEntity player) {
        if (!model.hasRestPoseAnimation()) return true;
        boolean moving = Math.abs(player.forwardSpeed) > 0.01f || Math.abs(player.sidewaysSpeed) > 0.01f;
        return !moving && player.isOnGround() && player.handSwingProgress <= 0f
                && !player.isGliding() && !player.isSwimming() && !player.isClimbing();
    }

    private void applyBindPose(CosmeticModel model) {
        CosmeticBbAnimation rest = model.findBbAnimation(
                "idling", "idle", "idle_arms", "idleMonsBob", "defaultstate"
        );
        if (rest != null) {
            CosmeticBbAnimation.apply(model, rest, 0f);
        }
    }

    public void applyBow(CosmeticModel model, float partialTicks, float pullProgress,
                         float swingProgress, float limbAngle, float limbDistance) {
        if (model == null) return;

        for (CosmeticBone root : model.getRootBones()) {
            root.resetAnim();
        }

        if (!model.hasBbAnimations()) {
            applyProcedural(model, partialTicks, swingProgress, limbAngle, limbDistance);
            return;
        }

        CosmeticBbAnimation anim = model.findBbAnimation("bowR", "bow", "bowL");
        if (anim == null) anim = model.getFirstBbAnimation();
        if (anim == null) return;

        float time = pullProgress > 0.02f
                ? pullProgress * anim.getLength()
                : (playback.computeIfAbsent(model.getId(), id -> new PlaybackState()).time + partialTicks / 20f);
        if (pullProgress <= 0.02f && anim.isLoop()) {
            time %= anim.getLength();
        }
        CosmeticBbAnimation.apply(model, anim, time);
    }

    private String pickPetAnimationName(CosmeticModel model) {
        if (model.findBbAnimation("idleMonsBob", "idlemonsbob") != null) {
            return prefer(model, "idleMonsBob", "idlemonsbob");
        }
        if (model.findBbAnimation("fly", "flying", "float", "hover", "idlefly") != null) {
            return prefer(model, "fly", "flying", "float", "hover", "idlefly");
        }
        if (model.findBbAnimation("idling", "idle", "happimons") != null) {
            return prefer(model, "idling", "idle", "happimons");
        }
        CosmeticBbAnimation first = model.getFirstBbAnimation();
        return first != null ? first.getName() : "idling";
    }

    private void applyBb(CosmeticModel model, float partialTicks, float swingProgress,
                         float limbAngle, float limbDistance) {
        PlaybackState state = playback.get(model.getId());
        if (state == null) return;

        CosmeticBbAnimation current = model.findBbAnimation(state.currentName);
        if (current == null) current = model.getFirstBbAnimation();
        if (current == null) return;

        float animTime = state.time + partialTicks / 20f;
        CosmeticBbAnimation.apply(model, current, animTime);

        if (swingProgress > 0f) {
            CosmeticBbAnimation attack = model.findBbAnimation(
                    "attackR", "attack", "mineR", "swingR", "swing", "punchR"
            );
            if (attack != null) {
                float t = swingProgress * attack.getLength();
                CosmeticBbAnimation.apply(model, attack, t);
            } else {
                applyProceduralArmsOnly(model, swingProgress);
            }
        }

        if (limbDistance > 0.01f && model.getType() == CosmeticType.MODEL) {
            applyProceduralLegsOnly(model, limbAngle, limbDistance);
        }
    }

    private void applyProceduralLegsOnly(CosmeticModel model, float limbAngle, float limbDistance) {
        float walk = limbAngle;
        float swing = MathHelper.sin(walk) * limbDistance * 57.3f;
        applyLegs(model, swing);
    }

    private String pickAnimationName(PlayerEntity player, CosmeticModel model) {
        boolean onGround = player.isOnGround();
        boolean moving = Math.abs(player.forwardSpeed) > 0.01f || Math.abs(player.sidewaysSpeed) > 0.01f;
        boolean sprint = player.isSprinting() && moving;
        boolean sneak = player.isSneaking();
        boolean swim = player.isSwimming() || player.isSubmergedInWater();
        boolean glide = player.isGliding();
        boolean climb = player.isClimbing();
        double yVel = player.getVelocity().y;

        if (player.handSwingProgress > 0f && model.findBbAnimation("attackR", "attack", "mineR") != null) {
            return prefer(model, "attackR", "attack", "mineR");
        }
        if (player.hurtTime > 0 && model.findBbAnimation("hurt") != null) {
            return "hurt";
        }
        if (glide && model.findBbAnimation("elytra", "elytradown") != null) {
            return prefer(model, "elytra", "elytradown");
        }
        if (swim && model.findBbAnimation("swimming", "water", "waterwalk") != null) {
            return prefer(model, "swimming", "water", "waterwalk");
        }
        if (climb && model.findBbAnimation("climbing", "climbstill") != null) {
            return prefer(model, "climbing", "climbstill");
        }
        if (!onGround) {
            if (yVel > 0.08 && model.findBbAnimation("jumpingup", "sprintjumpup", "sitjumpup", "flyup") != null) {
                return prefer(model, "jumpingup", "sprintjumpup", "sitjumpup", "flyup");
            }
            if (model.findBbAnimation("jumpingdown", "falling", "sprintjumpdown", "flydown") != null) {
                return prefer(model, "jumpingdown", "falling", "sprintjumpdown", "flydown");
            }
        }
        if (sneak && moving && model.findBbAnimation("crouchwalk", "crouchwalkback") != null) {
            return prefer(model, "crouchwalk", "crouchwalkback");
        }
        if (sneak && model.findBbAnimation("crouching", "crouch") != null) {
            return prefer(model, "crouching", "crouch");
        }
        if (sprint && model.findBbAnimation("sprinting", "flysprint", "sprint") != null) {
            return prefer(model, "sprinting", "flysprint", "sprint");
        }
        if (moving && model.findBbAnimation("walking", "walkingback", "flywalk", "walk") != null) {
            return prefer(model, "walking", "walkingback", "flywalk", "walk");
        }
        if (model.findBbAnimation("idling", "idle", "idle_arms", "defaultstate") != null) {
            return prefer(model, "idling", "idle", "idle_arms", "defaultstate");
        }
        if (model.getType() == CosmeticType.ACCESSORY && model.findBbAnimation("idle") != null) {
            return "idle";
        }
        if (model.findBbAnimation("mesmerized", "mesmerised") != null) {
            return prefer(model, "mesmerized", "mesmerised");
        }
        CosmeticBbAnimation first = model.getFirstBbAnimation();
        return first != null ? first.getName() : "idling";
    }

    private static String prefer(CosmeticModel model, String... names) {
        CosmeticBbAnimation anim = model.findBbAnimation(names);
        return anim != null ? anim.getName() : names[0];
    }

    private void applyProcedural(CosmeticModel model, float partialTicks, float swingProgress,
                                 float limbAngle, float limbDistance) {
        float walk = MathHelper.lerp(partialTicks, prevWalkCycle, walkCycle);
        float swing = MathHelper.sin(walk) * limbDistance * 57.3f;

        applyHead(model, partialTicks);
        applyLegs(model, swing);
        applyArms(model, swing, swingProgress);
        applyBody(model, swing);
    }

    private void applyProceduralArmsOnly(CosmeticModel model, float swingProgress) {
        float swingArm = MathHelper.sin(swingProgress * (float) Math.PI) * -60f;
        CosmeticBone rightArm = findBone(model, "rightarm", "right_arm", "arm_right", "armr", "rarm");
        CosmeticBone leftArm = findBone(model, "leftarm", "left_arm", "arm_left", "arml", "larm");
        if (rightArm != null) {
            rightArm.setAnimRotation(new Vector3f(swingArm, 0, 0));
        }
        if (leftArm != null) {
            leftArm.setAnimRotation(new Vector3f(-swingArm * 0.35f, 0, 0));
        }
    }

    private void applyLegs(CosmeticModel model, float swing) {
        CosmeticBone leftLeg = findBone(model, "leftleg", "left_leg", "leg_left", "legl", "lleg");
        CosmeticBone rightLeg = findBone(model, "rightleg", "right_leg", "leg_right", "legr", "rleg");

        if (leftLeg != null) leftLeg.setAnimRotation(new Vector3f(swing, 0, 0));
        if (rightLeg != null) rightLeg.setAnimRotation(new Vector3f(-swing, 0, 0));
    }

    private void applyArms(CosmeticModel model, float swing, float swingProgress) {
        CosmeticBone leftArm = findBone(model, "leftarm", "left_arm", "arm_left", "arml", "larm");
        CosmeticBone rightArm = findBone(model, "rightarm", "right_arm", "arm_right", "armr", "rarm");

        float swingArm = MathHelper.sin(swingProgress * (float) Math.PI) * -60f;
        float rightSwing = swingProgress > 0 ? swingArm : swing;
        float leftSwing = swingProgress > 0 ? -swingArm * 0.35f : -swing;

        if (rightArm != null) rightArm.setAnimRotation(new Vector3f(rightSwing, 0, 0));
        if (leftArm != null) leftArm.setAnimRotation(new Vector3f(leftSwing, 0, 0));
    }

    private void applyBody(CosmeticModel model, float swing) {
        CosmeticBone body = findBone(model, "body", "torso", "chest", "upper_body", "gorso");
        if (body == null) return;
        float tilt = MathHelper.sin(swing * 0.017453292f) * 3f;
        body.setAnimRotation(new Vector3f(0, tilt, 0));
    }

    private void applyHead(CosmeticModel model, float partialTicks) {
        CosmeticBone head = findBone(model, "head", "skull", "face", "gneck2");
        if (head == null || mc.player == null) return;

        float pitch = MathHelper.lerp(partialTicks, mc.player.prevPitch, mc.player.getPitch());
        float yaw = MathHelper.lerp(partialTicks, mc.player.prevYaw, mc.player.getYaw());
        float bodyYaw = MathHelper.lerp(partialTicks, mc.player.prevBodyYaw, mc.player.bodyYaw);
        float headYaw = MathHelper.wrapDegrees(yaw - bodyYaw);

        head.setAnimRotation(new Vector3f(-pitch, -headYaw, 0));
    }

    private CosmeticBone findBone(CosmeticModel model, String... names) {
        for (String name : names) {
            CosmeticBone bone = model.getBone(name);
            if (bone != null) return bone;
        }
        for (String name : names) {
            for (CosmeticBone bone : getAllBones(model)) {
                if (bone.getName().toLowerCase().contains(name.toLowerCase())) return bone;
            }
        }
        return null;
    }

    private java.util.List<CosmeticBone> getAllBones(CosmeticModel model) {
        java.util.List<CosmeticBone> all = new java.util.ArrayList<>();
        for (CosmeticBone root : model.getRootBones()) collectBones(root, all);
        return all;
    }

    private void collectBones(CosmeticBone bone, java.util.List<CosmeticBone> list) {
        list.add(bone);
        for (CosmeticBone child : bone.getChildren()) collectBones(child, list);
    }

    private static final class PlaybackState {
        private String currentName = "idling";
        private String previousName = "";
        private float time = 0f;
        private float blend = 1f;
    }
}
