package com.wusy.wusylibrary.util;

/**
 * 这是一个数据转化工具类
 */
public class DataUtil {
    /**
     * 判断传入Object是否为空
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj){
        if(obj==null||obj.equals("")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 字节数组转转hex字符串，可选长度
     * @param b
     * @param length
     * @return
     */
    public static String byteToHex(byte[] b, int length) {
        String keyVal = "";
        for (int i = 0; i < length; i++) {
            String temp = Integer.toHexString(b[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            keyVal = keyVal + temp;
        }
        return keyVal.toUpperCase();
    }
    /**
     * 将16进制字符串转化为byte数组
     * @param src
     * @return
     */
    public static byte[] HexString2Bytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < len; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }
    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     *  Hex字符串转int
     */
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }
    /**
     *  int字符串转Hex
     */
    public static String IntToHex(int intHex){
        return Integer.toHexString(intHex);
    }
}
