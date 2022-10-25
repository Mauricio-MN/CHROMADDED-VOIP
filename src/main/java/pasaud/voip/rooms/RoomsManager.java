package pasaud.voip.rooms;

import java.util.concurrent.ConcurrentHashMap;

public final class RoomsManager {
	
	private static ConcurrentHashMap<Integer, Room> rooms;

	private RoomsManager() {
		
	}
	
	public static void init() {
		rooms = new ConcurrentHashMap<>();
	}
	
	public static void addRoom(int id) {
		if(!haveRoom(id)) {
			rooms.put(id, new Room(id));
		}
	}
	
	public static void closeRoom(int id) {
		if(haveRoom(id)) {
			rooms.get(id).clearRoom();
			rooms.remove(id);
		}
	}
	
	public static Room getRoom(int id) {
		if(haveRoom(id)) {
			return rooms.get(id);
		}
		return null;
	}
	
	public static boolean haveRoom(int id) {
		if(rooms.containsKey(id)) {
			return true;
		}
		return false;
	}
}
