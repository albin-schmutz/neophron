package ch.vulture.neophron.runtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MemTests {

	private final Mem mem = new Mem(1000);

	@Test
	void putByte___255___getByteIs255() {
		var addr = 100;
		mem.putByte(addr, 255);
		assertEquals(255, mem.getByte(addr));
	}

	@Test
	void putByte___257___getByteIs1() {
		var addr = 100;
		mem.putByte(addr, 257);
		assertEquals(1, mem.getByte(addr));
	}

	@Test
	void putByte___08_09_aa_c0___getIntIs0809AAC0() {
		var addr = 200;
		var bytes = new int[] { 0x08, 0x09, 0xaa, 0xc0 };
		for (int i = 0, n = bytes.length; i < n; ++i)
			mem.putByte(addr + i, bytes[i]);
		assertEquals(0x0809aac0, mem.getInt(addr));
	}

	@Test
	void putInt___257___getIntIs257() {
		var addr = 100;
		mem.putInt(addr, 257);
		assertEquals(257, mem.getInt(addr));
	}

	@Test
	void putInt___minus257___getIntIsMinus257() {
		var addr = 100;
		mem.putInt(addr, -257);
		assertEquals(-257, mem.getInt(addr));
	}

	@Test
	void putLong___minus257945849823423___getLongIsMinus257945849823423() {
		var addr = 100;
		mem.putLong(addr, -257945849823423L);
		assertEquals(-257945849823423L, mem.getLong(addr));
	}
}
