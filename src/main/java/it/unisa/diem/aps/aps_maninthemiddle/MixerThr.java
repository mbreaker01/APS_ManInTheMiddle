/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Homomorphism;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.SetupParameters;
import static it.unisa.diem.aps.aps_maninthemiddle.UrnaThr.readCharFromIn;
import static it.unisa.diem.aps.aps_maninthemiddle.Utils.ZKP;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author giuseppe
 */
public class MixerThr {
    
    private static ArrayList<String> eList;
    private static ArrayList<ElGamalCT> CTList;
    private static ArrayList<ElGamalCT> inputs;


    
    static SSLContext createSSLContext(String n) throws Exception{
        KeyManagerFactory keyFact = KeyManagerFactory.getInstance("SunX509");
        KeyStore clientStore = KeyStore.getInstance("JKS");
        
        String store = "M" + n + "keystore.jks";
        String pass = "Mixer" + n + "Pass";
        
        clientStore.load(new FileInputStream(store), pass.toCharArray());

        keyFact.init(clientStore, pass.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS"); 
        sslContext.init(keyFact.getKeyManagers(), null, null);
		
        return sslContext;
    }
    
    static void writeOnVoteChain(String path) throws IOException{
        
        try( PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true))) ){
            
            out.write("---------------------------------- BLOCK START ----------------------------------\n");
            out.write("INPUTS:\n");
            for(ElGamalCT in: inputs){
                out.write( "\t" + in.C.toString() + " - " + in.C2.toString() + "\n");
            }
            out.write("OUTPUTS:\n");
            for(ElGamalCT in: CTList){
                out.write( "\t" + in.C.toString() + " - " + in.C2.toString() + "\n");
            }
            out.write("---------------------------------- BLOCK END ----------------------------------\n");
            out.close();
            
        }
        
    }

    
    
    
    static void clientProtocol(Socket cSock) throws Exception{
        String path = "C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\Votechain.txt";
        writeOnVoteChain(path);
        
        OutputStream     out = cSock.getOutputStream();
        String output=eList.size() + "\n";
        for(String el: eList){
            output = output.concat(el + "\n");
        }
        for(ElGamalCT cipher: CTList){
            output = output.concat(cipher.C.toString() + "\n" + cipher.C2.toString() + "\n");
        }
        out.write(output.getBytes());
        System.out.println("Mixer's connection ended");
    }
    
    static String readIntFromIn(InputStream in, char end) throws IOException{
        int ch = 0;
        String c = "";
        
        while ((ch = in.read()) != end){
            c = c.concat(String.valueOf( ch-48 ));
        }
        return c;
    }
    
    static String readCharFromIn(InputStream in, char end) throws IOException{
        int ch = 0;
        String c = "";
        
        while ((ch = in.read()) != end){
            c = c.concat(Character.toString((char)ch));
        }
        return c;
    }
    
    static void serverProtocol(Socket sSock) throws Exception{
        System.out.println("session started.");
        
        InputStream in = sSock.getInputStream();

        int len = Integer.valueOf(readIntFromIn(in, '\n'));
        
        for(int i=0; i<len; i++){
            eList.add(readCharFromIn(in, '\n'));
        }
        
        ObjectInputStream input;

        input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\PublicKeys.txt")));
        ElGamalPK PK = (ElGamalPK)input.readObject();
        input.close();
        
        for(int i=0; i<len; i++){
            BigInteger C = new BigInteger(readIntFromIn(in, '\n'));
            BigInteger C2 = new BigInteger(readIntFromIn(in, '\n'));
            
            ElGamalCT CT = new ElGamalCT(C,C2);
            
            CTList.add(CT);
            
            BigInteger M1=new BigInteger("0");
            ElGamalCT CT1=EncryptInTheExponent(PK,M1);
            ElGamalCT CTH=Homomorphism(PK,CT1,CT);
            
            ZKP(CT, CTH);
            
            CTList.add(CTH);
        }
        
        Collections.shuffle(CTList);
        
        sSock.close(); // close connection
        System.out.println("session closed.");
        
    }

    public static void main(String[] args) throws Exception{
        if(args.length != 3){
            System.err.println("Numero di parametri errato");
            return;
        }
        
        eList = new ArrayList<String>();
        CTList = new ArrayList<ElGamalCT>();
        inputs = new ArrayList<ElGamalCT>();
        
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(Integer.valueOf(args[0]));
        while(true){
            sSock.setNeedClientAuth(true);       
            SSLSocket sslSock = (SSLSocket)sSock.accept();
            serverProtocol(sslSock);
            
            //starting comunication with mixer1
            SSLContext sslContext = createSSLContext(args[2]); 
            SSLSocketFactory fact1 = sslContext.getSocketFactory(); 
            SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", Integer.valueOf(args[1]));
            clientProtocol(cSock);
            
            eList.clear();
            CTList.clear();

        }
    }
    
}
