package net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation;

import net.minecraft.block.MapColor;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import net.raphimc.immediatelyfast.injection.interfaces.IMapRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture.MAP_SIZE;

@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture", priority = 1100)
public abstract class MixinMapRendererMapTexture {

    @Shadow
    @Final
    private MapState mapState;

    @Mutable
    @Shadow
    @Final
    private NativeImageBackedTexture texture;

    @Unique
    private int immediatelyfast$atlasX;

    @Unique
    private int immediatelyfast$atlasY;

    @Unique
    private MapAtlasTexture immediatelyfast$atlasTexture;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Object;<init>()V", shift = At.Shift.AFTER, remap = false))
    private void immediatelyfast$initAtlasParameters(MapRenderer mapRenderer, MapState state, CallbackInfo ci) {
        final int packedLocation = ((IMapRenderer) mapRenderer).immediatelyfast$getAtlasMapping(state.getId());
        if (packedLocation == -1) {
            ImmediatelyFast.LOGGER.warn("Map " + state.getId() + " is not in an atlas");
            return;
        }

        this.immediatelyfast$atlasX = ((packedLocation >> 8) & 255) * MAP_SIZE;
        this.immediatelyfast$atlasY = (packedLocation & 255) * MAP_SIZE;
        this.immediatelyfast$atlasTexture = ((IMapRenderer) mapRenderer).immediatelyfast$getMapAtlasTexture(packedLocation >> 16);
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "(IIZ)Lnet/minecraft/client/texture/NativeImageBackedTexture;"))
    private NativeImageBackedTexture immediatelyfast$dontAllocateTexture(int width, int height, boolean useMipmaps) {
        if (this.immediatelyfast$atlasTexture != null) {
            return new NativeImageBackedTexture(1, 1, false);
        }
        return new NativeImageBackedTexture(width, height, useMipmaps);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;registerDynamicTexture(Ljava/lang/String;Lnet/minecraft/client/texture/NativeImageBackedTexture;)Lnet/minecraft/util/Identifier;"))
    private Identifier immediatelyfast$getAtlasTextureIdentifier(TextureManager textureManager, String id, NativeImageBackedTexture texture) {
        if (this.immediatelyfast$atlasTexture != null) {
            texture.close();
            this.texture = null;
            return this.immediatelyfast$atlasTexture.getIdentifier();
        }
        return textureManager.registerDynamicTexture(id, texture);
    }

    @Inject(method = "updateTexture", at = @At("HEAD"), cancellable = true)
    private void immediatelyfast$updateAtlasTexture(CallbackInfo ci) {
        if (this.immediatelyfast$atlasTexture == null) {
            return;
        }

        ci.cancel();
        final NativeImageBackedTexture atlasTexture = this.immediatelyfast$atlasTexture.getTexture();
        final NativeImage atlasImage = atlasTexture.getImage();
        if (atlasImage == null) {
            throw new IllegalStateException("Atlas texture has already been closed");
        }

        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                final int i = x + y * MAP_SIZE;
                final int color = this.mapState.colors[i] & 255;
                if (color / 4 == 0) {
                    atlasImage.setPixelColor(this.immediatelyfast$atlasX + x, this.immediatelyfast$atlasY + y, 0);
                } else {
                    atlasImage.setPixelColor(this.immediatelyfast$atlasX + x, this.immediatelyfast$atlasY + y, MapColor.COLORS[color / 4].getRenderColor(color & 3));
                }
            }
        }

        atlasTexture.bindTexture();
        atlasImage.upload(0, this.immediatelyfast$atlasX, this.immediatelyfast$atlasY, this.immediatelyfast$atlasX, this.immediatelyfast$atlasY, MAP_SIZE, MAP_SIZE, false, false);
    }

    @Redirect(
            method = "draw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;texture(FF)Lnet/minecraft/client/render/VertexConsumer;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;vertex(Lnet/minecraft/util/math/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;next()V", ordinal = 3)
            )
    )
    private VertexConsumer immediatelyfast$drawAtlasTexture(VertexConsumer instance, float u, float v) {
        if (this.immediatelyfast$atlasTexture != null) {
            final int atlasSize = this.immediatelyfast$atlasTexture.getAtlasSize();
            if (u == 0F && v == 1F) {
                u = (float) this.immediatelyfast$atlasX / atlasSize;
                v = (float) (this.immediatelyfast$atlasY + MAP_SIZE) / atlasSize;
            } else if (u == 1F && v == 1F) {
                u = (float) (this.immediatelyfast$atlasX + MAP_SIZE) / atlasSize;
                v = (float) (this.immediatelyfast$atlasY + MAP_SIZE) / atlasSize;
            } else if (u == 1F && v == 0F) {
                u = (float) (this.immediatelyfast$atlasX + MAP_SIZE) / atlasSize;
                v = (float) this.immediatelyfast$atlasY / atlasSize;
            } else if (u == 0F && v == 0F) {
                u = (float) this.immediatelyfast$atlasX / atlasSize;
                v = (float) this.immediatelyfast$atlasY / atlasSize;
            }
        }

        return instance.texture(u, v);
    }

    @Inject(method = "close", at = @At("HEAD"), cancellable = true)
    private void immediatelyfast$dontCloseAtlasTexture(CallbackInfo ci) {
        if (this.immediatelyfast$atlasTexture != null) {
            ci.cancel();
        }
    }

}

