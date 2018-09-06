package net.querz.nbt.test;

import junit.framework.TestCase;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TestUtil {

	public static void assertThrowsException(ExceptionRunnable r, Class<? extends Exception> e) {
		assertThrowsException(r, e, false);
	}

	public static void assertThrowsException(ExceptionRunnable r, Class<? extends Exception> e, boolean printStackTrace) {
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

	public static <T, E extends Exception> T assertThrowsNoException(ExceptionRunnable<T, E> r) {
		try {
			return r.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			TestCase.fail("Threw exception " + ex.getClass().getName() + " with message \"" + ex.getMessage() + "\"");
		}
		return null;
	}

	public static void assertThrowsRuntimeException(Runnable r, Class<? extends Exception> e) {
		assertThrowsRuntimeException(r, e, false);
	}

	public static void assertThrowsRuntimeException(Runnable r, Class<? extends Exception> e, boolean printStackTrace) {
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
	
	public static void assertThrowsRuntimeException(Runnable r, boolean printStackTrace) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			if (printStackTrace) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void assertThrowsNoRuntimeException(Runnable r) {
		try {
			r.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			TestCase.fail("Threw exception " + ex.getClass().getName() + " with message \"" + ex.getMessage() + "\"");
		}
	}

	public static File getNewTmpFile(String name) {
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

	public static File copyResourceToTmp(String resource) {
		URL resFileURL = TestUtil.class.getClassLoader().getResource(resource);
		TestCase.assertNotNull(resFileURL);
		File resFile = new File(resFileURL.getFile());
		File tmpFile = getNewTmpFile(resource);
		assertThrowsNoException(() -> Files.copy(resFile.toPath(), tmpFile.toPath()));
		return tmpFile;
	}

	public static void cleanupTmpDir() {
		String workingDir = System.getProperty("user.dir");
		File tmpDir = new File(workingDir, "tmp");
		File[] tmpFiles = tmpDir.listFiles();
		if (tmpFiles != null && tmpFiles.length != 0) {
			for (File file : tmpFiles) {
				file.delete();
			}
		}
	}

	public static String calculateFileMD5(File file) {
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

	public static String byteArrayToHexString(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi);
	}
}
