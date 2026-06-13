package dev.stashy.extrasounds.mc1_17.mixin.screens;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.AbstractCreativeInventoryHandler;
import dev.stashy.extrasounds.logics.impl.ScreenScrollHandler;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import dev.stashy.extrasounds.sounds.SoundType;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For Creative screen sound.
 */
@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Unique
    private static final int TYPE_INVENTORY = ItemGroup.INVENTORY.getIndex();
    @Unique
    private static final String METHOD_SIGN_SCROLL_ITEMS = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V";

    @Unique
    private final ScreenScrollHandler screenScrollHandler = new ScreenScrollHandler();
    @Unique
    private int currentTab = selectedTab;
    @Unique
    private final AbstractCreativeInventoryHandler inventoryHandler = new AbstractCreativeInventoryHandler() {
        @Override
        protected InventoryTabType getTabType() {
            if (selectedTab == TYPE_INVENTORY) {
                return InventoryTabType.INVENTORY;
            } else {
                return InventoryTabType.CREATIVE;
            }
        }

        @Override
        protected boolean isCreativeInventorySlot(Slot slot) {
            return CreativeInventoryScreenMixin.this.invokeIsCreativeInventorySlot(slot);
        }

        @Override
        protected Slot getDeleteItemSlot() {
            return CreativeInventoryScreenMixin.this.deleteItemSlot;
        }
    };

    @Shadow
    private static int selectedTab;
    @Shadow
    @Nullable
    private Slot deleteItemSlot;
    @Shadow
    private float scrollPosition;

    @Invoker("isCreativeInventorySlot")
    abstract boolean invokeIsCreativeInventorySlot(@Nullable Slot slot);

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "onMouseClick", at = @At("HEAD"))
    private void extrasounds$creativeInventoryClickEvent(@Nullable Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (this.client == null || this.client.player == null) {
            return;
        }

        this.inventoryHandler.onClick(this.client.player, slot, slotId, button, actionType, this.handler.getCursorStack());
    }

    @Inject(method = "setSelectedTab", at = @At("HEAD"))
    private void extrasounds$tabChange(ItemGroup group, CallbackInfo ci) {
        if (this.currentTab != group.getIndex()) {
            ExtraSounds.MANAGER.playSound2D(group.getIcon(), SoundType.GRAB);
            this.screenScrollHandler.resetScrollPos();
            this.currentTab = group.getIndex();
        }
    }

    @Unique
    private int extrasounds$getScreenScrollRow() {
        return Math.max((int) (this.scrollPosition * this.extrasounds$getScreenOverflowRows() + 0.5), 0);
    }

    @Unique
    private float extrasounds$getScreenOverflowRows() {
        return -Math.floorDiv(-this.getScreenHandler().itemList.size(), 9) - 5;
    }

    @Inject(method = "mouseDragged", at = @At(value = "INVOKE", target = METHOD_SIGN_SCROLL_ITEMS))
    private void extrasounds$creativeScreenScrollDrag(CallbackInfoReturnable<Boolean> cir) {
        this.screenScrollHandler.onScroll(this.extrasounds$getScreenScrollRow());
    }

    @Inject(method = "mouseScrolled", at = @At(value = "INVOKE", target = METHOD_SIGN_SCROLL_ITEMS))
    private void extrasounds$creativeScreenScroll(CallbackInfoReturnable<Boolean> cir) {
        this.screenScrollHandler.onScroll(this.extrasounds$getScreenScrollRow());
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void extrasounds$creativeScreenScrollOnResize(CallbackInfo ci) {
        this.screenScrollHandler.onScroll(this.extrasounds$getScreenScrollRow());
    }

    @Inject(method = "search", at = @At("HEAD"))
    private void extrasounds$resetCreativeScrollPos(CallbackInfo ci) {
        this.screenScrollHandler.resetScrollPos();
    }
}
