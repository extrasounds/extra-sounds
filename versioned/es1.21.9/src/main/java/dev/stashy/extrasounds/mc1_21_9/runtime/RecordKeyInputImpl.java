package dev.stashy.extrasounds.mc1_21_9.runtime;

import dev.stashy.extrasounds.logics.runtime.RecordKeyInputProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.input.KeyInput;

import java.lang.reflect.Method;

public class RecordKeyInputImpl extends RecordKeyInputProvider {
    private static final Class<KeyInput> KEY_INPUT_CLASS = KeyInput.class;

    @Override
    public boolean invokeKeyInput$isCut(Object instance) throws Exception {
        final Method $isCut;
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            $isCut = KEY_INPUT_CLASS.getMethod("isCut");
        } else {
            $isCut = KEY_INPUT_CLASS.getMethod("method_74244");
        }
        return (boolean) $isCut.invoke(instance);
    }

    @Override
    public boolean invokeKeyInput$isPaste(Object instance) throws Exception {
        final Method $isPaste;
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            $isPaste = KEY_INPUT_CLASS.getMethod("isPaste");
        } else {
            $isPaste = KEY_INPUT_CLASS.getMethod("method_74243");
        }
        return (boolean) $isPaste.invoke(instance);
    }
}
