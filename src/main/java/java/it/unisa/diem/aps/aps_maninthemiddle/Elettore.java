/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java.it.unisa.diem.aps.aps_maninthemiddle;

import java.it.unisa.diem.aps.aps_maninthemiddle.EnumerationState.State;

/**
 *
 * @author simon
 */
public class Elettore implements Runnable{
    
    private int ID;
    private State state;
    private int absenceCount;

    private static int cnt = 0;

    public Elettore(int ID, State state, int absenceCount) {
        this.ID = cnt++;
        this.state = state;
        this.absenceCount = absenceCount;
    }

    public int getID() {
        return ID;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }
    
    public Scheda vote(Scheda s, Candidato c){
        s.setVoto(c);
        return s;
    }

    @Override
    public void run() {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
