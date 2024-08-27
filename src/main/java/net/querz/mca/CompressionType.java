package net.querz.mca;

import java.io.*;
import java.util.zip.*;

public enum CompressionType {

	NONE_EXT(0x80, null,     CompressionType::buffered,                               CompressionType::buffered),
	GZIP_EXT(0x81, null,     GZIPInputStream::new,                                    CompressionType::buffered),
	ZLIB_EXT(0x82, null,     (i, s) -> new InflaterInputStream(i, new Inflater(), s), CompressionType::buffered),
	NONE    (0x0,  NONE_EXT,        CompressionType::buffered,                               CompressionType::buffered),
	GZIP    (0x1,  GZIP_EXT,        GZIPInputStream::new,                                    GZIPOutputStream::new),
	ZLIB    (0x2,  ZLIB_EXT,        (i, s) -> new InflaterInputStream(i, new Inflater(), s), (o, s) -> new DeflaterOutputStream(o, new Deflater(), s));

	private final CompressionType external;
	private final int id;
	private final CompressionTypeInputWrapper inputWrapper;
	private final CompressionTypeOutputWrapper outputWrapper;

	CompressionType(int id, CompressionType external, CompressionTypeInputWrapper inputWrapper, CompressionTypeOutputWrapper outputWrapper) {
		this.id = id;
		this.external = external;
		this.inputWrapper = inputWrapper;
		this.outputWrapper = outputWrapper;
	}

	public InputStream wrap(MCAFileHandle handle, int size) throws IOException {
		return inputWrapper.accept(handle.seekableData().startInputStream(), size);
	}

	public InputStream wrap(InputStream in, int size) throws IOException {
		return inputWrapper.accept(in, size);
	}

	public OutputStream wrap(OutputStream out, int size) throws IOException {
		return outputWrapper.accept(out, size);
	}

	public boolean isExternal() {
		return external == null;
	}

	public CompressionType getExternal() {
		if (external == null) {
			return this;
		}
		return external;
	}

	public int getID() {
		return id;
	}

	public static CompressionType fromByte(int id) throws IOException {
		switch (id) {
			case 0x0: return NONE;
			case 0x1: return GZIP;
			case 0x2: return ZLIB;
			case 0x80: return NONE_EXT;
			case 0x81: return GZIP_EXT;
			case 0x82: return ZLIB_EXT;
			default: throw new IOException("invalid compression type " + id);
		}
	}

	@FunctionalInterface
	private interface CompressionTypeInputWrapper {
		InputStream accept(InputStream in, int size) throws IOException;
	}

	@FunctionalInterface
	private interface CompressionTypeOutputWrapper {
		OutputStream accept(OutputStream out, int size) throws IOException;
	}

	private static OutputStream buffered(OutputStream out, int size) {
		if (out instanceof BufferedOutputStream) {
			return out;
		}
		return new BufferedOutputStream(out, size);
	}

	private static InputStream buffered(InputStream in, int size) {
		if (in instanceof BufferedInputStream) {
			return in;
		}
		return new BufferedInputStream(in, size);
	}
}
