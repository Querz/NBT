package net.querz.io.seekable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SeekableByteArray implements SeekableData {

	private static final float DEFAULT_BUFFER_GROWTH_FACTOR = 0.75f;
	private static final int DEFAULT_INITIAL_CAPACITY = 8196;
	private static final Pattern modes = Pattern.compile("^(?:r|rw|wr)$");

	private final boolean write;
	private final byte[] writeBuffer = new byte[8];

	private byte[] data;
	private int ptr;

	public SeekableByteArray() {
		data = new byte[DEFAULT_INITIAL_CAPACITY];
		write = true;
	}

	public SeekableByteArray(byte[] data, String mode) {
		this.data = data;
		if (!modes.matcher(mode).matches()) {
			throw new IllegalArgumentException("invalid mode " + mode);
		}
		write = mode.contains("w");
	}

	public byte[] getBytes() {
		byte[] result = new byte[ptr];
		System.arraycopy(data, 0, result, 0, result.length);
		return result;
	}

	public byte[] getBuffer() {
		return data;
	}

	@Override
	public void seek(long pos) throws IOException {
		ensureCapacity(pos);
		ptr = (int) pos;
	}

	private void ensureCapacity(long newCapacity) throws IOException {
		if (newCapacity > Integer.MAX_VALUE) {
			throw new EOFException("cannot seek past " + Integer.MAX_VALUE);
		}
		if (newCapacity >= data.length) {
			if (write) {
				increaseBuffer((int) Math.min(Integer.MAX_VALUE, newCapacity * DEFAULT_BUFFER_GROWTH_FACTOR));
			} else {
				throw new EOFException("cannot seek past EOF in read mode");
			}
		}
	}

	@Override
	public long getPointer() {
		return ptr;
	}

	@Override
	public InputStream startInputStream() {
		return new ByteArrayInputStream(data, ptr, data.length - ptr);
	}

	@Override
	public void readFully(byte[] b) {
		System.arraycopy(data, ptr, b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int off, int len) {
		System.arraycopy(data, ptr, b, off, len);
	}

	@Override
	public int skipBytes(int n) {
		if (ptr + n > data.length) {
			int p = ptr;
			ptr = data.length;
			return data.length - p;
		}
		ptr += n;
		return n;
	}

	@Override
	public boolean readBoolean() {
		return readByte() != 0;
	}

	@Override
	public byte readByte() {
		return data[ptr++];
	}

	@Override
	public int readUnsignedByte() {
		return data[ptr++] & 0xFF;
	}

	@Override
	public int read() {
		return data[ptr++] & 0xFF;
	}

	@Override
	public short readShort() {
		return (short) ((read() << 8) + read());
	}

	@Override
	public int readUnsignedShort() {
		return (read() << 8) + read();
	}

	@Override
	public char readChar() {
		return (char) ((read() << 8) + read());
	}

	@Override
	public int readInt() {
		return (read() << 24) + (read() << 16) + (read() << 8) + read();
	}

	@Override
	public long readLong() {
		return ((long) readInt() << 32) + (readInt() & 0xFFFFFFFFL);
	}

	@Override
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public String readLine() throws IOException {
		StringBuilder sb = new StringBuilder();
		int c;
		boolean eol = false;

		while (!eol) {
			switch (c = read()) {
				case '\n':
					eol = true;
					break;
				case '\r':
					eol = true;
					long cur = getPointer();
					if (read() != '\n') {
						seek(cur);
					}
					break;
				default:
					sb.append((char) c);
					break;
			}
		}

		if (sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}

	@Override
	public String readUTF() throws IOException {
		return DataInputStream.readUTF(this);
	}

	private void increaseBuffer(int newCapacity) {
		byte[] newData = new byte[newCapacity];
		System.arraycopy(data, 0, newData, 0, data.length);
		data = newData;
	}

	private void writeNoCheck(int b) {
		data[ptr++] = (byte) b;
	}

	@Override
	public void write(int b) throws IOException {
		ensureCapacity(ptr);
		data[ptr++] = (byte) b;
	}

	@Override
	public void write(byte[] b) throws IOException {
		ensureCapacity(ptr + b.length);
		System.arraycopy(b, 0, data, ptr, b.length);
		ptr += b.length;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		ensureCapacity(ptr);
		System.arraycopy(b, off, data, ptr, len);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		write(v ? 1 : 0);
	}

	@Override
	public void writeByte(int v) throws IOException {
		write(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 8);
		writeBuffer[1] = (byte) v;
		write(writeBuffer, 0, 2);
	}

	@Override
	public void writeChar(int v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 8);
		writeBuffer[1] = (byte) v;
		write(writeBuffer, 0, 2);
	}

	@Override
	public void writeInt(int v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 24);
		writeBuffer[1] = (byte) (v >>> 16);
		writeBuffer[2] = (byte) (v >>> 8);
		writeBuffer[3] = (byte) v;
		write(writeBuffer, 0, 4);
	}

	@Override
	public void writeLong(long v) throws IOException {
		writeBuffer[0] = (byte) (v >>> 56);
		writeBuffer[1] = (byte) (v >>> 48);
		writeBuffer[2] = (byte) (v >>> 40);
		writeBuffer[3] = (byte) (v >>> 32);
		writeBuffer[4] = (byte) (v >>> 24);
		writeBuffer[5] = (byte) (v >>> 16);
		writeBuffer[6] = (byte) (v >>> 8);
		writeBuffer[7] = (byte) v;
		write(writeBuffer, 0, 8);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeInt(Float.floatToIntBits(v));
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	@Override
	public void writeBytes(String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			write((byte) s.charAt(i));
		}
	}

	@Override
	public void writeChars(String s) throws IOException {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int v = s.charAt(i);
			writeBuffer[0] = (byte) (v >>> 8);
			writeBuffer[1] = (byte) v;
			write(writeBuffer, 0, 2);
		}
	}

	@Override
	public void writeUTF(String s) throws IOException {
		write(s.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void close() {}
}
