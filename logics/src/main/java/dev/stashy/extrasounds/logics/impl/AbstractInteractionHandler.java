package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.ActionResultState;
import dev.stashy.extrasounds.logics.runtime.VersionedBlockStateWrapper;
import dev.stashy.extrasounds.logics.runtime.VersionedSoundEventWrapper;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public abstract class AbstractInteractionHandler {
    protected VersionedBlockStateWrapper blockState;
    protected BlockEntity blockEntity;
    protected Block block;
    protected ItemStack currentHandStack;
    protected ItemStack mainHandStack;
    protected ItemStack offHandStack;

    protected abstract EquipmentSlot getPreferredSlot(ArmorStandEntity armorStandEntity, ItemStack itemStack);

    protected abstract EquipmentSlot getSlotFromPosition(ArmorStandEntity armorStandEntity, Vec3d position);

    protected abstract BlockPos getBlockPos(Vec3d vec3d);

    protected abstract boolean isFlowerPotBlocks();

    protected boolean shouldSoundFlowerPot(FlowerPotBlock block) {
        return true;
    }

    protected abstract boolean isRedstoneOreBlocks();

    protected abstract boolean isCampfireBlocks();

    protected abstract Optional<?> getCampfireRecipe(CampfireBlockEntity campfireBlockEntity, ItemStack currentHandStack);

    protected abstract boolean shouldSoundArmorStandEquipped(ItemStack currentStack, ItemStack equipped);

    protected abstract boolean shouldSoundArmorStandPreferred(ItemStack currentStack, ItemStack preferred);

    private boolean canInteractBlock(PlayerEntity player) {
        return !player.isSneaking() || (player.isSneaking() && this.mainHandStack.isEmpty() && this.offHandStack.isEmpty());
    }

    public final void setInteractionState(VersionedBlockStateWrapper blockState, BlockEntity blockEntity, ItemStack stackInHand, ItemStack mainHandStack, ItemStack offHandStack) {
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        this.block = blockState.getBlockImpl();
        this.currentHandStack = stackInHand.copy();
        this.mainHandStack = mainHandStack.copy();
        this.offHandStack = offHandStack.copy();
    }

    public final void onUse(ClientPlayerEntity player, BlockPos blockPos, ActionResultState actionResult) {
        final boolean bCanInteract = this.canInteractBlock(player);

        if (this.block == Blocks.REPEATER &&
                this.blockState.containsProperty(RepeaterBlock.DELAY) &&
                bCanInteract
        ) {
            // Repeater
            final VersionedSoundEventWrapper sound = this.blockState.getProperty(RepeaterBlock.DELAY) == 4 ? Sounds.Actions.REPEATER_RESET : Sounds.Actions.REPEATER_ADD;
            ExtraSounds.MANAGER.blockInteract(sound, blockPos);
        } else if (this.block == Blocks.DAYLIGHT_DETECTOR &&
                this.blockState.containsProperty(DaylightDetectorBlock.INVERTED) &&
                bCanInteract
        ) {
            // Daylight Detector
            final VersionedSoundEventWrapper sound = this.blockState.getProperty(DaylightDetectorBlock.INVERTED) ? Sounds.Actions.REDSTONE_COMPONENT_ON : Sounds.Actions.REDSTONE_COMPONENT_OFF;
            ExtraSounds.MANAGER.blockInteract(sound, blockPos);
        } else if (this.block == Blocks.REDSTONE_WIRE && bCanInteract &&
                actionResult == ActionResultState.SUCCESS
        ) {
            // Redstone Wire
            ExtraSounds.MANAGER.blockInteract(Sounds.Actions.REDSTONE_WIRE_CHANGE, blockPos);
        } else if (this.isRedstoneOreBlocks() &&
                this.blockState.containsProperty(RedstoneOreBlock.LIT) &&
                bCanInteract && !(this.mainHandStack.getItem() instanceof BlockItem)
        ) {
            // Redstone Ores
            ExtraSounds.MANAGER.blockInteract(this.block.asItem().getStackForRender(), blockPos);
        } else if (this.isCampfireBlocks() && (this.blockEntity instanceof CampfireBlockEntity)) {
            // Put item on Campfire
            final CampfireBlockEntity campfireBlockEntity = (CampfireBlockEntity) this.blockEntity;
            if (campfireBlockEntity.getItemsBeingCooked().stream().noneMatch(ItemStack::isEmpty)) {
                return;
            }

            Optional<?> recipe = this.getCampfireRecipe(campfireBlockEntity, this.currentHandStack);
            if (recipe.isPresent() && ActionResultState.isSuccess(actionResult)) {
                ExtraSounds.MANAGER.blockInteract(this.currentHandStack, blockPos);
            }
        } else if (this.isFlowerPotBlocks() &&
                (this.block instanceof FlowerPotBlock) &&
                actionResult == ActionResultState.SUCCESS
        ) {
            final FlowerPotBlock potBlock = (FlowerPotBlock) this.block;
            if (!this.shouldSoundFlowerPot(potBlock) || !(potBlock instanceof FlowerPotBlockConnector)) {
                return;
            }

            final FlowerPotBlockConnector connector = (FlowerPotBlockConnector) potBlock;
            if (connector.isContentEmpty()) {
                // Place into pot
                ExtraSounds.MANAGER.blockInteract(this.currentHandStack.getItem().getStackForRender(), blockPos);
            } else {
                // Take from pot
                ExtraSounds.MANAGER.blockInteract(potBlock.getContent().asItem().getStackForRender(), blockPos);
            }
        }
    }

    public final void onInteractEntityAt(ItemStack stackInHand, Entity entity, EntityHitResult hitResult, Vec3d target) {
        final ItemStack currentStack = stackInHand.copy();
        if (entity instanceof ArmorStandEntity) {
            final ArmorStandEntity armorStandEntity = (ArmorStandEntity) entity;
            final EquipmentSlot slotFromPosition = this.getSlotFromPosition(armorStandEntity, target);
            final EquipmentSlot slotPreferred = this.getPreferredSlot(armorStandEntity, currentStack);
            if (!armorStandEntity.hasStackEquipped(slotFromPosition) && !armorStandEntity.hasStackEquipped(slotPreferred)) {
                return;
            }

            final ItemStack equipped = armorStandEntity.getEquippedStack(slotFromPosition).copy();
            final ItemStack preferred = armorStandEntity.getEquippedStack(slotPreferred).copy();
            if (this.shouldSoundArmorStandEquipped(currentStack, equipped)) {
                ExtraSounds.MANAGER.blockInteract(equipped.getItem().getStackForRender(), this.getBlockPos(hitResult.getPos()));
            } else if (this.shouldSoundArmorStandPreferred(currentStack, preferred)) {
                ExtraSounds.MANAGER.blockInteract(preferred.getItem().getStackForRender(), this.getBlockPos(hitResult.getPos()));
            }
        }
    }
}
