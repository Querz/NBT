package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.TagType;
import net.querz.nbt.custom.ShortArrayTag;

public class TagTypeTest extends TestCase {
	public void testExceptions() {
		TestUtil.assertThrowsException(() -> TagType.getCustomTag(-1), IllegalArgumentException.class);
		TestUtil.assertThrowsException(() -> TagType.CUSTOM.getTag(), IllegalArgumentException.class);
		TestUtil.assertThrowsException(() -> TagType.match(-1), IllegalArgumentException.class);
		TagType.registerCustomTag(100, ShortArrayTag.class);
		TestUtil.assertThrowsException(() -> TagType.registerCustomTag(100, ShortArrayTag.class), IllegalArgumentException.class);
	}
	
	public void testCustomTags() {
		
	}
}
