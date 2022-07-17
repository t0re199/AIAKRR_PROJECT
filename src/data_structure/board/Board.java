package data_structure.board;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Move;

public interface Board {
	
	static final Set<Integer> VALID_ROW_INDEXES = new HashSet<>(Arrays.asList(0,1,2,3,4,5,6));
	
	static final Set<Integer> VALID_COL_INDEXES = new HashSet<>(Arrays.asList(0,1,2,3,4,5,6,7));
	
	byte get(int row, int col);

	void set(int row, int col, byte value);
	
	void setEmpty(int row, int col);

	Board execMinimizerMove(Move move);
	
	Board execMaximizerMove(Move move);

	Set<Cell> getMinimizerTowers();

	Set<Cell> getMaximizerTowers();
	
	void setMinimizerTowers(Set<Cell> minimizerTowers);

	void setMaximizerTowers(Set<Cell> maximizerTowers);
	
	List<Move> generateMinimizerMoves();
	
	List<Move> generateMaximizerMoves();
	
	boolean hasMinimizerMoves();
	
	boolean hasMaximizerMoves();
	
}
