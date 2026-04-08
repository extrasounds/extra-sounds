package dev.stashy.extrasounds.mc26_1;

import me.lonefelidae16.groominglib.api.AbstractVersionedMixinPlugin;

public class VersionedMixinPlugin extends AbstractVersionedMixinPlugin {
    @Override
    protected String earlierVersion() {
        return "26.1";
    }

    @Override
    protected String laterVersion() {
        return "26.1.1";
    }
}
