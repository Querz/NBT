package net.querz.nbt.io;

import net.querz.NBTTestCase;
import net.querz.nbt.tag.*;
import java.util.Arrays;

public class SNBTParserTest extends NBTTestCase {

	public void testParse() {
		Tag<?> t = assertThrowsNoException(() -> new SNBTParser("{abc: def, blah: 4b, blubb: \"string\", \"foo\": 2s}").parse());
		assertEquals(CompoundTag.class, t.getClass());
		CompoundTag c = (CompoundTag) t;
		assertEquals(4, c.size());
		assertEquals("def", c.getString("abc"));
		assertEquals((byte) 4, c.getByte("blah"));
		assertEquals("string", c.getString("blubb"));
		assertEquals((short) 2, c.getShort("foo"));
		assertFalse(c.containsKey("invalid"));

		// ------------------------------------------------- number tags

		Tag<?> tb = assertThrowsNoException(() -> new SNBTParser("16b").parse());
		assertEquals(ByteTag.class, tb.getClass());
		assertEquals((byte) 16, ((ByteTag) tb).asByte());

		tb = assertThrowsNoException(() -> new SNBTParser("16B").parse());
		assertEquals(ByteTag.class, tb.getClass());
		assertEquals((byte) 16, ((ByteTag) tb).asByte());

		assertThrowsException((() -> new SNBTParser("-129b").parse()), ParseException.class);

		Tag<?> ts = assertThrowsNoException(() -> new SNBTParser("17s").parse());
		assertEquals(ShortTag.class, ts.getClass());
		assertEquals((short) 17, ((ShortTag) ts).asShort());

		ts = assertThrowsNoException(() -> new SNBTParser("17S").parse());
		assertEquals(ShortTag.class, ts.getClass());
		assertEquals((short) 17, ((ShortTag) ts).asShort());

		assertThrowsException((() -> new SNBTParser("-32769s").parse()), ParseException.class);

		Tag<?> ti = assertThrowsNoException(() -> new SNBTParser("18").parse());
		assertEquals(IntTag.class, ti.getClass());
		assertEquals(18, ((IntTag) ti).asInt());

		assertThrowsException((() -> new SNBTParser("-2147483649").parse()), ParseException.class);

		Tag<?> tl = assertThrowsNoException(() -> new SNBTParser("19l").parse());
		assertEquals(LongTag.class, tl.getClass());
		assertEquals(19L, ((LongTag) tl).asLong());

		tl = assertThrowsNoException(() -> new SNBTParser("19L").parse());
		assertEquals(LongTag.class, tl.getClass());
		assertEquals(19L, ((LongTag) tl).asLong());

		assertThrowsException((() -> new SNBTParser("-9223372036854775809l").parse()), ParseException.class);

		Tag<?> tf = assertThrowsNoException(() -> new SNBTParser("20.3f").parse());
		assertEquals(FloatTag.class, tf.getClass());
		assertEquals(20.3f, ((FloatTag) tf).asFloat());

		tf = assertThrowsNoException(() -> new SNBTParser("20.3F").parse());
		assertEquals(FloatTag.class, tf.getClass());
		assertEquals(20.3f, ((FloatTag) tf).asFloat());

		Tag<?> td = assertThrowsNoException(() -> new SNBTParser("21.3d").parse());
		assertEquals(DoubleTag.class, td.getClass());
		assertEquals(21.3d, ((DoubleTag) td).asDouble());

		td = assertThrowsNoException(() -> new SNBTParser("21.3D").parse());
		assertEquals(DoubleTag.class, td.getClass());
		assertEquals(21.3d, ((DoubleTag) td).asDouble());

		td = assertThrowsNoException(() -> new SNBTParser("21.3").parse());
		assertEquals(DoubleTag.class, td.getClass());
		assertEquals(21.3d, ((DoubleTag) td).asDouble());

		Tag<?> tbo = assertThrowsNoException(() -> new SNBTParser("true").parse());
		assertEquals(ByteTag.class, tbo.getClass());
		assertEquals((byte) 1, ((ByteTag) tbo).asByte());

		tbo = assertThrowsNoException(() -> new SNBTParser("false").parse());
		assertEquals(ByteTag.class, tbo.getClass());
		assertEquals((byte) 0, ((ByteTag) tbo).asByte());

		// ------------------------------------------------- arrays

		Tag<?> ba = assertThrowsNoException(() -> new SNBTParser("[B; -128,0, 127]").parse());
		assertEquals(ByteArrayTag.class, ba.getClass());
		assertEquals(3, ((ByteArrayTag) ba).length());
		assertTrue(Arrays.equals(new byte[]{-128, 0, 127}, ((ByteArrayTag) ba).getValue()));

		Tag<?> ia = assertThrowsNoException(() -> new SNBTParser("[I; -2147483648, 0,2147483647]").parse());
		assertEquals(IntArrayTag.class, ia.getClass());
		assertEquals(3, ((IntArrayTag) ia).length());
		assertTrue(Arrays.equals(new int[]{-2147483648, 0, 2147483647}, ((IntArrayTag) ia).getValue()));

		Tag<?> la = assertThrowsNoException(() -> new SNBTParser("[L; -9223372036854775808, 0, 9223372036854775807 ]").parse());
		assertEquals(LongArrayTag.class, la.getClass());
		assertEquals(3, ((LongArrayTag) la).length());
		assertTrue(Arrays.equals(new long[]{-9223372036854775808L, 0, 9223372036854775807L}, ((LongArrayTag) la).getValue()));

		// ------------------------------------------------- invalid arrays

		assertThrowsException((() -> new SNBTParser("[B; -129]").parse()), ParseException.class);
		assertThrowsException((() -> new SNBTParser("[I; -2147483649]").parse()), ParseException.class);
		assertThrowsException((() -> new SNBTParser("[L; -9223372036854775809]").parse()), ParseException.class);
		assertThrowsException((() -> new SNBTParser("[B; 123b]").parse()), ParseException.class);
		assertThrowsException((() -> new SNBTParser("[I; 123i]").parse()), ParseException.class);
		assertThrowsException((() -> new SNBTParser("[L; 123l]").parse()), ParseException.class);
		assertThrowsException((() -> new SNBTParser("[K; -129]").parse()), ParseException.class);

		// ------------------------------------------------- high level errors

		assertThrowsException(() -> new SNBTParser("{20:10} {blah:blubb}").parse(), ParseException.class);

		// ------------------------------------------------- string tag

		Tag<?> st = assertThrowsNoException(() -> new SNBTParser("abc").parse());
		assertEquals(StringTag.class, st.getClass());
		assertEquals("abc", ((StringTag) st).getValue());

		st = assertThrowsNoException(() -> new SNBTParser("\"abc\"").parse());
		assertEquals(StringTag.class, st.getClass());
		assertEquals("abc", ((StringTag) st).getValue());

		st = assertThrowsNoException(() -> new SNBTParser("123a").parse());
		assertEquals(StringTag.class, st.getClass());
		assertEquals("123a", ((StringTag) st).getValue());

		// ------------------------------------------------- list tag

		Tag<?> lt = assertThrowsNoException(() -> new SNBTParser("[abc, \"def\", \"123\" ]").parse());
		assertEquals(ListTag.class, lt.getClass());
		assertEquals(StringTag.class, ((ListTag<?>) lt).getTypeClass());
		assertEquals(3, ((ListTag<?>) lt).size());
		assertEquals("abc", ((ListTag<?>) lt).asStringTagList().get(0).getValue());
		assertEquals("def", ((ListTag<?>) lt).asStringTagList().get(1).getValue());
		assertEquals("123", ((ListTag<?>) lt).asStringTagList().get(2).getValue());

		assertThrowsException(() -> new SNBTParser("[123, 456").parse(), ParseException.class);
		assertThrowsException(() -> new SNBTParser("[123, 456d]").parse(), ParseException.class);

		// ------------------------------------------------- compound tag

		Tag<?> ct = assertThrowsNoException(() -> new SNBTParser("{abc: def,\"key\": 123d, blah: [L;123, 456], blubb: [123, 456]}").parse());
		assertEquals(CompoundTag.class, ct.getClass());
		assertEquals(4, ((CompoundTag) ct).size());
		assertEquals("def", assertThrowsNoException(() -> ((CompoundTag) ct).getString("abc")));
		assertEquals(123D, assertThrowsNoException(() -> ((CompoundTag) ct).getDouble("key")));
		assertTrue(Arrays.equals(new long[]{123, 456}, assertThrowsNoException(() -> ((CompoundTag) ct).getLongArray("blah"))));
		assertEquals(2, assertThrowsNoException(() -> ((CompoundTag) ct).getListTag("blubb")).size());
		assertEquals(IntTag.class, ((CompoundTag) ct).getListTag("blubb").getTypeClass());

		assertThrowsException(() -> new SNBTParser("{abc: def").parse(), ParseException.class);
		assertThrowsException(() -> new SNBTParser("{\"\":empty}").parse(), ParseException.class);
		assertThrowsException(() -> new SNBTParser("{empty:}").parse(), ParseException.class);
	}
}
