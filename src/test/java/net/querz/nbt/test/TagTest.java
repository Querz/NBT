package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.EndTag;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import static net.querz.nbt.test.TestUtil.*;

public class TagTest extends TestCase {

	public void testByteTag() {
		ByteTag t = new ByteTag(Byte.MAX_VALUE);
		assertEquals(Byte.MAX_VALUE, t.asByte());
		assertEquals(Byte.MAX_VALUE, t.asShort());
		assertEquals(Byte.MAX_VALUE, t.asInt());
		assertEquals(Byte.MAX_VALUE, t.asLong());
		assertEquals(1, t.getID());
		assertEquals(Byte.MAX_VALUE + "b", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Byte.MAX_VALUE + "}", t.toString());

		ByteTag t2 = new ByteTag(Byte.MAX_VALUE);
		assertTrue(t.equals(t2));

		ByteTag t3 = new ByteTag(Byte.MIN_VALUE);
		assertFalse(t.equals(t3));

		//test cloning
		ByteTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{1, 0, 0, 127}, data));
		ByteTag tt = (ByteTag) deserialize(data);
		assertTrue(t.equals(tt));

		assertFalse(new ByteTag((byte) 0).asBoolean());
		assertFalse(new ByteTag(Byte.MIN_VALUE).asBoolean());
		assertTrue(new ByteTag((byte) 1).asBoolean());
		assertTrue(new ByteTag(Byte.MAX_VALUE).asBoolean());
	}

	public void testShortTag() {
		ShortTag t = new ShortTag(Short.MAX_VALUE);
		assertEquals(Short.MAX_VALUE, t.asShort());
		assertEquals(Short.MAX_VALUE, t.asInt());
		assertEquals(Short.MAX_VALUE, t.asLong());
		assertEquals(2, t.getID());
		assertEquals(Short.MAX_VALUE + "s", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Short.MAX_VALUE + "}", t.toString());

		ShortTag t2 = new ShortTag(Short.MAX_VALUE);
		assertTrue(t.equals(t2));

		ShortTag t3 = new ShortTag(Short.MIN_VALUE);
		assertFalse(t.equals(t3));

		//test cloning
		ShortTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{2, 0, 0, 127, -1}, data));
		ShortTag tt = (ShortTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testIntTag() {
		IntTag t = new IntTag(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, t.asInt());
		assertEquals(Integer.MAX_VALUE, t.asLong());
		assertEquals(3, t.getID());
		assertEquals(Integer.MAX_VALUE + "", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Integer.MAX_VALUE + "}", t.toString());

		IntTag t2 = new IntTag(Integer.MAX_VALUE);
		assertTrue(t.equals(t2));

		IntTag t3 = new IntTag(Integer.MIN_VALUE);
		assertFalse(t.equals(t3));

		//test cloning
		IntTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{3, 0, 0, 127, -1, -1, -1}, data));
		IntTag tt = (IntTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testLongTag() {
		LongTag t = new LongTag(Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, t.asLong());
		assertEquals(4, t.getID());
		assertEquals(Long.MAX_VALUE + "l", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Long.MAX_VALUE + "}", t.toString());

		LongTag t2 = new LongTag(Long.MAX_VALUE);
		assertTrue(t.equals(t2));

		LongTag t3 = new LongTag(Long.MIN_VALUE);
		assertFalse(t.equals(t3));

		//test cloning
		LongTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{4, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1}, data));
		LongTag tt = (LongTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testFloatTag() {
		FloatTag t = new FloatTag(Float.MAX_VALUE);
		assertEquals(Float.MAX_VALUE, t.asFloat());
		assertEquals(5, t.getID());
		assertEquals(Float.MAX_VALUE + "f", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Float.MAX_VALUE + "}", t.toString());

		FloatTag t2 = new FloatTag(Float.MAX_VALUE);
		assertTrue(t.equals(t2));

		FloatTag t3 = new FloatTag(Float.MIN_VALUE);
		assertFalse(t.equals(t3));

		//test cloning
		FloatTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{5, 0, 0, 127, 127, -1, -1}, data));
		FloatTag tt = (FloatTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testDoubleTag() {
		DoubleTag t = new DoubleTag(Double.MAX_VALUE);
		assertEquals(Double.MAX_VALUE, t.asDouble());
		assertEquals(6, t.getID());
		assertEquals(Double.MAX_VALUE + "d", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":" + Double.MAX_VALUE + "}", t.toString());

		DoubleTag t2 = new DoubleTag(Double.MAX_VALUE);
		assertTrue(t.equals(t2));

		DoubleTag t3 = new DoubleTag(Double.MIN_VALUE);
		assertFalse(t.equals(t3));

		//test cloning
		DoubleTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{6, 0, 0, 127, -17, -1, -1, -1, -1, -1, -1}, data));
		DoubleTag tt = (DoubleTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testStringTag() {
		StringTag t = new StringTag("foo");
		assertEquals("foo", t.getValue());
		assertEquals(8, t.getID());
		assertEquals("foo", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":\"foo\"}", t.toString());

		StringTag t2 = new StringTag("foo");
		assertTrue(t.equals(t2));

		StringTag t3 = new StringTag("something else");
		assertFalse(t.equals(t3));

		//test cloning
		StringTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{8, 0, 0, 0, 3, 102, 111, 111}, data));
		StringTag tt = (StringTag) deserialize(data);
		assertTrue(t.equals(tt));

		//test string escaping
		StringTag allValue = new StringTag("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+");
		assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789_-+", allValue.toTagString());
		StringTag escapeValue = new StringTag("öäü");
		assertEquals("\"öäü\"", escapeValue.toTagString());
		StringTag escapeSpecialChars = new StringTag("\\\n\r\t\"");
		assertEquals("\"\\\\\\n\\r\\t\\\"\"", escapeSpecialChars.toTagString());
		StringTag escapeEmpty = new StringTag("");
		assertEquals("\"\"", escapeEmpty.toTagString());

		//no null values allowed
		assertThrowsException(() -> new StringTag().setValue(null), NullPointerException.class);
	}

	public void testByteArrayTag() {
		ByteArrayTag t = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertTrue(Arrays.equals(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, t.getValue()));
		assertEquals(7, t.getID());
		assertEquals("[B;-128b,0b,127b]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-128,0,127]}", t.toString());

		ByteArrayTag t2 = new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertTrue(t.equals(t2));

		ByteArrayTag t3 = new ByteArrayTag(new byte[]{Byte.MAX_VALUE, 0, Byte.MIN_VALUE});
		assertFalse(t.equals(t3));

		//test cloning
		ByteArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{7, 0, 0, 0, 0, 0, 3, -128, 0, 127}, data));
		ByteArrayTag tt = (ByteArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testIntArrayTag() {
		IntArrayTag t = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertTrue(Arrays.equals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, t.getValue()));
		assertEquals(11, t.getID());
		assertEquals("[I;-2147483648,0,2147483647]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-2147483648,0,2147483647]}", t.toString());

		IntArrayTag t2 = new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertTrue(t.equals(t2));

		IntArrayTag t3 = new IntArrayTag(new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE});
		assertFalse(t.equals(t3));

		//test cloning
		IntArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{11, 0, 0, 0, 0, 0, 3, -128, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1}, data));
		IntArrayTag tt = (IntArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testLongArrayTag() {
		LongArrayTag t = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertTrue(Arrays.equals(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE}, t.getValue()));
		assertEquals(12, t.getID());
		assertEquals("[L;-9223372036854775808l,0l,9223372036854775807l]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-9223372036854775808,0,9223372036854775807]}", t.toString());

		LongArrayTag t2 = new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertTrue(t.equals(t2));

		LongArrayTag t3 = new LongArrayTag(new long[]{Long.MAX_VALUE, 0, Long.MIN_VALUE});
		assertFalse(t.equals(t3));

		//test cloning
		LongArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{12, 0, 0, 0, 0, 0, 3, -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, -1, -1, -1, -1, -1, -1, -1}, data));
		LongArrayTag tt = (LongArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testListTag() {
		ListTag<ByteTag> bl = new ListTag<>();
		bl.add(new ByteTag(Byte.MIN_VALUE));
		bl.add(new ByteTag((byte) 0));
		bl.add(new ByteTag(Byte.MAX_VALUE));
		assertTrue(3 == bl.size());
		assertEquals(Byte.MIN_VALUE, bl.get(0).asByte());
		assertEquals(0, bl.get(1).asByte());
		assertEquals(Byte.MAX_VALUE, bl.get(2).asByte());
		assertEquals("[-128b,0b,127b]", bl.toTagString());
		assertEquals("{\"type\":\"ListTag\"," +
				"\"value\":{" +
				"\"type\":\"ByteTag\"," +
				"\"list\":[" +
				"{\"type\":\"ByteTag\",\"value\":-128}," +
				"{\"type\":\"ByteTag\",\"value\":0}," +
				"{\"type\":\"ByteTag\",\"value\":127}]}}", bl.toString());

		//test equality
		ListTag<ByteTag> bl2 = new ListTag<>();
		bl2.addByte(Byte.MIN_VALUE);
		bl2.addByte((byte) 0);
		bl2.addByte(Byte.MAX_VALUE);
		assertTrue(bl.equals(bl2));

		ListTag<ByteTag> bl3 = new ListTag<>();
		bl2.addByte(Byte.MAX_VALUE);
		bl2.addByte((byte) 0);
		bl2.addByte(Byte.MIN_VALUE);
		assertFalse(bl.equals(bl3));

		ListTag<ByteTag> bl4 = new ListTag<>();
		bl2.addByte(Byte.MIN_VALUE);
		bl2.addByte((byte) 0);
		assertFalse(bl.equals(bl4));

		//test cloning
		ListTag<ByteTag> tc = bl.clone();
		assertTrue(bl.equals(tc));
		assertFalse(bl == tc);
		assertFalse(invokeGetValue(bl) == invokeGetValue(tc));

		//test serialization / deserialization
		byte[] data = serialize(bl);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 1, 0, 0, 0, 3, -128, 0, 127}, data));
		ListTag<ByteTag> tt = ((ListTag<?>) deserialize(data)).asByteTagList();
		assertTrue(bl.equals(tt));

		//test casting and type consistency
		ListTag<ByteTag> b = new ListTag<>();
		b.addByte((byte) 123);
		assertThrowsException(() -> b.addShort((short) 123), IllegalArgumentException.class);
		assertThrowsException(b::asShortTagList, ClassCastException.class);
		assertEquals(ByteTag.class, b.getTypeClass());
		assertEquals(1, b.getTypeID());

		b.clear();
		assertThrowsNoException(() -> b.addShort((short) 123));
		assertThrowsException(() -> b.addByte((byte) 123), IllegalArgumentException.class);
		assertThrowsNoException(b::asShortTagList);
		assertThrowsException(b::asByteTagList, ClassCastException.class);
		assertThrowsNoException(() -> b.asTypedList(ShortTag.class));
		assertThrowsException(() -> b.asTypedList(ByteTag.class), ClassCastException.class);

		b.remove(0);
		assertEquals(EndTag.class, b.getTypeClass());
	}

	public void testCompoundTag() {
		CompoundTag ct = new CompoundTag();
		invokeSetValue(ct, new LinkedHashMap<>());
		ct.put("b", new ByteTag(Byte.MAX_VALUE));
		ct.put("str", new StringTag("foo"));
		ct.put("list", new ListTag<>());
		ct.getListTag("list").addByte((byte) 123);
		assertTrue(3 == ct.size());
		assertTrue(ct.containsKey("b"));
		assertTrue(ct.containsKey("str"));
		assertTrue(ct.containsKey("list"));
		assertFalse(ct.containsKey("invalid"));
		assertEquals("{b:127b,str:foo,list:[123b]}", ct.toTagString());
		assertEquals("{\"type\":\"CompoundTag\"," +
				"\"value\":{" +
				"\"b\":{\"type\":\"ByteTag\",\"value\":127}," +
				"\"str\":{\"type\":\"StringTag\",\"value\":\"foo\"}," +
				"\"list\":{\"type\":\"ListTag\"," +
				"\"value\":{\"type\":\"ByteTag\",\"list\":[{\"type\":\"ByteTag\",\"value\":123}]}}}}", ct.toString());

		//test equality
		CompoundTag ct2 = new CompoundTag();
		ct2.putByte("b", Byte.MAX_VALUE);
		ct2.putString("str", "foo");
		ct2.put("list", new ListTag<>());
		ct2.getListTag("list").addByte((byte) 123);
		assertTrue(ct.equals(ct2));

		ct2.getListTag("list").asByteTagList().get(0).setValue((byte) 124);
		assertFalse(ct.equals(ct2));

		//test cloning
		CompoundTag cl = ct.clone();
		assertTrue(ct.equals(cl));
		assertFalse(ct == cl);
		assertFalse(ct.get("list") == cl.get("list"));
		assertFalse(invokeGetValue(ct) == invokeGetValue(cl));

		//test serialization / deserialization
		byte[] data = serialize(ct);
		assertTrue(Arrays.equals(new byte[]{10, 0, 0, 1, 0, 1, 98, 127, 8, 0, 3, 115, 116, 114, 0, 3, 102, 111, 111, 9, 0, 4, 108, 105, 115, 116, 1, 0, 0, 0, 1, 123, 0}, data));
		CompoundTag tt = (CompoundTag) deserialize(data);
		assertTrue(ct.equals(tt));

	}

	//TODO: test compoundtag, custom tags, writing to file, reading from file, recursion

