package com.ponxu.run.lang;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String ifEmpty(String str, String df) {
		return isNotEmpty(str) ? str : df;
	}

	public static String firstUpperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String firstLowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 != null)
			return str1.equalsIgnoreCase(str2);

		return false;
	}

	public static String md5(String str) {
		try {
			if (str == null)
				return null;

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(md5("admin"));
	}

	public static String change(String src, char c1, char c2) {
		char[] newChars = new char[src.length()];
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			if (c == c1)
				c = c2;
			else if (c == c2)
				c = c1;
			newChars[i] = c;
		}
		return new String(newChars);
	}

	/** 简易加密方法 */
	public static String createSignedValue(String secret, String value) {
		return null;
	}

	/** 简易解密方法 */
	public static String getSignedValue(String secret, String value) {
		return null;
	}
}
