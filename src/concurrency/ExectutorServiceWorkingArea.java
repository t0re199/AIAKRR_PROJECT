package concurrency;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algorithm.MiniMax;
import data_structure.node.Node;

public class ExectutorServiceWorkingArea implements WorkingArea{
	
	private static final ExecutorService WORKER = Executors.newSingleThreadExecutor();
	
	private Iterator<Node> iterator;
	
	private Future<Void> future;
	
	private int level;
	
	
	public ExectutorServiceWorkingArea(Collection<Node> list, int level) {
		iterator = list.iterator();
		this.level = level;
	}
	
	public ExectutorServiceWorkingArea() {
	}

	@Override
	public synchronized Node getNode() {
		return iterator.hasNext() ? iterator.next() : null ;
	}

	@Override
	public void join() throws InterruptedException, ExecutionException {
		future.get();
	}

	@Override
	public void start() {
		future = WORKER.submit(() -> {
			float alpha = Float.NEGATIVE_INFINITY;
			while(true) {
				Node node = getNode();
				if (node == null) {
					break;
				}
				MiniMax.miniMaxGen(node, level, alpha, Float.POSITIVE_INFINITY);
				alpha = Math.max(alpha, node.getValue());
			}
			return null;
		});
	}

	@Override
	public void setNodes(List<Node> nodes)
	{
		iterator = nodes.iterator();
	}

	@Override
	public void shutdown()
	{
		WORKER.shutdown();
	}
}
