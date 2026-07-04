package com.isusdlc.systems.modules.modules.movement;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.SlowDownEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@ModuleInfo(
   name = "No Slow",
   category = ModuleCategory.MOVEMENT,
   desc = "modules.descriptions.no_slow"
)
@Environment(EnvType.CLIENT)
public class NoSlow extends BaseModule {
   private final EventListener<SlowDownEvent> onSlowDown = event -> {
      if (mc.player != null) {
         event.cancel();
      }
   };
}
