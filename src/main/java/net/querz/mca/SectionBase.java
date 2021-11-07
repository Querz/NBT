package net.querz.mca;

import net.querz.nbt.tag.CompoundTag;
import java.util.*;

public abstract class SectionBase<T extends SectionBase<?>> implements Comparable<T> {
	protected static final int NO_HEIGHT_SENTINEL = Integer.MIN_VALUE;
	protected final CompoundTag data;
	protected int height = NO_HEIGHT_SENTINEL;
	protected SectionContainer container;

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
	* @return the Y value of this section. Multiply by 16 to get world Y value.
	*/
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
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
