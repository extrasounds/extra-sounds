package dev.stashy.extrasounds.mc1_15_1.runtime;

import dev.stashy.extrasounds.logics.impl.state.SlotActionTypeImpl;
import net.minecraft.container.SlotActionType;

public class SlotActionImpl extends SlotActionTypeImpl.Wrapper {
    @Override
    public SlotActionTypeImpl wrap(Object actionType) {
        if (actionType instanceof SlotActionType) {
            switch ((SlotActionType) actionType) {
                case CLONE:
                    return SlotActionTypeImpl.CLONE;
                case PICKUP:
                    return SlotActionTypeImpl.PICKUP;
                case PICKUP_ALL:
                    return SlotActionTypeImpl.PICKUP_ALL;
                case QUICK_CRAFT:
                    return SlotActionTypeImpl.QUICK_CRAFT;
                case QUICK_MOVE:
                    return SlotActionTypeImpl.QUICK_MOVE;
                case SWAP:
                    return SlotActionTypeImpl.SWAP;
                case THROW:
                    return SlotActionTypeImpl.THROW;
            }
        }
        return SlotActionTypeImpl.UNDEFINED;
    }
}
