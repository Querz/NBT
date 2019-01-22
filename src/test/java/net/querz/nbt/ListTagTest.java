package net.querz.nbt;

import junit.framework.TestCase;
import java.util.Arrays;
import java.util.Comparator;
import static org.junit.Assert.assertNotEquals;

public class ListTagTest extends NBTTestCase {

	public void testCreateInvalidList() {
		assertThrowsException(() -> new ListTag<>(EndTag.class), IllegalArgumentException.class);
		assertThrowsException(() -> new ListTag<>(null), NullPointerException.class);
	}
	
	private ListTag<ByteTag> createListTag() {
		ListTag<ByteTag> bl = new ListTag<>(ByteTag.class);
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
				"-128," +
				"0," +
				"127]}}", bl.toString());
		ListTag<?> lu = ListTag.createUnchecked();
		assertEquals("{\"type\":\"ListTag\",\"value\":{\"type\":\"EndTag\",\"list\":[]}}", lu.toString());
	}

	public void testEquals() {
		ListTag<ByteTag> bl = createListTag();

		ListTag<ByteTag> bl2 = new ListTag<>(ByteTag.class);
		bl2.addByte(Byte.MIN_VALUE);
		bl2.addByte((byte) 0);
		bl2.addByte(Byte.MAX_VALUE);
		assertTrue(bl.equals(bl2));

		ListTag<ByteTag> bl3 = new ListTag<>(ByteTag.class);
		bl2.addByte(Byte.MAX_VALUE);
		bl2.addByte((byte) 0);
		bl2.addByte(Byte.MIN_VALUE);
		assertFalse(bl.equals(bl3));

		ListTag<ByteTag> bl4 = new ListTag<>(ByteTag.class);
		bl2.addByte(Byte.MIN_VALUE);
		bl2.addByte((byte) 0);
		assertFalse(bl.equals(bl4));

		assertEquals(bl, bl);
		
		ListTag<IntTag> il = new ListTag<>(IntTag.class);
		il.addInt(1);
		il.clear();
		assertEquals(il, new ListTag<>(IntTag.class));

		// test empty untyped list
		ListTag<?> lu = ListTag.createUnchecked();
		ListTag<?> lu2 = ListTag.createUnchecked();
		assertTrue(lu.equals(lu2));
		lu2.asIntTagList();
		assertFalse(lu.equals(lu2));
		ListTag<IntTag> lie = new ListTag<>(IntTag.class);
		assertFalse(lu.equals(lie));
		lu.asIntTagList();
		assertTrue(lie.equals(lu));
	}

	public void testHashCode() {
		ListTag<StringTag> ls = new ListTag<>(StringTag.class);
		ListTag<IntTag> li = new ListTag<>(IntTag.class);
		assertNotEquals(ls.hashCode(), li.hashCode());
		ListTag<StringTag> ls2 = new ListTag<>(StringTag.class);
		assertEquals(ls.hashCode(), ls2.hashCode());
	}

	public void testClone() {
		ListTag<IntTag> i = new ListTag<>(IntTag.class);
		ListTag<IntTag> c = i.clone();
		assertThrowsRuntimeException(() -> c.addString("wrong type in clone"), IllegalArgumentException.class);
		c.addInt(123);
		assertFalse(i.equals(c));
		c.clear();
		assertTrue(i.equals(c));
		assertFalse(invokeGetValue(i) == invokeGetValue(c));

		i.addInt(123);
		ListTag<IntTag> c2 = i.clone();
		assertTrue(i.equals(c2));
		assertFalse(invokeGetValue(i) == invokeGetValue(c2));
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
		ListTag<IntTag> empty = new ListTag<>(IntTag.class);
		byte[] data = serialize(empty);
		assertTrue(Arrays.equals(new byte[]{9, 0, 0, 3, 0, 0, 0, 0}, data));
		ListTag<?> et = (ListTag<?>) deserialize(data);
		assertNotNull(et);
		assertThrowsRuntimeException(et::asByteTagList, ClassCastException.class);
	}

	public void testCasting() {
		ListTag<ByteTag> b = new ListTag<>(ByteTag.class);
		assertThrowsRuntimeException(() -> b.addShort((short) 123), IllegalArgumentException.class);
		assertThrowsNoRuntimeException(() -> b.addByte((byte) 123));
		assertThrowsNoRuntimeException(b::asByteTagList);
		assertThrowsRuntimeException(b::asShortTagList, ClassCastException.class);
		assertThrowsNoRuntimeException(() -> b.asTypedList(ByteTag.class));
		assertThrowsRuntimeException(() -> b.asTypedList(ShortTag.class), ClassCastException.class);
		b.remove(0);

		assertEquals(ByteTag.class, b.getTypeClass());
		assertThrowsRuntimeException(() -> b.addShort((short) 1), IllegalArgumentException.class);
		assertEquals(ByteTag.class, b.getTypeClass());
		b.clear();
		assertEquals(ByteTag.class, b.getTypeClass());

		//adjust ListTag type during deserialization
		ListTag<?> l = ListTag.createUnchecked();
		assertThrowsNoRuntimeException(l::asByteTagList);
		l.addByte(Byte.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asByteTagList);
		assertThrowsRuntimeException(l::asShortTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addShort(Short.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asShortTagList);
		assertThrowsRuntimeException(l::asIntTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addInt(Integer.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asIntTagList);
		assertThrowsRuntimeException(l::asLongTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addLong(Long.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asLongTagList);
		assertThrowsRuntimeException(l::asFloatTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addFloat(Float.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asFloatTagList);
		assertThrowsRuntimeException(l::asDoubleTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addDouble(Double.MAX_VALUE);
		assertThrowsNoRuntimeException(l::asDoubleTagList);
		assertThrowsRuntimeException(l::asStringTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addString("foo");
		assertThrowsNoRuntimeException(l::asStringTagList);
		assertThrowsRuntimeException(l::asByteArrayTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addByteArray(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertThrowsNoRuntimeException(l::asByteArrayTagList);
		assertThrowsRuntimeException(l::asIntArrayTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addIntArray(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertThrowsNoRuntimeException(l::asIntArrayTagList);
		assertThrowsRuntimeException(l::asLongArrayTagList, ClassCastException.class);

		l = ListTag.createUnchecked();
		l.addLongArray(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertThrowsNoRuntimeException(l::asLongArrayTagList);
		assertThrowsRuntimeException(l::asListTagList, ClassCastException.class);

		ListTag<ListTag<?>> lis = new ListTag<>(ListTag.class);
		lis.add(new ListTag<>(IntTag.class));
		assertThrowsNoRuntimeException(lis::asListTagList);
		assertThrowsRuntimeException(lis::asCompoundTagList, ClassCastException.class);

		ListTag<CompoundTag> lco = new ListTag<>(CompoundTag.class);
		lco.add(new CompoundTag());
		assertThrowsNoRuntimeException(lco::asCompoundTagList);
		assertThrowsRuntimeException(lco::asByteTagList, ClassCastException.class);

		ListTag<?> lg = ListTag.createUnchecked();
		ListTag<ByteTag> lb = assertThrowsNoRuntimeException(lg::asByteTagList);
		assertEquals(lb, lg);
		//only allow casting once from untyped list to typed list
		assertThrowsRuntimeException(lg::asShortTagList, ClassCastException.class);
	}

	public void testCompareTo() {
		ListTag<IntTag> li = new ListTag<>(IntTag.class);
		li.addInt(1);
		li.addInt(2);
		ListTag<IntTag> lo = new ListTag<>(IntTag.class);
		lo.addInt(3);
		lo.addInt(4);
		assertEquals(0, li.compareTo(lo));
		lo.addInt(5);
		assertEquals(-1, li.compareTo(lo));
		lo.remove(2);
		lo.remove(1);
		assertEquals(1, li.compareTo(lo));
		assertThrowsRuntimeException(() -> li.compareTo(null), NullPointerException.class);
	}

	public void testMaxDepth() {
		ListTag<ListTag<?>> root = new ListTag<>(ListTag.class);
		ListTag<ListTag<?>> rec = root;
		for (int i = 0; i < Tag.DEFAULT_MAX_DEPTH + 1; i++) {
			ListTag<ListTag<?>> l = new ListTag<>(ListTag.class);
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
		ListTag<ListTag<?>> recursive = new ListTag<>(ListTag.class);
		recursive.add(recursive);
		assertThrowsRuntimeException(() -> serialize(recursive), MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toString, MaxDepthReachedException.class);
		assertThrowsRuntimeException(recursive::toTagString, MaxDepthReachedException.class);
	}

	public void testContains() {
		ListTag<IntTag> l = new ListTag<>(IntTag.class);
		l.addInt(1);
		l.addInt(2);
		assertTrue(l.contains(new IntTag(1)));
		assertFalse(l.contains(new IntTag(3)));
		assertTrue(l.containsAll(Arrays.asList(new IntTag(1), new IntTag(2))));
		assertFalse(l.containsAll(Arrays.asList(new IntTag(1), new IntTag(3))));
	}

	public void testSort() {
		ListTag<IntTag> l = new ListTag<>(IntTag.class);
		l.addInt(2);
		l.addInt(1);
		l.sort(Comparator.comparingInt(NumberTag::asInt));
		assertEquals(1, l.get(0).asInt());
		assertEquals(2, l.get(1).asInt());
	}

	public void testIterator() {
		ListTag<IntTag> l = new ListTag<>(IntTag.class);
		l.addInt(1);
		l.addInt(2);
		for (IntTag i : l) {
			assertNotNull(i);
		}
		l.forEach(TestCase::assertNotNull);
	}

	public void testSet() {
		ListTag<ByteTag> l = createListTag();
		l.set(1, new ByteTag((byte) 5));
		assertEquals(3, l.size());
		assertEquals(5, l.get(1).asByte());
		assertThrowsRuntimeException(() -> l.set(0, null), NullPointerException.class);
	}

	public void testAddAll() {
		ListTag<ByteTag> l = createListTag();
		l.addAll(Arrays.asList(new ByteTag((byte) 5), new ByteTag((byte) 7)));
		assertEquals(5, l.size());
		assertEquals(5, l.get(3).asByte());
		assertEquals(7, l.get(4).asByte());
		l.addAll(1, Arrays.asList(new ByteTag((byte) 9), new ByteTag((byte) 11)));
		assertEquals(7, l.size());
		assertEquals(9, l.get(1).asByte());
		assertEquals(11, l.get(2).asByte());
	}

	public void testIndexOf() {
		ListTag<ByteTag> l = createListTag();
		assertEquals(0, l.indexOf(new ByteTag(Byte.MIN_VALUE)));
		assertEquals(1, l.indexOf(new ByteTag((byte) 0)));
		assertEquals(2, l.indexOf(new ByteTag(Byte.MAX_VALUE)));
		l.addByte((byte) 0);
		assertEquals(1, l.indexOf(new ByteTag((byte) 0)));
	}

	public void testAdd() {
		ListTag<ByteTag> l = new ListTag<>(ByteTag.class);
		l.addBoolean(true);
		assertThrowsRuntimeException(() -> l.addShort((short) 5), IllegalArgumentException.class);
		assertEquals(1, l.size());
		assertEquals(1, l.get(0).asByte());
		l.addByte(Byte.MAX_VALUE);
		assertEquals(2, l.size());
		assertEquals(Byte.MAX_VALUE, l.get(1).asByte());
		ListTag<ShortTag> s = new ListTag<>(ShortTag.class);
		s.addShort(Short.MAX_VALUE);
		assertEquals(1, s.size());
		assertEquals(Short.MAX_VALUE, s.get(0).asShort());
		ListTag<IntTag> i = new ListTag<>(IntTag.class);
		i.addInt(Integer.MAX_VALUE);
		assertEquals(1, i.size());
		assertEquals(Integer.MAX_VALUE, i.get(0).asInt());
		ListTag<LongTag> lo = new ListTag<>(LongTag.class);
		lo.addLong(Long.MAX_VALUE);
		assertEquals(1, lo.size());
		assertEquals(Long.MAX_VALUE, lo.get(0).asLong());
		ListTag<FloatTag> f = new ListTag<>(FloatTag.class);
		f.addFloat(Float.MAX_VALUE);
		assertEquals(1, f.size());
		assertEquals(Float.MAX_VALUE, f.get(0).asFloat());
		ListTag<DoubleTag> d = new ListTag<>(DoubleTag.class);
		d.addDouble(Double.MAX_VALUE);
		assertEquals(1, d.size());
		assertEquals(Double.MAX_VALUE, d.get(0).asDouble());
		ListTag<StringTag> st = new ListTag<>(StringTag.class);
		st.addString("foo");
		assertEquals(1, st.size());
		assertEquals("foo", st.get(0).getValue());
		ListTag<ByteArrayTag> ba = new ListTag<>(ByteArrayTag.class);
		ba.addByteArray(new byte[] {Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertEquals(1, ba.size());
		assertTrue(Arrays.equals(new byte[] {Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, ba.get(0).getValue()));
		ListTag<IntArrayTag> ia = new ListTag<>(IntArrayTag.class);
		ia.addIntArray(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertEquals(1, ia.size());
		assertTrue(Arrays.equals(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, ia.get(0).getValue()));
		ListTag<LongArrayTag> la = new ListTag<>(LongArrayTag.class);
		la.addLongArray(new long[] {Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertEquals(1, la.size());
		assertTrue(Arrays.equals(new long[] {Long.MIN_VALUE, 0, Long.MAX_VALUE}, la.get(0).getValue()));
	}
}
