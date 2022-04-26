package net.querz.mca.parsers;

public record EntityLocation(double x, double y, double z) {

	public EntityLocation() {
		this(0, 0, 0);
	}
}
