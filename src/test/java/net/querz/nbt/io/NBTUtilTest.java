package net.querz.nbt.io;

import java.io.*;

import net.querz.nbt.io.NBTUtil.Reader;
import net.querz.nbt.io.NBTUtil.Writer;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class NBTUtilTest {

	public static final StringTag TAG = new StringTag("TEST");

	public static final boolean DEFAULT_COMPRESSED = true;
	public static final boolean DEFAULT_LITTLE_ENDIAN = false;

	protected static byte[] serialize(Tag<?> tag, boolean compressed, boolean littleEndian) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (DataOutputStream dos = new DataOutputStream(baos)) {
			new NBTSerializer(compressed, littleEndian).toStream(new NamedTag(null, tag), dos);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
		return baos.toByteArray();
	}

	@Test
	public void testWriter_invalidParams() throws IOException {
		try {
			Writer.write().tag((NamedTag) null).to(new ByteArrayOutputStream());
			fail("did not check null tag");
		} catch (IllegalStateException ignored) { }

		try {
			Writer.write().tag(TAG).to((OutputStream) null);
			fail("did not check null output stream");
		} catch (IllegalStateException ignored) { }
	}

	@Test
	public void testWriter_compressionEnabled() throws IOException {
		final boolean compressed = true;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		Writer.write().tag(TAG).compressed(compressed).to(os);

		assertArrayEquals(serialize(TAG, compressed, DEFAULT_LITTLE_ENDIAN), os.toByteArray());
	}

	@Test
	public void testWriter_compressionDisabled() throws IOException {
		final boolean compressed = false;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		Writer.write().tag(TAG).compressed(compressed).to(os);

		assertArrayEquals(serialize(TAG, compressed, DEFAULT_LITTLE_ENDIAN), os.toByteArray());
	}

	@Test
	public void testReader_compressionEnabled() throws IOException {
		final boolean compressed = true;
		final InputStream is = new ByteArrayInputStream(serialize(TAG, compressed, DEFAULT_LITTLE_ENDIAN));

		final NamedTag tag = Reader.read().from(is);

		assertEquals(TAG, tag.getTag());
	}

	@Test
	public void testReader_compressionDisabled() throws IOException {
		final boolean compressed = false;
		final InputStream is = new ByteArrayInputStream(serialize(TAG, compressed, DEFAULT_LITTLE_ENDIAN));

		final NamedTag tag = Reader.read().from(is);

		assertEquals(TAG, tag.getTag());
	}


	@Test
	public void testWriter_littleEndian() throws IOException {
		final boolean littleEndian = true;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		Writer.write().tag(TAG).littleEndian().to(os);

		assertArrayEquals(serialize(TAG, DEFAULT_COMPRESSED, littleEndian), os.toByteArray());
	}

	@Test
	public void testWriter_bigEndian() throws IOException {
		final boolean littleEndian = false;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		Writer.write().tag(TAG).bigEndian().to(os);

		assertArrayEquals(serialize(TAG, DEFAULT_COMPRESSED, littleEndian), os.toByteArray());
	}

	@Test
	public void testReader_littleEndian() throws IOException {
		final boolean littleEndian = true;
		final InputStream is = new ByteArrayInputStream(serialize(TAG, DEFAULT_COMPRESSED, littleEndian));

		final NamedTag tag = Reader.read().littleEndian().from(is);

		assertEquals(TAG, tag.getTag());
	}

	@Test
	public void testReader_bigEndian() throws IOException {
		final boolean littleEndian = false;
		final InputStream is = new ByteArrayInputStream(serialize(TAG, DEFAULT_COMPRESSED, littleEndian));

		final NamedTag tag = Reader.read().bigEndian().from(is);

		assertEquals(TAG, tag.getTag());
	}

}
