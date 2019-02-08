package net.querz.nbt;

import java.util.Arrays;

public class ByteTagTest extends NBTTestCase {

	public void testStringConversion() {
		ByteTag t = new ByteTag(Byte.MAX_VALUE);
		assertEquals(Byte.MAX_VALUE, t.asByte());
		assertEquals(Byte.MAX_VALUE, t.asShort());
		assertEquals(Byte.MAX_VALUE, t.asInt());
		assertEquals(Byte.MAX_VALUE, t.asLong());
		assertEquals(1, t.getID());
		assertEquals(Byte.MAX_VALUE + "b", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Byte.MAX_VALUE + "}", t.toString());
	}

	public void testEquals() {
		ByteTag t = new ByteTag(Byte.MAX_VALUE);
		ByteTag t2 = new ByteTag(Byte.MAX_VALUE);
		assertTrue(t.equals(t2));
		ByteTag t3 = new ByteTag(Byte.MIN_VALUE);
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		ByteTag t = new ByteTag(Byte.MAX_VALUE);
		ByteTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		ByteTag t = new ByteTag(Byte.MAX_VALUE);
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{1, 0, 0, 127}, data));
		ByteTag tt = (ByteTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new ByteTag((byte) 5).compareTo(new ByteTag((byte) 5)));
		assertTrue(0 < new ByteTag((byte) 7).compareTo(new ByteTag((byte) 5)));
		assertTrue(0 > new ByteTag((byte) 5).compareTo(new ByteTag((byte) 7)));
		assertThrowsRuntimeException(() -> new ByteTag((byte) 5).compareTo(null), NullPointerException.class);
	}

	public void testBoolean() {
		assertFalse(new ByteTag((byte) 0).asBoolean());
		assertFalse(new ByteTag(Byte.MIN_VALUE).asBoolean());
		assertTrue(new ByteTag((byte) 1).asBoolean());
		assertTrue(new ByteTag(Byte.MAX_VALUE).asBoolean());
		assertEquals(1, new ByteTag(true).asByte());
		assertEquals(0, new ByteTag(false).asByte());
	}
}
