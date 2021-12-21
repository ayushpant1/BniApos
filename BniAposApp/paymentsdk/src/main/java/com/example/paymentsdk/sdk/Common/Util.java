package com.example.paymentsdk.sdk.Common;

public class Util {
    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
    public static String toHex(String ba) {
        StringBuilder str = new StringBuilder();
        for (char ch : ba.toCharArray()) {
            str.append(String.format("%02x", (int) ch));

        }
        return str.toString();
    }
}
