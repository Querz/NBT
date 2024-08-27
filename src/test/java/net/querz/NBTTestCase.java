package net.querz;

import net.querz.nbt.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public abstract class NBTTestCase {

	public static File getResourceFile(Object o, String resource) {
		URL url = o.getClass().getClassLoader().getResource(o.getClass().getPackageName().replace('.', '/') + "/" + resource);
		assertNotNull(url);
		return new File(url.getFile());
	}

	public static RandomAccessFile getResourceAsRandomAccessFile(Object o, String resource) throws FileNotFoundException {
		File file = getResourceFile(o, resource);
		return new RandomAccessFile(file, "r");
	}

	public static String readResourceString(Object o, String resource) throws IOException {
		char[] buffer = new char[1024];
		StringBuilder out = new StringBuilder();
		try (Reader in = new InputStreamReader(o.getClass().getResourceAsStream(resource), StandardCharsets.UTF_8)) {
			for (int read; (read = in.read(buffer, 0, buffer.length)) > 0; ) {
				out.append(buffer, 0, read);
			}
		}
		return out.toString();
	}

	public static byte[] readResourceBytes(Object o, String resource) throws IOException {
		return o.getClass().getResourceAsStream(resource).readAllBytes();
	}

	public static InputStream resourceAsStream(Object o, String resource) {
		return o.getClass().getResourceAsStream(resource);
	}

	public static <E extends Throwable> void assertThrowsException(ExceptionRunnable<E> r, Class<? extends Exception> e) {
		assertThrowsException(r, e, false);
	}

	public static <E extends Throwable> void assertThrowsException(ExceptionRunnable<E> r, Class<? extends Exception> e, boolean printStackTrace) {
		try {
			r.run();
			fail("No exception was throws but expected " + e.getName());
		} catch (Throwable ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}
			assertEquals(ex.getClass(), e);
		}
	}

	public static <E extends Throwable> void assertThrowsNoException(ExceptionRunnable<E> r) {
		try {
			r.run();
		} catch (Throwable ex) {
			fail("An exception was thrown but expected none");
			ex.printStackTrace();
		}
	}

	public static <T, E extends Throwable> T assertThrowsNoException(ExceptionSupplier<T, E> r) {
		try {
			return r.run();
		} catch (Throwable ex) {
			fail("An exception was thrown but expected none");
			ex.printStackTrace();
		}
		return null;
	}

	public static void assertThrowsRuntimeException(Runnable r, Class<? extends RuntimeException> e) {
		assertThrowsRuntimeException(r, e, false);
	}

	public static void assertThrowsRuntimeException(Runnable r, Class<? extends RuntimeException> e, boolean printStackTrace) {
		try {
			r.run();
			fail("No runtime exception was throws but expected " + e.getName());
		} catch (RuntimeException ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}
			assertEquals(e, ex.getClass());
		}
	}

	public static void assertThrowsNoRuntimeException(Runnable r) {
		try {
			r.run();
		} catch (RuntimeException ex) {
			fail("A runtime exception was thrown but expected none");
			ex.printStackTrace();
		}
	}

	public static <T> T assertThrowsNoRuntimeException(Supplier<T> r) {
		try {
			return r.get();
		} catch (RuntimeException ex) {
			fail("A runtime exception was thrown but expected none");
			ex.printStackTrace();
		}
		return null;
	}

	public static String calculateFileMD5(File file) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			fail(ex.getMessage());
		}
		try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
			byte[] buffer = new byte[8192];
			int numRead;
			do {
				numRead = bis.read(buffer);
				if (numRead > 0) {
					md.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
		} catch (IOException ex) {
			fail(ex.getMessage());
		}
		return byteArrayToHexString(md.digest());
	}

	public static String byteArrayToHexString(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi);
	}

	public static String tagToCode(Tag tag, int c) {
		return tagToCode(tag, new AtomicInteger(c));
	}

	public static <T> T getField(Object o, String fieldName, Class<T> type) {
		try {
			Field field = o.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return type.cast(field.get(o));
		} catch (NoSuchFieldException e) {
			fail("unknown field " + fieldName + " in object of type " + o.getClass().getName());
		} catch (IllegalAccessException e) {
			fail("failed to access field " + fieldName + " in object of type " + o.getClass().getName());
		} catch (ClassCastException e) {
			fail("field " + fieldName + " in object of type " + o.getClass().getName() + " does not match type " + type.getName());
		}
		fail("failed to get value of field " + fieldName + " from object of type " + o.getClass().getName());
		return type.cast(null);
	}

	protected static String tagToCode(Tag tag, AtomicInteger c) {
		StringBuilder sb = new StringBuilder();

		switch (tag.getType()) {
			case END: break;
			case BYTE:
				sb.append("ByteTag byteTag").append(c).append(" = ByteTag.valueOf((byte) ").append(((ByteTag) tag).asByte()).append(");");
				break;
			case SHORT:
				sb.append("ShortTag shortTag").append(c).append(" = ShortTag.valueOf((short) ").append(((ShortTag) tag).asShort()).append(");");
				break;
			case INT:
				sb.append("IntTag intTag").append(c).append(" = IntTag.valueOf(").append(((IntTag) tag).asInt()).append(");");
				break;
			case LONG:
				sb.append("LongTag longTag").append(c).append(" = LongTag.valueOf(").append(((LongTag) tag).asLong()).append("L);");
				break;
			case FLOAT:
				sb.append("FloatTag floatTag").append(c).append(" = FloatTag.valueOf(").append(((FloatTag) tag).asFloat()).append("F);");
				break;
			case DOUBLE:
				sb.append("DoubleTag doubleTag").append(c).append(" = DoubleTag.valueOf(").append(((DoubleTag) tag).asDouble()).append("D);");
				break;
			case BYTE_ARRAY:
				String asStringBytes = Arrays.toString(((ByteArrayTag) tag).getValue());
				sb.append("ByteArrayTag byteArrayTag").append(c).append(" = new ByteArrayTag(new byte[]{").append(asStringBytes, 1, asStringBytes.length() - 1).append("});");
				break;
			case STRING:
				sb.append("StringTag stringTag").append(c).append(" = StringTag.valueOf(\"").append(((StringTag) tag).getValue().replace("\\", "\\\\").replace("\"", "\\\"")).append("\");");
				break;
			case LIST:
				String name = "listTag" + c.getAndIncrement();
				sb.append("ListTag ").append(name).append(" = new ListTag();").append(((ListTag) tag).isEmpty() ? "" : "\n");;
				int i = ((ListTag) tag).size();
				for (Tag e : (ListTag) tag) {
					c.incrementAndGet();
					String es = tagToCode(e, c);
					sb.append(es).append("\n");
					String val = es.split(" ", 3)[1];
					sb.append(name).append(".add(").append(val).append(");").append(--i > 0 ? "\n" : "");
				}
				break;
			case COMPOUND:
				String nameCompound = "compoundTag" + c.getAndIncrement();
				sb.append("CompoundTag ").append(nameCompound).append(" = new CompoundTag();").append(((CompoundTag) tag).isEmpty() ? "" : "\n");
				int j = ((CompoundTag) tag).size();
				for (Map.Entry<String, Tag> e : (CompoundTag) tag) {
					c.incrementAndGet();
					String es = tagToCode(e.getValue(), c);
					sb.append(es).append("\n");
					String val = es.split(" ", 3)[1];
					sb.append(nameCompound).append(".put(\"").append(e.getKey().replace("\\", "\\\\").replace("\"", "\\\"")).append("\", ").append(val).append(");").append(--j > 0 ? "\n" : "");
				}
				break;
			case INT_ARRAY:
				String asStringInts = Arrays.toString(((IntArrayTag) tag).getValue());
				sb.append("IntArrayTag intArrayTag").append(c).append(" = new IntArrayTag(new int[]{").append(asStringInts, 1, asStringInts.length() - 1).append("});");
				break;
			case LONG_ARRAY:
				sb.append("LongArrayTag longArrayTag").append(c).append(" = new LongArrayTag(new long[]{");
				long[] value = ((LongArrayTag) tag).getValue();
				for (int k = 0; k < value.length; k++) {
					sb.append(k == 0 ? "" : ", ").append(value[k]).append("L");
				}
				sb.append("});");
				break;
		}
		return sb.toString();
	}

	public static class TestTagVisitor implements TagVisitor {

		@Override
		public void visit(ByteTag t) {

		}

		@Override
		public void visit(ShortTag t) {

		}

		@Override
		public void visit(IntTag t) {

		}

		@Override
		public void visit(LongTag t) {

		}

		@Override
		public void visit(FloatTag t) {

		}

		@Override
		public void visit(DoubleTag t) {

		}

		@Override
		public void visit(StringTag t) {

		}

		@Override
		public void visit(ByteArrayTag t) {

		}

		@Override
		public void visit(IntArrayTag t) {

		}

		@Override
		public void visit(LongArrayTag t) {

		}

		@Override
		public void visit(ListTag t) {

		}

		@Override
		public void visit(CompoundTag t) {

		}

		@Override
		public void visit(EndTag t) {

		}
	}
}
