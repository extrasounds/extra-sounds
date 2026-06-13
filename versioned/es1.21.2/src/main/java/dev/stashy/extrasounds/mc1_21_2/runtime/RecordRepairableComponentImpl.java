package dev.stashy.extrasounds.mc1_21_2.runtime;

import dev.stashy.extrasounds.logics.runtime.RecordRepairableComponentProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.type.RepairableComponent;

import java.lang.reflect.Method;

public class RecordRepairableComponentImpl extends RecordRepairableComponentProvider {
    private static final Class<RepairableComponent> REPAIRABLE_COMPONENT_CLASS = RepairableComponent.class;

    @Override
    public Object invokeRepairableComponent$items(Object instance) throws Exception {
        final Method $items;
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            $items = REPAIRABLE_COMPONENT_CLASS.getMethod("items");
        } else {
            $items = REPAIRABLE_COMPONENT_CLASS.getMethod("comp_2939");
        }
        return $items.invoke(instance);
    }
}
