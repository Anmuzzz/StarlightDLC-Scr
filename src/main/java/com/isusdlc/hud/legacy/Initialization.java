package com.isusdlc.hud.legacy;

import java.util.ArrayList;
import java.util.List;

public final class Initialization {
    private static Initialization instance;

    private Manager manager;

    public static Initialization getInstance() {
        if (instance == null) instance = new Initialization();
        return instance;
    }

    public Manager getManager() {
        if (manager == null) manager = new Manager();
        return manager;
    }

    public static class Manager {
        private ModuleProvider moduleProvider;

        public ModuleProvider getModuleProvider() {
            if (moduleProvider == null) moduleProvider = new ModuleProvider();
            return moduleProvider;
        }
    }

    public static class ModuleProvider {
        public List<ModuleStructure> getModuleStructures() {
            return new ArrayList<>();
        }
    }

    private Initialization() {
    }
}
