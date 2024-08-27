package net.querz.nbt.io.stream;

import net.querz.nbt.TagReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TagTree {
	private final int depth;
	private final Map<String, TagTree> tree;
	private final Map<String, TagReader<?>> selected;

	public TagTree(int depth, Map<String, TagTree> tree, Map<String, TagReader<?>> selected) {
		this.depth = depth;
		this.tree = tree;
		this.selected = selected;
	}

	/**
	 * Creates a new TagTree root element with {@code depth = 1}.
	 */
	public TagTree() {
		this(1, new HashMap<>(), new HashMap<>());
	}

	private TagTree(int depth) {
		this(depth, new HashMap<>(), new HashMap<>());
	}

	/**
	 * Recursively adds a new entry to the TagTree.
	 * @param selector The TagSelector that specifies which tag to select.
	 */
	public void addEntry(TagSelector selector) {
		if (depth > selector.path().size()) {
			selected.put(selector.name(), selector.type());
		} else {
			tree.computeIfAbsent(selector.path().get(depth - 1), k -> new TagTree(depth + 1)).addEntry(selector);
		}
	}

	/**
	 * Checks if a tag with a specific name and reader is selected on this depth of the TagTree.
	 * @param name The name of the selected tag
	 * @param reader The reader of the selected tag
	 * @return {@code true} if the TagTree contains the tag with the specified reader on the current depth.
	 */
	public boolean isSelected(String name, TagReader<?> reader) {
		return reader.equals(selected.get(name));
	}

	public int depth() {
		return depth;
	}

	public Map<String, TagTree> tree() {
		return tree;
	}

	public Map<String, TagReader<?>> selected() {
		return selected;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TagTree tagTree = (TagTree) o;
		return depth == tagTree.depth && Objects.equals(tree, tagTree.tree) && Objects.equals(selected, tagTree.selected);
	}

	@Override
	public int hashCode() {
		return Objects.hash(depth, tree, selected);
	}

}
