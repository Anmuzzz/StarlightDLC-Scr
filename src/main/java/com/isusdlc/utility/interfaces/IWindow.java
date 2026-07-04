package com.isusdlc.utility.interfaces;

import net.minecraft.client.util.Window;
import net.minecraft.client.MinecraftClient;

public interface IWindow {
   Window mw = MinecraftClient.getInstance().getWindow();
}
