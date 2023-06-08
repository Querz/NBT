package net.querz.nbt;

import net.querz.NBTTestCase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCompoundTag extends NBTTestCase {

	@Test
	public void testPartOf() {
		CompoundTag t = new CompoundTag();
		t.putString("key", "value");
		CompoundTag c = new CompoundTag();
		c.putInt("int", 123);
		c.putLong("long", 1234L);
		t.put("comp", c);
		ListTag l = new ListTag();
		l.addInt(1);
		l.addInt(2);
		l.addInt(3);
		t.put("list", l);

		CompoundTag t2 = new CompoundTag();
		t2.putString("key", "value");
		t2.putString("key2", "value2");
		CompoundTag c2 = new CompoundTag();
		c2.putShort("short", (short) 123);
		c2.putInt("int", 123);
		c2.putLong("long", 1234L);
		t2.put("comp", c2);
		ListTag l2 = new ListTag();
		l2.addInt(0);
		l2.addInt(1);
		l2.addInt(2);
		l2.addInt(3);
		l2.addInt(4);
		t2.put("list", l2);

		assertTrue(t.partOf(t2));

		CompoundTag t3 = new CompoundTag();
		t3.putString("kex", "value");
		t3.putString("key2", "value2");
		CompoundTag c3 = new CompoundTag();
		c3.putShort("short", (short) 123);
		c3.putInt("int", 123);
		c3.putLong("long", 1234L);
		t3.put("comp", c3);
		ListTag l3 = new ListTag();
		l3.addInt(0);
		l3.addInt(1);
		l3.addInt(2);
		l3.addInt(3);
		l3.addInt(4);
		t3.put("list", l3);

		assertFalse(t.partOf(t3));

		CompoundTag t4 = new CompoundTag();
		t4.putString("key", "value");
		t4.putString("key2", "value2");
		CompoundTag c4 = new CompoundTag();
		c4.putShort("short", (short) 123);
		c4.putInt("int", 123);
		c4.putLong("long", 1234L);
		t4.put("comp", c4);
		ListTag l4 = new ListTag();
		l4.addInt(0);
		l4.addInt(1);
		l4.addInt(3);
		l4.addInt(4);
		t4.put("list", l4);

		assertFalse(t.partOf(t4));
	}
}
