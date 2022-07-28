/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import it.unisa.diem.aps.aps_maninthemiddle.ElGamal;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Decrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Encrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Homomorphism;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.AggregatePartialPublicKeys;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.PartialDecrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.Setup;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.SetupParameters;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author giuseppe
 */
public class KeysGenerator implements Serializable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, ClassNotFoundException  {
         ElGamalSK Params=SetupParameters(512);   
	  ElGamalSK []SK=new ElGamalSK[4];
         for (int i=0;i<4;i++) { 
		  SK[i]=Setup(Params); 
	  }
        
        ObjectOutputStream output;
       for (int i=0;i<3;i++){
        try {
            output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\M"+i+"Keys.txt")));
            output.writeObject(SK[i]);
            output.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       }
         try {
            output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\SKeys.txt")));
            output.writeObject(SK[3]);
            output.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ElGamalPK []PartialPK=new ElGamalPK[4];
            for (int i=0;i<4;i++) 	
            PartialPK[i]=SK[i].PK;
            output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\PublicKeys.txt")));
            ElGamalPK PK=AggregatePartialPublicKeys(PartialPK);
            output.writeObject(PK);

            output.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }   
}
