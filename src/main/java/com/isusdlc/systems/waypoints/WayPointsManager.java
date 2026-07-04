package com.isusdlc.systems.waypoints;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import com.isusdlc.systems.file.FileManager;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.utility.game.MessageUtility;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;

public class WayPointsManager {
   private final Map<String, Vec3d> waypoints = new HashMap<>();
   private final File waypointsFile;

   public WayPointsManager() {
      File configsDir = new File(FileManager.DIRECTORY, "configs");
      if (!configsDir.exists()) {
         configsDir.mkdirs();
      }

      this.waypointsFile = new File(configsDir, "waypoints.json");
      this.loadWaypoints();
   }

   public void add(String name, int x, int y, int z) {
      Vec3d pos = new Vec3d(x, y, z);
      if (this.waypoints.containsKey(name)) {
         MessageUtility.error(Text.of(Localizator.translate("modules.waypoints.exists", name)));
      } else {
         this.waypoints.put(name, pos);
         MessageUtility.info(Text.of(Localizator.translate("modules.waypoints.added", name, x, y, z)));
         this.saveWaypoints();
      }
   }

   public void del(String name) {
      if (this.waypoints.remove(name) != null) {
         MessageUtility.info(Text.of(Localizator.translate("modules.waypoints.deleted", name)));
         this.saveWaypoints();
      } else {
         MessageUtility.info(Text.of(Localizator.translate("modules.waypoints.not_found", name)));
      }
   }

   public void clear() {
      this.waypoints.clear();
      MessageUtility.info(Text.of(Localizator.translate("modules.waypoints.cleared")));
      this.saveWaypoints();
   }

   public boolean contains(String name) {
      return this.waypoints.containsKey(name);
   }

   public Set<Entry<String, Vec3d>> getEntries() {
      return this.waypoints.entrySet();
   }

   private void saveWaypoints() {
      try {
         JsonArray jsonArray = new JsonArray();

         for (Entry<String, Vec3d> entry : this.waypoints.entrySet()) {
            JsonObject waypointObj = new JsonObject();
            waypointObj.addProperty("name", entry.getKey());
            waypointObj.addProperty("x", (int)entry.getValue().x);
            waypointObj.addProperty("y", (int)entry.getValue().y);
            waypointObj.addProperty("z", (int)entry.getValue().z);
            jsonArray.add(waypointObj);
         }

         try (FileWriter writer = new FileWriter(this.waypointsFile)) {
            writer.write(FileManager.GSON.toJson(jsonArray));
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   private void loadWaypoints() {
      if (this.waypointsFile.exists()) {
         try (FileReader reader = new FileReader(this.waypointsFile)) {
            JsonArray jsonArray = (JsonArray)FileManager.GSON.fromJson(reader, JsonArray.class);
            if (jsonArray != null) {
               for (JsonElement element : jsonArray) {
                  JsonObject waypointObj = element.getAsJsonObject();
                  String name = waypointObj.get("name").getAsString();
                  int x = waypointObj.get("x").getAsInt();
                  int y = waypointObj.get("y").getAsInt();
                  int z = waypointObj.get("z").getAsInt();
                  this.waypoints.put(name, new Vec3d(x, y, z));
               }
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      }
   }
}
