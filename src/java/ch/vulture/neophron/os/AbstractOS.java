package ch.vulture.neophron.os;

public abstract class AbstractOS implements Exit, Input, Output {

	public static final int WordSize = 8;
	public static final int InstrSize = 4;

	public static final long MaxInt = 0x7FFFFFFFFFFFFFFFL;
	public static final long MinInt = -MaxInt - 1;
}
