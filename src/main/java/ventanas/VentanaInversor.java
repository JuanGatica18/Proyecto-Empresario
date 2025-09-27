package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import principal.Sistema;
import humanos.Inversor;
import excepciones.DatosInvalidosException;

public class VentanaInversor extends VentanaBase {

    public VentanaInversor(Sistema sistema) {
        super(sistema, "Registrar Inversor");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label lblNombre = new Label("Nombre:");
        TextField tfNombre = new TextField();

        Label lblRut = new Label("RUT:");
        TextField tfRut = new TextField();

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField();

        Label lblCapital = new Label("Capital Disponible:");
        TextField tfCapital = new TextField();

        Button btnAgregar = new Button("Agregar Inversor");
        btnAgregar.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");

        btnAgregar.setOnAction(e -> {
            try {
                String nombre = tfNombre.getText().trim();
                String rut = tfRut.getText().trim();
                String email = tfEmail.getText().trim();
                double capital = Double.parseDouble(tfCapital.getText().trim());

                if (validarCampos(nombre, rut, email, capital)) {
                    Inversor iNuevo = new Inversor(nombre, rut, email, capital);
                    sistema.agregarInversor(iNuevo);
                    
                    mostrarMensaje("Éxito", "Inversor agregado correctamente.");
                    limpiarCampos(tfNombre, tfRut, tfEmail, tfCapital);
                }
            } catch (NumberFormatException ex) {
                mostrarError("Error", "El capital debe ser un número válido.");
            } catch (DatosInvalidosException ex) {
                mostrarError("Error", ex.getMessage());
            }
        });

        grid.add(lblNombre, 0, 0);
        grid.add(tfNombre, 1, 0);
        grid.add(lblRut, 0, 1);
        grid.add(tfRut, 1, 1);
        grid.add(lblEmail, 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(lblCapital, 0, 3);
        grid.add(tfCapital, 1, 3);
        grid.add(btnAgregar, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
    }

    private boolean validarCampos(String nombre, String rut, String email, double capital) {
        // Implementar validación
        return true;
    }

    private void limpiarCampos(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }
}