package net.querz.mca.parsers;

import java.util.Objects;

public class BlockLocation {

	private final int x;
	private final int y;
	private final int z;

	public BlockLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockLocation() {
		this(0, 0, 0);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int z() {
		return z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BlockLocation that = (BlockLocation) o;
		return x == that.x && y == that.y && z == that.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

}
