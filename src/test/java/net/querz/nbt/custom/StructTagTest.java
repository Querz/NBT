package net.querz.nbt.custom;

import junit.framework.TestCase;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.IntTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.LongTag;
import net.querz.nbt.NBTTestCase;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;
import java.util.Arrays;
import static org.junit.Assert.assertNotEquals;

public class StructTagTest extends NBTTestCase {

	private StructTag createStructTag() {
		StructTag s = new StructTag();
		s.add(new ByteTag(Byte.MAX_VALUE));
		s.add(new IntTag(Integer.MAX_VALUE));
		return s;
	}

	public void testStringConversion() {
		StructTag.register();
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

	public void testHashCode() {
		StructTag s = createStructTag();
		StructTag s2 = createStructTag();
		s2.addInt(123);
		assertNotEquals(s.hashCode(), s2.hashCode());
		assertEquals(s.hashCode(), s.clone().hashCode());

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

	public void testCompareTo() {
		StructTag st = new StructTag();
		st.addInt(1);
		st.addInt(2);
		StructTag so = new StructTag();
		so.addInt(3);
		so.addInt(4);
		assertEquals(0, st.compareTo(so));
		so.addInt(5);
		assertEquals(-1, st.compareTo(so));
		so.remove(2);
		so.remove(1);
		assertEquals(1, st.compareTo(so));
		assertThrowsRuntimeException(() -> st.compareTo(null), NullPointerException.class);
	}

	public void testContains() {
		StructTag l = new StructTag();
		l.addInt(1);
		l.addLong(2);
		assertTrue(l.contains(new IntTag(1)));
		assertFalse(l.contains(new IntTag(2)));
		assertTrue(l.containsAll(Arrays.asList(new IntTag(1), new LongTag(2))));
		assertFalse(l.containsAll(Arrays.asList(new IntTag(1), new IntTag(2))));
	}

	public void testIterator() {
		StructTag l = new StructTag();
		l.addInt(1);
		l.addLong(2);
		for (Tag<?> t : l) {
			assertNotNull(t);
		}
		l.forEach(TestCase::assertNotNull);
	}

	public void testSet() {
		StructTag l = createStructTag();
		l.set(1, new ByteTag((byte) 5));
		assertEquals(2, l.size());
		assertEquals(5, l.getByte(1));
		assertThrowsRuntimeException(() -> l.set(0, null), NullPointerException.class);
	}
	
	public void testAdd() {
		StructTag l = new StructTag();
		l.addBoolean(true);
		assertThrowsNoRuntimeException(() -> l.addShort((short) 5));
		assertEquals(2, l.size());
		assertEquals(1, l.getByte(0));
		l.addByte(Byte.MAX_VALUE);
		assertEquals(3, l.size());
		assertEquals(Byte.MAX_VALUE, l.getByte(2));
		l.addBoolean(true);
		assertEquals(1, l.getByte(3));
		l.addBoolean(false);
		assertEquals(0, l.getByte(4));
		assertTrue(l.getBoolean(3));
		assertFalse(l.getBoolean(4));
		l.remove(new ByteTag(Byte.MAX_VALUE));
		assertEquals(4, l.size());
		assertThrowsRuntimeException(() -> l.remove(-1), ArrayIndexOutOfBoundsException.class);
		assertThrowsRuntimeException(() -> l.remove(4), IndexOutOfBoundsException.class);
		assertEquals(new ByteTag(true), assertThrowsNoRuntimeException(() -> l.remove(2)));
		l.clear();
		assertEquals(0, l.size());

		StructTag s = new StructTag();
		s.addShort(Short.MAX_VALUE);
		assertEquals(1, s.size());
		assertEquals(Short.MAX_VALUE, s.getShort(0));
		StructTag i = new StructTag();
		i.addInt(Integer.MAX_VALUE);
		assertEquals(1, i.size());
		assertEquals(Integer.MAX_VALUE, i.getInt(0));
		StructTag lo = new StructTag();
		lo.addLong(Long.MAX_VALUE);
		assertEquals(1, lo.size());
		assertEquals(Long.MAX_VALUE, lo.getLong(0));
		StructTag f = new StructTag();
		f.addFloat(Float.MAX_VALUE);
		assertEquals(1, f.size());
		assertEquals(Float.MAX_VALUE, f.getFloat(0));
		StructTag d = new StructTag();
		d.addDouble(Double.MAX_VALUE);
		assertEquals(1, d.size());
		assertEquals(Double.MAX_VALUE, d.getDouble(0));
		StructTag st = new StructTag();
		st.addString("foo");
		assertEquals(1, st.size());
		assertEquals("foo", st.getString(0));
		StructTag ba = new StructTag();
		ba.addByteArray(new byte[] {Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		assertEquals(1, ba.size());
		assertTrue(Arrays.equals(new byte[] {Byte.MIN_VALUE, 0, Byte.MAX_VALUE}, ba.getByteArray(0)));
		StructTag ia = new StructTag();
		ia.addIntArray(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE});
		assertEquals(1, ia.size());
		assertTrue(Arrays.equals(new int[] {Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, ia.getIntArray(0)));
		StructTag la = new StructTag();
		la.addLongArray(new long[] {Long.MIN_VALUE, 0, Long.MAX_VALUE});
		assertEquals(1, la.size());
		assertTrue(Arrays.equals(new long[] {Long.MIN_VALUE, 0, Long.MAX_VALUE}, la.getLongArray(0)));
		StructTag co = new StructTag();
		co.add(new CompoundTag());
		assertEquals(1, co.size());
		assertEquals(new CompoundTag(), co.getCompoundTag(0));
		StructTag li = new StructTag();
		li.add(new ListTag<>(IntTag.class));
		assertEquals(1, li.size());
		assertEquals(new ListTag<>(IntTag.class), li.getListTag(0));

		StructTag t = new StructTag();
		t.add(0, new StringTag("foo"));
		t.add(0, new IntTag(Integer.MAX_VALUE));
		assertEquals(2, t.size());
		assertEquals(new IntTag(Integer.MAX_VALUE), t.get(0));
		assertEquals(new StringTag("foo"), t.get(1));
	}
}
