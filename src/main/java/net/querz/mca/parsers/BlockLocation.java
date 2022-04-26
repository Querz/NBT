package net.querz.mca.parsers;

public record BlockLocation(int x, int y, int z) {

	public BlockLocation() {
		this(0, 0, 0);
	}
}
