/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.clientProtocol;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.createSSLContext;
import static it.unisa.diem.aps.aps_maninthemiddle.PresidenteThr.serverProtocol;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
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
    
    static void serverProtocol(Socket sSock) throws Exception{
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
        System.out.println((char)ch);
        sSock.close(); // close connection
        System.out.println("session closed.");
        
    }

    public static void main(String[] args) throws Exception{
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(4001);
        while(true){
            sSock.setNeedClientAuth(true);       
            SSLSocket sslSock = (SSLSocket)sSock.accept();

            serverProtocol(sslSock);
        }
    }
    
}
