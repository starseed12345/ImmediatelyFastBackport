package net.raphimc.immediatelyfast.injection.mixins.core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {

    @Inject(method = "initRenderer", at = @At("RETURN"))
    private static void immediatelyfast$initRenderer(int debugVerbosity, boolean debugSync, CallbackInfo ci) {
        ImmediatelyFast.onRenderSystemInit();
    }

    @Inject(method = "flipFrame", at = @At("HEAD"))
    private static void immediatelyfast$endFrame(long window, CallbackInfo ci) {
        ImmediatelyFast.onEndFrame();
    }

}

