package dev.stashy.extrasounds.logics.impl.state;

import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;

import java.util.Objects;

public enum SlotActionTypeImpl {
    PICKUP,
    QUICK_MOVE,
    SWAP,
    CLONE,
    THROW,
    QUICK_CRAFT,
    PICKUP_ALL,
    UNDEFINED;

    public static abstract class Wrapper {
        public static final Wrapper INSTANCE;

        static {
            Wrapper wrapper = null;
            try {
                Class<Wrapper> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.SlotActionImpl");
                wrapper = Objects.requireNonNull(clazz.getConstructor().newInstance());
            } catch (Exception ex) {
                ExtraSounds.LOGGER.error("Cannot find 'SlotAction' class.", ex);
            }
            INSTANCE = wrapper;
        }

        public abstract SlotActionTypeImpl wrap(Object actionType);
    }
}
