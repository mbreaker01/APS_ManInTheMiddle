package it.unisa.diem.aps.aps_maninthemiddle;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.util.concurrent.TimeUnit;


public class SSLServerWithClientAuth
    extends SSLServer
{



    public static void main(
        String[] args)
        throws Exception
    {
     	SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket  sSock = (SSLServerSocket)fact.createServerSocket(4000);
        while(true){
        sSock.setNeedClientAuth(true);       
        SSLSocket sslSock = (SSLSocket)sSock.accept();
        
        Protocol(sslSock);
    }
    }
}
