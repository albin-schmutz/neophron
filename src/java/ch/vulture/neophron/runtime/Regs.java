package ch.vulture.neophron.runtime;

class Regs {

	static final String[] texts = new String[] {
		"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7",
		"R8", "R9", "CP", "GP", "FP", "SP", "RT", "PC"
	};

	static final int R0 = 0;
	static final int R1 = 1;
	static final int R2 = 2;
	static final int R3 = 3;
	static final int R4 = 4;
	static final int R5 = 5;
	static final int R6 = 6;
	static final int R7 = 7;
	static final int R8 = 8;
	static final int R9 = 9;
	static final int CP = 10; // compare register
	static final int GP = 11; // global pointer
	static final int FP = 12; // frame pointer
	static final int SP = 13; // stack pointer
	static final int RT = 14; // return pointer
	static final int PC = 15; // program counter

	static boolean valid(int reg) {
		return reg >= 0 && reg <= Ops.BIT_REG_MASK;
	}

	private Regs() {
		throw new IllegalStateException("Utility class");
	}
}
