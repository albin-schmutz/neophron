package ch.vulture.neophron.runtime;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Mem {

	private final ByteBuffer data;

	Mem(int size) {
		data = ByteBuffer.allocate(size);
		data.order(ByteOrder.BIG_ENDIAN);
	}

	int getByte(int addr) {
		return Byte.toUnsignedInt(data.get(addr));
	}

	void putByte(int addr, int value) {
		data.put(addr, (byte)value);
	}

	int getInt(int addr) {
		return data.getInt(addr);
	}

	void putInt(int addr, int value) {
		data.putInt(addr, value);
	}

	long getLong(int addr) {
		return data.getLong(addr);
	}

	void putLong(int addr, long value) {
		data.putLong(addr, value);
	}
}
