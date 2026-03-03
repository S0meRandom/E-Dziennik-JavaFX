package org.src.DTO;

import org.src.Konta.JsonDataBase;
import org.src.model.teacher;
import org.src.model.klasa;
import org.src.model.konto;
import org.src.model.student;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DaneSystemu {
    private static List<student> wszyscyStudenci;
    private static List<klasa> wszystkieKlasy;
    private static List<teacher> wszyscyNauczyciele;
    public static List<String> wszystkiePrzedmioty;
    private JsonDataBase<student> db;
    private JsonDataBase<teacher> dbT;
    private JsonDataBase<klasa> dbK;
    private JsonDataBase<String> dbP;




    public static void inicjalizuj(JsonDataBase<student> db, JsonDataBase<klasa> dbK, JsonDataBase<teacher> dbT, JsonDataBase<String> dbP) throws IOException {
        wszyscyStudenci = db.loadAll();
        wszystkieKlasy = dbK.loadAll();
        wszyscyNauczyciele = dbT.loadAll();
        wszystkiePrzedmioty = dbP.loadAll();


        if (wszystkieKlasy != null) {
            wszystkieKlasy.removeIf(k -> k.getNazwaKlasy() == null || k.getNazwaKlasy().equals("nazwa"));
        } else {
            wszystkieKlasy = new ArrayList<>();
        }


        if (wszystkieKlasy.isEmpty() && wszyscyStudenci != null) {
            Map<String, klasa> mapa = new HashMap<>();
            for (student s : wszyscyStudenci) {
                if (s.getKlasa() != null) {
                    mapa.putIfAbsent(s.getKlasa().getNazwaKlasy(), s.getKlasa());
                }
            }
            wszystkieKlasy.addAll(mapa.values());
        }


        for (klasa k : wszystkieKlasy) {

            List<student> uczniowie = wszyscyStudenci.stream()
                    .filter(s -> s.getKlasa() != null && s.getNazwaKlasy().equals(k.getNazwaKlasy()))
                    .collect(Collectors.toList());

            k.setUczniowie(uczniowie);


            for (student s : uczniowie) {
                s.setKlasa(k);
            }
        }
    }


    public static List<klasa> getWszystkieKlasy() {
        return wszystkieKlasy;
    }
    public static List<teacher> getWszyscyNauczyciele(){
        return wszyscyNauczyciele;
    }
    public static List<student> getWszyscyStudenci(){return wszyscyStudenci;}
    public static List<klasa> generujKlasy(List<student> studenci) {
        if (studenci == null) return new ArrayList<>();


        return studenci.stream()
                .map(student::getKlasa)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        klasa::getNazwaKlasy,
                        k -> k,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
    public static void refresh(JsonDataBase<student> db,JsonDataBase<teacher> dbT) throws IOException {
        wszyscyStudenci = db.loadAll();
        wszystkieKlasy = generujKlasy(wszyscyStudenci);
        wszyscyNauczyciele = dbT.loadAll();
    }
    public static student znajdzStudentaPoId(konto user){
        int szukaneId = user.getId();
        return wszyscyStudenci.stream().filter(s-> s.getUserId()==szukaneId).findFirst().orElse(null);
    }
    public static teacher znajdzNauczycielaPoId(konto user){
        int szukaneId = user.getId();
        return wszyscyNauczyciele.stream().filter(s-> s.getId()==szukaneId).findFirst().orElse(null);
    }
    public List<String> getWszystkiePrzedmioty(){
        return wszystkiePrzedmioty;
    }

}