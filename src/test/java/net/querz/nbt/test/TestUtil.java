package net.querz.nbt.test;

import junit.framework.TestCase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestUtil {

	public static void assertThrowsException(Runnable r, Class<? extends Exception> e) {
		assertThrowsException(r, e, false);
	}

	public static void assertThrowsException(Runnable r, Class<? extends Exception> e, boolean printStackTrace) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			if (printStackTrace)
				ex.printStackTrace();
			TestCase.assertEquals(ex.getClass(), e);
		}
	}
	
	public static void assertThrowsException(Runnable r, boolean printStackTrace) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			if (printStackTrace)
				ex.printStackTrace();
		}
	}
	
	public static void assertThrowsNoException(Runnable r) {
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
			tmpFile = new File(tmpDir, System.currentTimeMillis() + name);
		}
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
		try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath())))
		{
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
