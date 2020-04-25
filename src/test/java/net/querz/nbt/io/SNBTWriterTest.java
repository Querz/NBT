package net.querz.nbt.io;

import net.querz.NBTTestCase;
import net.querz.nbt.tag.*;

import java.util.LinkedHashMap;

public class SNBTWriterTest extends NBTTestCase {

	public void testWrite() {

		// write number tags

		assertEquals("127b", assertThrowsNoException(() -> SNBTUtil.toSNBT(new ByteTag(Byte.MAX_VALUE))));
		assertEquals("-32768s", assertThrowsNoException(() -> SNBTUtil.toSNBT(new ShortTag(Short.MIN_VALUE))));
		assertEquals("-2147483648", assertThrowsNoException(() -> SNBTUtil.toSNBT(new IntTag(Integer.MIN_VALUE))));
		assertEquals("-9223372036854775808l", assertThrowsNoException(() -> SNBTUtil.toSNBT(new LongTag(Long.MIN_VALUE))));
		assertEquals("123.456f", assertThrowsNoException(() -> SNBTUtil.toSNBT(new FloatTag(123.456F))));
		assertEquals("123.456d", assertThrowsNoException(() -> SNBTUtil.toSNBT(new DoubleTag(123.456D))));

		// write array tags

		assertEquals("[B;-128,0,127]", assertThrowsNoException(() -> SNBTUtil.toSNBT(new ByteArrayTag(new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE}))));
		assertEquals("[I;-2147483648,0,2147483647]", assertThrowsNoException(() -> SNBTUtil.toSNBT(new IntArrayTag(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}))));
		assertEquals("[L;-9223372036854775808,0,9223372036854775807]", assertThrowsNoException(() -> SNBTUtil.toSNBT(new LongArrayTag(new long[]{Long.MIN_VALUE, 0, Long.MAX_VALUE}))));

		// write string tag

		assertEquals("abc", assertThrowsNoException(() -> SNBTUtil.toSNBT(new StringTag("abc"))));
		assertEquals("\"123\"", assertThrowsNoException(() -> SNBTUtil.toSNBT(new StringTag("123"))));
		assertEquals("\"123.456\"", assertThrowsNoException(() -> SNBTUtil.toSNBT(new StringTag("123.456"))));
		assertEquals("\"-123\"", assertThrowsNoException(() -> SNBTUtil.toSNBT(new StringTag("-123"))));
		assertEquals("\"-1.23e14\"", assertThrowsNoException(() -> SNBTUtil.toSNBT(new StringTag("-1.23e14"))));
		assertEquals("\"äöü\\\\\"", assertThrowsNoException(() -> SNBTUtil.toSNBT(new StringTag("äöü\\"))));

		// write list tag

		ListTag<StringTag> lt = new ListTag<>(StringTag.class);
		lt.addString("blah");
		lt.addString("blubb");
		lt.addString("123");
		assertEquals("[blah,blubb,\"123\"]", assertThrowsNoException(() -> SNBTUtil.toSNBT(lt)));

		// write compound tag
		CompoundTag ct = new CompoundTag();
		invokeSetValue(ct, new LinkedHashMap<>());
		ct.putString("key", "value");
		ct.putByte("byte", Byte.MAX_VALUE);
		ct.putByteArray("array", new byte[]{Byte.MIN_VALUE, 0, Byte.MAX_VALUE});
		ListTag<StringTag> clt = new ListTag<>(StringTag.class);
		clt.addString("foo");
		clt.addString("bar");
		ct.put("list", clt);
		String ctExpected = "{key:value,byte:127b,array:[B;-128,0,127],list:[foo,bar]}";
		assertEquals(ctExpected, assertThrowsNoException(() -> SNBTUtil.toSNBT(ct)));
	}
}
