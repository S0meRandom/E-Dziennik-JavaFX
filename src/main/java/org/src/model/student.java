package org.src.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class student {
    private String imie;
    private String nazwisko;
    private klasa klasa;
    private Map<String, List<Double>> oceny = new HashMap<>();
    public int userId;
    private transient konto Konto;


    public student(int userId,String imie, String nazwisko,konto Konto  ){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.Konto = Konto;
        this.userId = Konto.getId();
        this.klasa = new klasa("Brak");

    }
    public String getImie(){return imie;}
    public String getNazwisko(){return nazwisko;}
    public klasa getKlasa(){return klasa;}
    public void setImie(String i){this.imie = i;}
    public void setNazwisko(String n){this.nazwisko = n;}
    public void setKlasa(klasa kl){this.klasa = kl;}
    public String getNazwaKlasy(){return klasa.getNazwaKlasy();}
    public int getUserId(){return userId;}
    public Map<String, List<Double>> getOceny(){
        if(oceny == null){
            oceny = new HashMap<>();
        }
        return oceny;
    }
    public void dodajOcene(String przedmiot, Double ocena){
        getOceny().computeIfAbsent(przedmiot,k-> new ArrayList<>()).add(ocena);

    }
    public void usunOcene(String przedmiot, Double ocena){
        if(oceny.containsKey(przedmiot)){
            getOceny().get(przedmiot).remove(ocena);
        }
    }
    public String getNazwyKlasyStudenta(){
        return(klasa != null)? klasa.getNazwaKlasy() : "";
    }


}
