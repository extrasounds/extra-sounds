package dev.stashy.extrasounds.logics.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import org.jetbrains.annotations.Nullable;

public abstract class RecordRepairableComponentProvider {
    @Nullable
    public static final RecordRepairableComponentProvider INSTANCE;

    protected static final String METHOD_KEY_ITEMS = RecordRepairableComponentProvider.class.getCanonicalName() + "#item";

    static {
        RecordRepairableComponentProvider instance = null;
        try {
            Class<RecordRepairableComponentProvider> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.RecordRepairableComponentImpl");
            instance = clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot get class RecordRepairableComponentImpl", ex);
        }
        INSTANCE = instance;
    }

    public abstract Object invokeRepairableComponent$items(Object instance) throws Exception;
}
