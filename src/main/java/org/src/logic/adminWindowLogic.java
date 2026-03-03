package org.src.logic;

import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.src.DTO.DaneSystemu;
import org.src.Konta.JsonDataBase;
import org.src.model.teacher;
import org.src.model.klasa;
import org.src.model.konto;
import org.src.model.student;
import org.src.uprawnienia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class adminWindowLogic {
    private JsonDataBase<konto> db;
    List<konto> listaUser;
    List<student> listaUczniow;
    List<teacher> listaNauczycieli;
    List<String> listaPrzedmiotow;
    List<klasa> listaKlas;
    TableView<konto> tabela = new TableView<>();
    private JsonDataBase<student> dbS;
    private JsonDataBase<teacher> dbT;
    private JsonDataBase<klasa> dbK;
    private JsonDataBase<String> dbP;
    private Stage stage;


    public adminWindowLogic() throws IOException {
        this.stage = stage;
        Type listType = new TypeToken<List<konto>>(){}.getType();
        this.db = new JsonDataBase<>("src/main/java/org.src/Konta/konta.json", listType);
        this.listaUser = db.loadAll();
        Type studentType = new TypeToken<List<student>>(){}.getType();
        this.dbS = new JsonDataBase<>("src/main/java/org.src/Konta/studenci.json", studentType);
        this.listaUczniow = dbS.loadAll();
        Type teacherType = new TypeToken<List<teacher>>(){}.getType();
        this.dbT = new JsonDataBase<>("src/main/java/org.src/Konta/nauczyciele.json", teacherType);
        this.listaNauczycieli = dbT.loadAll();
        Type klasaType = new TypeToken<List<klasa>>(){}.getType();
        this.dbK = new JsonDataBase<>("src/main/java/org.src/Konta/klasy.json", klasaType);
        this.listaKlas = dbK.loadAll();
        Type przedmiotType = new TypeToken<List<String>>(){}.getType();
        this.dbP = new JsonDataBase<>("src/main/java/org.src/Konta/przedmioty.json", przedmiotType);
        this.listaPrzedmiotow = dbP.loadAll();
    }
    public void changeUserPassword(konto user, String haslo){
        if(haslo != null){
            user.setHaslo(haslo);
            try {
                db.saveAll(listaUser);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Zmieniono hasło użytkownika: "+user.getLogin());
        }

    }
    public void deleteUser(konto user) throws IOException {
        int idDoUsuniecia = user.getId();

        if(user.getRola() == uprawnienia.NAUCZYCIEL){
            listaNauczycieli.removeIf(n -> n.getId() == idDoUsuniecia);
            dbT.saveAll(listaNauczycieli);
        }
        else if(user.getRola() == uprawnienia.UCZEN){
            listaUczniow.removeIf(s -> s.getUserId() == idDoUsuniecia);
            dbS.saveAll(listaUczniow);
        }


        listaUser.removeIf(u -> u.getId() == idDoUsuniecia);
        db.saveAll(listaUser);

        DaneSystemu.refresh(dbS,dbT);
        System.out.println("Użytkownik został usunięty");

    }
    public void stworzKlase(String nazwa) throws IOException {
        if (nazwa == null || nazwa.trim().isEmpty()) {
            return;
        }


        boolean czyIstnieje = listaKlas.stream()
                .anyMatch(k -> k.getNazwaKlasy().equals(nazwa));

        if (czyIstnieje) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Klasa o nazwie " + nazwa + " już istnieje!");
            alert.show();
            return;
        }
        klasa nowaKlasa = new klasa(nazwa);
        listaKlas.add(nowaKlasa);
        dbK.saveAll(listaKlas);

        Alert sukces = new Alert(Alert.AlertType.INFORMATION, "Pomyślnie utworzono klasę: " + nazwa);
        sukces.show();
    }
    public void otworzOknoPrzypisaniaNauczyciela(konto user) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL); // Blokuje okno pod spodem
        popup.setTitle("Konfiguracja Planu Zajęć");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);


        Label lblKlasa = new Label("Wybierz klasę:");
        ComboBox<klasa> cbKlasa = new ComboBox<>(FXCollections.observableArrayList(DaneSystemu.getWszystkieKlasy()));
        cbKlasa.setPromptText("-- Wybierz --");
        cbKlasa.setMaxWidth(Double.MAX_VALUE);


        Label lblPrzedmiot = new Label("Wybierz przedmiot:");
        ComboBox<String> cbPrzedmiot = new ComboBox<>(FXCollections.observableArrayList(DaneSystemu.wszystkiePrzedmioty));
        cbPrzedmiot.setPromptText("-- Wybierz --");
        cbPrzedmiot.setMaxWidth(Double.MAX_VALUE);


        Button btnZatwierdz = new Button("Zapisz w Planie");
        btnZatwierdz.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        btnZatwierdz.setOnAction(e -> {
            klasa k = cbKlasa.getValue();
            String p = cbPrzedmiot.getValue();
            teacher n = DaneSystemu.znajdzNauczycielaPoId(user);


            if (k != null && p != null) {

                k.getPlanZajec().put(p, n.getId());
                n.dodajKlase(k);

                try {
                    dbT.saveAll(DaneSystemu.getWszyscyNauczyciele());
                    dbK.saveAll(DaneSystemu.getWszystkieKlasy());
                    popup.close();
                    new Alert(Alert.AlertType.INFORMATION, "Pomyślnie przypisano!").show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Wypełnij wszystkie pola!").show();
            }
        });

        layout.getChildren().addAll(lblKlasa, cbKlasa, lblPrzedmiot, cbPrzedmiot,btnZatwierdz);

        Scene scene = new Scene(layout, 350, 400);
        popup.setScene(scene);
        popup.showAndWait();
    }
    public void oknoPrzypisaniaUcznia(konto user){
        Stage popup = new Stage();
        student student = DaneSystemu.znajdzStudentaPoId(user);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Konfiguracja Planu Zajęć");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);


        Label lblKlasa = new Label("Wybierz klasę:");
        ComboBox<klasa> cbKlasa = new ComboBox<>(FXCollections.observableArrayList(DaneSystemu.getWszystkieKlasy()));
        cbKlasa.setPromptText("-- Wybierz --");
        cbKlasa.setMaxWidth(Double.MAX_VALUE);
        Button btnZatwierdz = new Button("Zapisz");
        btnZatwierdz.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        btnZatwierdz.setOnAction(e->{
            cbKlasa.getValue().dodajUcznia(student);
            student.setKlasa(cbKlasa.getValue());
            try {

                dbK.saveAll(DaneSystemu.getWszystkieKlasy());
                dbS.saveAll(DaneSystemu.getWszyscyStudenci());
                popup.close();
                new Alert(Alert.AlertType.INFORMATION, "Pomyślnie przypisano!").show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        layout.getChildren().addAll(lblKlasa, cbKlasa,btnZatwierdz);

        Scene scene = new Scene(layout, 350, 400);
        popup.setScene(scene);
        popup.showAndWait();


    }


}
