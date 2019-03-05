package net.querz.nbt.io;

import net.querz.nbt.MaxDepthReachedException;

public interface MaxDepthIO {

	default int decrementMaxDepth(int maxDepth) {
		if (maxDepth < 0) {
			throw new IllegalArgumentException("negative maximum depth is not allowed");
		} else if (maxDepth == 0) {
			throw new MaxDepthReachedException("reached maximum depth of NBT structure");
		}
		return --maxDepth;
	}
}
