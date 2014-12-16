package tony.train.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Base32 {

	private static int delta = 0x9E3779B8;

	static String fromCharCode(int... args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append((char) args[i]);
		}
		return sb.toString();
	}

	static String join(String[] args, String delimeter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length - 1; i++) {
			sb.append(args[i]).append(delimeter);
		}
		sb.append(args[args.length - 1]);
		return sb.toString();
	}

	public static String longArrayToString(Integer[] data, boolean includeLength) {
		int length = data.length;
		int n = (length - 1) << 2;
		if (includeLength) {
			int m = data[length - 1];
			if ((m < n - 3) || (m > n))
				return null;
			n = m;
		}
		String[] arr = new String[length];
		for (int i = 0; i < length; i++) {
			arr[i] = fromCharCode(data[i] & 0xff, data[i] >>> 8 & 0xff, data[i] >>> 16 & 0xff, data[i] >>> 24 & 0xff);
		}
		if (includeLength) {
			return join(arr, "").substring(0, n);
		} else {
			return join(arr, "");
		}
	}

	public static Integer[] stringToLongArray(String string, boolean includeLength) {
		int length = string.length();
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < length; i += 4) {
			result.add(string.charAt(i) | string.charAt(i + 1) << 8 | string.charAt(i + 2) << 16
					| string.charAt(i + 3) << 24);
		}
		if (includeLength) {
			result.add(length);
		}
		return result.toArray(new Integer[0]);
	};

	private static String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	public static String encode32(String input) {
		input = escape(input);
		String output = "";
		int chr1, chr2, chr3;
		int enc1, enc2, enc3, enc4;
		int i = 0;
		do {
			chr1 = input.charAt(i++);
			if (i >= input.length() - 1) {
				chr2 = 0;
				chr3 = 0;
			} else {
				chr2 = input.charAt(i++);
				if (i >= input.length() - 1) {
					chr3 = 0;
				} else {
					chr3 = input.charAt(i++);
				}
			}
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
			if (chr2 == 0) {
				enc3 = enc4 = 64;
			} else if (chr3 == 0) {
				enc4 = 64;
			}
			output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
			chr1 = chr2 = chr3 = 0;
			enc1 = enc2 = enc3 = enc4 = 0;
		} while (i < input.length());
		return output;
	};

	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String bin216(String s) {
		String o = "";
		String n;
		int i, l;
		char b;
		for (i = 0, l = s.length(); i < l; i++) {
			b = s.charAt(i);
			n = Integer.toHexString(b);
			o += n.length() < 2 ? "0" + n : n;
		}
		return o;
	};

	public static String encrypt(String string, String key) {
		if (string == "") {
			return "";
		}
		Integer[] v = stringToLongArray(string, true);
		Integer[] k = stringToLongArray(key, false);
		if (k.length < 4) {
			Integer[] k1 = new Integer[4];
			System.arraycopy(k, 0, k1, 0, k.length);
			// avoid NPE
			for (int temp = k.length; temp < k1.length; temp++) {
				k1[temp] = new Integer(0);
			}
			k = k1;
		}
		int n = v.length - 1;
		int z = v[n], y = v[0];
		int mx, e, p;
		double q = Math.floor(6 + 52 / (n + 1));
		int sum = 0;
		while (0 < q--) {
			sum = sum + delta & 0xffffffff;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
				z = v[p] = v[p] + mx & 0xffffffff;
			}
			y = v[0];
			mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			z = v[n] = v[n] + mx & 0xffffffff;
		}
		System.out.println(Arrays.toString(v));
		return longArrayToString(v, false);
	};
	
	public static String calculateParam(String value, String key) {
		String encrypt = encrypt(value, key);
		return encode32(bin216(encrypt));
	}

	public static void main(String[] args) {
		String key = "ODc5NTU0";
		String value = "1111";
		//NjI2OTkz : YzExNGM2OTM5MjQ0NzMzYQ==
		// ODc5NTU0 : NzY3MDg3MzdhY2Y4N2Y4Yw==

		String encrypt = encrypt(value, key);
		System.out.println(encrypt);
		// OGVmNjAzOTBkYzRmMmQ3MQ==
		System.out.println(encode32(bin216(encrypt)));
	}
}
