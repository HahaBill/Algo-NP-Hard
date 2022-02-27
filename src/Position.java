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

	public Position getNext(EDirection dir) {
		switch(dir) {
			case UP: return new Position(this.x, this.y - 1);
			case DOWN: return new Position(this.x, this.y + 1);
			case LEFT: return new Position(this.x - 1, this.y);
			case RIGHT: return new Position(this.x + 1, this.y);
			default: return new Position(0, 0);
		}
	}
}