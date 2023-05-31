package net.querz.nbt;

public sealed abstract class NumberTag implements Tag permits ByteTag, DoubleTag, FloatTag, IntTag, LongTag, ShortTag {

	public abstract byte asByte();

	public abstract short asShort();

	public abstract int asInt();

	public abstract long asLong();

	public abstract float asFloat();

	public abstract double asDouble();

	public abstract Number asNumber();

	public abstract String toString();

}
