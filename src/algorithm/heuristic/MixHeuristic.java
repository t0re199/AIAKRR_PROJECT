package algorithm.heuristic;

import static util.ByteUtils.getType;

import configuration.Configuration;
import data_structure.board.Board;
import data_structure.board.frame.Frame;
import data_structure.node.Node;


public class MixHeuristic implements Heuristic
{
	private static Configuration configuration;

	private static final float STONE_BLOCKING_BONUS = 1.5f,
							   TOWER_BONUS = 100f,
							   MOBILITY_BONUS = 30f;
	
	public static final float WIN = (float) (Float.MAX_VALUE/(Math.pow(10, 34)));

	private static final float STONE_SCORES[] = 
												{
													0.5f,  1f, 2f, 2f, 2f, 2f, 1f, 0.5f,
													3f, 5f, 10f, 10f, 10f, 10f, 5f, 3f,
													7f, 10f, 15f, 15f, 15f, 15f, 10f, 7f,
													23f, 25f, 35f, 35f, 35f, 35f, 35f, 7f,
													47f, 50f, 75f, 75f, 75f, 75f, 50f, 47f,
													12f, 15f, 17f, 17f, 17f, 17f, 15f, 12f,
													WIN, WIN, WIN, WIN, WIN, WIN, WIN, WIN
												};
	
	private static final float TOWER_SCORES[] = {
													7f, 10f, 15f, 15f, 15f, 15f, 10f, 7f,
													25f, 35f, 45f, 45f, 45f, 45f, 35f, 25f,
													60f, 75f, 85f, 85f, 85f, 85f, 75f, 60f,
													90f, 100f, 115f, 115f, 115f, 115f, 100f, 90f,
													120f, 135f, 150f, 150f, 150f, 150f, 135f, 120f,
													40f, 50f, 65f, 65f, 65f, 65f, 50f, 40f,
													WIN, WIN, WIN, WIN, WIN, WIN, WIN, WIN
												};
	
	private static final int MASKS[][] = { { 0, 1, 255, 255 },
			   							   { 0, 2, 255, 255 } 
			  							 };

	private static final int RESULTS[] = { 1, 2 };	
	
	@Override
	public float evaluate(Node node, int level)
	{
		
		Board board = node.getBoard();
		int type = node.getType().ordinal();
		
		Frame frame = new Frame(board);
		
		float normalizer = ((float) Math.pow(10, level*4));
		
		final float winningScore = configuration.getMultiplier(type) *  WIN * normalizer;
		
		if(!board.hasMinimizerMoves()) {
			return configuration.getMaximizerMultiplier() * WIN * normalizer;
		}
		
		if(!board.hasMaximizerMoves()) {
			return configuration.getMinimizerMultiplier() * WIN * normalizer;
		}
		
		float score = configuration.getMaximizerMultiplier() * board.getMaximizerTowers().size();
		
		score += configuration.getMinimizerMultiplier() * board.getMinimizerTowers().size();
		
		score *= TOWER_BONUS;
 		
		byte color;

		for(int i = 0x0; i < 0x7; i++)
		{
			for(int j = 0x0; j < 0x8; j++)
			{
				byte param = board.get(i, j);
				switch (param & 0x3)
				{
					case 0x1:
						color = getType(param);
						score += (configuration.getMultiplier(color) * 
								 STONE_SCORES[((configuration.getBase(color) + (i * configuration.getMovement(color))) << 0x3) | j]);
						score += computeStoneScore(frame, color, i, j);
						break;
						
					case 0x2:
						color = getType(param);
						score += (configuration.getMultiplier(color) * 
								 TOWER_SCORES[((configuration.getBase(color) + (i * configuration.getMovement(color))) << 0x3) | j]);
						score += computeTowerScore(frame, color, i, j);
						break;
				}
				
				if(Float.isNaN(score)) {
					return winningScore;
				}
				
			}
		}
		
		return score * normalizer;
	}

