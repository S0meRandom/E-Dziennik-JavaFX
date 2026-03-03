package org.src.UI;

import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.src.DTO.DaneSystemu;
import org.src.Konta.JsonDataBase;
import org.src.logic.adminWindowLogic;
import org.src.model.teacher;
import org.src.model.klasa;
import org.src.model.konto;
import org.src.model.student;
import org.src.uprawnienia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class adminWindow {
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
    adminWindowLogic adminWindowLogic = new adminWindowLogic();



    private Stage stage;
    public adminWindow(Stage stage) throws IOException {
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
    public void show() {

        TableColumn<konto,String> kolumnaLogin = new TableColumn<>("Nazwa użytkownika");
        kolumnaLogin.setCellValueFactory(new PropertyValueFactory<>("Login"));
        TableColumn<konto, String> kolumnaRola = new TableColumn<>("Rola w systemie");
        kolumnaRola.setCellValueFactory(new PropertyValueFactory<>("rola"));
        tabela.getColumns().add(kolumnaLogin);
        tabela.getColumns().add(kolumnaRola);
        ObservableList<konto> dane = FXCollections.observableArrayList(listaUser);
        tabela.setItems(dane);

        tabela.setOnMouseClicked(e->{
            if (e.getClickCount()==2) {
                konto wybrane = tabela.getSelectionModel().getSelectedItem();
                if(wybrane!=null){
                    System.out.println("Wybrales uzytkownika: "+wybrane.getLogin());
                    showUserWindow(wybrane);

                }
            }
        });
        Button stworzKlaseBtn = new Button("Stwórz klase");
        stworzKlaseBtn.setOnAction(e -> {
            TextInputDialog okno = new TextInputDialog();
            okno.setTitle("Nowa klasa");
            okno.setHeaderText("Tworzenie nowej klasy w systemie");
            okno.setContentText("Podaj nazwę klasy :");

            okno.showAndWait().ifPresent(nazwa -> {
                try {
                    adminWindowLogic.stworzKlase(nazwa);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });
        Button btnNowyPrzedmiot = new Button("Stwórz nowy przedmiot");
        btnNowyPrzedmiot.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nowy przedmiot");
            dialog.setHeaderText("Dodaj przedmiot do bazy szkolnej");
            dialog.setContentText("Nazwa przedmiotu:");

            dialog.showAndWait().ifPresent(nazwa -> {
                if(!DaneSystemu.wszystkiePrzedmioty.contains(nazwa)) {
                    listaPrzedmiotow.add(nazwa);
                    try {
                        dbP.saveAll(listaPrzedmiotow);
                        new Alert(Alert.AlertType.INFORMATION, "Dodano: " + nazwa).show();
                    } catch (IOException ex) { ex.printStackTrace(); }
                }
            });
        });


        VBox layout = new VBox(tabela,stworzKlaseBtn,btnNowyPrzedmiot);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.show();
    }
    public void showUserWindow(konto user){
        Stage uStage = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);


        Button changePas = new Button("Zmień hasło.");
        PasswordField passField = new PasswordField();
        changePas.setOnAction(e->{
            adminWindowLogic.changeUserPassword(user, passField.getText());
            try {
                db.saveAll(listaUser);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });


        ComboBox<uprawnienia> uprawnieniaBox = new ComboBox<>();
        uprawnieniaBox.setItems(FXCollections.observableArrayList(uprawnienia.values()));
        uprawnieniaBox.setValue(user.getRola());
        Button changeUprawnienie = new Button("Zapisz uprawnienie");
        changeUprawnienie.setOnAction(e->{
            user.setRola(uprawnieniaBox.getValue());
            try {
                db.saveAll(listaUser);
                tabela.refresh();

                System.out.println("Zmieniłeś uprawnienia użytkownika: "+user.getLogin());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        Button deleteBtn = new Button("Usuń użytkownika");
        deleteBtn.setOnAction(e-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usuwanie użytkownika");
            alert.setContentText("Czy napewno chcesz usunąć użytkownika: "+user.getLogin()+" ?");
            Optional<ButtonType> wybor = alert.showAndWait();

            if(wybor.get()==ButtonType.OK){
                try {
                    adminWindowLogic.deleteUser(user);
                    uStage.close();
                    tabela.refresh();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
        Button klasaBtn = new Button("Przypisz klase");
        klasaBtn.setOnAction(e->{
            if(user.getRola()==uprawnienia.NAUCZYCIEL){
                adminWindowLogic.otworzOknoPrzypisaniaNauczyciela(user);
            }else if(user.getRola()==uprawnienia.UCZEN){
                adminWindowLogic.oknoPrzypisaniaUcznia(user);

            }


        });


        layout.getChildren().addAll(
                passField,changePas,
                uprawnieniaBox,changeUprawnienie,
                klasaBtn,
                deleteBtn
        );
        Scene scene = new Scene(layout, 350, 450);
        uStage.setScene(scene);
        uStage.setTitle("Dziennik: "+user.getLogin());
        uStage.show();
    }





}
