package net.querz.nbt;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.Assert.assertNotEquals;

public class CompoundTagTest extends NBTTestCase {

	private CompoundTag createCompoundTag() {
		CompoundTag ct = new CompoundTag();
		invokeSetValue(ct, new LinkedHashMap<>());
		ct.put("b", new ByteTag(Byte.MAX_VALUE));
		ct.put("str", new StringTag("foo"));
		ct.put("list", new ListTag<>(ByteTag.class));
		ct.getListTag("list").addByte((byte) 123);
		return ct;
	}

	public void testStringConversion() {
		CompoundTag ct = createCompoundTag();
		assertEquals("{b:127b,str:foo,list:[123b]}", ct.toTagString());
		assertEquals("{\"type\":\"CompoundTag\"," +
				"\"value\":{" +
				"\"b\":{\"type\":\"ByteTag\",\"value\":127}," +
				"\"str\":{\"type\":\"StringTag\",\"value\":\"foo\"}," +
				"\"list\":{\"type\":\"ListTag\"," +
				"\"value\":{\"type\":\"ByteTag\",\"list\":[123]}}}}", ct.toString());
	}

	public void testEquals() {
		CompoundTag ct = createCompoundTag();
		CompoundTag ct2 = new CompoundTag();
		ct2.putByte("b", Byte.MAX_VALUE);
		ct2.putString("str", "foo");
		ct2.put("list", new ListTag<>(ByteTag.class));
		ct2.getListTag("list").addByte((byte) 123);
		assertTrue(ct.equals(ct2));

		ct2.getListTag("list").asByteTagList().get(0).setValue((byte) 124);
		assertFalse(ct.equals(ct2));

		ct2.remove("str");
		assertFalse(ct.equals(ct2));

		assertThrowsNoRuntimeException(() -> ct.equals("blah"));
		assertFalse(ct.equals("blah"));

		assertEquals(ct, ct);
	}

	public void testHashCode() {
		CompoundTag t = new CompoundTag();
		for (int i = 0; i < 256; i++) {
			t.putByte("key_byte" + i, (byte) i);
			t.putShort("key_short" + i, (short) i);
			t.putInt("key_int" + i, i);
			t.putLong("key_long" + i, i);
			t.putFloat("key_float" + i, i * 1.001f);
			t.putDouble("key_double" + i, i * 1.001);
			t.putString("key_string" + i, "value" + i);

			byte[] bArray = new byte[257];
			int[] iArray = new int[257];
			long[] lArray = new long[257];
			for (byte b = -128; b < 127; b++) {
				bArray[b + 128] = b;
				iArray[b + 128] = b;
				lArray[b + 128] = b;
			}
			bArray[256] = (byte) i;
			iArray[256] = i;
			lArray[256] = i;
			t.putByteArray("key_byte_array" + i, bArray);
			t.putIntArray("key_int_array" + i, iArray);
			t.putLongArray("key_long_array" + i, lArray);

			ListTag<StringTag> l = new ListTag<>(StringTag.class);
			for (int j = 0; j < 256; j++) {
				l.addString("value" + j);
			}
			l.addString("value" + i);
			t.put("key_list" + i, l);

			CompoundTag c = new CompoundTag();
			c.putString("key_string" + i, "value" + i);
			t.put("key_child" + i, c);
		}
		CompoundTag t2 = new CompoundTag();
		for (int i = 0; i < 256; i++) {
			t2.putString("key_string" + i, "value" + i);
			t2.putDouble("key_double" + i, i * 1.001);
			t2.putFloat("key_float" + i, i * 1.001f);
			t2.putLong("key_long" + i, i);
			t2.putInt("key_int" + i, i);
			t2.putShort("key_short" + i, (short) i);
			t2.putByte("key_byte" + i, (byte) i);

			byte[] bArray = new byte[257];
			int[] iArray = new int[257];
			long[] lArray = new long[257];
			for (byte b = -128; b < 127; b++) {
				bArray[b + 128] = b;
				iArray[b + 128] = b;
				lArray[b + 128] = b;
			}
			bArray[256] = (byte) i;
			iArray[256] = i;
			lArray[256] = i;
			t2.putByteArray("key_byte_array" + i, bArray);
			t2.putIntArray("key_int_array" + i, iArray);
			t2.putLongArray("key_long_array" + i, lArray);

			ListTag<StringTag> l = new ListTag<>(StringTag.class);
			for (int j = 0; j < 256; j++) {
				l.addString("value" + j);
			}
			l.addString("value" + i);
			t2.put("key_list" + i, l);

			CompoundTag c = new CompoundTag();
			c.putString("key_string" + i, "value" + i);
			t2.put("key_child" + i, c);
		}

		assertEquals(t, t2);
		assertEquals(t.hashCode(), t2.hashCode());

		t.getCompoundTag("key_child1").remove("key_string1");

		assertNotEquals(t, t2);
		assertNotEquals(t.hashCode(), t2.hashCode());
	}

