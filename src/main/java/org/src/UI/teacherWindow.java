package org.src.UI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.google.gson.reflect.TypeToken;
import org.src.DTO.DaneSystemu;
import org.src.Konta.JsonDataBase;
import org.src.logic.teacherWindowLogic;
import org.src.model.teacher;
import org.src.model.klasa;
import org.src.model.konto;
import org.src.model.student;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class teacherWindow {
    private Stage stage;
    private JsonDataBase<student> dbStudent;
    private TableView<student> tabelaUczniow;
    private ComboBox<klasa> klasaSelect;
    private ComboBox<String> przedmiotySelect;
    private teacher aktualnyNauczyciel;
    private teacherWindowLogic teacherWindowLogic = new teacherWindowLogic();



    private TableColumn<student, String> colOceny;
    private TableColumn<student, String> colSrednia;

    public teacherWindow(Stage stage) throws IOException {
        this.stage = stage;
        Type listType = new TypeToken<List<student>>(){}.getType();
        this.dbStudent = new JsonDataBase<>("src/main/java/org.src/Konta/studenci.json", listType);
    }

    public void show(Stage primaryStage, String imieNauczyciela, konto user) {

        this.aktualnyNauczyciel = DaneSystemu.znajdzNauczycielaPoId(user);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));


        HBox header = new HBox(20);
        header.setPadding(new Insets(0, 0, 15, 0));
        Label title = new Label("Panel Nauczyciela: " + imieNauczyciela);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button logoutBtn = new Button("Wyloguj");
        logoutBtn.setOnAction(e->{
            primaryStage.close();

        });
        header.getChildren().addAll(title, spacer, logoutBtn);
        root.setTop(header);


        VBox sidebar = new VBox(10);
        sidebar.setMinWidth(180);
        sidebar.setPadding(new Insets(0, 15, 0, 0));


        List<klasa> klasyNauczyciela = DaneSystemu.getWszystkieKlasy().stream()
                .filter(k -> k.getPlanZajec().containsValue(aktualnyNauczyciel.getId()))
                .collect(Collectors.toList());

        this.klasaSelect = new ComboBox<>(FXCollections.observableArrayList(klasyNauczyciela));
        klasaSelect.setPromptText("Wybierz klasę");
        klasaSelect.setMaxWidth(Double.MAX_VALUE);
        klasaSelect.setOnAction(e -> teacherWindowLogic.wybierzKlase());

        this.przedmiotySelect = new ComboBox<>();
        przedmiotySelect.setPromptText("Wybierz przedmiot");
        przedmiotySelect.setMaxWidth(Double.MAX_VALUE);
        przedmiotySelect.setOnAction(e -> teacherWindowLogic.wybierzPrzedmiot());

        sidebar.getChildren().addAll(new Label("Twoje Klasy:"), klasaSelect, new Label("Przedmiot:"), przedmiotySelect);
        root.setLeft(sidebar);


        tabelaUczniow = new TableView<>();

        TableColumn<student, String> colImieNazwisko = new TableColumn<>("Uczeń");
        colImieNazwisko.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNazwisko() + " " + cellData.getValue().getImie()));

        colOceny = new TableColumn<>("Oceny");
        colOceny.setPrefWidth(250);

        colSrednia = new TableColumn<>("Średnia");
        colSrednia.setPrefWidth(80);

        tabelaUczniow.getColumns().addAll(colImieNazwisko, colOceny, colSrednia);
        root.setCenter(tabelaUczniow);


        HBox actions = new HBox(10);
        actions.setPadding(new Insets(15, 0, 0, 0));

        Button btnDodaj = new Button("Dodaj ocenę");
        btnDodaj.setOnAction(e -> teacherWindowLogic.dodajOcene());

        Button btnUsun = new Button("Usuń ostatnią");
        btnUsun.setOnAction(e -> teacherWindowLogic.usunOstatniaOcene());

        actions.getChildren().addAll(btnDodaj, btnUsun);
        root.setBottom(actions);

        Scene scene = new Scene(root, 900, 550);
        primaryStage.setTitle("Dziennik - Panel Nauczyciela");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}




