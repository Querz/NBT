package net.querz.nbt;

import java.io.IOException;

public class EndTag extends Tag {
	public EndTag() {
		super();
	}
	
	@Override
	public Object getValue() {
		return null;
	}

	@Override
	protected void serialize(NBTOutputStream nbtOut, int depth) throws IOException {
		//already writes (byte) 0 as the tag type in superclass
	}

	@Override
	protected Tag deserialize(NBTInputStream nbtIn, int depth) throws IOException {
		return this;
	}

	@Override
	public String toTagString() {
		return "";
	}
	
	@Override
	protected String valueToTagString(int depth) {
		return "";
	}
	
	@Override
	public String toString() {
		return "<end>";
	}
	
	@Override
	public EndTag clone() {
		return new EndTag();
	}
}
