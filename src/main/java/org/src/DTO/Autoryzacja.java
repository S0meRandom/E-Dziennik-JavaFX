package org.src.DTO;

import com.google.gson.reflect.TypeToken;
import org.mindrot.jbcrypt.BCrypt;
import org.src.Konta.JsonDataBase;
import org.src.model.teacher;
import org.src.model.konto;
import org.src.model.student;
import org.src.uprawnienia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Autoryzacja {
    private JsonDataBase<konto> dbKonta;
    private JsonDataBase<student> dbStudent;
    private JsonDataBase<teacher> dbTeacher;

    public Autoryzacja() throws IOException {
        Type listType = new TypeToken<List<konto>>() {}.getType();
        this.dbKonta = new JsonDataBase<>("src/main/java/org.src/Konta/konta.json", listType);

        Type listTypeStudenci = new TypeToken<List<student>>() {}.getType();
        this.dbStudent = new JsonDataBase<>("src/main/java/org.src/Konta/studenci.json", listTypeStudenci);

        Type listTypeTeacher = new TypeToken<List<teacher>>(){}.getType();
        this.dbTeacher = new JsonDataBase<>("src/main/java/org.src/Konta/nauczyciele.json",listTypeTeacher);


    }

    public Optional<konto> Login(String login, String password) throws IOException {
        List<konto> existingUseres = dbKonta.loadAll();

        return existingUseres.stream().filter(u -> u.getLogin().equalsIgnoreCase(login))
                .findFirst().filter(u -> BCrypt.checkpw(password, u.getHaslo()));
    }

    public boolean registerStudent(String login, String password, String imie, String nazwisko) throws Exception {

        List<konto> existingUsers = dbKonta.loadAll();
        for (konto k : existingUsers) {
            if (k.getLogin().equalsIgnoreCase(login)) {
                return false;
            }

        }
        int noweId = existingUsers.stream()
                .mapToInt(konto::getId)
                .max()
                .orElse(0) + 1;
        String hashPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        konto noweKonto = new konto(noweId,login,hashPassword,uprawnienia.UCZEN);
        student nowyStudent = new student(noweId,imie,nazwisko,noweKonto);
        dbKonta.save(noweKonto);
        dbStudent.save(nowyStudent);
        return true;

    }
    public boolean registerTeacher(String login, String password,String imie,String nazwisko) throws Exception {
        List<konto> existingUsers = dbKonta.loadAll();
        for (konto k : existingUsers) {
            if (k.getLogin().equalsIgnoreCase(login)) {
                return false;
            }

        }
        int noweId = existingUsers.stream()
                .mapToInt(konto::getId)
                .max()
                .orElse(0) + 1;
        String hashPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        konto noweKonto = new konto(noweId,login,hashPassword,uprawnienia.NAUCZYCIEL);
        teacher nowyNauczyciel = new teacher(noweId,imie,nazwisko,new ArrayList<>(),noweKonto);


        dbKonta.save(noweKonto);
        dbTeacher.save(nowyNauczyciel);
        return true;

    };
    public boolean registerAdmin(String login, String password,String imie, String nazwisko) throws Exception {
        List<konto> existingUsers = dbKonta.loadAll();
        for (konto k : existingUsers) {
            if (k.getLogin().equalsIgnoreCase(login)) {
                return false;
            }

        }
        int noweId = existingUsers.stream()
                .mapToInt(konto::getId)
                .max()
                .orElse(0) + 1;
        String hashPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        konto noweKonto = new konto(noweId,login,hashPassword,uprawnienia.ADMIN);



        dbKonta.save(noweKonto);
        return true;


    };


}
