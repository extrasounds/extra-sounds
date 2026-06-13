package dev.stashy.extrasounds.mc1_20.compat;

import dev.stashy.extrasounds.mc1_20.VersionedMixinPlugin;
import net.fabricmc.loader.api.FabricLoader;

public class VersionedCompatMixinPlugin extends VersionedMixinPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!super.shouldApplyMixin(targetClassName, mixinClassName)) {
            return false;
        }

        if (mixinClassName.contains(VersionedCompatMixinPlugin.class.getPackage().getName() + ".mixin.rei")) {
            return FabricLoader.getInstance().isModLoaded("roughlyenoughitems");
        }

        return false;
    }
}
