package ch.vulture.neophron.runtime;

class Ops {

	static final int BIT_OC = 6;
	static final int BIT_REG_MASK = 0xf;
	static final int BIT_REG = 4;
	static final int BIT_OC_MASK = 0x3f;

	static final String[] texts = new String[] {
		null, null, null, null, null, null, null, null,
		"JUMP", "CALL", "BEQ", "BNE", "BLS", "BGE", "BLE", "BGT",
		null, null, null, null, null, null, null, null,
		"MOV", "MVN", "CMP", "MUL", "DIV", "MOD", "ADD", "SUB",
		"MOVI", "MVNI", "CMPI", "MULI", "DIVI", "MODI", "ADDI", "SUBI",
		"LDB", "LDW", "STB", "STW", "POP", "PSH", null, "SYS",
		"MOVI2", "MVNI2", "CMPI2", "MULI2", "DIVI2", "MODI2", "ADDI2", "SUBI2",
		"LDB2", "LDW2", "STB2", "STW2", null, null, null, null
	};

	// format oc offs

	static final int JUMP = 8; // r[pc] += (offs * InstrSize)
	static final int CALL = 9; // r[rt] = r[pc]; jump

	static final int BEQ = 10; // if r[cp] == 0 r[pc] += (offs * InstrSize)
	static final int BNE = 11; // if r[cp] != 0 r[pc] += (offs * InstrSize)
	static final int BLS = 12; // if r[cp] <  0 r[pc] += (offs * InstrSize)
	static final int BGE = 13; // if r[cp] >= 0 r[pc] += (offs * InstrSize)
	static final int BLE = 14; // if r[cp] <= 0 r[pc] += (offs * InstrSize)
	static final int BGT = 15; // if r[cp] >  0 r[pc] += (offs * InstrSize)

	// format oc a b reg/imm18

	static final int MOV = 24; // r[a] = r[c] << b
	static final int MVN = 25; // r[a] = -(r[c] << b)
	static final int CMP = 26; // r[cp] = r[a] - r[c]
	static final int MUL = 27; // r[a] = r[b] * r[c]
	static final int DIV = 28; // r[a] = r[b] / r[c]
	static final int MOD = 29; // r[a] = r[b] % r[c]
	static final int ADD = 30; // r[a] = r[b] + r[c]
	static final int SUB = 31; // r[a] = r[b] - r[c]

	static final int MOVI = 32; // r[a] = c << b
	static final int MVNI = 33; // r[a] = -(c << b)
	static final int CMPI = 34; // r[cp] = r[a] - c
	static final int MULI = 35; // r[a] = r[b] * c
	static final int DIVI = 36; // r[a] = r[b] / c
	static final int MODI = 37; // r[a] = r[b] % c
	static final int ADDI = 38; // r[a] = r[b] + c
	static final int SUBI = 39; // r[a] = r[b] - c

	static final int LDB = 40; // r[a] = mem[r[b] + c]
	static final int LDW = 41; // r[a] = mem[r[b] + c]
	static final int STB = 42; // mem[r[b] + c] = r[a]
	static final int STW = 43; // mem[r[b] + c] = r[a]

	static final int POP = 44; // r[a] = mem[r[b]]; r[b] += c
	static final int PSH = 45; // r[b] += c; mem[r[b]] = r[a]

	static final int SYS = 47; // sys(c, r[a]) // c: call-nr, r[a]: record address

	// format oc a b imm64

	static final int MOVI2 = 48; // r[a] = c << b
	static final int MVNI2 = 49; // r[a] = -(c << b)
	static final int CMPI2 = 50; // r[cp] = r[a] - c
	static final int MULI2 = 51; // r[a] = r[b] * c
	static final int DIVI2 = 52; // r[a] = r[b] / c
	static final int MODI2 = 53; // r[a] = r[b] % c
	static final int ADDI2 = 54; // r[a] = r[b] + c
	static final int SUBI2 = 55; // r[a] = r[b] - c

	static final int LDB2 = 56; // r[a] = mem[r[b] + c]
	static final int LDW2 = 57; // r[a] = mem[r[b] + c]
	static final int STB2 = 58; // mem[r[b] + c] = r[a]
	static final int STW2 = 59; // mem[r[b] + c] = r[a]

	static boolean valid(int oc) {
		return Ops.texts[oc] != null;
	}

	private Ops() {
		throw new IllegalStateException("Utility class");
	}
}
