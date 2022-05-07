package net.querz.mca;

import net.querz.mca.seekable.SeekableByteArray;
import net.querz.mca.seekable.SeekableFile;
import net.querz.nbt.io.stream.TagSelector;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCAFile implements Iterable<Chunk> {

	private static final Pattern filePattern = Pattern.compile("^r\\.(?<x>-?\\d+)\\.(?<z>-?\\d+)\\.mca$");

	private final int x;
	private final int z;
	private File file;

	private Chunk[] chunks;

	public MCAFile(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public MCAFile(File file) {
		Matcher m = filePattern.matcher(file.getName());
		if (m.find()) {
			x = Integer.parseInt(m.group("x"));
			z = Integer.parseInt(m.group("z"));
			this.file = file;
		} else {
			throw new IllegalArgumentException("invalid region file name " + file.getName());
		}
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public String getName() {
		return file == null ? "r." + x + "." + z + ".mca" : file.getName();
	}

	public void loadFromBytes(byte[] data, TagSelector... chunkTagSelectors) throws IOException {
		load(new MCAFileHandle(file.getParentFile(), new SeekableByteArray(data, "r"), MCCFileHandler.DEFAULT_HANDLER), chunkTagSelectors);
	}

	public void load(TagSelector... chunkTagSelectors) throws IOException {
		try (MCAFileHandle handle = new MCAFileHandle(file.getParentFile(), new SeekableFile(file, "r"), MCCFileHandler.DEFAULT_HANDLER)) {
			load(handle, chunkTagSelectors);
		}
	}

	public void load(MCAFileHandle handle, TagSelector... chunkTagSelectors) throws IOException {
		SeekableData s = handle.seekableData();
		chunks = new Chunk[1024];
		int ox = x << 5;
		int oz = z << 5;
		for (int i = 0; i < 1024; i++) {
			s.seek(i * 4);
			int offset = s.read() << 16;
			offset |= (s.read() & 0xFF) << 8;
			offset |= s.read() & 0xFF;
			if (s.read() == 0) {
				// skip if length of chunk in header is declared to be 0
				continue;
			}
			s.seek(4096L + i * 4);
			int timestamp = s.readInt();
			int cz = i >> 5;
			int cx = i - cz * 32;
			Chunk chunk = new Chunk(ox + cx, oz + cz, timestamp);
			s.seek(4096L * offset + 4);
			chunk.load(handle, chunkTagSelectors);
			chunks[i] = chunk;
		}
	}

	public byte[] saveToBytes() throws IOException {
		SeekableByteArray sba = new SeekableByteArray();
		save(new MCAFileHandle(file.getParentFile(), sba, MCCFileHandler.DEFAULT_HANDLER));
		return sba.getBytes();
	}

	public void save() throws IOException {
		try (MCAFileHandle handle = new MCAFileHandle(file.getParentFile(), new SeekableFile(file, "rw"), MCCFileHandler.DEFAULT_HANDLER)) {
			save(handle);
		}
	}

	public void save(MCAFileHandle handle) throws IOException {
		SeekableData s = handle.seekableData();
		int globalOffset = 2;
		int lastWritten = 0;

		s.seek(0);
		for (int i = 0; i < 1024; i++) {
			Chunk chunk = chunks[i];
			if (chunk == null || chunk.isEmpty()) {
				continue;
			}
			s.seek(globalOffset * 4096L);
			lastWritten = chunk.save(handle);

			int sectors = (lastWritten >> 12) + ((lastWritten & 4095) == 0 ? 0 : 1);

			// write offset and sector count
			s.seek(i * 4);
			s.write(globalOffset >>> 16);
			s.write(globalOffset >> 8 & 0xFF);
			s.write(globalOffset & 0xFF);
			s.write(sectors);

			// write timestamp
			s.seek(4096 + i * 4);
			s.writeInt(chunk.getTimestamp());

			globalOffset += sectors;
		}

		// padding
		if ((lastWritten & 4095) != 0) {
			s.seek(globalOffset * 4096L - 1);
			s.write(0);
		}
	}

	public static int timestamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	private int chunkCoordinatesToIndex(int chunkX, int chunkZ) {
		return (chunkX & 0x1F) + (chunkZ & 0x1F) * 32;
	}

	public Chunk getChunkAt(int chunkX, int chunkZ) {
		return getChunkAt(chunkCoordinatesToIndex(chunkX, chunkZ));
	}

	public Chunk getChunkAt(int index) {
		return chunks[index];
	}

	public void setChunkAt(Chunk chunk) {
		Objects.requireNonNull(chunk);
		setChunkAt(chunkCoordinatesToIndex(chunk.getX(), chunk.getZ()), chunk);
	}

	public void setChunkAt(int chunkX, int chunkZ, Chunk chunk) {
		setChunkAt(chunkCoordinatesToIndex(chunkX, chunkZ), chunk);
	}

	public void setChunkAt(int index, Chunk chunk) {
		chunks[index] = chunk;
	}

	@Override
	public Iterator<Chunk> iterator() {
		return Arrays.asList(chunks).iterator();
	}
}
