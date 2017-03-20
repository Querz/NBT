package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.util.NBTUtil;

public class NBTUtilTest extends TestCase {
	
	private byte[] byteArray = new byte[] {Byte.MIN_VALUE, -2, -1, 0, 1, 2, Byte.MAX_VALUE};
	private short[] shortArray = new short[] {Short.MIN_VALUE, -2, -1, 0, 1, 2, Short.MAX_VALUE};
	private int[] intArray = new int[] {Integer.MIN_VALUE, -2, -1, 0, 1, 2, Integer.MAX_VALUE};
	
	public void testJoinArray() {
		assertEquals(NBTUtil.joinArray(",", byteArray), Byte.MIN_VALUE + ",-2,-1,0,1,2," + Byte.MAX_VALUE);
		assertEquals(NBTUtil.joinArray(",", shortArray), Short.MIN_VALUE + ",-2,-1,0,1,2," + Short.MAX_VALUE);
		assertEquals(NBTUtil.joinArray(",", intArray), Integer.MIN_VALUE + ",-2,-1,0,1,2," + Integer.MAX_VALUE);
	}
}
