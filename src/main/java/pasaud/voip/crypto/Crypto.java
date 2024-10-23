package pasaud.voip.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	
	private SecretKey secretKey;
	
	private final static int GCM_IV_LENGTH = 12;
	private final static int GCM_TAG_LENGTH = 16;
	
	private byte[] ivEnc;
	private byte[] ivDec;
	
	private static byte[] _iv = new byte[GCM_IV_LENGTH];
	public static void setIV(byte[] iv) {
		_iv = iv;
	}
	public static byte[] getIV() {
		return _iv;
	}
	
	public Crypto(byte[] key) {

		secretKey = new SecretKeySpec(key, "AES");
		
		ivEnc = new byte[GCM_IV_LENGTH];
		ivDec = new byte[GCM_IV_LENGTH];
		
		System.arraycopy(Crypto.getIV(), 0, ivEnc, 0, GCM_IV_LENGTH);
		System.arraycopy(Crypto.getIV(), 0, ivDec, 0, GCM_IV_LENGTH);
		
	}
	
	public byte[] encrypt(byte[] data) {
		byte[] cipherText = new byte[1];
		cipherText[0] = 0;
		try {
			Cipher cipherEnc = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec parameterSpecEnc = new GCMParameterSpec(128, ivEnc); //128 bit auth tag length
			cipherEnc.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpecEnc);
			
			cipherText = cipherEnc.doFinal(data);
	        return cipherText;
		} catch (IllegalBlockSizeException | BadPaddingException | 
				NoSuchAlgorithmException | NoSuchPaddingException | 
				InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return cipherText;
	}
	
	public byte[] decrypt(byte[] data) {
		byte[] plainText = new byte[0];
		try {
			Cipher cipherDec = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec parameterSpecDec = new GCMParameterSpec(128, ivDec); //128 bit auth tag length
			cipherDec.init(Cipher.DECRYPT_MODE, secretKey, parameterSpecDec);
			
			plainText = cipherDec.doFinal(data, 0, data.length);
			return plainText;
		} catch (IllegalBlockSizeException | BadPaddingException | 
				NoSuchAlgorithmException | NoSuchPaddingException | 
				InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Can't decrypt");
		}
        return plainText;
	}

}
