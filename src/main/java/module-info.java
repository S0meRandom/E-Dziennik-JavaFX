module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jbcrypt;

    exports org.src;
    exports org.src.UI;
    exports org.src.DTO;
    exports org.src.model;
    opens org.src.model to com.google.gson;
    opens org.src to javafx.fxml;
    exports org.src.runner;
    opens org.src.runner to com.google.gson;
}