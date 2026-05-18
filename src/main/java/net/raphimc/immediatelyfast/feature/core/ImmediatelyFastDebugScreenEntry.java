package net.raphimc.immediatelyfast.feature.core;

import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.util.MathUtil;

import java.util.List;

public final class ImmediatelyFastDebugScreenEntry {

    private ImmediatelyFastDebugScreenEntry() {
    }

    public static void appendLines(List<String> lines) {
        lines.add("ImmediatelyFast " + ImmediatelyFast.VERSION);
        lines.add("Buffer Pool: " + BufferBuilderPool.getAllocatedSize() + " buffers");
        lines.add("Approx Buffer Pool Memory: " + MathUtil.formatBytes((long) BufferBuilderPool.getAllocatedSize() * 256L));
    }

}

