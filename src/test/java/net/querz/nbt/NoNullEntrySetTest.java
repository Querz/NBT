package net.querz.nbt;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class NoNullEntrySetTest extends NBTTestCase {

	public void testForwards() {
		Map<String, String> m = new TreeMap<>();
		m.put("foo", "bar");
		m.put("blah", "blubb");
		NonNullEntrySet<String, String> s = new NonNullEntrySet<>(m.entrySet());
		assertEquals(2, s.size());
		assertFalse(s.isEmpty());
		assertTrue(s.contains(new TreeMap.SimpleEntry<>("foo", "bar")));
		assertFalse(s.contains(new TreeMap.SimpleEntry<>("bar", "foo")));
		assertTrue(s.containsAll(Arrays.asList(new TreeMap.SimpleEntry<>("foo", "bar"), new TreeMap.SimpleEntry<>("blah", "blubb"))));
		assertFalse(s.containsAll(Arrays.asList(new TreeMap.SimpleEntry<>("foo", "bar"), new TreeMap.SimpleEntry<>("bar", "foo"))));
		assertEquals(2, s.toArray().length);
		assertEquals(2, s.toArray(new Object[0]).length);
		assertThrowsRuntimeException(() -> s.add(new TreeMap.SimpleEntry<>("faz", "baz")), UnsupportedOperationException.class);
		assertThrowsRuntimeException(() -> s.addAll(Arrays.asList(new TreeMap.SimpleEntry<>("fuz", "buz"), new TreeMap.SimpleEntry<>("faz", "baz"))), UnsupportedOperationException.class);
		assertTrue(assertThrowsNoRuntimeException(() -> s.remove(new TreeMap.SimpleEntry<>("foo", "bar"))));
		assertFalse(assertThrowsNoRuntimeException(() -> s.remove(new TreeMap.SimpleEntry<>("fuz", "baz"))));
		assertEquals(1, m.size());
		m.put("foo", "bar");
		assertEquals(2, s.size());
		assertTrue(assertThrowsNoRuntimeException(() -> s.removeAll(Arrays.asList(new TreeMap.SimpleEntry<>("foo", "bar"), new TreeMap.SimpleEntry<>("faz", "baz")))));
		assertEquals(1, m.size());
		m.put("foo", "bar");
		assertTrue(assertThrowsNoRuntimeException(() -> s.retainAll(Arrays.asList(new TreeMap.SimpleEntry<>("foo", "bar"), new TreeMap.SimpleEntry<>("bar", "foo")))));
		m.put("blah", "blubb");
		for (Map.Entry<String, String> e : s) {
			assertNotNull(e.getKey());
			assertNotNull(e.getValue());
			assertThrowsRuntimeException(() -> e.setValue(null), NullPointerException.class);
			assertThrowsNoRuntimeException(() -> e.setValue("kaz"));
			switch (e.getKey()) {
			case "foo":
				assertEquals(4386, e.hashCode());
				assertTrue(e.equals(new TreeMap.SimpleEntry<>("foo", "kaz")));
				break;
			case "blah":
				assertEquals(3125269, e.hashCode());
				assertTrue(e.equals(new TreeMap.SimpleEntry<>("blah", "kaz")));
				break;
			default:
				fail("unknown element in NoNullEntrySet");
			}
		}
		assertThrowsNoRuntimeException(s::clear);
		assertEquals(0, m.size());
	}
}
