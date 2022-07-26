package it.unisa.diem.aps.aps_maninthemiddle;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
//structures for ElGamal public-key
//Vincenzo Iovino
public class ElGamalPK implements Serializable{
		BigInteger g,h,p,q; // description of the group and public-key h=g^s
		int securityparameter; // security parameter
		
		public ElGamalPK(BigInteger p,BigInteger q,BigInteger g,BigInteger h,int securityparameter) {
			this.p=p;
			this.q=q;
			this.g=g;
			this.h=h;
			this.securityparameter=securityparameter;
					
		}
		}