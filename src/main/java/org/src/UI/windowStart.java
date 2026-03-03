package org.src.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.src.DTO.Autoryzacja;
import org.src.DTO.DaneSystemu;
import org.src.model.konto;

import java.io.IOException;
import java.util.Optional;

public class windowStart extends Application {
    Autoryzacja auth = new Autoryzacja();

    public windowStart() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("System Logowania");


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Label titleLabel = new Label("Dziennik Elektroniczny");
        titleLabel.setFont(new Font("Arial", 20));
        grid.add(titleLabel, 0, 0, 2, 1);

        Label userLabel = new Label("Użytkownik:");
        grid.add(userLabel, 0, 1);

        TextField userField = new TextField();
        grid.add(userField, 1, 1);

        Label passLabel = new Label("Hasło:");
        grid.add(passLabel, 0, 2);


        PasswordField passField = new PasswordField();
        grid.add(passField, 1, 2);

        Button logBtn = new Button("Zaloguj");
        logBtn.setOnAction(e -> {
            try {
                Optional<konto> success = auth.Login(userField.getText(), passField.getText());
                if(success.isPresent()) {
                    konto k = success.get();
                    switch(k.getRola()){
                        case UCZEN -> {
                            studentWindow sW = new studentWindow(primaryStage, DaneSystemu.znajdzStudentaPoId(k));
                            sW.show(primaryStage);
                        }
                        case ADMIN -> {
                            adminWindow aW = new adminWindow(primaryStage);
                            aW.show();
                        }
                        case NAUCZYCIEL -> {
                            teacherWindow tW = new teacherWindow(primaryStage);
                            tW.show(primaryStage,userField.getText(), k);
                        }
                    }

                } else {
                    System.out.println("Logowanie nie powiodło się.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        grid.add(logBtn, 1, 4);
        Button regBtn = new Button("Zarejestruj");
        regBtn.setOnAction(e -> {

            registerWindow rW;
            try {
                rW = new registerWindow();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            rW.show(primaryStage);

        });
        grid.add(regBtn,1,5);


        Label messageLabel = new Label();
        grid.add(messageLabel, 1, 6);
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }}
