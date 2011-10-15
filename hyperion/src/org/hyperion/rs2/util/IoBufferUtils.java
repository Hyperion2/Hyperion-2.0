package org.hyperion.rs2.util;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * A utility class for dealing with <code>IoBuffer</code>s.
 * @author Graham Edgecombe
 *
 */
public class IoBufferUtils {
	
	/**
	 * Reads a RuneScape string from a buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String getRS2String(IoBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(buf.hasRemaining() && (b = buf.get()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	/**
	 * Writes a RuneScape string to a buffer.
	 * @param buf The buffer.
	 * @param string The string.
	 */
	public static void putRS2String(IoBuffer buf, String string) {
		buf.put(string.getBytes());
		buf.put((byte) 10);
	}
	
	/**
	 * Reads a RuneScape string from a buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static void putRS2String(ByteBuffer buffer, String string) {
		buffer.put(string.getBytes());
		buffer.put((byte) 10);
	}

	/**
	 * Writes a RuneScape string to a buffer.
	 * @param buf The buffer.
	 * @param string The string.
	 */
	public static String getRS2String(ByteBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while(buffer.remaining() > 0 && (b = buffer.get()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
}
