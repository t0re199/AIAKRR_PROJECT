package data_structure.board;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import data.Direction;
import data.Move;
import util.ByteUtils;
import util.GameUtil;

public class FlattenedBoard implements Board
{
	private static final int SIZE = 56;
	
	public static int maximizerType = 0;
	
	private Set<Cell> maximizerTowers,
	  				  minimizerTowers;
	
	private byte board[];
	

	public FlattenedBoard(int color) {
		FlattenedBoard.maximizerType = color;
		board = new byte[FlattenedBoard.SIZE];
		maximizerTowers = new HashSet<>();
		minimizerTowers = new HashSet<>();
		
		for(int i = 1; i < 6; i++) {
			for(int j = 0; j < 8; j++) {
				set(i,j,GameUtil.EMPTY);
			}
		}
		
		for(int j = 0; j < 8; j++) {
			set(0,j,GameUtil.WHITE_TOWER);
			set(6,j,GameUtil.BLACK_TOWER);
			if(color == 0) { //noi siamo i max e giochiamo con il bianco
				this.maximizerTowers.add(new Cell((byte)0,(byte)j)); //white tower
				this.minimizerTowers.add(new Cell((byte)6,(byte)j)); //black tower
			}
			else { //noi siamo i max e giochiamo con il nero
				this.minimizerTowers.add(new Cell((byte)0,(byte)j)); //white tower
				this.maximizerTowers.add(new Cell((byte)6,(byte)j)); //black tower
			}
		}
		
	}
	
	public FlattenedBoard(byte[] board, Set<Cell> minimizerTowers, Set<Cell> maximizerTowers) {
		this.board = board;
		this.minimizerTowers = minimizerTowers;
		this.maximizerTowers = maximizerTowers;
	}
	
	@Override
	public byte get(int row, int col)
	{
		return board[(row << 3) | col];
	}

	@Override
	public void set(int row, int col, byte value)
	{
		board[(row << 3) | col] = value;
	}

	@Override
	public void setEmpty(int row, int col)
	{
		board[(row << 3) | col] = 0;
	}

	@Override
	public Board execMinimizerMove(Move move) {

		byte[] tmpBoard = new byte[SIZE];

		System.arraycopy(board, 0, tmpBoard, 0, SIZE);
		
		Set<Cell> currentTowers = new HashSet<Cell>(minimizerTowers);
		
		FlattenedBoard currentBoard = new FlattenedBoard(tmpBoard, currentTowers, maximizerTowers);

		int row = move.getRow();
		int col = move.getCol();

		Cell cell = new Cell((byte) row, (byte) col);

		currentTowers.remove(cell);

		Direction direction = move.getDirection();
		int rowIncrement = direction.getRowIncrement();
		int colIncrement = direction.getColIncrement();

		byte argument = get(row,col); // per distinguere se giochiamo
												// da min o da max
		byte ourType = ByteUtils.getType(argument); // bianco o nero

		int nextRow = row + rowIncrement;
		int nextCol = col + colIncrement;

		argument = get(nextRow, nextCol);

		
		if (GameUtil.getOpponentStone(ourType) == argument) { // se essere cosi mangiamo pietra avversaria
			currentBoard.setEmpty(nextRow,nextCol);
			currentBoard.set(row, col, GameUtil.getOurStone(ourType)); 
			return currentBoard;
		}

		// dobbiamo muovere torre
		currentBoard.setEmpty(row,col);
		currentBoard.set(nextRow, nextCol, ByteUtils.increment(argument, ourType));

		if (ByteUtils.getValue(currentBoard.get(nextRow, nextCol)) == 2) {
			cell.row = (byte) nextRow;
			cell.col = (byte) nextCol;
			currentTowers.add(cell);
		}

		nextRow = nextRow + rowIncrement;
		nextCol = nextCol + colIncrement;
		argument = get(nextRow, nextCol);
		currentBoard.set(nextRow, nextCol, ByteUtils.increment(argument, ourType));

		if (ByteUtils.getValue(currentBoard.get(nextRow, nextCol)) == 2) {
			currentTowers.add(new Cell((byte) nextRow, (byte) nextCol));
		}

		return currentBoard;
	}

