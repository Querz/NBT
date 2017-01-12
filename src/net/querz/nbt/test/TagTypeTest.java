package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.TagType;
import net.querz.nbt.custom.ShortArrayTag;
import static net.querz.nbt.test.TestUtil.*;

public class TagTypeTest extends TestCase {
	public void testExceptions() {
		assertThrowsException(() -> TagType.getCustomTag(-1), IllegalArgumentException.class);
		assertThrowsException(() -> TagType.CUSTOM.getTag(), IllegalArgumentException.class);
		assertThrowsException(() -> TagType.match(-1), IllegalArgumentException.class);
		TagType.registerCustomTag(100, ShortArrayTag.class);
		assertThrowsException(() -> TagType.registerCustomTag(100, ShortArrayTag.class), IllegalArgumentException.class);
	}
	
	public void testCustomTags() {
		
	}
}
