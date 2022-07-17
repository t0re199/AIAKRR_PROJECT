package algorithm;

import java.util.LinkedList;
import java.util.List;

import algorithm.heuristic.Heuristic;
import algorithm.heuristic.MixHeuristic;
import configuration.Configuration;
import data.Move;
import data.Type;
import data_structure.board.Board;
import data_structure.node.LinkedNode;
import data_structure.node.Node;
import util.GameUtil;

public class MiniMax {
	
	private static Configuration configuration;
	private static final Heuristic heuristic = new MixHeuristic();
	
	public static long panicTime = Long.MAX_VALUE;
	
	private MiniMax()
	{
	}

	public static float miniMax(Node node, int level) {
		if (node.getType() == Type.MAX) {
			return maximizer(node, level);
		} else {
			return minimizer(node, level);
		}
	}

	private static float minimizer(Node node, int level) {
		if (level == 0 || System.currentTimeMillis() >= panicTime) {
			float value = MiniMax.heuristic.evaluate(node,level);
			node.setValue(value);
			return value;
		}
		
		if(node.getBoard().getMinimizerTowers().isEmpty()) {
			float value = MiniMax.heuristic.evaluate(node,level);
			node.setValue(value);
			return value;
		}
		
		for(int j = 0; j <= 7; j++) {
			if(node.getBoard().get(0, j) == GameUtil.BLACK_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(1) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
			
			if(node.getBoard().get(6, j) == GameUtil.WHITE_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(0) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
		
		}
		List<Node> childs = node.getChilds();
		
		if (childs == null) {
			float value =  miniMaxGen(node, level, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
			node.setValue(value);
			return value;
		}

		float min = Float.POSITIVE_INFINITY;
		for (Node child : childs) {
			float value = maximizer(child, level - 1);
			if (Float.compare(value, min) < 0) {
				min = value;
			}
		}
		node.setValue(min);
		return min;
	}

	private static float maximizer(Node node, int level) {
		if (level == 0 || System.currentTimeMillis() >= panicTime) {
			float value = MiniMax.heuristic.evaluate(node,level);
			node.setValue(value);
			return value;
		}
		
		if(node.getBoard().getMaximizerTowers().isEmpty()) {
			float value = MiniMax.heuristic.evaluate(node,level);
			node.setValue(value);
			return value;
		}

		for(int j = 0; j <= 7; j++) { 
			if(node.getBoard().get(0, j) == GameUtil.BLACK_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(1) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
			
			if(node.getBoard().get(6, j) == GameUtil.WHITE_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(0) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
		
		}
		List<Node> childs = node.getChilds();

		if (childs == null) {
			float value =  miniMaxGen(node, level, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
			node.setValue(value);
			return value;
		}
		float max = Float.NEGATIVE_INFINITY;
		for (Node child : childs) {
			float value = minimizer(child, level - 1);
			if (Float.compare(value, max) > 0) {
				max = value;
			}
		}
		node.setValue(max);
		return max;
	}

	public static float miniMaxGen(Node node, int level, float alpha, float beta) {
		if (node.getType() == Type.MAX) {
			return maximizerGen(node, level, alpha, beta);
		} else {
			return minimizerGen(node, level, alpha, beta);

		}
	}

	private static float minimizerGen(Node node, int level, float alpha, float beta) {
		if(System.currentTimeMillis() >= panicTime)
		{
			float value = MiniMax.heuristic.evaluate(node, 0);
			node.setValue(value);
			return value;
		}
		
		if (level == 0) {
			float value = MiniMax.heuristic.evaluate(node, level);
			node.setValue(value);
			return value;
		}
		
		for(int j = 0; j <= 7; j++) {
			
			if(node.getBoard().get(0, j) == GameUtil.BLACK_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(1) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
			
			if(node.getBoard().get(6, j) == GameUtil.WHITE_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(0) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
		
		}
		Board board = node.getBoard();
		
		List<Move> moves = board.generateMinimizerMoves();

		if(moves.isEmpty())
		{
			float normalizer = ((float) Math.pow(10, level*4));
			float value = configuration.getMaximizerMultiplier() * MixHeuristic.WIN * normalizer;
			node.setValue(value);
			return value;
		}
		
		List<Node> childs = new LinkedList<>(); 

		float currentBeta = Float.POSITIVE_INFINITY;

		for (Move move : moves) {
			Node child = new LinkedNode(move, Type.MAX.ordinal(), board.execMinimizerMove(move));

			currentBeta = Math.min(currentBeta, maximizerGen(child, level - 1, alpha, beta));
			beta = Math.min(beta, currentBeta);
			if (Float.compare(beta, alpha) < 0) {
				break;
			}
			childs.add(child);
		}
		node.setChilds(childs);
		node.setValue(currentBeta);
		return currentBeta;
	}

	private static float maximizerGen(Node node, int level, float alpha, float beta) {
		if(System.currentTimeMillis() >= panicTime)
		{
			float value = MiniMax.heuristic.evaluate(node, 0);
			node.setValue(value);
			return value;
		}
		
		if (level == 0) {
			float value = MiniMax.heuristic.evaluate(node, level);
			node.setValue(value);
			return value;
		}
		

		for(int j = 0; j <= 7; j++) {
			
			if(node.getBoard().get(0, j) == GameUtil.BLACK_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(1) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
			
			if(node.getBoard().get(6, j) == GameUtil.WHITE_STONE) {
				float normalizer = ((float) Math.pow(10, level*4));
				float value = configuration.getMultiplier(0) * MixHeuristic.WIN * normalizer;
				node.setValue(value);
				return value;
			}
		}
			
		Board board =  node.getBoard();
		
		List<Move> moves = board.generateMaximizerMoves();

		if(moves.isEmpty())
		{
			float normalizer = ((float) Math.pow(10, level*4));
			float value = configuration.getMinimizerMultiplier() * MixHeuristic.WIN * normalizer;
			node.setValue(value);
			return value;
		}
		
		List<Node> childs = new LinkedList<>(); 

		float currentAlpha = Float.NEGATIVE_INFINITY;

		for (Move move : moves) {
			Node child = new LinkedNode(move, Type.MIN.ordinal(), board.execMaximizerMove(move));

			currentAlpha = Math.max(currentAlpha, minimizerGen(child, level - 1, alpha, beta));
			alpha = Math.max(currentAlpha, alpha);
			if (Float.compare(beta, alpha) < 0) {
				break;
			}
			childs.add(child);
		}
		node.setChilds(childs);
		node.setValue(currentAlpha);
		return currentAlpha;
	}
	
	public static void setup() {
		MiniMax.configuration = Configuration.getInstance();
	}
}
