package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import principal.Sistema;
import humanos.Emprendedor;
import principal.Bitacora;
import excepciones.ElementoNoEncontradoException;
import excepciones.DatosInvalidosException;

public class VentanaGestionEmprendedores extends VentanaBase {

    private TableView<Emprendedor> tableView;
    private ComboBox<String> cbBitacoras;

    public VentanaGestionEmprendedores(Sistema sistema) {
        super(sistema, "Gestión de Emprendedores");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Panel superior con controles
        VBox panelSuperior = new VBox(10);
        panelSuperior.setPadding(new Insets(10));

        Label lblTitulo = new Label("GESTIÓN DE EMPRENDEDORES");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox panelFiltros = new HBox(10);
        cbBitacoras = new ComboBox<>();
        cbBitacoras.getItems().add("Todos");
        sistema.getBitacoras().forEach(b -> cbBitacoras.getItems().add(b.getTipo()));
        cbBitacoras.setValue("Todos");

        Button btnFiltrar = new Button("Filtrar");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnExportar = new Button("Exportar CSV");

        panelFiltros.getChildren().addAll(
            new Label("Filtrar por bitácora:"), cbBitacoras, btnFiltrar,
            btnEditar, btnEliminar, btnExportar
        );

        panelSuperior.getChildren().addAll(lblTitulo, panelFiltros);
        root.setTop(panelSuperior);

        // Tabla central
        tableView = new TableView<>();
        configurarTabla();
        root.setCenter(tableView);

        // Eventos
        btnFiltrar.setOnAction(e -> filtrarEmprendedores());
        btnEditar.setOnAction(e -> editarEmprendedor());
        btnEliminar.setOnAction(e -> eliminarEmprendedor());
        btnExportar.setOnAction(e -> exportarCSV());

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }

    private void configurarTabla() {
        TableColumn<Emprendedor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Emprendedor, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));

        TableColumn<Emprendedor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Emprendedor, Double> colCapital = new TableColumn<>("Capital");
        colCapital.setCellValueFactory(new PropertyValueFactory<>("capital"));

        TableColumn<Emprendedor, String> colBitacora = new TableColumn<>("Bitácora");
        // Lógica para obtener la bitácora del emprendedor

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colBitacora);
        cargarTodosEmprendedores();
    }

    private void cargarTodosEmprendedores() {
        tableView.getItems().clear();
        for (Bitacora b : sistema.getBitacoras()) {
            tableView.getItems().addAll(b.getEmprendedores());
        }
    }

    private void filtrarEmprendedores() {
        String filtro = cbBitacoras.getValue();
        if ("Todos".equals(filtro)) {
            cargarTodosEmprendedores();
        } else {
            tableView.getItems().clear();
            for (Bitacora b : sistema.getBitacoras()) {
                if (b.getTipo().equals(filtro)) {
                    tableView.getItems().addAll(b.getEmprendedores());
                }
            }
        }
    }

    private void editarEmprendedor() {
        Emprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Abrir ventana de edición (similar a VentanaEmprendedor pero para editar)
            mostrarMensaje("Editar", "Funcionalidad de edición en desarrollo...");
        } else {
            mostrarError("Error", "Seleccione un emprendedor para editar.");
        }
    }

    private void eliminarEmprendedor() {
        Emprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                sistema.eliminarEmprendedor(seleccionado.getRut());
                mostrarMensaje("Éxito", "Emprendedor eliminado correctamente.");
                cargarTodosEmprendedores();
            } catch (ElementoNoEncontradoException ex) {
                mostrarError("Error", ex.getMessage());
            }
        } else {
            mostrarError("Error", "Seleccione un emprendedor para eliminar.");
        }
    }

    private void exportarCSV() {
        sistema.guardarEmprendedores("emprendedores_exportado.csv");
        mostrarMensaje("Éxito", "Datos exportados a emprendedores_exportado.csv");
    }
}