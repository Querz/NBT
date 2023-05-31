package net.querz.nbt.io.stream;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.TagReader;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class SelectionStreamTagVisitor extends StreamTagVisitor {

	private int remainingNumberOfFields;
	private final Set<TagReader<?>> wantedTypes;
	private final Deque<TagTree> stack = new ArrayDeque<>();

	public SelectionStreamTagVisitor(TagSelector... selectors) {
		remainingNumberOfFields = selectors.length;
		Set<TagReader<?>> wt = new HashSet<>();
		TagTree tree = new TagTree();

		for (TagSelector selector : selectors) {
			tree.addEntry(selector);
			wt.add(selector.type());
		}

		stack.push(tree);
		wt.add(CompoundTag.READER);
		wantedTypes = Collections.unmodifiableSet(wt);
	}

	@Override
	public ValueResult visitRootEntry(TagReader<?> reader) {
		return reader != CompoundTag.READER ? ValueResult.RETURN : super.visitRootEntry(reader);
	}

	@Override
	public EntryResult visitEntry(TagReader<?> reader) {
		TagTree tree = stack.element();
		if (depth() > tree.depth()) {
			return super.visitEntry(reader);
		} else if (remainingNumberOfFields <= 0) {
			return EntryResult.RETURN;
		} else {
			return !wantedTypes.contains(reader) ? EntryResult.SKIP : super.visitEntry(reader);
		}
	}

	@Override
	public EntryResult visitEntry(TagReader<?> reader, String name) {
		TagTree tree = stack.element();
		if (depth() > tree.depth()) {
			return super.visitEntry(reader, name);
		} else if (tree.selected().remove(name, reader)) {
			remainingNumberOfFields--;
			return super.visitEntry(reader, name);
		} else {
			if (reader == CompoundTag.READER) {
				TagTree child = tree.tree().get(name);
				if (child != null) {
					stack.push(child);
					return super.visitEntry(reader, name);
				}
			}
			return EntryResult.SKIP;
		}
	}

	@Override
	public ValueResult visitContainerEnd() {
		if (depth() == stack.element().depth()) {
			stack.pop();
		}
		return super.visitContainerEnd();
	}

	public int getRemainingNumberOfFields() {
		return remainingNumberOfFields;
	}
}
