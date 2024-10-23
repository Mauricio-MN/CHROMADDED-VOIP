package pasaud.voip.types;

public class Coord {

	private int x,y,z;
	
	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Coord(Coord coord) {
		setCoord(coord);
	}
	
	public void setCoord(Coord coord) {
		this.x = coord.getX();
		this.y = coord.getY();
		this.z = coord.getZ();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
}
