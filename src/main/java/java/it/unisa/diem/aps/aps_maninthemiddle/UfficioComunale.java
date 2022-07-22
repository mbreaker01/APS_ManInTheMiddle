/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.it.unisa.diem.aps.aps_maninthemiddle;

import java.util.List;

/**
 *
 * @author simon
 */
public class UfficioComunale {
    private int ID;
    private List<Candidato> Candidati;
    

    private static int cnt = 0;

    public UfficioComunale() {
        this.ID = cnt++;
    }
}
