package net.querz.mca.parsers.impl.anvil118;

import net.querz.mca.parsers.SectionParser;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.Tag;
import java.util.HashMap;
import java.util.Map;

public class SectionParser118 implements SectionParser {

	private final CompoundTag chunk;
	private final Map<Integer, CompoundTag> parsedSections;

	public SectionParser118(CompoundTag chunk) {
		this.chunk = chunk;
		parsedSections = new HashMap<>();
		for (Tag tag : chunk.getList("sections")) {
			CompoundTag section = ((CompoundTag) tag);
			int y = section.getInt("Y");
			parsedSections.put(y, section);
		}
	}

	@Override
	public CompoundTag getDataAt(int index) {
		return parsedSections.get(index);
	}

	@Override
	public void setDataAt(int index, CompoundTag data) {
		parsedSections.put(index, data);
	}

	@Override
	public int getSize() {
		return parsedSections.size();
	}

	@Override
	public void apply() {
		ListTag sections = new ListTag();
		parsedSections.forEach((k, v) -> sections.add(v));
		chunk.put("sections", sections);
	}
}
