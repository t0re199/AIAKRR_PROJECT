package algorithm;

import java.util.LinkedList;
import java.util.List;

import data.Move;
import data.Type;
import data_structure.board.Board;
import data_structure.node.LinkedNode;
import data_structure.node.Node;
import util.GameUtil;

public class LevelGenerator
{
	private LevelGenerator()
	{
	}

	public static void generate(Node node, int level)
	{
		if (node.getType() == Type.MAX)
		{
			maximizerGen(node, level);
		}
		else
		{
			minimizerGen(node, level);
		}
	}

	private static void minimizerGen(Node node, int level) 
	{
		if (level == 0)
		{
			return;
		}

		if (node.getBoard().getMinimizerTowers().isEmpty())
		{
			return;
		}

		for (int j = 0; j <= 7; j++)
		{

			if (node.getBoard().get(0, j) == GameUtil.BLACK_STONE)
			{
				return;
			}

			if (node.getBoard().get(6, j) == GameUtil.WHITE_STONE)
			{
				return;
			}
		}

		List<Node> childs = new LinkedList<>();

		Board board = node.getBoard();

		List<Move> moves = board.generateMinimizerMoves();

		for (Move move : moves)
		{
			Node child = new LinkedNode(move, Type.MAX.ordinal(), board.execMinimizerMove(move));

			maximizerGen(child, level - 1);
			childs.add(child);
		}
		node.setChilds(childs);
	}

	private static void maximizerGen(Node node, int level)
	{
		if (level == 0)
		{
			return;
		}

		if (node.getBoard().getMaximizerTowers().isEmpty())
		{
			return;
		}

		for (int j = 0; j <= 7; j++)
		{

			if (node.getBoard().get(0, j) == GameUtil.BLACK_STONE)
			{
				return;
			}

			if (node.getBoard().get(6, j) == GameUtil.WHITE_STONE)
			{
				return;
			}
		}

		List<Node> childs = new LinkedList<>();

		Board board = node.getBoard();

		List<Move> moves = board.generateMaximizerMoves();

		for (Move move : moves)
		{
			Node child = new LinkedNode(move, Type.MIN.ordinal(), board.execMaximizerMove(move));

			minimizerGen(child, level - 1);
			childs.add(child);
		}
		node.setChilds(childs);
	}
}
