package net.querz.mca.seekable;

import net.querz.mca.SeekableData;
import java.io.*;

public class SeekableFile extends RandomAccessFile implements SeekableData {

	public SeekableFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public SeekableFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	@Override
	public long getPointer() throws IOException {
		return super.getFilePointer();
	}

	@Override
	public InputStream startInputStream() throws IOException {
		return new FileInputStream(getFD());
	}
}
