/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Homomorphism;
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
    
    static void clientProtocol(Socket cSock, String msg) throws Exception{
        OutputStream     out = cSock.getOutputStream();
        InputStream      in = cSock.getInputStream();
        out.write(Utils.toByteArray(msg));
        
        System.out.println("Presidente's connection ended");
    }
    
    static String serverProtocol(Socket sSock, String certName) throws Exception{
        System.out.println("session started.");
        
        InputStream in = sSock.getInputStream();
        OutputStream out = sSock.getOutputStream();
        
        int ch = 0;
        int i = 0;
        char[] msg = new char[20];
        while ((ch = in.read()) != '\n'){
            System.out.print((char)ch);
            msg[i] = (char)ch;
            i++;
            TimeUnit.SECONDS.sleep(1);
            }
        msg[i]='\n';
        System.out.println((char)ch);
        sSock.close(); // close connection
        System.out.println("session closed.");
        
        
        return String.valueOf(msg);
        
    }
    


    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            System.err.println("Numero di parametri errato");
            return;
        }
        
        votanti = new HashMap<String, String>();
        for(int i=0;i<5;i++){
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
                urnaProtocol(sslSock);
            }
            else{
            CT= elettoreProtocol(sslSock, certName);
            SSLContext sslContext = createSSLContext(); 
            SSLSocketFactory fact1 = sslContext.getSocketFactory(); 
            SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", Integer.valueOf(args[1]));

            mixerProtocol(cSock,CT);
            }
            
            //starting comunication with mixer1
           
        }
    }

    private static void urnaProtocol(SSLSocket sslSock) {
        
        

    }

    private static ElGamalCT elettoreProtocol(SSLSocket sslSock, String certName) throws IOException, ClassNotFoundException, InterruptedException {
        if(votanti.containsKey(certName) && votanti.get(certName).compareTo("no")==0){
            InputStream in = sslSock.getInputStream();
           
            votanti.put(certName, "attesa");
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
            
            System.out.println("nell'if");
            sslSock.close();
            return CT;
            
        }
        return null;
    }

    private static void mixerProtocol(SSLSocket cSock,ElGamalCT CT) throws IOException {
         OutputStream     out = cSock.getOutputStream();
        out.write(CT.C.toByteArray());
        out.write('\n');
        out.write(CT.C2.toByteArray());
        out.write('\n');
        System.out.println("Presidente's connection ended");
        
    
    }
    
}
