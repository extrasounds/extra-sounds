package dev.stashy.extrasounds.logics.impl;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.state.ActionResultState;
import dev.stashy.extrasounds.logics.mixin.access.FlowerPotBlockAccessor;
import dev.stashy.extrasounds.sounds.Sounds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class AbstractInteractionHandler {
    protected BlockState blockState;
    protected BlockEntity blockEntity;
    protected Block block;
    protected ItemStack currentHandStack;
    protected ItemStack mainHandStack;
    protected ItemStack offHandStack;

    protected abstract EquipmentSlot getPreferredSlot(ArmorStand armorStandEntity, ItemStack itemStack);

    protected abstract EquipmentSlot getSlotFromPosition(ArmorStand armorStandEntity, Vec3 position);

    protected abstract BlockPos getBlockPos(Vec3 vec3d);

    protected abstract boolean isFlowerPotBlocks();

    protected abstract boolean isRedstoneOreBlocks();

    protected abstract boolean isCampfireBlocks();

    protected abstract Optional<?> getCampfireRecipe(CampfireBlockEntity campfireBlockEntity, ItemStack currentHandStack);

    protected abstract boolean shouldSoundArmorStandEquipped(ItemStack currentStack, ItemStack equipped);

    protected abstract boolean shouldSoundArmorStandPreferred(ItemStack currentStack, ItemStack preferred);

    private boolean canInteractBlock(Player player) {
        return !player.isCrouching() || (player.isCrouching() && this.mainHandStack.isEmpty() && this.offHandStack.isEmpty());
    }

    public final void setInteractionState(BlockState blockState, BlockEntity blockEntity, ItemStack stackInHand, ItemStack mainHandStack, ItemStack offHandStack) {
        this.blockState = blockState;
        this.blockEntity = blockEntity;
        this.block = blockState.getBlock();
        this.currentHandStack = stackInHand.copy();
        this.mainHandStack = mainHandStack.copy();
        this.offHandStack = offHandStack.copy();
    }

    public final void onUse(LocalPlayer player, BlockPos blockPos, ActionResultState actionResult) {
        final boolean bCanInteract = this.canInteractBlock(player);

        if (this.blockState.is(Blocks.REPEATER) &&
                this.blockState.hasProperty(RepeaterBlock.DELAY) &&
                bCanInteract
        ) {
            // Repeater
            final var sound = this.blockState.getValue(RepeaterBlock.DELAY) == 4 ? Sounds.Actions.REPEATER_RESET : Sounds.Actions.REPEATER_ADD;
            ExtraSounds.MANAGER.blockInteract(sound, blockPos);
        } else if (this.blockState.is(Blocks.DAYLIGHT_DETECTOR) &&
                this.blockState.hasProperty(DaylightDetectorBlock.INVERTED) &&
                bCanInteract
        ) {
            // Daylight Detector
            final var sound = this.blockState.getValue(DaylightDetectorBlock.INVERTED) ? Sounds.Actions.REDSTONE_COMPONENT_ON : Sounds.Actions.REDSTONE_COMPONENT_OFF;
            ExtraSounds.MANAGER.blockInteract(sound, blockPos);
        } else if (this.blockState.is(Blocks.REDSTONE_WIRE) && bCanInteract &&
                actionResult == ActionResultState.SUCCESS
        ) {
            // Redstone Wire
            ExtraSounds.MANAGER.blockInteract(Sounds.Actions.REDSTONE_WIRE_CHANGE, blockPos);
        } else if (this.isRedstoneOreBlocks() &&
                this.blockState.hasProperty(RedStoneOreBlock.LIT) &&
                bCanInteract && !(this.mainHandStack.getItem() instanceof BlockItem)
        ) {
            // Redstone Ores
            ExtraSounds.MANAGER.blockInteract(this.block.asItem(), blockPos);
        } else if (this.isCampfireBlocks() && (this.blockEntity instanceof CampfireBlockEntity campfireBlockEntity)) {
            // Put item on Campfire
            if (campfireBlockEntity.getItems().stream().noneMatch(ItemStack::isEmpty)) {
                return;
            }

            var recipe = this.getCampfireRecipe(campfireBlockEntity, this.currentHandStack);
            if (recipe.isPresent() && actionResult == ActionResultState.CONSUME) {
                ExtraSounds.MANAGER.blockInteract(this.currentHandStack.getItem(), blockPos);
            }
        } else if (this.isFlowerPotBlocks() &&
                (this.block instanceof FlowerPotBlock potBlock) &&
                actionResult == ActionResultState.SUCCESS
        ) {
            if (!((FlowerPotBlockAccessor) potBlock).invokeIsEmpty()) {
                // Take from pot
                ExtraSounds.MANAGER.blockInteract(potBlock.getPotted().asItem(), blockPos);
            } else {
                // Place into pot
                ExtraSounds.MANAGER.blockInteract(this.currentHandStack.getItem(), blockPos);
            }
        }
    }

    public final void onInteractEntityAt(ItemStack stackInHand, Entity entity, EntityHitResult hitResult, Vec3 target) {
        final ItemStack currentStack = stackInHand.copy();
        if (entity instanceof ArmorStand armorStandEntity) {
            final EquipmentSlot slotFromPosition = this.getSlotFromPosition(armorStandEntity, target);
            final EquipmentSlot slotPreferred = this.getPreferredSlot(armorStandEntity, currentStack);
            if (!armorStandEntity.hasItemInSlot(slotFromPosition) && !armorStandEntity.hasItemInSlot(slotPreferred)) {
                return;
            }

            final ItemStack equipped = armorStandEntity.getItemBySlot(slotFromPosition).copy();
            final ItemStack preferred = armorStandEntity.getItemBySlot(slotPreferred).copy();
            if (this.shouldSoundArmorStandEquipped(currentStack, equipped)) {
                ExtraSounds.MANAGER.blockInteract(equipped.getItem(), this.getBlockPos(hitResult.getLocation()));
            } else if (this.shouldSoundArmorStandPreferred(currentStack, preferred)) {
                ExtraSounds.MANAGER.blockInteract(preferred.getItem(), this.getBlockPos(hitResult.getLocation()));
            }
        }
    }
}
