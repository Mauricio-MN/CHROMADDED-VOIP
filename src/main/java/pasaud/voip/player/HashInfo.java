
package pasaud.voip.player;

import java.net.InetAddress;

public class HashInfo {

    private final String AddressPort;
    private final Long token;

    public HashInfo(InetAddress address, int port, long token) {
        this.AddressPort = address.getHostName() + port;
        this.token = token;
    }

    public String getHash() {
        String result = AddressPort + token;
        return result;
    }

}
