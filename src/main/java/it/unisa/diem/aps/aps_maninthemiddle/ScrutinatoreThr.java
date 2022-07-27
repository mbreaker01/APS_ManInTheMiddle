/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Decrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.PartialDecrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.UrnaThr.readCharFromIn;
import static it.unisa.diem.aps.aps_maninthemiddle.UrnaThr.readIntFromIn;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author mario
 */
public class ScrutinatoreThr {

    /**
     * @param args the command line arguments
     */
    static ArrayList<ElGamalCT> readVotes(String path) throws Exception{
        System.out.println("session started.");
        
        ArrayList<ElGamalCT> votes = new ArrayList<>();
        
        ElGamalCT elGamal;
        
        ObjectInputStream input;
        
        try {
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
            
            while((elGamal = (ElGamalCT) input.readObject()) != null){
                votes.add(elGamal);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
   
        
        return votes;
    }
    
    
    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\Urna.txt";
        ArrayList<ElGamalCT> votes = readVotes(path);
        
        ElGamalSK []SK=new ElGamalSK[4];
        
        ObjectInputStream input;

        input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\M0Keys.txt")));
        SK[0] = (ElGamalSK)input.readObject();
        
        input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\M1Keys.txt")));
        SK[1] = (ElGamalSK)input.readObject();
        
        input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\M2Keys.txt")));
        SK[2] = (ElGamalSK)input.readObject();
        
        input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\SKeys.txt")));
        SK[3] = (ElGamalSK)input.readObject();
        
        for(ElGamalCT vote: votes){
            //decrypt
            for (int i=0;i<3;i++){
                vote=PartialDecrypt(vote,SK[i]);
                i++;
            }
            BigInteger res=Decrypt(vote,SK[3]);
            System.out.println(res);
        }
        
    }
    
}
