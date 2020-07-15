package net.querz.nbt.io;

import net.querz.io.ExceptionBiFunction;
import net.querz.io.MaxDepthIO;
import net.querz.nbt.tag.ByteArrayTag;
import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.EndTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.IntArrayTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.LongArrayTag;
import net.querz.nbt.tag.LongTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LittleEndianNBTInputStream implements DataInput, NBTInput, MaxDepthIO, Closeable {

	private final DataInputStream input;

	private static Map<Byte, ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException>> readers = new HashMap<>();
	private static Map<Byte, Class<?>> idClassMapping = new HashMap<>();

	static {
		put(EndTag.ID, (i, d) -> EndTag.INSTANCE, EndTag.class);
		put(ByteTag.ID, (i, d) -> readByte(i), ByteTag.class);
		put(ShortTag.ID, (i, d) -> readShort(i), ShortTag.class);
		put(IntTag.ID, (i, d) -> readInt(i), IntTag.class);
		put(LongTag.ID, (i, d) -> readLong(i), LongTag.class);
		put(FloatTag.ID, (i, d) -> readFloat(i), FloatTag.class);
		put(DoubleTag.ID, (i, d) -> readDouble(i), DoubleTag.class);
		put(ByteArrayTag.ID, (i, d) -> readByteArray(i), ByteArrayTag.class);
		put(StringTag.ID, (i, d) -> readString(i), StringTag.class);
		put(ListTag.ID, LittleEndianNBTInputStream::readListTag, ListTag.class);
		put(CompoundTag.ID, LittleEndianNBTInputStream::readCompound, CompoundTag.class);
		put(IntArrayTag.ID, (i, d) -> readIntArray(i), IntArrayTag.class);
		put(LongArrayTag.ID, (i, d) -> readLongArray(i), LongArrayTag.class);
	}

	private static void put(byte id, ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException> reader, Class<?> clazz) {
		readers.put(id, reader);
		idClassMapping.put(id, clazz);
	}

	public LittleEndianNBTInputStream(InputStream in) {
		input = new DataInputStream(in);
	}

	public LittleEndianNBTInputStream(DataInputStream in) {
		input = in;
	}

	public NamedTag readTag(int maxDepth) throws IOException {
		byte id = readByte();
		return new NamedTag(readUTF(), readTag(id, maxDepth));
	}

	public Tag<?> readRawTag(int maxDepth) throws IOException {
		byte id = readByte();
		return readTag(id, maxDepth);
	}

	private Tag<?> readTag(byte type, int maxDepth) throws IOException {
		ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException> f;
		if ((f = readers.get(type)) == null) {
			throw new IOException("invalid tag id \"" + type + "\"");
		}
		return f.accept(this, maxDepth);
	}

	private static ByteTag readByte(LittleEndianNBTInputStream in) throws IOException {
		return new ByteTag(in.readByte());
	}

	private static ShortTag readShort(LittleEndianNBTInputStream in) throws IOException {
		return new ShortTag(in.readShort());
	}

	private static IntTag readInt(LittleEndianNBTInputStream in) throws IOException {
		return new IntTag(in.readInt());
	}

	private static LongTag readLong(LittleEndianNBTInputStream in) throws IOException {
		return new LongTag(in.readLong());
	}

	private static FloatTag readFloat(LittleEndianNBTInputStream in) throws IOException {
		return new FloatTag(in.readFloat());
	}

	private static DoubleTag readDouble(LittleEndianNBTInputStream in) throws IOException {
		return new DoubleTag(in.readDouble());
	}

	private static StringTag readString(LittleEndianNBTInputStream in) throws IOException {
		return new StringTag(in.readUTF());
	}

	private static ByteArrayTag readByteArray(LittleEndianNBTInputStream in) throws IOException {
		ByteArrayTag bat = new ByteArrayTag(new byte[in.readInt()]);
		in.readFully(bat.getValue());
		return bat;
	}

	private static IntArrayTag readIntArray(LittleEndianNBTInputStream in) throws IOException {
		int l = in.readInt();
		int[] data = new int[l];
		IntArrayTag iat = new IntArrayTag(data);
		for (int i = 0; i < l; i++) {
			data[i] = in.readInt();
		}
		return iat;
	}

	private static LongArrayTag readLongArray(LittleEndianNBTInputStream in) throws IOException {
		int l = in.readInt();
		long[] data = new long[l];
		LongArrayTag iat = new LongArrayTag(data);
		for (int i = 0; i < l; i++) {
			data[i] = in.readLong();
		}
		return iat;
	}

	private static ListTag<?> readListTag(LittleEndianNBTInputStream in, int maxDepth) throws IOException {
		byte listType = in.readByte();
		ListTag<?> list = ListTag.createUnchecked(idClassMapping.get(listType));
		int length = in.readInt();
		if (length < 0) {
			length = 0;
		}
		for (int i = 0; i < length; i++) {
			list.addUnchecked(in.readTag(listType, in.decrementMaxDepth(maxDepth)));
		}
		return list;
	}

	private static CompoundTag readCompound(LittleEndianNBTInputStream in, int maxDepth) throws IOException {
		CompoundTag comp = new CompoundTag();
		for (int id = in.readByte() & 0xFF; id != 0; id = in.readByte() & 0xFF) {
			String key = in.readUTF();
			Tag<?> element = in.readTag((byte) id, in.decrementMaxDepth(maxDepth));
			comp.put(key, element);
		}
		return comp;
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		input.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		input.readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return input.skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return input.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return input.readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return input.readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return Short.reverseBytes(input.readShort());
	}

	public int readUnsignedShort() throws IOException {
		return Short.toUnsignedInt(Short.reverseBytes(input.readShort()));
	}

	@Override
	public char readChar() throws IOException {
		return Character.reverseBytes(input.readChar());
	}

	@Override
	public int readInt() throws IOException {
		return Integer.reverseBytes(input.readInt());
	}

	@Override
	public long readLong() throws IOException {
		return Long.reverseBytes(input.readLong());
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(Integer.reverseBytes(input.readInt()));
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(Long.reverseBytes(input.readLong()));
	}

	@Override
	@Deprecated
	public String readLine() throws IOException {
		return input.readLine();
	}

	@Override
	public void close() throws IOException {
		input.close();
	}

	@Override
	public String readUTF() throws IOException {
		byte[] bytes = new byte[readUnsignedShort()];
		readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
