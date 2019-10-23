package net.querz.nbt;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.querz.nbt.NBTUtil.serializedSize;

/**
 * Tests for new FileOptions handling
 */
public class LittleEndianTest extends NBTTestCase
{
    public void testWriteReadTag() {
        CompoundTag t = new CompoundTag();
        invokeSetValue(t, new LinkedHashMap<>());
        t.putByte("byte", Byte.MAX_VALUE);
        t.putShort("short", Short.MAX_VALUE);
        File file = getNewTmpFile("littleendian.dat");
        NBTUtil.FileOptions options = NBTUtil.FileOptions.builder()
                .compressionOption(NBTUtil.CompressionOption.NONE)
                .isLittleEndian(true)
                .build();
        try {
            NBTUtil.writeTag(t, file, options);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        assertEquals("D4EDEF66B790491EC206C82528BFB38E", calculateFileMD5(file));

        try {
            CompoundTag tt = (CompoundTag) NBTUtil.readTag(file, options);
            assertEquals(t, tt);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    public void testWriteReadTagWithHeader() {
        int version = 3;
        AtomicInteger size = new AtomicInteger();
        CompoundTag t = new CompoundTag();
        invokeSetValue(t, new LinkedHashMap<>());
        t.putByte("byte", Byte.MAX_VALUE);
        t.putShort("short", Short.MAX_VALUE);
        File file = getNewTmpFile("littleendianheader.dat");
        NBTUtil.FileOptions options = NBTUtil.FileOptions.builder()
                .compressionOption(NBTUtil.CompressionOption.NONE)
                .isLittleEndian(true)
                .headerWriter(dos -> {
                    size.set(serializedSize(t));
                    dos.writeInt(version);
                    dos.writeInt(size.get());
                    assertEquals(22, size.get());
                })
                .headerReader(dis -> {
                    int v = dis.readInt();
                    assertEquals(version, v);
                    int s = dis.readInt();
                    assertEquals(size.get(), s);
                })
                .build();
        try {
            NBTUtil.writeTag(t, file, options);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        assertEquals("E95B2013C082CC893439C71626F8FC55", calculateFileMD5(file));

        try {
            CompoundTag tt = (CompoundTag) NBTUtil.readTag(file, options);
            assertEquals(t, tt);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
