package org.src.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class klasa {
    private transient List<student> uczniowie = new ArrayList<>();
    private String nazwa;
    private Map<String, Integer> planZajec;

    public klasa(String nazwa){
        this.nazwa = nazwa;
        this.uczniowie = new ArrayList<>();

    }
    public int getLiczbaUczniow(){return uczniowie.size();}
    public String getNazwaKlasy(){return nazwa;}
    public List<student> getUczniowie(){
        if(uczniowie== null){
            uczniowie = new ArrayList<>();
        }
        return uczniowie;
    }
    public void dodajUcznia(student nowyUczen){
        if(uczniowie == null){
            uczniowie = new ArrayList<>();
        }
        uczniowie.add(nowyUczen);
    }
    public void usunUcznia(student uczen){
        if(uczniowie == null){
            uczniowie.remove(uczen);
        }

    }

    public void setUczniowie(List<student> uczniowie) { this.uczniowie = uczniowie; }
    @Override
    public String toString(){
        return this.nazwa;
    }
    public Map<String, Integer> getPlanZajec(){
        if(planZajec == null){
            planZajec = new HashMap<>();
        }
        return planZajec;
    }
    public void setPlanZajec(Map<String, Integer> planZajec){
        this.planZajec = planZajec;
    }

}
