package com.isusdlc.hud.legacy;

public class ModuleStructure {
    private final String name;
    private final int key;
    private final boolean state;

    public ModuleStructure(String name, int key, boolean state) {
        this.name = name;
        this.key = key;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public boolean isState() {
        return state;
    }
}
