package net.querz.nbt.tag;

import net.querz.NBTTestCase;

import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;

public class DoubleTagTest extends NBTTestCase {
	
	public void testCreate() {
		DoubleTag t = new DoubleTag(Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, t.asDouble());
		t = new DoubleTag();
		assertEquals(DoubleTag.ZERO_VALUE, t.asDouble());
	}

	public void testSetValue() {
		DoubleTag t = new DoubleTag();
		t.setValue(123.4);
		assertEquals(123.4, t.asDouble());
	}

	public void testStringConversion() {
		DoubleTag t = new DoubleTag(Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, t.asDouble());
		assertEquals(6, t.getID());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Double.MAX_VALUE + "}", t.toString());
	}

	public void testEquals() {
		DoubleTag t = new DoubleTag(Double.MAX_VALUE);
		assertEquals(t, new DoubleTag(Double.MAX_VALUE));
		assertNotEquals(t, new DoubleTag(Double.MIN_VALUE));
		assertEquals(new DoubleTag(Double.NaN), new DoubleTag(Double.NaN));
	}

	public void testClone() {
		DoubleTag t = new DoubleTag(Double.MAX_VALUE);
		DoubleTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
	}

	public void testSerializeDeserialize() {
		DoubleTag t = new DoubleTag(Double.MAX_VALUE);
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{6, 0, 0, 127, -17, -1, -1, -1, -1, -1, -1}, data));
		DoubleTag tt = (DoubleTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCompareTo() {
		assertEquals(0, new DoubleTag(5).compareTo(new DoubleTag(5)));
		assertTrue(0 < new DoubleTag(7).compareTo(new DoubleTag(5)));
		assertTrue(0 > new DoubleTag(5).compareTo(new DoubleTag(7)));
		assertThrowsRuntimeException(() -> new DoubleTag(5).compareTo(null), NullPointerException.class);
	}
}
