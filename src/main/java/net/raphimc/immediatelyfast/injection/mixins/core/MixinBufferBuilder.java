package net.raphimc.immediatelyfast.injection.mixins.core;

import net.minecraft.client.render.BufferBuilder;
import net.raphimc.immediatelyfast.injection.interfaces.IBufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements IBufferBuilder {

    @Shadow
    private ByteBuffer buffer;

    @Override
    public boolean immediatelyfast$isReleased() {
        return this.buffer == null;
    }

    @Override
    public void immediatelyfast$release() {
        this.buffer = null;
    }

}

