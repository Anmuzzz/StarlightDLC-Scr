package com.isusdlc.features.autobuy.settings;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class AutoBuySettingsManager {
   private static AutoBuySettingsManager instance;
   private final Map<String, SettingsData> settingsMap = new HashMap<>();

   private AutoBuySettingsManager() {}

   public static AutoBuySettingsManager getInstance() {
      if (instance == null) instance = new AutoBuySettingsManager();
      return instance;
   }

   public void saveSettings(String itemName, AutoBuyItemSettings settings) {
      settingsMap.put(itemName.toLowerCase(), new SettingsData(settings.getBuyBelow(), settings.getSellAbove(), settings.getMinQuantity()));
   }

   public void loadSettings(String itemName, AutoBuyItemSettings settings) {
      SettingsData data = settingsMap.get(itemName.toLowerCase());
      if (data != null) {
         settings.setBuyBelow(data.buyBelow);
         settings.setSellAbove(data.sellAbove);
         settings.setMinQuantity(data.minQuantity);
      }
   }

   public boolean hasSettings(String itemName) {
      return settingsMap.containsKey(itemName.toLowerCase());
   }

   public JsonObject saveToJson() {
      JsonObject root = new JsonObject();
      for (var entry : settingsMap.entrySet()) {
         JsonObject obj = new JsonObject();
         obj.addProperty("buyBelow", entry.getValue().buyBelow);
         obj.addProperty("sellAbove", entry.getValue().sellAbove);
         obj.addProperty("minQuantity", entry.getValue().minQuantity);
         root.add(entry.getKey(), obj);
      }
      return root;
   }

   public void loadFromJson(JsonObject root) {
      if (root != null) {
         for (String key : root.keySet()) {
            JsonObject obj = root.getAsJsonObject(key);
            settingsMap.put(key.toLowerCase(), new SettingsData(
               obj.get("buyBelow").getAsInt(),
               obj.get("sellAbove").getAsInt(),
               obj.get("minQuantity").getAsInt()
            ));
         }
      }
   }

   private static class SettingsData {
      int buyBelow, sellAbove, minQuantity;
      SettingsData(int buyBelow, int sellAbove, int minQuantity) {
         this.buyBelow = buyBelow;
         this.sellAbove = sellAbove;
         this.minQuantity = minQuantity;
      }
   }
}
