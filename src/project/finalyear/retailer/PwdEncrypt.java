package project.finalyear.retailer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PwdEncrypt {
    public static String encrypt(String plaintext) {
    	/*
        MessageDigest msgDigest = null;
        String hashValue = null;
        try {
            msgDigest = MessageDigest.getInstance("SHA-1");
            msgDigest.update(plaintext.getBytes("UTF-8"));
            byte rawByte[] = msgDigest.digest();
            hashValue = new String(rawByte);
 
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm Exists");
        } catch (UnsupportedEncodingException e) {
            System.out.println("The Encoding Is Not Supported");
        }
        return hashValue;
        */
    	return plaintext+plaintext.charAt(0);
    }
}
