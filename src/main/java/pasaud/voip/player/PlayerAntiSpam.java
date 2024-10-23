package pasaud.voip.player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import pasaud.voip.types.CAddress;

public class PlayerAntiSpam {
	private static PlayerAntiSpam playerAntiSpam = new PlayerAntiSpam();

	public static PlayerAntiSpam get() {
		return playerAntiSpam;
	}
	
	public class SpamInfo{
		
		private AtomicInteger connectTrys;
		
		AtomicLong lastPacketTime;
		AtomicLong medianPacketTime;
		
		public SpamInfo(){
			connectTrys = new AtomicInteger();
			connectTrys.set(0);
			
			lastPacketTime = new AtomicLong();
			medianPacketTime = new AtomicLong();
			
			lastPacketTime.set(System.currentTimeMillis());
			medianPacketTime.set(100);
		}
		
		public void attLastPacketTime() {
			long nowTime = System.currentTimeMillis();
			long totalTime = nowTime - lastPacketTime.get();
			medianPacketTime.set((medianPacketTime.get() + totalTime) / 2);
			lastPacketTime.set(nowTime);
		}
		
		public void addConnectTrys() {
			connectTrys.addAndGet(1);
		}
		
		public void setConnectTrys(int value) {
			connectTrys.set(value);
		}
		
		public int getConnectTrys(){
			return connectTrys.get();
		}
		
		public long getPacktTimeMedian() {
			return medianPacketTime.get();
		}
	}
	
	private ConcurrentHashMap<String, SpamInfo> infoByAddress;
	private ConcurrentHashMap<String, SpamInfo> infoByAddress_BLOCKED;
	
	private int maxFailConnection;
	public int getMaxFailConnection() {
		return maxFailConnection;
	}

	public void setMaxFailConnection(int maxFailConnection) {
		this.maxFailConnection = maxFailConnection;
	}

	public long getMinPacketTime() {
		return minPacketTime;
	}

	public void setMinPacketTime(long minPacketTime) {
		this.minPacketTime = minPacketTime;
	}

	private long minPacketTime;
	
	public PlayerAntiSpam() {
		maxFailConnection = 10;
		minPacketTime = 8;
		infoByAddress = new ConcurrentHashMap<String, SpamInfo>();
		infoByAddress_BLOCKED = new ConcurrentHashMap<String, SpamInfo>();
	}
	
	public void sucessLegiblePacket(CAddress caddress) {
		if(addressExist(caddress)) {
			SpamInfo spamInfo = infoByAddress.get(caddress.toString());
			spamInfo.setConnectTrys(0);
		}
		if(isBlocked(caddress)) {
			deBLOCK(caddress);
		}
	}
	
	public boolean addressExist(CAddress caddress) {
		if (infoByAddress.containsKey(caddress.toString())) {
			return true;
		}
		return false;
	}
	
	private SpamInfo getAddressInfo(CAddress caddress) {
    	SpamInfo spaminfo = infoByAddress.get(caddress.toString());
        return spaminfo;
    }
	
	public int getConnectionFailed(CAddress caddress) {
		if (addressExist(caddress)) {
			return getAddressInfo(caddress).getConnectTrys();
        }
		return 0;
	}
	
	private void checkPut(CAddress caddress) {
		if (!addressExist(caddress)) {
			infoByAddress.put(caddress.toString(), new SpamInfo());
		}
	}
	
	public boolean isBlocked(CAddress caddress) {
		if(infoByAddress_BLOCKED.containsKey(caddress.toString())) {
			return true;
		}
		return false;
	}
	
	private void checkPut_BLOCKED(CAddress caddress) {
		if (!isBlocked(caddress)) {
			infoByAddress_BLOCKED.put(caddress.toString(), getAddressInfo(caddress));
		}
	}
	
	private void deBLOCK(CAddress caddress) {
		if (isBlocked(caddress)) {
			infoByAddress_BLOCKED.remove(caddress.toString());
		}
	}
	
	public void addFailedConnect(CAddress caddress) {
		checkPut(caddress);
		getAddressInfo(caddress).addConnectTrys();
		if(getAddressInfo(caddress).getConnectTrys() > maxFailConnection) {
			checkPut_BLOCKED(caddress);
		}
	}
	
	public long getPacketTimeMedian(CAddress caddress) {
		if (addressExist(caddress)) {
			return getAddressInfo(caddress).getPacktTimeMedian();
        }
		return 100;
	}
	
	public void attPacketTime(CAddress caddress) {
		checkPut(caddress);
		getAddressInfo(caddress).attLastPacketTime();
		if(getAddressInfo(caddress).getPacktTimeMedian() < minPacketTime) {
			checkPut_BLOCKED(caddress);
		} else if(getAddressInfo(caddress).getConnectTrys() > maxFailConnection) {
			checkPut_BLOCKED(caddress);
		} else {
			deBLOCK(caddress);
		}
	}
}
