package net.querz.nbt.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import junit.framework.TestCase;

public class TestUtil {

	public static final String RESOURCES_PATH = "./src/test/java/net/querz/nbt/test/resources/";
	
	public static void assertThrowsException(Runnable r, Class<? extends Exception> e) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {
			TestCase.assertEquals(ex.getClass(), e);
		}
	}
	
	public static void assertThrowsException(Runnable r) {
		try {
			r.run();
			TestCase.fail();
		} catch (Exception ex) {}
	}
	
	public static void assertThrowsNoException(Runnable r) {
		try {
			r.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			TestCase.fail("Threw exception " + ex.getClass().getName() + " with message \"" + ex.getMessage() + "\"");
			
		}
	}
	
	public static String readStringFromFile(String file) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(RESOURCES_PATH + file));
		return lines.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
}
