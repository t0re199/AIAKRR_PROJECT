package configuration;

public class Configuration
{
	private static Configuration instance;
	
	
	private int base[] = {0, 6};
	
	private int movement[] = {1, -1};
	
	private int multipliers[] = new int[0x2];
	
	private int maximizerColor;
	
	
	private Configuration(int maximizerColor)
	{
		this.maximizerColor = maximizerColor;
		multipliers[maximizerColor] = 0x1;
		multipliers[0x1 - maximizerColor] = -0x1;
	}
	
	public int getMultiplier(int color)
	{
		return multipliers[color];
	}
	
	public int getMovement(int color)
	{
		return movement[color];
	}
	
	
	public int colorToType(int color)
	{
		return multipliers[color] == 0x1 ? 0x1 : -0x1;
	}
	
	public int getMaximizerMultiplier()
	{
		return multipliers[maximizerColor];
	}
	
	public int getMinimizerMultiplier()
	{
		return multipliers[0x1 - maximizerColor];
	}
	
	public int getBase(int color)
	{
		return base[color];
	}
	
	public int getMaximizerColor()
	{
		return maximizerColor;
	}
	
	public static Configuration getInstance()
	{
		return instance;
	}
	
	
	public static void setup(int maximizerColor) { 
		Configuration.instance = new Configuration(maximizerColor);
	}
}
