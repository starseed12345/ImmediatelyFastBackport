package net.raphimc.immediatelyfast.injection.mixins.hud_batching;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextRenderer.class)
public abstract class MixinTextRenderer {

    @Redirect(
            method = {
                    "draw(Ljava/lang/String;FFILnet/minecraft/util/math/Matrix4f;ZZ)I",
                    "draw(Lnet/minecraft/text/OrderedText;FFILnet/minecraft/util/math/Matrix4f;Z)I"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;immediate(Lnet/minecraft/client/render/BufferBuilder;)Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;")
    )
    private VertexConsumerProvider.Immediate immediatelyfast$useHudTextBatch(BufferBuilder fallbackBuffer) {
        if (BatchingBuffers.TEXT_CONSUMER instanceof VertexConsumerProvider.Immediate) {
            return (VertexConsumerProvider.Immediate) BatchingBuffers.TEXT_CONSUMER;
        }
        return VertexConsumerProvider.immediate(fallbackBuffer);
    }

    @Redirect(
            method = {
                    "draw(Ljava/lang/String;FFILnet/minecraft/util/math/Matrix4f;ZZ)I",
                    "draw(Lnet/minecraft/text/OrderedText;FFILnet/minecraft/util/math/Matrix4f;Z)I"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw()V")
    )
    private void immediatelyfast$deferHudTextDraw(VertexConsumerProvider.Immediate immediate) {
        if (immediate != BatchingBuffers.getHudBatch()) {
            immediate.draw();
        }
    }

}

