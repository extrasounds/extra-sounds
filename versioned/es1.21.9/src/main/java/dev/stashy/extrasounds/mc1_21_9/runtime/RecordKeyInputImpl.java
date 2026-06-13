package dev.stashy.extrasounds.mc1_21_9.runtime;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.RecordKeyInputProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.input.KeyInput;

import java.lang.reflect.Method;

public class RecordKeyInputImpl extends RecordKeyInputProvider {
    private static final Class<KeyInput> KEY_INPUT_CLASS = KeyInput.class;

    @Override
    public boolean invokeKeyInput$isCut(Object instance) throws Exception {
        Method $isCut = ExtraSounds.CACHED_METHOD_MAP.getOrDefault(METHOD_KEY_IS_CUT, null);
        if ($isCut == null) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                $isCut = KEY_INPUT_CLASS.getMethod("isCut");
            } else {
                $isCut = KEY_INPUT_CLASS.getMethod("method_74244");
            }
            ExtraSounds.CACHED_METHOD_MAP.put(METHOD_KEY_IS_CUT, $isCut);
        }
        return (boolean) $isCut.invoke(instance);
    }

    @Override
    public boolean invokeKeyInput$isPaste(Object instance) throws Exception {
        Method $isPaste = ExtraSounds.CACHED_METHOD_MAP.getOrDefault(METHOD_KEY_IS_PASTE, null);
        if ($isPaste == null) {
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                $isPaste = KEY_INPUT_CLASS.getMethod("isPaste");
            } else {
                $isPaste = KEY_INPUT_CLASS.getMethod("method_74243");
            }
            ExtraSounds.CACHED_METHOD_MAP.put(METHOD_KEY_IS_PASTE, $isPaste);
        }
        return (boolean) $isPaste.invoke(instance);
    }
}
