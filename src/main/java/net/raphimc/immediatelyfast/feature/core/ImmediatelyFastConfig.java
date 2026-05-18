package net.raphimc.immediatelyfast.feature.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.raphimc.immediatelyfast.ImmediatelyFast;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImmediatelyFastConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean enhanced_batching = true;
    public boolean font_atlas_resizing = true;
    public int font_atlas_size = 1024;
    public boolean map_atlas_generation = true;
    public int map_atlas_size = 2048;
    public boolean skip_text_translucency_sorting = true;
    public boolean fast_text_lookup = true;
    public boolean avoid_redundant_framebuffer_switching = true;
    public boolean fix_slow_buffer_upload_on_apple_gpu = true;
    public boolean experimental_disable_resource_pack_conflict_handling = false;
    public boolean experimental_sign_text_buffering = false;
    public boolean experimental_disable_error_checking = false;
    public boolean debug_only_and_not_recommended_disable_universal_batching = false;
    public boolean debug_only_and_not_recommended_disable_mod_conflict_handling = false;
    public boolean debug_only_and_not_recommended_disable_hardware_conflict_handling = false;
    public boolean debug_only_print_additional_error_information = false;
    public boolean debug_only_use_last_usage_for_batch_ordering = false;
    public boolean debug_only_detailed_memory_leak_detection = false;
    public boolean dont_add_info_into_debug_hud = false;
    public boolean hud_batching = false;
    public boolean experimental_hud_batching = false;
    public boolean fast_buffer_upload = false;
    public long fast_buffer_upload_size_mb = 256;
    public boolean fast_buffer_upload_explicit_flush = true;

    public static ImmediatelyFastConfig load() {
        final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("immediatelyfast.json");
        ImmediatelyFastConfig config = new ImmediatelyFastConfig();

        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
                final ImmediatelyFastConfig loaded = GSON.fromJson(reader, ImmediatelyFastConfig.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (Exception e) {
                ImmediatelyFast.LOGGER.warn("Failed to load ImmediatelyFast config. Defaults will be used.", e);
            }
        }

        config.normalize();

        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            ImmediatelyFast.LOGGER.warn("Failed to save ImmediatelyFast config.", e);
        }

        return config;
    }

    private void normalize() {
        if (this.font_atlas_size <= 0) {
            this.font_atlas_size = 1024;
        }
        if (this.map_atlas_size <= 0) {
            this.map_atlas_size = 2048;
        }
        if (!isPowerOfTwo(this.font_atlas_size)) {
            ImmediatelyFast.LOGGER.warn("Font atlas size {} is not a power of two. Rounding up to the next power of two.", this.font_atlas_size);
            this.font_atlas_size = roundToPowerOfTwo(this.font_atlas_size);
        }
        if (!isPowerOfTwo(this.map_atlas_size)) {
            ImmediatelyFast.LOGGER.warn("Map atlas size {} is not a power of two. Rounding up to the next power of two.", this.map_atlas_size);
            this.map_atlas_size = roundToPowerOfTwo(this.map_atlas_size);
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if ("enhanced_batching".equals(key)) {
            return this.enhanced_batching;
        }
        if ("font_atlas_resizing".equals(key)) {
            return this.font_atlas_resizing;
        }
        if ("map_atlas_generation".equals(key)) {
            return this.map_atlas_generation;
        }
        if ("skip_text_translucency_sorting".equals(key)) {
            return this.skip_text_translucency_sorting;
        }
        if ("fast_text_lookup".equals(key)) {
            return this.fast_text_lookup;
        }
        if ("avoid_redundant_framebuffer_switching".equals(key)) {
            return this.avoid_redundant_framebuffer_switching;
        }
        if ("fix_slow_buffer_upload_on_apple_gpu".equals(key)) {
            return this.fix_slow_buffer_upload_on_apple_gpu;
        }
        if ("experimental_disable_resource_pack_conflict_handling".equals(key)) {
            return this.experimental_disable_resource_pack_conflict_handling;
        }
        if ("experimental_sign_text_buffering".equals(key)) {
            return this.experimental_sign_text_buffering;
        }
        if ("experimental_disable_error_checking".equals(key)) {
            return this.experimental_disable_error_checking;
        }
        if ("hud_batching".equals(key)) {
            return this.hud_batching && this.experimental_hud_batching;
        }
        if ("fast_buffer_upload".equals(key)) {
            return this.fast_buffer_upload;
        }
        if ("debug_only_and_not_recommended_disable_mod_conflict_handling".equals(key)) {
            return this.debug_only_and_not_recommended_disable_mod_conflict_handling;
        }
        if ("debug_only_and_not_recommended_disable_hardware_conflict_handling".equals(key)) {
            return this.debug_only_and_not_recommended_disable_hardware_conflict_handling;
        }
        if ("debug_only_print_additional_error_information".equals(key)) {
            return this.debug_only_print_additional_error_information;
        }
        if ("debug_only_use_last_usage_for_batch_ordering".equals(key)) {
            return this.debug_only_use_last_usage_for_batch_ordering;
        }
        if ("debug_only_detailed_memory_leak_detection".equals(key)) {
            return this.debug_only_detailed_memory_leak_detection;
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        if ("font_atlas_size".equals(key)) {
            return this.font_atlas_size;
        }
        if ("map_atlas_size".equals(key)) {
            return this.map_atlas_size;
        }
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {
        if ("fast_buffer_upload_size_mb".equals(key)) {
            return this.fast_buffer_upload_size_mb;
        }
        return defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return defaultValue;
    }

    public boolean isHudBatchingEnabled() {
        return this.hud_batching && this.experimental_hud_batching;
    }

    private static boolean isPowerOfTwo(int value) {
        return value > 0 && (value & value - 1) == 0;
    }

    private static int roundToPowerOfTwo(int value) {
        int rounded = 1;
        while (rounded < value && rounded < 16384) {
            rounded <<= 1;
        }
        return rounded;
    }

}

