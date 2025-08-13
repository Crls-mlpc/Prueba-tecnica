package com.pruetec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {
	private static final String AES_KEY;
	private static final int GCM_TAG_LENGTH = 128;
	
	static {
		String envKey = System.getenv("AES_KEY_BASE64");
		if (envKey != null && !envKey.isBlank()) {
			AES_KEY = envKey;
		}
		
		else {
			AES_KEY = generateRandomKey();
			System.out.println("AES_KEY_BASE64 GENERADA: " + AES_KEY);
			
			
		}
	}
	
	public static String encrypt(String plainText) {
		try {
			byte[] keyBytes = Base64.getDecoder().decode(AES_KEY);
			SecretKey key = new SecretKeySpec(keyBytes, "AES");
			
			byte[] iv = new byte[12];
			new SecureRandom().nextBytes(iv);
			
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            
            return Base64.getEncoder().encodeToString(combined);
		}
		catch (Exception e) {
			throw new RuntimeException("Encryption error", e);
					
		}
	}
	
	public static String decrypt (String cipherText) {
		
		try {
			
			byte[] data = Base64.getDecoder().decode(cipherText);
            byte[] iv = new byte[12];
            byte[] enc = new byte[data.length - 12];
            System.arraycopy(data, 0, iv, 0, 12);
            System.arraycopy(data, 12, enc, 0, enc.length);
            
            byte[] keyBytes = Base64.getDecoder().decode(AES_KEY);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] decrypted = cipher.doFinal(enc);
            
            return new String(decrypted);
		}
		catch (Exception e) {
			throw new RuntimeException("Error decrypting", e);
		
			
		}
	}
	
	 private static String generateRandomKey() {
	        try {
	            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	            keyGen.init(256);
	            SecretKey key = keyGen.generateKey();
	            return Base64.getEncoder().encodeToString(key.getEncoded());
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

}
