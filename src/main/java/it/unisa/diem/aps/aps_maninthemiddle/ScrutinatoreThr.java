/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

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
            // Iterating over elements in object
            System.out.println(sc.next());
        }
        
        
        
   
        
        return votes;
    }
    
    static ArrayList<ElGamalCT> writeVotes(String path) throws Exception{
        System.out.println("session started.");
        
        ArrayList<ElGamalCT> votes = new ArrayList<>();
        
        ElGamalCT elGamal;
        try( PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true))) ){
            
            for(int i=0; i<20; i++){
                Random randNum = new Random();
                String c = String.valueOf(randNum.nextInt() * 1000000 );
                String c2 = String.valueOf(randNum.nextInt() * 1000000 );
                
                out.write(c + '\n');
                out.write(c2 + '\n');
            }  
            
        }
        
        return votes;
        
       
    }
    
    
    
    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\mario\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\Urna.txt";
        writeVotes(path);
        ArrayList<ElGamalCT> votes = readVotes(path);
        
        //System.out.println(votes.size());
        
        
        
        
        
        
        
        
    }
    
}
