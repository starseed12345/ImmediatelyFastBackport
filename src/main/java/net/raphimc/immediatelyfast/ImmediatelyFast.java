package net.raphimc.immediatelyfast;

import net.fabricmc.api.ClientModInitializer;
import net.raphimc.immediatelyfast.feature.core.ImmediatelyFastConfig;
import net.raphimc.immediatelyfast.feature.core.BufferBuilderPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public final class ImmediatelyFast implements ClientModInitializer {

    public static final String MOD_ID = "immediatelyfast";
    public static final Logger LOGGER = LogManager.getLogger("ImmediatelyFast");
    public static String VERSION = "unknown";
    public static ImmediatelyFastConfig config;
    public static net.raphimc.immediatelyfast.feature.core.ImmediatelyFastRuntimeConfig runtimeConfig;

    @Override
    public void onInitializeClient() {
        loadConfig();
        refreshVersion();
        PlatformCode.checkModCompatibility();
        LOGGER.info("ImmediatelyFast 1.16.5 backport initialized");
    }

    public static void loadConfig() {
        if (config == null) {
            config = ImmediatelyFastConfig.load();
            runtimeConfig = new net.raphimc.immediatelyfast.feature.core.ImmediatelyFastRuntimeConfig(config);
        }
    }

    public static void onRenderSystemInit() {
        loadConfig();
        refreshVersion();
        LOGGER.info("Initializing ImmediatelyFast {} on {} ({}) with OpenGL {}",
                VERSION,
                GL11.glGetString(GL11.GL_RENDERER),
                GL11.glGetString(GL11.GL_VENDOR),
                GL11.glGetString(GL11.GL_VERSION));
    }

    public static void onEndFrame() {
        BufferBuilderPool.onEndFrame();
    }

    public static void onLevelChange() {
    }

    private static void refreshVersion() {
        VERSION = PlatformCode.getModVersion(MOD_ID).orElse(VERSION);
    }

}

