package net.querz.nbt.io;

import net.querz.NBTTestCase;

public class StringPointerTest extends NBTTestCase {

	public void testLookAhead() {
		StringPointer ptr = new StringPointer("abcdefg");
		char c = ptr.lookAhead(3);
		assertEquals('d', c);
		assertEquals('a', ptr.currentChar());
	}

	public void testSkip() {
		StringPointer ptr = new StringPointer("abcdefg");
		ptr.skip(3);
		assertEquals('d', ptr.currentChar());
	}

	public void testNext() {
		StringPointer ptr = new StringPointer("abcdefg");
		assertEquals('a', ptr.next());
		assertEquals('b', ptr.currentChar());
	}

	public void testCurrentChar() {
		StringPointer ptr = new StringPointer("abcdefg");
		assertEquals('a', ptr.currentChar());
		ptr.skip(3);
		assertEquals('d', ptr.currentChar());
	}

	public void testHasCharsLeft() {
		StringPointer ptr = new StringPointer("abcdefg");
		assertTrue(ptr.hasCharsLeft(1));
		assertTrue(ptr.hasCharsLeft(6));
		assertFalse(ptr.hasCharsLeft(7));
	}

	public void testHasNext() {
		StringPointer ptr = new StringPointer("abcdefg");
		assertTrue(ptr.hasNext());
		ptr.skip(6);
		assertTrue(ptr.hasNext());
		ptr.skip(1);
		assertFalse(ptr.hasNext());
	}

	public void testSkipWhitespace() {
		StringPointer ptr = new StringPointer("abc \t  defg");
		ptr.skip(3);
		assertEquals(' ', ptr.currentChar());
		ptr.skipWhitespace();
		assertEquals('d', ptr.currentChar());
	}

	public void testExpectChar() {
		StringPointer ptr = new StringPointer("abcdefg");
		assertThrowsNoException(() -> ptr.expectChar('a'));
		assertThrowsException(() -> ptr.expectChar('a'), ParseException.class);
	}

	public void testNextArrayElement() {
		StringPointer ptr = new StringPointer("1, 2, 3");
		ptr.next();
		assertTrue(ptr.nextArrayElement());
		ptr.next();
		assertTrue(ptr.nextArrayElement());
		ptr.next();
		assertFalse(ptr.nextArrayElement());
	}

	public void testParseQuotedString() {
		StringPointer ptr = new StringPointer("\"abcdefg\"");
		assertEquals("abcdefg", assertThrowsNoException(ptr::parseQuotedString));
		ptr = new StringPointer("\"abc\\def\"");
		assertThrowsException(ptr::parseQuotedString, ParseException.class);
		ptr = new StringPointer("\"abc\\\\def\\\\\"");
		assertEquals("abc\\def\\", assertThrowsNoException(ptr::parseQuotedString));
		ptr = new StringPointer("\"abc");
		assertThrowsException(ptr::parseQuotedString, ParseException.class);
	}

	public void testParseSimpleString() {
		StringPointer ptr = new StringPointer("abcdefg},{something else}");
		assertEquals("abcdefg", ptr.parseSimpleString());
		ptr = new StringPointer("abcdefg");
		assertEquals("abcdefg", ptr.parseSimpleString());
	}
}
