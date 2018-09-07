package net.querz.nbt;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;

public class TagTest extends NBTTestCase {

	public void testWriteReadTag() {
		CompoundTag t = new CompoundTag();
		invokeSetValue(t, new LinkedHashMap<>());
		t.putByte("byte", Byte.MAX_VALUE);
		t.putShort("short", Short.MAX_VALUE);
		File file = getNewTmpFile("compressed.dat");
		try {
			NBTUtil.writeTag(t, "name", file, true);
		} catch (IOException ex) {
			fail(ex.getMessage());
		}

		assertEquals("E8F7B55F81FADB8A5657461D9188DE73", calculateFileMD5(file));

		try {
			CompoundTag tt = (CompoundTag) NBTUtil.readTag(file);
			assertTrue(t.equals(tt));
		} catch (IOException ex) {
			fail(ex.getMessage());
		}
	}

//	public void testApplyDecompression() {
//		ByteArrayInputStream baisComp = new ByteArrayInputStream(new byte[]{(byte) 0x1F, (byte) 0x8B, 0});
//		try (DataInputStream in = new DataInputStream(baisComp)) {
//			assertTrue(NBTUtil.applyDecompression(in) instanceof GZIPInputStream);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//			fail(ex.getMessage());
//		}
//	}
}
