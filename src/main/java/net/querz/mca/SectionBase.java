package net.querz.mca;

import net.querz.nbt.tag.CompoundTag;
import java.util.*;

public abstract class SectionBase<T extends SectionBase<?>> implements Comparable<T> {
	public static final int NO_HEIGHT_SENTINEL = Integer.MIN_VALUE;
	protected final CompoundTag data;
	protected int height = NO_HEIGHT_SENTINEL;

	public SectionBase(CompoundTag sectionRoot) {
		Objects.requireNonNull(sectionRoot, "sectionRoot must not be null");
		this.data = sectionRoot;
	}

	public SectionBase() {
		data = new CompoundTag();
	}

	@Override
	public int compareTo(T o) {
		if (o == null) {
			return -1;
		}
		return Integer.compare(height, o.height);
	}

	/**
	 * Checks whether the data of this Section is empty.
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * Gets the height of the bottom of this section relative to Y0 as a section-y value, each 1 section-y is equal to
	 * 16 blocks.
	 * This library (as a whole) will attempt to keep the value returned by this function in sync with the actual
	 * location it has been placed within its chunk.
	 * <p>The value returned may be unreliable if this section is placed in multiple chunks at different heights.
	 * or if user code calls {@link #syncHeight(int)} on a section which is referenced by any chunk.</p>
	 *
	 * @return The Y value of this section.
	 * @deprecated Prefer using {@code chunk.getSectionY(section)} which will always be accurate
	 * within the context of the chunk.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * This method should only be called from a container of Sections such as implementers of
	 * {@link SectionedChunkBase} in an effort to keep the value accurate, or when building sections prior to adding
	 * to a chunk where you want to use this section height property for the convenience of not having to track the
	 * value separately.
	 *
	 * @deprecated To set section height (aka section-y) use
	 * {@code chunk.putSection(int, SectionBase, boolean)} instead of this function. Setting the section height
	 * by calling this function WILL NOT have any affect upon the sections height in the Chunk or or MCA data when
	 * serialized.
	 */
	@Deprecated
	public void setHeight(int height) {
		syncHeight(height);
	}

	void syncHeight(int height) {
		this.height = height;
	}

	protected void checkY(int y) {
		if (y == NO_HEIGHT_SENTINEL) {
			throw new IndexOutOfBoundsException("section height not set");
		}
	}

	/**
	 * Updates the raw CompoundTag that this Section is based on.
	 * This must be called before saving a Section to disk if the Section was manually created
	 * to set the Y of this Section.
	 * @param y The Y-value of this Section to include in the returned tag.
	 *             DOES NOT update this sections height value permanently.
	 * @return A reference to the raw CompoundTag this Section is based on
	 */
	public abstract CompoundTag updateHandle(int y);

	public CompoundTag updateHandle() {
		return updateHandle(height);
	}
}
