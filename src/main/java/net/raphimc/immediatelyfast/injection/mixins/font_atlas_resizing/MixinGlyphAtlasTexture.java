package net.raphimc.immediatelyfast.injection.mixins.font_atlas_resizing;

import net.minecraft.client.font.GlyphAtlasTexture;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GlyphAtlasTexture.class)
public abstract class MixinGlyphAtlasTexture {

    @ModifyConstant(method = "*", constant = @Constant(intValue = 256))
    private int immediatelyfast$modifyGlyphAtlasTextureSize(int original) {
        ImmediatelyFast.loadConfig();
        return ImmediatelyFast.config.font_atlas_resizing ? ImmediatelyFast.config.font_atlas_size : original;
    }

    @ModifyConstant(method = "*", constant = @Constant(floatValue = 256F))
    private float immediatelyfast$modifyGlyphAtlasTextureSize(float original) {
        ImmediatelyFast.loadConfig();
        return ImmediatelyFast.config.font_atlas_resizing ? (float) ImmediatelyFast.config.font_atlas_size : original;
    }

}

