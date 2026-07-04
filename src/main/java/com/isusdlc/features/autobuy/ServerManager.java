package com.isusdlc.features.autobuy;

import com.isusdlc.systems.setting.settings.ModeSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;

import java.util.ArrayList;
import java.util.List;

public class ServerManager {
   private final List<String> anarchyServers165 = new ArrayList<>();
   private final List<String> anarchyServers214 = new ArrayList<>();
   private int currentServerIndex = 0;
   private String currentServer = "";
   private boolean inHub = false;
   private boolean waitingForServerLoad = false;
   private final TimerUtil hubCheckTimer = TimerUtil.create();
   private final TimerUtil serverSwitchCooldown = TimerUtil.create();
   private final ModeSetting versionSetting;

   public ServerManager(ModeSetting versionSetting) {
      this.versionSetting = versionSetting;
      this.initializeServers();
   }

   private void initializeServers() {
      anarchyServers165.addAll(List.of("/an102", "/an103", "/an104", "/an105", "/an106", "/an107"));
      for (int i = 203; i <= 220; i++) anarchyServers165.add("/an" + i);
      for (int i = 302; i <= 311; i++) anarchyServers165.add("/an" + i);
      anarchyServers165.addAll(List.of("/an502", "/an503", "/an504", "/an505", "/an506", "/an507", "/an602"));
      for (int i = 11; i <= 14; i++) anarchyServers214.add("/an" + i);
      for (int i = 21; i <= 27; i++) anarchyServers214.add("/an" + i);
      for (int i = 31; i <= 34; i++) anarchyServers214.add("/an" + i);
      for (int i = 51; i <= 53; i++) anarchyServers214.add("/an" + i);
      anarchyServers214.add("/an91");
   }

   public void resetTimers() {
      hubCheckTimer.resetCounter();
      serverSwitchCooldown.resetCounter();
   }

   public void reset() {
      currentServerIndex = 0;
      currentServer = "";
      inHub = false;
      waitingForServerLoad = false;
   }

   public void updateHubStatus(ClientWorld world) {
      inHub = isInHubInternal(world);
   }

   private boolean isInHubInternal(ClientWorld world) {
      if (world == null) return true;
      ScoreboardObjective obj = world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
      if (obj == null) return true;
      return !obj.getDisplayName().getString().contains("Анархия-");
   }

   private int getCurrentAnarchyNumber(ClientWorld world) {
      if (world == null) return -1;
      ScoreboardObjective obj = world.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
      if (obj != null) {
         String name = obj.getDisplayName().getString();
         if (name.contains("Анархия-")) {
            String[] parts = name.split("-");
            if (parts.length > 1) {
               try { return Integer.parseInt(parts[1].trim()); } catch (NumberFormatException e) { return -1; }
            }
         }
      }
      return -1;
   }

   private String getNextServer(List<String> servers, ClientWorld world) {
      if (servers.isEmpty()) return null;
      int currentNum = getCurrentAnarchyNumber(world);
      if (currentNum != -1) {
         String currentStr = "/an" + currentNum;
         int idx = servers.indexOf(currentStr);
         if (idx != -1) currentServerIndex = idx;
      }
      currentServerIndex = (currentServerIndex + 1) % servers.size();
      return servers.get(currentServerIndex);
   }

   public void switchToNextServer(ClientPlayerEntity player) {
      if (serverSwitchCooldown.hasTimeElapsed(3000L)) {
         List<String> servers = getAvailableServers();
         if (servers != null && player.getWorld() instanceof ClientWorld world) {
            String next = getNextServer(servers, world);
            if (next != null) {
               currentServer = next;
               CommandSender.sendCommand(player, next);
               waitingForServerLoad = true;
               serverSwitchCooldown.resetCounter();
            }
         }
      }
   }

   public void joinAnarchyFromHub(ClientPlayerEntity player) {
      List<String> servers = getAvailableServers();
      if (servers != null && !servers.isEmpty()) {
         CommandSender.sendCommand(player, servers.get(0));
         waitingForServerLoad = true;
         hubCheckTimer.resetCounter();
      }
   }

   private List<String> getAvailableServers() {
      String ver = versionSetting.getValue().getName();
      if ("1.21.4".equals(ver)) return new ArrayList<>(anarchyServers214);
      if ("1.16.5".equals(ver)) return new ArrayList<>(anarchyServers165);
      return null;
   }

   public boolean shouldJoinAnarchy() {
      return inHub && hubCheckTimer.hasTimeElapsed(3000L)
         && ("1.16.5".equals(versionSetting.getValue().getName()) || "1.21.4".equals(versionSetting.getValue().getName()));
   }

   public boolean isInHub() { return inHub; }
   public boolean isWaitingForServerLoad() { return waitingForServerLoad; }
   public void setWaitingForServerLoad(boolean waiting) { this.waitingForServerLoad = waiting; }
}
