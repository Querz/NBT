package net.querz.nbt.custom;

import net.querz.nbt.NBTTestCase;
import net.querz.nbt.NBTUtil;
import net.querz.nbt.TagFactory;
import static org.junit.Assert.assertNotEquals;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ObjectTagTest extends NBTTestCase {

	public void testStringConversion() {
		ObjectTag.register();
		DummyObject d = new DummyObject();
		ObjectTag<DummyObject> o = new ObjectTag<>(d);
		assertEquals(90, o.getID());
		assertEquals("{\"type\":\"ObjectTag\",\"value\":\"" + d + "\"}", o.toString());
		assertEquals("\"" + d + "\"", o.toTagString());
	}

	public void testEquals() {
		DummyObject d = new DummyObject();
		ObjectTag<DummyObject> o = new ObjectTag<>(d);
		ObjectTag<DummyObject> o2 = new ObjectTag<>(d);
		assertTrue(o.equals(o2));
		ObjectTag<DummyObject> o3 = new ObjectTag<>(new DummyObject());
		assertFalse(o.equals(o3));
		ObjectTag<DummyObject> o4 = new ObjectTag<>(d.clone());
		assertTrue(o.equals(o4));
	}

	public void testHashCode() {
		DummyObject d = new DummyObject();
		DummyObject d2 = new DummyObject();
		ObjectTag<DummyObject> o = new ObjectTag<>(d);
		ObjectTag<DummyObject> o2 = new ObjectTag<>(d2);
		assertNotEquals(o.hashCode(), o2.hashCode());
		assertEquals(o.hashCode(), o.clone().hashCode());
		assertEquals(0, new ObjectTag<DummyObject>().hashCode());
	}

	public void testClone() {
		DummyObject d = new DummyObject();
		ObjectTag<DummyObject> o = new ObjectTag<>(d);
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
	}

	public void testSerializeDeserialize() {
		DummyObject d = new DummyObject();
		ObjectTag<DummyObject> o = new ObjectTag<>(d);
		ObjectTag.register();
		byte[] data = serialize(o);
		ObjectTag<?> oo = ((ObjectTag<?>) deserialize(data));
		assertNotNull(oo);
		assertThrowsNoRuntimeException(() -> oo.asTypedObjectTag(AbstractDummyObject.class));
		assertThrowsRuntimeException(() -> oo.asTypedObjectTag(String.class), ClassCastException.class);
		ObjectTag<AbstractDummyObject> ooo = oo.asTypedObjectTag(AbstractDummyObject.class);
		assertTrue(o.equals(ooo));
	}

	public void testNullValue() {
		ObjectTag<DummyObject> n = new ObjectTag<>();
		assertNull(n.getValue());
		assertEquals("{\"type\":\"ObjectTag\",\"value\":null}", n.toString());
		assertEquals("null", n.toTagString());
	}

	public void testNullValueEquals() {
		ObjectTag<DummyObject> n = new ObjectTag<>();
		ObjectTag<DummyObject> n2 = new ObjectTag<>(null);
		assertTrue(n.equals(n2));
		ObjectTag<String> n3 = new ObjectTag<>(null);
		assertTrue(n.equals(n3));
	}

	public void testNullValueClone() {
		ObjectTag<DummyObject> n = new ObjectTag<>();
		ObjectTag<DummyObject> nc = n.clone();
		assertTrue(n.equals(nc));
		assertFalse(n == nc);
		assertTrue(n.getValue() == nc.getValue());
	}

	public void testNullValueSerializeDeserialize() {
		ObjectTag<DummyObject> n = new ObjectTag<>();
		ObjectTag.register();
		byte[] data = serialize(n);
		ObjectTag<?> nn = ((ObjectTag<?>) deserialize(data));
		assertNotNull(nn);
		ObjectTag<AbstractDummyObject> nnn = nn.asTypedObjectTag(AbstractDummyObject.class);
		assertTrue(n.equals(nnn));
	}

	public void testCompareTo() {
		ObjectTag<DummyObject> d = new ObjectTag<>(new DummyObject());
		ObjectTag<DummyObject> d2 = new ObjectTag<>(new DummyObject());
		//not comparable
		assertEquals(0, d.compareTo(d2));

		ObjectTag<String> d3 = new ObjectTag<>("abc");
		ObjectTag<String> d4 = new ObjectTag<>("abd");
		assertTrue(0 > d3.compareTo(d4));
		assertTrue(0 < d4.compareTo(d3));

		ObjectTag<String> d5 = new ObjectTag<>("abc");
		assertEquals(0, d3.compareTo(d5));

		DummyObject o = new DummyObject();
		ObjectTag<DummyObject> d6 = new ObjectTag<>(o);
		ObjectTag<DummyObject> d7 = new ObjectTag<>(o);
		assertEquals(0, d6.compareTo(d7));

		ObjectTag<DummyObject> d8 = new ObjectTag<>();
		assertEquals(1, d8.compareTo(d7));
		assertEquals(-1, d7.compareTo(d8));

		ObjectTag<DummyObject> d9 = new ObjectTag<>();
		assertEquals(0, d8.compareTo(d9));

		List<ObjectTag<DummyObject>> l = new ArrayList<>();
		l.add(d);
		l.add(d9);
		l.add(d2);
		l.sort(Comparator.naturalOrder());
		assertEquals(d9, l.get(2));
	}

	public void testUnknownObject() {
		TagFactory.registerCustomTag(90, ObjectTag::new, ObjectTag.class);
		assertThrowsException(() -> NBTUtil.readTag(getResourceFile("unknown_object_tag.dat")), IOException.class);
	}

	public static abstract class AbstractDummyObject implements Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			return "AbstractDummyObject";
		}
	}

	private static final Random RANDOM = new Random();

	public static class DummyObject extends AbstractDummyObject implements Cloneable {
		private static final long serialVersionUID = 1L;
		
		public byte a = (byte) RANDOM.nextInt(Byte.MAX_VALUE);
		public short b = (short) RANDOM.nextInt(Short.MAX_VALUE);
		public int c = RANDOM.nextInt();
		public long d = RANDOM.nextLong();
		public float e = RANDOM.nextFloat();
		public double f = RANDOM.nextDouble();

		@Override
		public DummyObject clone() {
			try {
				return (DummyObject) super.clone();
			} catch (CloneNotSupportedException ex) {
				ex.printStackTrace();
				return null;
			}
		}

		@Override
		public String toString() {
			return String.format("%d/%d/%d/%d/%f/%f", a, b, c, d, e, f);
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof DummyObject) {
				DummyObject t = (DummyObject) other;
				return t.a == a
						&& t.b == b
						&& t.c == c
						&& t.d == d
						&& t.e == e
						&& t.f == f;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(a, b, c, d, e, f);
		}
	}
}
