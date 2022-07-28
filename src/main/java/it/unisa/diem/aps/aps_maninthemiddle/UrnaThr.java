/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Homomorphism;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.clientProtocol;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.readCharFromIn;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.readIntFromIn;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.serverProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Random;
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
public class UrnaThr {
    
    private static ArrayList<String> eList;
    
    static SSLContext createSSLContext() throws Exception{
        KeyManagerFactory keyFact = KeyManagerFactory.getInstance("SunX509");
        KeyStore clientStore = KeyStore.getInstance("JKS");

        clientStore.load(new FileInputStream("Ukeystore.jks"), "UrnaPass".toCharArray());

        keyFact.init(clientStore, "UrnaPass".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS"); 
        sslContext.init(keyFact.getKeyManagers(), null, null);
		
        return sslContext;
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
        
        
        String path = "C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\Urna.txt";
        
        try( PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true))) ){
            
            for(int i=0; i<len; i++){
                out.write(readCharFromIn(in, '\n') + '\n');
                out.write(readCharFromIn(in, '\n') + '\n');
            }  
            
        }
        
        /*
        ObjectOutputStream output;
        
        try {
            output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\Urna.txt", true)));
            for(int i=0; i<len; i++){
                BigInteger C = new BigInteger(readIntFromIn(in, '\n'));
                BigInteger C2 = new BigInteger(readIntFromIn(in, '\n'));

                ElGamalCT CT = new ElGamalCT(C,C2);
                output.writeObject(CT);
            }
            output.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */
        sSock.close(); // close connection
        System.out.println("session closed.");
        
    }
    
    static void clientProtocol(Socket cSock) throws Exception{
        OutputStream     out = cSock.getOutputStream();
        String output=eList.size() + "\n";
        for(String el: eList){
            output = output.concat(el + "\n");
        }
        out.write(output.getBytes());
        System.out.println("Urna's connection ended");
    }

    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            System.err.println("Numero di parametri errato");
            return;
        }
        
        eList = new ArrayList<String>();
        
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(Integer.valueOf(args[0]));
        while(true){
            sSock.setNeedClientAuth(true);
            SSLSocket sslSock = (SSLSocket)sSock.accept();

            serverProtocol(sslSock);
            
                    //starting comunication with mixer1
            SSLContext sslContext = createSSLContext();
            SSLSocketFactory fact1 = sslContext.getSocketFactory();
            SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", Integer.valueOf(args[1]));

            clientProtocol(cSock);
            
            eList.clear();
        }
    }
    
}
