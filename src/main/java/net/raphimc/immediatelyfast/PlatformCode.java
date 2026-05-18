package net.raphimc.immediatelyfast;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.Optional;

public final class PlatformCode {

    private PlatformCode() {
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static Optional<String> getModVersion(String mod) {
        return FabricLoader.getInstance().getModContainer(mod)
                .map(container -> container.getMetadata().getVersion().getFriendlyString());
    }

    public static void checkModCompatibility() {
        if (FabricLoader.getInstance().isModLoaded("optifabric")) {
            ImmediatelyFast.LOGGER.warn("OptiFabric detected. ImmediatelyFast may be incompatible.");
        }
    }

}

