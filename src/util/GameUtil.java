package util;

public class GameUtil
{
	public static final String[][] OPENING_STRATEGIES = {
			{"G1,NE", "G8,NW"},
			{"A1,SE", "A8,SW"}
	};
	
	public static final int BLACK = 0x1,
							WHITE = 0x0;
	
	public static final byte WHITE_STONE = (byte) 1,
							 BLACK_STONE = (byte) 65,
							 WHITE_TOWER = (byte) 2,
							 BLACK_TOWER = (byte) 66,
							 EMPTY = (byte) 0;
	
	public static final byte getOpponentStone(byte type)
	{
		return (byte) ((1 - type) << 6 | 1);
	}

	public static final byte getOurStone(byte type)
	{
		return (byte) (type << 6 | 1);
	}
	
	
}
