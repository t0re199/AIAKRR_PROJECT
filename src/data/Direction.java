package data;

public enum Direction {
	
	N(1, 0),

	NE(1, 1),

	NW(1, -1),

	E(0, 1),

	S(-1, 0),

	SE(-1, 1),

	SW(-1, -1),

	W(0, -1);

	private int rowIncrement, colIncrement;

	Direction(int rowIncrement, int colIncrement) {
		this.colIncrement = colIncrement;
		this.rowIncrement = rowIncrement;
	}

	public int getColIncrement() {
		return colIncrement;
	}

	public int getRowIncrement() {
		return rowIncrement;
	}

}
