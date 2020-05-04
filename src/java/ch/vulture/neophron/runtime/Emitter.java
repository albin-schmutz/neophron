package ch.vulture.neophron.runtime;

import ch.vulture.neophron.os.AbstractOS;

class Emitter {

	private final Mem mem;

	private int pc;

	Emitter(Mem mem, int pc) {
		this.mem = mem;
		this.pc = pc;
	}

	void offs(int oc, int offs) {
		if (!Ops.valid(oc) || oc > Ops.BGT || !between(offs, 0x2000000)) {
			return;
		}
		var instr = oc + (offs << Ops.BIT_OC);
		put(instr);
	}

	void abc(int oc, int a, int b, long c) {
		if (!Ops.valid(oc) || oc <= Ops.BGT || !Regs.valid(a) || !Regs.valid(b)) {
			return;
		}
		if (oc <= Ops.SUB && Regs.valid((int)c)) {
			var instr = oc +
				(a << Ops.BIT_OC) +
				(b << (Ops.BIT_OC + Ops.BIT_REG)) +
				((int)c << (Ops.BIT_OC + Ops.BIT_REG + Ops.BIT_REG));
			put(instr);
			return;
		}
		if (oc <= Ops.SYS && between(c, 0x20000)) {
			if (oc == Ops.SYS) {
				b = 0;
			}
			var instr = oc +
				(a << Ops.BIT_OC) +
				(b << (Ops.BIT_OC + Ops.BIT_REG)) +
				((int)c << (Ops.BIT_OC + Ops.BIT_REG + Ops.BIT_REG));
			put(instr);
			return;
		}
		if (oc <= Ops.STW) {
			oc += 16;
		}
		var instr = oc +
			(a << Ops.BIT_OC) +
			(b << (Ops.BIT_OC + Ops.BIT_REG));
		put(instr, c);
	}

	private void put(int instr, long imm) {
		put(instr);
		mem.putLong(pc, imm);
		pc += AbstractOS.WordSize;
	}

	private void put(int instr) {
		mem.putInt(pc, instr);
		pc += AbstractOS.InstrSize;
	}

	private boolean between(long x, long limit) {
		return x >= -limit && x < limit;
	}
}
