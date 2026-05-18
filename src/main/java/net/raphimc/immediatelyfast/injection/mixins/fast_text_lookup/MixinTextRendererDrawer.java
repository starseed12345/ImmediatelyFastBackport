package net.raphimc.immediatelyfast.injection.mixins.fast_text_lookup;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.font.TextRenderer$Drawer")
public abstract class MixinTextRendererDrawer {

    @Unique
    private Identifier immediatelyfast$lastFontId;

    @Unique
    private FontStorage immediatelyfast$lastFontStorage;

    @Unique
    private RenderLayer immediatelyfast$lastRenderLayer;

    @Unique
    private VertexConsumer immediatelyfast$lastVertexConsumer;

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;method_27519(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/font/FontStorage;"))
    private FontStorage immediatelyfast$cacheFontStorage(TextRenderer textRenderer, Identifier fontId) {
        if (fontId.equals(this.immediatelyfast$lastFontId) && this.immediatelyfast$lastFontStorage != null) {
            return this.immediatelyfast$lastFontStorage;
        }

        this.immediatelyfast$lastFontId = fontId;
        this.immediatelyfast$lastFontStorage = ((TextRendererAccess) textRenderer).immediatelyfast$getFontStorage(fontId);
        return this.immediatelyfast$lastFontStorage;
    }

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer immediatelyfast$cacheVertexConsumer(VertexConsumerProvider vertexConsumers, RenderLayer renderLayer) {
        if (this.immediatelyfast$lastRenderLayer == renderLayer && this.immediatelyfast$lastVertexConsumer != null) {
            return this.immediatelyfast$lastVertexConsumer;
        }

        this.immediatelyfast$lastRenderLayer = renderLayer;
        this.immediatelyfast$lastVertexConsumer = vertexConsumers.getBuffer(renderLayer);
        return this.immediatelyfast$lastVertexConsumer;
    }

}

