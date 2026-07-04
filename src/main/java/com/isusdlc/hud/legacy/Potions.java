package com.isusdlc.hud.legacy;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import com.isusdlc.utility.animation.base.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Potions extends AbstractHudElement {

    private List<StatusEffectInstance> effectsList = new ArrayList<>();
    private Map<String, Float> effectAnimations = new LinkedHashMap<>();
    private Map<String, StatusEffectInstance> cachedEffects = new LinkedHashMap<>();
    private Map<String, Integer> maxDurations = new HashMap<>();
    private Set<String> activeEffectIds = new HashSet<>();

    private float animatedWidth = 100;
    private float animatedHeight = 20;
    private long lastUpdateTime = System.currentTimeMillis();

    private long lastEffectChange = 0;
    private String currentRandomEffect = "speed";

    private static final List<String> RANDOM_EFFECTS = List.of(
            "speed", "slowness", "haste", "mining_fatigue", "strength",
            "jump_boost", "regeneration", "resistance", "fire_resistance",
            "water_breathing", "invisibility", "night_vision", "hunger",
            "weakness", "poison", "wither", "health_boost", "absorption"
    );

    private static final float ANIMATION_SPEED = 8.0f;
    private static final float ICON_SIZE = 9f;
    private static final float ROW_HEIGHT = 14f;
    private static final float HEADER_HEIGHT = 20f;
    private static final int LOW_DURATION_TICKS = 300;

    public Potions() {
        super("Potions", 300, 100, 100, 20, true);
        stopAnimation();
    }

    @Override
    public boolean visible() {
        return !scaleAnimation.isFinished(Direction.BACKWARDS);
    }

    @Override
    public void tick() {
        if (mc.player == null) {
            effectsList = new ArrayList<>();
            activeEffectIds.clear();
            maxDurations.clear();
            stopAnimation();
            return;
        }

        Collection<StatusEffectInstance> effects = mc.player.getStatusEffects();
        effectsList = new ArrayList<>(effects.stream()
                .filter(StatusEffectInstance::shouldShowIcon)
                .toList());

        activeEffectIds.clear();
        for (StatusEffectInstance effect : effectsList) {
            String id = getEffectId(effect);
            activeEffectIds.add(id);
            cachedEffects.put(id, effect);

            int currentDuration = effect.getDuration();
            if (!maxDurations.containsKey(id) || currentDuration > maxDurations.get(id)) {
                maxDurations.put(id, currentDuration);
            }

            if (!effectAnimations.containsKey(id)) {
                effectAnimations.put(id, 0f);
            }
        }

        boolean hasActiveEffects = !activeEffectIds.isEmpty() || !effectAnimations.isEmpty();
        boolean inChat = isChat(mc.currentScreen);

        if (hasActiveEffects || inChat) {
            startAnimation();
        } else {
            stopAnimation();
        }

        if (effectsList.isEmpty() && inChat) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastEffectChange >= 1000) {
                currentRandomEffect = RANDOM_EFFECTS.get(new Random().nextInt(RANDOM_EFFECTS.size()));
                lastEffectChange = currentTime;
            }
        }
    }

    private String getEffectId(StatusEffectInstance effect) {
        return effect.getEffectType().getKey()
                .map(key -> key.getValue().toString())
                .orElse("unknown_" + effect.hashCode());
    }

    private float lerp(float current, float target, float deltaTime) {
        float factor = (float) (1.0 - Math.pow(0.001, deltaTime * ANIMATION_SPEED));
        return current + (target - current) * factor;
    }

    private String formatDuration(int ticks) {
        if (ticks <= -1) {
            return "\u221e";
        }
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return minutes + ":" + String.format("%02d", seconds);
    }

    private String getEffectName(StatusEffectInstance effect) {
        return effect.getEffectType().value().getName().getString();
    }

    private String getDisplayName(StatusEffectInstance effect) {
        String name = getEffectName(effect);
        int amplifier = effect.getAmplifier();
        if (amplifier > 0) {
            return name + " " + (amplifier + 1);
        }
        return name;
    }

    private Identifier getEffectTexture(RegistryEntry<StatusEffect> effect) {
        return effect.getKey()
                .map(RegistryKey::getValue)
                .map(id -> id.withPrefixedPath("mob_effect/"))
                .orElse(Identifier.ofVanilla("mob_effect/speed"));
    }

    private Identifier getRandomEffectTexture() {
        return Identifier.ofVanilla("mob_effect/" + currentRandomEffect);
    }

    private Color getTimerColor(StatusEffectInstance effect, int rowAlpha) {
        int duration = effect.getDuration();
        boolean isNegative = effect.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL;

        if (isNegative) {
            return new Color(255, 75, 75, rowAlpha);
        }

        if (duration != -1 && duration <= LOW_DURATION_TICKS) {
            return new Color(255, 170, 0, rowAlpha);
        }

        return new Color(130, 140, 255, rowAlpha);
    }

    @Override
    public void drawDraggable(DrawContext context, int alpha) {
        if (alpha <= 0) return;

        float alphaFactor = alpha / 255.0f;

        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;
        deltaTime = Math.min(deltaTime, 0.1f);

        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, Float> entry : effectAnimations.entrySet()) {
            String id = entry.getKey();
            float currentAnim = entry.getValue();
            float targetAnim = activeEffectIds.contains(id) ? 1f : 0f;
            float newAnim = lerp(currentAnim, targetAnim, deltaTime);

            if (Math.abs(newAnim - targetAnim) < 0.01f) {
                newAnim = targetAnim;
            }

            if (newAnim <= 0.01f && targetAnim == 0f) {
                toRemove.add(id);
            } else {
                effectAnimations.put(id, newAnim);
            }
        }
        for (String id : toRemove) {
            effectAnimations.remove(id);
            cachedEffects.remove(id);
            maxDurations.remove(id);
        }

        float x = getX();
        float y = getY();

        boolean hasAnimatingEffects = !effectAnimations.isEmpty();
        boolean showExample = !hasAnimatingEffects && isChat(mc.currentScreen);

        float targetWidth = 110;
        float contentRows = 0;

        if (showExample) {
            contentRows = 1;
            String displayName = "Example Effect 10";
            String timer = "0:00";
            float nameWidth = Fonts.BOLD.getWidth(displayName, 6);
            float timerWidth = Fonts.BOLD.getWidth(timer, 6);
            targetWidth = Math.max(nameWidth + timerWidth + 40, targetWidth);
        } else if (hasAnimatingEffects) {
            for (Map.Entry<String, Float> entry : effectAnimations.entrySet()) {
                String id = entry.getKey();
                float animation = entry.getValue();
                if (animation <= 0) continue;

                StatusEffectInstance effect = cachedEffects.get(id);
                if (effect == null) continue;

                contentRows += animation;

                String displayName = getDisplayName(effect);
                String timer = formatDuration(effect.getDuration());
                float nameWidth = Fonts.BOLD.getWidth(displayName, 6);
                float timerWidth = Fonts.BOLD.getWidth(timer, 6);
                targetWidth = Math.max(nameWidth + timerWidth + 40, targetWidth);
            }
        }

        float targetHeight = HEADER_HEIGHT + contentRows * ROW_HEIGHT + 4;

        animatedWidth = lerp(animatedWidth, targetWidth, deltaTime);
        animatedHeight = lerp(animatedHeight, targetHeight, deltaTime);

        if (Math.abs(animatedWidth - targetWidth) < 0.3f) animatedWidth = targetWidth;
        if (Math.abs(animatedHeight - targetHeight) < 0.3f) animatedHeight = targetHeight;

        setWidth((int) Math.ceil(animatedWidth));
        setHeight((int) Math.ceil(animatedHeight));

        float w = getWidth();
        float h = animatedHeight;
        int bgAlpha = (int) (230 * alphaFactor);

        if (h > 0) {
            Render2D.gradientRect(x, y, w, h,
                    new int[]{
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB(),
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB()
                    },
                    5);
            Render2D.outline(x, y, w, h, 0.35f, new Color(45, 45, 45, bgAlpha).getRGB(), 5);
        }

        Scissor.enable(x, y, w, h, 2);

        Fonts.BOLD.draw("Potions", x + 8, y + 6.5f, 8, new Color(255, 255, 255, (int)(255 * alphaFactor)).getRGB());

        float iconBoxSize = 12;
        float iconBoxX = x + w - iconBoxSize - 5;
        float iconBoxY = y + 4;
        Render2D.gradientRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize,
                new int[]{
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB(),
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB()
                }, 3);
        Fonts.HUDNEW.draw("I", iconBoxX + 1.5f, iconBoxY + 1f, 9, new Color(130, 140, 255, (int)(255 * alphaFactor)).getRGB());

        float rowY = y + HEADER_HEIGHT;

        if (showExample) {
            drawEffectRow(context, x, rowY, w, "Example Effect 10", "0:00", getRandomEffectTexture(), 1.0f, 0.5f, new Color(150, 100, 255, (int)(255 * alphaFactor)).getRGB(), (int)(255 * alphaFactor));
        } else if (hasAnimatingEffects) {
            for (Map.Entry<String, Float> entry : effectAnimations.entrySet()) {
                String id = entry.getKey();
                float animation = entry.getValue();
                if (animation <= 0) continue;

                StatusEffectInstance effect = cachedEffects.get(id);
                if (effect == null) continue;

                int duration = effect.getDuration();
                int rowAlpha = (int) (255 * animation * alphaFactor);

                float progress = 1.0f;
                if (duration != -1 && maxDurations.containsKey(id)) {
                    progress = (float) duration / maxDurations.get(id);
                }

                drawEffectRow(context, x, rowY, w, getDisplayName(effect), formatDuration(duration), getEffectTexture(effect.getEffectType()), animation, progress, getTimerColor(effect, rowAlpha).getRGB(), rowAlpha);

                rowY += animation * ROW_HEIGHT;
            }
        }

        Scissor.disable();
    }

    private void drawEffectRow(DrawContext context, float x, float y, float w, String name, String timer, Identifier icon, float animation, float progress, int timerColor, int alpha) {
        float timerTextWidth = Fonts.BOLD.getWidth(timer, 6);
        float arcSize = 5.0f;
        float spacing = 2.5f;

        float boxWidth = arcSize + spacing + timerTextWidth + 8;
        float boxHeight = 11;
        float boxX = x + w - boxWidth - 6;
        float boxY = y + (ROW_HEIGHT - boxHeight) / 2f;

        Render2D.gradientRect(boxX, boxY, boxWidth, boxHeight,
                new int[]{
                        new Color(25, 25, 25, (int)(alpha * 0.6f)).getRGB(),
                        new Color(20, 20, 20, (int)(alpha * 0.6f)).getRGB(),
                        new Color(25, 25, 25, (int)(alpha * 0.6f)).getRGB(),
                        new Color(20, 20, 20, (int)(alpha * 0.6f)).getRGB()
                },
                3);
        Render2D.outline(boxX, boxY, boxWidth, boxHeight, 0.2f, new Color(45, 45, 45, (int)(alpha * 0.6f)).getRGB(), 3);

        float arcX = boxX + 4;
        float arcY = boxY + (boxHeight - arcSize) / 2f;
        float degree = progress * 360f;
        float rotation = -90f + (degree / 2f);

        Render2D.arc(arcX, arcY, arcSize, 1.2f, 360f, 0, new Color(40, 40, 40, alpha).getRGB());
        Render2D.arc(arcX, arcY, arcSize, 1.2f, degree, rotation, timerColor);

        float textHeight = 5f;
        Fonts.BOLD.draw(timer, arcX + arcSize + spacing, boxY + (boxHeight - textHeight) / 2f - 1.0f, 6, timerColor);

        float scale = ICON_SIZE / 18f;
        float iconX = x + 8;
        float iconY = y + (ROW_HEIGHT - ICON_SIZE) / 2f;

        context.getMatrices().push();
        context.getMatrices().translate(iconX, iconY, 0);
        context.getMatrices().scale(scale, scale, 1);
        context.drawGuiTexture(net.minecraft.client.render.RenderLayer::getGuiTextured, icon, 0, 0, 18, 18, new Color(255, 255, 255, alpha).getRGB());
        context.getMatrices().pop();

        Fonts.BOLD.draw(name, x + 20, y + (ROW_HEIGHT - 6) / 2f + 0.5f, 6, new Color(220, 220, 220, alpha).getRGB());
    }
}
