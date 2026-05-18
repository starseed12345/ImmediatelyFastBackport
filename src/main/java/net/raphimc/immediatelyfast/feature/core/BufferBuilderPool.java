package net.raphimc.immediatelyfast.feature.core;

import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.client.render.BufferBuilder;
import net.raphimc.immediatelyfast.injection.interfaces.IBufferBuilder;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;

public final class BufferBuilderPool {

    private static final Set<Pair<BufferBuilder, Long>> POOL = new ReferenceArraySet<>(256);
    private static long lastCleanup;

    private BufferBuilderPool() {
    }

    public static BufferBuilder get() {
        if (lastCleanup < System.currentTimeMillis() - 5_000L) {
            lastCleanup = System.currentTimeMillis();
            cleanup();
        }

        for (Pair<BufferBuilder, Long> entry : POOL) {
            final BufferBuilder bufferBuilder = entry.getKey();
            if (!bufferBuilder.isBuilding() && !((IBufferBuilder) bufferBuilder).immediatelyfast$isReleased()) {
                entry.setValue(System.currentTimeMillis());
                return bufferBuilder;
            }
        }

        final BufferBuilder bufferBuilder = new BufferBuilder(256);
        POOL.add(new MutablePair<BufferBuilder, Long>(bufferBuilder, System.currentTimeMillis()));
        return bufferBuilder;
    }

    public static int getAllocatedSize() {
        cleanup();
        return POOL.size();
    }

    public static void onEndFrame() {
        cleanup();
    }

    private static void cleanup() {
        POOL.removeIf(entry -> ((IBufferBuilder) entry.getKey()).immediatelyfast$isReleased());
        POOL.removeIf(entry -> {
            if (entry.getValue() < System.currentTimeMillis() - 120_000L) {
                ((IBufferBuilder) entry.getKey()).immediatelyfast$release();
                return true;
            }
            return false;
        });
    }

}

