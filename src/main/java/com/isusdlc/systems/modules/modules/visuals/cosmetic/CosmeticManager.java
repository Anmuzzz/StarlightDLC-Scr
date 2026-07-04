package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import lombok.Getter;
import lombok.Setter;

public class CosmeticManager {
    @Getter
    private static final CosmeticManager instance = new CosmeticManager();

    @Getter
    private final CosmeticAnimator animator = new CosmeticAnimator();
    @Getter
    private final CosmeticRenderer renderer = new CosmeticRenderer();

    @Getter @Setter
    private CosmeticModel activeModel;
    @Getter @Setter
    private CosmeticModel activePet;
    @Getter @Setter
    private CosmeticModel activeAccessory;

    private boolean initialized;

    public void init() {
        String modelId = activeModel != null ? activeModel.getId() : null;
        String petId = activePet != null ? activePet.getId() : null;
        String accessoryId = activeAccessory != null ? activeAccessory.getId() : null;

        for (CosmeticModel model : CosmeticRepository.getInstance().all()) {
            model.resetTexture();
        }
        CosmeticRepository.getInstance().reload();
        animator.clearState();

        CosmeticRepository repo = CosmeticRepository.getInstance();
        activeModel = modelId != null ? repo.get(modelId) : null;
        activePet = petId != null ? repo.get(petId) : null;
        activeAccessory = accessoryId != null ? repo.get(accessoryId) : null;

        initialized = true;
    }

    public CosmeticModel getActive(CosmeticType type) {
        return switch (type) {
            case PET -> activePet;
            case ACCESSORY -> activeAccessory;
            case MODEL -> activeModel;
        };
    }

    public void setActive(CosmeticType type, CosmeticModel model) {
        if (model != null) {
            CosmeticModel resolved = CosmeticRepository.getInstance().get(model.getId());
            if (resolved != null) {
                model = resolved;
            }
        }
        switch (type) {
            case PET -> activePet = model;
            case ACCESSORY -> activeAccessory = model;
            case MODEL -> activeModel = model;
        }
    }

    public void clear(CosmeticType type) {
        setActive(type, null);
    }

    public boolean hasAnyActive() {
        return activeModel != null || activePet != null || activeAccessory != null;
    }

    public boolean isActive() {
        return activeModel != null;
    }
}
