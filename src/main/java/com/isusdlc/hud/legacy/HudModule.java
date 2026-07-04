package com.isusdlc.hud.legacy;

public final class HudModule {
    private static HudModule instance;

    public static HudModule getInstance() {
        if (instance == null) instance = new HudModule();
        return instance;
    }

    public final BooleanSetting showBps = new BooleanSetting(true);

    public static class BooleanSetting {
        private boolean value;

        public BooleanSetting(boolean defaultValue) {
            this.value = defaultValue;
        }

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }

    private HudModule() {
    }
}