	@Override
	public Board execMaximizerMove(Move move) {
		
		byte[] tmpBoard = new byte[FlattenedBoard.SIZE];
		
		System.arraycopy(board, 0, tmpBoard, 0, SIZE);
		
		Set<Cell> currentTowers = new HashSet<Cell>(maximizerTowers);
		
		FlattenedBoard currentBoard = new FlattenedBoard(tmpBoard,minimizerTowers,currentTowers);

		int row = move.getRow();
		int col = move.getCol();

		Cell cell = new Cell((byte) row, (byte) col);

		currentTowers.remove(cell);

		Direction direction = move.getDirection();
		int rowIncrement = direction.getRowIncrement();
		int colIncrement = direction.getColIncrement();

		byte argument = get(row,col); // per distinguere se giochiamo
												// da min o da max
		byte ourType = ByteUtils.getType(argument); // bianco o nero

		int nextRow = row + rowIncrement;
		int nextCol = col + colIncrement;

		argument = get(nextRow, nextCol);
		//System.out.println("r:" + nextRow + " c:" + nextCol + " arg:" + argument);

		if (GameUtil.getOpponentStone(ourType) == argument) {// se essere cosi mangiamo pietra avversaria
			currentBoard.setEmpty(nextRow,nextCol);
			currentBoard.set(row, col, GameUtil.getOurStone(ourType)); 
			return currentBoard;
		}

		// dobbiamo muovere torre
		currentBoard.setEmpty(row,col);
		currentBoard.set(nextRow, nextCol, ByteUtils.increment(argument, ourType));

		if (ByteUtils.getValue(currentBoard.get(nextRow, nextCol)) == 2) {
			cell.row = (byte) nextRow;
			cell.col = (byte) nextCol;
			currentTowers.add(cell);
		}

		nextRow = nextRow + rowIncrement;
		nextCol = nextCol + colIncrement;
		argument = get(nextRow, nextCol);
		currentBoard.set(nextRow, nextCol, ByteUtils.increment(argument, ourType));

		if (ByteUtils.getValue(currentBoard.get(nextRow, nextCol)) == 2) {
			currentTowers.add(new Cell((byte) nextRow, (byte) nextCol));
		}
		
		return currentBoard;
	}

	@Override
	public Set<Cell> getMinimizerTowers()
	{
		return minimizerTowers;
	}

	@Override
	public Set<Cell> getMaximizerTowers()
	{
		return maximizerTowers;
	}

	@Override
	public void setMinimizerTowers(Set<Cell> minimizerTowers)
	{
		this.minimizerTowers = minimizerTowers;
		
	}

	@Override
	public void setMaximizerTowers(Set<Cell> maximizerTowers)
	{
		this.maximizerTowers = maximizerTowers;
		
	}

