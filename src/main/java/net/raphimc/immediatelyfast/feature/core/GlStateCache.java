package net.raphimc.immediatelyfast.feature.core;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.raphimc.immediatelyfast.ImmediatelyFast;

public final class GlStateCache {

    private static final Int2IntOpenHashMap FRAMEBUFFERS = new Int2IntOpenHashMap();
    private static int viewportX = Integer.MIN_VALUE;
    private static int viewportY = Integer.MIN_VALUE;
    private static int viewportWidth = Integer.MIN_VALUE;
    private static int viewportHeight = Integer.MIN_VALUE;

    static {
        FRAMEBUFFERS.defaultReturnValue(Integer.MIN_VALUE);
    }

    private GlStateCache() {
    }

    public static boolean shouldBindFramebuffer(int target, int framebuffer) {
        if (ImmediatelyFast.config != null && !ImmediatelyFast.config.avoid_redundant_framebuffer_switching) {
            FRAMEBUFFERS.put(target, framebuffer);
            return true;
        }

        if (FRAMEBUFFERS.get(target) == framebuffer) {
            return false;
        }
        FRAMEBUFFERS.put(target, framebuffer);
        return true;
    }

    public static boolean shouldSetViewport(int x, int y, int width, int height) {
        if (ImmediatelyFast.config != null && !ImmediatelyFast.config.avoid_redundant_framebuffer_switching) {
            viewportX = x;
            viewportY = y;
            viewportWidth = width;
            viewportHeight = height;
            return true;
        }

        if (viewportX == x && viewportY == y && viewportWidth == width && viewportHeight == height) {
            return false;
        }
        viewportX = x;
        viewportY = y;
        viewportWidth = width;
        viewportHeight = height;
        return true;
    }

}

