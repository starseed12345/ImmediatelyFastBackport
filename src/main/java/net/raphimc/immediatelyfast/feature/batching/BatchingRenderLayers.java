package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormats;

public final class BatchingRenderLayers {

    public static final RenderLayer GUI_FILL = new ImmediatelyFastRenderLayer(
            "gui_fill",
            7,
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.disableTexture();
                RenderSystem.defaultBlendFunc();
            },
            () -> {
                RenderSystem.enableTexture();
                RenderSystem.disableBlend();
            }
    );

    private BatchingRenderLayers() {
    }

    private static final class ImmediatelyFastRenderLayer extends RenderLayer {

        private ImmediatelyFastRenderLayer(String name, int drawMode, Runnable startAction, Runnable endAction) {
            super("immediatelyfast_" + name, VertexFormats.POSITION_COLOR, drawMode, 2048, false, true, startAction, endAction);
        }

    }

}

