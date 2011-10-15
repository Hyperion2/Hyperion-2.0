package org.hyperion.rs2.content;


/**
 * Didn't want to create a subClass.
 * @author phil
 *
 */
public class Projectile {
	
	private short gfx;
	private byte[] height;
	private byte delay, slowness, radius, curve;

	/**
	 * @return the gfx
	 */
	public short getGfx() {
		return gfx;
	}

	/**
	 * @return the height
	 */
	public byte[] getHeight() {
		return height;
	}

	/**
	 * @return the delay
	 */
	public byte getDelay() {
		return delay;
	}

	/**
	 * @return the slowness
	 */
	public byte getSlowness() {
		return slowness;
	}

	/**
	 * @return the radius
	 */
	public byte getRadius() {
		return radius;
	}

	/**
	 * @return the curve
	 */
	public byte getCurve() {
		return curve;
	}
}
