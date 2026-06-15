package dev.stashy.extrasounds.mc1_14.mixin.resource;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.entry.SoundPackLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.LinkedList;
import java.util.List;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @ModifyVariable(method = "init", at = @At("STORE"))
    private List<ResourcePack> extrasounds$insertResPack(List<ResourcePack> original) {
        ExtraSounds.LOGGER.info("registering Runtime ResPack");
        List<ResourcePack> modifiable = new LinkedList<>(original);
        modifiable.add(0, (ResourcePack) SoundPackLoader.EXTRA_SOUNDS_RESOURCE);
        return modifiable;
    }
}
