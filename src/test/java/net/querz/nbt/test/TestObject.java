package net.querz.nbt.test;

import java.io.Serializable;
import java.util.Random;

public class TestObject implements Serializable, Cloneable {
	private static final Random RANDOM = new Random();

	public byte a = (byte) RANDOM.nextInt(Byte.MAX_VALUE);
	public short b = (short) RANDOM.nextInt(Short.MAX_VALUE);
	public int c = RANDOM.nextInt();
	public long d = RANDOM.nextLong();
	public float e = RANDOM.nextFloat();
	public double f = RANDOM.nextDouble();

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getAsString() {
		return String.format("%d/%d/%d/%d/%f/%f",
				a, b, c, d, e, f
		);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TestObject) {
			TestObject t = (TestObject) other;
			return t.a == a
					&& t.b == b
					&& t.c == c
					&& t.d == d
					&& t.e == e
					&& t.f == f;
		}
		return false;
	}
}
