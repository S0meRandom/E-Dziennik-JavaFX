package org.src.logic;

import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.src.DTO.DaneSystemu;
import org.src.Konta.JsonDataBase;
import org.src.model.teacher;
import org.src.model.klasa;
import org.src.model.student;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class teacherWindowLogic {
    private Stage stage;
    private JsonDataBase<student> dbStudent;
    private TableView<student> tabelaUczniow;
    private ComboBox<klasa> klasaSelect;
    private ComboBox<String> przedmiotySelect;
    private teacher aktualnyNauczyciel;
    private TableColumn<student, String> colOceny;
    private TableColumn<student, String> colSrednia;

    public teacherWindowLogic() throws IOException {
        this.stage = stage;
        Type listType = new TypeToken<List<student>>(){}.getType();
        this.dbStudent = new JsonDataBase<>("src/main/java/org.src/Konta/studenci.json", listType);

    }
    public void wybierzKlase() {
        klasa wybranaKlasa = klasaSelect.getValue();
        if (wybranaKlasa == null) return;


        List<student> uczniowie = wybranaKlasa.getUczniowie();
        tabelaUczniow.setItems(FXCollections.observableArrayList(uczniowie));


        List<String> przedmiotyNauczyciela = wybranaKlasa.getPlanZajec().entrySet().stream()
                .filter(entry -> entry.getValue() == aktualnyNauczyciel.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        przedmiotySelect.setItems(FXCollections.observableArrayList(przedmiotyNauczyciela));

        if (!przedmiotyNauczyciela.isEmpty()) {

            przedmiotySelect.getSelectionModel().selectFirst();
        } else {

            colOceny.setCellValueFactory(cellData -> new SimpleStringProperty("-"));
            colSrednia.setCellValueFactory(cellData -> new SimpleStringProperty("-"));
            tabelaUczniow.refresh();
        }
    }
    public void dodajOcene() {
        student uczen = tabelaUczniow.getSelectionModel().getSelectedItem();
        String przedmiot = przedmiotySelect.getValue();

        if (uczen == null || przedmiot == null) {
            new Alert(Alert.AlertType.WARNING, "Wybierz ucznia i przedmiot!").show();
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nowa ocena");
        dialog.setHeaderText("Dodawanie oceny z przedmiotu: " + przedmiot);
        dialog.setContentText("Wpisz ocenę (np. 5.0):");

        dialog.showAndWait().ifPresent(val -> {
            try {
                Double ocena = Double.parseDouble(val.replace(",", "."));


                uczen.getOceny()
                        .computeIfAbsent(przedmiot, k -> new ArrayList<>())
                        .add(ocena);

                dbStudent.saveAll(DaneSystemu.getWszyscyStudenci());
                tabelaUczniow.refresh();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Błędny format oceny!").show();
            }
        });
    }
    public void usunOstatniaOcene() {
        student uczen = tabelaUczniow.getSelectionModel().getSelectedItem();
        String przedmiot = przedmiotySelect.getValue();

        if (uczen != null && przedmiot != null) {
            List<Double> oceny = uczen.getOceny().get(przedmiot);
            if (oceny != null && !oceny.isEmpty()) {
                oceny.remove(oceny.size() - 1);
                try {
                    dbStudent.saveAll(DaneSystemu.getWszyscyStudenci());
                    tabelaUczniow.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void wybierzPrzedmiot() {
        String wybranyPrzedmiot = przedmiotySelect.getValue();
        if (wybranyPrzedmiot == null) return;


        colOceny.setCellValueFactory(cellData -> {
            student s = cellData.getValue();
            if (s.getOceny() == null) return new SimpleStringProperty("Brak ocen");

            List<Double> oceny = s.getOceny().get(wybranyPrzedmiot);

            if (oceny == null || oceny.isEmpty()) {
                return new SimpleStringProperty("Brak ocen");
            }
            return new SimpleStringProperty(oceny.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        });


        colSrednia.setCellValueFactory(cellData -> {
            student s = cellData.getValue();
            if (s.getOceny() == null) return new SimpleStringProperty("-");

            List<Double> oceny = s.getOceny().get(wybranyPrzedmiot);

            if (oceny == null || oceny.isEmpty()) {
                return new SimpleStringProperty("-");
            }

            double avg = oceny.stream().mapToDouble(d -> d).average().orElse(0.0);
            return new SimpleStringProperty(String.format("%.2f", avg));
        });


        tabelaUczniow.refresh();
    }

}
