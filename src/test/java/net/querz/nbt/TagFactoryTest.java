package net.querz.nbt;

import net.querz.nbt.custom.CharTag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TagFactoryTest extends NBTTestCase {

	public void testFromID() {
		assertEquals(EndTag.class, TagFactory.fromID(0).getClass());
		assertEquals(ByteTag.class, TagFactory.fromID(1).getClass());
		assertEquals(ShortTag.class, TagFactory.fromID(2).getClass());
		assertEquals(IntTag.class, TagFactory.fromID(3).getClass());
		assertEquals(LongTag.class, TagFactory.fromID(4).getClass());
		assertEquals(FloatTag.class, TagFactory.fromID(5).getClass());
		assertEquals(DoubleTag.class, TagFactory.fromID(6).getClass());
		assertEquals(ByteArrayTag.class, TagFactory.fromID(7).getClass());
		assertEquals(StringTag.class, TagFactory.fromID(8).getClass());
		assertEquals(ListTag.class, TagFactory.fromID(9).getClass());
		assertEquals(CompoundTag.class, TagFactory.fromID(10).getClass());
		assertEquals(IntArrayTag.class, TagFactory.fromID(11).getClass());
		assertEquals(LongArrayTag.class, TagFactory.fromID(12).getClass());
		assertThrowsRuntimeException(() -> TagFactory.fromID(-1), IllegalArgumentException.class);
		TagFactory.registerCustomTag(127, InvalidCustomTag.class);
		assertThrowsRuntimeException(() -> TagFactory.fromID(127), RuntimeException.class);
	}

	public void testRegisterCustomTag() {
		assertThrowsRuntimeException(() -> TagFactory.registerCustomTag(-1, CharTag.class), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> TagFactory.registerCustomTag(12, CharTag.class), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> TagFactory.registerCustomTag(128, CharTag.class), IllegalArgumentException.class);
		CharTag.register();
		assertThrowsRuntimeException(CharTag::register, IllegalArgumentException.class);
	}

	public void testUnregisterCustomTag() {
		CharTag.register();
		assertThrowsNoRuntimeException(() -> TagFactory.unregisterCustomTag(new CharTag().getID()));
	}

	public class InvalidCustomTag extends Tag<Void> {

		public InvalidCustomTag(InvalidCustomTag invalidConstructorParam) {

		}

		@Override
		public byte getID() {
			return 127;
		}

		@Override
		protected Void getEmptyValue() {
			return null;
		}

		@Override
		public void serializeValue(DataOutputStream dos, int depth) throws IOException {

		}

		@Override
		public void deserializeValue(DataInputStream dis, int depth) throws IOException {

		}

		@Override
		public String valueToString(int depth) {
			return null;
		}

		@Override
		public String valueToTagString(int depth) {
			return null;
		}

		@Override
		public InvalidCustomTag clone() {
			return null;
		}

		@Override
		public int compareTo(Tag<Void> o) {
			return 0;
		}
	}
}