	public void testClone() {
		CompoundTag ct = createCompoundTag();
		CompoundTag cl = ct.clone();
		assertTrue(ct.equals(cl));
		assertFalse(ct == cl);
		assertFalse(ct.get("list") == cl.get("list"));
		assertFalse(invokeGetValue(ct) == invokeGetValue(cl));
	}

	public void testClear() {
		CompoundTag cclear = new CompoundTag();
		cclear.putString("test", "blah");
		assertEquals(1, cclear.size());
		cclear.clear();
		assertEquals(0, cclear.size());
	}

	public void testSerializeDeserialize() {
		CompoundTag ct = createCompoundTag();
		byte[] data = serialize(ct);
		assertTrue(Arrays.equals(new byte[]{10, 0, 0, 1, 0, 1, 98, 127, 8, 0, 3, 115, 116, 114, 0, 3, 102, 111, 111, 9, 0, 4, 108, 105, 115, 116, 1, 0, 0, 0, 1, 123, 0}, data));
		CompoundTag tt = (CompoundTag) deserialize(data);
		assertTrue(ct.equals(tt));
	}

	public void testCasting() {
		CompoundTag cc = new CompoundTag();
		assertNull(cc.get("b", ByteTag.class));
		assertNull(cc.get("b"));
		cc.putByte("b", Byte.MAX_VALUE);
		assertEquals(new ByteTag(Byte.MAX_VALUE), cc.getByteTag("b"));
		assertNull(cc.getByteTag("bb"));
		assertEquals(Byte.MAX_VALUE, cc.get("b", ByteTag.class).asByte());
		assertThrowsRuntimeException(() -> cc.getShort("b"), ClassCastException.class);
		assertEquals(0, cc.getByte("bb"));
		assertEquals(true, cc.getBoolean("b"));
		cc.putByte("b2", (byte) 0);
		assertEquals(false, cc.getBoolean("b2"));
		cc.putBoolean("b3", false);
		assertEquals(0, cc.getByte("b3"));
		cc.putBoolean("b4", true);
		assertEquals(1, cc.getByte("b4"));

		cc.putShort("s", Short.MAX_VALUE);
		assertEquals(new ShortTag(Short.MAX_VALUE), cc.getShortTag("s"));
		assertNull(cc.getShortTag("ss"));
		assertEquals(Short.MAX_VALUE, cc.get("s", ShortTag.class).asShort());
		assertThrowsRuntimeException(() -> cc.getInt("s"), ClassCastException.class);
		assertEquals(0, cc.getShort("ss"));

		cc.putInt("i", Integer.MAX_VALUE);
		assertEquals(new IntTag(Integer.MAX_VALUE), cc.getIntTag("i"));
		assertNull(cc.getIntTag("ii"));
		assertEquals(Integer.MAX_VALUE, cc.get("i", IntTag.class).asInt());
		assertThrowsRuntimeException(() -> cc.getLong("i"), ClassCastException.class);
		assertEquals(0, cc.getInt("ii"));
		
		cc.putLong("l", Long.MAX_VALUE);
		assertEquals(new LongTag(Long.MAX_VALUE), cc.getLongTag("l"));
		assertNull(cc.getLongTag("ll"));
		assertEquals(Long.MAX_VALUE, cc.get("l", LongTag.class).asLong());
		assertThrowsRuntimeException(() -> cc.getFloat("l"), ClassCastException.class);
		assertEquals(0, cc.getLong("ll"));

		cc.putFloat("f", Float.MAX_VALUE);
		assertEquals(new FloatTag(Float.MAX_VALUE), cc.getFloatTag("f"));
		assertNull(cc.getFloatTag("ff"));
		assertEquals(Float.MAX_VALUE, cc.get("f", FloatTag.class).asFloat());
		assertThrowsRuntimeException(() -> cc.getDouble("f"), ClassCastException.class);
		assertEquals(0.0F, cc.getFloat("ff"));

		cc.putDouble("d", Double.MAX_VALUE);
		assertEquals(new DoubleTag(Double.MAX_VALUE), cc.getDoubleTag("d"));
		assertNull(cc.getDoubleTag("dd"));
		assertEquals(Double.MAX_VALUE, cc.get("d", DoubleTag.class).asDouble());
		assertThrowsRuntimeException(() -> cc.getString("d"), ClassCastException.class);
		assertEquals(0.0D, cc.getDouble("dd"));

		cc.putString("st", "foo");
		assertEquals(new StringTag("foo"), cc.getStringTag("st"));
		assertNull(cc.getStringTag("stst"));
		assertEquals("foo", cc.get("st", StringTag.class).getValue());
		assertThrowsRuntimeException(() -> cc.getByteArray("st"), ClassCastException.class);
		assertEquals("", cc.getString("stst"));

		cc.putByteArray("ba", new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertEquals(new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}), cc.getByteArrayTag("ba"));
		assertNull(cc.getByteArrayTag("baba"));
		assertTrue(Arrays.equals(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, cc.get("ba", ByteArrayTag.class).getValue()));
		assertThrowsRuntimeException(() -> cc.getIntArray("ba"), ClassCastException.class);
		assertTrue(Arrays.equals(new byte[0], cc.getByteArray("baba")));

