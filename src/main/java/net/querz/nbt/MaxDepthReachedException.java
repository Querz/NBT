package net.querz.nbt;

public class MaxDepthReachedException extends RuntimeException {
	private static final long serialVersionUID = 4466414196534014945L;

	public MaxDepthReachedException() {
		super("Reached maximum depth (" + Tag.MAX_DEPTH + ") of NBT structure");
	}
}
