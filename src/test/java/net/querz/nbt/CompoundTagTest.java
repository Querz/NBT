package net.querz.nbt;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompoundTagTest extends NBTTestCase {

	private CompoundTag createCompoundTag() {
		CompoundTag ct = new CompoundTag();
		invokeSetValue(ct, new LinkedHashMap<>());
		ct.put("b", new ByteTag(Byte.MAX_VALUE));
		ct.put("str", new StringTag("foo"));
		ct.put("list", new ListTag<>());
		ct.getListTag("list").addByte((byte) 123);
		return ct;
	}

	public void testStringConversion() {
		CompoundTag ct = createCompoundTag();
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
	}

	public void testEquals() {
		CompoundTag ct = createCompoundTag();
		CompoundTag ct2 = new CompoundTag();
		ct2.putByte("b", Byte.MAX_VALUE);
		ct2.putString("str", "foo");
		ct2.put("list", new ListTag<>());
		ct2.getListTag("list").addByte((byte) 123);
		assertTrue(ct.equals(ct2));

		ct2.getListTag("list").asByteTagList().get(0).setValue((byte) 124);
		assertFalse(ct.equals(ct2));
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
		cc.putInt("one", 1);
		assertThrowsRuntimeException(() -> cc.getLong("one"), ClassCastException.class);
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
	}

	public void testMaxDepth() {
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
		for (Map.Entry<String, Tag> en : e.entrySet()) {
			assertThrowsRuntimeException(() -> en.setValue(null), NullPointerException.class);
			assertThrowsNoRuntimeException(() -> en.setValue(new IntTag(321)));
		}
		assertEquals(1, e.size());
		assertEquals(321, e.getInt("int"));
	}

}
