package net.querz.nbt;

import net.querz.nbt.custom.CharTag;

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
	}

	public void testClassFromID() {
		assertThrowsNoRuntimeException(() -> TagFactory.classFromID(1));
		assertThrowsRuntimeException(() -> TagFactory.classFromID(20), IllegalArgumentException.class);
	}

	public void testIDFromClass() {
		assertThrowsNoRuntimeException(() -> TagFactory.idFromClass(ByteTag.class));
		assertThrowsRuntimeException(() -> TagFactory.idFromClass(CharTag.class), IllegalArgumentException.class);
	}

	public void testRegisterCustomTag() {
		assertThrowsRuntimeException(() -> TagFactory.registerCustomTag(-1, CharTag::new, CharTag.class), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> TagFactory.registerCustomTag(12, CharTag::new, CharTag.class), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> TagFactory.registerCustomTag(128, CharTag::new, CharTag.class), IllegalArgumentException.class);
		CharTag.register();
		assertThrowsRuntimeException(CharTag::register, IllegalArgumentException.class);
	}

	public void testUnregisterCustomTag() {
		CharTag.register();
		assertThrowsNoRuntimeException(() -> TagFactory.unregisterCustomTag(new CharTag().getID()));
	}
}
