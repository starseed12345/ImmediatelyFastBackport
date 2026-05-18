package net.raphimc.immediatelyfast.feature.core;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.raphimc.immediatelyfast.ImmediatelyFast;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class ImmediateAdapter extends VertexConsumerProvider.Immediate implements AutoCloseable {

    private static final BufferBuilder FALLBACK_BUFFER = new BufferBuilder(0);

    protected final Reference2ObjectMap<RenderLayer, BufferBuilder> fallbackBuffers = new Reference2ObjectLinkedOpenHashMap<>();
    protected final ReferenceSet<RenderLayer> activeLayers = new ReferenceLinkedOpenHashSet<>();
    private boolean drawFallbackLayersFirst;

    public ImmediateAdapter() {
        this(ImmutableMap.of());
    }

    public ImmediateAdapter(Map<RenderLayer, BufferBuilder> layerBuffers) {
        super(FALLBACK_BUFFER, layerBuffers);
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer layer) {
        final Optional<RenderLayer> newLayer = layer.asOptional();
        if (!this.drawFallbackLayersFirst && !this.currentLayer.equals(newLayer) && this.currentLayer.isPresent() && !this.layerBuffers.containsKey(this.currentLayer.get())) {
            this.drawFallbackLayersFirst = true;
        }
        this.currentLayer = newLayer;

        final BufferBuilder bufferBuilder = this.getOrCreateBufferBuilder(layer);
        if (!bufferBuilder.isBuilding()) {
            bufferBuilder.begin(layer.getDrawMode(), layer.getVertexFormat());
        }
        if (ImmediatelyFast.config.debug_only_use_last_usage_for_batch_ordering && this.activeLayers.contains(layer)) {
            this.activeLayers.remove(layer);
            this.activeLayers.add(layer);
        } else {
            this.activeLayers.add(layer);
        }
        return bufferBuilder;
    }

    @Override
    public void draw() {
        if (this.activeLayers.isEmpty()) {
            this.close();
            return;
        }

        this.drawCurrentLayer();
        for (RenderLayer layer : this.layerBuffers.keySet()) {
            this.draw(layer);
        }
    }

    @Override
    public void draw(RenderLayer layer) {
        if (this.drawFallbackLayersFirst) {
            this.drawCurrentLayer();
        }

        this.activeLayers.remove(layer);
        this.drawLayer(layer);
        this.fallbackBuffers.remove(layer);

        if (this.currentLayer.equals(layer.asOptional())) {
            this.currentLayer = Optional.empty();
        }
    }

    @Override
    public void close() {
        this.currentLayer = Optional.empty();
        this.drawFallbackLayersFirst = false;

        for (RenderLayer layer : this.activeLayers) {
            for (BufferBuilder bufferBuilder : this.getBufferBuilder(layer)) {
                if (bufferBuilder.isBuilding()) {
                    bufferBuilder.end();
                    bufferBuilder.clear();
                }
            }
        }

        this.activeLayers.clear();
        this.fallbackBuffers.clear();
    }

    public boolean hasActiveLayers() {
        return !this.activeLayers.isEmpty();
    }

    protected abstract void drawLayer(RenderLayer layer);

    protected void drawCurrentLayer() {
        this.currentLayer = Optional.empty();
        this.drawFallbackLayersFirst = false;

        for (RenderLayer layer : this.activeLayers.toArray(new RenderLayer[0])) {
            if (!this.layerBuffers.containsKey(layer)) {
                this.draw(layer);
            }
        }
    }

    protected BufferBuilder getOrCreateBufferBuilder(RenderLayer layer) {
        if (this.layerBuffers.containsKey(layer)) {
            return this.layerBuffers.get(layer);
        }

        BufferBuilder bufferBuilder = this.fallbackBuffers.get(layer);
        if (bufferBuilder == null) {
            bufferBuilder = BufferBuilderPool.get();
            this.fallbackBuffers.put(layer, bufferBuilder);
        }
        return bufferBuilder;
    }

    protected Set<BufferBuilder> getBufferBuilder(RenderLayer layer) {
        if (this.fallbackBuffers.containsKey(layer)) {
            return Collections.singleton(this.fallbackBuffers.get(layer));
        }
        if (this.layerBuffers.containsKey(layer)) {
            return Collections.singleton(this.layerBuffers.get(layer));
        }
        return Collections.emptySet();
    }

}

