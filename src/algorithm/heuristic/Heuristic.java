package algorithm.heuristic;

import data_structure.node.Node;

public interface Heuristic {
	
	float evaluate(Node node, int level);
	
}
