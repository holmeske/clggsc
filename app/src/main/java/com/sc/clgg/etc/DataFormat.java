
package com.sc.clgg.etc;

public final class DataFormat {

	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}

	}

	public static String bytesToHex(byte[] data) {
		if (data == null) {
			return null;
		} else {
			int len = data.length;
			String str = "";
			for (int i = 0; i < len; i++) {
				if ((data[i] & 0xFF) < 16) {
					str = str + "0" + Integer.toHexString(data[i] & 0xFF);
				} else {
					str = str + Integer.toHexString(data[i] & 0xFF);
				}
			}
			return str.toUpperCase();
		}
	}

	/*
	 * 补数 输入数据为16进制的hex码，不足8字节倍数补足，如果8字节倍长，继续补
	 */
	public static String padding(String hexdata) {
		String paddataString = null;

		int len = hexdata.length();
		if (len == 0) {
			paddataString = "8000000000000000";
		} else if (len % 2 != 0) {
			hexdata = hexdata + "0";
		}
		// 先补足偶数倍
		len = len / 2;
		if (len % 8 == 0) {
			hexdata = hexdata + "8000000000000000";
		} else {
			hexdata = hexdata + "80";
			len = len + 1;
			if (len % 8 == 0) {
				paddataString = hexdata;
			} else {
				for (int i = 0; i < 8 - len % 8; i++) {
					hexdata = hexdata + "00";
				}
				paddataString = hexdata;
			}
		}
		return paddataString;
	}

	/**
	 * 两个等长的数组做异或
	 *
	 * @param source1
	 * @param source2
	 * @return
	 */
	public static byte[] diffOr(byte[] source1, byte[] source2) {
		int len = source1.length;
		byte[] dest = new byte[len];
		for (int i = 0; i < len; i++) {
			dest[i] = (byte) (source1[i] ^ source2[i]);
		}
		return dest;
	}

	public static String DataXor(String s1, String s2) {
		byte[] iArr = diffOr(hexToBytes(s1), hexToBytes(s2));
		return bytesToHex(iArr);
	}

}
