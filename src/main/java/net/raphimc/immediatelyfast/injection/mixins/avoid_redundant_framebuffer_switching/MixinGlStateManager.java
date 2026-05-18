package net.raphimc.immediatelyfast.injection.mixins.avoid_redundant_framebuffer_switching;

import com.mojang.blaze3d.platform.GlStateManager;
import net.raphimc.immediatelyfast.feature.core.GlStateCache;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GlStateManager.class)
public abstract class MixinGlStateManager {

    @Redirect(method = "bindFramebuffer", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL30;glBindFramebuffer(II)V", remap = false))
    private static void immediatelyfast$bindFramebufferGl30(int target, int framebuffer) {
        if (GlStateCache.shouldBindFramebuffer(target, framebuffer)) {
            GL30.glBindFramebuffer(target, framebuffer);
        }
    }

    @Redirect(method = "bindFramebuffer", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/ARBFramebufferObject;glBindFramebuffer(II)V", remap = false))
    private static void immediatelyfast$bindFramebufferArb(int target, int framebuffer) {
        if (GlStateCache.shouldBindFramebuffer(target, framebuffer)) {
            ARBFramebufferObject.glBindFramebuffer(target, framebuffer);
        }
    }

    @Redirect(method = "bindFramebuffer", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/EXTFramebufferObject;glBindFramebufferEXT(II)V", remap = false))
    private static void immediatelyfast$bindFramebufferExt(int target, int framebuffer) {
        if (GlStateCache.shouldBindFramebuffer(target, framebuffer)) {
            EXTFramebufferObject.glBindFramebufferEXT(target, framebuffer);
        }
    }

    @Redirect(method = "viewport", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glViewport(IIII)V", remap = false))
    private static void immediatelyfast$viewport(int x, int y, int width, int height) {
        if (GlStateCache.shouldSetViewport(x, y, width, height)) {
            GL11.glViewport(x, y, width, height);
        }
    }

}

