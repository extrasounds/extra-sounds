package dev.stashy.extrasounds.logics.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Property;

import java.lang.reflect.Constructor;
import java.util.Objects;

public abstract class VersionedBlockStateWrapper {
    private static final Constructor<VersionedBlockStateWrapper> CTOR_WITH_COPY;

    static {
        Constructor<VersionedBlockStateWrapper> ctorCopy = null;
        try {
            Class<VersionedBlockStateWrapper> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.BlockStateImpl");
            ctorCopy = clazz.getConstructor(Object.class);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Failed to find 'BlockState' class.", ex);
        }
        CTOR_WITH_COPY = Objects.requireNonNull(ctorCopy);
    }

    public static VersionedBlockStateWrapper newInstance(Object blockState) {
        try {
            return CTOR_WITH_COPY.newInstance(blockState);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot wrap 'BlockState' using its instance.", ex);
        }
        return null;
    }

    public abstract Block getBlockImpl();

    public abstract boolean containsProperty(Property<?> property);

    public abstract<T extends Comparable<T>> T getProperty(Property<T> property);

    public abstract BlockSoundGroup getSoundGroupImpl();
}
