package org.src.Konta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.src.model.konto;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDataBase<T>{
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file;
    private final Type listType;


    public JsonDataBase(String path,Type listType) throws IOException {
        this.file = new File(path);
        this.listType = listType;
        File folder = file.getParentFile();
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void save(T obj ) throws Exception {
        List<T> list = loadAll();
        list.add(obj);

        try(Writer writer = new FileWriter(file)){
            gson.toJson(list,writer);
        }
    }
    public List<T> loadAll() throws IOException {
        if (!file.exists()) {
            return new ArrayList<>();
        }


        if (file.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            List<T> data = gson.fromJson(reader, listType);
            return data != null ? data : new ArrayList<>();
        }
    }
    public void remove(konto wybrane) throws IOException {
        List<T> lista = loadAll();
        lista.remove(wybrane);
        try(Writer writer = new FileWriter(file)){
            gson.toJson(lista,writer);
        }

    }
    public void saveAll(List<T> data) throws IOException {


        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        }
    }


}
