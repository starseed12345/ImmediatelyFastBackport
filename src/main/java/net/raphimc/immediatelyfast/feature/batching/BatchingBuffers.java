package net.raphimc.immediatelyfast.feature.batching;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.core.BatchableImmediate;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BatchingBuffers {

    public static VertexConsumerProvider FILL_CONSUMER;
    public static VertexConsumerProvider TEXT_CONSUMER;

    private static final BatchableImmediate HUD_BATCH = new BatchableImmediate(createLayerBuffers(BatchingRenderLayers.GUI_FILL));

    private BatchingBuffers() {
    }

    public static void beginHudBatching() {
        ImmediatelyFast.loadConfig();
        if (!ImmediatelyFast.config.isHudBatchingEnabled() || !ImmediatelyFast.config.enhanced_batching) {
            return;
        }

        if (HUD_BATCH.hasActiveLayers()) {
            ImmediatelyFast.LOGGER.warn("HUD batching was already active. Closing the stale batch.");
            HUD_BATCH.close();
        }

        FILL_CONSUMER = HUD_BATCH;
        TEXT_CONSUMER = HUD_BATCH;
    }

    public static void endHudBatching() {
        if (FILL_CONSUMER == null && TEXT_CONSUMER == null) {
            return;
        }

        FILL_CONSUMER = null;
        TEXT_CONSUMER = null;
        HUD_BATCH.draw();
    }

    public static boolean isHudBatching() {
        return FILL_CONSUMER != null || TEXT_CONSUMER != null;
    }

    public static BatchableImmediate getHudBatch() {
        return HUD_BATCH;
    }

    public static Map<RenderLayer, BufferBuilder> createLayerBuffers(RenderLayer... layers) {
        final Map<RenderLayer, BufferBuilder> layerBuffers = new LinkedHashMap<>();
        for (RenderLayer layer : layers) {
            layerBuffers.put(layer, new BufferBuilder(layer.getExpectedBufferSize()));
        }
        return layerBuffers;
    }

}

