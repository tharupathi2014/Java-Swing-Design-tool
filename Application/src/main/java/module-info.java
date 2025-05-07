module org.example.project1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens org.example.project1 to javafx.fxml;
    exports org.example.project1;
}