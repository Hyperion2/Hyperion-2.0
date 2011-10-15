package org.hyperion.rs2.model;

/**
 * Represents a single location in the game world.
 * @author Graham Edgecombe
 *
 */
public class Location {
	
	/**
	 * The x coordinate.
	 */
	private final int x;
	
	/**
	 * The y coordinate.
	 */
	private final int y;
	
	/**
	 * The z coordinate.
	 */
	private final int z;
	
	/**
	 * Creates a location.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @return The location.
	 */
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}
	
	/**
	 * Creates a location.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	private Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the absolute x coordinate.
	 * @return The absolute x coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the absolute y coordinate.
	 * @return The absolute y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the z coordinate, or height.
	 * @return The z coordinate.
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * Gets the local x coordinate relative to this region.
	 * @return The local x coordinate relative to this region.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}
	
	/**
	 * Gets the local y coordinate relative to this region.
	 * @return The local y coordinate relative to this region.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}
	
	/**
	 * Gets the local x coordinate relative to a specific region.
	 * @param l The region the coordinate will be relative to.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Location l) {
		return x - 8 * l.getRegionX();
	}
	
	/**
	 * Gets the Location of the Entity based on their size.
	 * @param size The size.
	 * @return New location.
	 */
	public Location applySizeDistoration(int size) {
		int distort = (int) Math.floor((size-1)/2);
		return create(x+distort, y+distort, z);
	}
	
	/**
	 * Gets the local y coordinate relative to a specific region.
	 * @param l The region the coordinate will be relative to.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Location l) {
		return y - 8 * l.getRegionY();
	}
	
	/**
     * Checks if a specific location is within a specific radius.
     * @param rad The radius.
     * @return True if we're within distance/range, false if not.
     */
	public boolean withinRange(Location p, int rad) {
		if (p == null) {
			return false;
		}
		int dX = Math.abs(x - p.x);
		int dY = Math.abs(y - p.y);
		return dX <= rad && dY <= rad;
	}
	
	/**
	 * Gets the region x coordinate.
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}
	
	/**
	 * Gets the region y coordinate.
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}
	
	/**
	 * Checks if this location is within range of another.
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range,
	 * <code>false</code> if not.
	 */
	public boolean isWithinDistance(Location other) {
		if(z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}
	
	/**
	 * Checks if this location is within interaction range of another.
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range,
	 * <code>false</code> if not.
	 */
	public boolean isWithinInteractionDistance(Location other) {
		if(z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
	}
	
	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}
	
	/**
	 * Gets the distance from a point.
	 * @param other The target.
	 * @return blah distance.
	 */
	public double getDistanceFromLocation(Location other) {
    	return Math.round(Math.sqrt(((other.getX() - getX()) ^ 2) + ((other.getY() - getY()) ^ 2)));
    }
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+","+z+"]";
	}

	/**
	 * Creates a new location based on this location.
	 * @param diffX X difference.
	 * @param diffY Y difference.
	 * @param diffZ Z difference.
	 * @return The new location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return Location.create(x + diffX, y + diffY, z + diffZ);
	}

}
