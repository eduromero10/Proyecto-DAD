module com.example.proyectodad {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proyectodad to javafx.fxml;
    exports com.example.proyectodad;
}