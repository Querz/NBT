package net.querz.mca;

import java.io.File;
import java.io.IOException;

public record MCAFileHandle(File directory, SeekableData seekableData, MCCFileHandler mccFileHandler) implements AutoCloseable {

	@Override
	public void close() throws IOException {
		seekableData.close();
	}
}
