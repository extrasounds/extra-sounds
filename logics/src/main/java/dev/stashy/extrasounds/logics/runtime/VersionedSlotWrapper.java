package dev.stashy.extrasounds.logics.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import me.lonefelidae16.groominglib.api.McVersionInterchange;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Objects;

public abstract class VersionedSlotWrapper {
    private static final Constructor<VersionedSlotWrapper> CTOR_WITH_COPY;
    private static final VersionedSlotWrapper EMPTY_SLOT = new VersionedSlotWrapper(null) {
        @Override
        public ItemStack getStack() {
            return ItemStack.EMPTY.copy();
        }

        @Override
        public boolean canInsert(ItemStack cursorStack) {
            return false;
        }

        @Override
        public boolean hasStack() {
            return false;
        }
    };

    protected final Object instance;

    static {
        Constructor<VersionedSlotWrapper> ctorCopy = null;
        try {
            Class<VersionedSlotWrapper> clazz = McVersionInterchange.getCompatibleClass(ExtraSounds.BASE_PACKAGE, "runtime.SlotImpl");
            ctorCopy = clazz.getConstructor(Object.class);
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Failed to find 'Slot' class.", ex);
        }
        CTOR_WITH_COPY = Objects.requireNonNull(ctorCopy);
    }

    public VersionedSlotWrapper(Object slot) {
        this.instance = slot;
    }

    public static VersionedSlotWrapper newInstance(@Nullable Object slot) {
        try {
            return Objects.requireNonNull(CTOR_WITH_COPY.newInstance(slot));
        } catch (Exception ex) {
            ExtraSounds.LOGGER.error("Cannot instantiate 'Slot' class.", ex);
        }
        return EMPTY_SLOT;
    }

    public abstract ItemStack getStack();

    public abstract boolean canInsert(ItemStack cursorStack);

    public abstract boolean hasStack();

    public Object getInstance() {
        return this.instance;
    }
}
