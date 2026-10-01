module org.example.registroempleados {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.example.registroempleados to javafx.fxml;
    opens org.example.registroempleados.controller to javafx.fxml;
    opens org.example.registroempleados.model to javafx.base;
    exports org.example.registroempleados;
}
