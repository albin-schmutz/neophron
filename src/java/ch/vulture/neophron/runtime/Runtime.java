package ch.vulture.neophron.runtime;

import ch.vulture.neophron.os.AbstractOS;

/*
 * memory model
 *
 * R8           ---------
 *              |       |
 *              |  dm   |      dynamic memory (heap)
 *              |       |
 * R9           ---------
 *              |       |
 * PC      -->  |  pm   |      program memory
 *              |       |
 * GP      -->  ---------
 *              |       |      static memory
 * SP      -->  |  sm   |      (global variables & stack)
 *              |       |
 *              ---------
 */

class Runtime {

	private final Mem mem;
	private final long[] regs = new long[16];

	Runtime() {
		this(0, 0, 0);
	}

	Runtime(int smSizeWords, int pgSizeWords, int dmSizeWords) {
		if (smSizeWords < 1) smSizeWords = 512;
		if (pgSizeWords < 1) pgSizeWords = 128;
		if (dmSizeWords < 1) dmSizeWords = 384;
		var smSize = smSizeWords * AbstractOS.WordSize;
		var pmSize = pgSizeWords * AbstractOS.WordSize;
		var dmSize = dmSizeWords * AbstractOS.WordSize;
		var memSize = smSize + pmSize + dmSize; 
		mem = new Mem(memSize);
		regs[Regs.GP] = smSize;
		regs[Regs.SP] = regs[Regs.GP];
		regs[Regs.PC] = regs[Regs.GP];
		regs[Regs.R9] = regs[Regs.GP] + pmSize;
		regs[Regs.R8] = memSize;
	}

	Emitter createEmitter() {
		return new Emitter(mem, (int)regs[Regs.PC]);
	}

	void run() {
		// push return address 0
		regs[Regs.SP] -= AbstractOS.WordSize;
		mem.putLong((int)regs[Regs.SP], 0);
		// run loop
		while (regs[Regs.PC] != 0) {
			var pc = (int)regs[Regs.PC];
			regs[Regs.PC] += AbstractOS.InstrSize;
			var instr = mem.getInt(pc);
			run(instr);
		}
	}

	void run(int instr) {
		var oc = instr & Ops.BIT_OC_MASK;
		if (Ops.valid(oc)) {
			if (oc <= Ops.BGT) {
				var c = instr >> Ops.BIT_OC;
				run(oc, 0, 0, c);
			} else {
				var a = (instr >> Ops.BIT_OC) & Ops.BIT_REG_MASK;
				var b = (instr >> (Ops.BIT_OC + Ops.BIT_REG)) & Ops.BIT_REG_MASK;
				if (oc <= Ops.SYS) {
					var c = (instr >> (Ops.BIT_OC + Ops.BIT_REG + Ops.BIT_REG));
					run(oc, a, b, c);
				} else {
					var c = mem.getLong((int)regs[Regs.PC]);
					regs[Regs.PC] += AbstractOS.WordSize;
					run(oc - 16, a, b, c);
				}
			}
		}
	}

	void run(int oc, int a, int b, long c) {
		switch (oc) {
		case Ops.JUMP:
			regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.CALL:
			regs[Regs.RT] = regs[Regs.PC];
			regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.BEQ:
			if (regs[Regs.CP] == 0) regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.BNE:
			if (regs[Regs.CP] != 0) regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.BLS:
			if (regs[Regs.CP] < 0) regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.BGE:
			if (regs[Regs.CP] >= 0) regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.BLE:
			if (regs[Regs.CP] <= 0) regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.BGT:
			if (regs[Regs.CP] > 0) regs[Regs.PC] += c * AbstractOS.InstrSize;
			break;
		case Ops.MOV:
			regs[a] = regs[(int)c] << b;
			break;
		case Ops.MVN:
			regs[a] = -(regs[(int)c] << b);
			break;
		case Ops.CMP:
			regs[Regs.CP] = regs[a] - regs[(int)c];
			break;
		case Ops.MUL:
			regs[a] = regs[b] * regs[(int)c];
			break;
		case Ops.DIV:
			regs[a] = regs[b] / regs[(int)c];
			break;
		case Ops.MOD:
			regs[a] = regs[b] % regs[(int)c];
			break;
		case Ops.ADD:
			regs[a] = regs[b] + regs[(int)c];
			break;
		case Ops.SUB:
			regs[a] = regs[b] - regs[(int)c];
			break;
		case Ops.MOVI:
			regs[a] = c << b;
			break;
		case Ops.MVNI:
			regs[a] = -(c << b);
			break;
		case Ops.CMPI:
			regs[Regs.CP] = regs[a] - c;
			break;
		case Ops.MULI:
			regs[a] = regs[b] * c;
			break;
		case Ops.DIVI:
			regs[a] = regs[b] / c;
			break;
		case Ops.MODI:
			regs[a] = regs[b] % c;
			break;
		case Ops.ADDI:
			regs[a] = regs[b] + c;
			break;
		case Ops.SUBI:
			regs[a] = regs[b] - c;
			break;
		case Ops.LDB:
			regs[a] = mem.getByte((int)(regs[b] + c));
			break;
		case Ops.LDW:
			regs[a] = mem.getLong((int)(regs[b] + c));
			break;
		case Ops.STB:
			mem.putByte((int)(regs[b] + c), (byte)regs[a]);
			break;
		case Ops.STW:
			mem.putLong((int)(regs[b] + c), regs[a]);
			break;
		case Ops.POP:
			regs[a] = mem.getLong((int)regs[b]);
			regs[b] += c;
			break;
		case Ops.PSH:
			regs[b] += c;
			mem.putLong((int)regs[b], regs[a]);
			break;
		case Ops.SYS:
			sys((int)c, (int)regs[a]);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	private void sys(int nr, int addr) {
		throw new UnsupportedOperationException(String.format("sys %d %d", nr, addr));
	}

	boolean assertCPIsZero() {
		return regs[Regs.CP] == 0;
	}
}
