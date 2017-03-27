package net.querz.nbt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public class NBTFileReader {
	private static final byte[] GZIP_MAGIC_NUMBERS = {(byte) 0x1F, (byte) 0x8B};
	private File file;
	
	public NBTFileReader(File file) {
		this.file = file;
	}
	
	public NBTFileReader(String file) {
		this(new File(file));
	}
	
	public Tag read() {
		try (
			FileInputStream fileIn = new FileInputStream(file);
			NBTInputStream nbtIn = new NBTInputStream(decompress(fileIn))
		) {
			return nbtIn.readTag();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private InputStream decompress(InputStream is) throws IOException {
		PushbackInputStream pbis = new PushbackInputStream(is, 2);
		byte[] signature = new byte[2];
		pbis.read(signature);
		pbis.unread(signature);
		if (Arrays.equals(signature, GZIP_MAGIC_NUMBERS))
			return new GZIPInputStream(pbis);
		return pbis;
	}
}
