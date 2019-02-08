package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class EndTag extends Tag<Void> {

	static final EndTag INSTANCE = new EndTag();

	private EndTag() {
		super(null);
	}

	@Override
	protected Void checkValue(Void value) {
		return value;
	}

	@Override
	public void serializeValue(DataOutputStream dos, int maxDepth) {
		//nothing to do
	}

	@Override
	public void deserializeValue(DataInputStream dis, int maxDepth) {
		//nothing to do
	}

	@Override
	public String valueToString(int maxDepth) {
		return "\"end\"";
	}

	@Override
	public String valueToTagString(int maxDepth) {
		throw new UnsupportedOperationException("EndTag cannot be turned into a String");
	}

	@Override
	public EndTag clone() {
		return INSTANCE;
	}
}
