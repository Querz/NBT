package net.querz.nbt.io;

import net.querz.nbt.NBTTestCase;

public class StringDeserializerTest extends NBTTestCase {

	public void test() {
		String s = "{blah:1.3f,\"foo\":\"bar\",test:\" moo\",bytes:[B;1,2,3,4]}";
		MSONDeserializer m = new MSONDeserializer(s);
		System.out.println(m.read(1));
	}
}
