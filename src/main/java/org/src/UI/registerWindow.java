package org.src.UI;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.src.DTO.Autoryzacja;
import org.src.model.konto;
import org.src.uprawnienia;

import java.io.IOException;
import java.util.ArrayList;

public class registerWindow {
    private TextField loginField;
    private PasswordField passwordField;
    private PasswordField repeatPasswordField;
    private Label messageLabel;
    Autoryzacja auth = new Autoryzacja();

    public registerWindow() throws IOException {
    }

    public void show(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setPrefWidth(350);


        Label headerLabel = new Label("Utwórz nowe konto");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField imieField = new TextField();
        imieField.setPromptText("Imie");

        TextField nazwiskoField = new TextField();
        nazwiskoField.setPromptText("Nazwisko");


        loginField = new TextField();
        loginField.setPromptText("Login");

        passwordField = new PasswordField();
        passwordField.setPromptText("Hasło");

        repeatPasswordField = new PasswordField();
        repeatPasswordField.setPromptText("Powtórz hasło");


        ComboBox<uprawnienia> roleComboBox = new ComboBox<>();

        roleComboBox.setItems(FXCollections.observableArrayList(uprawnienia.values()));
        roleComboBox.setPromptText("Wybierz rolę...");
        roleComboBox.setMaxWidth(Double.MAX_VALUE);


        Button registerBtn = new Button("Zarejestruj się");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        registerBtn.setOnAction(e->{
           switch(roleComboBox.getValue()){
               case UCZEN-> {
                   try {
                       auth.registerStudent(loginField.getText(),passwordField.getText(),imieField.getText(),nazwiskoField.getText());
                       System.out.println("Pomyślnie utworzono konto ucznia");
                   } catch (Exception ex) {
                       throw new RuntimeException(ex);
                   }
               }
               case NAUCZYCIEL -> {
                   try {
                       auth.registerTeacher(loginField.getText(),passwordField.getText(),imieField.getText(),nazwiskoField.getText());
                       System.out.println("Pomyślnie utworzono konta nauczyciela");
                   } catch (Exception ex) {
                       throw new RuntimeException(ex);
                   }

               }
               case ADMIN -> {
                   try {
                       auth.registerAdmin(loginField.getText(),passwordField.getText(),imieField.getText(),nazwiskoField.getText());
                       System.out.println("Pomyślnie utworzono konto admina");
                   } catch (Exception ex) {
                       throw new RuntimeException(ex);
                   }

               }
           }



        });
        Button backBtn = new Button("Cofnij");
        backBtn.setOnAction(e->{
            try {
                windowStart wS = new windowStart();
                wS.start(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        });


        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: red;");


        layout.getChildren().addAll(
                headerLabel,
                new Separator(),
                new Label("Dane logowania:"),
                imieField,
                nazwiskoField,
                loginField,
                passwordField,
                repeatPasswordField,
                new Label("Typ konta:"),
                roleComboBox,
                registerBtn,
                backBtn,
                messageLabel

        );

        Scene scene = new Scene(layout);
        stage.setTitle("Rejestracja - System Szkolny");
        stage.setScene(scene);
        stage.show();


    }
}
