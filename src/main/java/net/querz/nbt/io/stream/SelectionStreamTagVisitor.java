package net.querz.nbt.io.stream;

import net.querz.nbt.CompoundTag;
import net.querz.nbt.TagType;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class SelectionStreamTagVisitor extends StreamTagVisitor {

	private int remainingNumberOfFields;
	private final Set<TagType<?>> wantedTypes;
	private final Deque<TagTree> stack = new ArrayDeque<>();

	public SelectionStreamTagVisitor(TagSelector... selectors) {
		remainingNumberOfFields = selectors.length;
		Set<TagType<?>> wt = new HashSet<>();
		TagTree tree = new TagTree();

		for (TagSelector selector : selectors) {
			tree.addEntry(selector);
			wt.add(selector.type());
		}

		stack.push(tree);
		wt.add(CompoundTag.TYPE);
		wantedTypes = Collections.unmodifiableSet(wt);
	}

	@Override
	public ValueResult visitRootEntry(TagType<?> type) {
		return type != CompoundTag.TYPE ? ValueResult.RETURN : super.visitRootEntry(type);
	}

	@Override
	public EntryResult visitEntry(TagType<?> type) {
		TagTree tree = stack.element();
		if (depth() > tree.depth()) {
			return super.visitEntry(type);
		} else if (remainingNumberOfFields <= 0) {
			return EntryResult.RETURN;
		} else {
			return !wantedTypes.contains(type) ? EntryResult.SKIP : super.visitEntry(type);
		}
	}

	@Override
	public EntryResult visitEntry(TagType<?> type, String name) {
		TagTree tree = stack.element();
		if (depth() > tree.depth()) {
			return super.visitEntry(type, name);
		} else if (tree.selected().remove(name, type)) {
			remainingNumberOfFields--;
			return super.visitEntry(type, name);
		} else {
			if (type == CompoundTag.TYPE) {
				TagTree child = tree.tree().get(name);
				if (child != null) {
					stack.push(child);
					return super.visitEntry(type, name);
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
