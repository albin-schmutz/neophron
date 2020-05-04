package ch.vulture.neophron.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuntimeOpcodeCalcTests {

	private final Runtime runtime = new Runtime();
	private final Emitter emitter = runtime.createEmitter();

	// MOV, MOVI, MOVI2

	@Test
	void run___MOVI_R7_9_128___R7is65536() {
		emitter.abc(Ops.MOVI, Regs.R7, 9, 128);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R7 = 65536
		runtime.run(Ops.SUBI, Regs.CP, Regs.R7, 65536);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R7_MinInt___R7isMinInt() {
		emitter.abc(Ops.MOVI, Regs.R7, 0, Long.MIN_VALUE);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R7 = Long.MIN_VALUE
		runtime.run(Ops.ADDI, Regs.CP, Regs.R7, Long.MAX_VALUE);
		runtime.run(Ops.ADDI, Regs.CP, Regs.CP, 1);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R6_Minus1000_MOV_R8_1_R7___R8isMinus2000() {
		emitter.abc(Ops.MOVI, Regs.R7, 0, -1000);
		emitter.abc(Ops.MOV, Regs.R8, 1, Regs.R7);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R8 = -2000
		runtime.run(Ops.SUBI, Regs.CP, Regs.R8, -2000);
		assertTrue(runtime.assertCPIsZero());
	}

	// MVN, MVNI, MVNI2

	@Test
	void run___MVNI_R7_8_256___R7isMinus65536() {
		emitter.abc(Ops.MVNI, Regs.R7, 8, 256);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R7 = -65536
		runtime.run(Ops.SUBI, Regs.CP, Regs.R7, -65536);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MVNI2_R7_MaxInt___R7isMinIntPlus1() {
		emitter.abc(Ops.MVNI2, Regs.R7, 0, Long.MAX_VALUE);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R7 = Long.MIN_VALUE + 1
		runtime.run(Ops.ADDI, Regs.CP, Regs.R7, Long.MAX_VALUE);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R6_Minus1000_MVN_R8_1_R7___R8is2000() {
		emitter.abc(Ops.MOVI, Regs.R7, 0, -1000);
		emitter.abc(Ops.MVN, Regs.R8, 1, Regs.R7);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R8 = 2000
		runtime.run(Ops.ADDI, Regs.CP, Regs.R8, -2000);
		assertTrue(runtime.assertCPIsZero());
	}

	// CMP, CMPI, CMPI2

	@Test
	void run___MOVI_R5_200000000000_CMPI_R5_199999999999___CPis1() {
		emitter.abc(Ops.MOVI2, Regs.R5, 0, 200000000000L);
		emitter.abc(Ops.CMPI, Regs.R5, 0, 199999999999L);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert CP = 1
		runtime.run(Ops.ADDI, Regs.CP, Regs.CP, -1);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R0_1_MOVI_R1_9_CMP_R0_R1___CPisMinus8() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, 1);
		emitter.abc(Ops.MOVI, Regs.R1, 0, 9);
		emitter.abc(Ops.CMP, Regs.R0, 0, Regs.R1);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert CP = -8
		runtime.run(Ops.SUBI, Regs.CP, Regs.CP, -8);
		assertTrue(runtime.assertCPIsZero());
	}

	// MUL, MULI, MULI2

	@Test
	void run___MOVI_R0_MinIntDiv4_MULI_R1_R0_4___R1isMinInt() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, Long.MIN_VALUE / 4);
		emitter.abc(Ops.MULI, Regs.R1, Regs.R0, 4);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R1 = Long.MIN_VALUE
		runtime.run(Ops.ADDI, Regs.CP, Regs.R1, Long.MIN_VALUE);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R0_MinIntDiv8_MOVI_R1_8_MUL_R2_R0_R1___R2isMinInt() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, Long.MIN_VALUE / 8);
		emitter.abc(Ops.MOVI, Regs.R1, 0, 8);
		emitter.abc(Ops.MUL, Regs.R2, Regs.R0, Regs.R1);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R2 = Long.MIN_VALUE
		runtime.run(Ops.ADDI, Regs.CP, Regs.R2, Long.MIN_VALUE);
		assertTrue(runtime.assertCPIsZero());
	}

	// DIV, DIVI, DIVI2

	@Test
	void run___MOVI_R0_5555556_DIVI_R1_R0_5___R1is1111111() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, 5555556);
		emitter.abc(Ops.DIVI, Regs.R1, Regs.R0, 5);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R1 = 1111111
		runtime.run(Ops.ADDI, Regs.CP, Regs.R1, -1111111);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R0_MinIntDiv8_MOVI_R1_MinIntDiv32_DIV_R2_R0_R1___R2is4() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, Long.MIN_VALUE / 8);
		emitter.abc(Ops.MOVI, Regs.R1, 0, Long.MIN_VALUE / 32);
		emitter.abc(Ops.DIV, Regs.R2, Regs.R0, Regs.R1);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R2 = 4
		runtime.run(Ops.SUBI, Regs.CP, Regs.R2, 4);
		assertTrue(runtime.assertCPIsZero());
	}

	// MOD, MODI, MODI2

	@Test
	void run___MOVI_R0_5555556_MODI_R1_R0_5___R1is1() {
		emitter.abc(Ops.MOVI, Regs.R0, 0, 5555556);
		emitter.abc(Ops.MODI, Regs.R1, Regs.R0, 5);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R1 = 1
		runtime.run(Ops.SUBI, Regs.CP, Regs.R1, 1);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R0_45A34503487BCF_MOVI_R1_256_MOD_R2_R0_R1___R2isCF() {
		// x MOD 256 simulates x & 0xff 
		emitter.abc(Ops.MOVI, Regs.R0, 0, 0x45a34503487bcfL);
		emitter.abc(Ops.MOVI, Regs.R1, 8, 1);
		emitter.abc(Ops.MOD, Regs.R2, Regs.R0, Regs.R1);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R2 = 0CFH
		runtime.run(Ops.SUBI, Regs.CP, Regs.R2, 0xcf);
		assertTrue(runtime.assertCPIsZero());
	}

	// ADD, ADDI, ADDI2

	@Test
	void run___MOVI_R5_MaxInt_ADDI_R6_R5_2___R6isMinusMaxInt() {
		emitter.abc(Ops.MOVI, Regs.R5, 0, Long.MAX_VALUE);
		emitter.abc(Ops.ADDI, Regs.R6, Regs.R5, 2);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R6 = -Long.MAX_VALUE
		runtime.run(Ops.SUBI, Regs.CP, Regs.R6, -Long.MAX_VALUE);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R4_MinInt_MOVI_R5_MaxInt_ADD_R4_R4_R5___R4isMinus1() {
		emitter.abc(Ops.MOVI, Regs.R4, 0, Long.MIN_VALUE);
		emitter.abc(Ops.MOVI, Regs.R5, 0, Long.MAX_VALUE);
		emitter.abc(Ops.ADD, Regs.R4, Regs.R4, Regs.R5);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R4 = -1
		runtime.run(Ops.SUBI, Regs.CP, Regs.R4, -1);
		assertTrue(runtime.assertCPIsZero());
	}

	// SUB, SUBI, SUBI2

	@Test
	void run___MOVI_R5_MaxInt_SUBI_R6_R5_MaxIntMinus3___R6is3() {
		emitter.abc(Ops.MOVI, Regs.R5, 0, Long.MAX_VALUE);
		emitter.abc(Ops.SUBI, Regs.R6, Regs.R5, Long.MAX_VALUE - 3);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R6 = 3
		runtime.run(Ops.ADDI, Regs.CP, Regs.R6, -3);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void run___MOVI_R4_MinInt_MOVI_R5_MaxInt_SUB_R4_R4_R5___R4is1() {
		emitter.abc(Ops.MOVI, Regs.R4, 0, Long.MIN_VALUE);
		emitter.abc(Ops.MOVI, Regs.R5, 0, Long.MAX_VALUE);
		emitter.abc(Ops.SUB, Regs.R4, Regs.R4, Regs.R5);
		emitter.abc(Ops.POP, Regs.PC, Regs.SP, 0);
		runtime.run();
		// assert R4 = 3
		runtime.run(Ops.ADDI, Regs.CP, Regs.R4, -1);
		assertTrue(runtime.assertCPIsZero());
	}
}
