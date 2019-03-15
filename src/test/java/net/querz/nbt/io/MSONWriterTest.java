package net.querz.nbt.io;

import net.querz.nbt.EndTag;
import net.querz.nbt.NBTTestCase;

import java.io.StringWriter;

public class MSONWriterTest extends NBTTestCase {

	public void testWrite() {
		StringWriter sw = new StringWriter();
		assertThrowsNoException(() -> MSONWriter.write(EndTag.INSTANCE, sw));
		System.out.println(sw.toString());
	}
}
