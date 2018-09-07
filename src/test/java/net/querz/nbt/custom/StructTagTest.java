package net.querz.nbt.custom;

import net.querz.nbt.ByteTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.NBTTestCase;
import java.util.Arrays;

public class StructTagTest extends NBTTestCase {

	private StructTag createStructTag() {
		StructTag s = new StructTag();
		s.add(new ByteTag(Byte.MAX_VALUE));
		s.add(new IntTag(Integer.MAX_VALUE));
		return s;
	}

	public void testStringConversion() {
		StructTag s = createStructTag();
		assertEquals(120, s.getID());
		assertEquals("[127b,2147483647]", s.toTagString());
		assertEquals("{\"type\":\"StructTag\",\"value\":[{\"type\":\"ByteTag\",\"value\":127},{\"type\":\"IntTag\",\"value\":2147483647}]}", s.toString());
	}

	public void testEquals() {
		StructTag s = createStructTag();

		StructTag s2 = new StructTag();
		s2.add(new ByteTag(Byte.MAX_VALUE));
		s2.add(new IntTag(Integer.MAX_VALUE));
		assertTrue(s.equals(s2));

		StructTag s3 = new StructTag();
		s3.add(new IntTag(Integer.MAX_VALUE));
		s3.add(new ByteTag(Byte.MAX_VALUE));
		assertFalse(s.equals(s3));

		StructTag s4 = new StructTag();
		s4.add(new ByteTag(Byte.MAX_VALUE));
		assertFalse(s.equals(s4));
	}

	public void testClone() {
		StructTag s = createStructTag();
		StructTag c = s.clone();
		assertTrue(s.equals(c));
		assertFalse(s == c);
		assertFalse(invokeGetValue(s) == invokeGetValue(c));
	}

	public void testSerializeDeserialize() {
		StructTag s = createStructTag();
		StructTag.register();
		byte[] data = serialize(s);
		assertTrue(Arrays.equals(new byte[]{120, 0, 0, 0, 0, 0, 2, 1, 127, 3, 127, -1, -1, -1}, data));
		StructTag ss = (StructTag) deserialize(data);
		assertTrue(s.equals(ss));
	}
}
