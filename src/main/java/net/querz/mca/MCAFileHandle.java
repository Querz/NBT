package net.querz.mca;

import net.querz.mca.seekable.SeekableData;
import net.querz.nbt.TagTypeVisitor;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public record MCAFileHandle(File directory, SeekableData seekableData, MCCFileHandler mccFileHandler, Supplier<TagTypeVisitor> tagTypeVisitorSupplier) implements AutoCloseable {

	@Override
	public void close() throws IOException {
		seekableData.close();
	}
}
