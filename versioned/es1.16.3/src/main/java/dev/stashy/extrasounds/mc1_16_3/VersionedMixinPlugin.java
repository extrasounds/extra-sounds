package dev.stashy.extrasounds.mc1_16_3;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "1.16.3";
    }

    @Override
    protected String laterVersion() {
        return "1.16.4";
    }
}
