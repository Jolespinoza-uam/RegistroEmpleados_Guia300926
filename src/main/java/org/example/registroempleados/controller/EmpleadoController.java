package org.example.registroempleados.controller;

import org.example.registroempleados.database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.registroempleados.model.Empleado;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class EmpleadoController {
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCargo;
    @FXML
    private TextField txtSalario;
    @FXML
    private ComboBox<String> cmbDepartamento, cmbEstado;
    @FXML
    private DatePicker dpFechaContratacion;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnActualizar;

    @FXML
    private TableView<Empleado> tblEmpleados;
    @FXML
    private TableColumn<Empleado, Integer> colId;
    @FXML
    private TableColumn<Empleado, String> colNombres;
    @FXML
    private TableColumn<Empleado, String> colApellidos;
    @FXML
    private TableColumn<Empleado, String> colCedula;
    @FXML
    private TableColumn<Empleado, String> colCorreo;
    @FXML
    private TableColumn<Empleado, String> colTelefono;
    @FXML
    private TableColumn<Empleado, String> colCargo;
    @FXML
    private TableColumn<Empleado, String> colDepartamento;
    @FXML
    private TableColumn<Empleado, String> colEstado;

    @FXML
    private TableColumn<Empleado, BigDecimal> colSalario;
    @FXML
    private TableColumn<Empleado, LocalDate> colFechaContratacion;

    private final ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblEmpleados.setItems(listaEmpleados);

        cmbDepartamento.setItems(FXCollections.observableArrayList(
                "Sistemas", "Finanzas", "Recursos Humanos", "Ventas", "Operaciones"));
        cmbEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));

        cargarEmpleados();
    }

    private void cargarEmpleados() {
        String sql = "SELECT * FROM empleado";
        // String sql = "SELECT * FROM empleado WHERE estado = 'Activo'";
        // String sql = "SELECT * FROM empleado WHERE departamento = 'Sistemas'";
        // String sql = "SELECT * FROM empleado WHERE salario > 1000";
        // String sql = "SELECT * FROM empleado ORDER BY salario DESC";
        // String sql = "SELECT * FROM empleado ORDER BY apellidos ASC";

        listaEmpleados.clear();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Empleado e = new Empleado();
                e.setId(rs.getInt("id"));
                e.setNombres(rs.getString("nombres"));
                e.setApellidos(rs.getString("apellidos"));
                e.setCedula(rs.getString("cedula"));
                e.setCorreo(rs.getString("correo"));
                e.setTelefono(rs.getString("telefono"));
                e.setCargo(rs.getString("cargo"));
                e.setDepartamento(rs.getString("departamento"));
                e.setSalario(rs.getBigDecimal("salario"));
                e.setFechaContratacion(rs.getDate("fecha_contratacion").toLocalDate());
                e.setEstado(rs.getString("estado"));
                listaEmpleados.add(e);
            }
        } catch (SQLException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos",
                    "No se pudo consultar los empleados:\n" + ex.getMessage());
        }
    }

    @FXML
    private void guardarEmpleado() {
        if (!validarFormulario()) return;

        String sql = "INSERT INTO empleado (nombres, apellidos, cedula, correo, telefono, cargo, "
                + "departamento, salario, fecha_contratacion, estado) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombres.getText().trim());
            ps.setString(2, txtApellidos.getText().trim());
            ps.setString(3, txtCedula.getText().trim());
            ps.setString(4, vacioANull(txtCorreo.getText()));
            ps.setString(5, vacioANull(txtTelefono.getText()));
            ps.setString(6, vacioANull(txtCargo.getText()));
            ps.setString(7, cmbDepartamento.getValue());
            ps.setBigDecimal(8, new BigDecimal(txtSalario.getText().trim()));
            ps.setDate(9, Date.valueOf(dpFechaContratacion.getValue()));
            ps.setString(10, cmbEstado.getValue());
            ps.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado registrado correctamente.");
            limpiarFormulario();
            cargarEmpleados();

        } catch (SQLException ex) {
            String msg = "23505".equals(ex.getSQLState())
                    ? "Ya existe un empleado con esa cédula o correo."
                    : "No se pudo guardar el empleado:\n" + ex.getMessage();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos", msg);
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        txtCedula.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtCargo.clear();
        txtSalario.clear();
        cmbDepartamento.setValue(null);
        cmbEstado.setValue(null);
        dpFechaContratacion.setValue(null);
    }

    @FXML
    private void actualizarTabla() {
        cargarEmpleados();
    }

    private boolean validarFormulario() {
        if (txtNombres.getText().isBlank())   return error("El campo Nombres es obligatorio.");
        if (txtApellidos.getText().isBlank()) return error("El campo Apellidos es obligatorio.");
        if (txtCedula.getText().isBlank())    return error("El campo Cédula es obligatorio.");
        if (cmbDepartamento.getValue() == null) return error("Seleccione un departamento.");
        try {
            BigDecimal s = new BigDecimal(txtSalario.getText().trim());
            if (s.signum() < 0) return error("El salario no puede ser negativo.");
        } catch (NumberFormatException e) {
            return error("El salario debe ser un valor numérico válido (ej. 1200.50).");
        }
        if (dpFechaContratacion.getValue() == null) return error("Seleccione la fecha de contratación.");
        if (cmbEstado.getValue() == null) return error("Seleccione un estado.");
        return true;
    }

    private boolean error(String mensaje) {
        mostrarAlerta(Alert.AlertType.WARNING, "Validación", mensaje);
        return false;
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
