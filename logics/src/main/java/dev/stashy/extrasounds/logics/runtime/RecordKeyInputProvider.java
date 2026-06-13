package dev.stashy.extrasounds.logics.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import org.jetbrains.annotations.Nullable;

public abstract class RecordKeyInputProvider {
    @Nullable
    public static final RecordKeyInputProvider INSTANCE;

    protected static final String METHOD_KEY_IS_CUT = RecordKeyInputProvider.class.getCanonicalName() + "#isCut";
    protected static final String METHOD_KEY_IS_PASTE = RecordKeyInputProvider.class.getCanonicalName() + "#isPaste";

    static {
        RecordKeyInputProvider instance = null;
        try {
            Class<RecordKeyInputProvider> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.RecordKeyInputImpl");
            instance = clazz.getConstructor().newInstance();
        } catch (Exception ignored) {
        }
        INSTANCE = instance;
    }

    public abstract boolean invokeKeyInput$isCut(Object instance) throws Exception;

    public abstract boolean invokeKeyInput$isPaste(Object instance) throws Exception;
}
