package net.querz.mca;

import net.querz.nbt.tag.CompoundTag;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Abstraction for the base of all chunk types which represent chunks composed of sub-chunks {@link SectionBase}.
 * @param <T> Concrete type of section.
 */
public abstract class SectionedChunkBase<T extends SectionBase<?>> extends ChunkBase implements Iterable<T> {
	private final TreeMap<Integer, T> sections = new TreeMap<>();
	private final Map<T, Integer> sectionHeightLookup = new HashMap<>();

	SectionedChunkBase(int lastMCAUpdate) {
		super(lastMCAUpdate);
	}

	/**
	 * Create a new chunk based on raw base data from a region file.
	 * @param data The raw base data to be used.
	 */
	public SectionedChunkBase(CompoundTag data) {
		super(data);
	}

	public boolean containsSection(int sectionY) {
		return sections.containsKey(sectionY);
	}

	public boolean containsSection(T section) {
		return sectionHeightLookup.containsKey(section);
	}

	/**
	 * Sets the section at the specified section-y and synchronizes section-y by calling
	 * {@code section.setHeight(sectionY);}.
	 * @param sectionY Section-y to place the section at. It is the developers responsibility to ensure this value is
	 *                 reasonable. Remember that sections are 16x16x16 cubes and that 1 section-y equals 16 block-y's.
	 * @param section Section to set, may be null to remove the section.
	 * @param moveAllowed If false, and the given section is already present in this chunk {@link IllegalArgumentException}
	 *                       is thrown. If ture, and the given section is already present in this chunk its former
	 *                       section-y location is set {@code null} and the section is updated to live at the
	 *                       specified section-y.
	 * @return The previous section at section-y, or null if there was none or if the given section was already
	 * present at sectionY.
	 * @throws IllegalArgumentException Thrown when the given section is already present in this chunk
	 * and {@code moveAllowed} is false.
	 */
	public T putSection(int sectionY, T section, boolean moveAllowed) throws IllegalArgumentException {
		checkRaw();
		if (sectionY < Byte.MIN_VALUE || sectionY > Byte.MAX_VALUE) {
			throw new IllegalArgumentException(
					"sectionY must be in the range of a BYTE [-128..127], given value " + sectionY);
		}
		if (section != null) {
			if (sectionHeightLookup.containsKey(section)) {
				final int oldY = sectionHeightLookup.getOrDefault(section, SectionBase.NO_HEIGHT_SENTINEL);
				if (sectionY == oldY) return null;
				if (!moveAllowed) {
					throw new IllegalArgumentException(
							String.format("cannot place section at %d, it's already at %d", sectionY, oldY));
				}
				final T oldSection = sections.remove(oldY);
				sectionHeightLookup.remove(oldSection);
				assert(oldSection == section);
				assert(sections.size() == sectionHeightLookup.size());
			}
			section.syncHeight(sectionY);
			sectionHeightLookup.put(section, sectionY);
			final T oldSection = sections.put(sectionY, section);
			if (oldSection != null) sectionHeightLookup.remove(oldSection);
			assert(sections.size() == sectionHeightLookup.size());
			return oldSection;
		} else {
			final T oldSection = sections.remove(sectionY);
			sectionHeightLookup.remove(oldSection);
			assert(sections.size() == sectionHeightLookup.size());
			return oldSection;
		}
	}

	/**
	 * Sets the section at the specified section-y and synchronizes section-y by calling
	 * {@code section.setHeight(sectionY);}.
	 * @param sectionY Section-y to place the section at. It is the developers responsibility to ensure this value is
	 *                 reasonable. Remember that sections are 16x16x16 cubes and that 1 section-y equals 16 block-y's.
	 * @param section Section to set, may be null to remove the section.
	 * @return The previous section at section-y, or null if there was none or if the given section was already
	 * present at sectionY.
	 * @throws IllegalArgumentException Thrown when the given section is already present in this chunk.
	 * Call {@code putSection(sectionY, section, true)} to not throw this error and to move the section instead.
	 */
	public T putSection(int sectionY, T section) {
		return putSection(sectionY, section, false);
	}

