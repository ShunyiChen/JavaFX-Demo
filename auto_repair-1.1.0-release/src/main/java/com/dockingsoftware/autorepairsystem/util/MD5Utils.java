/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.LogManager;

/**
 * 字符串MD5加密 - 单向
 * 
 * @author Chen
 */
public class MD5Utils {

    public static String md5(String input) {
        String md5 = null;
        if (null == input) {
            return null;
        }
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            org.apache.logging.log4j.Logger logger = LogManager.getLogger(MD5Utils.class.getName());
            logger.error(e);
        }
        return md5;
    }
    
    public static void main(String[] args) {
 
            String password = "MyPassword123";
 
            System.out.println("MD5 in hex: " + md5(password));
 
             
            System.out.println("MD5 in hex: " + md5(null));
            //= d41d8cd98f00b204e9800998ecf8427e
             
             
            System.out.println("MD5 in hex: "
                + md5("The quick brown fox jumps over the lazy dog"));
            
 
            String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
            String hash = md5(password + salt);
            System.out.println(hash);
            //= 9e107d9d372bb6826bd81d3542a419d6
    }
}
