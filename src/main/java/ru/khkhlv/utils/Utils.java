package ru.khkhlv.utils;

import java.util.Properties;

public class Utils {

    public static Properties getAppProperties() {
        return AppProperties.getInstance().getProperties();
    }

    public static String getBinaryStr(int num, int depth) {
        String binaryStr = Integer.toBinaryString(num);
        while (binaryStr.length() < depth) {
            binaryStr = "0" + binaryStr;
        }
        binaryStr = binaryStr.substring(binaryStr.length() - depth, binaryStr.length());
        return binaryStr;
    }

//    public static String byteToBinaryString(byte b) {
//        StringBuilder binaryStringBuilder = new StringBuilder();
//        for (int i = 0; i < 8; i++)
//            binaryStringBuilder.append(((0x80 >>> i) & b) == 0 ? '0' : '1');
//        return binaryStringBuilder.toString();
//    }
}
