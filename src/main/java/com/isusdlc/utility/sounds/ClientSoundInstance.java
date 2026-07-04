package com.isusdlc.utility.sounds;

import lombok.Generated;
import com.isusdlc.elegant;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.client.sound.SoundInstance.AttenuationType;

public class ClientSoundInstance extends PositionedSoundInstance {
   private static final float DEFAULT_PITCH = 1.0F;
   private final String fileName;

   public ClientSoundInstance(String fileName, float volume) {
      super(
         Identifier.of(elegant.MOD_ID + ":" + fileName),
         SoundCategory.MASTER,
         volume,
         1.0F,
         SoundInstance.createRandom(),
         false,
         0,
         AttenuationType.NONE,
         0.0,
         0.0,
         0.0,
         true
      );
      this.fileName = fileName;
      System.out.println("[ClientSoundInstance] Created sound: " + elegant.MOD_ID + ":" + fileName);
   }

   public ClientSoundInstance(String fileName, float volume, float pitch) {
      super(
         Identifier.of(elegant.MOD_ID + ":" + fileName),
         SoundCategory.MASTER,
         volume,
         pitch,
         SoundInstance.createRandom(),
         false,
         0,
         AttenuationType.NONE,
         0.0,
         0.0,
         0.0,
         true
      );
      this.fileName = fileName;
   }

    public void play(float volume) {
       var sm = MinecraftClient.getInstance().getSoundManager();
       if (sm != null) {
          sm.play(new ClientSoundInstance(this.fileName, volume));
       }
    }

    public void play(float volume, float pitch) {
       var sm = MinecraftClient.getInstance().getSoundManager();
       if (sm != null) {
          sm.play(new ClientSoundInstance(this.fileName, volume, pitch));
       }
    }

   @Generated
   public String getFileName() {
      return this.fileName;
   }
}
