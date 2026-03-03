package org.src.UI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.src.model.student;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class studentWindow {
    private student zalogowanyUczen;
    private Stage stage;

    public studentWindow(Stage stage ,student uczen) {
        this.zalogowanyUczen = uczen;
        this.stage = stage;
    }

    public void show(Stage stage) {

        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(15));

        Label tytul = new Label("Twoje oceny - " + zalogowanyUczen.getImie() + " " + zalogowanyUczen.getNazwisko());
        tytul.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Map.Entry<String, List<Double>>> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        TableColumn<Map.Entry<String, List<Double>>, String> kolPrzedmiot = new TableColumn<>("Przedmiot");
        kolPrzedmiot.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        kolPrzedmiot.setPrefWidth(150);


        TableColumn<Map.Entry<String, List<Double>>, String> kolOceny = new TableColumn<>("Oceny");
        kolOceny.setCellValueFactory(data -> {
            String ocenyTekst = data.getValue().getValue().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(ocenyTekst);
        });
        kolOceny.setPrefWidth(250);
        TableColumn<Map.Entry<String, List<Double>>, String> colSrednia = new TableColumn<>("Średnia");
        colSrednia.setCellValueFactory(cellData -> {
            List<Double> oceny = cellData.getValue().getValue();
            if (oceny == null || oceny.isEmpty()) return new SimpleStringProperty("0.00");

            // Obliczanie średniej ze strumienia
            double avg = oceny.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            return new SimpleStringProperty(String.format("%.2f", avg));
        });


        tabela.getColumns().addAll(kolPrzedmiot, kolOceny,colSrednia);


        ObservableList<Map.Entry<String, List<Double>>> dane =
                FXCollections.observableArrayList(zalogowanyUczen.getOceny().entrySet());
        tabela.setItems(dane);

        layout.getChildren().addAll(tytul, tabela);
        stage.setScene(new Scene(layout, 450, 400));
        stage.setTitle("Panel Ucznia");
        stage.show();
    }
}
