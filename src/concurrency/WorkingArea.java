package concurrency;

import java.util.List;
import java.util.concurrent.ExecutionException;

import data_structure.node.Node;

public interface WorkingArea {
	
	Node getNode();
	
	void join() throws InterruptedException, ExecutionException;
	
	void setNodes(List<Node> nodes);
	
	void start();
	
	void shutdown();

}
