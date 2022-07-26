/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

import java.io.Serializable;

/**
 *
 * @author simon
 */
class Scheda implements Serializable{
    
    private String voto;
    private final String[] candidati = {"Boccia", "Ciardiello", "D'Ambrosio", "Granato"};
    
    public Scheda() {
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }

    public String[] getCandidati() {
        return candidati;
    }
    
}
