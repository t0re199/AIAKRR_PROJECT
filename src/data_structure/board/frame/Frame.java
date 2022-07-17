package data_structure.board.frame;

import java.util.List;
import java.util.Set;

import data.Move;
import data_structure.board.Board;
import data_structure.board.Cell;

public class Frame implements Board
{
	private Board board;

	public Frame(Board board)
	{
		this.board = board;
	}
	
	public byte get(int row, int col)
	{
		if(Board.VALID_COL_INDEXES.contains(col) && Board.VALID_ROW_INDEXES.contains(row))
		{
			return board.get(row, col);
		}
		return (byte)255;
	}

	@Override
	public void set(int row, int col, byte value)
	{
		board.set(row, col, value);
	}

	@Override
	public void setEmpty(int row, int col)
	{
		board.setEmpty(row, col);
	}

	@Override
	public Board execMinimizerMove(Move move)
	{
		return null;
	}

	@Override
	public Board execMaximizerMove(Move move)
	{
		return null;
	}

	@Override
	public Set<Cell> getMinimizerTowers()
	{
		return null;
	}

	@Override
	public Set<Cell> getMaximizerTowers()
	{
		return null;
	}

	@Override
	public void setMinimizerTowers(Set<Cell> minimizerTowers)
	{
	}

	@Override
	public void setMaximizerTowers(Set<Cell> maximizerTowers)
	{
	}

	@Override
	public List<Move> generateMinimizerMoves()
	{
		return null;
	}

	@Override
	public List<Move> generateMaximizerMoves()
	{
		return null;
	}

	@Override
	public boolean hasMinimizerMoves()
	{
		return false;
	}

	@Override
	public boolean hasMaximizerMoves()
	{
		return false;
	}
}
