package net.querz.nbt.io.stream;

import net.querz.nbt.TagReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TagSelector {
	private final List<String> path;
	private final String name;
	private final TagReader<?> type;

	public TagSelector(List<String> path, String name, TagReader<?> type) {
		this.path = path;
		this.name = name;
		this.type = type;
	}

	public TagSelector(String name, TagReader<?> reader) {
		this(Collections.emptyList(), name, reader);
	}

	public TagSelector(String p1, String name, TagReader<?> reader) {
		this(Collections.singletonList(p1), name, reader);
	}

	public TagSelector(String p1, String p2, String name, TagReader<?> reader) {
		this(Arrays.asList(p1, p2), name, reader);
	}

	public List<String> path() {
		return path;
	}

	public String name() {
		return name;
	}

	public TagReader<?> type() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TagSelector that = (TagSelector) o;
		return Objects.equals(path, that.path) && Objects.equals(name, that.name) && Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, name, type);
	}

}
