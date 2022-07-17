package data_structure.node;

import java.util.List;

import data.Move;
import data.Type;
import data_structure.board.Board;

public interface Node extends Comparable<Node> {
	static final float VALUES[] = { Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY };

	float getValue();

	void setValue(float value);

	Move getMove();

	Type getType();

	Board getBoard();

	default List<Node> getChilds() {
		return null;
	}
	
	void setChilds(List<Node> childs);
	
}
