package net.querz.mca;

import net.querz.nbt.tag.CompoundTag;

import java.util.Iterator;

/**
 * Enhanced iterable/iterator for iterating over {@link Section} block data.
 * See {@link Section#blocksStates()}
 */
public interface BlockStateIterator extends Iterable<CompoundTag>, Iterator<CompoundTag> {
    /**
     * Sets the block state for the current block.
     * Be careful to remember that the block state tag returned by this iterator is a reference
     * that will affect all blocks using that tag. If your intention is to modify "just this one block"
     * then copy the tag before modification - then call this function.
     * @param state State to set. Must not be null.
     */
    void setBlockStateAtCurrent(CompoundTag state);

    /**
     * Performs palette and block state cleanup if, and only if, changes were made via this iterator.
     */
    void cleanupPaletteAndBlockStatesIfDirty();

    /** current block index (in range 0-4095) */
    int currentIndex();
    /** current block x within section (in range 0-15) */
    int currentX();
    /** current block z within section (in range 0-15) */
    int currentZ();
    /** current block y within section (in range 0-15) */
    int currentY();
    /** current block world level y */
    int currentBlockY();
}
