package net.querz.nbt;

public abstract class NumberTag implements Tag {

	public abstract byte asByte();

	public abstract short asShort();

	public abstract int asInt();

	public abstract long asLong();

	public abstract float asFloat();

	public abstract double asDouble();

	public abstract Number asNumber();

	public abstract String toString();

}
