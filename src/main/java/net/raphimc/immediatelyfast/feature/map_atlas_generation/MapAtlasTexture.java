package net.raphimc.immediatelyfast.feature.map_atlas_generation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.raphimc.immediatelyfast.ImmediatelyFast;

public final class MapAtlasTexture implements AutoCloseable {

    public static final int MAP_SIZE = 128;

    private final int id;
    private final int atlasSize;
    private final int mapsPerAtlas;
    private final Identifier identifier;
    private final NativeImageBackedTexture texture;
    private int mapCount;

    public MapAtlasTexture(int id) {
        this.id = id;
        ImmediatelyFast.loadConfig();
        this.atlasSize = Math.max(MAP_SIZE, ImmediatelyFast.config.map_atlas_size);
        this.mapsPerAtlas = (this.atlasSize / MAP_SIZE) * (this.atlasSize / MAP_SIZE);
        this.identifier = new Identifier("immediatelyfast", "map_atlas/" + id);
        this.texture = new NativeImageBackedTexture(this.atlasSize, this.atlasSize, true);
        MinecraftClient.getInstance().getTextureManager().registerTexture(this.identifier, this.texture);
    }

    public int getNextMapLocation() {
        if (this.mapCount >= this.mapsPerAtlas) {
            return -1;
        }

        final int atlasX = this.mapCount % (this.atlasSize / MAP_SIZE);
        final int atlasY = this.mapCount / (this.atlasSize / MAP_SIZE);
        this.mapCount++;
        return (this.id << 16) | (atlasX << 8) | atlasY;
    }

    public int getId() {
        return this.id;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public int getAtlasSize() {
        return this.atlasSize;
    }

    public NativeImageBackedTexture getTexture() {
        return this.texture;
    }

    @Override
    public void close() {
        this.texture.close();
        MinecraftClient.getInstance().getTextureManager().destroyTexture(this.identifier);
    }

}

