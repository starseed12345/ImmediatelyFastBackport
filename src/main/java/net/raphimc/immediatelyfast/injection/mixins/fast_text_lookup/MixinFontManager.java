package net.raphimc.immediatelyfast.injection.mixins.fast_text_lookup;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.FontStorage;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Function;

@Mixin(value = FontManager.class, priority = 500)
public abstract class MixinFontManager {

    @Shadow
    @Final
    private Map<Identifier, FontStorage> fontStorages;

    @Shadow
    private Map<Identifier, Identifier> idOverrides;

    @Shadow
    protected abstract FontStorage method_27542(Identifier id);

    @Unique
    private final Map<Identifier, FontStorage> immediatelyfast$overriddenFontStorages = new Object2ObjectOpenHashMap<>();

    @Unique
    private FontStorage immediatelyfast$defaultFontStorage;

    @Unique
    private FontStorage immediatelyfast$unicodeFontStorage;

    @Inject(method = "setIdOverrides", at = @At("RETURN"))
    private void immediatelyfast$rebuildOverriddenFontStoragesOnChange(CallbackInfo ci) {
        this.immediatelyfast$rebuildOverriddenFontStorages();
    }

    @ModifyArg(method = "createTextRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;<init>(Ljava/util/function/Function;)V"))
    private Function<Identifier, FontStorage> immediatelyfast$overrideFontStorage(Function<Identifier, FontStorage> original) {
        this.immediatelyfast$rebuildOverriddenFontStorages();
        return id -> {
            if (MinecraftClient.DEFAULT_FONT_ID.equals(id) && this.immediatelyfast$defaultFontStorage != null) {
                return this.immediatelyfast$defaultFontStorage;
            }
            if (MinecraftClient.UNICODE_FONT_ID.equals(id) && this.immediatelyfast$unicodeFontStorage != null) {
                return this.immediatelyfast$unicodeFontStorage;
            }

            final FontStorage storage = this.immediatelyfast$overriddenFontStorages.get(id);
            if (storage != null) {
                return storage;
            }

            return original.apply(id);
        };
    }

    @Unique
    private void immediatelyfast$rebuildOverriddenFontStorages() {
        this.immediatelyfast$overriddenFontStorages.clear();
        this.immediatelyfast$overriddenFontStorages.putAll(this.fontStorages);
        for (Identifier key : this.idOverrides.keySet()) {
            this.immediatelyfast$overriddenFontStorages.put(key, this.method_27542(key));
        }

        this.immediatelyfast$defaultFontStorage = this.immediatelyfast$overriddenFontStorages.get(MinecraftClient.DEFAULT_FONT_ID);
        this.immediatelyfast$unicodeFontStorage = this.immediatelyfast$overriddenFontStorages.get(MinecraftClient.UNICODE_FONT_ID);
    }

}

