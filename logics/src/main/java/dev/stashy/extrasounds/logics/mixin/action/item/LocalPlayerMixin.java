package dev.stashy.extrasounds.logics.mixin.action.item;

import com.mojang.authlib.GameProfile;
import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import dev.stashy.extrasounds.sounds.SoundType;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    @Nullable
    private VersionedSoundEventWrapper extrasounds$playingSound = null;

    @Inject(method = "startUsingItem", at = @At("HEAD"))
    private void extrasounds$bowPullSound(InteractionHand hand, CallbackInfo ci) {
        final Item interactingItem = this.getItemInHand(hand).getItem();

        if (interactingItem == Items.BOW) {
            this.extrasounds$playingSound = Sounds.Actions.BOW_PULL;
        } else if (interactingItem == Items.SHIELD) {
            this.extrasounds$playingSound = Sounds.Actions.SHIELD_BLOCK_START;
        }

        if (this.extrasounds$playingSound != null) {
            ExtraSounds.MANAGER.playSoundUI(this.extrasounds$playingSound, SoundType.ITEM_INTR);
        }
    }

    @Inject(method = "stopUsingItem", at = @At(value = "HEAD"))
    private void extrasounds$cancelPullSound(CallbackInfo ci) {
        if (this.extrasounds$playingSound == null) {
            return;
        }

        ExtraSounds.MANAGER.stopSound(this.extrasounds$playingSound, SoundType.ITEM_INTR);

        if (this.extrasounds$playingSound == Sounds.Actions.SHIELD_BLOCK_START) {
            ExtraSounds.MANAGER.playSoundUI(Sounds.Actions.SHIELD_BLOCK_STOP, SoundType.ITEM_INTR);
        }
        this.extrasounds$playingSound = null;
    }
}
