class Position {
	int x;
	int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(Position other) {
		this.x += other.x;
		this.y += other.y;
	}
}