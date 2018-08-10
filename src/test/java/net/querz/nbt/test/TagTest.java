package net.querz.nbt.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import static net.querz.nbt.test.TestUtil.*;
import junit.framework.TestCase;
import net.querz.nbt.*;
import net.querz.nbt.custom.ObjectTag;
import net.querz.nbt.custom.ShortArrayTag;
import net.querz.nbt.custom.StructTag;

public class TagTest extends TestCase {
//	private static final Random RANDOM = new Random();
//
//	private ByteTag maxByte = new ByteTag("byte", Byte.MAX_VALUE);
//	private ShortTag maxShort = new ShortTag("short", Short.MAX_VALUE);
//	private IntTag maxInt = new IntTag("int", Integer.MAX_VALUE);
//	private LongTag maxLong = new LongTag("long", Long.MAX_VALUE);
//	private FloatTag maxFloat = new FloatTag("float", Float.MAX_VALUE);
//	private DoubleTag maxDouble = new DoubleTag("double", Double.MAX_VALUE);
//	private ByteTag minByte = new ByteTag("byte", Byte.MIN_VALUE);
//	private ShortTag minShort = new ShortTag("short", Short.MIN_VALUE);
//	private IntTag minInt = new IntTag("int", Integer.MIN_VALUE);
//	private LongTag minLong = new LongTag("long", Long.MIN_VALUE);
//	private FloatTag minFloat = new FloatTag("float", Float.MIN_VALUE);
//	private DoubleTag minDouble = new DoubleTag("double", Double.MIN_VALUE);
//	private ByteTag zeroByte = new ByteTag("byte", (byte) 0);
//	private ShortTag zeroShort = new ShortTag("short", (short) 0);
//	private FloatTag decFloat = new FloatTag("decFloat", (float) Math.PI);
//	private DoubleTag decDouble = new DoubleTag("decDouble", Math.PI);
//	private ByteTag boolTrue = new ByteTag("boolTrue", true);
//	private ByteTag boolFalse = new ByteTag("boolFalse", false);
//	private ByteArrayTag byteArray = new ByteArrayTag("byteArray", new byte[] {Byte.MIN_VALUE, -2, -1, 0, 1, 2, Byte.MAX_VALUE});
//	private ShortArrayTag shortArray = new ShortArrayTag("shortArray", new short[] {Short.MIN_VALUE, -2, -1, 0, 1, 2, Short.MAX_VALUE});
//	private IntArrayTag intArray = new IntArrayTag("intArray", new int[] {Integer.MIN_VALUE, -2, -1, 0, 1, 2, Integer.MAX_VALUE});
//	private LongArrayTag longArray = new LongArrayTag("longArray", new long[] {Long.MIN_VALUE, -2, -1, 0, 1, 2, Long.MAX_VALUE});
//	private StringTag string = new StringTag("string0aAÂ«âˆ‘â‚¬Â®â€ Î©Â¨â�„Ã¸Ï€â€¢Â±Ã¥â€šâˆ‚Æ’Â©ÂªÂºâˆ†@Å“Ã¦â€˜Â¥â‰ˆÃ§âˆšâˆ«~Âµâˆžâ€¦", "0aAÂ«âˆ‘â‚¬Â®â€ Î©Â¨â�„Ã¸Ï€â€¢Â±Ã¥â€šâˆ‚Æ’Â©ÂªÂºâˆ†@Å“Ã¦â€˜Â¥â‰ˆÃ§âˆšâˆ«~Âµâˆžâ€¦");
//	private ListTag<ByteTag> byteList = new ListTag<>("byteList");
//
//	private void populateCompoundTag(CompoundTag tag) {
//		tag.putBoolean("boolTrue", boolTrue.asBoolean());
//		tag.putBoolean("boolFalse", boolFalse.asBoolean());
//		tag.putByte("maxByte", maxByte.asByte());
//		tag.putByte("minByte", minByte.asByte());
//		tag.putShort("maxShort", maxShort.asShort());
//		tag.putShort("minShort", minShort.asShort());
//		tag.putInt("maxInt", maxInt.asInt());
//		tag.putInt("minInt", minInt.asInt());
//		tag.putLong("maxLong", maxLong.asLong());
//		tag.putLong("minLong", minLong.asLong());
//		tag.putFloat("maxFloat", maxFloat.asFloat());
//		tag.putFloat("minFloat", minFloat.asFloat());
//		tag.putDouble("maxDouble", maxDouble.asDouble());
//		tag.putDouble("minDouble", minDouble.asDouble());
//		tag.put(byteArray);
//		tag.put(intArray);
//		tag.put(string);
//		tag.put(byteList);
//		CompoundTag compoundClone = tag.clone();
//		CompoundTag compoundClone2 = tag.clone();
//		compoundClone.setName("compoundClone");
//		compoundClone2.setName("compoundClone2");
//		tag.put(compoundClone);
//		tag.put(compoundClone2);
//	}
//
//	private void populateStructTag(StructTag tag) {
//		tag.add(maxByte);
//		tag.add(decDouble);
//	}
//
//	public void setUp() throws Exception {
//		super.setUp();
//		byteList.add(maxByte);
//		byteList.addByte(minByte.asByte());
//		byteList.addBoolean((boolTrue).asBoolean());
//		byteList.addBoolean((boolFalse).asBoolean());
//	}
//
//	public void testByteTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(maxByte), maxByte);
//		assertTagNotNullEquals(serializeAndDeserialize(minByte), minByte);
//		assertTagNotNullEquals(serializeAndDeserialize(boolTrue), boolTrue);
//		assertTagNotNullEquals(serializeAndDeserialize(boolFalse), boolFalse);
//		assertEquals(maxByte.toString(), "byte:127b");
//		assertTrue(maxByte.equals(serializeAndDeserialize(maxByte)));
//		assertFalse(maxByte.equals(minByte));
//		assertFalse(maxByte.equals(null));
//		assertFalse(zeroByte.equals(zeroShort));
//	}
//
//	public void testShortTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(maxShort), maxShort);
//		assertTagNotNullEquals(serializeAndDeserialize(minShort), minShort);
//		assertEquals(maxShort.toString(), "short:32767s");
//		assertThrowsException(maxShort::asByte, IllegalStateException.class);
//		assertThrowsNoException(maxShort::asShort);
//	}
//
//	public void testIntTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(maxInt), maxInt);
//		assertTagNotNullEquals(serializeAndDeserialize(minInt), minInt);
//		assertEquals(maxInt.toString(), "int:2147483647");
//		assertThrowsException(maxInt::asShort, IllegalStateException.class);
//		assertThrowsNoException(maxInt::asInt);
//	}
//
//	public void testLongTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(maxLong), maxLong);
//		assertTagNotNullEquals(serializeAndDeserialize(minLong), minLong);
//		assertEquals(maxLong.toString(), "long:9223372036854775807l");
//		assertThrowsException(maxLong::asInt, IllegalStateException.class);
//		assertThrowsNoException(maxLong::asLong);
//	}
//
//	public void testFloatTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(maxFloat), maxFloat);
//		assertTagNotNullEquals(serializeAndDeserialize(minFloat), minFloat);
//		assertTagNotNullEquals(serializeAndDeserialize(decFloat), decFloat);
//		assertEquals(maxFloat.toString(), "float:3.4028235E38f");
//		assertThrowsException(maxFloat::asShort, IllegalStateException.class);
//		assertThrowsException(maxFloat::asInt, IllegalStateException.class);
//		assertThrowsNoException(maxFloat::asFloat);
//		assertThrowsNoException(maxFloat::asDouble);
//	}
//
//	public void testDoubleTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(maxDouble), maxDouble);
//		assertTagNotNullEquals(serializeAndDeserialize(minDouble), minDouble);
//		assertTagNotNullEquals(serializeAndDeserialize(decDouble), decDouble);
//		assertEquals(maxDouble.toString(), "double:1.7976931348623157E308d");
//		assertThrowsException(maxDouble::asFloat, IllegalStateException.class);
//		assertThrowsException(maxDouble::asLong, IllegalStateException.class);
//		assertThrowsNoException(maxDouble::asDouble);
//	}
//
//	public void testStringTag() throws Exception {
//		assertTagNotNullEquals(serializeAndDeserialize(string), string);
//		assertTagNotNullEquals(string, string.clone());
//		assertTrue(string != string.clone());
//		assertEquals(string.toString(), "\"" + string.getName() + "\":\"" + string.getValue() + "\"");
//	}
//
//	public void testByteArrayTag() throws Exception {
//		ByteArrayTag t = (ByteArrayTag) serializeAndDeserialize(byteArray);
//		assertNotNull(t);
//		assertEquals(t.getID(), byteArray.getID());
//		assertEquals(t.getName(), byteArray.getName());
//		assertTrue(Arrays.equals(t.getValue(), byteArray.getValue()));
//		assertEquals(t, byteArray);
//		assertTrue(t != t.clone());
//		assertTrue(t.getValue() != t.clone().getValue());
//		assertEquals(byteArray.toString(), "byteArray:[B;-128b,-2b,-1b,0b,1b,2b,127b]");
//	}
//
//	public void testIntArrayTag() throws Exception {
//		IntArrayTag t = (IntArrayTag) serializeAndDeserialize(intArray);
//		assertNotNull(t);
//		assertEquals(t.getID(), intArray.getID());
//		assertEquals(t.getName(), intArray.getName());
//		assertTrue(Arrays.equals(t.getValue(), intArray.getValue()));
//		assertTrue(t.equals(intArray));
//		assertTrue(t != t.clone());
//		assertTrue(t.getValue() != t.clone().getValue());
//		assertEquals(intArray.toString(), "intArray:[I;-2147483648,-2,-1,0,1,2,2147483647]");
//	}
//
//	public void testLongArrayTag() throws Exception {
//		LongArrayTag t = (LongArrayTag) serializeAndDeserialize(longArray);
//		assertNotNull(t);
//		assertEquals(t.getID(), longArray.getID());
//		assertEquals(t.getName(), longArray.getName());
//		assertTrue(Arrays.equals(t.getValue(), longArray.getValue()));
//		assertTrue(t.equals(longArray));
//		assertTrue(t != t.clone());
//		assertTrue(t.getValue() != t.clone().getValue());
//		assertEquals(longArray.toString(), "longArray:[L;-9223372036854775808l,-2l,-1l,0l,1l,2l,9223372036854775807l]");
//	}
//
//	public void testShortArrayTag() throws Exception {
//		ShortArrayTag.register();
//		ShortArrayTag t = (ShortArrayTag) serializeAndDeserialize(shortArray);
//		assertNotNull(t);
//		assertEquals(t.getID(), shortArray.getID());
//		assertEquals(t.getName(), shortArray.getName());
//		assertTrue(Arrays.equals(t.getValue(), shortArray.getValue()));
//		assertTrue(t.equals(shortArray));
//		assertTrue(t != t.clone());
//		assertTrue(t.getValue() != t.clone().getValue());
//		assertEquals(shortArray.toString(), "shortArray:[S;-32768s,-2s,-1s,0s,1s,2s,32767s]");
//	}
//
//	public void testStructTag() throws Exception {
//		StructTag.register();
//		StructTag struct = new StructTag("struct");
//		populateStructTag(struct);
//
//		//test cloning
//		assertEquals(struct, struct.clone());
//		assertFalse(struct == struct.clone());
//
//		StructTag struct2 = (StructTag) serializeAndDeserialize(struct);
//		assertEquals(struct, struct2);
//	}
//
//	public void testObjectTag() throws Exception {
//		ObjectTag.register();
//		ObjectTag<DummyObject> o = new ObjectTag<>("object", new DummyObject());
//		ObjectTag<DummyObject> c = o.clone();
//		assertTrue(o != c);
//		assertThrowsNoException(() -> wrappedSerializeAndDeserialize(o));
//		ObjectTag<DummyObject> o2 = new ObjectTag<>("nullObject", null);
//		assertThrowsNoException(() -> wrappedSerializeAndDeserialize(o2));
//	}
//
//	public void testListTag() throws Exception {
//		ListTag<?> byteList2 = (ListTag<?>) serializeAndDeserialize(byteList);
//		assertNotNull(byteList2);
//		ListTag<ByteTag> byteList3 = byteList2.asByteTagList();
//		assertEquals(byteList3.getID(), byteList.getID());
//		assertEquals(byteList3.getTypeID(), byteList.getTypeID());
//		assertEquals(byteList3.getName(), byteList.getName());
//		for (int i = 0; i < byteList.size(); i++) {
//			//ListTag ignores tag names, therefore only check values
//			assertEquals(byteList.get(i).asByte(), byteList3.get(i).asByte());
//		}
//		assertTrue(byteList3.(0));
//		assertFalse(byteList3.get(1).asBoolean());
//		assertTrue(byteList3.get(2).asBoolean());
//		assertFalse(byteList3.get(3).asBoolean());
//		assertEquals(byteList3.get(0).asByte(), byteList.get(0).asByte());
//
//		//equals should ignore tag names in list tags
//		assertTrue(byteList2.equals(byteList));
//		assertTrue(byteList != byteList.clone());
//		assertTrue(byteList.getValue() != byteList.clone().getValue());
//
//		assertEquals(byteList.toString(), TestUtil.readStringFromFile("test_list_toString.txt"));
//		assertEquals(byteList.toTagString(), TestUtil.readStringFromFile("test_list_toTagString.txt"));
//	}
//
//	public void testCustomListTag() throws Exception {
//		ShortArrayTag.register();
//		StructTag.register();
//
//		ListTag custom = new ListTag("custom", TagType.CUSTOM);
//
//		StructTag struct = new StructTag("struct");
//		populateStructTag(struct);
//		custom.add(shortArray);
//		assertThrowsException(() -> custom.add(struct), IllegalArgumentException.class);
//		assertThrowsException(() -> custom.add(maxByte), IllegalArgumentException.class);
//		assertThrowsNoException(() -> custom.add(shortArray));
//		assertThrowsNoException(() -> wrappedSerializeAndDeserialize(custom));
//		assertEquals(custom, serializeAndDeserialize(custom));
//	}
//
//	public void testCompoundTag() throws Exception {
//		CompoundTag compound = new CompoundTag("compound");
//		compound.clearOrdered();
//		populateCompoundTag(compound);
//
//		//test cloning
//		assertEquals(compound, compound.clone());
//		assertFalse(compound == compound.clone());
//
//		CompoundTag compound2 = (CompoundTag) serializeAndDeserialize(compound);
//		assertEquals(compound, compound2);
//
//		assertEquals(compound.toString(), TestUtil.readStringFromFile("test_compound_toString.txt"));
//		assertEquals(compound.toTagString(), TestUtil.readStringFromFile("test_compound_toTagString.txt"));
//	}
//
//	public void testCustomCompoundTag() throws Exception {
//		ShortArrayTag.register();
//		StructTag.register();
//		CompoundTag custom = new CompoundTag("compound");
//
//		StructTag struct = new StructTag("struct");
//		populateStructTag(struct);
//		custom.set(shortArray);
//		custom.set(struct);
//		assertThrowsNoException(() -> wrappedSerializeAndDeserialize(custom));
//		assertEquals(custom, serializeAndDeserialize(custom));
//	}
//
//	public void testNBTFileReader() {
//		Tag t = new NBTFileReader(TestUtil.RESOURCES_PATH + "test_compound_gzip.dat").read();
//		assertNotNull(t);
//		Tag t2 = new NBTFileReader(TestUtil.RESOURCES_PATH + "test_compound.dat").read();
//		assertNotNull(t2);
//	}
//
//	public void testNBTFileWriter() {
//		CompoundTag compound = new CompoundTag("compound");
//		populateCompoundTag(compound);
//		new NBTFileWriter(TestUtil.RESOURCES_PATH + "test_NBTFileWriter.dat").write(compound);
//		assertTrue(Files.exists(Paths.get(TestUtil.RESOURCES_PATH + "test_NBTFileWriter.dat")));
//	}
//
//	public void testCircularReference() {
//		CompoundTag t = new CompoundTag("c");
//		t.set(t);
//
//		assertThrowsException(t::toString, MaxDepthReachedException.class);
//		assertThrowsException(() -> wrappedSerializeAndDeserialize(t), MaxDepthReachedException.class);
//		assertThrowsException(t::toTagString, MaxDepthReachedException.class);
//
//
//		ListTag l = new ListTag("l", TagType.LIST);
//		l.add(l);
//
//		assertThrowsException(l::toString, MaxDepthReachedException.class);
//		assertThrowsException(() -> wrappedSerializeAndDeserialize(l), MaxDepthReachedException.class);
//		assertThrowsException(l::toTagString, MaxDepthReachedException.class);
//
//
//		CompoundTag b = new CompoundTag("base");
//		CompoundTag current = new CompoundTag("depth_0");
//		b.set(current);
//
//		//counting to a depth of Tag.MAX_DEPTH - 1
//		for (int i = 1; i < Tag.MAX_DEPTH - 1; i++) {
//			CompoundTag c = new CompoundTag("depth_" + i);
//			current.set(c);
//			c.set(new IntTag("randomInt", RANDOM.nextInt()));
//			current = c;
//		}
//
//		assertThrowsNoException(b::toString);
//		assertThrowsNoException(b::toTagString);
//		assertThrowsNoException(() -> wrappedSerializeAndDeserialize(b));
//	}
//
//	public void tearDown() throws Exception {
//		super.tearDown();
//		Files.deleteIfExists(Paths.get(TestUtil.RESOURCES_PATH + "test_NBTFileWriter.dat"));
//		TagType.unregisterAllCustomTags();
//	}
//
//	//only works with primitive tags
//	private void assertTagNotNullEquals(Tag t1, Tag t2) {
//		assertTrue(t1 != null && t2 != null);
//		assertEquals(t1.getType(), t2.getType());
//		if (t1.getName() != null && t2.getName() != null)
//			assertEquals(t1.getName(), t2.getName());
//		else
//			assertEquals(t1.getName(), t2.getName());
//		//compares reference if it's not a primitive
//		assertEquals(t1.getValue(), t2.getValue());
//	}
//
//	private Tag serializeAndDeserialize(Tag tag) throws Exception {
//		return serializeAndDeserialize(tag, false);
//	}
//
//	private Tag serializeAndDeserialize(Tag tag, boolean debugBytes) throws Exception {
//		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//		try (NBTOutputStream nbtOut = new NBTOutputStream(byteOut)) {
//			nbtOut.writeTag(tag);
//		}
//
//		if (debugBytes) {
//			System.out.println(Arrays.toString(byteOut.toByteArray()));
//		}
//
//		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
//		try (NBTInputStream nbtIn = new NBTInputStream(byteIn)) {
//			return nbtIn.readTag();
//		}
//	}
//
//	private Tag wrappedSerializeAndDeserialize(Tag tag) {
//		try {
//			return serializeAndDeserialize(tag);
//		} catch (RuntimeException ex) {
//			throw ex;
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}
}
