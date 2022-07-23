/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.SSLClient.Protocol;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author giuseppe
 */
public class ElettoreThr {

    static SSLContext createSSLContext() throws Exception{
        KeyManagerFactory keyFact = KeyManagerFactory.getInstance("SunX509");
        KeyStore clientStore = KeyStore.getInstance("JKS");

        clientStore.load(new FileInputStream("Ekeystore.jks"), "ElettorePass".toCharArray());

        keyFact.init(clientStore, "ElettorePass".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS"); 
	sslContext.init(keyFact.getKeyManagers(), null, null);
		
        return sslContext;
    }
    
    static void clientProtocol(Socket cSock, String msg) throws Exception{
        OutputStream     out = cSock.getOutputStream();
        InputStream      in = cSock.getInputStream();
        out.write(Utils.toByteArray(msg));
        
        System.out.println("Elettore's connection ended");
    }
    
    public static void main(String[] args) throws Exception
    {
        
        SSLContext sslContext = createSSLContext(); 
        SSLSocketFactory fact = sslContext.getSocketFactory(); 
        SSLSocket cSock = (SSLSocket)fact.createSocket("localhost", 4000);

        clientProtocol(cSock, "ciao\n");
        //while (true){
        //}
    }
}
