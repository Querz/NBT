package net.querz.nbt.util;

public class Array {
	private static RuntimeException badArray(Object array) {
		if (array == null)
			return new NullPointerException("Array argument is null");
		else if (!array.getClass().isArray())
			return new IllegalArgumentException("Argument is not an array");
		else
			return new IllegalArgumentException("Array is of incompatible type");
	}

	public static int getLength(Object array) {
		if (array instanceof Object[])
			return ((Object[]) array).length;
		if (array instanceof boolean[])
			return ((boolean[]) array).length;
		if (array instanceof byte[])
			return ((byte[]) array).length;
		if (array instanceof char[])
			return ((char[]) array).length;
		if (array instanceof short[])
			return ((short[]) array).length;
		if (array instanceof int[])
			return ((int[]) array).length;
		if (array instanceof long[])
			return ((long[]) array).length;
		if (array instanceof float[])
			return ((float[]) array).length;
		if (array instanceof double[])
			return ((double[]) array).length;
		throw badArray(array);
	}

	public static boolean getBoolean(Object array, int index) {
		if (array instanceof boolean[])
			return ((boolean[]) array)[index];
		throw badArray(array);
	}

	public static byte getByte(Object array, int index) {
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		throw badArray(array);
	}

	public static char getChar(Object array, int index) {
		if (array instanceof char[])
			return ((char[]) array)[index];
		throw badArray(array);
	}

	public static short getShort(Object array, int index) {
		if (array instanceof short[])
			return ((short[]) array)[index];
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		throw badArray(array);
	}

	public static int getInt(Object array, int index) {
		if (array instanceof int[])
			return ((int[]) array)[index];
		if (array instanceof short[])
			return ((short[]) array)[index];
		if (array instanceof char[])
			return ((char[]) array)[index];
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		throw badArray(array);
	}

	public static long getLong(Object array, int index) {
		if (array instanceof long[])
			return ((long[]) array)[index];
		if (array instanceof int[])
			return ((int[]) array)[index];
		if (array instanceof short[])
			return ((short[]) array)[index];
		if (array instanceof char[])
			return ((char[]) array)[index];
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		throw badArray(array);
	}

	public static float getFloat(Object array, int index) {
		if (array instanceof float[])
			return ((float[]) array)[index];
		if (array instanceof long[])
			return ((long[]) array)[index];
		if (array instanceof int[])
			return ((int[]) array)[index];
		if (array instanceof short[])
			return ((short[]) array)[index];
		if (array instanceof char[])
			return ((char[]) array)[index];
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		throw badArray(array);
	}

	public static double getDouble(Object array, int index) {
		if (array instanceof double[])
			return ((double[]) array)[index];
		if (array instanceof float[])
			return ((float[]) array)[index];
		if (array instanceof long[])
			return ((long[]) array)[index];
		if (array instanceof int[])
			return ((int[]) array)[index];
		if (array instanceof short[])
			return ((short[]) array)[index];
		if (array instanceof char[])
			return ((char[]) array)[index];
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		throw badArray(array);
	}

	public static Object get(Object array, int index) {
		if (array instanceof Object[])
			return ((Object[]) array)[index];
		if (array instanceof boolean[])
			return ((boolean[]) array)[index];
		if (array instanceof byte[])
			return ((byte[]) array)[index];
		if (array instanceof char[])
			return ((char[]) array)[index];
		if (array instanceof short[])
			return ((short[]) array)[index];
		if (array instanceof int[])
			return ((int[]) array)[index];
		if (array instanceof long[])
			return ((long[]) array)[index];
		if (array instanceof float[])
			return ((float[]) array)[index];
		if (array instanceof double[])
			return ((double[]) array)[index];
		throw badArray(array);
	}

	public static void setBoolean(Object array, int index, boolean z) {
		if (array instanceof boolean[])
			((boolean[]) array)[index] = z;
		else
			throw badArray(array);
	}

	public static void setByte(Object array, int index, byte b) {
		if (array instanceof byte[])
			((byte[]) array)[index] = b;
		else if (array instanceof short[])
			((short[]) array)[index] = b;
		else if (array instanceof int[])
			((int[]) array)[index] = b;
		else if (array instanceof long[])
			((long[]) array)[index] = b;
		else if (array instanceof float[])
			((float[]) array)[index] = b;
		else if (array instanceof double[])
			((double[]) array)[index] = b;
		else
			throw badArray(array);
	}

	public static void setChar(Object array, int index, char c) {
		if (array instanceof char[])
			((char[]) array)[index] = c;
		else if (array instanceof int[])
			((int[]) array)[index] = c;
		else if (array instanceof long[])
			((long[]) array)[index] = c;
		else if (array instanceof float[])
			((float[]) array)[index] = c;
		else if (array instanceof double[])
			((double[]) array)[index] = c;
		else
			throw badArray(array);
	}

	public static void setShort(Object array, int index, short s) {
		if (array instanceof short[])
			((short[]) array)[index] = s;
		else if (array instanceof int[])
			((int[]) array)[index] = s;
		else if (array instanceof long[])
			((long[]) array)[index] = s;
		else if (array instanceof float[])
			((float[]) array)[index] = s;
		else if (array instanceof double[])
			((double[]) array)[index] = s;
		else
			throw badArray(array);
	}

	public static void setInt(Object array, int index, int i) {
		if (array instanceof int[])
			((int[]) array)[index] = i;
		else if (array instanceof long[])
			((long[]) array)[index] = i;
		else if (array instanceof float[])
			((float[]) array)[index] = i;
		else if (array instanceof double[])
			((double[]) array)[index] = i;
		else
			throw badArray(array);
	}

	public static void setLong(Object array, int index, long l) {
		if (array instanceof long[])
			((long[]) array)[index] = l;
		else if (array instanceof float[])
			((float[]) array)[index] = l;
		else if (array instanceof double[])
			((double[]) array)[index] = l;
		else
			throw badArray(array);
	}

	public static void setFloat(Object array, int index, float f) {
		if (array instanceof float[])
			((float[]) array)[index] = f;
		else if (array instanceof double[])
			((double[]) array)[index] = f;
		else
			throw badArray(array);
	}

	public static void setDouble(Object array, int index, double d) {
		if (array instanceof double[])
			((double[]) array)[index] = d;
		else
			throw badArray(array);
	}

	public static void set(Object array, int index, Object value) {
		if (array instanceof Object[]) {
			try {
				((Object[]) array)[index] = value;
			} catch (ArrayStoreException e) {
				throw badArray(array);
			}
		} else if (value instanceof Boolean)
			setBoolean(array, index, (boolean) value);
		else if (value instanceof Byte)
			setByte(array, index, (byte) value);
		else if (value instanceof Short)
			setShort(array, index, (short) value);
		else if (value instanceof Character)
			setChar(array, index, (char) value);
		else if (value instanceof Integer)
			setInt(array, index, (int) value);
		else if (value instanceof Long)
			setLong(array, index, (long) value);
		else if (value instanceof Float)
			setFloat(array, index, (float) value);
		else if (value instanceof Double)
			setDouble(array, index, (double) value);
		else
			throw badArray(array);
	}
}
