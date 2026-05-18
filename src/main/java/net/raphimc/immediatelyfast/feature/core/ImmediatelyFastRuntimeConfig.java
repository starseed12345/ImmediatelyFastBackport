package net.raphimc.immediatelyfast.feature.core;

public final class ImmediatelyFastRuntimeConfig {

    public boolean font_atlas_resizing;
    public boolean disable_fast_buffer_upload;

    public ImmediatelyFastRuntimeConfig(ImmediatelyFastConfig config) {
        this.font_atlas_resizing = config.font_atlas_resizing;
        this.disable_fast_buffer_upload = false;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if ("font_atlas_resizing".equals(key)) {
            return this.font_atlas_resizing;
        }
        if ("disable_fast_buffer_upload".equals(key)) {
            return this.disable_fast_buffer_upload;
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {
        return defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return defaultValue;
    }

}

