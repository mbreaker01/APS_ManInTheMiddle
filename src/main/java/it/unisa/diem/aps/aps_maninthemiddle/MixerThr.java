/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Homomorphism;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.clientProtocol;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.serverProtocol;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.SetupParameters;
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
import java.util.ArrayList;
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
    
    static void clientProtocol(Socket cSock, String msg) throws Exception{
        OutputStream     out = cSock.getOutputStream();
        InputStream      in = cSock.getInputStream();
        out.write(Utils.toByteArray(msg));
        
        System.out.println("Mixer's connection ended");
    }
    
    static String readFromIn(InputStream in, char end) throws IOException{
        int ch = 0;
        String c = "";
        
        while ((ch = in.read()) != end){
            c = c.concat(String.valueOf( ch-48 ));
        }
        
        return c;
    }
    
    
    static byte[] serverProtocol(Socket sSock) throws Exception{
        System.out.println("session started.");
        
        InputStream in = sSock.getInputStream();

        int len = Integer.getInteger(readFromIn(in, '\n'));
        
        ArrayList<String> eList = new ArrayList<>();

        
        for(int i=0; i<len; i++){
            eList.add(readFromIn(in, '\n'));     
        }
        
        
        
        

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

        System.out.println(CT.C);
        System.out.println(CT.C2);
        sslSock.close();
        return CT;
        
        
        
        
        
        
        InputStream in = sSock.getInputStream();
        OutputStream out = sSock.getOutputStream();
        
        ElGamalSK Params=SetupParameters(512);
        
       byte[] input = in.readAllBytes();
            
            int ch = 0;
            int i = 0;
            char[] msg = new char[2048];
            while ((ch = in.read()) != '\n'){
            System.out.print((char)ch);
            msg[i] = (char)ch;
            i++;
            TimeUnit.SECONDS.sleep(1);
            }
            String Smsg=String.valueOf(msg);
            BigInteger C =new BigInteger(Smsg);
            
            ch = 0;
            i = 0;
            char[] msg1 = new char[2048];
            while ((ch = in.read()) != '\n'){
            System.out.print((char)ch);
            msg1[i] = (char)ch;
            i++;
            TimeUnit.SECONDS.sleep(1);
            }
            String Smsg1=String.valueOf(msg1);
            BigInteger C2 =new BigInteger(Smsg1);
            ElGamalCT CT = new ElGamalCT(C,C2);
            
            System.out.println(CT.C);
            System.out.println(CT.C2);
            
            BigInteger M1=new BigInteger("0");
            ObjectInputStream inputF;
            inputF = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\PublicKeys.txt")));
            ElGamalPK PK = (ElGamalPK)inputF.readObject();
            ElGamalCT CT1=EncryptInTheExponent(PK,M1); 
            ElGamalCT CTH=Homomorphism(PK,CT1,CT);
            System.out.println(CTH.C);
            System.out.println(CTH.C2);
            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
            ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
            oos1.writeObject(CTH);
            oos1.flush();
            byte [] CTSend = bos1.toByteArray();
            
        sSock.close(); // close connection
        System.out.println("session closed.");
        return CTSend;
        
        
    }

    public static void main(String[] args) throws Exception{
        if(args.length != 3){
            System.err.println("Numero di parametri errato");
            return;
        }
        
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(Integer.valueOf(args[0]));
        while(true){
            sSock.setNeedClientAuth(true);       
            SSLSocket sslSock = (SSLSocket)sSock.accept();
            byte[] B=serverProtocol(sslSock);
            
            
                    //starting comunication with mixer1
            //SSLContext sslContext = createSSLContext(args[2]); 
            //SSLSocketFactory fact1 = sslContext.getSocketFactory(); 
            //SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", Integer.valueOf(args[1]));

           // clientProtocol(cSock, msg);
        }
    }
    
}
