package com.isusdlc.systems.modules.modules.movement;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.InputEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import net.minecraft.client.gui.screen.ChatScreen;
import static org.lwjgl.glfw.GLFW.*;

@ModuleInfo(
   name = "Inventory Move",
   category = ModuleCategory.MOVEMENT,
   desc = "Позволяет двигаться с открытым инвентарём"
)
public class InventoryMove extends BaseModule {
   private final EventListener<InputEvent> onInput = event -> {
      if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen) && mc.player != null && mc.player.isAlive()) {
         long handle = mc.getWindow().getHandle();
         float f = 0, s = 0;
         if (glfwGetKey(handle, GLFW_KEY_W) == GLFW_PRESS) f++;
         if (glfwGetKey(handle, GLFW_KEY_S) == GLFW_PRESS) f--;
         if (glfwGetKey(handle, GLFW_KEY_A) == GLFW_PRESS) s++;
         if (glfwGetKey(handle, GLFW_KEY_D) == GLFW_PRESS) s--;

         event.setForward(f);
         event.setStrafe(s);
         event.setJump(glfwGetKey(handle, GLFW_KEY_SPACE) == GLFW_PRESS);
         event.setSneak(glfwGetKey(handle, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS || glfwGetKey(handle, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS);
         event.setSprint(glfwGetKey(handle, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS || glfwGetKey(handle, GLFW_KEY_RIGHT_CONTROL) == GLFW_PRESS);
      }
   };
}
