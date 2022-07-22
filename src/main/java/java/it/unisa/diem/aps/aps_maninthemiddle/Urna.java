/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.it.unisa.diem.aps.aps_maninthemiddle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon
 */
class Urna {
    private List<Scheda> schede;

    public Urna() {
        this.schede = new ArrayList<>();
    }
    
    public void addSchede(Scheda s){
        schede.add(s);
    }
}
