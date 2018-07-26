package com.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/*
 * AES对称加密和解密    没有密匙
 */
public class JDK8Encoder {
	public static String getEncode(String count) {
		return Base64.getEncoder().encodeToString(count.getBytes(StandardCharsets.UTF_8));
	}

	public static String getDecoder(String count) {
		return new String(Base64.getDecoder().decode(count), StandardCharsets.UTF_8);
	}

	public static void main(String[] args) {
		String str = getEncode("1234567" + "ppdai");
		System.out.println("原数字：" + "1234567" + "ppdai");
		System.out.println("加密之后：" + str);
		String str2 = getDecoder(str);
		System.out.println("解密：" + str2);
	}

}