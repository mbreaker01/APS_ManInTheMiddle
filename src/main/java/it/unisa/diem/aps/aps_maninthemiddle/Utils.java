/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import java.math.BigInteger;

/**
 *
 * @author mario
 */
public class Utils {
    
    public static byte[] toByteArray( String string ){
        byte[]	bytes = new byte[string.length()];
        char[]  chars = string.toCharArray();
        
        for (int i = 0; i != chars.length; i++)
        {
            bytes[i] = (byte)chars[i];
        }
        
        return bytes;
    }
    
    public static void ZKPonVoteChain(ElGamalCT CT, BigInteger plain){
        
    }
    
    public static void ZKPonVoteChain(ElGamalCT CT, ElGamalCT CTH){
        
    }
    
    
}
