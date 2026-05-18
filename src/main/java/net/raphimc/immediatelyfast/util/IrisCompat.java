package net.raphimc.immediatelyfast.util;

import net.fabricmc.loader.api.FabricLoader;

public final class IrisCompat {

    public static final boolean IRIS_LOADED = FabricLoader.getInstance().isModLoaded("iris");

    private IrisCompat() {
    }

}

