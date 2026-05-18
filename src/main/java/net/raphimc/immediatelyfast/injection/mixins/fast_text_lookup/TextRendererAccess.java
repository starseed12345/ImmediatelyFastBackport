package net.raphimc.immediatelyfast.injection.mixins.fast_text_lookup;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextRenderer.class)
public interface TextRendererAccess {

    @Invoker("getFontStorage")
    FontStorage immediatelyfast$getFontStorage(Identifier id);

}

