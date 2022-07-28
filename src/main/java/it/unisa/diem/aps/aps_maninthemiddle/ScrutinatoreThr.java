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
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

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
        
        File f = new File(path);
        Scanner sc = new Scanner(f);
        
        while (sc.hasNext()) {
            votes.add(new ElGamalCT(new BigInteger(sc.next()), new BigInteger(sc.next())));
             
        }
        
        sc.close();

        return votes;
    }

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\Urna.txt";
        ArrayList<ElGamalCT> votes = readVotes(path);

        ElGamalSK []SK=new ElGamalSK[4];

        ObjectInputStream input;
        
        ArrayList<String> fileName = new ArrayList<>();
        fileName.add("M0");
        fileName.add("M1");
        fileName.add("M2");
        fileName.add("S");
        
        int i = 0;
        
        for(String s: fileName){
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\" + s + "Keys.txt")));
            SK[i] = (ElGamalSK)input.readObject();
            i++;
            input.close();
        }
        
        HashMap<BigInteger, Integer> results = new HashMap<>();

        for(ElGamalCT vote: votes){
            //decrypt
            for (i=0;i<3;i++){
                vote=PartialDecrypt(vote,SK[i]);
            }
            BigInteger v=Decrypt(vote,SK[3]);
            if(results.containsKey(v)){
                results.put(v, results.get(v) + 1);
            }
            else{
                results.put(v, 1);
            }
        }
        
        
        
       
    }
    
}
