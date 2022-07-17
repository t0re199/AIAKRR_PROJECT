package data;

public final class Move {
	
	private Direction direction;

	private int row, col;

	public Move(int row, int col, Direction direction) {
		this.row = row;
		this.col = col;
		this.direction = direction;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Direction getDirection() {
		return direction;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (col != other.col)
			return false;
		if (direction != other.direction)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MOVE " + (char) ('G' - row) + (col + 1) + "," + direction;
	}
	
	public static Move parse(String move) {
		String[] splits = move.split(",");
		return new Move('G' - splits[0].charAt(0), 
						splits[0].charAt(1) - '1',
						Direction.valueOf(splits[1])); 
	}
}
