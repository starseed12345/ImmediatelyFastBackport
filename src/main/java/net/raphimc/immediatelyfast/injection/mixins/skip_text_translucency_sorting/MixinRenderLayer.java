package net.raphimc.immediatelyfast.injection.mixins.skip_text_translucency_sorting;

import net.minecraft.client.render.RenderLayer;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = RenderLayer.class, priority = 500)
public abstract class MixinRenderLayer {

    @ModifyArg(
            method = {
                    "getText",
                    "getTextSeeThrough"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;of(Ljava/lang/String;Lnet/minecraft/client/render/VertexFormat;IIZZLnet/minecraft/client/render/RenderLayer$MultiPhaseParameters;)Lnet/minecraft/client/render/RenderLayer$MultiPhase;"),
            index = 5
    )
    private static boolean immediatelyfast$skipTextTranslucencySorting(boolean value) {
        ImmediatelyFast.loadConfig();
        return ImmediatelyFast.config.skip_text_translucency_sorting ? false : value;
    }

}

