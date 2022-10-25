
package pasaud.voip;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

import pasaud.voip.genericsocket.GenericSocketType;
import pasaud.voip.genericsocket.PlayersSocket;
import pasaud.voip.maps.Map;
import pasaud.voip.maps.MapsManager;
import pasaud.voip.player.Player;
import pasaud.voip.player.PlayersManager;
import pasaud.voip.player.hash.PlayerHashInfo;
import pasaud.voip.player.hash.PlayerPreConnectHashInfo;
import pasaud.voip.protocol.udp.ProtocolFromClient;
import pasaud.voip.protocol.udp.ProtocolToClient;

public class Main {

    public static void main(String args[]) {

        try {
        	ProtocolFromClient.init();
        	ProtocolToClient.init();
        	PlayersSocket.init(GenericSocketType.DATAGRAM);
        	
            System.out.println("Chromadded Valuable On Inside Projects, ;D");
            System.out.println("Chromadded VOIP?");
            System.out.println("Initializing Maps Manager");
            
            MapsManager.init(new Map[3]);
            
            System.out.println("Initalized! ");
            System.out.println("---");
            
            System.out.println("Initializing Players Manager");
            PlayersManager.init();

            byte[] bid = new byte[]{0,1,2,3};

            int id = new BigInteger(bid).intValue();
            
            InetAddress fakeAddress = InetAddress.getByName("127.0.0.1");
            PlayerPreConnectHashInfo testPlayerhash = new PlayerPreConnectHashInfo(0101, 1234);

            PlayersManager.addPreConnect(testPlayerhash, new byte[]{0,2,4,7,2,7,4,9});
            
            System.out.println("Initalized! ");
            System.out.println("---");
            
            System.out.println("Initializing UDP server");
            
            UDPServer.init(443);
            
            System.out.println("Initalized! ");
            System.out.println("---");

			try {
				Scanner input = new Scanner(System.in);
				while (true) {
					// primitive wuga buga block SORRY, just dump for initial tests
					Thread.sleep(100);
					System.out.println("Receiving UDP packets");
					System.out.println("");
					String text = input.next();
					String[] splText = text.split("\\s+");

					if (splText[0].equals("add")) {
						if (splText[1].equals("player")) {
							if(splText.length > 5) {
								System.out.println("Command not found...");
								System.out.println("Need be : " + splText[0] + 
										" player [register_id Integer] [public_id Integer] [cryptoKey 128 bits(16 bytes/characters) String]");
							} else {
								PlayerHashInfo hash = 
										new PlayerPreConnectHashInfo(Integer.parseInt(splText[2]), Integer.parseInt(splText[3]));
								byte[] cryptkey = splText[4].getBytes("ASCII");
								cryptkey = Arrays.copyOfRange(cryptkey, 0, cryptkey.length - 2);
								if(cryptkey.length != 16) {
									System.out.println("cryptoKey need 16 characters");
								} else {
									PlayersManager.addPreConnect(hash, cryptkey);
								}
							}
						}
					} else if (splText[0].equals("remove")){
						if (splText[1].equals("player")) {
							if(splText.length > 3) {
								System.out.println("Command not found...");
								System.out.println("Need be : " + splText[0] + " player [name/public_id]");
							} else {
								if(isNumeric(splText[2])) {
									Player player = PlayersManager.getPlayerByID(Integer.parseInt(splText[2]));
									if(player != null) {
										PlayerHashInfo hashConnected = player.getHashCode();
										PlayersManager.disconnect(hashConnected);
									}
								} else {
									Player player = PlayersManager.getPlayerByName(splText[2]);
									if(player != null) {
										PlayerHashInfo hashConnected = player.getHashCode();
										PlayersManager.disconnect(hashConnected);
									}
								}
							}
						}
					} else if (!text.equals("")) {
						System.out.println("Command not found...");
					}

				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

        } catch (IOException ex) {
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
