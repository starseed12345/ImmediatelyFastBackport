package net.raphimc.immediatelyfast.injection.interfaces;

import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;

public interface IMapRenderer {

    MapAtlasTexture immediatelyfast$getMapAtlasTexture(int atlasId);

    int immediatelyfast$getAtlasMapping(String mapId);

}

