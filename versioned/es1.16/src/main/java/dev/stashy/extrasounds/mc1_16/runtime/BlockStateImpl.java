package dev.stashy.extrasounds.mc1_16.runtime;

import dev.stashy.extrasounds.logics.runtime.VersionedBlockStateWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Property;

public class BlockStateImpl extends VersionedBlockStateWrapper {
    private final BlockState blockState;

    public BlockStateImpl(Object blockState) {
        this.blockState = (BlockState) blockState;
    }

    @Override
    public Block getBlockImpl() {
        return this.blockState.getBlock();
    }

    @Override
    public boolean containsProperty(Property<?> property) {
        return this.blockState.contains(property);
    }

    @Override
    public <T extends Comparable<T>> T getProperty(Property<T> property) {
        return this.blockState.get(property);
    }

    @Override
    public BlockSoundGroup getSoundGroupImpl() {
        return this.blockState.getSoundGroup();
    }
}
