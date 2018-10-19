package net.querz.nbt;

import javafx.util.Callback;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
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
			assertEquals(t, tt);
		} catch (IOException ex) {
			fail(ex.getMessage());
		}
	}

	public void testApplyDecompression() {
		ByteArrayInputStream baisComp = new ByteArrayInputStream(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0});
		try (DataInputStream in = new DataInputStream(baisComp)) {
			assertTrue(NBTUtil.applyDecompression(in) instanceof GZIPInputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}

		ByteArrayInputStream baisUncomp = new ByteArrayInputStream(new byte[]{0, 0});
		try (DataInputStream in = new DataInputStream(baisUncomp)) {
			assertTrue(NBTUtil.applyDecompression(in) instanceof PushbackInputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testMakeMyCoverageGreatAgain() {
		assertThrowsException(() -> NBTUtil.readTag((String) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, (String) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, (File) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, (String) null, false), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, (File) null, false), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, null, (String) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, (File) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, null, (File) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, (File) null), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, null, (File) null, false), NullPointerException.class);
		assertThrowsException(() -> NBTUtil.writeTag(null, null, (String) null, false), NullPointerException.class);
	}
}
