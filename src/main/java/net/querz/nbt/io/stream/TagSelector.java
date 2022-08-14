package net.querz.nbt.io.stream;

import net.querz.nbt.TagReader;
import java.util.List;

public record TagSelector (
	List<String> path,
	String name,
	TagReader<?> type) {

	public TagSelector(String name, TagReader<?> reader) {
		this(List.of(), name, reader);
	}

	public TagSelector(String p1, String name, TagReader<?> reader) {
		this(List.of(p1), name, reader);
	}

	public TagSelector(String p1, String p2, String name, TagReader<?> reader) {
		this(List.of(p1, p2), name, reader);
	}
}
