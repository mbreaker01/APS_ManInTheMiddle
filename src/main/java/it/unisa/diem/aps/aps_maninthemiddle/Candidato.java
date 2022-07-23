/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.aps.aps_maninthemiddle;

/**
 *
 * @author simon
 */
class Candidato {
    private static int cnt = 0;
    
    private int ID;

    public Candidato() {
        this.ID = cnt++;
    }

    public int getID() {
        return ID;
    }
    
}
