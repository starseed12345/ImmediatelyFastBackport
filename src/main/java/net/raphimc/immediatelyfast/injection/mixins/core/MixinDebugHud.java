package net.raphimc.immediatelyfast.injection.mixins.core;

import net.minecraft.client.gui.hud.DebugHud;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.core.ImmediatelyFastDebugScreenEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud {

    @Inject(method = "getLeftText", at = @At("RETURN"))
    private void immediatelyfast$addBufferPoolStats(CallbackInfoReturnable<List<String>> cir) {
        ImmediatelyFast.loadConfig();
        if (ImmediatelyFast.config.dont_add_info_into_debug_hud) {
            return;
        }
        ImmediatelyFastDebugScreenEntry.appendLines(cir.getReturnValue());
    }

}

