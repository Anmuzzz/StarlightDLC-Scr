package com.isusdlc.systems.modules.modules.visuals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.SoundEvent;
import com.isusdlc.systems.event.impl.render.PreHudRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SelectSetting;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.render.Utils;
import com.isusdlc.utility.render.batching.Batching;
import com.isusdlc.utility.render.batching.impl.FontBatching;
import com.isusdlc.utility.render.batching.impl.RectBatching;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;

@ModuleInfo(
   name = "Sound ESP",
   category = ModuleCategory.VISUALS,
   enabledByDefault = true,
   desc = "Показывает где был воспроизведен звук"
)
public class SoundESP extends BaseModule {
   private final SelectSetting select = new SelectSetting(this, "Отображать");
   private final SelectSetting.Value trident = new SelectSetting.Value(this.select, "Трезубец").select();
   private final SelectSetting.Value tnt = new SelectSetting.Value(this.select, "Динамит");
   private final SelectSetting.Value fireworks = new SelectSetting.Value(this.select, "Фейерверки");
   private final Map<String, SoundESP.SoundMarker> markers = new HashMap<>();
   private static final long MARKER_LIFETIME_MS = 5000L;
   private static final long FADE_OUT_MS = 500L;
   private static final long FADE_IN_MS = 200L;
   private final Set<String> TARGET_SOUNDS = new HashSet<>(
      Arrays.asList(
         "minecraft:entity.generic.explode", "minecraft:item.trident.throw", "minecraft:item.trident.return", "minecraft:entity.firework_rocket.launch"
      )
   );
   private final EventListener<SoundEvent> onSoundInstanceEvent = event -> {
      SoundInstance sound = event.getSound();
      Identifier soundId = sound.getId();
      String soundIdStr = soundId.toString();
      if (this.TARGET_SOUNDS.contains(soundIdStr)) {
         boolean add = false;
         if (soundIdStr.contains("generic.explode") && this.tnt.isSelected()) {
            add = true;
         } else if ((soundIdStr.contains("trident.throw") || soundIdStr.contains("trident.return")) && this.trident.isSelected()) {
            add = true;
         } else if (soundIdStr.contains("firework_rocket.launch") && this.fireworks.isSelected()) {
            add = true;
         }

         if (add && mc.player != null && mc.world != null) {
            String displayName = this.simplifySoundName(soundIdStr);
            long creationTime = System.currentTimeMillis();
            String key = displayName + "_" + creationTime;
            Vec3d pos = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
            this.add(key, displayName, pos.x, pos.y, pos.z);
         }
      }
   };
   private final EventListener<PreHudRenderEvent> onHudRenderEvent = event -> {
      MatrixStack matrices = event.getContext().getMatrices();
      long currentTime = System.currentTimeMillis();
      Iterator<Map.Entry<String, SoundESP.SoundMarker>> it = this.markers.entrySet().iterator();
      while (it.hasNext()) {
         SoundESP.SoundMarker m = it.next().getValue();
         long age = currentTime - m.creationTime;
         if (age > MARKER_LIFETIME_MS) {
            it.remove();
         } else {
            m.alphaAnim.update(age > MARKER_LIFETIME_MS - FADE_OUT_MS ? 0.0F : 1.0F);
         }
      }

      Batching rect = new RectBatching(VertexFormats.POSITION_COLOR, event.getContext().getMatrices());

      for (SoundESP.SoundMarker marker : this.markers.values()) {
         if (marker.alphaAnim.getValue() < 0.01F) continue;
         float distance = (float)mc.player.getPos().distanceTo(marker.pos);
         this.renderBack(event, matrices, distance, marker);
      }

      rect.draw();
      FontBatching batching = new FontBatching(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.MEDIUM);

      for (SoundESP.SoundMarker marker : this.markers.values()) {
         if (marker.alphaAnim.getValue() < 0.01F) continue;
         float distance = (float)mc.player.getPos().distanceTo(marker.pos);
         this.renderText(event, matrices, distance, marker);
      }

      batching.draw();
   };