	/**
	 * Fetches the section at the specified section-y and synchronizes section-y by calling
	 * {@code section.setHeight(sectionY);} before returning it.
	 * @param sectionY The y-coordinate of the section in this chunk. One section y is equal to 16 world y's
	 * @return The Section.
	 */
	public T getSection(int sectionY) {
		T section = sections.get(sectionY);
		if (section != null) {
			section.syncHeight(sectionY);
		}
		return section;
	}

	/**
	 * Alias for {@link #putSection(int, SectionBase)}
	 * <p>Sets a section at a given section y-coordinate.
	 * @param sectionY The y-coordinate of the section in this chunk. One section y is equal to 16 world y's
	 * @param section The section to be set. May be null to remove the section.
	 * @return the previous value associated with {@code sectionY}, or null if there was no section at {@code sectionY}
	 * or if the section was already at that y.
	 * @throws IllegalStateException Thrown if adding the given section would result in that section instance occurring
	 * multiple times in this chunk. Use {@link #putSection} as an alternative to allow moving the section, otherwise
	 * it is the developers responsibility to first remove the section from this chunk
	 * ({@code setSection(sectionY, null);}) before placing it at a new section-y.
	 */
	public T setSection(int sectionY, T section) {
		return putSection(sectionY, section, false);
	}

	/**
	 * Looks up the section-y for the given section. This is a safer alternative to using
	 * {@link SectionBase#getHeight()} as it will always be accurate within the context of this chunk.
	 * @param section section to lookup the section-y for.
	 * @return section-y; may be negative for worlds with a min build height below zero. If the given section is
	 * {@code null} or is not found in this chunk then {@link SectionBase#NO_HEIGHT_SENTINEL} is returned.
	 */
	public int getSectionY(T section) {
		if (section == null) return SectionBase.NO_HEIGHT_SENTINEL;
		int y = sectionHeightLookup.getOrDefault(section, SectionBase.NO_HEIGHT_SENTINEL);
		section.syncHeight(y);
		return y;
	}

	/**
	 * Gets the minimum section y-coordinate.
	 * @return The y of the lowest populated section in the chunk or {@link SectionBase#NO_HEIGHT_SENTINEL} if there is none.
	 */
	public int getMinSectionY() {
		if (!sections.isEmpty()) {
			return sections.firstKey();
		}
		return SectionBase.NO_HEIGHT_SENTINEL;
	}

	/**
	 * Gets the minimum section y-coordinate.
	 * @return The y of the highest populated section in the chunk or {@link SectionBase#NO_HEIGHT_SENTINEL} if there is none.
	 */
	public int getMaxSectionY() {
		if (!sections.isEmpty()) {
			return sections.lastKey();
		}
		return SectionBase.NO_HEIGHT_SENTINEL;
	}

	/***
	 * Creates a new section and places it in this chunk at the specified section-y
	 * @param sectionY section y
	 * @return new section
	 * @throws IllegalArgumentException thrown if the specified y already has a section - basically throwns if
	 * {@link #containsSection(int)} would return true.
	 */
	public abstract T createSection(int sectionY) throws IllegalArgumentException;

	/**
	 * Sections provided by {@link Iterator#next()} are guaranteed to have correct values returned from
	 * calls to {@link SectionBase#getHeight()}. Also note that the iterator itself can be queried via
	 * {@link SectionIterator#sectionY()} for the true section-y without calling a deprecated method.
	 * @return Section iterator. Supports {@link Iterator#remove()}.
	 */
	@Override
	public SectionIterator<T> iterator() {
		return new SectionIteratorImpl();
	}

	public Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	protected class SectionIteratorImpl implements SectionIterator<T> {
		private final Iterator<Map.Entry<Integer, T>> iter;
		private Map.Entry<Integer, T> current;

		public SectionIteratorImpl() {
			 iter = sections.entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public T next() {
			current = iter.next();
			current.getValue().syncHeight(current.getKey());
			return current.getValue();
		}

		@Override
		public void remove() {
			sectionHeightLookup.remove(current.getValue());
			iter.remove();
		}

		@Override
		public int sectionY() {
			return current.getKey();
		}

		@Override
		public int sectionBlockMinY() {
			return sectionY() * 16;
		}

		@Override
		public int sectionBlockMaxY() {
			return sectionY() * 16 + 15;
		}
	}
}
