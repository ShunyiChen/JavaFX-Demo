/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * 字符串加密解密 - 双向
 * 
 * @author Chen
 */
public class Encryptor {

    public static Encryptor getInstance() {
        if (single == null) {
            single = new Encryptor();
        }
        return single;
    }

    private void init() throws Exception {
        if (keyStr == null || keyStr.length() != 16) {
            throw new Exception("bad aes key configured");
        }
        if (aesKey == null) {
            aesKey = new SecretKeySpec(keyStr.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        }
    }

    public String encrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return toHexString(cipher.doFinal(text.getBytes()));
    }

    public String decrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(toByteArray(text)));
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static void main(String[] args) throws Exception {
        // 生成新序列号
        String txt = "20160901";
        String str1 = getInstance().encrypt(txt);
        System.out.println("str1=" + str1);

        String str2 = getInstance().decrypt(str1);
        System.out.println("str2=" + str2);
    }

    private static Encryptor single = null;
    private String keyStr = "1234567890123456";
    private Key aesKey = null;
    private Cipher cipher = null;

    /*
     * DO NOT DELETE
     * 
     * Use this commented code if you don't like using DatatypeConverter dependency
     */
    // public static String toHexStringOld(byte[] bytes) {
    // StringBuilder sb = new StringBuilder();
    // for (byte b : bytes) {
    // sb.append(String.format("%02X", b));
    // }
    // return sb.toString();
    // }
    //
    // public static byte[] toByteArrayOld(String s) {
    // int len = s.length();
    // byte[] data = new byte[len / 2];
    // for (int i = 0; i < len; i += 2) {
    // data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i +
    // 1), 16));
    // }
    // return data;
    // }
}
