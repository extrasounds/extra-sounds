package dev.stashy.extrasounds.mc1_16_4.entry;

import dev.stashy.extrasounds.logics.entry.BaseVanillaGenerator;
import dev.stashy.extrasounds.mapping.SoundDefinition;
import dev.stashy.extrasounds.mapping.SoundGenerator;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.Optional;

import static dev.stashy.extrasounds.sounds.Categories.*;
import static dev.stashy.extrasounds.sounds.Sounds.aliased;
import static dev.stashy.extrasounds.sounds.Sounds.event;

public final class VanillaGenerator extends BaseVanillaGenerator {
    @Override
    protected SoundGenerator generate() {
        return SoundGenerator.of(item -> {
            if (item instanceof BlockItem) {
                return this.generateFromBlock(((BlockItem) item).getBlock());
            } else if (item instanceof ToolItem) {
                final ToolMaterial mats = ((ToolItem) item).getMaterial();
                if (mats instanceof ToolMaterials) {
                    return this.generateFromToolMaterial((ToolMaterials) mats);
                }
                return SoundDefinition.of(aliased(Gear.GENERIC));
            } else if (item instanceof ArmorItem) {
                final ArmorMaterial mats = ((ArmorItem) item).getMaterial();
                if (mats instanceof ArmorMaterials) {
                    return this.generateFromArmorMaterial((ArmorMaterials) mats);
                }
                return SoundDefinition.of(aliased(Gear.GENERIC));
            } else if (this.isPotionItem(item)) {
                return SoundDefinition.of(aliased(POTION));
            } else if (item instanceof BucketItem) {
                final SoundEntry soundEntry = this.getBucketFillSound(((BucketItem) item).fluid).map(sound -> event(sound.getId(), 0.7f)).orElse(aliased(METAL));
                return SoundDefinition.of(soundEntry);
            }

            return super.generalSounds(item);
        });
    }

    private Optional<SoundEvent> getBucketFillSound(Fluid fluid) {
        if (fluid == null) {
            return Optional.empty();
        }

        if (fluid instanceof LavaFluid) {
            return Optional.of(SoundEvents.ITEM_BUCKET_FILL_LAVA);
        } else if (fluid instanceof WaterFluid) {
            return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
        } else {
            return Optional.empty();
        }
    }

    private boolean isPotionItem(Item item) {
        return item instanceof PotionItem || item instanceof ExperienceBottleItem;
    }

    private SoundDefinition generateFromToolMaterial(ToolMaterials mats) {
        switch (mats) {
            case WOOD:
                return SoundDefinition.of(aliased(Gear.WOOD));
            case STONE:
                return SoundDefinition.of(aliased(Gear.STONE));
            case IRON:
                return SoundDefinition.of(aliased(Gear.IRON));
            case DIAMOND:
                return SoundDefinition.of(aliased(Gear.DIAMOND));
            case GOLD:
                return SoundDefinition.of(aliased(Gear.GOLDEN));
            case NETHERITE:
                return SoundDefinition.of(aliased(Gear.NETHERITE));
            default:
                return SoundDefinition.of(aliased(Gear.GENERIC));
        }
    }

    private SoundDefinition generateFromArmorMaterial(ArmorMaterials mats) {
        switch (mats) {
            case IRON:
                return SoundDefinition.of(aliased(Gear.IRON));
            case GOLD:
                return SoundDefinition.of(aliased(Gear.GOLDEN));
            case DIAMOND:
                return SoundDefinition.of(aliased(Gear.DIAMOND));
            case NETHERITE:
                return SoundDefinition.of(aliased(Gear.NETHERITE));
            case CHAIN:
                return SoundDefinition.of(aliased(Gear.CHAIN));
            case TURTLE:
                return SoundDefinition.of(aliased(Gear.TURTLE));
            case LEATHER:
                return SoundDefinition.of(aliased(Gear.LEATHER));
            default:
                return SoundDefinition.of(aliased(Gear.GENERIC));
        }
    }
}