	private List<Move> generateWhiteMoves(Set<Cell> towers) 
	{
		byte opponentStone = GameUtil.BLACK_STONE;
		List<Move> result = new LinkedList<>();

		byte nValid = 0, 
			 sValid = 0, 
			 wValid = 0,
			 eValid = 0;
		
		byte nextArgument = 0x0,
			 lastArgument = 0x0;
		
		boolean skip = false;
		
		for (Cell tower : towers)
		{
			nValid = 0;
			sValid = 0;
			wValid = 0;
			eValid = 0;
			int row = tower.row;
			int col = tower.col;
			
			int nextRow = row + 0x1;
			
			skip = false;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				nValid = 2;
			}
			else if(nValid < 2)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.N));
					skip = true;
				}
			}
			
			int lastRow = row + 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				nValid++;
			}
			else if(nValid < 2 && !skip)
			{ 
				lastArgument = get(lastRow, col);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.N));
				}
			}
			
			skip = false;
			int nextCol = col + 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				eValid = 2;
			}
			else if(eValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.E));
					skip = true;
				}
			}
			
			int lastCol = col + 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				eValid++;
			}
			else if(eValid < 2 && !skip)
			{
				lastArgument = get(row, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.E));
				}
			}
			
			skip = false;
			if((nValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.NE));
					skip = true;
				}
			}
			if(!skip && (nValid | eValid) == 0) {
				lastArgument = get(lastRow, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.NE));
				}
			}
			
			skip = false;
			nextRow = row - 1;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				sValid = 2;
			}
			else if(sValid < 2 && !skip)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.S));
					skip = true;
				}
			}
			
			lastRow = row - 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				sValid++;
			}
			else if(sValid < 2 && !skip)
			{
				lastArgument = get(lastRow, col);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.S));
				}
			}
			
			skip = false;
			if((sValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.SE));
					skip = true;
				}
			}
			if(!skip && (sValid | eValid) == 0){
				lastArgument = get(lastRow, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.SE));
				}
			}
			
			skip = false;
			nextCol = col - 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				wValid = 2;
			}
			else if(wValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.W));
					skip = true;
				}
			}
			
			lastCol = col - 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				wValid++;
			}
			else if(wValid < 2 && !skip)
			{
				lastArgument = get(row, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.W));
				}
			}
			
			skip = false;
			if((sValid | wValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.SW));
					skip = true;
				}
				
			}
			if(!skip && (sValid | wValid) == 0){
				lastArgument = get(lastRow, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.SW));
				}
			}
			
			skip = false;
			if((nValid | wValid) <= 1)
			{
				nextRow = row + 1;
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.NW));
					skip = true;
				}
			}
			if (!skip && (nValid | wValid) == 0){
				lastArgument = get(nextRow + 1, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					result.add(new Move(row, col, Direction.NW));
				}
			}
		}
		return result;
	}
	
	private List<Move> generateBlackMoves(Set<Cell> towers) 
	{
		byte opponentStone = GameUtil.WHITE_STONE;
		List<Move> result = new LinkedList<>();

		byte nValid = 0, 
			 sValid = 0, 
			 wValid = 0,
			 eValid = 0;
		
		byte nextArgument = 0x0,
			 lastArgument = 0x0;
		
		boolean skip = false;
		
		for (Cell tower : towers)
		{
			nValid = 0;
			sValid = 0;
			wValid = 0;
			eValid = 0;
			int row = tower.row;
			int col = tower.col;
			
			
			int nextRow = row + 0x1;
			
			skip = false;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				nValid = 2;
			}
			else if(nValid < 2)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.N));
					skip = true;
				}
			}
			
			int lastRow = row + 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				nValid++;
			}
			else if(nValid < 2  && !skip)
			{
				lastArgument = get(lastRow, col);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.N));
				}
			}
			
			skip = false;
			int nextCol = col + 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				eValid = 2;
			}
			else if(eValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.E));
					skip = true;
				}
			}
			
			int lastCol = col + 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				eValid++;
			}
			else if(eValid < 2  && !skip)
			{
				lastArgument = get(row, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.E));
				}
			}
			

			skip = false;
			if((nValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.NE));
					skip = true;
				}
			}
			
			if(!skip && (nValid | eValid) == 0) {
				lastArgument = get(lastRow, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.NE));
				}
			}
			
			skip = false;
			nextRow = row - 1;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				sValid = 2;
			}
			else if(sValid < 2)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.S));
					skip = true;
				}
			}
			
			lastRow = row - 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				sValid++;
			}
			else if(sValid < 2  && !skip)
			{
				lastArgument = get(lastRow, col);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.S));
				}
			}
			
			skip = false;
			if((sValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.SE));
					skip = true;
				}
			}
			if(!skip && (sValid | eValid) == 0){
				lastArgument = get(lastRow, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.SE));
				}
			}
			
			skip = false;
			nextCol = col - 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				wValid = 2;
			}
			else if(wValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.W));
					skip = true;
				}
			}
			
			lastCol = col - 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				wValid++;
			}
			else if(wValid < 2  && !skip)
			{
				lastArgument = get(row, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.W));
				}
			}
			
			skip = false;
			if((sValid | wValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.SW));
					skip = true;
				}
				
			}
			if(!skip && (sValid | wValid) == 0){
				lastArgument = get(lastRow, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.SW));
				}
			}
			
			skip = false;
			if((nValid | wValid) <= 1)
			{
				nextRow = row + 1;
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					result.add(new Move(row, col, Direction.NW));
					skip = true;
				}
			}
			if (!skip && (nValid | wValid) == 0){
				lastArgument = get(nextRow + 1, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					result.add(new Move(row, col, Direction.NW));
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(!(obj instanceof Board))
			return false;
		Board other = (Board) obj;
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 8; j++) {
				if(other.get(i, j) != this.get(i, j))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Move> generateMinimizerMoves() {
		if(FlattenedBoard.maximizerType == 1)
			return generateWhiteMoves(minimizerTowers);
		return generateBlackMoves(minimizerTowers);
	}

	@Override
	public List<Move> generateMaximizerMoves() {
		if(FlattenedBoard.maximizerType == 0)
			return generateWhiteMoves(maximizerTowers);
		return generateBlackMoves(maximizerTowers);
	}

	@Override
	public boolean hasMinimizerMoves()
	{
		if(FlattenedBoard.maximizerType == 1)
			return hasWhiteMoves(minimizerTowers);
		return hasBlackMoves(minimizerTowers);
	}

	@Override
	public boolean hasMaximizerMoves()
	{
		if(FlattenedBoard.maximizerType == 0)
			return hasWhiteMoves(maximizerTowers);
		return hasBlackMoves(maximizerTowers);
	}
	
	private boolean hasWhiteMoves(Set<Cell> towers) 
	{
		byte opponentStone = GameUtil.BLACK_STONE;

		byte nValid = 0, 
			 sValid = 0, 
			 wValid = 0,
			 eValid = 0;
		
		byte nextArgument = 0x0,
			 lastArgument = 0x0;
		
		boolean skip = false;
		
		for (Cell tower : towers)
		{
			nValid = 0;
			sValid = 0;
			wValid = 0;
			eValid = 0;
			int row = tower.row;
			int col = tower.col;
			
			int nextRow = row + 0x1;
			
			skip = false;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				nValid = 2;
			}
			else if(nValid < 2)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			int lastRow = row + 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				nValid++;
			}
			else if(nValid < 2 && !skip)
			{ 
				lastArgument = get(lastRow, col);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			int nextCol = col + 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				eValid = 2;
			}
			else if(eValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			int lastCol = col + 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				eValid++;
			}
			else if(eValid < 2 && !skip)
			{
				lastArgument = get(row, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			if((nValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			if(!skip && (nValid | eValid) == 0) {
				lastArgument = get(lastRow, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			nextRow = row - 1;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				sValid = 2;
			}
			else if(sValid < 2 && !skip)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			lastRow = row - 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				sValid++;
			}
			else if(sValid < 2 && !skip)
			{
				lastArgument = get(lastRow, col);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			if((sValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			if(!skip && (sValid | eValid) == 0){
				lastArgument = get(lastRow, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			nextCol = col - 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				wValid = 2;
			}
			else if(wValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			lastCol = col - 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				wValid++;
			}
			else if(wValid < 2 && !skip)
			{
				lastArgument = get(row, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			if((sValid | wValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
				
			}
			if(!skip && (sValid | wValid) == 0){
				lastArgument = get(lastRow, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
			
			skip = false;
			if((nValid | wValid) <= 1)
			{
				nextRow = row + 1;
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			if (!skip && (nValid | wValid) == 0){
				lastArgument = get(nextRow + 1, lastCol);
				int toCheck = (lastArgument | nextArgument) | GameUtil.WHITE_STONE;
				if (toCheck == GameUtil.WHITE_STONE) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hasBlackMoves(Set<Cell> towers) 
	{
		byte opponentStone = GameUtil.WHITE_STONE;

		byte nValid = 0, 
			 sValid = 0, 
			 wValid = 0,
			 eValid = 0;
		
		byte nextArgument = 0x0,
			 lastArgument = 0x0;
		
		boolean skip = false;
		
		for (Cell tower : towers)
		{
			nValid = 0;
			sValid = 0;
			wValid = 0;
			eValid = 0;
			int row = tower.row;
			int col = tower.col;
			
			
			int nextRow = row + 0x1;
			
			skip = false;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				nValid = 2;
			}
			else if(nValid < 2)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			int lastRow = row + 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				nValid++;
			}
			else if(nValid < 2  && !skip)
			{
				lastArgument = get(lastRow, col);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			
			skip = false;
			int nextCol = col + 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				eValid = 2;
			}
			else if(eValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			int lastCol = col + 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				eValid++;
			}
			else if(eValid < 2  && !skip)
			{
				lastArgument = get(row, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			

			skip = false;
			if((nValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			if(!skip && (nValid | eValid) == 0) {
				lastArgument = get(lastRow, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			
			skip = false;
			nextRow = row - 1;
			if(!Board.VALID_ROW_INDEXES.contains(nextRow))
			{
				sValid = 2;
			}
			else if(sValid < 2)
			{
				nextArgument = get(nextRow, col);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			lastRow = row - 2;
			
			if(!Board.VALID_ROW_INDEXES.contains(lastRow))
			{
				sValid++;
			}
			else if(sValid < 2  && !skip)
			{
				lastArgument = get(lastRow, col);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			
			skip = false;
			if((sValid | eValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			if(!skip && (sValid | eValid) == 0){
				lastArgument = get(lastRow, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			
			skip = false;
			nextCol = col - 0x1;
			if(!Board.VALID_COL_INDEXES.contains(nextCol))
			{
				wValid = 2;
			}
			else if(wValid < 2)
			{
				nextArgument = get(row, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			
			lastCol = col - 2;
			
			if(!Board.VALID_COL_INDEXES.contains(lastCol))
			{
				wValid++;
			}
			else if(wValid < 2  && !skip)
			{
				lastArgument = get(row, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			
			skip = false;
			if((sValid | wValid) <= 1)
			{
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
				
			}
			if(!skip && (sValid | wValid) == 0){
				lastArgument = get(lastRow, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
			
			skip = false;
			if((nValid | wValid) <= 1)
			{
				nextRow = row + 1;
				nextArgument = get(nextRow, nextCol);
				if(nextArgument == opponentStone)
				{
					return true;
				}
			}
			if (!skip && (nValid | wValid) == 0){
				lastArgument = get(nextRow + 1, lastCol);
				byte lastValue = ByteUtils.getValue(lastArgument);
				int toCheck = (ByteUtils.getValue(nextArgument) | lastValue) + (1 << ((1 ^ ByteUtils.getType(lastArgument)) & lastValue));
				if (toCheck <= 2) {
					return true;
				}
			}
		}
		return false;
	}

	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 8; j++) {
				stringBuilder.append(get(i, j));
				stringBuilder.append("\t");
			}
			stringBuilder.append("\n");
		}
		stringBuilder.append("\n\n");
		return stringBuilder.toString();
	}
}
