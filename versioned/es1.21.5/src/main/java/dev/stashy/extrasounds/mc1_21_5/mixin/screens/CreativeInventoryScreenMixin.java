package dev.stashy.extrasounds.mc1_21_5.mixin.screens;

import dev.stashy.extrasounds.logics.ExtraSounds;
import dev.stashy.extrasounds.logics.impl.AbstractCreativeInventoryHandler;
import dev.stashy.extrasounds.logics.impl.ScreenScrollHandler;
import dev.stashy.extrasounds.logics.impl.state.InventoryTabType;
import dev.stashy.extrasounds.logics.impl.state.SlotActionTypeImpl;
import dev.stashy.extrasounds.logics.runtime.VersionedSlotWrapper;
import dev.stashy.extrasounds.sounds.SoundType;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
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
public abstract class CreativeInventoryScreenMixin extends HandledScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Unique
    private static final ItemGroup.Type TYPE_INVENTORY = ItemGroup.Type.INVENTORY;
    @Unique
    private static final String METHOD_SIGN_SCROLL_ITEMS = "Lnet/minecraft/client/gui/screen/ingame/CreativeInventoryScreen$CreativeScreenHandler;scrollItems(F)V";

    @Unique
    private final ScreenScrollHandler screenScrollHandler = new ScreenScrollHandler();
    @Unique
    private ItemGroup currentTab = selectedTab;
    @Unique
    private final AbstractCreativeInventoryHandler inventoryHandler = new AbstractCreativeInventoryHandler() {
        @Override
        protected InventoryTabType getTabType() {
            if (selectedTab.getType() == TYPE_INVENTORY) {
                return InventoryTabType.INVENTORY;
            } else {
                return InventoryTabType.CREATIVE;
            }
        }

        @Override
        protected boolean isCreativeInventorySlot(VersionedSlotWrapper slot) {
            return CreativeInventoryScreenMixin.this.invokeIsCreativeInventorySlot((Slot) slot.getInstance());
        }

        @Override
        protected VersionedSlotWrapper getDeleteItemSlot() {
            return VersionedSlotWrapper.newInstance(CreativeInventoryScreenMixin.this.deleteItemSlot);
        }
    };

    @Shadow
    private static ItemGroup selectedTab;
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

        SlotActionTypeImpl wrapped = SlotActionTypeImpl.Wrapper.INSTANCE.wrap(actionType);
        this.inventoryHandler.onClick(this.client.player, VersionedSlotWrapper.newInstance(slot), slotId, button, wrapped, this.handler.getCursorStack());
    }

    @Inject(method = "setSelectedTab", at = @At("HEAD"))
    private void extrasounds$tabChange(ItemGroup group, CallbackInfo ci) {
        if (this.currentTab != group) {
            ExtraSounds.MANAGER.playSound2D(group.getIcon(), SoundType.GRAB);
            this.screenScrollHandler.resetScrollPos();
            this.currentTab = group;
        }
    }

    @Inject(method = "mouseDragged", at = @At(value = "INVOKE", target = METHOD_SIGN_SCROLL_ITEMS))
    private void extrasounds$creativeScreenScrollDrag(CallbackInfoReturnable<Boolean> cir) {
        this.screenScrollHandler.onScroll(this.getScreenHandler().getRow(this.scrollPosition));
    }

    @Inject(method = "mouseScrolled", at = @At(value = "INVOKE", target = METHOD_SIGN_SCROLL_ITEMS))
    private void extrasounds$creativeScreenScroll(CallbackInfoReturnable<Boolean> cir) {
        this.screenScrollHandler.onScroll(this.getScreenHandler().getRow(this.scrollPosition));
    }

    @Inject(method = "resize", at = @At(value = "INVOKE", target = METHOD_SIGN_SCROLL_ITEMS))
    private void extrasounds$creativeScreenScrollOnResize(CallbackInfo ci) {
        this.screenScrollHandler.onScroll(this.getScreenHandler().getRow(this.scrollPosition));
    }

    @Inject(method = "search", at = @At("HEAD"))
    private void extrasounds$resetCreativeScrollPos(CallbackInfo ci) {
        this.screenScrollHandler.resetScrollPos();
    }
}
