package net.querz.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EndTag implements Tag {

	public static final EndTag INSTANCE = new EndTag();

	private EndTag() {}

	@Override
	public void write(DataOutput out) throws IOException {}

	@Override
	public byte getID() {
		return END;
	}

	@Override
	public TagType<?> getType() {
		return TYPE;
	}

	@Override
	public EndTag copy() {
		return this;
	}

	@Override
	public void accept(TagVisitor visitor) {
		visitor.visit(this);
	}

	public static final TagType<EndTag> TYPE = new TagType<>() {

		@Override
		public EndTag read(DataInput in, int depth) throws IOException {
			return INSTANCE;
		}

		@Override
		public TagTypeVisitor.ValueResult read(DataInput in, TagTypeVisitor visitor) throws IOException {
			return visitor.visitEnd();
		}

		@Override
		public void skip(DataInput in) throws IOException {}
	};
}
