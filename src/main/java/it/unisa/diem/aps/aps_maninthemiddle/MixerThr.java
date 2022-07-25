/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.clientProtocol;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.serverProtocol;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
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
    
    static String serverProtocol(Socket sSock) throws Exception{
        System.out.println("session started.");
        
        InputStream in = sSock.getInputStream();
        OutputStream out = sSock.getOutputStream();
        
        System.out.println(sSock.getLocalSocketAddress());
        System.out.println(sSock.getRemoteSocketAddress());
        
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
        if(args.length != 3){
            System.err.println("Numero di parametri errato");
            return;
        }
        
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(Integer.valueOf(args[0]));
        while(true){
            sSock.setNeedClientAuth(true);       
            SSLSocket sslSock = (SSLSocket)sSock.accept();

            String msg = serverProtocol(sslSock);
            
                    //starting comunication with mixer1
            SSLContext sslContext = createSSLContext(args[2]); 
            SSLSocketFactory fact1 = sslContext.getSocketFactory(); 
            SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", Integer.valueOf(args[1]));

            clientProtocol(cSock, msg);
        }
    }
    
}
