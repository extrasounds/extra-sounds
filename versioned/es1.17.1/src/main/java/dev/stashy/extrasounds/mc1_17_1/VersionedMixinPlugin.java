package dev.stashy.extrasounds.mc1_17_1;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "1.17.1";
    }

    @Override
    protected String laterVersion() {
        return "1.17.1";
    }
}
