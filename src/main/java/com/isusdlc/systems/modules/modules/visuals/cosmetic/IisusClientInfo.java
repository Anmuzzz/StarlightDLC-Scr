package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import net.fabricmc.loader.api.FabricLoader;

public class IisusClientInfo {
    public static final String NAME = "isusdlc";
    public static final String CONFIG_PATH_COSMETICS = FabricLoader.getInstance().getGameDir().resolve("isusdlc/cosmetics").toString();
    public static final String GAME_PATH = FabricLoader.getInstance().getGameDir().toString();
}
