package net.querz.nbt.io.stream;

import net.querz.nbt.TagType;
import java.util.List;

public record TagSelector (
	List<String> path,
	String name,
	TagType<?> type) {

	public TagSelector(String name, TagType<?> type) {
		this(List.of(), name, type);
	}

	public TagSelector(String p1, String name, TagType<?> type) {
		this(List.of(p1), name, type);
	}

	public TagSelector(String p1, String p2, String name, TagType<?> type) {
		this(List.of(p1, p2), name, type);
	}
}
