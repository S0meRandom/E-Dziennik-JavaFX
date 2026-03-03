package org.src.model;

import java.util.ArrayList;

public class teacher {
    private String imie;
    private String nazwisko;
    private transient konto Konto;
    private int id;
    private ArrayList<klasa> klasy;


    public teacher(int Id, String imie, String nazwisko, ArrayList<klasa> klasy, konto Konto){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.Konto = Konto;
        this.id = id;
        this.klasy = klasy;


    }
    public String getImie(){return imie;}
    public String getNazwisko(){return nazwisko;}
    public int getId(){return id;}
    public ArrayList<klasa> getKlasy(){return klasy;}
    public void dodajKlase(klasa nowaKlasa){
        klasy.add(nowaKlasa);

    }



}
