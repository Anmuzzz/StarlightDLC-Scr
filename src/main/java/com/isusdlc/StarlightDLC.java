package com.isusdlc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public final class StarlightDLC implements ClientModInitializer {

    private static StarlightDLC instance;
    private static boolean initialized;

    public static StarlightDLC getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!initialized) {
                initialized = true;
                elegant.INSTANCE.initialize();
            }
        });
    }
}
