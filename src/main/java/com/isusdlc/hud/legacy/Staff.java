package com.isusdlc.hud.legacy;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import com.isusdlc.utility.animation.base.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Staff extends AbstractHudElement {

    private static final Identifier STEVE_SKIN = Identifier.ofVanilla("textures/entity/steve.png");

    private static class StaffInfo {
        String name;
        String role;
        Color roleColor;
        Identifier skin;
        String status;
        Color statusColor;

        StaffInfo(String name, String role, Color roleColor, Identifier skin, String status, Color statusColor) {
            this.name = name;
            this.role = role;
            this.roleColor = roleColor;
            this.skin = skin;
            this.status = status;
            this.statusColor = statusColor;
        }
    }

    private final Map<String, StaffInfo> staffMap = new LinkedHashMap<>();
    private final Map<String, Float> staffAnimations = new LinkedHashMap<>();
    private final Set<String> activeStaffIds = new HashSet<>();
    private long lastUpdateTime = System.currentTimeMillis();

    private float animatedWidth = 120;
    private float animatedHeight = 20;

    private static final float ANIMATION_SPEED = 8.0f;
    private static final float FACE_SIZE = 10f;
    private static final float ROW_HEIGHT = 16f;
    private static final float HEADER_HEIGHT = 20f;

    public Staff() {
        super("Staff", 300, 150, 140, 20, true);
        stopAnimation();
    }

    @Override
    public boolean visible() {
        return !scaleAnimation.isFinished(Direction.BACKWARDS);
    }

    private Identifier getSkinFromEntry(PlayerListEntry entry) {
        try {
            SkinTextures skinTextures = entry.getSkinTextures();
            if (skinTextures != null && skinTextures.texture() != null) {
                return skinTextures.texture();
            }
        } catch (Exception ignored) {}
        return STEVE_SKIN;
    }

    @Override
    public void tick() {
        if (mc.player == null || mc.world == null) {
            staffMap.clear();
            activeStaffIds.clear();
            stopAnimation();
            return;
        }

        activeStaffIds.clear();
        Collection<PlayerListEntry> online = mc.player.networkHandler.getPlayerList();

        for (PlayerListEntry entry : online) {
            String name = entry.getProfile().getName();
            Team team = entry.getScoreboardTeam();
            String prefix = team != null ? team.getPrefix().getString().toLowerCase() : "";

            boolean isStaff = prefix.contains("стажер") || prefix.contains("хелпер") || prefix.contains("админ") ||
                              prefix.contains("модер") || prefix.contains("ютубер") || prefix.contains("владелец") ||
                              prefix.contains("куратор") || prefix.contains("owner") || prefix.contains("admin") ||
                              prefix.contains("mod") || prefix.contains("helper") || prefix.contains("yt") ||
                              prefix.contains("тестер") || prefix.contains("разраб") || prefix.contains("support");

            if (isStaff) {
                activeStaffIds.add(name);

                String role = null;
                Color roleColor = new Color(150, 150, 150);

                if (prefix.contains("владелец") || prefix.contains("owner")) {
                    role = "Owner";
                    roleColor = new Color(255, 50, 50);
                } else if (prefix.contains("админ") || prefix.contains("admin")) {
                    if (prefix.contains("ст.") || prefix.contains("ст ")) role = "St.Admin";
                    else if (prefix.contains("мл.") || prefix.contains("мл ")) role = "Ml.Admin";
                    else role = "Admin";
                    roleColor = new Color(255, 75, 75);
                } else if (prefix.contains("модер") || prefix.contains("mod") || prefix.contains("moder")) {
                    if (prefix.contains("ст.") || prefix.contains("ст ")) role = "St.Mod";
                    else if (prefix.contains("мл.") || prefix.contains("мл ")) role = "Ml.Mod";
                    else role = "Mod";
                    roleColor = new Color(75, 150, 255);
                } else if (prefix.contains("хелпер") || prefix.contains("helper")) {
                    if (prefix.contains("ст.") || prefix.contains("ст ")) role = "St.Helper";
                    else if (prefix.contains("мл.") || prefix.contains("мл ")) role = "Ml.Helper";
                    else role = "Helper";
                    roleColor = new Color(100, 100, 255);
                } else if (prefix.contains("стажер") || prefix.contains("trainee")) {
                    role = (prefix.contains("ст.") || prefix.contains("ст ")) ? "St.Trainee" : "Trainee";
                    roleColor = new Color(150, 255, 150);
                } else if (prefix.contains("ютубер") || prefix.contains("yt") || prefix.contains("youtube")) {
                    role = "YouTube";
                    roleColor = new Color(255, 50, 50);
                } else if (prefix.contains("куратор")) {
                    role = "Curator";
                    roleColor = new Color(255, 150, 50);
                } else if (prefix.contains("разраб") || prefix.contains("developer")) {
                    role = "Dev";
                    roleColor = new Color(150, 100, 255);
                } else if (prefix.contains("тестер") || prefix.contains("tester")) {
                    role = "Tester";
                    roleColor = new Color(255, 150, 255);
                } else if (prefix.contains("support") || prefix.contains("саппорт")) {
                    role = "Support";
                    roleColor = new Color(100, 255, 200);
                }

                GameMode gameMode = entry.getGameMode();
                String status = (gameMode != null && gameMode == GameMode.SPECTATOR) ? "SPEC" : "PLAYING";
                Color statusColor = status.equals("SPEC") ? new Color(255, 75, 75) : new Color(50, 200, 50);

                if (!staffMap.containsKey(name)) {
                    staffMap.put(name, new StaffInfo(name, role, roleColor, getSkinFromEntry(entry), status, statusColor));
                } else {
                    StaffInfo info = staffMap.get(name);
                    info.skin = getSkinFromEntry(entry);
                    info.status = status;
                    info.statusColor = statusColor;
                    info.role = role;
                    info.roleColor = roleColor;
                }

                if (!staffAnimations.containsKey(name)) {
                    staffAnimations.put(name, 0f);
                }
            }
        }

        boolean hasActiveStaff = !activeStaffIds.isEmpty() || !staffAnimations.isEmpty();
        boolean inChat = isChat(mc.currentScreen);

        if (hasActiveStaff || inChat) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private float lerp(float current, float target, float deltaTime) {
        float factor = (float) (1.0 - Math.pow(0.001, deltaTime * ANIMATION_SPEED));
        return current + (target - current) * factor;
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
        for (Map.Entry<String, Float> entry : staffAnimations.entrySet()) {
            String id = entry.getKey();
            float currentAnim = entry.getValue();
            float targetAnim = activeStaffIds.contains(id) ? 1f : 0f;
            float newAnim = lerp(currentAnim, targetAnim, deltaTime);

            if (Math.abs(newAnim - targetAnim) < 0.01f) newAnim = targetAnim;
            if (newAnim <= 0.01f && targetAnim == 0f) toRemove.add(id);
            else staffAnimations.put(id, newAnim);
        }
        for (String id : toRemove) {
            staffAnimations.remove(id);
            staffMap.remove(id);
        }

        float x = getX();
        float y = getY();

        boolean hasAnimatingStaff = !staffAnimations.isEmpty();
        boolean showExample = !hasAnimatingStaff && isChat(mc.currentScreen);

        float targetWidth = 120;
        float contentRows = 0;

        if (showExample) {
            contentRows = 1;
            float bw = 26;
            float nameWidth = Fonts.BOLD.getWidth("DeadInside ", 7f);
            float statusWidth = Fonts.BOLD.getWidth("PLAYING", 5.5f) + 14;
            targetWidth = Math.max(8 + bw + 6 + 10 + 5 + nameWidth + 8 + statusWidth + 8, targetWidth);
        } else {
            for (Map.Entry<String, Float> entry : staffAnimations.entrySet()) {
                float anim = entry.getValue();
                if (anim <= 0) continue;
                StaffInfo info = staffMap.get(entry.getKey());
                if (info == null) continue;
                contentRows += anim;

                float bw = 26;
                if (info.role != null) bw = Math.max(bw, Fonts.BOLD.getWidth(info.role, 5.5f) + 6);

                float nameWidth = Fonts.BOLD.getWidth(info.name, 7f);
                float statusWidth = Fonts.BOLD.getWidth(info.status, 5.5f) + 14;

                float rowWidth = 8 + bw + 6 + 10 + 5 + nameWidth + 8 + statusWidth + 8;
                targetWidth = Math.max(rowWidth, targetWidth);
            }
        }

        float targetHeight = HEADER_HEIGHT + contentRows * ROW_HEIGHT + 4;
        animatedWidth = lerp(animatedWidth, targetWidth, deltaTime);
        animatedHeight = lerp(animatedHeight, targetHeight, deltaTime);

        setWidth((int) Math.ceil(animatedWidth));
        setHeight((int) Math.ceil(animatedHeight));

        int bgAlpha = (int) (230 * alphaFactor);
        Render2D.gradientRect(x, y, animatedWidth, animatedHeight,
                new int[]{
                        new Color(20, 20, 20, bgAlpha).getRGB(),
                        new Color(15, 15, 15, bgAlpha).getRGB()
                }, 6);
        Render2D.outline(x, y, animatedWidth, animatedHeight, 0.35f, new Color(45, 45, 45, bgAlpha).getRGB(), 6);

        Fonts.BOLD.draw("Staff List", x + 8, y + 6.5f, 8, new Color(255, 255, 255, (int)(255 * alphaFactor)).getRGB());

        float iconBoxSize = 12;
        float iconBoxX = x + animatedWidth - iconBoxSize - 5;
        float iconBoxY = y + 4;
        Render2D.gradientRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize,
                new int[]{
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB(),
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB()
                }, 3);
        Fonts.HUDNEW.draw("J", iconBoxX + 2f, iconBoxY + 1.5f, 8, new Color(130, 140, 255, (int)(255 * alphaFactor)).getRGB());

        float rowY = y + HEADER_HEIGHT;

        if (showExample) {
            Identifier playerSkin = STEVE_SKIN;
            if (mc.player != null) {
                var renderer = mc.getEntityRenderDispatcher().getRenderer(mc.player);
                if (renderer instanceof LivingEntityRenderer<?, ?, ?>) {
                    @SuppressWarnings("unchecked")
                    var livingRenderer = (net.minecraft.client.render.entity.LivingEntityRenderer<net.minecraft.entity.LivingEntity, net.minecraft.client.render.entity.state.LivingEntityRenderState, ?>) renderer;
                    var state = livingRenderer.getAndUpdateRenderState(mc.player, lastTickDelta);
                    playerSkin = livingRenderer.getTexture(state);
                }
            }
            drawStaffRow(context, x, rowY, animatedWidth, "Helper", new Color(100, 100, 255), playerSkin, "DeadInside ", "PLAYING", new Color(50, 200, 50), 1.0f, (int)(255 * alphaFactor));
        } else {
            for (Map.Entry<String, Float> entry : staffAnimations.entrySet()) {
                float anim = entry.getValue();
                if (anim <= 0) continue;
                StaffInfo info = staffMap.get(entry.getKey());
                if (info == null) continue;
                int rowAlpha = (int) (255 * anim * alphaFactor);
                drawStaffRow(context, x, rowY, animatedWidth, info.role, info.roleColor, info.skin, info.name, info.status, info.statusColor, anim, rowAlpha);
                rowY += anim * ROW_HEIGHT;
            }
        }
    }

    private void drawStaffRow(DrawContext context, float x, float rowY, float width, String role, Color roleColor, Identifier skin, String name, String status, Color statusColor, float animation, int alpha) {
        float centerY = rowY + (ROW_HEIGHT / 2f);
        int textColor = new Color(255, 255, 255, alpha).getRGB();

        float bw = 26;
        if (role != null) bw = Math.max(bw, Fonts.BOLD.getWidth(role, 5.5f) + 6);

        float fx = x + 8 + bw + 6;
        float nx = fx + FACE_SIZE + 5;
        String cleanName = (name == null || name.isEmpty()) ? "Staff" : name.replaceAll("(?i)§[0-9A-FK-OR]", "");
        if (cleanName.isEmpty()) cleanName = name;

        Fonts.BOLD.draw(cleanName, nx, centerY - 3f, 7f, textColor);

        int roleBgAlpha = (int)(alpha * 0.2f);
        Render2D.rect(x + 8, centerY - 4, bw, 8, (role != null ? new Color(roleColor.getRed(), roleColor.getGreen(), roleColor.getBlue(), roleBgAlpha).getRGB() : new Color(30, 30, 35, (int)(alpha * 0.4f)).getRGB()), 2);
        if (role != null) {
            Fonts.BOLD.draw(role, x + 8 + (bw - Fonts.BOLD.getWidth(role, 5.5f)) / 2f, centerY - 3f, 5.5f, new Color(roleColor.getRed(), roleColor.getGreen(), roleColor.getBlue(), alpha).getRGB());
        }

        float sw = Fonts.BOLD.getWidth(status, 5.5f) + 14;
        float sx = x + width - sw - 8;
        int sbAlpha = (int)(alpha * 0.15f);
        Render2D.rect(sx, centerY - 4.25f, sw, 8.5f, new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), sbAlpha).getRGB(), 3);

        Fonts.HUDNEW.draw("F", sx + 3, centerY - 4f, 6.5f, new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), alpha).getRGB());
        Fonts.BOLD.draw(status, sx + 11, centerY - 3f, 5.5f, new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), alpha).getRGB());

        drawFace(skin, fx, centerY - 5, alpha);
    }

    private void drawFace(Identifier skin, float faceX, float faceY, int alpha) {
        int color = new Color(255, 255, 255, alpha).getRGB();
        Render2D.texture(skin, faceX, faceY, FACE_SIZE, FACE_SIZE, 8f / 64f, 8f / 64f, 16f / 64f, 16f / 64f, color, 0, 2f);
        Render2D.texture(skin, faceX, faceY, FACE_SIZE, FACE_SIZE, 40f / 64f, 8f / 64f, 48f / 64f, 16f / 64f, color, 0, 2f);
    }
}
