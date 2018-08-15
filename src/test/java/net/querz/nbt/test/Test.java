package net.querz.nbt.test;

import junit.framework.TestCase;
import net.querz.nbt.ByteArrayTag;
import net.querz.nbt.ByteTag;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.DoubleTag;
import net.querz.nbt.FloatTag;
import net.querz.nbt.IntArrayTag;
import net.querz.nbt.ListTag;
import net.querz.nbt.StringTag;
import net.querz.nbt.Tag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;

public class Test extends TestCase {

	public void test() {
		ListTag<ByteTag> l = new ListTag<>("name");
		l.add(new ByteTag("b", (byte) 1));
		l.add(new ByteTag("b2", (byte) 2));
		System.out.println(l);
	}

	public void test2() {
		ByteTag b1 = new ByteTag("b1", (byte) 1);
		FloatTag f1 = new FloatTag("f1", 2.34F);
		ByteTag b2 = new ByteTag("b2", (byte) 2);

		System.out.println(b1.compareTo(b2));
		System.out.println(b2.compareTo(b1));
	}

	public void test3() throws IOException {
		CompoundTag c = new CompoundTag("compound");
		c.put(new ByteTag("b1", (byte) 2));
		c.put(new DoubleTag("d1", 0.2345));

		ListTag<ByteTag> l = new ListTag<>("name");
		l.add(new ByteTag("b", (byte) 1));
		l.add(new ByteTag("b2", (byte) 2));

		c.put(l);

		System.out.println(c);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		c.serialize(dos, 0);

		dos.close();

		byte[] data = baos.toByteArray();

		System.out.println(Arrays.toString(data));

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);

		Tag tag = Tag.deserialize(dis, 0);

		System.out.println(tag);

		System.out.println(c.getBoolean("d1"));
	}

	public void test4() {
		ByteArrayTag bat = new ByteArrayTag("bat1");
		ByteArrayTag bat2 = new ByteArrayTag("bat1");
		bat.setValue(null);
		IntArrayTag iat = new IntArrayTag("iat1");

		System.out.println(bat.equals(bat2));
	}

	public void testMutableName() {
		ByteTag b1 = new ByteTag("b1", (byte) 1);
		StringTag s1 = new StringTag("s1", "string");
		DoubleTag d1 = new DoubleTag("d1", Math.PI);

		CompoundTag c = new CompoundTag("compound");
		initCompoundTagWithLinkedHashMap(c);
		c.put(b1);
		c.put(s1);
		c.put(d1);
		System.out.println(c);
	}

	private void initCompoundTagWithLinkedHashMap(CompoundTag tag) {
		try {
			Method m = Tag.class.getDeclaredMethod("setValue", Object.class);
			m.setAccessible(true);
			m.invoke(tag, new LinkedHashMap<String, Tag>());
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			fail(e.getMessage());
		}
	}

	public void testEscape() {
		Random random = new Random();

		String f = "abcdefghijklmnopqrstuvwxyz0123456789";
		String e = "";
//		e = f;
		for (int i = 0; i < 100; i++) {
			e += f;
		}

		char[] invalidChars = new char[]{'\\', '\"', '\n', '\r', '\t'};

		long t = 0;
		for (int i = 0; i < 10000; i++) {
			String s = e;
			for (int j = 0; j < 50; j++) {
				int randomIndex = random.nextInt(e.length());
				String first = s.substring(0, randomIndex);
				String last = s.substring(randomIndex, e.length());
				s = first + invalidChars[random.nextInt(invalidChars.length)] + last;
			}
			long start = System.nanoTime();
//			String escaped = Tag.escapeString(s);
			t += System.nanoTime() - start;
//			System.out.println(escaped);
		}
		System.out.println(t + "ns");
		//334123292ns --> 0.3 sec
		//348197230ns
		//335784566ns
		//1435883455ns --> 1.4 sec
		//1414529504ns
		//1468419233ns

		//39351666ns --> 0.04 sec
		//40008463ns
		//38475999ns
		//155038819ns --> 0.15 sec
		//148221579ns
		//177940815ns

		//30409297ns --> 0.028 sec
		//27270969ns
		//29703712ns
		//131828425ns --> 0.13 sec
		//126128930ns
		//126263161ns

	}
}
