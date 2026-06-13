package dev.stashy.extrasounds.logics;

import dev.stashy.soundcategories.CategoryLoader;
import net.minecraft.sound.SoundCategory;

public final class Mixers implements CategoryLoader {
    @Register(master = true, defaultLevel = 0.33f, preview = ExtraSounds.MODID + ":item.pickup_all")
    public static SoundCategory MASTER;

    @Register(preview = ExtraSounds.MODID + ":generic.scroll")
    public static SoundCategory SCREENS;

    @Register(preview = {
            ExtraSounds.MODID + ":item.category.gear.generic",
            ExtraSounds.MODID + ":item.category.gear.leather",
            ExtraSounds.MODID + ":item.category.gear.stone",
            ExtraSounds.MODID + ":item.category.gear.iron",
            ExtraSounds.MODID + ":item.category.gear.copper",
            ExtraSounds.MODID + ":item.category.gear.golden",
            ExtraSounds.MODID + ":item.category.gear.diamond",
            ExtraSounds.MODID + ":item.category.gear.netherite"
    })
    public static SoundCategory HOTBAR;

    @Register(preview = ExtraSounds.MODID + ":chat.message")
    public static SoundCategory CHAT;

    @Register(preview = ExtraSounds.MODID + ":chat.mention")
    public static SoundCategory CHAT_MENTION;

    @Register(toggle = true, preview = ExtraSounds.MODID + ":keyboard.screenshot")
    public static SoundCategory SCREENSHOT;

    @Register(preview = {
            ExtraSounds.MODID + ":effect.add.positive",
            ExtraSounds.MODID + ":effect.add.negative"
    })
    public static SoundCategory EFFECTS;

    @Register(preview = ExtraSounds.MODID + ":keyboard.type")
    public static SoundCategory TYPING;

    @Register(preview = ExtraSounds.MODID + ":action.bow")
    public static SoundCategory ITEM_INTR;

    @Register(preview = {
            ExtraSounds.MODID + ":action.redstone_component.on",
            ExtraSounds.MODID + ":action.redstone_component.off"
    })
    public static SoundCategory BLOCK_INTR;

    @Register(defaultLevel = 0.f, preview = ExtraSounds.MODID + ":entity.poof")
    public static SoundCategory ENTITY;

    @Register(toggle = true, preview = ExtraSounds.MODID + ":item.drop")
    public static SoundCategory ITEM_DROP;
}
