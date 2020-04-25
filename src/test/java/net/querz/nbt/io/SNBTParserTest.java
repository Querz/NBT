package net.querz.nbt.io;

import net.querz.NBTTestCase;
import net.querz.nbt.tag.*;
import java.util.Arrays;

public class SNBTParserTest extends NBTTestCase {

	public void testParse() {
		Tag<?> t = assertThrowsNoException(() -> SNBTParser.parse("{abc: def, blah: 4b, blubb: \"string\", \"foo\": 2s}"));
		assertEquals(CompoundTag.class, t.getClass());
		CompoundTag c = (CompoundTag) t;
		assertEquals(4, c.size());
		assertEquals("def", c.getString("abc"));
		assertEquals((byte) 4, c.getByte("blah"));
		assertEquals("string", c.getString("blubb"));
		assertEquals((short) 2, c.getShort("foo"));
		assertFalse(c.containsKey("invalid"));

		// ------------------------------------------------- number tags

		Tag<?> tb = assertThrowsNoException(() -> SNBTParser.parse("16b"));
		assertEquals(ByteTag.class, tb.getClass());
		assertEquals((byte) 16, ((ByteTag) tb).asByte());

		tb = assertThrowsNoException(() -> SNBTParser.parse("16B"));
		assertEquals(ByteTag.class, tb.getClass());
		assertEquals((byte) 16, ((ByteTag) tb).asByte());

		assertThrowsException((() -> SNBTParser.parse("-129b")), ParseException.class);

		Tag<?> ts = assertThrowsNoException(() -> SNBTParser.parse("17s"));
		assertEquals(ShortTag.class, ts.getClass());
		assertEquals((short) 17, ((ShortTag) ts).asShort());

		ts = assertThrowsNoException(() -> SNBTParser.parse("17S"));
		assertEquals(ShortTag.class, ts.getClass());
		assertEquals((short) 17, ((ShortTag) ts).asShort());

		assertThrowsException((() -> SNBTParser.parse("-32769s")), ParseException.class);

		Tag<?> ti = assertThrowsNoException(() -> SNBTParser.parse("18"));
		assertEquals(IntTag.class, ti.getClass());
		assertEquals(18, ((IntTag) ti).asInt());

		assertThrowsException((() -> SNBTParser.parse("-2147483649")), ParseException.class);

		Tag<?> tl = assertThrowsNoException(() -> SNBTParser.parse("19l"));
		assertEquals(LongTag.class, tl.getClass());
		assertEquals(19L, ((LongTag) tl).asLong());

		tl = assertThrowsNoException(() -> SNBTParser.parse("19L"));
		assertEquals(LongTag.class, tl.getClass());
		assertEquals(19L, ((LongTag) tl).asLong());

		assertThrowsException((() -> SNBTParser.parse("-9223372036854775809l")), ParseException.class);

		Tag<?> tf = assertThrowsNoException(() -> SNBTParser.parse("20.3f"));
		assertEquals(FloatTag.class, tf.getClass());
		assertEquals(20.3f, ((FloatTag) tf).asFloat());

		tf = assertThrowsNoException(() -> SNBTParser.parse("20.3F"));
		assertEquals(FloatTag.class, tf.getClass());
		assertEquals(20.3f, ((FloatTag) tf).asFloat());

		Tag<?> td = assertThrowsNoException(() -> SNBTParser.parse("21.3d"));
		assertEquals(DoubleTag.class, td.getClass());
		assertEquals(21.3d, ((DoubleTag) td).asDouble());

		td = assertThrowsNoException(() -> SNBTParser.parse("21.3D"));
		assertEquals(DoubleTag.class, td.getClass());
		assertEquals(21.3d, ((DoubleTag) td).asDouble());

		td = assertThrowsNoException(() -> SNBTParser.parse("21.3"));
		assertEquals(DoubleTag.class, td.getClass());
		assertEquals(21.3d, ((DoubleTag) td).asDouble());

		Tag<?> tbo = assertThrowsNoException(() -> SNBTParser.parse("true"));
		assertEquals(ByteTag.class, tbo.getClass());
		assertEquals((byte) 1, ((ByteTag) tbo).asByte());

		tbo = assertThrowsNoException(() -> SNBTParser.parse("false"));
		assertEquals(ByteTag.class, tbo.getClass());
		assertEquals((byte) 0, ((ByteTag) tbo).asByte());

		// ------------------------------------------------- arrays

		Tag<?> ba = assertThrowsNoException(() -> SNBTParser.parse("[B; -128,0, 127]"));
		assertEquals(ByteArrayTag.class, ba.getClass());
		assertEquals(3, ((ByteArrayTag) ba).length());
		assertTrue(Arrays.equals(new byte[]{-128, 0, 127}, ((ByteArrayTag) ba).getValue()));

		Tag<?> ia = assertThrowsNoException(() -> SNBTParser.parse("[I; -2147483648, 0,2147483647]"));
		assertEquals(IntArrayTag.class, ia.getClass());
		assertEquals(3, ((IntArrayTag) ia).length());
		assertTrue(Arrays.equals(new int[]{-2147483648, 0, 2147483647}, ((IntArrayTag) ia).getValue()));

		Tag<?> la = assertThrowsNoException(() -> SNBTParser.parse("[L; -9223372036854775808, 0, 9223372036854775807 ]"));
		assertEquals(LongArrayTag.class, la.getClass());
		assertEquals(3, ((LongArrayTag) la).length());
		assertTrue(Arrays.equals(new long[]{-9223372036854775808L, 0, 9223372036854775807L}, ((LongArrayTag) la).getValue()));

		// ------------------------------------------------- invalid arrays

		assertThrowsException((() -> SNBTParser.parse("[B; -129]")), ParseException.class);
		assertThrowsException((() -> SNBTParser.parse("[I; -2147483649]")), ParseException.class);
		assertThrowsException((() -> SNBTParser.parse("[L; -9223372036854775809]")), ParseException.class);
		assertThrowsException((() -> SNBTParser.parse("[B; 123b]")), ParseException.class);
		assertThrowsException((() -> SNBTParser.parse("[I; 123i]")), ParseException.class);
		assertThrowsException((() -> SNBTParser.parse("[L; 123l]")), ParseException.class);
		assertThrowsException((() -> SNBTParser.parse("[K; -129]")), ParseException.class);

		// ------------------------------------------------- high level errors

		assertThrowsException(() -> SNBTParser.parse("{20:10} {blah:blubb}"), ParseException.class);

		// ------------------------------------------------- string tag

		Tag<?> st = assertThrowsNoException(() -> SNBTParser.parse("abc"));
		assertEquals(StringTag.class, st.getClass());
		assertEquals("abc", ((StringTag) st).getValue());

		st = assertThrowsNoException(() -> SNBTParser.parse("\"abc\""));
		assertEquals(StringTag.class, st.getClass());
		assertEquals("abc", ((StringTag) st).getValue());

		st = assertThrowsNoException(() -> SNBTParser.parse("123a"));
		assertEquals(StringTag.class, st.getClass());
		assertEquals("123a", ((StringTag) st).getValue());

		// ------------------------------------------------- list tag

		Tag<?> lt = assertThrowsNoException(() -> SNBTParser.parse("[abc, \"def\", \"123\" ]"));
		assertEquals(ListTag.class, lt.getClass());
		assertEquals(StringTag.class, ((ListTag<?>) lt).getTypeClass());
		assertEquals(3, ((ListTag<?>) lt).size());
		assertEquals("abc", ((ListTag<?>) lt).asStringTagList().get(0).getValue());
		assertEquals("def", ((ListTag<?>) lt).asStringTagList().get(1).getValue());
		assertEquals("123", ((ListTag<?>) lt).asStringTagList().get(2).getValue());

		assertThrowsException(() -> SNBTParser.parse("[123, 456"), ParseException.class);
		assertThrowsException(() -> SNBTParser.parse("[123, 456d]"), ParseException.class);

		// ------------------------------------------------- compound tag

		Tag<?> ct = assertThrowsNoException(() -> SNBTParser.parse("{abc: def,\"key\": 123d, blah: [L;123, 456], blubb: [123, 456]}"));
		assertEquals(CompoundTag.class, ct.getClass());
		assertEquals(4, ((CompoundTag) ct).size());
		assertEquals("def", assertThrowsNoException(() -> ((CompoundTag) ct).getString("abc")));
		assertEquals(123D, assertThrowsNoException(() -> ((CompoundTag) ct).getDouble("key")));
		assertTrue(Arrays.equals(new long[]{123, 456}, assertThrowsNoException(() -> ((CompoundTag) ct).getLongArray("blah"))));
		assertEquals(2, assertThrowsNoException(() -> ((CompoundTag) ct).getListTag("blubb")).size());
		assertEquals(IntTag.class, ((CompoundTag) ct).getListTag("blubb").getTypeClass());

		assertThrowsException(() -> SNBTParser.parse("{abc: def"), ParseException.class);
		assertThrowsException(() -> SNBTParser.parse("{\"\":empty}"), ParseException.class);
		assertThrowsException(() -> SNBTParser.parse("{empty:}"), ParseException.class);
	}
}
