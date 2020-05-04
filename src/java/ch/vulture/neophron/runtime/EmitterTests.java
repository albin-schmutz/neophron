package ch.vulture.neophron.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmitterTests {

	private final Mem mem = new Mem(100);

	@Test
	void offs_forward___BGE_1___0000004d() {
		var emitter = new Emitter(mem, 0);
		emitter.offs(Ops.BGE, 1);
		assertEquals(0x0000004d, mem.getInt(0));
	}

	@Test
	void offs_backward___pc4_CALL_Minus2___ffffff89() {
		var emitter = new Emitter(mem, 4);
		emitter.offs(Ops.CALL, -2);
		assertEquals(0xffffff89, mem.getInt(4));
	}

	@Test
	void abc_regFormat___MOD_R0_SP_CP___0002b41d() {
		var emitter = new Emitter(mem, 0);
		emitter.abc(Ops.MOD, 0, Regs.SP, Regs.CP);
		assertEquals(0x0002b41d, mem.getInt(0));
	}

	@Test
	void abc_movFormat___MVN_R9_1_SP___00034659() {
		var emitter = new Emitter(mem, 0);
		emitter.abc(Ops.MVN, 9, 1, Regs.SP);
		assertEquals(0x00034659, mem.getInt(0));
	}

	@Test
	void abc_smallFormat___SUBI_R0_PC_Minus8748___f7753c27() {
		var emitter = new Emitter(mem, 0);
		emitter.abc(Ops.SUBI, 0, Regs.PC, -8748);
		assertEquals(0xf7753c27, mem.getInt(0));
	}

	@Test
	void abc_sysFormat___R0_Minus555___ff75402f() {
		var emitter = new Emitter(mem, 0);
		emitter.abc(Ops.SYS, 0, Regs.SP, -555);
		assertEquals(0xff75402f, mem.getInt(0));
	}

	@Test
	void abc_bigFormat___MULI_R0_PC_minus38984498748___00003c33() {
		var emitter = new Emitter(mem, 0);
		emitter.abc(Ops.MULI, 0, 15, -38984498748L);
		assertEquals(0x00003c33, mem.getInt(0));
	}

	@Test
	void abc_offs___multipleEmits___emited() {
		var emitter = new Emitter(mem, 4);
		emitter.abc(Ops.MULI, 0, 15, -38984498748L);
		emitter.offs(Ops.BGE, 1);
		assertEquals(0, mem.getInt(0));
		assertEquals(0x00003c33, mem.getInt(4));
		assertEquals(-38984498748L, mem.getLong(8));
		assertEquals(0x0000004d, mem.getInt(16));
	}
}
