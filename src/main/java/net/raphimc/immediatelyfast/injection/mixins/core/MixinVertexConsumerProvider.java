package net.raphimc.immediatelyfast.injection.mixins.core;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.core.BatchableImmediate;
import net.raphimc.immediatelyfast.feature.core.VanillaImmediate;
import net.raphimc.immediatelyfast.injection.interfaces.IBufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;

@Mixin(VertexConsumerProvider.class)
public interface MixinVertexConsumerProvider {

    
    @Overwrite
    static VertexConsumerProvider.Immediate immediate(Map<RenderLayer, BufferBuilder> layerBuffers, BufferBuilder fallbackBuffer) {
        ImmediatelyFast.loadConfig();
        if (!ImmediatelyFast.config.enhanced_batching || ImmediatelyFast.config.debug_only_and_not_recommended_disable_universal_batching || fallbackBuffer == null) {
            if (fallbackBuffer == null) {
                fallbackBuffer = Tessellator.getInstance().getBuffer();
            }
            return new VanillaImmediate(fallbackBuffer, layerBuffers);
        }

        if (fallbackBuffer != Tessellator.getInstance().getBuffer()) {
            ((IBufferBuilder) fallbackBuffer).immediatelyfast$release();
        }
        return new BatchableImmediate(layerBuffers);
    }

}

