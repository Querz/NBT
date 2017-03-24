package net.querz.nbt.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Random;

import static net.querz.nbt.test.TestUtil.*;

import junit.framework.TestCase;
import net.querz.nbt.*;

public class TagTest extends TestCase {
	private static final Random RANDOM = new Random();
	
	private ByteTag maxByte = new ByteTag("byte", Byte.MAX_VALUE);
	private ShortTag maxShort = new ShortTag("short", Short.MAX_VALUE);
	private IntTag maxInt = new IntTag("int", Integer.MAX_VALUE);
	private LongTag maxLong = new LongTag("long", Long.MAX_VALUE);
	private FloatTag maxFloat = new FloatTag("float", Float.MAX_VALUE);
	private DoubleTag maxDouble = new DoubleTag("double", Double.MAX_VALUE);
	private ByteTag minByte = new ByteTag("byte", Byte.MIN_VALUE);
	private ShortTag minShort = new ShortTag("short", Short.MIN_VALUE);
	private IntTag minInt = new IntTag("int", Integer.MIN_VALUE);
	private LongTag minLong = new LongTag("long", Long.MIN_VALUE);
	private FloatTag minFloat = new FloatTag("float", Float.MIN_VALUE);
	private DoubleTag minDouble = new DoubleTag("double", Double.MIN_VALUE);
	private FloatTag decFloat = new FloatTag("decFloat", (float) Math.PI);
	private DoubleTag decDouble = new DoubleTag("decDouble", Math.PI);
	private ByteTag boolTrue = new ByteTag("boolTrue", true);
	private ByteTag boolFalse = new ByteTag("boolFalse", false);
	private ByteArrayTag byteArray = new ByteArrayTag("byteArray", new byte[] {Byte.MIN_VALUE, -2, -1, 0, 1, 2, Byte.MAX_VALUE});
	private IntArrayTag intArray = new IntArrayTag("intArray", new int[] {Integer.MIN_VALUE, -2, -1, 0, 1, 2, Integer.MAX_VALUE});
	private StringTag string = new StringTag("string0aAÂ«âˆ‘â‚¬Â®â€ Î©Â¨â�„Ã¸Ï€â€¢Â±Ã¥â€šâˆ‚Æ’Â©ÂªÂºâˆ†@Å“Ã¦â€˜Â¥â‰ˆÃ§âˆšâˆ«~Âµâˆžâ€¦", "0aAÂ«âˆ‘â‚¬Â®â€ Î©Â¨â�„Ã¸Ï€â€¢Â±Ã¥â€šâˆ‚Æ’Â©ÂªÂºâˆ†@Å“Ã¦â€˜Â¥â‰ˆÃ§âˆšâˆ«~Âµâˆžâ€¦");
	private ListTag byteList = new ListTag("byteList", TagType.BYTE);
	private Tag nullName = new StringTag("");
	
	public void setUp() {
		byteList.add(maxByte);
		byteList.addByte((byte) minByte.getValue());
		byteList.addBoolean(((ByteTag) boolTrue).getBoolean());
		byteList.addBoolean(((ByteTag) boolFalse).getBoolean());
		nullName.setName(null);
	}
	
