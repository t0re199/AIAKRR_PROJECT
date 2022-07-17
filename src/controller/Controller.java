package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

import algorithm.LevelGenerator;
import algorithm.MiniMax;
import algorithm.heuristic.MixHeuristic;
import concurrency.ExectutorServiceWorkingArea;
import concurrency.WorkingArea;
import configuration.Configuration;
import data.Move;
import data.Type;
import data_structure.board.Board;
import data_structure.board.FlattenedBoard;
import data_structure.node.LinkedNode;
import data_structure.node.Node;
import util.GameUtil;

public class Controller {

	private static long PANIC_THRESHOLD = 910;
	  
	private static int MAX_BOOSTING_LEVEL = 6, 
             		   BOOSTING_LEVEL = 5,
             		   DEFAULT_LEVEL = 4;
	
	
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	private WorkingArea workingArea;
	
	public Controller(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		}catch(Exception e) {
			throw new Error(e);
		}
	}
	
	public void run() {
		int color = 0;
		String msg = null;
		try {
			msg = reader.readLine();
			if(msg.equals("WELCOME White")) {
				color = 0;
			}
			else if(msg.equals("WELCOME Black")) {
				color = 1;
			}
			reader.readLine();
			reader.readLine();
			Configuration.setup(color);
			MiniMax.setup();
			MixHeuristic.setup();
			Board board = new FlattenedBoard(color);
			Node root = null; 
			
			
			if(color == 0) {
				root = new LinkedNode(null, Type.MAX.ordinal(), board);
				reader.readLine(); //your turn
				Move toSend = Move.parse(GameUtil.OPENING_STRATEGIES[color][0]);
				root = new LinkedNode(toSend, Type.MIN.ordinal(), board.execMaximizerMove(toSend));
				writer.println(toSend);
				writer.flush();
				reader.readLine(); //valid move
				for(int i = 1; i < GameUtil.OPENING_STRATEGIES[color].length; i++) {
					msg = reader.readLine(); //opponent move
					Move opponentMove = Move.parse(msg.split(" ")[1]);
					root = new LinkedNode(opponentMove, Type.MAX.ordinal(), root.getBoard().execMinimizerMove(opponentMove));
					reader.readLine(); //your turn
					toSend = Move.parse(GameUtil.OPENING_STRATEGIES[color][i]);
					root = new LinkedNode(toSend, Type.MIN.ordinal(), root.getBoard().execMaximizerMove(toSend));
					writer.println(toSend);
					writer.flush();
					reader.readLine(); //valid move
				}
			}
			else {
				root= new LinkedNode(null, Type.MIN.ordinal(), board);
				for(String move : GameUtil.OPENING_STRATEGIES[color]) {
					msg = reader.readLine(); //opponent move
					Move opponentMove = Move.parse(msg.split(" ")[1]);
					root = new LinkedNode(opponentMove, Type.MAX.ordinal(), root.getBoard().execMinimizerMove(opponentMove));
					reader.readLine(); //your turn
					Move toSend = Move.parse(move);
					root = new LinkedNode(toSend, Type.MIN.ordinal(), root.getBoard().execMaximizerMove(toSend));
					writer.println(toSend);
					writer.flush();
					reader.readLine(); //valid move
				}
			}
			
			LevelGenerator.generate(root, 2);
			
			int level = 0,
				oldLevel = DEFAULT_LEVEL,
				moveSincePanic = 3;
			
			
			long lastElapsedTime = Long.MAX_VALUE;
			
			boolean easyMove = false;
			
			while(true) {
				msg = reader.readLine(); //opponent move
				long beginning = System.currentTimeMillis();
				long panicTime = beginning + PANIC_THRESHOLD ;
				
				MiniMax.panicTime = panicTime;
				
				Move move = Move.parse(msg.split(" ")[1]);
				Node tmp = null;
				for(Node child: root.getChilds()) {
					if(child.getMove().equals(move)) {
						tmp = child;
						break;
					}
				}

				root = tmp;
			
				
				if(easyMove && ++moveSincePanic >= 2)
				{
					if(oldLevel == BOOSTING_LEVEL && lastElapsedTime < 120)
					{
						level = MAX_BOOSTING_LEVEL;
					}
					else if(oldLevel == MAX_BOOSTING_LEVEL && lastElapsedTime < 510)
					{
						level = MAX_BOOSTING_LEVEL;
					}
					else
					{
						level = BOOSTING_LEVEL;
					}
				}
				else 
				{
					if(oldLevel > DEFAULT_LEVEL)
					{
						easyMove = true;
						level = --oldLevel;
					}
					else 
					{
						level = DEFAULT_LEVEL;
					}
				}
				
				workingArea = new ExectutorServiceWorkingArea(root.getChilds(), level);
				float alpha = Float.NEGATIVE_INFINITY;
				workingArea.start();
				while(true) {
					Node node = workingArea.getNode();
					if (node == null) {
						break;
					}
					MiniMax.miniMaxGen(node, level, alpha, Float.POSITIVE_INFINITY);
					alpha = Math.max(alpha, node.getValue());
				}
				workingArea.join();
				
				long elapsedTime = System.currentTimeMillis() - beginning;
				
				if(elapsedTime > PANIC_THRESHOLD)
				{
					moveSincePanic = 0;
					oldLevel = DEFAULT_LEVEL;
				}
				
				Iterator<Node> iterator = root.getChilds().iterator();
				tmp = iterator.next();
				float max = tmp.getValue();
				while(iterator.hasNext()) {
					Node child = iterator.next();
					if(Float.compare(child.getValue(),max) > 0) {
						tmp = child;
						max = child.getValue();
					}
				}
				
				root = tmp;
				writer.println(root.getMove());
				writer.flush();
				
				if(easyMove)
				{
					easyMove = elapsedTime < 840;
				}
				else
				{
					easyMove = elapsedTime < 230;
				}
				
				root.setChilds(null);
				LevelGenerator.generate(root, 2);
				
				oldLevel = level;
				lastElapsedTime = elapsedTime;
				
				reader.readLine(); //your turn
				reader.readLine(); //valid move
			}
			
		}
		catch(Exception e) 
		{
			workingArea.shutdown();
		}
	}

	public static void main(String[] args)
	{
		new Controller(args[0], Integer.parseInt(args[1])).run();
	}
}
