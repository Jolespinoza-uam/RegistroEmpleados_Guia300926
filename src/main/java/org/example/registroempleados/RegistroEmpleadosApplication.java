package org.example.registroempleados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistroEmpleadosApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                RegistroEmpleadosApplication.class.getResource("views/views/empleado-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 720);
        stage.setTitle("Registro de empleados");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
