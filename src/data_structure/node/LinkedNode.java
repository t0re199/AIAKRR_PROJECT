package data_structure.node;

import java.util.List;

import data.Move;
import data.Type;
import data_structure.board.Board;

public class LinkedNode implements Node {
	
	private Board board;

	private List<Node> childs;
	
	private Move move;
	
	private Type type;
	
	private float value;



	public LinkedNode(float value, Move move, Type type) {
		this.value = value;
		this.move = move;
		this.type = type;
	}

	public LinkedNode(float value, Move move, Type type, Board board) {
		this.value = value;
		this.move = move;
		this.type = type;
		this.board = board;
	}
	
	public LinkedNode(Move move, int type) {
		this.move = move;
		this.type = Type.values()[type];
		this.value = Node.VALUES[type];
	}
	
	public LinkedNode(Move move, int type, Board board) {
		this.move = move;
		this.type = Type.values()[type];
		this.value = Node.VALUES[type];
		this.board = board;
	}

	@Override
	public float getValue() {
		return value;
	}

	@Override
	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public Move getMove() {
		return move;
	}

	@Override
	public List<Node> getChilds() {
		return childs;
	}

	@Override
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public void setChilds(List<Node> childs) {
		this.childs = childs;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + ((childs == null) ? 0 : childs.hashCode());
		result = prime * result + ((move == null) ? 0 : move.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkedNode other = (LinkedNode) obj;
		
		if(this.value != other.value) 
			return false;

		if(this.type != other.type) 
			return false;
		
		if(!(this.move == null && other.move == null))
		{
			if(this.move == null || other.move == null) 
			{
				return false;
			}
			
			if(!this.move.equals(other.move))
			{
				return false;
			}
		}
		
		if(!this.board.equals(other.board)) 
		{
			return false;
		}

		if(!(this.childs == null && other.childs == null))
		{
			if(this.childs == null || other.childs == null)
			{
				return false;
			}
			
			if(this.childs.size() != other.childs.size()) 
			{
				return false;
			}
			
			for(int i = 0; i < this.childs.size(); i++) 
			{
				if(!other.childs.contains(this.childs.get(i))) 
					return false;
			}
			
		}
		
		return true;
	}

	@Override
	public int compareTo(Node o) {
		if(o.getValue() < this.getValue()) {
			return -1;
		}
		if(o.getValue() > this.getValue()) {
			return 1;
		}
		return 0;
	}

	
}
