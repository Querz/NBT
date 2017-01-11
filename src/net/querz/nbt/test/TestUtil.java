package net.querz.nbt.test;

import junit.framework.TestCase;

public class TestUtil {
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
}
