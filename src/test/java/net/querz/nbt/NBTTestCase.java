package net.querz.nbt;

import junit.framework.TestCase;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public abstract class NBTTestCase extends TestCase {

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		TagFactory.unregisterCustomTag(90);
		TagFactory.unregisterCustomTag(100);
		TagFactory.unregisterCustomTag(110);
		TagFactory.unregisterCustomTag(120);
		cleanupTmpDir();
	}

	protected byte[] serialize(Tag tag) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (DataOutputStream dos = new DataOutputStream(baos)) {
			tag.serialize(dos, 0);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
		return baos.toByteArray();
	}

	protected Tag deserialize(byte[] data) {
		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data))) {
			return Tag.deserialize(dis, 0);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
			return null;
		}
	}

	protected Tag deserializeFromFile(String f) {
		URL resource = getClass().getClassLoader().getResource(f);
		assertNotNull(resource);
		File file = new File(resource.getFile());
		try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
			return Tag.deserialize(dis, 0);
		} catch (IOException ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
			return null;
		}
	}

	protected <T> void invokeSetValue(Tag<T> tag, T value) {
		try {
			Class<?> c = tag.getClass();
			Method m;
			while (c != Object.class) {
				try {
					m = c.getDeclaredMethod("setValue", Object.class);
					m.setAccessible(true);
					m.invoke(tag, value);
					return;
				} catch (NoSuchMethodException ex) {
					c = c.getSuperclass();
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			fail("unable to invoke setValue() on " + tag.getClass().getSimpleName());
		}
		fail("could not find setValue()");
	}

	@SuppressWarnings("unchecked")
	protected <T> T invokeGetValue(Tag<T> tag) {
		try {
			Class<?> c = tag.getClass();
			Method m;
			while (c != Object.class) {
				try {
					m = c.getDeclaredMethod("getValue");
					m.setAccessible(true);
					return (T) m.invoke(tag);
				} catch (NoSuchMethodException ex) {
					c = c.getSuperclass();
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			fail("unable to invoke getValue() on " + tag.getClass().getSimpleName());
		}
		fail("could not find getValue()");
		return null;
	}



	protected void assertThrowsException(ExceptionRunnable r, Class<? extends Exception> e) {
		assertThrowsException(r, e, false);
	}

	protected void assertThrowsException(ExceptionRunnable r, Class<? extends Exception> e, boolean printStackTrace) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}
			TestCase.assertEquals(ex.getClass(), e);
		}
	}

	protected <T, E extends Exception> T assertThrowsNoException(ExceptionRunnable<T, E> r) {
		try {
			return r.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			TestCase.fail("Threw exception " + ex.getClass().getName() + " with message \"" + ex.getMessage() + "\"");
		}
		return null;
	}

	protected void assertThrowsRuntimeException(Runnable r, Class<? extends Exception> e) {
		assertThrowsRuntimeException(r, e, false);
	}

	protected void assertThrowsRuntimeException(Runnable r, Class<? extends Exception> e, boolean printStackTrace) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}
			TestCase.assertEquals(ex.getClass(), e);
		}
	}

	protected void assertThrowsRuntimeException(Runnable r, boolean printStackTrace) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}
		}
	}

	protected void assertThrowsNoRuntimeException(Runnable r) {
		try {
			r.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			TestCase.fail("Threw exception " + ex.getClass().getName() + " with message \"" + ex.getMessage() + "\"");
		}
	}

	protected File getNewTmpFile(String name) {
		String workingDir = System.getProperty("user.dir");
		File tmpDir = new File(workingDir, "tmp");
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		File tmpFile = new File(tmpDir, name);
		if (tmpFile.exists()) {
			tmpFile = new File(tmpDir, UUID.randomUUID() + name);
		}
		return tmpFile;
	}

	protected File copyResourceToTmp(String resource) {
		URL resFileURL = getClass().getClassLoader().getResource(resource);
		TestCase.assertNotNull(resFileURL);
		File resFile = new File(resFileURL.getFile());
		File tmpFile = getNewTmpFile(resource);
		assertThrowsNoException(() -> Files.copy(resFile.toPath(), tmpFile.toPath()));
		return tmpFile;
	}

	protected void cleanupTmpDir() {
		String workingDir = System.getProperty("user.dir");
		File tmpDir = new File(workingDir, "tmp");
		File[] tmpFiles = tmpDir.listFiles();
		if (tmpFiles != null && tmpFiles.length != 0) {
			for (File file : tmpFiles) {
				file.delete();
			}
		}
	}

	protected String calculateFileMD5(File file) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			TestCase.fail(ex.getMessage());
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
			TestCase.fail(ex.getMessage());
		}
		return byteArrayToHexString(md.digest());
	}

	protected String byteArrayToHexString(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi);
	}
}
