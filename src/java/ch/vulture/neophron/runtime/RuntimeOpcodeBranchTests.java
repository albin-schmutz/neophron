package ch.vulture.neophron.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuntimeOpcodeBranchTests {

	private final Runtime runtime = new Runtime();
	private final Emitter emitter = runtime.createEmitter();

	// JUMP, CALL

	@Test
	void run___JUMP_1_ADDI_R0_1_ADDI_R0_2___R0is2() {
		emitter.offs(Ops.JUMP, 1); // jump one instruction forward
		emitter.abc(Ops.ADDI, Regs.R0, 0, 1); // over jumped
		emitter.abc(Ops.ADDI, Regs.R0, 0, 2); // jump target
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0); // exit
		runtime.run();
		// assert R0 = 2
		runtime.run(Ops.SUBI, Regs.CP, Regs.R0, 2);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___CALL_1_ADDI_R0_1_ADDI_R0_2___RTisGPplus4() {
		emitter.offs(Ops.CALL, 1); // jump one instruction forward
		emitter.abc(Ops.ADDI, Regs.R0, 0, 1); // over jumped
		emitter.abc(Ops.ADDI, Regs.R0, 0, 2); // jump target
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0); // exit
		runtime.run();
		// assert GP + 4 = RT
		runtime.run(Ops.ADDI, Regs.R9, Regs.GP, 4);
		runtime.run(Ops.CMP, Regs.R9, 0, Regs.RT);
		assertTrue(runtime.assertCPIsZero());
	}

	// BEQ, BNE, BLS, BGE, BLE, BGT

	@Test
	void run___ADDI_R0_1_loopWhileBNE10___R0is10() {
		emitter.abc(Ops.ADDI, Regs.R0, 0, 1);
		emitter.abc(Ops.CMPI, Regs.R0, 0, 10);
		emitter.offs(Ops.BNE, -3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R0 = 10
		runtime.run(Ops.SUBI, Regs.CP, Regs.R0, 10);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___ADDI_R0_1_loopWhileBEQ1___R0is2() {
		emitter.abc(Ops.ADDI, Regs.R0, 0, 1);
		emitter.abc(Ops.CMPI, Regs.R0, 0, 1);
		emitter.offs(Ops.BEQ, -3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R0 = 2
		runtime.run(Ops.SUBI, Regs.CP, Regs.R0, 2);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___ADDI_R0_3_loopWhileBLS10___R0is12() {
		emitter.abc(Ops.ADDI, Regs.R0, 0, 3);
		emitter.abc(Ops.CMPI, Regs.R0, 0, 10);
		emitter.offs(Ops.BLS, -3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R0 = 12
		runtime.run(Ops.SUBI, Regs.CP, Regs.R0, 12);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___ADDI_R0_3_loopWhileBLE12___R0is15() {
		emitter.abc(Ops.ADDI, Regs.R0, 0, 3);
		emitter.abc(Ops.CMPI, Regs.R0, 0, 12);
		emitter.offs(Ops.BLE, -3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R0 = 15
		runtime.run(Ops.SUBI, Regs.CP, Regs.R0, 15);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___SUBI_R0_3_loopWhileBGTMinus10___R0isMinus12() {
		emitter.abc(Ops.SUBI, Regs.R0, 0, 3);
		emitter.abc(Ops.CMPI, Regs.R0, 0, -10);
		emitter.offs(Ops.BGT, -3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R0 = -12
		runtime.run(Ops.ADDI, Regs.CP, Regs.R0, 12);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___SUBI_R0_3_loopWhileBGEMinus12___R0isMinus15() {
		emitter.abc(Ops.SUBI, Regs.R0, 0, 3);
		emitter.abc(Ops.CMPI, Regs.R0, 0, -12);
		emitter.offs(Ops.BGE, -3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R0 = -15
		runtime.run(Ops.ADDI, Regs.CP, Regs.R0, 15);
		assertTrue(runtime.assertCPIsZero());
	}
}
