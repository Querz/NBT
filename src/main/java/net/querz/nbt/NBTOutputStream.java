package net.querz.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NBTOutputStream implements Closeable {
	protected final DataOutputStream dos;
	
	public NBTOutputStream(OutputStream os) {
		dos = new DataOutputStream(os);
	}
	
	public void writeTag(Tag tag) throws IOException {
		tag.serializeTag(this);
	}
	
	@Override
	public void close() throws IOException {
		dos.close();
	}
	
	public DataOutputStream getDataOutputStream() {
		return dos;
	}
}
