package dev.stashy.extrasounds.mc1_17;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "1.17";
    }

    @Override
    protected String laterVersion() {
        return "1.17";
    }
}
