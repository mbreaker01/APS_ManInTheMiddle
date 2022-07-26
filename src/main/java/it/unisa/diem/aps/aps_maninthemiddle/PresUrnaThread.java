/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author giuseppe
 */
public class PresUrnaThread implements Runnable{

    public PresUrnaThread() {
    }
    
    static void server2Protocol(Socket sSock) throws Exception{
        System.out.println("session Urna started.");
        
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
        System.out.println("session Urna closed.");
        
        
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            SSLServerSocket  sSock;
            try {
                sSock = (SSLServerSocket)fact.createServerSocket(4005);
                while(true){
                    sSock.setNeedClientAuth(true);
                    SSLSocket sslSock = (SSLSocket)sSock.accept();

                    
                    server2Protocol(sslSock);
                }
            }catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        
        }
    
    }
}
