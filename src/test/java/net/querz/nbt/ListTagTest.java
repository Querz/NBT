package net.querz.nbt;

import java.util.Arrays;

public class ListTagTest extends NBTTestCase {

	private ListTag<ByteTag> createListTag() {
		ListTag<ByteTag> bl = new ListTag<>();
		bl.add(new ByteTag(Byte.MIN_VALUE));
		bl.add(new ByteTag((byte) 0));
		bl.add(new ByteTag(Byte.MAX_VALUE));
		return bl;
	}

	public void testStringConversion() {
		ListTag<ByteTag> bl = createListTag();
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
	}

	public void testEquals() {
		ListTag<ByteTag> bl = createListTag();

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
	}

	public void testClone() {
		ListTag<ByteTag> bl = createListTag();

		ListTag<ByteTag> tc = bl.clone();
		assertTrue(bl.equals(tc));
		assertFalse(bl == tc);
		assertFalse(invokeGetValue(bl) == invokeGetValue(tc));
	}

	public void testSerializeDeserialize() {
		ListTag<ByteTag> bl = createListTag();
		byte[] data = serialize(bl);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 1, 0, 0, 0, 3, -128, 0, 127}, data));
		ListTag<?> tt = (ListTag<?>) deserialize(data);
		assertNotNull(tt);
		ListTag<ByteTag> ttt = tt.asByteTagList();
		assertTrue(bl.equals(ttt));
	}

	public void testSerializeDeserializeEmptyList() {
		ListTag<IntTag> empty = new ListTag<>();
		byte[] data = serialize(empty);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 0, 0, 0, 0, 0}, data));
		ListTag<?> et = (ListTag<?>) deserialize(data);
		assertNotNull(et);
		ListTag<ByteTag> ett = et.asByteTagList();
		assertTrue(empty.equals(ett));
	}

	public void testCasting() {
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
	}

	public void testCompareTo() {
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
	}

	public void testMaxDepth() {
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
	}

	public void testRecursion() {
		ListTag<ListTag<?>> recursive = new ListTag<>();
		recursive.add(recursive);
		assertThrowsRuntimeException(() -> serialize(recursive), MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toTagString, MaxDepthReachedException.class);
	}
}
