package dev.stashy.extrasounds.logics.compat.mixin.midnightcontrols;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.VersionedHotbarSoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {
    @Unique
    private final VersionedHotbarSoundHandler soundHandler = ExtraSounds.MANAGER.getHotbarSoundHandler();

    @WrapMethod(method = "lambda$setup$2")
    private void extrasounds$touchHotbar_integrateMidnightControls(long window, int button, int action, int mods, Operation<Void> original) {
        final LocalPlayer player = Minecraft.getInstance().player;
        final int currentHotbarSlot = this.soundHandler.getPlayerInventorySlot(player);

        original.call(window, button, action, mods);

        if (currentHotbarSlot != VersionedHotbarSoundHandler.INVALID_HOTBAR_SLOT && currentHotbarSlot != this.soundHandler.getPlayerInventorySlot(player)) {
            this.soundHandler.onChange();
        }
    }
}
