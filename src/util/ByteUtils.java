package util;

public final class ByteUtils {

	private ByteUtils() {
	}

	public static byte getValue(byte param) {
		return (byte) (param & 3);
	}

	public static byte getType(byte param) {
		return (byte) ((param & 64) >> 6);
	}

	public static byte increment(byte param, byte type) {
		return (byte)((type << 6) | (ByteUtils.getValue(param) + 1));
	}
		
	public static byte decrement(byte param, byte type) {
		return (byte)((ByteUtils.getValue(param) - 1) | ( ( (ByteUtils.getValue(param) >> 1) & type ) << 6));
	}
}