	public void testByteTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(maxByte), maxByte);
		assertTagNotNullEquals(serializeAndDeserialize(minByte), minByte);
		assertTagNotNullEquals(serializeAndDeserialize(boolTrue), boolTrue);
		assertTagNotNullEquals(serializeAndDeserialize(boolFalse), boolFalse);
	}
	
	public void testShortTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(maxShort), maxShort);
		assertTagNotNullEquals(serializeAndDeserialize(minShort), minShort);
	}
	
	public void testIntTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(maxInt), maxInt);
		assertTagNotNullEquals(serializeAndDeserialize(minInt), minInt);
	}
	
	public void testLongTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(maxLong), maxLong);
		assertTagNotNullEquals(serializeAndDeserialize(minLong), minLong);
	}
	
	public void testFloatTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(maxFloat), maxFloat);
		assertTagNotNullEquals(serializeAndDeserialize(minFloat), minFloat);
		assertTagNotNullEquals(serializeAndDeserialize(decFloat), decFloat);
	}
	
	public void testDoubleTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(maxDouble), maxDouble);
		assertTagNotNullEquals(serializeAndDeserialize(minDouble), minDouble);
		assertTagNotNullEquals(serializeAndDeserialize(decDouble), decDouble);
	}
	
	public void testStringTag() throws IOException {
		assertTagNotNullEquals(serializeAndDeserialize(string), string);
		assertTagNotNullEquals(string, string.clone());
		assertTrue(string != string.clone());
	}
	
	public void testByteArrayTag() throws IOException {
		ByteArrayTag t = (ByteArrayTag) serializeAndDeserialize(byteArray);
		assertNotNull(t);
		assertEquals(t.getType(), byteArray.getType());
		assertEquals(t.getName(), byteArray.getName());
		assertTrue(Arrays.equals(t.getValue(), byteArray.getValue()));
		assertTrue(t.equals(byteArray));
		assertTrue(t != t.clone());
		assertTrue(t.getValue() != t.clone().getValue());
	}
	
	public void testIntArrayTag() throws IOException {
		IntArrayTag t = (IntArrayTag) serializeAndDeserialize(intArray);
		assertNotNull(t);
		assertEquals(t.getType(), intArray.getType());
		assertEquals(t.getName(), intArray.getName());
		assertTrue(Arrays.equals(t.getValue(), intArray.getValue()));
		assertTrue(t.equals(intArray));
		assertTrue(t != t.clone());
		assertTrue(t.getValue() != t.clone().getValue());
	}
	
	public void testListTag() throws IOException {
		assertEquals(byteList.toString(), "<list:byteList:[<byte:byte:127>,<byte::-128>,<byte::1>,<byte::0>]>");
		assertEquals(byteList.toTagString(), "byteList:[127b,-128b,1b,0b]");
		
		ListTag byteList2 = (ListTag) serializeAndDeserialize(byteList);
		assertNotNull(byteList2);
		assertEquals(byteList2.getType(), byteList.getType());
		assertEquals(byteList2.getListType(), byteList.getListType());
		assertEquals(byteList2.getName(), byteList.getName());
		for (int i = 0; i < byteList.size(); i++) {
			//ListTag ignores tag names, therefore only check values
			assertEquals(byteList.get(i).getValue(), byteList2.get(i).getValue());
		}
		assertTrue(byteList2.getBoolean(0));
		assertFalse(byteList2.getBoolean(1));
		assertTrue(byteList2.getBoolean(2));
		assertFalse(byteList2.getBoolean(3));
		assertEquals(byteList2.asByte(0), byteList.getByte(0));
		assertThrowsException(() -> byteList.add(maxShort), IllegalArgumentException.class);
		
		// equals should ignore tag names in list tags
		assertTrue(byteList2.equals(byteList));
		assertTrue(byteList != byteList.clone());
		assertTrue(byteList.getValue() != byteList.clone().getValue());
	}
	
	public void testCompoundTag() throws IOException {
		CompoundTag compound = new CompoundTag("compound");
		compound.clearOrdered();
		compound.setBoolean("boolTrue", boolTrue.getBoolean());
		compound.setBoolean("boolFalse", boolFalse.getBoolean());
		compound.setByte("maxByte", maxByte.getValue());
		compound.setByte("minByte", minByte.getValue());
		compound.setShort("maxShort", maxShort.getValue());
		compound.setShort("minShort", minShort.getValue());
		compound.setInt("maxInt", maxInt.getValue());
		compound.setInt("minInt", minInt.getValue());
		compound.setLong("maxLong", maxLong.getValue());
		compound.setLong("minLong", minLong.getValue());
		compound.setFloat("maxFloat", maxFloat.getValue());
		compound.setFloat("minFloat", minFloat.getValue());
		compound.setDouble("maxDouble", maxDouble.getValue());
		compound.setDouble("minDouble", minDouble.getValue());
		compound.set(byteArray);
		compound.set(intArray);
		compound.set(string);
		compound.set(byteList);
		
		//test cloning
		assertEquals(compound, compound.clone());
		
		CompoundTag compoundClone = compound.clone();
		CompoundTag compoundClone2 = compound.clone();
		compoundClone2.setName("compoundClone2");
		compoundClone.setName("compoundClone");
		compound.set(compoundClone);
		compound.set(compoundClone2);
		
		//test if there's no recursion; compound should be cloned before being added
		CompoundTag compound2 = (CompoundTag) serializeAndDeserialize(compound);
		assertEquals(compound, compound2);

		assertEquals(compound.toString(), TestUtil.readStringFromFile("test_compound_toString.txt"));
		assertEquals(compound.toTagString(), TestUtil.readStringFromFile("test_compound_toTagString.txt"));
		
	}
	
	public void testNBTFileReader() throws IOException {
		System.out.println("workingdir: " + (new File(".")).getCanonicalPath());
		Tag t = new NBTFileReader(TestUtil.RESOURCES_PATH + "test_compound_gzip.dat").read();
		assertNotNull(t);
		Tag t2 = new NBTFileReader(TestUtil.RESOURCES_PATH + "test_compound.dat").read();
		assertNotNull(t2);
	}
	
	public void testCircularReference() {
		CompoundTag t = new CompoundTag("c");
		t.set(t);
		
		assertThrowsException(() -> t.toString(), MaxDepthReachedException.class);
		assertThrowsException(() -> wrappedSerializeAndDeserialize(t), MaxDepthReachedException.class);
		assertThrowsException(() -> t.toTagString(), MaxDepthReachedException.class);
		
		
		ListTag l = new ListTag("l", TagType.LIST);
		l.add(l);
		
		assertThrowsException(() -> l.toString(), MaxDepthReachedException.class);
		assertThrowsException(() -> wrappedSerializeAndDeserialize(l), MaxDepthReachedException.class);
		assertThrowsException(() -> l.toTagString(), MaxDepthReachedException.class);
		
		
		CompoundTag b = new CompoundTag("base");
		CompoundTag current = new CompoundTag("depth_0");
		b.set(current);
		
		//counting to a depth of Tag.MAX_DEPTH - 1
		for (int i = 1; i < Tag.MAX_DEPTH - 1; i++) {
			CompoundTag c = new CompoundTag("depth_" + i);
			current.set(c);
			c.set(new IntTag("randomInt", RANDOM.nextInt()));
			current = c;
		}
//		System.out.println(b);
		assertThrowsNoException(() -> b.toString());
		assertThrowsNoException(() -> b.toTagString());
		assertThrowsNoException(() -> wrappedSerializeAndDeserialize(b));
	}
	
	//only works with primitive tags
	private void assertTagNotNullEquals(Tag t1, Tag t2) {
		assertTrue(t1 != null && t2 != null);
		assertEquals(t1.getType(), t2.getType());
		if (t1.getName() != null && t2.getName() != null)
			assertEquals(t1.getName(), t2.getName());
		else
			assertEquals(t1.getName(), t2.getName());
		//compares reference if it's not a primitive
		assertEquals(t1.getValue(), t2.getValue());
	}
	
	private Tag serializeAndDeserialize(Tag tag) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try (NBTOutputStream nbtOut = new NBTOutputStream(byteOut)) {
			nbtOut.writeTag(tag);
		}
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		try (NBTInputStream nbtIn = new NBTInputStream(byteIn)) {
			return nbtIn.readTag();
		}
	}
	
	private Tag wrappedSerializeAndDeserialize(Tag tag) {
		try {
			return serializeAndDeserialize(tag);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
}
