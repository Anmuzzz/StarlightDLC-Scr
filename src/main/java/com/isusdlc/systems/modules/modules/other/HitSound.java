package com.isusdlc.systems.modules.modules.other;

import java.util.Random;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.sounds.ClientSoundInstance;
import com.isusdlc.utility.sounds.ClientSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(
   name = "Hit Sound",
   category = ModuleCategory.OTHER,
   desc = "modules.descriptions.hit_sound"
)
@Environment(EnvType.CLIENT)
public class HitSound extends BaseModule {
   private final Random random = new Random();
   private final BooleanSetting onlyCritical = new BooleanSetting(
      this, "modules.settings.hit_sound.only_critical", "modules.settings.hit_sound.only_critical.description"
   );
   private final ModeSetting soundMode = new ModeSetting(this, "modules.settings.hit_sound.sound");
   private final ModeSetting.Value hit1 = new ModeSetting.Value(this.soundMode, "Hit 1").select();
   private final ModeSetting.Value hit2 = new ModeSetting.Value(this.soundMode, "Hit 2");
   private final ModeSetting.Value bell = new ModeSetting.Value(this.soundMode, "Bell");
   private final ModeSetting.Value bonk = new ModeSetting.Value(this.soundMode, "Bonk");
   private final ModeSetting.Value bubble = new ModeSetting.Value(this.soundMode, "Bubble");
   private final ModeSetting.Value krit = new ModeSetting.Value(this.soundMode, "Krit");
   private final ModeSetting.Value pop = new ModeSetting.Value(this.soundMode, "Pop");
   private final ModeSetting.Value uwu = new ModeSetting.Value(this.soundMode, "UwU");
   private final ModeSetting.Value moan1 = new ModeSetting.Value(this.soundMode, "Moan 1");
   private final ModeSetting.Value moan2 = new ModeSetting.Value(this.soundMode, "Moan 2");
   private final ModeSetting.Value moan3 = new ModeSetting.Value(this.soundMode, "Moan 3");
   private final ModeSetting.Value moan4 = new ModeSetting.Value(this.soundMode, "Moan 4");
   private final ModeSetting.Value randomMode = new ModeSetting.Value(this.soundMode, "Random");
   private final SliderSetting volume = new SliderSetting(this, "modules.settings.hit_sound.volume", "modules.settings.hit_sound.volume.description")
      .min(0.1F)
      .max(2.0F)
      .step(0.1F)
      .currentValue(1.0F);
   private final SliderSetting pitch = new SliderSetting(this, "modules.settings.hit_sound.pitch", "modules.settings.hit_sound.pitch.description")
      .min(0.5F)
      .max(2.0F)
      .step(0.1F)
      .currentValue(1.0F);
   private final EventListener<AttackEvent> onAttack = event -> {
      if (mc.world != null && mc.player != null) {
         if (event.getEntity() != null) {
            mc.execute(
               () -> {
                  if (this.onlyCritical.isEnabled()) {
                     boolean isCritical = mc.player.fallDistance > 0.0F
                        && !mc.player.isOnGround()
                        && !mc.player.isClimbing()
                        && !mc.player.isTouchingWater()
                        && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS);
                     if (!isCritical) {
                        return;
                     }
                  }

                  ClientSoundInstance sound = this.getSelectedSound();
                  if (sound != null) {
                     mc.getSoundManager().play(new ClientSoundInstance(sound.getFileName(), this.volume.getCurrentValue(), this.pitch.getCurrentValue()));
                  }
               }
            );
         }
      }
   };

   private ClientSoundInstance getSelectedSound() {
      if (this.soundMode.is(this.hit1)) {
         return ClientSounds.HITSOUND1;
      } else if (this.soundMode.is(this.hit2)) {
         return ClientSounds.HITSOUND2;
      } else if (this.soundMode.is(this.bell)) {
         return ClientSounds.HITSOUND3;
      } else if (this.soundMode.is(this.bonk)) {
         return ClientSounds.HITSOUND4;
      } else if (this.soundMode.is(this.bubble)) {
         return ClientSounds.HITSOUND5;
      } else if (this.soundMode.is(this.krit)) {
         return ClientSounds.HITSOUND6;
      } else if (this.soundMode.is(this.pop)) {
         return ClientSounds.HITSOUND7;
      } else if (this.soundMode.is(this.uwu)) {
         return ClientSounds.HITSOUND8;
      } else if (this.soundMode.is(this.moan1)) {
         return ClientSounds.MOAN1;
      } else if (this.soundMode.is(this.moan2)) {
         return ClientSounds.MOAN2;
      } else if (this.soundMode.is(this.moan3)) {
         return ClientSounds.MOAN3;
      } else if (this.soundMode.is(this.moan4)) {
         return ClientSounds.MOAN4;
      } else if (this.soundMode.is(this.randomMode)) {
         ClientSoundInstance[] hitSounds = new ClientSoundInstance[]{
            ClientSounds.HITSOUND1,
            ClientSounds.HITSOUND2,
            ClientSounds.HITSOUND3,
            ClientSounds.HITSOUND4,
            ClientSounds.HITSOUND5,
            ClientSounds.HITSOUND6,
            ClientSounds.HITSOUND7,
            ClientSounds.HITSOUND8
         };
         return hitSounds[this.random.nextInt(hitSounds.length)];
      } else {
         return ClientSounds.HITSOUND1;
      }
   }
}
