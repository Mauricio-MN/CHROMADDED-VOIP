package pasaud.voip.types;

public class CoordFloat {

	private float x,y,z;
	
	public CoordFloat(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public CoordFloat(CoordFloat coord) {
		setCoord(coord);
	}
	
	public void setCoord(CoordFloat coord) {
		this.x = coord.getX();
		this.y = coord.getY();
		this.z = coord.getZ();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
