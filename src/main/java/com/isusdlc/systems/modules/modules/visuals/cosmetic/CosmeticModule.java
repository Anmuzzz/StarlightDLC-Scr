package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ButtonSetting;

@ModuleInfo(name = "Cosmetic", category = ModuleCategory.VISUALS, desc = "Косметические модели")
public class CosmeticModule extends BaseModule {
    private static final CosmeticModule instance = new CosmeticModule();

    public static CosmeticModule getInstance() {
        return instance;
    }

    private final ButtonSetting openGui = new ButtonSetting(this, "Open GUI")
        .action(() -> net.minecraft.client.MinecraftClient.getInstance().execute(() ->
            net.minecraft.client.MinecraftClient.getInstance().setScreen(new ScreenCosmetic())));

    private final ButtonSetting openFolder = new ButtonSetting(this, "Open cosmetics folder")
        .action(CosmeticRepository::openCosmeticsFolder);

    private final ButtonSetting refreshCatalog = new ButtonSetting(this, "Refresh catalog")
        .action(() -> {
            CosmeticManager.getInstance().init();
        });

    public CosmeticModule() {
        CosmeticManager.getInstance().init();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        CosmeticManager.getInstance().init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        CosmeticManager mgr = CosmeticManager.getInstance();
        mgr.setActiveModel(null);
        mgr.setActivePet(null);
        mgr.setActiveAccessory(null);
    }

    private final EventListener<ClientPlayerTickEvent> listener = event -> {
        if (!isEnabled() || !CosmeticManager.getInstance().hasAnyActive()) return;
        if (mc.player == null) return;
        CosmeticManager.getInstance().getAnimator().tick(mc.player);
    };
}
