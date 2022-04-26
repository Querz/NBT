package net.querz.nbt;

import net.querz.NBTTestCase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestByteTag extends NBTTestCase {

	@Test
	public void testCache() {
		ByteTag bt = ByteTag.valueOf((byte) -128);
		assertEquals(-128, (byte) getField(bt, "value", Byte.class));

	}
}
