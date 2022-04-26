package net.querz.mca;

import java.io.*;

public interface SeekableData extends DataInput, DataOutput, Closeable {

	int read() throws IOException;

	void write(int b) throws IOException;

	void seek(long pos) throws IOException;

	long getPointer() throws IOException;

	InputStream startInputStream() throws IOException;
}
