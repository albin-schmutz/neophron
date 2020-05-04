package ch.vulture.neophron.os;

import java.io.IOException;

public class DefaultOS extends AbstractOS {

	@Override
	public void exit(int status) throws Exception {
		System.exit(status);
	}

	@Override
	public int getChar() {
		try {
			return System.in.read();
		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public void putChar(int c) {
		System.out.write(c);
	}
}
