package de.querz.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NBTInputStream implements Closeable {
	protected final DataInputStream dis;
	
	public NBTInputStream(InputStream is) {
		dis = new DataInputStream(is);
	}
	
	public Tag readTag() throws IOException {
		return Tag.deserializeTag(this);
	}
	
	@Override
	public void close() throws IOException {
		dis.close();
	}
}
