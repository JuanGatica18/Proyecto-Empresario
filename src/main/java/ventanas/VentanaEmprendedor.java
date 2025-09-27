package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import principal.Sistema;
import humanos.Emprendedor;
import excepciones.DatosInvalidosException;

public class VentanaEmprendedor extends VentanaBase {

    public VentanaEmprendedor(Sistema sistema) {
        super(sistema, "Registrar Emprendedor");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Campos del formulario
        Label lblNombre = new Label("Nombre:");
        TextField tfNombre = new TextField();
        tfNombre.setPromptText("Ingrese nombre completo");

        Label lblRut = new Label("RUT:");
        TextField tfRut = new TextField();
        tfRut.setPromptText("Ej: 12345678-9");

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("ejemplo@correo.com");

        Label lblCapital = new Label("Capital Inicial:");
        TextField tfCapital = new TextField();
        tfCapital.setPromptText("0.0");

        Label lblTipo = new Label("Tipo de Bitácora:");
        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Tecnología", "Salud", "Educación", "Alimentos", "Manufactura");
        cbTipo.setPromptText("Seleccione tipo");

        Button btnAgregar = new Button("Agregar Emprendedor");
        btnAgregar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        btnAgregar.setOnAction(e -> {
            try {
                if (validarCampos(tfNombre.getText(), tfRut.getText(), tfEmail.getText(), 
                                 tfCapital.getText(), cbTipo.getValue())) {
                    
                    String nombre = tfNombre.getText().trim();
                    String rut = tfRut.getText().trim();
                    String email = tfEmail.getText().trim();
                    double capital = Double.parseDouble(tfCapital.getText().trim());
                    String tipo = cbTipo.getValue();

                    Emprendedor eNuevo = new Emprendedor(nombre, rut, email, capital);
                    sistema.agregarEmprendedor(tipo, eNuevo);

                    mostrarMensaje("Éxito", "Emprendedor agregado correctamente a la bitácora: " + tipo);
                    limpiarCampos(tfNombre, tfRut, tfEmail, tfCapital, cbTipo);
                }
            } catch (NumberFormatException ex) {
                mostrarError("Error", "El capital debe ser un número válido.");
            } catch (DatosInvalidosException ex) {
                mostrarError("Error", ex.getMessage());
            }
        });

        // Organizar en grid
        grid.add(lblNombre, 0, 0);
        grid.add(tfNombre, 1, 0);
        grid.add(lblRut, 0, 1);
        grid.add(tfRut, 1, 1);
        grid.add(lblEmail, 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(lblCapital, 0, 3);
        grid.add(tfCapital, 1, 3);
        grid.add(lblTipo, 0, 4);
        grid.add(cbTipo, 1, 4);
        grid.add(btnAgregar, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 400, 350);
        stage.setScene(scene);
    }

    private boolean validarCampos(String nombre, String rut, String email, String capital, String tipo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("Error", "El nombre es obligatorio.");
            return false;
        }
        if (rut == null || rut.trim().isEmpty()) {
            mostrarError("Error", "El RUT es obligatorio.");
            return false;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            mostrarError("Error", "Email inválido.");
            return false;
        }
        if (capital == null || capital.trim().isEmpty()) {
            mostrarError("Error", "El capital es obligatorio.");
            return false;
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            mostrarError("Error", "Debe seleccionar un tipo de bitácora.");
            return false;
        }
        return true;
    }

    private void limpiarCampos(TextField nombre, TextField rut, TextField email, 
                              TextField capital, ComboBox<String> tipo) {
        nombre.clear();
        rut.clear();
        email.clear();
        capital.clear();
        tipo.getSelectionModel().clearSelection();
    }
}