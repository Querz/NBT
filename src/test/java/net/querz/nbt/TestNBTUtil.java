package net.querz.nbt;

import net.querz.NBTTestCase;
import net.querz.mca.Chunk;
import net.querz.mca.MCAFile;
import net.querz.nbt.io.*;
import net.querz.nbt.io.stream.TagSelector;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import static org.junit.jupiter.api.Assertions.*;

public class TestNBTUtil extends NBTTestCase {

	@Test
	public void testParse() throws IOException {
//		var visitor = new StreamTagVisitor();

		Tag t = null;
		try (DataInputStream dis = new DataInputStream(new GZIPInputStream(this.getClass().getResourceAsStream("level.dat")))) {
//			t = NBTUtil.parseStream(dis, new TagSelector("Data", "WorldGenSettings", "dimensions", CompoundTag.TYPE));
//			t = NBTUtil.parseStream(dis, new TagSelector(List.of("Data"), "Player", CompoundTag.TYPE));
			t = new NBTReader().read(dis);
		} catch (Throwable ex) {
			fail();
		}
		assertNotNull(t);

		System.out.println(NBTUtil.toSNBT(t, "\t"));


		byte[] raw;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			     DataOutputStream dos = new DataOutputStream(bos)) {

			new NBTWriter().write(dos, t);

			raw = bos.toByteArray();
		}

		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(raw))) {
			t = NBTUtil.parseStream(dis, new TagSelector(Arrays.asList("Data", "Player", "Attributes"), "Name", StringTag.READER));

			System.out.println(NBTUtil.toSNBT(t, "\t"));
		}

//		String s = readResourceString(this, "level.dat.snbt");
//		Tag t2 = new SNBTParser(s).parse();
//		assertEquals(t.tag(), t2);
//
//		String s2 = readResourceString(this, "level.dat.indent.snbt");
//		Tag t3 = new SNBTParser(s).parse();
//		assertEquals(t.tag(), t3);
	}

	@Test
	public void testChunkCoords() {
		int cx = -33;
		int cz = 31;

		int i = (cx & 0x1F) + (cz & 0x1F) * 32;
		int z = i >> 5;
		int x = i - z * 32;

		System.out.printf("i: %d, x: %d, z: %d\n", i, x, z);
	}

	@Test
	public void testByteHex() {
		byte b = -127;
		int i = b & 0xFF;
		System.out.println(Integer.toHexString(i));
	}

	@Test
	public void testBlah() throws IOException {
		CompoundTag root = new CompoundTag();
		ListTag list1 = new ListTag();
		CompoundTag level1 = new CompoundTag();
		level1.putByte("level1", (byte) 1);
		level1.putString("skip", "skip");
		list1.add(level1);
		CompoundTag level1_2 = new CompoundTag();
		level1_2.putByte("level1", (byte) 2);
		list1.add(level1_2);
		root.put("level0", list1);

		root.putString("skip", "skip");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		NBTUtil.write(baos, root);

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//		Tag result = NBTUtil.read(bais, false);
		Tag result = NBTUtil.read(bais, false, new TagSelector("level0", "level1", ByteTag.READER));

		System.out.println(NBTUtil.toSNBT(result, "\t"));
	}























}
