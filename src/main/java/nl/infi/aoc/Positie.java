package nl.infi.aoc;

import java.util.Objects;

final class Positie {
	final Integer x;
	final Integer y;
	
	private Positie(Integer x, Integer y) {
		this.x = Objects.requireNonNull(x, "Expect non-null x value");
		this.y = Objects.requireNonNull(y, "Expect non-null y value");
	}
	
	public static Positie of(Integer x, Integer y) {
		return new Positie(x, y);
	}
	
	public Positie add(Positie pos) {
		Objects.requireNonNull(pos);
		return Positie.of(this.x + pos.x, this.y + pos.y);
	}

	@Override
	public String toString() {
		return "(x=" + x + ", y=" + y + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x.hashCode();
		result = prime * result + y.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		final Positie other = (Positie) obj;
		return this.x.equals(other.x) && this.y.equals(other.y);
	}
}