package ch.vulture.neophron.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuntimeOpcodeMemTests {

	private final Runtime runtime = new Runtime();
	private final Emitter emitter = runtime.createEmitter();

	// POP, PSH, LDB, LDW, STB, STW

	/* SP
	 * 7   AA
	 * 6   25
	 * 5   C2
	 * 4   8D
	 * 3   54
	 * 2   AF
	 * 1   00
	 * 0   00
	 */
	@Test
	void run___PSH_R0_AF548DC225AA_POP_R1___R1is0AF548DC225AAH() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, 0xaf548dc225aaL);
		emitter.abc(Ops.PSH, Regs.R0, Regs.SP, -8);
		emitter.abc(Ops.POP, Regs.R1, Regs.SP, 8);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R1 = 0AF548DC225AAH
		runtime.run(Ops.SUBI, Regs.CP, Regs.R1, 0xaf548dc225aaL);
		assertTrue(runtime.assertCPIsZero());
	}

	/* SP
	 * 7   AA
	 * 6   25
	 * 5   C2
	 * 4   8D
	 * 3   54
	 * 2   AF
	 * 1   00
	 * 0   00
	 */
	@Test
	void run___STW_AF548DC225AA_LDB_R1_3___R1is54H() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, 0xaf548dc225aaL);
		emitter.abc(Ops.STW, Regs.R0, Regs.SP, -8);
		emitter.abc(Ops.LDB, Regs.R1, Regs.SP, -5);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R1 = 54H
		runtime.run(Ops.SUBI, Regs.CP, Regs.R1, 0x54);
		assertTrue(runtime.assertCPIsZero());
	}

	/* SP
	 * 7   00
	 * 6   AF
	 * 5   00
	 * 4   00
	 * 3   00
	 * 2   00
	 * 1   00
	 * 0   00
	 */
	@Test
	void run___STB_AF_6_LDW_R1___R1is0AF00H() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, 0);
		emitter.abc(Ops.STW, Regs.R0, Regs.SP, -8);
		emitter.abc(Ops.MOVI, Regs.R0, 0, 0xaf);
		emitter.abc(Ops.STB, Regs.R0, Regs.SP, -2);
		emitter.abc(Ops.LDW, Regs.R1, Regs.SP, -8);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R1 = 0AF00H
		runtime.run(Ops.SUBI, Regs.CP, Regs.R1, 0xaf00);
		assertTrue(runtime.assertCPIsZero());
	}
}
