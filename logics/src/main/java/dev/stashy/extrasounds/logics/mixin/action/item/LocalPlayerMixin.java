package dev.stashy.extrasounds.logics.mixin.action.item;

import com.mojang.authlib.GameProfile;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Bow pull sound.
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"))
    private void extrasounds$bowPullSound(InteractionHand hand, CallbackInfo ci) {
        if (!this.getItemInHand(hand).is(Items.BOW)) {
            return;
        }

        ExtraSounds.MANAGER.playSound2D(Sounds.Actions.BOW_PULL, SoundType.ITEM_INTR);
    }

    @Inject(method = "stopUsingItem", at = @At(value = "HEAD"))
    private void extrasounds$cancelPullSound(CallbackInfo ci) {
        if (!this.useItem.is(Items.BOW)) {
            return;
        }

        ExtraSounds.MANAGER.stopSound(Sounds.Actions.BOW_PULL, SoundType.ITEM_INTR);
    }
}
