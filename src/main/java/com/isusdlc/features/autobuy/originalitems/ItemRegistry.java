package com.isusdlc.features.autobuy.originalitems;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.list.DonatorProvider;
import com.isusdlc.features.autobuy.items.list.HolyWorldProvider;
import com.isusdlc.features.autobuy.items.list.MiscProvider;
import com.isusdlc.features.autobuy.items.list.PotionProvider;
import com.isusdlc.features.autobuy.items.list.SphereProvider;
import com.isusdlc.features.autobuy.items.list.SpookyTimeProvider;
import com.isusdlc.features.autobuy.items.list.TalismanProvider;
import com.isusdlc.features.autobuy.util.krushprovider.KrushProvider;
import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
   private static List<AutoBuyableItem> allItems = null;

   public static List<AutoBuyableItem> getAllItems() {
      if (allItems == null) {
         allItems = new ArrayList<>();
         allItems.addAll(getKrush());
         allItems.addAll(getTalismans());
         allItems.addAll(getSpheres());
         allItems.addAll(getMisc());
         allItems.addAll(getDonator());
         allItems.addAll(getPotions());
         allItems.addAll(getHolyWorld());
      }

      return allItems;
   }

   public static void reload() {
      allItems = null;
      HolyWorldProvider.reload();
      SpookyTimeProvider.reload();
   }

   public static List<AutoBuyableItem> getKrush() {
      return KrushProvider.getKrush();
   }

   public static List<AutoBuyableItem> getTalismans() {
      return TalismanProvider.getTalismans();
   }

   public static List<AutoBuyableItem> getSpheres() {
      return SphereProvider.getSpheres();
   }

   public static List<AutoBuyableItem> getMisc() {
      return MiscProvider.getMisc();
   }

   public static List<AutoBuyableItem> getDonator() {
      return DonatorProvider.getDonator();
   }

   public static List<AutoBuyableItem> getPotions() {
      return PotionProvider.getPotions();
   }

   public static List<AutoBuyableItem> getHolyWorld() {
      return HolyWorldProvider.getItems();
   }

   public static List<AutoBuyableItem> getSpookyTime() {
      return SpookyTimeProvider.getItems();
   }

   public static List<AutoBuyableItem> getFunTimeItems() {
      ArrayList var0 = new ArrayList();
      var0.addAll(getKrush());
      var0.addAll(getTalismans());
      var0.addAll(getSpheres());
      var0.addAll(getMisc());
      var0.addAll(getDonator());
      var0.addAll(getPotions());
      return var0;
   }
}
