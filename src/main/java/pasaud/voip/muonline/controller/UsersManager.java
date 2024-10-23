package pasaud.voip.muonline.controller;

import java.util.ArrayList;
import java.util.HashMap;

import pasaud.voip.muonline.database.RoomDTO;
import pasaud.voip.muonline.database.VoiceDTO;
import pasaud.voip.player.PlayersManager;
import pasaud.voip.rooms.RoomsManager;
import pasaud.voip.player.Player;
import pasaud.voip.player.PlayerNormal;

public class UsersManager implements Runnable {
	
	private HashMap<Integer,VoiceDTO> playersDTO;
	private ArrayList<Integer> playersListID;
	
	private HashMap<Integer,RoomDTO> roomsDTO;
	private ArrayList<Integer> roomsListID;
	
	private DataBaseConnection dataBase;
	
	public UsersManager(DataBaseConnection dataBase) {
		this.dataBase = dataBase;
		playersDTO = new HashMap<>();
		playersListID = new ArrayList<>();
		
		roomsDTO = new HashMap<>();
		roomsListID = new ArrayList<>();
	}

	public void run() {
		while(true) {
			ArrayList<RoomDTO> tempRoomDTO = dataBase.getRoomsDTO();
			
			ArrayList<RoomDTO> roomAddDTO = new ArrayList<>();
			
			HashMap<Integer,RoomDTO> tempHashMapRoomDTO = new HashMap<>();
			
			for(RoomDTO dto : tempRoomDTO) {
				tempHashMapRoomDTO.put(dto.getId(), dto);
				
				if(!roomsDTO.containsKey(dto.getId())) {
					roomAddDTO.add(dto);
					roomsDTO.put(dto.getId(), dto);
				}
			}
			
			for(RoomDTO room : roomAddDTO) {
				if(!RoomsManager.haveRoom(room.getId())) {
					RoomsManager.addRoom(room.getId());
					roomsListID.add(room.getId());
					System.out.println("MuOnline add room: "+room.getId());
				}
			}
			
			ArrayList<Integer> newRoomsList = new ArrayList<>();
			for(int id : roomsListID) {
				//last Room don't exist on actual state?
				if(!tempHashMapRoomDTO.containsKey(id)) {
					roomsDTO.remove(id);
					RoomsManager.closeRoom(id);
					System.out.println("MuOnline remove room: "+id);
				} else {
					newRoomsList.add(id);
				}
			}
			roomsListID = newRoomsList;
			
			
			
			
			ArrayList<VoiceDTO> dbDTO = dataBase.getPlayersDTO();
			
			ArrayList<VoiceDTO> connectDTO = new ArrayList<>();
			ArrayList<VoiceDTO> updateConnectionInfoDTO = new ArrayList<>();
			ArrayList<VoiceDTO> updateRoomDTO = new ArrayList<>();
			ArrayList<VoiceDTO> updateMapDTO = new ArrayList<>();
			ArrayList<VoiceDTO> updateListenTalk = new ArrayList<>();
			
			HashMap<Integer,VoiceDTO> tempHashMapDTO = new HashMap<>();
			
			for(VoiceDTO dto : dbDTO) {
				tempHashMapDTO.put(dto.getId(), dto);
				
				if(!playersDTO.containsKey(dto.getId())) {
					connectDTO.add(dto);
				} else {
					VoiceDTO lastInfo = playersDTO.get(dto.getId());
					VoiceDTO actualInfo = dto;
					if(lastInfo.getToken() != actualInfo.getToken()) {
						updateConnectionInfoDTO.add(dto);
						lastInfo.setToken(actualInfo.getToken());
					} else {
						if(actualInfo.getRoom() != lastInfo.getRoom()) {
							updateRoomDTO.add(dto);
							lastInfo.setRoom(actualInfo.getRoom());
						}
						if(actualInfo.getMap() != lastInfo.getMap()) {
							updateMapDTO.add(dto);
							lastInfo.setMap(actualInfo.getMap());
						}
						if(actualInfo.isTalkLocal()    != lastInfo.isTalkLocal() || 
							actualInfo.isTalkGroup()   != lastInfo.isTalkGroup() || 
							actualInfo.isListenLocal() != lastInfo.isListenLocal() || 
							actualInfo.isListenGroup() != lastInfo.isListenGroup()) {
							updateListenTalk.add(dto);
							lastInfo.setTalkLocal(actualInfo.isTalkLocal());
							lastInfo.setTalkGroup(actualInfo.isTalkGroup());
							lastInfo.setListenLocal(actualInfo.isListenLocal());
							lastInfo.setListenGroup(actualInfo.isListenGroup());
						}
					}
				}
			}
			
			for(VoiceDTO dto : updateListenTalk) {
				Player player = PlayersManager.getPlayer(dto.getId());
				player.setTalkInLocal(dto.isTalkLocal());
				player.setTalkInGroup(dto.isTalkGroup());
				player.setListenLocal(dto.isListenLocal());
				player.setListenGroup(dto.isListenGroup());
				System.out.println("MuOnline player "+dto.getId()+" listen/talk");
			}
			
			for(VoiceDTO dto : updateMapDTO) {
				PlayersManager.getPlayer(dto.getId()).setMap(dto.getMap());
				System.out.println("MuOnline player "+dto.getId()+" change to map: "+dto.getMap());
			}
			
			for(VoiceDTO dto : updateRoomDTO) {
				PlayersManager.getPlayer(dto.getId()).setGroupId(dto.getRoom());
				System.out.println("MuOnline player "+dto.getId()+" change to room: "+dto.getRoom());
			}
			
			//reconnect
			for(VoiceDTO dto : updateConnectionInfoDTO) {
				PlayersManager.disconnect(dto.getId());
				System.out.println("MuOnline remove to update index: "+dto.getId());
				connectDTO.add(dto);
			}
			
			for(VoiceDTO dto : connectDTO) {
				Player player = new PlayerNormal();
				player.setID(dto.getId());
				player.setScretID(dto.getToken());
				player.setPublicId("");
				player.setGroupId(dto.getRoom());
				player.initCrypto(dto.getCryptoKey().getBytes());
				player.setMap(dto.getMap());
				
				PlayersManager.addPreConnect(player);
				
				System.out.println("MuOnline add player: "+dto.getId()+" | Token: "+dto.getToken());
				
				if(!playersDTO.containsKey(dto.getId())) {
					playersDTO.put(dto.getId(), dto);
					playersListID.add(dto.getId());
				} else {
					VoiceDTO lastPlayer = playersDTO.get(dto.getId());
					VoiceDTO actualPlayer = dto;
					if(!lastPlayer.getString().equals(actualPlayer.getString())) {
						playersDTO.remove(dto.getId());
						playersDTO.put(dto.getId(), dto);
					}
				}
			}
			
			ArrayList<Integer> newPlayerList = new ArrayList<>();
			for(int id : playersListID) {
				//lastPlayer dont exist on actual state?
				if(!tempHashMapDTO.containsKey(id)) {
					playersDTO.remove(id);
					PlayersManager.disconnect(id);
					System.out.println("MuOnline remove player: "+id);
				} else {
					newPlayerList.add(id);
				}
			}
			playersListID = newPlayerList;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
