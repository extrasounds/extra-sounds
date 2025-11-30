package dev.stashy.extrasounds.mc1_21_9;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "1.21.9";
    }

    @Override
    protected String laterVersion() {
        return "1.21.10";
    }
}
