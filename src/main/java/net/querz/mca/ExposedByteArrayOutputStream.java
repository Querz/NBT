package net.querz.mca;

import java.io.ByteArrayOutputStream;

class ExposedByteArrayOutputStream extends ByteArrayOutputStream {

	public ExposedByteArrayOutputStream() {
		super();
	}

	public ExposedByteArrayOutputStream(int size) {
		super(size);
	}

	public byte[] getBuffer() {
		return buf;
	}
}
