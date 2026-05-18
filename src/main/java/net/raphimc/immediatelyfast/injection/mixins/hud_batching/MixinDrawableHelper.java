package net.raphimc.immediatelyfast.injection.mixins.hud_batching;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Matrix4f;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.feature.batching.BatchingRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawableHelper.class)
public abstract class MixinDrawableHelper {

    @Inject(method = "fill(Lnet/minecraft/util/math/Matrix4f;IIIII)V", at = @At("HEAD"), cancellable = true)
    private static void immediatelyfast$batchFill(Matrix4f matrix, int x1, int y1, int x2, int y2, int color, CallbackInfo ci) {
        if (BatchingBuffers.FILL_CONSUMER == null) {
            return;
        }

        if (x1 < x2) {
            final int i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            final int i = y1;
            y1 = y2;
            y2 = i;
        }

        final float alpha = (float) (color >> 24 & 255) / 255.0F;
        final float red = (float) (color >> 16 & 255) / 255.0F;
        final float green = (float) (color >> 8 & 255) / 255.0F;
        final float blue = (float) (color & 255) / 255.0F;
        final VertexConsumer vertexConsumer = BatchingBuffers.FILL_CONSUMER.getBuffer(BatchingRenderLayers.GUI_FILL);
        vertexConsumer.vertex(matrix, (float) x1, (float) y2, 0.0F).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix, (float) x2, (float) y2, 0.0F).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix, (float) x2, (float) y1, 0.0F).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix, (float) x1, (float) y1, 0.0F).color(red, green, blue, alpha).next();
        ci.cancel();
    }

}

