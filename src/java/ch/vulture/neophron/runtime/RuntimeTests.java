package ch.vulture.neophron.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuntimeTests {

	/*
	 * minimal memory model
	 *
	 * R8           ---------   8192
	 *              |       |
	 *              |  dm   |
	 *              |       |
	 * R9           ---------   5120
	 *              |       |
	 * PC      -->  |  pm   |
	 *              |       |
	 * GP      -->  ---------   4096
	 *              |       |
	 * SP      -->  |  sm   |
	 *              |       |
	 *              ---------
	 */

	@Test
	void constructor___TooSmallSizeInits___MinimalMemoryModelWith_SM4096_PM1024_DM3072() {
		var runtime = new Runtime();
		// assert R8 = 2000H (8192)
		runtime.run(Ops.SUBI, Regs.CP, Regs.R8, 0x2000);
		assertTrue(runtime.assertCPIsZero());
		// assert R9 = 1400H (5120)
		runtime.run(Ops.SUBI, Regs.CP, Regs.R9, 0x1400);
		assertTrue(runtime.assertCPIsZero());
		// assert GP = 1000H (4096)
		runtime.run(Ops.SUBI, Regs.CP, Regs.GP, 0x1000);
		assertTrue(runtime.assertCPIsZero());
		// assert SP = 1000H (4096)
		runtime.run(Ops.SUBI, Regs.CP, Regs.SP, 0x1000);
		assertTrue(runtime.assertCPIsZero());
		// assert PC = 1000H (4096)
		runtime.run(Ops.SUBI, Regs.CP, Regs.PC, 0x1000);
		assertTrue(runtime.assertCPIsZero());
	}

	@Test
	void constructor___512_128_512___MemoryModelWith_SM4096_PM1024_DM4096() {
		var runtime = new Runtime(512, 128, 512);
		// assert R8 = 2400H (9216)
		runtime.run(Ops.SUBI, Regs.CP, Regs.R8, 0x2400);
		assertTrue(runtime.assertCPIsZero());
		// assert R9 = 1400H (5120)
		runtime.run(Ops.SUBI, Regs.CP, Regs.R9, 0x1400);
		assertTrue(runtime.assertCPIsZero());
		// assert GP = 1000H (4096)
		runtime.run(Ops.SUBI, Regs.CP, Regs.GP, 0x1000);
		assertTrue(runtime.assertCPIsZero());
		// assert SP = 1000H (4096)
		runtime.run(Ops.SUBI, Regs.CP, Regs.SP, 0x1000);
		assertTrue(runtime.assertCPIsZero());
		// assert PC = 1000H (4096)
		runtime.run(Ops.SUBI, Regs.CP, Regs.PC, 0x1000);
		assertTrue(runtime.assertCPIsZero());
	}
}
