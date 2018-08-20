package net.querz.nbt.test;

import junit.framework.TestCase;

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
}
