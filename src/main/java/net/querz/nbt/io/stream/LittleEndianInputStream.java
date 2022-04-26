package net.querz.nbt.io.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LittleEndianInputStream implements DataInput {

	private final DataInputStream in;

	public LittleEndianInputStream(InputStream in) {
		if (in instanceof DataInputStream) {
			this.in = (DataInputStream) in;
		} else {
			this.in = new DataInputStream(in);
		}
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		in.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		in.readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return in.skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return in.readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return in.readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return in.readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return Short.reverseBytes(in.readShort());
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return Short.toUnsignedInt(Short.reverseBytes(in.readShort()));
	}

	@Override
	public char readChar() throws IOException {
		return Character.reverseBytes(in.readChar());
	}

	@Override
	public int readInt() throws IOException {
		return Integer.reverseBytes(in.readInt());
	}

	@Override
	public long readLong() throws IOException {
		return Long.reverseBytes(in.readLong());
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(Long.reverseBytes(in.readLong()));
	}

	@Override
	@Deprecated
	public String readLine() throws IOException {
		return in.readLine();
	}

	@Override
	public String readUTF() throws IOException {
		byte[] bytes = new byte[readUnsignedShort()];
		readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
