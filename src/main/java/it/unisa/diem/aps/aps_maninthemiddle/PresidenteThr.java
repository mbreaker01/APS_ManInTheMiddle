/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.SSLClient.Protocol;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLClient.s;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLClientWithClientAuth.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLServer.Protocol;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLServer.s;
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
public class PresidenteThr extends SSLServer{

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
    
    static String serverProtocol(Socket sSock) throws Exception{
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
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(4000);
        while(true){
            sSock.setNeedClientAuth(true);       
            SSLSocket sslSock = (SSLSocket)sSock.accept();

            String msg = serverProtocol(sslSock);
            
                    //starting comunication with mixer1
            SSLContext sslContext = createSSLContext(); 
            SSLSocketFactory fact1 = sslContext.getSocketFactory(); 
            SSLSocket cSock = (SSLSocket)fact1.createSocket("localhost", 4001);

            clientProtocol(cSock, msg);
        }
    }
    
}