//	public void tearDown() throws Exception {
//		super.tearDown();
//		Files.deleteIfExists(Paths.get(TestUtil.RESOURCES_PATH + "test_NBTFileWriter.dat"));
//		TagType.unregisterAllCustomTags();
//	}


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
			ex.printStackTrace();
			fail(ex.getMessage());
			return null;
		}
	}

	private <T> void invokeSetValue(Tag<T> tag, T value) {
		try {
			Class<?> c = tag.getClass();
			Method m;
			while (c != Object.class) {
				try {
					m = c.getDeclaredMethod("setValue", Object.class);
					m.setAccessible(true);
					m.invoke(tag, value);
					return;
				} catch (NoSuchMethodException ex) {
					c = c.getSuperclass();
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			fail("unable to invoke setValue() on " + tag.getClass().getSimpleName());
		}
		fail("could not find setValue()");
	}

	@SuppressWarnings("unchecked")
	private <T> T invokeGetValue(Tag<T> tag) {
		try {
			Class<?> c = tag.getClass();
			Method m;
			while (c != Object.class) {
				try {
					m = c.getDeclaredMethod("getValue");
					m.setAccessible(true);
					return (T) m.invoke(tag);
				} catch (NoSuchMethodException ex) {
					c = c.getSuperclass();
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			fail("unable to invoke getValue() on " + tag.getClass().getSimpleName());
		}
		fail("could not find getValue()");
		return null;
	}
}
