/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon
 */
public class Elezione {
    private Presidente presidente;
    private Scrutinatore scrutinatore;
    private Urna urna;
    private List<Candidato> allCandidati;
    private List<Elettore> allElettori;
    private List<UfficioComunale> allUffici;
    private List<Mixer> mixer;

    public Elezione(Presidente presidente, Scrutinatore scrutinatore, Urna urna) {
        this.presidente = presidente;
        this.scrutinatore = scrutinatore;
        this.urna = urna;
        this.allCandidati = new ArrayList<>();
        this.allElettori = new ArrayList<>();
        this.allUffici = new ArrayList<>();
        this.mixer = new ArrayList<>();
        for(UfficioComunale uc : allUffici){
            for(Candidato c : uc.getCandidati()){
                allCandidati.add(c);
            }
            for(Elettore e : uc.getElettori()){
                allElettori.add(e);
            }
        }
        
     
        
        
    }
   

    
    
}
