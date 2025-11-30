package dev.stashy.extrasounds.mc1_21_6;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "1.21.6";
    }

    @Override
    protected String laterVersion() {
        return "1.21.8";
    }
}
