package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.PreHudRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.render.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

@ModuleInfo(
   name = "Enemy Info",
   category = ModuleCategory.VISUALS,
   desc = "Показывает информацию о цели: здоровье, броня, оружие, эффекты"
)
public class EnemyInfo extends BaseModule {
   private final BooleanSetting showHealth = new BooleanSetting(this, "Health").enable();
   private final BooleanSetting showArmor = new BooleanSetting(this, "Armor").enable();
   private final BooleanSetting showWeapon = new BooleanSetting(this, "Weapon").enable();
   private final BooleanSetting showEffects = new BooleanSetting(this, "Effects").enable();
   private final BooleanSetting showDistance = new BooleanSetting(this, "Distance").enable();

   private final SliderSetting scale = new SliderSetting(this, "Scale")
      .min(0.5F).max(2.0F).step(0.1F).currentValue(1.0F);

   private final EventListener<PreHudRenderEvent> onRender2D = event -> {
      if (mc.world == null || mc.player == null) return;

      CustomDrawContext ctx = event.getContext();
      MatrixStack ms = ctx.getMatrices();
      Font font = Fonts.MEDIUM.getFont(8.0F * scale.getCurrentValue());

      for (net.minecraft.entity.Entity entity : mc.world.getEntities()) {
         if (!(entity instanceof PlayerEntity target)) continue;
         if (target == mc.player || target.isInvisible()) continue;
         if (!target.isAlive()) continue;

         Vec3d pos = new Vec3d(
            MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(false), target.prevX, target.getX()),
            MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(false), target.prevY, target.getY()),
            MathHelper.lerp(mc.getRenderTickCounter().getTickDelta(false), target.prevZ, target.getZ())
         );

         Vec2f screen = Utils.worldToScreen(pos.add(0, target.getHeight() + 0.7, 0));
         if (screen == null) continue;

         float distance = mc.player.distanceTo(target);
         float distScale = MathHelper.clamp(1.0F - distance / 30.0F, 0.5F, 1.0F) * scale.getCurrentValue();

         ms.push();
         ms.translate(screen.x, screen.y, 0);
         ms.scale(distScale, distScale, 1);

         float lineHeight = font.height() + 2.0F;
         float padding = 4.0F;
         float x = 0;
         float y = 0;
         float maxWidth = 0;

         java.util.List<String> lines = new java.util.ArrayList<>();

         String name = target.getDisplayName().getString();
         lines.add("§f" + name);

         if (showHealth.isEnabled()) {
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();
            String hpColor = hp > 10 ? "§a" : hp > 5 ? "§e" : "§c";
            lines.add(hpColor + "HP: " + Math.round(hp) + "/" + Math.round(maxHp));
         }

         if (showArmor.isEnabled()) {
            int armor = target.getArmor();
            lines.add("§7Armor: " + armor);
         }

         if (showWeapon.isEnabled()) {
            ItemStack weapon = target.getMainHandStack();
            if (!weapon.isEmpty()) {
               lines.add("§6" + weapon.getItem().getName().getString());
            }
         }

         if (showEffects.isEnabled()) {
            Collection<StatusEffectInstance> effects = target.getStatusEffects();
            if (!effects.isEmpty()) {
               StringBuilder sb = new StringBuilder("§d");
               for (StatusEffectInstance effect : effects) {
                  if (sb.length() > 2) sb.append(", ");
                  sb.append(effect.getEffectType().value().getName().getString());
                  int amp = effect.getAmplifier() + 1;
                  if (amp > 1) sb.append(" ").append(amp);
               }
               lines.add(sb.toString());
            }
         }

         if (showDistance.isEnabled()) {
            lines.add("§7" + Math.round(distance) + "m");
         }

         for (String line : lines) {
            float w = font.width(line);
            if (w > maxWidth) maxWidth = w;
         }

         float bgWidth = maxWidth + padding * 2;
         float bgHeight = lines.size() * lineHeight + padding;

         ctx.drawRoundedRect(
            -bgWidth / 2.0F, 0,
            bgWidth, bgHeight,
            BorderRadius.all(4.0F),
            new ColorRGBA(0.0F, 0.0F, 0.0F, 140.0F)
         );

         y = padding / 2.0F + 1.0F;
         for (String line : lines) {
            ctx.drawText(font, line, -bgWidth / 2.0F + padding, y, Colors.WHITE);
            y += lineHeight;
         }

         ms.pop();
      }
   };
}
