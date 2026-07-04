package com.isusdlc.features.autobuy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AutoBuyBanList {
   private static final String DEFAULT_BANNED = "deletecheater";
   private static final Set<String> bannedPlayers = Collections.synchronizedSet(new HashSet<>());

   public static boolean addBan(String name) {
      return name != null && !name.isEmpty() ? bannedPlayers.add(name.toLowerCase().trim()) : false;
   }

   public static boolean removeBan(String name) {
      return name != null && !name.isEmpty() ? bannedPlayers.remove(name.toLowerCase().trim()) : false;
   }

   public static boolean isBanned(String name) {
      if (name != null && !name.isEmpty()) {
         String clean = name.replaceAll("§[0-9a-fk-or]", "").replaceAll("§.", "").trim().toLowerCase();
         return bannedPlayers.contains(clean);
      }
      return false;
   }

   public static Set<String> getBannedPlayers() {
      return Collections.unmodifiableSet(new HashSet<>(bannedPlayers));
   }

   public static void clear() {
      bannedPlayers.clear();
   }

   public static int size() {
      return bannedPlayers.size();
   }

   public static void loadFromList(Collection<String> names) {
      bannedPlayers.clear();
      if (names != null) {
         for (String name : names) {
            if (name != null && !name.isEmpty()) {
               bannedPlayers.add(name.toLowerCase().trim());
            }
         }
      }
      if (bannedPlayers.isEmpty()) {
         bannedPlayers.add("deletecheater");
      }
   }

   static {
      bannedPlayers.add("deletecheater");
   }
}
