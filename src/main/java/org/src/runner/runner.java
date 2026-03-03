package org.src.runner;

import com.google.gson.reflect.TypeToken;
import org.src.DTO.DaneSystemu;
import org.src.Konta.JsonDataBase;
import org.src.UI.windowStart;
import org.src.model.teacher;
import org.src.model.klasa;
import org.src.model.student;

import java.util.List;


public class runner  {
    public static void main(String[] args) throws Exception {
        JsonDataBase<student> db = new JsonDataBase<>("src/main/java/org.src/Konta/studenci.json", new TypeToken<List<student>>(){}.getType());
        JsonDataBase<klasa> dbK = new JsonDataBase<>("src/main/java/org.src/Konta/klasy.json", new TypeToken<List<klasa>>(){}.getType());
        JsonDataBase<teacher> dbT = new JsonDataBase<>("src/main/java/org.src/Konta/nauczyciele.json",new TypeToken<List<teacher>>(){}.getType());
        JsonDataBase<String> dbP = new JsonDataBase<>("src/main/java/org.src/Konta/przedmioty.json",new TypeToken<List<String>>(){}.getType());
        DaneSystemu.inicjalizuj(db, dbK,dbT,dbP);
        windowStart.launch(windowStart.class);

    }


}