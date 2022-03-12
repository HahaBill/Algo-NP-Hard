import game.actions.EDirection;

class Position {
	int x;
	int y;
	// random prime for the hash function
	final int MAGIC_PRIME = 1511;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Position getDisplaced(int x, int y, EDirection dir, int scale) {
		switch(dir) {
			case UP: return new Position(x, y - scale);
			case DOWN: return new Position(x, y + scale);
			case LEFT: return new Position(x - scale, y);
			case RIGHT: return new Position(x + scale, y);
			default: return new Position(0, 0);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Position)) return false;
		Position other = (Position) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		return this.x + this.y * MAGIC_PRIME;
	}
}