	private float computeTowerScore(Frame frame, int color, int row, int col)
	{
		float score = 0f;
		
		int value = 0;
		
		int movement = configuration.getMovement(color);
		
		int multiplier = configuration.getMultiplier(color);
		
		int base = configuration.getBase(color);
		
		byte nextArgument = 0x0,
			 lastArgument = 0x0;
		
		if((base + (movement * row)) == 0x4)
		{
			if(col <= 1)
			{
				nextArgument = frame.get(row + movement, col);
				lastArgument = frame.get(row + (movement << 0x1), col);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}
				
				nextArgument = frame.get(row + movement, col + 0x1);
				lastArgument = frame.get(row + (movement << 0x1), col + 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}
				
				//destra
				nextArgument = frame.get(row, col + 1);
				lastArgument = frame.get(row, col + 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				
				if(value == RESULTS[color]) {
					score += MOBILITY_BONUS;
				}
			}
			else if(col >= 6)
			{
				nextArgument = frame.get(row + movement, col);
				lastArgument = frame.get(row + (movement << 0x1), col);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}

				nextArgument = frame.get(row + movement, col - 0x1);
				lastArgument = frame.get(row + (movement << 0x1), col - 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}
				
				//sinistra
				nextArgument = frame.get(row, col - 1);
				lastArgument = frame.get(row, col - 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				
				if(value == RESULTS[color]) {
					score += MOBILITY_BONUS;
				}	
			}
			else
			{
				nextArgument = frame.get(row + movement, col);
				lastArgument = frame.get(row + (movement << 0x1), col);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}

				nextArgument = frame.get(row + movement, col + 0x1);
				lastArgument = frame.get(row + (movement << 0x1), col + 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}
								
				nextArgument = frame.get(row + movement, col - 0x1);
				lastArgument = frame.get(row + (movement << 0x1), col - 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				if(value == RESULTS[color])
				{
					return multiplier * WIN;
				}
				
				//destra
				nextArgument = frame.get(row, col + 1);
				lastArgument = frame.get(row, col + 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				
				if(value == RESULTS[color]) {
					score += MOBILITY_BONUS;
				}
				
				//sinistra
				nextArgument = frame.get(row, col - 1);
				lastArgument = frame.get(row, col - 2);
				value = RESULTS[color] | 
						MASKS[getType(nextArgument)][nextArgument & 3] | 
						MASKS[getType(lastArgument)][lastArgument & 3];
				
				if(value == RESULTS[color]) {
					score += MOBILITY_BONUS;
				}	
			}
		}
		else 
		{
			//avanti
			nextArgument = frame.get(row + movement, col);
			lastArgument = frame.get(row + (movement << 1), col);
			value = RESULTS[color] | 
					MASKS[getType(nextArgument)][nextArgument & 3] | 
					MASKS[getType(lastArgument)][lastArgument & 3];
			
			if(value == RESULTS[color]) {
				score += MOBILITY_BONUS;
			}
			
			//destra-avanti
			nextArgument = frame.get(row + movement, col + 1);
			lastArgument = frame.get(row + (movement << 1), col + 2);
			value = RESULTS[color] | 
					MASKS[getType(nextArgument)][nextArgument & 3] | 
					MASKS[getType(lastArgument)][lastArgument & 3];
			
			if(value == RESULTS[color]) {
				score += MOBILITY_BONUS;
			}
			
			//sinistra-avanti
			nextArgument = frame.get(row + movement, col - 1);
			lastArgument = frame.get(row + (movement << 1), col - 2);
			value = RESULTS[color] | 
					MASKS[getType(nextArgument)][nextArgument & 3] | 
					MASKS[getType(lastArgument)][lastArgument & 3];
			
			if(value == RESULTS[color]) {
				score += MOBILITY_BONUS;
			}
			
			//destra
			nextArgument = frame.get(row, col + 1);
			lastArgument = frame.get(row, col + 2);
			value = RESULTS[color] | 
					MASKS[getType(nextArgument)][nextArgument & 3] | 
					MASKS[getType(lastArgument)][lastArgument & 3];
			
			if(value == RESULTS[color]) {
				score += MOBILITY_BONUS;
			}
			
			//sinistra
			nextArgument = frame.get(row, col - 1);
			lastArgument = frame.get(row, col - 2);
			value = RESULTS[color] | 
					MASKS[getType(nextArgument)][nextArgument & 3] | 
					MASKS[getType(lastArgument)][lastArgument & 3];
			
			if(value == RESULTS[color]) {
				score += MOBILITY_BONUS;
			}			
		}
		
		return score * multiplier;
	}

	private float computeStoneScore(Frame frame, int color, int row, int col)
	{
		byte oppenentTowers = 0x0;
		
		int multiplier = configuration.getMultiplier(color);
			
		int movement = configuration.getMovement(color);
		
		row += movement << 0x1;
		
		byte oppenentTower = (byte) ((color << 6) | 2);
		
		for(int j = -1; j <= 1; j++)
		{
			if(frame.get(row, col + j) == oppenentTower)
			{
				oppenentTowers++;
			}
		}
		
		return oppenentTowers * STONE_BLOCKING_BONUS * multiplier;
	}
	
	public static void setup()
	{
		MixHeuristic.configuration = Configuration.getInstance();
	}
	
}
