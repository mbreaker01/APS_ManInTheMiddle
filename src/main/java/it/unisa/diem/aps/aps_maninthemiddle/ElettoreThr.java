/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Decrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.Encrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ElGamal.EncryptInTheExponent;
import static it.unisa.diem.aps.aps_maninthemiddle.SSLClient.Protocol;
import static it.unisa.diem.aps.aps_maninthemiddle.ScrutinatoreThr.readVotes;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.PartialDecrypt;
import static it.unisa.diem.aps.aps_maninthemiddle.ThresholdElGamal.SetupParameters;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author giuseppe
 */
public class ElettoreThr {

    static SSLContext createSSLContext(String n) throws Exception{
        KeyManagerFactory keyFact = KeyManagerFactory.getInstance("SunX509");
        KeyStore clientStore = KeyStore.getInstance("JKS");
        
        String store = "E" + n + "keystore.jks";
        String pass = "Elettore" + n + "Pass";
        
        clientStore.load(new FileInputStream(store), pass.toCharArray());

        keyFact.init(clientStore, pass.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS"); 
        sslContext.init(keyFact.getKeyManagers(), null, null);
		
        return sslContext;
    }   
    
    static void votoProtocol(Socket cSock, String voto) throws Exception {
        OutputStream     out = cSock.getOutputStream();
        
        ElGamalSK Params=SetupParameters(512);
        
        ObjectInputStream input;

        input = new ObjectInputStream(new BufferedInputStream(new FileInputStream("C:\\Users\\giuseppe\\Documents\\NetBeansProjects\\APS_ManInTheMiddle\\src\\main\\java\\PublicKeys.txt")));
        ElGamalPK PK = (ElGamalPK)input.readObject();
        input.close();
        
        BigInteger plaint = new BigInteger(voto);
        
        ElGamalCT CT=Encrypt(PK,plaint);
        
        System.out.println("cipher:");
        System.out.println(CT.C);
        System.out.println(CT.C2);

        out.write((CT.C.toString() + "\n" + CT.C2.toString() + "\n").getBytes());
        
        System.out.println("Elettore's connection ended");
    }
    
    public static void main(String[] args) throws Exception
    {
        if(args.length != 3){
            System.err.println("Numero di parametri errato");
            return;
        }
        
        String voto = args[2];
        
        SSLContext sslContext = createSSLContext(args[1]);
        SSLSocketFactory fact = sslContext.getSocketFactory(); 
        SSLSocket cSock = (SSLSocket)fact.createSocket("localhost", Integer.valueOf(args[0]));

        votoProtocol(cSock, voto);

    }
}
