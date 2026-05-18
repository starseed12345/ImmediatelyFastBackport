package net.raphimc.immediatelyfast.feature.core;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.Map;

public final class VanillaImmediate extends VertexConsumerProvider.Immediate {

    public VanillaImmediate(BufferBuilder fallbackBuffer, Map<RenderLayer, BufferBuilder> layerBuffers) {
        super(fallbackBuffer, layerBuffers);
    }

}

