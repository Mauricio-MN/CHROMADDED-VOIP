
package pasaud.voip;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import pasaud.voip.crypto.Crypto;
import pasaud.voip.maps.MapsManager;
import pasaud.voip.maps.ServerMap;
import pasaud.voip.muonline.controller.DataBaseConnection;
import pasaud.voip.muonline.controller.UsersManager;
import pasaud.voip.player.Player;
import pasaud.voip.player.PlayerNormal;
import pasaud.voip.player.PlayersManager;
import pasaud.voip.protocol.udp.ProtocolFromClient;
import pasaud.voip.protocol.udp.ProtocolToClient;
import pasaud.voip.rooms.RoomsManager;
import pasaud.voip.types.Coord;

public class Main {
	
	public static boolean debug = false;
	
	private static Scanner input;
	
	private static Runnable muOnlinePlayerManager;
	private static Thread muOnlinePlayerManagerThread;

    public static void main(String args[]) {

        try {
        	
        	String configFilePath = "config.txt";
        	Integer port = 0;
        	boolean encrypt = false;
        	String encrypt_iv = "1234567890123456";
        	boolean accept_coords_from_player = false;
        	Integer base_threads = 4;
        	boolean muonline = false;
        	String mssql_host = "";
			String mssql_database = "";
			String mssql_user = "";
			String mssql_password = "";
			Integer mssql_serverCode = 0;
			
            try {
            	ConfigReader config = new ConfigReader(configFilePath);

                port = config.getInt("port");
            	encrypt = config.getBoolean("encrypt");
            	encrypt_iv = config.getString("encrypt_iv");
            	accept_coords_from_player = config.getBoolean("accept_coords_from_player");
            	base_threads = config.getInt("base_threads");
                muonline = config.getBoolean("muonline");
            	mssql_host = config.getString("mssql_host");
    			mssql_database = config.getString("mssql_database");
    			mssql_user = config.getString("mssql_user");
    			mssql_password = config.getString("mssql_password");
    			mssql_serverCode = config.getInt("mssql_serverCode");
            } catch (IOException e) {
                System.err.println("Error on read config file: " + e.getMessage());
            }
            
        	ProtocolFromClient.init();
        	ProtocolToClient.init();
        	
        	input = new Scanner(System.in);
        	
            //System.out.println("Chromadded Valuable On Inside Projects, ;D");
            //System.out.println("Chromadded VOIP?");
        	System.out.println("CRMD VOIP");
            System.out.println("Initializing Maps Manager");
            
            ServerMap[] maps = new ServerMap[100];
            for (int i = 0; i < maps.length; i++) {
                maps[i] = new ServerMap(16, 16, 1, 10);
            }
            
            MapsManager.init(maps);
            
            System.out.println("Initalized! ");
            System.out.println("---");
            
            System.out.println("Initializing Rooms Manager");
            RoomsManager.init();
            System.out.println("Initalized! ");
            System.out.println("---");
            
            if(encrypt) {
	            System.out.println("Set IV");
	            if(encrypt_iv.length() < 12) {
	            	System.out.println("Incorrect IV size, need be 12.");
	            	encrypt_iv = "123456789012";
	            }
	            Crypto.setIV(encrypt_iv.getBytes());
	            System.out.println("IV Set: " + encrypt_iv + ".");
	            System.out.println("---");
            }
            
            System.out.println("Initializing Players Manager");
            PlayersManager.init();
            System.out.println("Initalized! ");
            System.out.println("---");
            
            if(muonline) {
            	System.out.println("Initializing MuOnline DataBase module");
            	
            	DataBaseConnection dataConn = 
						new DataBaseConnection(mssql_host, mssql_database, mssql_user, mssql_password, mssql_serverCode);
				muOnlinePlayerManager = new UsersManager(dataConn);
				muOnlinePlayerManagerThread = new Thread(muOnlinePlayerManager);
				muOnlinePlayerManagerThread.start();
				
				System.out.println("Initalized! ");
	            System.out.println("---");
            }
            
            System.out.println("Initializing UDP server");

            UDPServer.init(port, base_threads, encrypt, accept_coords_from_player);
            UDPServer.setNeedCryptograph(encrypt);
            System.out.println("Initalized on port "+ port + ".");
           
            System.out.println("---");
            
            System.out.println("Receiving UDP packets");
			System.out.println("");

				while (true) {
					// primitive wuga buga block SORRY, just dump for initial tests
					Thread.sleep(100);
					String text = input.nextLine();
					String[] splText = text.split("\\s+");
					if(splText.length > 0) {
						
						if(splText[0].equals("database")) {
							if(splText.length >= 8) {
								if(splText[1].equals("init")) {
									if(splText[2].equals("muonline")){
										String host = splText[3];
										String database = splText[4];
										String user = splText[5];
										String password = splText[6];
										Integer serverCode = Integer.valueOf(splText[7]);
										DataBaseConnection dataConn = 
												new DataBaseConnection(host, database, user, password, serverCode);
										muOnlinePlayerManager = new UsersManager(dataConn);
										muOnlinePlayerManagerThread = new Thread(muOnlinePlayerManager);
										muOnlinePlayerManagerThread.start();
										
									}
								}
							}
						} else if (splText[0].equals("add")) {
							if(splText.length < 5) {
								System.out.println("Command not found...");
								System.out.println("Need be : add " + 
										" player [secret_id Integer] [public_id Integer] [cryptoKey 256 bits(32 bytes/characters) String] optional[name String]");
							} else {
								
								if (splText[1].equals("player")) {
								
									byte[] cryptkey = splText[4].getBytes("ASCII");
									cryptkey = Arrays.copyOfRange(cryptkey, 0, cryptkey.length);
									if(cryptkey.length != 32) {
										System.out.println("cryptoKey need 32 characters");
									} else {
										Player player = new PlayerNormal();
										player.setID(Integer.parseInt(splText[3]));
										player.setScretID(Integer.parseInt(splText[2]));
										
										player.initCrypto(cryptkey);
										
										if(splText.length > 5) {
											player.setPublicId(splText[5]);
										}
										PlayersManager.addPreConnect(player);
										System.out.println("success, added player: " + splText[3]);
									}
								}
							}
							
						} else if (splText[0].equals("remove")){
							if(splText.length < 3) {
								System.out.println("Need be : remove player [name String or id Integer] ");
							} else if (splText[1].equals("player")) {
								if(splText.length > 3) {
									System.out.println("Command not found...");
									System.out.println("Need be : " + splText[0] + " player [name/public_id]");
								} else {
									if(isNumeric(splText[2])) {
										PlayersManager.disconnect(Integer.parseInt(splText[2]));
										System.out.println("success, removed player: " + splText[2]);
									} else {
										Player player = PlayersManager.getPlayerByName(splText[2]);
										if(player != null) {
											PlayersManager.disconnect(player.getID());
											System.out.println("success, removed player: " + splText[2]);
										}
									}
								}
							}
						} else if(splText[0].equals("set")) {
							boolean invalidOption = false;
							
							if(splText.length == 3) {
								if(splText[1].equals("iv")) {
									if(splText[2].length() >= 12) {
										Crypto.setIV(splText[2].getBytes());
										System.out.println("success, set iv to: " + splText[2]);
									} else {
										invalidOption = true;
									}
								} else {
									invalidOption = true;
								}
							} else if(splText.length == 5) {
								if(splText[1].equals("player")) {
									if(splText[2].equals("map")) {
										
										try {
											int playerId = Integer.parseInt(splText[3]);
											int map = Integer.parseInt(splText[4]);
											
											if(PlayersManager.containsPlayerConnected(playerId)) {
												Player player = PlayersManager.getPlayer(playerId);
												player.setMap(map);
												System.out.println("success, set player " + playerId + " map to " + map);
											} else {
												System.out.println("Invalid player");
											}
										} catch (NumberFormatException ex) {
											System.out.println("Invalid integer number");
										}
										
									} else {
										invalidOption = true;
									}
								} else {
									invalidOption = true;
								}
							} else {
								invalidOption = true;
							}
							
							if(invalidOption) {
								System.out.println("Command not found...");
								System.out.println("Need be :");
								System.out.println(splText[0] + " iv <12 Bytes IV string>");
								System.out.println(splText[0] + " player map <player Id> <map id>");
							}
						}
						else if(splText[0].equals("show")) {
							//show player id
							if(splText.length == 3) {
								if(splText[1].equals("player")) {
									Integer playerId = Integer.parseInt(splText[2]);
									if(PlayersManager.containsPlayerConnected(playerId)) {
										Player player = PlayersManager.getPlayer(playerId);
										System.out.println("Player: "+playerId);
										System.out.println("Map: " + player.getMap());
										Coord coords = player.getCoord();
										System.out.println("Coords: " + coords.getX() + ";" + coords.getY() + ";" + coords.getZ());
										System.out.println("GroupId: " + player.getGroupId());
									}
								} else {
									System.out.println("show player <player id>");
								}
							} else {
								System.out.println("show player <player id>");
							}
						}
						else if(splText[0].equals("exit")) {
							UDPServer.close();
							System.exit(0);
						} else if (!text.equals("")) {
							System.out.println("Command not found...");
							System.out.println("add");
							System.out.println("remove");
							System.out.println("set");
							System.out.println("show");
							System.out.println("...");
						}
						
					}

				}

        } catch (Exception  ex) {
            System.out.println(ex.toString());
        }


    }
    
    public static boolean isNumeric(String str) {
    	return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    
    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
              "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }
    
    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            return null;
        }
        
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

}