		cc.putIntArray("ia", new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertEquals(new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}), cc.getIntArrayTag("ia"));
		assertNull(cc.getIntArrayTag("iaia"));
		assertTrue(Arrays.equals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, cc.get("ia", IntArrayTag.class).getValue()));
		assertThrowsRuntimeException(() -> cc.getLongArray("ia"), ClassCastException.class);
		assertTrue(Arrays.equals(new int[0], cc.getIntArray("iaia")));

		cc.putLongArray("la", new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertEquals(new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE}), cc.getLongArrayTag("la"));
		assertNull(cc.getLongArrayTag("lala"));
		assertTrue(Arrays.equals(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE}, cc.get("la", LongArrayTag.class).getValue()));
		assertThrowsRuntimeException(() -> cc.getListTag("la"), ClassCastException.class);
		assertTrue(Arrays.equals(new long[0], cc.getLongArray("lala")));

		cc.put("li", new ListTag<>(IntTag.class));
		assertEquals(new ListTag<>(IntTag.class), cc.getListTag("li"));
		assertNull(cc.getListTag("lili"));
		assertThrowsRuntimeException(() -> cc.getCompoundTag("li"), ClassCastException.class);

		cc.put("co", new CompoundTag());
		assertEquals(new CompoundTag(), cc.getCompoundTag("co"));
		assertNull(cc.getCompoundTag("coco"));
		assertThrowsRuntimeException(() -> cc.getByte("co"), ClassCastException.class);
	}

	public void testCompareTo() {
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
		assertThrowsRuntimeException(() -> ci.compareTo(null), NullPointerException.class);
	}

	public void testMaxDepth() {
		CompoundTag root = new CompoundTag();
		CompoundTag rec = root;
		for (int i = 0; i < Tag.DEFAULT_MAX_DEPTH + 1; i++) {
			CompoundTag c = new CompoundTag();
			rec.put("c" + i, c);
			rec = c;
		}
		assertThrowsRuntimeException(() -> serialize(root), MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> deserializeFromFile("max_depth_reached.dat"), MaxDepthReachedException.class);
		assertThrowsNoRuntimeException(() -> root.toString(Tag.DEFAULT_MAX_DEPTH + 1));
		assertThrowsRuntimeException(root::toString, MaxDepthReachedException.class);
		assertThrowsNoRuntimeException(() -> root.toTagString(Tag.DEFAULT_MAX_DEPTH + 1));
		assertThrowsRuntimeException(root::toTagString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(() -> root.valueToString(-1), IllegalArgumentException.class);
		assertThrowsRuntimeException(() -> root.valueToTagString(-1), IllegalArgumentException.class);
	}

	public void testRecursion() {
		CompoundTag recursive = new CompoundTag();
		recursive.put("recursive", recursive);
		assertThrowsRuntimeException(() -> serialize(recursive), MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toTagString, MaxDepthReachedException.class);
	}

	public void testEntrySet() {
		CompoundTag e = new CompoundTag();
		e.putInt("int", 123);
		for (Map.Entry<String, Tag<?>> en : e.entrySet()) {
			assertThrowsRuntimeException(() -> en.setValue(null), NullPointerException.class);
			assertThrowsNoRuntimeException(() -> en.setValue(new IntTag(321)));
		}
		assertEquals(1, e.size());
		assertEquals(321, e.getInt("int"));
	}

	public void testContains() {
		CompoundTag ct = createCompoundTag();
		assertTrue(3 == ct.size());
		assertTrue(ct.containsKey("b"));
		assertTrue(ct.containsKey("str"));
		assertTrue(ct.containsKey("list"));
		assertFalse(ct.containsKey("invalid"));
		assertTrue(ct.containsValue(new StringTag("foo")));
		ListTag<ByteTag> l = new ListTag<>(ByteTag.class);
		l.addByte((byte) 123);
		assertTrue(ct.containsValue(l));
		assertTrue(ct.containsValue(new ByteTag(Byte.MAX_VALUE)));
		assertFalse(ct.containsValue(new ByteTag(Byte.MIN_VALUE)));
		assertFalse(ct.containsKey("blah"));
	}

	public void testIterator() {
		CompoundTag ct = createCompoundTag();
		for (Tag<?> t : ct.values()) {
			assertNotNull(t);
		}
		ct.values().remove(new StringTag("foo"));
		assertFalse(ct.containsKey("str"));
		assertThrowsRuntimeException(() -> ct.values().add(new StringTag("foo")), UnsupportedOperationException.class);
		ct.putString("str", "foo");
		for (String k : ct.keySet()) {
			assertNotNull(k);
			assertTrue(ct.containsKey(k));
		}
		ct.keySet().remove("str");
		assertFalse(ct.containsKey("str"));
		assertThrowsRuntimeException(() -> ct.keySet().add("str"), UnsupportedOperationException.class);
		ct.putString("str", "foo");
		for (Map.Entry<String, Tag<?>> e : ct.entrySet()) {
			assertNotNull(e.getKey());
			assertNotNull(e.getValue());
			assertThrowsRuntimeException(() -> e.setValue(null), NullPointerException.class);
			if (e.getKey().equals("str")) {
				assertThrowsNoRuntimeException(() -> e.setValue(new StringTag("bar")));
			}
		}
		assertTrue(ct.containsKey("str"));
		assertEquals("bar", ct.getString("str"));
		for (Map.Entry<String, Tag<?>> e : ct) {
			assertNotNull(e.getKey());
			assertNotNull(e.getValue());
			assertThrowsRuntimeException(() -> e.setValue(null), NullPointerException.class);
			if (e.getKey().equals("str")) {
				assertThrowsNoRuntimeException(() -> e.setValue(new StringTag("foo")));
			}
		}
		assertTrue(ct.containsKey("str"));
		assertEquals("foo", ct.getString("str"));
		ct.forEach((k, v) -> {
			assertNotNull(k);
			assertNotNull(v);
		});
		assertEquals(3, ct.size());
	}
}
