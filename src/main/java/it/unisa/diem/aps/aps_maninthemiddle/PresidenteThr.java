/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Homomorphism;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.readCharFromIn;
import static it.unisa.diem.aps.aps_maninthemiddle.MixerThr.readIntFromIn;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLClient.Protocol;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLClient.s;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLClientWithClientAuth.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLServer.Protocol;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLServer.s;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
public class PresidenteThr extends SSLServer{
    
    private static Map<String, String> votanti;

    static SSLContext createSSLContext() throws Exception{
        KeyManagerFactory keyFact = KeyManagerFactory.getInstance("SunX509");
        KeyStore clientStore = KeyStore.getInstance("JKS");

        clientStore.load(new FileInputStream("Pkeystore.jks"), "PresidentePass".toCharArray());

        keyFact.init(clientStore, "PresidentePass".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS"); 
        sslContext.init(keyFact.getKeyManagers(), null, null);
		
        return sslContext;
    }    
    
    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            System.err.println("Numero di parametri errato");
            return;
        }
        
        votanti = new HashMap<>();
        ArrayList<String> pending = new ArrayList<>();
        ArrayList<ElGamalCT> CTList = new ArrayList<>();
        
        for(int i=0;i<10;i++){
            votanti.put("CN=E"+i, "no");
        }
        
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(Integer.valueOf(args[0]));
        
        while(true){
            sSock.setNeedClientAuth(true);
            SSLSocket sslSock = (SSLSocket)sSock.accept();
            String certName = sslSock.getSession().getPeerPrincipal().getName();
            ElGamalCT CT;
            if(certName.compareTo("CN=Urna") == 0){
                ArrayList<String> toRemove = urnaProtocol(sslSock);
                for(String v : toRemove){
                    if(votanti.containsKey(v)){
                        votanti.put(v, "si");
                    }
                }
            }
            else if(votanti.containsKey(certName) && votanti.get(certName).compareTo("no")==0){
                CT= elettoreProtocol(sslSock, certName);
                pending.add(certName);
                CTList.add(CT);
                if(pending.size()>4){
                    SSLContext sslContext = createSSLContext(); 
                    SSLSocketFactory fact1 = sslContext.getSocketFactory(); 
                    SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", Integer.valueOf(args[1]));
                    Collections.shuffle(CTList);
                    mixerProtocol(cSock,CTList,pending);
                    pending.clear();
                    CTList.clear();
                }
            }
            
            //starting comunication with mixer1
           
        }
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

    private static ArrayList<String> urnaProtocol(SSLSocket sslSock) throws IOException {
        System.out.println("session started.");
        
        InputStream in = sslSock.getInputStream();

        int len = Integer.valueOf(readIntFromIn(in, '\n'));
        
        ArrayList<String> eList = new ArrayList<>();
        
        for(int i=0; i<len; i++){
            eList.add(readCharFromIn(in, '\n'));
        }
        

        return eList;
        
        

    }

    private static ElGamalCT elettoreProtocol(SSLSocket sslSock, String certName) throws IOException, ClassNotFoundException, InterruptedException {
        
        InputStream in = sslSock.getInputStream();

        votanti.put(certName, "attesa");
        int ch = 0;

        String c = "";
        String c2 = "";

        while ((ch = in.read()) != '\n'){
            c = c.concat(String.valueOf(ch-48));
        }

        while ((ch = in.read()) != '\n'){
            c2 = c2.concat(String.valueOf(ch-48));
        }

        BigInteger C = new BigInteger(c);
        BigInteger C2 = new BigInteger(c2);

        ElGamalCT CT = new ElGamalCT(C,C2);
      
        sslSock.close();
        return CT;
            
        
    }

    private static void mixerProtocol(SSLSocket cSock,ArrayList<ElGamalCT> CTList, ArrayList<String> pending) throws IOException {
        OutputStream     out = cSock.getOutputStream();
        String output=pending.size() + "\n";
        for(String el: pending){
            output = output.concat(el + "\n");
        }
        for(ElGamalCT cipher: CTList){
            output = output.concat(cipher.C.toString() + "\n" + cipher.C2.toString() + "\n");
        }
        out.write(output.getBytes());
        System.out.println("Presidente's connection ended");
        
    
    }
    
}
