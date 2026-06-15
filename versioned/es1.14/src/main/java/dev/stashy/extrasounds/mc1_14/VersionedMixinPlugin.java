package dev.stashy.extrasounds.mc1_14;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "1.14";
    }

    @Override
    protected String laterVersion() {
        return "1.14.4";
    }
}
