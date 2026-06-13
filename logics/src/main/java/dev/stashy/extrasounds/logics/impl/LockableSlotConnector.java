package dev.stashy.extrasounds.logics.impl;

public interface LockableSlotConnector {
    default boolean extrasounds$isCreativeSlot() {
        return false;
    }
}
