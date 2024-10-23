package pasaud.voip.muonline.database;

import java.util.ArrayList;

public class RoomDTO {
	
	private int id;
	private int type;

	public RoomDTO() {
		id = -1;
		type = 0;
	}
	
	public boolean isValid() {
		if(id != -1) return true;
		return false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
