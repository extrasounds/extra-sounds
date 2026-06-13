package dev.stashy.extrasounds.logics.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.text.Text;

import java.lang.reflect.Constructor;
import java.util.Objects;

public abstract class VersionedText {
    public static final VersionedText INSTANCE;

    static {
        VersionedText instance = null;
        try {
            Class<VersionedText> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.TextImpl");
            Constructor<VersionedText> constructor = clazz.getConstructor();
            instance = constructor.newInstance();
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot instantiate 'TextImpl'", ex);
        }
        INSTANCE = Objects.requireNonNull(instance);
    }

    public abstract Text empty();

    public abstract Text getDoneText();

    public abstract Text getCancelText();

    public abstract Text translatable(String key, Object... args);
}
