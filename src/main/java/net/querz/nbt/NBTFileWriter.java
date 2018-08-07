package net.querz.nbt;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

public class NBTFileWriter {
	private File file;
	
	public NBTFileWriter(File file) {
		this.file = file;
	}
	
	public NBTFileWriter(String file) {
		this(new File(file));
	}
	
	public void write(Tag tag) {
		write(tag, true);
	}
	
	public void write(Tag tag, boolean gzip) {
		try (
			FileOutputStream fileOut = new FileOutputStream(file);
			GZIPOutputStream gzipOut = gzip ? new GZIPOutputStream(fileOut) : null;
			NBTOutputStream nbtOut = new NBTOutputStream(gzip ? gzipOut : fileOut)
		) {
			nbtOut.writeTag(tag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
