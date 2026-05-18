package net.raphimc.immediatelyfast.injection;

import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class ImmediatelyFastMixinPlugin implements IMixinConfigPlugin {

    private String mixinPackage;

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage + ".";
        ImmediatelyFast.loadConfig();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(this.mixinPackage)) {
            return false;
        }

        final String mixinName = mixinClassName.substring(this.mixinPackage.length());
        final String packageName = mixinName.substring(0, mixinName.lastIndexOf('.'));

        if (!ImmediatelyFast.config.font_atlas_resizing && packageName.startsWith("font_atlas_resizing")) {
            return false;
        }
        if (!ImmediatelyFast.config.map_atlas_generation && packageName.startsWith("map_atlas_generation")) {
            return false;
        }
        if (!ImmediatelyFast.config.fast_text_lookup && packageName.startsWith("fast_text_lookup")) {
            return false;
        }
        if (!ImmediatelyFast.config.avoid_redundant_framebuffer_switching && packageName.startsWith("avoid_redundant_framebuffer_switching")) {
            return false;
        }
        if (!ImmediatelyFast.config.experimental_disable_error_checking && packageName.startsWith("disable_error_checking")) {
            return false;
        }
        if (!ImmediatelyFast.config.skip_text_translucency_sorting && packageName.startsWith("skip_text_translucency_sorting")) {
            return false;
        }
        if (!ImmediatelyFast.config.isHudBatchingEnabled() && packageName.startsWith("hud_batching")) {
            return false;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

}

