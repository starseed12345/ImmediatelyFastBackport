package net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.item.map.MapState;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import net.raphimc.immediatelyfast.injection.interfaces.IMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapRenderer.class)
public abstract class MixinMapRenderer implements IMapRenderer {

    @Unique
    private final Int2ObjectMap<MapAtlasTexture> immediatelyfast$mapAtlasTextures = new Int2ObjectOpenHashMap<>();

    @Unique
    private final Object2IntMap<String> immediatelyfast$mapIdToAtlasMapping = new Object2IntOpenHashMap<>();

    @Inject(method = "clearStateTextures", at = @At("RETURN"))
    private void immediatelyfast$clearMapAtlasTextures(CallbackInfo ci) {
        for (MapAtlasTexture texture : this.immediatelyfast$mapAtlasTextures.values()) {
            texture.close();
        }

        this.immediatelyfast$mapAtlasTextures.clear();
        this.immediatelyfast$mapIdToAtlasMapping.clear();
    }

    @Inject(method = "getMapTexture", at = @At("HEAD"))
    private void immediatelyfast$createMapAtlasTexture(MapState state, CallbackInfoReturnable<Object> cir) {
        this.immediatelyfast$mapIdToAtlasMapping.computeIfAbsent(state.getId(), key -> {
            for (MapAtlasTexture atlasTexture : this.immediatelyfast$mapAtlasTextures.values()) {
                final int location = atlasTexture.getNextMapLocation();
                if (location != -1) {
                    return location;
                }
            }

            final MapAtlasTexture atlasTexture = new MapAtlasTexture(this.immediatelyfast$mapAtlasTextures.size());
            this.immediatelyfast$mapAtlasTextures.put(atlasTexture.getId(), atlasTexture);
            return atlasTexture.getNextMapLocation();
        });
    }

    @Override
    public MapAtlasTexture immediatelyfast$getMapAtlasTexture(int atlasId) {
        return this.immediatelyfast$mapAtlasTextures.get(atlasId);
    }

    @Override
    public int immediatelyfast$getAtlasMapping(String mapId) {
        return this.immediatelyfast$mapIdToAtlasMapping.getOrDefault(mapId, -1);
    }

}

