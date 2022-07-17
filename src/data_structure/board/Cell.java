package data_structure.board;

public class Cell {

	byte row, 
		 col;

	public Cell(byte row, byte col) {
		this.row = row;
		this.col = col;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (255 & col);
		result = prime * result + (255 & row);
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public byte getRow() {
		return row;
	}

	public byte getCol() {
		return col;
	}

	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + "]";
	}
}
