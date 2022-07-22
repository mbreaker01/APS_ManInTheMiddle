/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.it.unisa.diem.aps.aps_maninthemiddle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author simon
 */
public class UfficioComunale {
    private int ID;
    private List<Candidato> candidati;
    private List<Elettore> elettori;
    private HashMap<String,String> credenziali;

    private static int cnt = 0;

    public UfficioComunale(int ID, List<Candidato> candidati, List<Elettore> elettori, HashMap<String, String> credenziali) {
        this.ID = cnt++;
        this.candidati = new ArrayList<Candidato>();
        this.elettori = new ArrayList<Elettore>();
        this.credenziali = new HashMap<String, String>();
    }

    public List<Candidato> getCandidati() {
        return candidati;
    }

    public List<Elettore> getElettori() {
        return elettori;
    }

    public HashMap<String, String> getCredenziali() {
        return credenziali;
    }
   
    public void addCandidato(Candidato c){
        candidati.add(c);   
    }
    
    public void addElettore(Elettore e){
        elettori.add(e);
    }
}
