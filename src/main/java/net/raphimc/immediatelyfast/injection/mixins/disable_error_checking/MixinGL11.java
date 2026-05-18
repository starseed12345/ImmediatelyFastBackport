package net.raphimc.immediatelyfast.injection.mixins.disable_error_checking;

import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.system.NativeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GL11.class, remap = false)
public abstract class MixinGL11 {

    
    @NativeType("GLenum")
    @Overwrite
    public static int glGetError() {
        ImmediatelyFast.loadConfig();
        if (ImmediatelyFast.config.experimental_disable_error_checking) {
            return GL11C.GL_NO_ERROR;
        }
        return GL11C.glGetError();
    }

}

