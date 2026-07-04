package com.isusdlc.systems.modules.constructions.swinganim.presets;

import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.systems.file.FileManager;
import com.isusdlc.systems.modules.constructions.swinganim.SwingManager;
import com.isusdlc.systems.modules.constructions.swinganim.SwingPhase;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.interfaces.IMinecraft;

public class SwingPresetFile implements IMinecraft {
   private final File file;
   private final String fileName;
   private final Animation hoverAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);
   private final Animation activeAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);

   public SwingPresetFile(String fileName) {
      this.fileName = fileName;
      File presetsFolder = new File(mc.runDirectory, "elegant/presets/swing");
      if (!presetsFolder.exists()) {
         presetsFolder.mkdirs();
      }

      this.file = new File(presetsFolder, fileName + ".myth");
   }

   public void load() {
      if (this.file.exists()) {
         try (FileReader reader = new FileReader(this.file)) {
            JsonObject json = (JsonObject)FileManager.GSON.fromJson(reader, JsonObject.class);
            SwingManager manager = elegant.getInstance().getSwingManager();
            if (json.has("bezier")) {
               manager.getBezier().load(json.get("bezier"));
            }

            if (json.has("swingBack")) {
               manager.getBack().enabled(json.get("swingBack").getAsBoolean());
            }

            if (json.has("speed")) {
               manager.getSpeed().setCurrentValue(json.get("speed").getAsFloat());
            }

            if (json.has("startPhase")) {
               JsonObject startPhase = json.getAsJsonObject("startPhase");
               SwingPhase start = manager.getStartPhase();
               if (startPhase.has("anchorX")) {
                  start.getAnchorX().setCurrentValue(startPhase.get("anchorX").getAsFloat());
               }

               if (startPhase.has("anchorY")) {
                  start.getAnchorY().setCurrentValue(startPhase.get("anchorY").getAsFloat());
               }

               if (startPhase.has("anchorZ")) {
                  start.getAnchorZ().setCurrentValue(startPhase.get("anchorZ").getAsFloat());
               }

               if (startPhase.has("moveX")) {
                  start.getMoveX().setCurrentValue(startPhase.get("moveX").getAsFloat());
               }

               if (startPhase.has("moveY")) {
                  start.getMoveY().setCurrentValue(startPhase.get("moveY").getAsFloat());
               }

               if (startPhase.has("moveZ")) {
                  start.getMoveZ().setCurrentValue(startPhase.get("moveZ").getAsFloat());
               }

               if (startPhase.has("rotateX")) {
                  start.getRotateX().setCurrentValue(startPhase.get("rotateX").getAsFloat());
               }

               if (startPhase.has("rotateY")) {
                  start.getRotateY().setCurrentValue(startPhase.get("rotateY").getAsFloat());
               }

               if (startPhase.has("rotateZ")) {
                  start.getRotateZ().setCurrentValue(startPhase.get("rotateZ").getAsFloat());
               }
            }

            if (json.has("endPhase")) {
               JsonObject endPhase = json.getAsJsonObject("endPhase");
               SwingPhase end = manager.getEndPhase();
               if (endPhase.has("anchorX")) {
                  end.getAnchorX().setCurrentValue(endPhase.get("anchorX").getAsFloat());
               }

               if (endPhase.has("anchorY")) {
                  end.getAnchorY().setCurrentValue(endPhase.get("anchorY").getAsFloat());
               }

               if (endPhase.has("anchorZ")) {
                  end.getAnchorZ().setCurrentValue(endPhase.get("anchorZ").getAsFloat());
               }

               if (endPhase.has("moveX")) {
                  end.getMoveX().setCurrentValue(endPhase.get("moveX").getAsFloat());
               }

               if (endPhase.has("moveY")) {
                  end.getMoveY().setCurrentValue(endPhase.get("moveY").getAsFloat());
               }

               if (endPhase.has("moveZ")) {
                  end.getMoveZ().setCurrentValue(endPhase.get("moveZ").getAsFloat());
               }

               if (endPhase.has("rotateX")) {
                  end.getRotateX().setCurrentValue(endPhase.get("rotateX").getAsFloat());
               }

               if (endPhase.has("rotateY")) {
                  end.getRotateY().setCurrentValue(endPhase.get("rotateY").getAsFloat());
               }

               if (endPhase.has("rotateZ")) {
                  end.getRotateZ().setCurrentValue(endPhase.get("rotateZ").getAsFloat());
               }
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }
      }
   }

   public void save() {
      try {
         if (!this.file.exists()) {
            this.file.createNewFile();
         }

         SwingManager manager = elegant.getInstance().getSwingManager();
         JsonObject json = new JsonObject();
         json.add("bezier", manager.getBezier().save());
         json.addProperty("swingBack", manager.getBack().isEnabled());
         json.addProperty("speed", manager.getSpeed().getCurrentValue());
         JsonObject startPhase = new JsonObject();
         SwingPhase start = manager.getStartPhase();
         startPhase.addProperty("anchorX", start.getAnchorX().getCurrentValue());
         startPhase.addProperty("anchorY", start.getAnchorY().getCurrentValue());
         startPhase.addProperty("anchorZ", start.getAnchorZ().getCurrentValue());
         startPhase.addProperty("moveX", start.getMoveX().getCurrentValue());
         startPhase.addProperty("moveY", start.getMoveY().getCurrentValue());
         startPhase.addProperty("moveZ", start.getMoveZ().getCurrentValue());
         startPhase.addProperty("rotateX", start.getRotateX().getCurrentValue());
         startPhase.addProperty("rotateY", start.getRotateY().getCurrentValue());
         startPhase.addProperty("rotateZ", start.getRotateZ().getCurrentValue());
         json.add("startPhase", startPhase);
         JsonObject endPhase = new JsonObject();
         SwingPhase end = manager.getEndPhase();
         endPhase.addProperty("anchorX", end.getAnchorX().getCurrentValue());
         endPhase.addProperty("anchorY", end.getAnchorY().getCurrentValue());
         endPhase.addProperty("anchorZ", end.getAnchorZ().getCurrentValue());
         endPhase.addProperty("moveX", end.getMoveX().getCurrentValue());
         endPhase.addProperty("moveY", end.getMoveY().getCurrentValue());
         endPhase.addProperty("moveZ", end.getMoveZ().getCurrentValue());
         endPhase.addProperty("rotateX", end.getRotateX().getCurrentValue());
         endPhase.addProperty("rotateY", end.getRotateY().getCurrentValue());
         endPhase.addProperty("rotateZ", end.getRotateZ().getCurrentValue());
         json.add("endPhase", endPhase);

         try (FileWriter writer = new FileWriter(this.file)) {
            writer.write(FileManager.GSON.toJson(json));
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }
   }

   public void delete() {
      if (this.file.exists()) {
         this.file.delete();
      }
   }

   @Generated
   public String getFileName() {
      return this.fileName;
   }

   @Generated
   public File getFile() {
      return this.file;
   }

   @Generated
   public Animation getHoverAnimation() {
      return this.hoverAnimation;
   }

   @Generated
   public Animation getActiveAnimation() {
      return this.activeAnimation;
   }
}
