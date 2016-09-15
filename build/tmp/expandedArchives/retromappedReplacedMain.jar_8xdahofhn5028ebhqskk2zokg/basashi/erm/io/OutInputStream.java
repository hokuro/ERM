package basashi.erm.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class OutInputStream extends OutputStream {
	/**
	 * データを貯めこむバイト配列ストリーム.
	 */
	private final ByteArrayOutputStream inner = new ByteArrayOutputStream();

	@Override
	public void write(final int b) throws IOException {
		inner.write(b);
	}

	/**
	 * 入力ストリームを生成して返す.
	 * @return 入力ストリーム
	 */
	public InputStream getInputStream() {
		return new ByteArrayInputStream(inner.toByteArray());
	}
}