package it.unisa.diem.aps.aps_maninthemiddle;


import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import javax.crypto.spec.SecretKeySpec;

/**
 * 
 */
public class ElGamal
{
    private Cipher cipher;
    private  KeyPairGenerator generator;
    
   public ElGamal() throws NoSuchAlgorithmException, NoSuchPaddingException{
    SecureRandom  random = new SecureRandom();  
    generator = KeyPairGenerator.getInstance("ELGamal");     
    generator.initialize(2048, random);
    cipher = Cipher.getInstance("ElGamal/None/PKCS1Padding");
    
  }
  public byte[] Encrypt (byte[] input,Key pubKey)  throws Exception{
        SecureRandom  random = new SecureRandom();
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
        byte[] cipherText = cipher.doFinal(input); 
        return cipherText;
  }
  public byte[] Decrypt (byte[] cipherText,Key privKey)  throws Exception{
      cipher.init(Cipher.DECRYPT_MODE, privKey);
       byte[] plainText = cipher.doFinal(cipherText); 
       return plainText;
  }
 public KeyPair ElGamalPair(){
    return generator.genKeyPair();
 }
}