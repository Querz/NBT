package net.querz.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

public class ByteArrayTagTest extends NBTTestCase {

	public void testStringConversion() {
		ByteArrayTag t = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertTrue(Arrays.equals(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, t.getValue()));
		assertEquals(7, t.getID());
		assertEquals("[B;-128b,0b,127b]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-128,0,127]}", t.toString());
	}

	public void testEquals() {
		ByteArrayTag t = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		ByteArrayTag t2 = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertTrue(t.equals(t2));
		ByteArrayTag t3 = new ByteArrayTag(new byte[]{Byte.MAX_VALUE, 0, Byte.MIN_VALUE});
		assertFalse(t.equals(t3));
	}

	public void testClone() {
		ByteArrayTag t = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		ByteArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());
	}

	public void testSerializeDeserialize() {
		ByteArrayTag t = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{7, 0, 0, 0, 0, 0, 3, -128, 0, 127}, data));
		ByteArrayTag tt = (ByteArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		ByteArrayTag t = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		ByteArrayTag t2 = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		ByteArrayTag t3 = new ByteArrayTag(new byte[]{Byte.MAX_VALUE, 0, Byte.MIN_VALUE});
		ByteArrayTag t4 = new ByteArrayTag(new byte[]{0, Byte.MIN_VALUE});
		assertEquals(0, t.compareTo(t2));
		assertEquals(0, t.compareTo(t3));
		assertTrue(0 < t.compareTo(t4));
		assertTrue(0 > t4.compareTo(t));
		assertThrowsRuntimeException(() -> t.compareTo(null), NullPointerException.class);
	}

	public void testInvalidType() {
		assertThrowsRuntimeException(NotAnArrayTag::new, UnsupportedOperationException.class);
		assertThrowsRuntimeException(() -> new NotAnArrayTag("test"), UnsupportedOperationException.class);
	}

	public class NotAnArrayTag extends ArrayTag<String> {

		public NotAnArrayTag() {
			super("");
		}

		public NotAnArrayTag(String value) {
			super(value);
		}

		@Override
		public void serializeValue(DataOutputStream dos, int depth) {
			throw new UnsupportedOperationException("goddammit, this is a test class, you don't want to save it.");
		}

		@Override
		public void deserializeValue(DataInputStream dis, int depth) {
			throw new UnsupportedOperationException("goddammit, this is a test class, you don't want to load it.");
		}

		@Override
		public String valueToTagString(int depth) {
			return escapeString(getValue(), true);
		}

		@Override
		public NotAnArrayTag clone() {
			return new NotAnArrayTag(getName());
		}
	}
}
