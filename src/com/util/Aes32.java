package com.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;

/**
 * 
 * @author 有密匙加密
 *
 */
public class Aes32 {
	
	//Light.GREEN.getnCode();
	// 加密
	public String Encrypt(String str) {
		try {
			String key = "11223344556677889900112233445566"; // 密钥
			byte[] kb = key.getBytes("utf-8");
			SecretKeySpec sks = new SecretKeySpec(kb, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 算法/模式/补码方式
			cipher.init(Cipher.ENCRYPT_MODE, sks);
			byte[] eb = cipher.doFinal(str.getBytes("utf-8"));
			return new Base64().encodeToString(eb);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 解密
	public String Decrypt(String str) {
		try {
			String key = "11223344556677889900112233445566"; // 密钥
			byte[] kb = key.getBytes("utf-8");
			SecretKeySpec sks = new SecretKeySpec(kb, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, sks);
			byte[] by = new Base64().decode(str);
			byte[] db = cipher.doFinal(by);
			return new String(db);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		Aes32 aes = new Aes32();
		String str = "233";
		System.out.println("原数据:" + str);
		String en = aes.Encrypt(str);
		System.out.println("加密:" + en);
		String de = aes.Decrypt(en);
		System.out.println("解密:" + de);
	}

}
