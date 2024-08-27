package net.querz.mca.parsers;

import java.util.Objects;

public class EntityLocation {

	private final double x;
	private final double y;
	private final double z;

	public EntityLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public EntityLocation() {
		this(0, 0, 0);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EntityLocation that = (EntityLocation) o;
		return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0 && Double.compare(z, that.z) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

}
