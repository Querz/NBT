package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.FloatTag;
import net.querz.nbt.IntArrayTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.LongArrayTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.ShortTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TagTest2 extends TestCase {

	public void testByteTag() {
		ByteTag t = new ByteTag("b1", Byte.MAX_VALUE);
		assertEquals(Byte.MAX_VALUE, t.asByte());
		assertEquals(Byte.MAX_VALUE, t.asShort());
		assertEquals(Byte.MAX_VALUE, t.asInt());
		assertEquals(Byte.MAX_VALUE, t.asLong());
		assertEquals("b1", t.getName());
		assertEquals(1, t.getID());
		assertEquals("b1:" + Byte.MAX_VALUE + "b", t.toTagString());
		assertEquals("{\"name\":\"b1\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Byte.MAX_VALUE + "}", t.toString());

		ByteTag t2 = new ByteTag("b1", Byte.MAX_VALUE);
		assertTrue(t.equals(t2));

		ByteTag t3 = new ByteTag("b1", Byte.MIN_VALUE);
		assertFalse(t.equals(t3));

		ByteTag t4 = new ByteTag("b2", Byte.MAX_VALUE);
		assertFalse(t.equals(t4));

		ByteTag t5 = new ByteTag("b2", Byte.MIN_VALUE);
		assertFalse(t.equals(t5));

		//test cloning
		ByteTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{1, 0, 2, 98, 49, 127}, data));
		ByteTag tt = (ByteTag) deserialize(data);
		assertTrue(t.equals(tt));

		assertFalse(new ByteTag("b", (byte) 0).asBoolean());
		assertFalse(new ByteTag("b", Byte.MIN_VALUE).asBoolean());
		assertTrue(new ByteTag("b", (byte) 1).asBoolean());
		assertTrue(new ByteTag("b", Byte.MAX_VALUE).asBoolean());
	}

	public void testShortTag() {
		ShortTag t = new ShortTag("s1", Short.MAX_VALUE);
		assertEquals(Short.MAX_VALUE, t.asShort());
		assertEquals(Short.MAX_VALUE, t.asInt());
		assertEquals(Short.MAX_VALUE, t.asLong());
		assertEquals("s1", t.getName());
		assertEquals(2, t.getID());
		assertEquals("s1:" + Short.MAX_VALUE + "s", t.toTagString());
		assertEquals("{\"name\":\"s1\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Short.MAX_VALUE + "}", t.toString());

		ShortTag t2 = new ShortTag("s1", Short.MAX_VALUE);
		assertTrue(t.equals(t2));

		ShortTag t3 = new ShortTag("s1", Short.MIN_VALUE);
		assertFalse(t.equals(t3));

		ShortTag t4 = new ShortTag("s2", Short.MAX_VALUE);
		assertFalse(t.equals(t4));

		ShortTag t5 = new ShortTag("s2", Short.MIN_VALUE);
		assertFalse(t.equals(t5));

		//test cloning
		ShortTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{2, 0, 2, 115, 49, 127, -1}, data));
		ShortTag tt = (ShortTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testIntTag() {
		IntTag t = new IntTag("i1", Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, t.asInt());
		assertEquals(Integer.MAX_VALUE, t.asLong());
		assertEquals("i1", t.getName());
		assertEquals(3, t.getID());
		assertEquals("i1:" + Integer.MAX_VALUE, t.toTagString());
		assertEquals("{\"name\":\"i1\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Integer.MAX_VALUE + "}", t.toString());

		IntTag t2 = new IntTag("i1", Integer.MAX_VALUE);
		assertTrue(t.equals(t2));

		IntTag t3 = new IntTag("i1", Integer.MIN_VALUE);
		assertFalse(t.equals(t3));

		IntTag t4 = new IntTag("i2", Integer.MAX_VALUE);
		assertFalse(t.equals(t4));

		IntTag t5 = new IntTag("i2", Integer.MIN_VALUE);
		assertFalse(t.equals(t5));

		//test cloning
		IntTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{3, 0, 2, 105, 49, 127, -1, -1, -1}, data));
		IntTag tt = (IntTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testLongTag() {
		LongTag t = new LongTag("l1", Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, t.asLong());
		assertEquals("l1", t.getName());
		assertEquals(4, t.getID());
		assertEquals("l1:" + Long.MAX_VALUE + "l", t.toTagString());
		assertEquals("{\"name\":\"l1\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Long.MAX_VALUE + "}", t.toString());

		LongTag t2 = new LongTag("l1", Long.MAX_VALUE);
		assertTrue(t.equals(t2));

		LongTag t3 = new LongTag("l1", Long.MIN_VALUE);
		assertFalse(t.equals(t3));

		LongTag t4 = new LongTag("l2", Long.MAX_VALUE);
		assertFalse(t.equals(t4));

		LongTag t5 = new LongTag("l2", Long.MIN_VALUE);
		assertFalse(t.equals(t5));

		//test cloning
		LongTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{4, 0, 2, 108, 49, 127, -1, -1, -1, -1, -1, -1, -1}, data));
		LongTag tt = (LongTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testFloatTag() {
		FloatTag t = new FloatTag("f1", Float.MAX_VALUE);
		assertEquals(Float.MAX_VALUE, t.asFloat());
		assertEquals("f1", t.getName());
		assertEquals(5, t.getID());
		assertEquals("f1:" + Float.MAX_VALUE + "f", t.toTagString());
		assertEquals("{\"name\":\"f1\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Float.MAX_VALUE + "}", t.toString());

		FloatTag t2 = new FloatTag("f1", Float.MAX_VALUE);
		assertTrue(t.equals(t2));

		FloatTag t3 = new FloatTag("f1", Float.MIN_VALUE);
		assertFalse(t.equals(t3));

		FloatTag t4 = new FloatTag("f2", Float.MAX_VALUE);
		assertFalse(t.equals(t4));

		FloatTag t5 = new FloatTag("f2", Float.MIN_VALUE);
		assertFalse(t.equals(t5));

		//test cloning
		FloatTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{5, 0, 2, 102, 49, 127, 127, -1, -1}, data));
		FloatTag tt = (FloatTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testDoubleTag() {
		DoubleTag t = new DoubleTag("d1", Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, t.asDouble());
		assertEquals("d1", t.getName());
		assertEquals(6, t.getID());
		assertEquals("d1:" + Double.MAX_VALUE + "d", t.toTagString());
		assertEquals("{\"name\":\"d1\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Double.MAX_VALUE + "}", t.toString());

		DoubleTag t2 = new DoubleTag("d1", Double.MAX_VALUE);
		assertTrue(t.equals(t2));

		DoubleTag t3 = new DoubleTag("d1", Double.MIN_VALUE);
		assertFalse(t.equals(t3));

		DoubleTag t4 = new DoubleTag("d2", Double.MAX_VALUE);
		assertFalse(t.equals(t4));

		DoubleTag t5 = new DoubleTag("d2", Double.MIN_VALUE);
		assertFalse(t.equals(t5));

		//test cloning
		DoubleTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{6, 0, 2, 100, 49, 127, -17, -1, -1, -1, -1, -1, -1}, data));
		DoubleTag tt = (DoubleTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testStringTag() {
		StringTag t = new StringTag("string", "foo");
		assertEquals("foo", t.getValue());
		assertEquals("string", t.getName());
		assertEquals(8, t.getID());
		assertEquals("string:foo", t.toTagString());
		assertEquals("{\"name\":\"string\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":\"foo\"}", t.toString());

		StringTag t2 = new StringTag("string", "foo");
		assertTrue(t.equals(t2));

		StringTag t3 = new StringTag("string", "something else");
		assertFalse(t.equals(t3));

		StringTag t4 = new StringTag("gnirts", "foo");
		assertFalse(t.equals(t4));

		StringTag t5 = new StringTag("gnirts", "something else");
		assertFalse(t.equals(t5));

		//test cloning
		StringTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{8, 0, 6, 115, 116, 114, 105, 110, 103, 0, 3, 102, 111, 111}, data));
		StringTag tt = (StringTag) deserialize(data);
		assertTrue(t.equals(tt));

		//test string escaping
		StringTag allName = new StringTag("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+", "foo");
		assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+:foo", allName.toTagString());
		StringTag escapeName = new StringTag("öäü", "foo");
		assertEquals("\"öäü\":foo", escapeName.toTagString());
		StringTag allValue = new StringTag("foo", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+");
		assertEquals("foo:abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+", allValue.toTagString());
		StringTag escapeValue = new StringTag("foo", "öäü");
		assertEquals("foo:\"öäü\"", escapeValue.toTagString());
		StringTag escapeSpecialChars = new StringTag("\\\n\r\t\"", "foo");
		assertEquals("\"\\\\\\n\\r\\t\\\"\":foo", escapeSpecialChars.toTagString());
		StringTag escapeEmpty = new StringTag("", "foo");
		assertEquals("\"\":foo", escapeEmpty.toTagString());

		//no null values allowed
		TestUtil.assertThrowsException(() -> new StringTag().setName(null), NullPointerException.class);
		TestUtil.assertThrowsException(() -> new StringTag().setValue(null), NullPointerException.class);
	}

	public void testByteArrayTag() {
		ByteArrayTag t = new ByteArrayTag("ba", new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertTrue(Arrays.equals(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, t.getValue()));
		assertEquals("ba", t.getName());
		assertEquals(7, t.getID());
		assertEquals("ba:[B;-128b,0b,127b]", t.toTagString());
		assertEquals("{\"name\":\"ba\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-128,0,127]}", t.toString());

		ByteArrayTag t2 = new ByteArrayTag("ba", new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertTrue(t.equals(t2));

		ByteArrayTag t3 = new ByteArrayTag("ba", new byte[]{Byte.MAX_VALUE, 0, Byte.MIN_VALUE});
		assertFalse(t.equals(t3));

		ByteArrayTag t4 = new ByteArrayTag("ab", new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertFalse(t.equals(t4));

		ByteArrayTag t5 = new ByteArrayTag("ab", new byte[]{Byte.MAX_VALUE, 0, Byte.MIN_VALUE});
		assertFalse(t.equals(t5));

		//test cloning
		ByteArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{7, 0, 2, 98, 97, 0, 0, 0, 3, -128, 0, 127}, data));
		ByteArrayTag tt = (ByteArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testIntArrayTag() {
		IntArrayTag t = new IntArrayTag("ia", new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertTrue(Arrays.equals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, t.getValue()));
		assertEquals("ia", t.getName());
		assertEquals(11, t.getID());
		assertEquals("ia:[I;-2147483648,0,2147483647]", t.toTagString());
		assertEquals("{\"name\":\"ia\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-2147483648,0,2147483647]}", t.toString());

		IntArrayTag t2 = new IntArrayTag("ia", new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertTrue(t.equals(t2));

		IntArrayTag t3 = new IntArrayTag("ia", new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE});
		assertFalse(t.equals(t3));

		IntArrayTag t4 = new IntArrayTag("ai", new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertFalse(t.equals(t4));

		IntArrayTag t5 = new IntArrayTag("ai", new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE});
		assertFalse(t.equals(t5));

		//test cloning
		IntArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{11, 0, 2, 105, 97, 0, 0, 0, 3, -128, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1}, data));
		IntArrayTag tt = (IntArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testLongArrayTag() {
		LongArrayTag t = new LongArrayTag("la", new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertTrue(Arrays.equals(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE}, t.getValue()));
		assertEquals("la", t.getName());
		assertEquals(12, t.getID());
		assertEquals("la:[L;-9223372036854775808l,0l,9223372036854775807l]", t.toTagString());
		assertEquals("{\"name\":\"la\",\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-9223372036854775808,0,9223372036854775807]}", t.toString());

		LongArrayTag t2 = new LongArrayTag("la", new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertTrue(t.equals(t2));

		LongArrayTag t3 = new LongArrayTag("la", new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE});
		assertFalse(t.equals(t3));

		LongArrayTag t4 = new LongArrayTag("al", new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertFalse(t.equals(t4));

		LongArrayTag t5 = new LongArrayTag("al", new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE});
		assertFalse(t.equals(t5));

		//test cloning
		LongArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{12, 0, 2, 108, 97, 0, 0, 0, 3, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1}, data));
		LongArrayTag tt = (LongArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testListTag() {
		ListTag<ByteTag> bl = new ListTag<>("list");
		bl.add(new ByteTag("b", Byte.MIN_VALUE));
		bl.add(new ByteTag("b", (byte) 0));
		bl.add(new ByteTag("b", Byte.MAX_VALUE));
		assertEquals("list", bl.getName());
		assertTrue(3 == bl.size());
		assertEquals(Byte.MIN_VALUE, bl.get(0).asByte());
		assertEquals(0, bl.get(1).asByte());
		assertEquals(Byte.MAX_VALUE, bl.get(2).asByte());
		assertEquals("list:[-128b,0b,127b]", bl.toTagString());
		assertEquals("{\"name\":\"list\",\"type\":\"ListTag\",\"value\":[{\"name\":\"b\",\"type\":\"ByteTag\",\"value\":-128},{\"name\":\"b\",\"type\":\"ByteTag\",\"value\":0},{\"name\":\"b\",\"type\":\"ByteTag\",\"value\":127}]}", bl.toString());
	}

	//TODO: test compoundtag, custom tags, writing to file, reading from file, recursion

	private byte[] serialize(Tag tag) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (DataOutputStream dos = new DataOutputStream(baos)) {
			tag.serialize(dos, 0);
		} catch (IOException ex) {
			fail(ex.getMessage());
		}
		return baos.toByteArray();
	}

	private Tag deserialize(byte[] data) {
		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
			return Tag.deserialize(dis, 0);
		} catch (IOException ex) {
			fail(ex.getMessage());
			return null;
		}
	}
}