   private void renderText(PreHudRenderEvent event, MatrixStack matrices, float distance, SoundESP.SoundMarker marker) {
      Vec3d renderPos = marker.pos;
      Vec3d renderPosAdjusted = renderPos.add(0.0, 0.5, 0.0);
      Vec2f screenPos = Utils.worldToScreen(renderPosAdjusted);
      if (screenPos != null) {
         float scale = MathHelper.clamp(1.0F - distance / 20.0F, 0.5F, 1.0F);
         String text = marker.name + " (" + String.format("%.1f", distance) + "m)";
         int width = (int)Fonts.MEDIUM.getFont(11.0F).width(text);
         int x = -width / 2;
         float alpha = marker.alphaAnim.getValue();
         matrices.push();
         matrices.translate(screenPos.x, screenPos.y, 0.0F);
         matrices.scale(scale, scale, 1.0F);
         event.getContext().drawText(Fonts.MEDIUM.getFont(11.0F), text, x + 16, 5.0F, ColorRGBA.WHITE.withAlpha(ColorRGBA.WHITE.getAlpha() * alpha));
         if (marker.name.toLowerCase().contains("взрыв")) {
            event.getContext().drawItem(Items.TNT, (float)x, 3.0F, 0.75F);
         } else if (marker.name.toLowerCase().contains("трезубец")) {
            event.getContext().drawItem(Items.TRIDENT, (float)x, 3.0F, 0.75F);
         } else if (marker.name.toLowerCase().contains("фейерверк")) {
            event.getContext().drawItem(Items.FIREWORK_ROCKET, (float)x, 3.0F, 0.75F);
         }
         matrices.pop();
      }
   }

   private void renderBack(PreHudRenderEvent event, MatrixStack matrices, float distance, SoundESP.SoundMarker marker) {
      Vec3d renderPos = marker.pos;
      Vec3d renderPosAdjusted = renderPos.add(0.0, 0.5, 0.0);
      Vec2f screenPos = Utils.worldToScreen(renderPosAdjusted);
      if (screenPos != null) {
         float scale = MathHelper.clamp(1.0F - distance / 20.0F, 0.5F, 1.0F);
         String text = marker.name + " (" + String.format("%.1f", distance) + "m)";
         float alpha = marker.alphaAnim.getValue();
         matrices.push();
         matrices.translate(screenPos.x, screenPos.y, 0.0F);
         matrices.scale(scale, scale, 1.0F);
         int textWidth = (int)Fonts.MEDIUM.getFont(11.0F).width(text);
         int x = -textWidth / 2;
         int y = 1;
         event.getContext().drawRect(x - 3, y, textWidth + 26, Fonts.MEDIUM.getFont(11.0F).height() + 8.0F, new ColorRGBA(0.0F, 0.0F, 0.0F, 100.0F * alpha));
         matrices.pop();
      }
   }

   private void add(String key, String displayName, double x, double y, double z) {
      Vec3d pos = new Vec3d(x, y, z);
      this.markers.put(key, new SoundESP.SoundMarker(displayName, pos, System.currentTimeMillis(), new Animation(FADE_IN_MS, 0.0F, Easing.CUBIC_OUT)));
   }

   private String simplifySoundName(String soundId) {
      if (soundId.contains("generic.explode")) {
         return "Взрыв";
      } else if (soundId.contains("trident.throw")) {
         return "Трезубец брошен";
      } else if (soundId.contains("trident.return")) {
         return "Трезубец";
      } else {
         return soundId.contains("firework_rocket.launch") ? "Фейерверк" : soundId.replace("minecraft:", "");
      }
   }

   static class SoundMarker {
      final String name;
      final Vec3d pos;
      final long creationTime;
      final Animation alphaAnim;

      SoundMarker(String name, Vec3d pos, long creationTime, Animation alphaAnim) {
         this.name = name;
         this.pos = pos;
         this.creationTime = creationTime;
         this.alphaAnim = alphaAnim;
      }
   }
}
