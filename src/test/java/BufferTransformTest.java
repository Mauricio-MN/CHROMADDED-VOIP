
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import pasaud.voip.protocol.udp.BufferConvertType;
import pasaud.voip.protocol.udp.BufferTransform;

public class BufferTransformTest {

	@Test
	public void bufferTransformLong(){
		byte[] buffer = {(byte)0xD4,(byte)0xAB,(byte)0xCD, (byte)0x43};
		Long longtest = 0L;
		longtest = new BufferTransform<Long>().parse(buffer, BufferConvertType.LONG, longtest);
		Assert.assertEquals((Long)(-726938301L), longtest);
	}
	
	@Test
	public void bufferTransformInteger(){
		byte[] buffer = {(byte)0xD4,(byte)0xAB,(byte)0xCD, (byte)0x43};
		Integer inttest = 0;
		inttest = new BufferTransform<Integer>().parse(buffer, BufferConvertType.INTEGER, inttest);
		Assert.assertEquals((Integer)(-726938301), inttest);
	}
	
	@Test
	public void bufferTransformString(){
		String s = "passed";
		byte[] buffer = s.getBytes(StandardCharsets.US_ASCII);
		String strtest = "";
		strtest = new BufferTransform<String>().parse(buffer, BufferConvertType.STRING, strtest);
		Assert.assertEquals(s, strtest);
	}
	
}