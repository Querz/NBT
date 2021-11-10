package net.querz.mca;

import net.querz.nbt.tag.CompoundTag;

public interface TagWrapper {
    /**
     * Updates the data tag held by this wrapper and returns it.
     * @return A reference to the raw CompoundTag this object is based on.
     */
    CompoundTag updateHandle();

    /**
     * Provides a reference to the wrapped data tag.
     * May be null for objects which support partial loading such as chunks.
     * @return A reference to the raw CompoundTag this object is based on.
     */
    CompoundTag getHandle();
}
