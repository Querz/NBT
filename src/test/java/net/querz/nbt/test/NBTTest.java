package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.*;
import net.querz.nbt.custom.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import static net.querz.nbt.test.TestUtil.*;

public class NBTTest extends TestCase {

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
		assertThrowsRuntimeException(() -> new StringTag().setValue(null), NullPointerException.class);
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
		ListTag<?> tt = (ListTag<?>) deserialize(data);
		assertNotNull(tt);
		ListTag<ByteTag> ttt = tt.asByteTagList();
		assertTrue(bl.equals(ttt));

		//test serialization / deserialization of empty list
		ListTag<IntTag> empty = new ListTag<>();
		data = serialize(empty);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 0, 0, 0, 0, 0}, data));
		ListTag<?> et = (ListTag<?>) deserialize(data);
		assertNotNull(et);
		ListTag<ByteTag> ett = et.asByteTagList();
		assertTrue(empty.equals(ett));

		//test casting and type consistency
		ListTag<ByteTag> b = new ListTag<>();
		b.addByte((byte) 123);
		assertThrowsRuntimeException(() -> b.addShort((short) 123), IllegalArgumentException.class);
		assertThrowsRuntimeException(b::asShortTagList, ClassCastException.class);
		assertEquals(ByteTag.class, b.getTypeClass());
		assertEquals(1, b.getTypeID());

		b.clear();
		assertThrowsNoRuntimeException(() -> b.addShort((short) 123));
		assertThrowsRuntimeException(() -> b.addByte((byte) 123), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(b::asShortTagList);
		assertThrowsRuntimeException(b::asByteTagList, ClassCastException.class);
		assertThrowsNoRuntimeException(() -> b.asTypedList(ShortTag.class));
		assertThrowsRuntimeException(() -> b.asTypedList(ByteTag.class), ClassCastException.class);

		b.remove(0);
		assertEquals(EndTag.class, b.getTypeClass());

		//test compareTo
		ListTag<IntTag> li = new ListTag<>();
		li.addInt(1);
		li.addInt(2);
		ListTag<IntTag> lo = new ListTag<>();
		lo.addInt(3);
		lo.addInt(4);
		assertEquals(0, li.compareTo(lo));
		lo.addInt(5);
		assertEquals(-1, li.compareTo(lo));
		lo.remove(2);
		lo.remove(1);
		assertEquals(1, li.compareTo(lo));

		//test max depth
		ListTag<ListTag<?>> root = new ListTag<>();
		ListTag<ListTag<?>> rec = root;
		for (int i = 0; i < Tag.MAX_DEPTH + 1; i++) {
			ListTag<ListTag<?>> l = new ListTag<>();
			rec.add(l);
			rec = l;
		}
		assertThrowsRuntimeException(() -> serialize(root), MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> deserializeFromFile("max_depth_reached.dat"), MaxDepthReachedException.class);
		assertThrowsRuntimeException(root::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(root::toTagString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> root.valueToString(-1), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> root.valueToTagString(-1), IllegalArgumentException.class);

		//test recursion
		ListTag<ListTag<?>> recursive = new ListTag<>();
		recursive.add(recursive);
		assertThrowsRuntimeException(() -> serialize(recursive), MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toTagString, MaxDepthReachedException.class);
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

		//casting
		CompoundTag cc = new CompoundTag();
		cc.putInt("one", 1);
		assertThrowsRuntimeException(() -> cc.getLong("one"), ClassCastException.class);

		//test compareTo
		CompoundTag ci = new CompoundTag();
		ci.putInt("one", 1);
		ci.putInt("two", 2);
		CompoundTag co = new CompoundTag();
		co.putInt("three", 3);
		co.putInt("four", 4);
		assertEquals(0, ci.compareTo(co));
		co.putInt("five", 5);
		assertEquals(-1, ci.compareTo(co));
		co.remove("five");
		co.remove("four");
		assertEquals(1, ci.compareTo(co));

		//test max depth
		CompoundTag root = new CompoundTag();
		CompoundTag rec = root;
		for (int i = 0; i < Tag.MAX_DEPTH + 1; i++) {
			CompoundTag c = new CompoundTag();
			rec.put("c" + i, c);
			rec = c;
		}
		assertThrowsRuntimeException(() -> serialize(root), MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> deserializeFromFile("max_depth_reached.dat"), MaxDepthReachedException.class);
		assertThrowsRuntimeException(root::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(root::toTagString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> root.valueToString(-1), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> root.valueToTagString(-1), IllegalArgumentException.class);

		//test recursion
		CompoundTag recursive = new CompoundTag();
		recursive.put("recursive", recursive);
		assertThrowsRuntimeException(() -> serialize(recursive), MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toTagString, MaxDepthReachedException.class);

		//test entrySet setValue
		CompoundTag e = new CompoundTag();
		e.putInt("int", 123);
		for (Map.Entry<String, Tag> en : e.entrySet()) {
			assertThrowsRuntimeException(() -> en.setValue(null), NullPointerException.class);
			assertThrowsNoRuntimeException(() -> en.setValue(new IntTag(321)));
		}
		assertEquals(1, e.size());
		assertEquals(321, e.getInt("int"));
	}

	public void testCustomCharTag() {
		CharTag.register();
		CharTag t = new CharTag('a');
		assertEquals('a', (char) t.getValue());
		assertEquals(110, t.getID());
		assertEquals("a", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":\"a\"}", t.toString());

		CharTag t2 = new CharTag('a');
		assertTrue(t.equals(t2));

		CharTag t3 = new CharTag('b');
		assertFalse(t.equals(t3));

		//test cloning
		CharTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{110, 0, 0, 0, 97}, data));
		CharTag tt = (CharTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCustomShortArrayTag() {
		ShortArrayTag.register();
		ShortArrayTag t = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		assertTrue(Arrays.equals(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE}, t.getValue()));
		assertEquals(100, t.getID());
		assertEquals("[S;-32768s,0s,32767s]", t.toTagString());
		assertEquals("{\"type\":\"" + t.getClass().getSimpleName() + "\",\"value\":[-32768,0,32767]}", t.toString());

		ShortArrayTag t2 = new ShortArrayTag(new short[]{Short.MIN_VALUE, 0, Short.MAX_VALUE});
		assertTrue(t.equals(t2));

		ShortArrayTag t3 = new ShortArrayTag(new short[]{Short.MAX_VALUE, 0, Short.MIN_VALUE});
		assertFalse(t.equals(t3));

		//test cloning
		ShortArrayTag tc = t.clone();
		assertTrue(t.equals(tc));
		assertFalse(t == tc);
		assertFalse(t.getValue() == tc.getValue());

		//test serialization
		byte[] data = serialize(t);
		assertTrue(Arrays.equals(new byte[]{100, 0, 0, 0, 0, 0, 3, -128, 0, 0, 0, 127, -1}, data));
		ShortArrayTag tt = (ShortArrayTag) deserialize(data);
		assertTrue(t.equals(tt));
	}

	public void testCustomObjectTag() {
		ObjectTag.register();
		DummyObject d = new DummyObject();
		ObjectTag<DummyObject> o = new ObjectTag<>(d);
		assertEquals(90, o.getID());
		assertEquals("{\"type\":\"ObjectTag\",\"value\":\"" + d + "\"}", o.toString());
		assertEquals("\"" + d + "\"", o.toTagString());

		//test equality
		ObjectTag<DummyObject> o2 = new ObjectTag<>(d);
		assertTrue(o.equals(o2));

		ObjectTag<DummyObject> o3 = new ObjectTag<>(new DummyObject());
		assertFalse(o.equals(o3));

		ObjectTag<DummyObject> o4 = new ObjectTag<>(d.clone());
		assertTrue(o.equals(o4));

		//test cloning
		ObjectTag<DummyObject> c = o.clone();
		assertTrue(o.equals(c));
		assertFalse(o == c);
		assertFalse(o.getValue() == c.getValue());

		ObjectTag<String> s = new ObjectTag<>("string");
		ObjectTag<String> cs = s.clone();
		assertTrue(s.equals(cs));
		assertFalse(s == cs);
		//String is immutable and not cloneable, so it still has the same reference
		//noinspection StringEquality
		assertTrue(s.getValue() == cs.getValue());

		//test serialization
		byte[] data = serialize(o);
		ObjectTag<?> oo = ((ObjectTag<?>) deserialize(data));
		assertNotNull(oo);
		assertThrowsNoRuntimeException(() -> oo.asTypedObjectTag(AbstractDummyObject.class));
		assertThrowsRuntimeException(() -> oo.asTypedObjectTag(String.class), ClassCastException.class);
		ObjectTag<AbstractDummyObject> ooo = oo.asTypedObjectTag(AbstractDummyObject.class);
		assertTrue(o.equals(ooo));

		//test null value
		ObjectTag<DummyObject> n = new ObjectTag<>();
		assertNull(n.getValue());
		assertEquals("{\"type\":\"ObjectTag\",\"value\":null}", n.toString());
		assertEquals("null", n.toTagString());

		ObjectTag<DummyObject> n2 = new ObjectTag<>(null);
		assertTrue(n.equals(n2));

		//null is equals to null, no matter the type
		ObjectTag<String> n3 = new ObjectTag<>(null);
		assertTrue(n.equals(n3));

		ObjectTag<DummyObject> nc = n.clone();
		assertTrue(n.equals(nc));
		assertFalse(n == nc);
		assertTrue(n.getValue() == nc.getValue());

		data = serialize(n);
		ObjectTag<?> nn = ((ObjectTag<?>) deserialize(data));
		assertNotNull(nn);
		ObjectTag<AbstractDummyObject> nnn = nn.asTypedObjectTag(AbstractDummyObject.class);
		assertTrue(n.equals(nnn));
	}

	public void testCustomStructTag() {
		StructTag.register();
		StructTag s = new StructTag();
		s.add(new ByteTag(Byte.MAX_VALUE));
		s.add(new IntTag(Integer.MAX_VALUE));
		assertEquals(120, s.getID());
		assertEquals("[127b,2147483647]", s.toTagString());
		assertEquals("{\"type\":\"StructTag\",\"value\":[{\"type\":\"ByteTag\",\"value\":127},{\"type\":\"IntTag\",\"value\":2147483647}]}", s.toString());

		//test equality
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

		//test cloning
		StructTag c = s.clone();
		assertTrue(s.equals(c));
		assertFalse(s == c);
		assertFalse(invokeGetValue(s) == invokeGetValue(c));

		//test serialization
		byte[] data = serialize(s);
		assertTrue(Arrays.equals(new byte[]{120, 0, 0, 0, 0, 0, 2, 1, 127, 3, 127, -1, -1, -1}, data));
		StructTag ss = (StructTag) deserialize(data);
		assertTrue(s.equals(ss));
	}

	public void testWriteReadTag() {
		CompoundTag t = new CompoundTag();
		invokeSetValue(t, new LinkedHashMap<>());
		t.putByte("byte", Byte.MAX_VALUE);
		t.putShort("short", Short.MAX_VALUE);
		File file = getNewTmpFile("compressed.dat");
		try {
			NBTUtil.writeTag(t, "name", file, true);
		} catch (IOException ex) {
			fail(ex.getMessage());
		}

		assertEquals("E8F7B55F81FADB8A5657461D9188DE73", calculateFileMD5(file));

		try {
			CompoundTag tt = (CompoundTag) NBTUtil.readTag(file);
			assertTrue(t.equals(tt));
		} catch (IOException ex) {
			fail(ex.getMessage());
		}
	}

	public void tearDown() throws Exception {
		super.tearDown();
		TagFactory.unregisterCustomTag(90);
		TagFactory.unregisterCustomTag(100);
		TagFactory.unregisterCustomTag(110);
		TagFactory.unregisterCustomTag(120);
		cleanupTmpDir();
	}


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

	private Tag deserializeFromFile(String f) {
		URL resource = getClass().getClassLoader().getResource(f);
		assertNotNull(resource);
		File file = new File(resource.getFile());
		try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
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
