package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import principal.Sistema;
import humanos.Mentor;
import excepciones.DatosInvalidosException;

public class VentanaMentor extends VentanaBase {

    public VentanaMentor(Sistema sistema) {
        super(sistema, "Registrar Mentor");
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

        Label lblEspecialidad = new Label("Especialidad:");
        ComboBox<String> cbEspecialidad = new ComboBox<>();
        cbEspecialidad.getItems().addAll("Tecnología", "Finanzas", "Marketing", "Operaciones", "Legal");

        Label lblExperiencia = new Label("Años de Experiencia:");
        Spinner<Integer> spExperiencia = new Spinner<>(0, 50, 0);

        Button btnAgregar = new Button("Agregar Mentor");
        btnAgregar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        btnAgregar.setOnAction(e -> {
            try {
                String nombre = tfNombre.getText().trim();
                String rut = tfRut.getText().trim();
                String email = tfEmail.getText().trim();
                String especialidad = cbEspecialidad.getValue();
                String experiencia = String.valueOf(spExperiencia.getValue());

                if (validarCampos(nombre, rut, email, especialidad)) {
                    Mentor mNuevo = new Mentor(nombre, rut, email, especialidad, experiencia);
                    sistema.agregarMentor(mNuevo);
                    
                    mostrarMensaje("Éxito", "Mentor agregado correctamente.");
                    limpiarCampos(tfNombre, tfRut, tfEmail, cbEspecialidad, spExperiencia);
                }
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
        grid.add(lblEspecialidad, 0, 3);
        grid.add(cbEspecialidad, 1, 3);
        grid.add(lblExperiencia, 0, 4);
        grid.add(spExperiencia, 1, 4);
        grid.add(btnAgregar, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
    }

    private boolean validarCampos(String nombre, String rut, String email, String especialidad) {
        // Implementar validación similar a VentanaEmprendedor
        return true;
    }

    private void limpiarCampos(TextField nombre, TextField rut, TextField email, 
                              ComboBox<String> especialidad, Spinner<Integer> experiencia) {
        nombre.clear();
        rut.clear();
        email.clear();
        especialidad.getSelectionModel().clearSelection();
        experiencia.getValueFactory().setValue(0);
    }
}