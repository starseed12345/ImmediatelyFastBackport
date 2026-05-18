package net.raphimc.immediatelyfast.injection.mixins.hud_batching;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Inject(method = "render", at = @At("HEAD"))
    private void immediatelyfast$beginHudBatching(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        BatchingBuffers.beginHudBatching();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void immediatelyfast$endHudBatching(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        BatchingBuffers.endHudBatching();
    }

}

