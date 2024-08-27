package net.querz.mca;

import net.querz.io.seekable.SeekableData;
import net.querz.nbt.TagTypeVisitor;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

public class MCAFileHandle implements AutoCloseable {

	private final File directory;
	private final SeekableData seekableData;
	private final MCCFileHandler mccFileHandler;
	private final Supplier<TagTypeVisitor> tagTypeVisitorSupplier;

	public MCAFileHandle(File directory, SeekableData seekableData, MCCFileHandler mccFileHandler, Supplier<TagTypeVisitor> tagTypeVisitorSupplier) {
		this.directory = directory;
		this.seekableData = seekableData;
		this.mccFileHandler = mccFileHandler;
		this.tagTypeVisitorSupplier = tagTypeVisitorSupplier;
	}

	@Override
	public void close() throws IOException {
		seekableData.close();
	}

	public File directory() {
		return directory;
	}

	public SeekableData seekableData() {
		return seekableData;
	}

	public MCCFileHandler mccFileHandler() {
		return mccFileHandler;
	}

	public Supplier<TagTypeVisitor> tagTypeVisitorSupplier() {
		return tagTypeVisitorSupplier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MCAFileHandle that = (MCAFileHandle) o;
		return Objects.equals(directory, that.directory) && Objects.equals(seekableData, that.seekableData) && Objects.equals(mccFileHandler, that.mccFileHandler) && Objects.equals(tagTypeVisitorSupplier, that.tagTypeVisitorSupplier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(directory, seekableData, mccFileHandler, tagTypeVisitorSupplier);
	}

}
