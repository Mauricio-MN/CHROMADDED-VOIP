package pasaud.voip.muonline.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pasaud.voip.muonline.database.RoomDTO;
import pasaud.voip.muonline.database.VoiceDTO;

public class DataBaseConnection {
	
	private Connection conn;
	
	private String host;
	private String dataBase;
	private String user;
	private String password;
	private Integer serverCode;
	
	private void connect() {
		String url = "jdbc:sqlserver://"+host+"\\MSSQLSERVER:1433;databaseName="+dataBase;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			
			 System.out.println("Try SQL Connect.");
		
			if (conn != null) {
	            System.out.println("The connection has been successfully established.");
	            
	            DatabaseMetaData dm = conn.getMetaData();
	            System.out.println("Driver name: " + dm.getDriverName());
	            System.out.println("Driver version: " + dm.getDriverVersion());
	            System.out.println("Product name: " + dm.getDatabaseProductName());
	            System.out.println("Product version: " + dm.getDatabaseProductVersion());
	        } else {
	        	System.out.println("Failed MSSQL connect...");
	        }
			
		} catch (SQLException e) {
			System.out.println("Exception: Problem on MSSQL connect...");
		}
	}
	
	public DataBaseConnection(String host, String dataBase, String user, String password, Integer serverCode) {
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			this.host = host;
			this.dataBase = dataBase;
			this.user = user;
			this.password = password;
			connect();
			this.serverCode = serverCode;
		} catch (SQLException e) {
			System.out.println("Exception: Problem on MSSQL DRIVER...");
		}
	}
	
	private void tryResolveConnection() {
		System.out.println("Reconnect...");
		try {
			if(conn == null) {
				connect();
			} else {
				if(!conn.isValid(conn.getNetworkTimeout()) || !conn.isClosed()) {
					System.out.println("Try close MSSQL connection.");
					conn.abort(Runnable::run);
					if(!conn.isClosed()) {
						conn.close();
					}
					connect();
				}
			}
		} catch (SQLException e1) {
			System.out.println("Exception: MSSQL instance, failed check/connect...");
			System.out.println(e1.getMessage());
			conn = null;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
				System.out.println("Exception: MSSQL instance, failed wait...");
			}
		}
	}
	
	//Not fast
	public List<Integer> getValidPublicIDs(){
		ArrayList<Integer> ids = new ArrayList<>();
		
		Statement st;
		try {
			st = conn.createStatement();
			String Sql = "select * from VoiceToken where Online > 0 and serverCode="+serverCode+" "; 
			ResultSet rs = st.executeQuery(Sql);
			
			while (rs.next()) {
				ids.add(rs.getInt("id"));
	        }
			
		} catch (SQLException e) {
			System.out.println("Problem on MSSQL get valid public IDs...");
			tryResolveConnection();
		}
		
		return ids;
		
	}
	
	public ArrayList<RoomDTO> getRoomsDTO() {
		ArrayList<RoomDTO> dto = new ArrayList<>();
		
		Statement st;
		try {
			if(conn == null) {
				throw new SQLException();
			}
			st = conn.createStatement();
			String Sql = "select * from VoiceRooms where serverCode="+serverCode+" and isActive=1 "; 
			ResultSet rs = st.executeQuery(Sql);
			
			while (rs.next()) {
				RoomDTO tempDTO = new RoomDTO();
				tempDTO.setId(rs.getInt("id"));
				tempDTO.setType(rs.getInt("type"));
				dto.add(tempDTO);
	        }
			
		} catch (SQLException e) {
			System.out.println("MSSQL problem, failed to get all rooms...");
			tryResolveConnection();
		}
		
		return dto;
	}
	
	public ArrayList<VoiceDTO> getPlayersDTO() {
		ArrayList<VoiceDTO> dto = new ArrayList<>();
		
		Statement st;
		try {
			if(conn == null) {
				throw new SQLException();
			}
			st = conn.createStatement();
			String Sql = "select * from VoiceToken where Online > 0 and serverCode="+serverCode+" "; 
			ResultSet rs = st.executeQuery(Sql);
			
			while (rs.next()) {
				VoiceDTO tempDTO = new VoiceDTO();
				tempDTO.setId(rs.getInt("id"));
				tempDTO.setToken(rs.getInt("Token"));
				tempDTO.setRoom(rs.getInt("room"));
				tempDTO.setCryptoKey(rs.getString("cryptokey"));
				tempDTO.setMap(rs.getInt("map"));
				if(rs.getInt("talkLocal") == 1) {
					tempDTO.setTalkLocal(true);
				} else {
					tempDTO.setTalkLocal(false);
				}
				if(rs.getInt("talkGroup") == 1) {
					tempDTO.setTalkGroup(true);
				} else {
					tempDTO.setTalkGroup(false);
				}
				if(rs.getInt("listenLocal") == 1) {
					tempDTO.setListenLocal(true);
				} else {
					tempDTO.setListenLocal(false);
				}
				if(rs.getInt("listenGroup") == 1) {
					tempDTO.setListenGroup(true);
				} else {
					tempDTO.setListenGroup(false);
				}
				dto.add(tempDTO);
	        }
			
		} catch (SQLException e) {
			System.out.println("MSSQL problem, failed to get all voice tokens...");
			tryResolveConnection();
		}
		
		return dto;
	}

}
