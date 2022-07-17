package net.querz.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LittleEndianOutputStream implements DataOutput {

	public final DataOutputStream out;

	public LittleEndianOutputStream(OutputStream out) {
		if (out instanceof DataOutputStream) {
			this.out = (DataOutputStream) out;
		} else {
			this.out = new DataOutputStream(out);
		}
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		out.writeBoolean(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		out.writeByte(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		out.writeShort(Short.reverseBytes((short) v));
	}

	@Override
	public void writeChar(int v) throws IOException {
		out.writeChar(Character.reverseBytes((char) v));
	}

	@Override
	public void writeInt(int v) throws IOException {
		out.writeInt(Integer.reverseBytes(v));
	}

	@Override
	public void writeLong(long v) throws IOException {
		out.writeLong(Long.reverseBytes(v));
	}

	@Override
	public void writeFloat(float v) throws IOException {
		out.writeInt(Integer.reverseBytes(Float.floatToIntBits(v)));
	}

	@Override
	public void writeDouble(double v) throws IOException {
		out.writeLong(Long.reverseBytes(Double.doubleToLongBits(v)));
	}

	@Override
	public void writeBytes(String s) throws IOException {
		out.writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		out.writeChars(s);
	}

	@Override
	public void writeUTF(String s) throws IOException {
		byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
		writeShort(bytes.length);
		out.write(bytes);
	}
}
