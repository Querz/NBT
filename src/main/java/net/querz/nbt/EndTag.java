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
	protected void serialize(NBTOutputStream nbtOut) throws IOException {
		//already writes (byte) 0 as the tag type in superclass
	}

	@Override
	protected Tag deserialize(NBTInputStream nbtIn) throws IOException {
		return this;
	}

	@Override
	public String toTagString() {
		return "";
	}
	
	@Override
	public String valueToTagString() {
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
