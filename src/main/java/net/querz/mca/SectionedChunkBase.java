package net.querz.mca;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.querz.mca.LoadFlags.*;

/**
 * Abstraction for the base of all chunk types which represent chunks composed of sub-chunks {@link SectionBase}.
 * @param <T> Concrete type of section.
 */
public abstract class SectionedChunkBase<T extends SectionBase<?>> extends ChunkBase implements Iterable<T> {

	protected final TreeMap<Integer, T> sections = new TreeMap<>();

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

	/**
	 * Fetches the section at the given y-coordinate.
	 * @param sectionY The y-coordinate of the section in this chunk. One section y is equal to 16 world y's
	 * @return The Section.
	 */
	public T getSection(int sectionY) {
		T section = sections.get(sectionY);
		if (section != null) {
			section.setHeight(sectionY);
		}
		return section;
	}

	/**
	 * Sets a section at a given section y-coordinate
	 * @param sectionY The y-coordinate of the section in this chunk. One section y is equal to 16 world y's
	 * @param section The section to be set.
	 */
	public void setSection(int sectionY, T section) {
		checkRaw();
		if (section != null) {
			section.setHeight(sectionY);
		}
		sections.put(sectionY, section);
	}

	/**
	 * Gets the minimum section y-coordinate.
	 * @return The y of the lowest section in the chunk.
	 */
	public int getMinSectionY() {
		return sections.firstKey();
	}

	/**
	 * Gets the minimum section y-coordinate.
	 * @return The y of the highest populated section in the chunk.
	 */
	public int getMaxSectionY() {
		return sections.lastKey();
	}

	@Override
	public Iterator<T> iterator() {
		return sections.values().iterator();
	}

	public Stream<T> stream() {
		return sections.values().stream();
	}

}
