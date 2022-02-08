package com.runnerup.runit;

import com.google.protobuf.ByteString;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DecodeFileid implements Serializable {


    String filedecoded;
    byte[] filedecodedbytes;
    boolean matchedok;
    String usrpkey;
    String usraccnt;
    String usrprofilecontractid;

    public  DecodeFileid(ByteString filein, String pwordin) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        String filedatain = filein.toStringUtf8();

        SecretKeySpec secretKey;
        byte[] key;

        MessageDigest sha = null;

        key = pwordin.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        filedecoded = new String(cipher.doFinal(Base64.getDecoder().decode(filedatain)));
        filedecodedbytes = filedecoded.getBytes();

        // check account number from hederafile is same as input
        // check password is valid

        String array[] = (filedecoded).split("/");

        System.out.println("RUN hedera file # of parameters in it " + array.length);

        if ( array.length < 4 )  {
            usraccnt = "0.0.00000";
            return ;
        }

        String accnt = array[0], pkey = array[1], hashout = array[2], contractid = array[3];

        System.out.println("Hedera File ID contents.. : " + accnt + " " + pkey + " " + hashout + " " + contractid);

        usraccnt = accnt;

        usrprofilecontractid = contractid;

        matchedok = validatePassword(pwordin,hashout);

        if (matchedok)
            usrpkey = pkey;

    }


    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }


    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;

    }

}
