package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import principal.Sistema;
import principal.Proyecto;
import humanos.Emprendedor;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentanaProyectos extends VentanaBase {

    private TableView<ProyectoConEmprendedor> tableView;
    private TextArea taDetalles;
    private ComboBox<String> cbFiltroTipo;
    private ComboBox<String> cbFiltroEstado;

    public VentanaProyectos(Sistema sistema) {
        super(sistema, "Gestión de Proyectos");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Panel superior con filtros
        VBox panelSuperior = crearPanelFiltros();
        root.setTop(panelSuperior);

        // Tabla central
        tableView = new TableView<>();
        configurarTabla();
        
        // Panel derecho con detalles
        taDetalles = new TextArea();
        taDetalles.setEditable(false);
        taDetalles.setPrefWidth(350);
        taDetalles.setPromptText("Seleccione un proyecto para ver los detalles");
        taDetalles.setStyle("-fx-font-family: monospace;");

        // Layout horizontal: tabla + detalles
        HBox panelCentral = new HBox(10);
        panelCentral.getChildren().addAll(tableView, taDetalles);
        
        root.setCenter(panelCentral);

        // Panel inferior con botones
        HBox panelInferior = crearPanelBotones();
        root.setBottom(panelInferior);

        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);

        // Cargar datos iniciales
        cargarTodosProyectos();
    }

    private VBox crearPanelFiltros() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-border-radius: 5;");

        Label lblTitulo = new Label("GESTIÓN Y VISUALIZACIÓN DE PROYECTOS");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Filtros
        GridPane gridFiltros = new GridPane();
        gridFiltros.setHgap(15);
        gridFiltros.setVgap(8);

        Label lblFiltroTipo = new Label("Filtrar por Tipo:");
        cbFiltroTipo = new ComboBox<>();
        cbFiltroTipo.getItems().addAll("Todos", "Tecnologia", "Salud", "Educacion", "Alimentos");
        cbFiltroTipo.setValue("Todos");

        Label lblFiltroEstado = new Label("Filtrar por Estado:");
        cbFiltroEstado = new ComboBox<>();
        cbFiltroEstado.getItems().addAll("Todos", "Planificacion", "En desarrollo", "Beta", "Activo", "Finalizado");
        cbFiltroEstado.setValue("Todos");

        Button btnFiltrar = new Button("Aplicar Filtros");
        btnFiltrar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Button btnMostrarTodos = new Button("Mostrar Todos");
        btnMostrarTodos.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        gridFiltros.add(lblFiltroTipo, 0, 0);
        gridFiltros.add(cbFiltroTipo, 1, 0);
        gridFiltros.add(lblFiltroEstado, 2, 0);
        gridFiltros.add(cbFiltroEstado, 3, 0);
        gridFiltros.add(btnFiltrar, 4, 0);
        gridFiltros.add(btnMostrarTodos, 5, 0);

        // Eventos de filtros
        btnFiltrar.setOnAction(e -> aplicarFiltros());
        btnMostrarTodos.setOnAction(e -> {
            cbFiltroTipo.setValue("Todos");
            cbFiltroEstado.setValue("Todos");
            cargarTodosProyectos();
        });

        panel.getChildren().addAll(lblTitulo, new Separator(), gridFiltros);
        return panel;
    }

    private HBox crearPanelBotones() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));

        Button btnAgregarProyecto = new Button("Agregar Proyecto");
        Button btnEditarProyecto = new Button("Editar Proyecto");
        Button btnEliminarProyecto = new Button("Eliminar Proyecto");
        Button btnVerDetalles = new Button("Ver Detalles Completos");

        btnAgregarProyecto.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnEditarProyecto.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        btnEliminarProyecto.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnVerDetalles.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;");

        btnAgregarProyecto.setOnAction(e -> mostrarVentanaAgregarProyecto());
        btnEditarProyecto.setOnAction(e -> editarProyectoSeleccionado());
        btnEliminarProyecto.setOnAction(e -> eliminarProyectoSeleccionado());
        btnVerDetalles.setOnAction(e -> mostrarDetallesCompletos());

        panel.getChildren().addAll(btnAgregarProyecto, btnEditarProyecto, btnEliminarProyecto, 
                                  new Separator(), btnVerDetalles);
        return panel;
    }

    private void configurarTabla() {
        TableColumn<ProyectoConEmprendedor, String> colNombre = new TableColumn<>("Nombre Proyecto");
        colNombre.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProyecto().getNombre()));
        colNombre.setPrefWidth(160);

        TableColumn<ProyectoConEmprendedor, String> colEmprendedor = new TableColumn<>("Emprendedor");
        colEmprendedor.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getNombre()));
        colEmprendedor.setPrefWidth(120);

        TableColumn<ProyectoConEmprendedor, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProyecto().getTipo()));
        colTipo.setPrefWidth(80);

        TableColumn<ProyectoConEmprendedor, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProyecto().getEstado()));
        colEstado.setPrefWidth(100);

        TableColumn<ProyectoConEmprendedor, String> colInversion = new TableColumn<>("Inversión");
        colInversion.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("$" + 
                String.format("%,.2f", cellData.getValue().getProyecto().getInversion())));
        colInversion.setPrefWidth(100);

        TableColumn<ProyectoConEmprendedor, String> colAvance = new TableColumn<>("Avance");
        colAvance.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.format("%.1f%%", cellData.getValue().getProyecto().getPorcentajeAvance())));
        colAvance.setPrefWidth(70);

        TableColumn<ProyectoConEmprendedor, String> colRiesgo = new TableColumn<>("Riesgo");
        colRiesgo.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProyecto().calcularRiesgo()));
        colRiesgo.setPrefWidth(60);

        tableView.getColumns().addAll(colNombre, colEmprendedor, colTipo, colEstado, colInversion, colAvance, colRiesgo);

        // Evento de selección para mostrar detalles
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetallesProyecto(newSelection.getProyecto());
            }
        });
    }

    private void cargarTodosProyectos() {
        List<ProyectoConEmprendedor> proyectosConEmprendedor = new ArrayList<>();
        
        for (Emprendedor emp : sistema.getMapaEmprendedores().values()) {
            for (Proyecto proyecto : emp.getProyectos()) {
                proyectosConEmprendedor.add(new ProyectoConEmprendedor(proyecto, emp));
            }
        }
        
        tableView.getItems().clear();
        tableView.getItems().addAll(proyectosConEmprendedor);
        
        taDetalles.setText("Total de proyectos en el sistema: " + proyectosConEmprendedor.size());
    }

    private void aplicarFiltros() {
        String filtroTipo = cbFiltroTipo.getValue();
        String filtroEstado = cbFiltroEstado.getValue();
        
        List<ProyectoConEmprendedor> proyectosFiltrados = new ArrayList<>();
        
        for (Emprendedor emp : sistema.getMapaEmprendedores().values()) {
            for (Proyecto proyecto : emp.getProyectos()) {
                boolean cumpleTipo = "Todos".equals(filtroTipo) || proyecto.getTipo().equalsIgnoreCase(filtroTipo);
                boolean cumpleEstado = "Todos".equals(filtroEstado) || proyecto.getEstado().equalsIgnoreCase(filtroEstado);
                
                if (cumpleTipo && cumpleEstado) {
                    proyectosFiltrados.add(new ProyectoConEmprendedor(proyecto, emp));
                }
            }
        }
        
        tableView.getItems().clear();
        tableView.getItems().addAll(proyectosFiltrados);
        
        taDetalles.setText(String.format("Proyectos filtrados: %d\nTipo: %s | Estado: %s", 
                                        proyectosFiltrados.size(), filtroTipo, filtroEstado));
    }

    private void mostrarDetallesProyecto(Proyecto proyecto) {
        taDetalles.setText(proyecto.mostrarInfoCompleta());
    }

    private void mostrarVentanaAgregarProyecto() {
        // Crear ventana simple para agregar proyecto
        Stage ventana = new Stage();
        ventana.setTitle("Agregar Nuevo Proyecto");
        ventana.initOwner(stage);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Campos del formulario
        TextField tfNombre = new TextField();
        TextArea taDescripcion = new TextArea();
        taDescripcion.setPrefRowCount(3);
        TextField tfInversion = new TextField();
        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("Planificacion", "En desarrollo", "Beta", "Activo");
        cbEstado.setValue("Planificacion");
        
        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Tecnologia", "Salud", "Educacion", "Alimentos");
        
        ComboBox<Emprendedor> cbEmprendedor = new ComboBox<>();
        sistema.getMapaEmprendedores().values().forEach(cbEmprendedor.getItems()::add);

        Button btnGuardar = new Button("Guardar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> {
            try {
                String nombre = tfNombre.getText().trim();
                String descripcion = taDescripcion.getText().trim();
                double inversion = Double.parseDouble(tfInversion.getText().trim());
                String estado = cbEstado.getValue();
                String tipo = cbTipo.getValue();
                Emprendedor emprendedor = cbEmprendedor.getValue();

                if (nombre.isEmpty() || descripcion.isEmpty() || tipo == null || emprendedor == null) {
                    mostrarError("Error", "Complete todos los campos obligatorios");
                    return;
                }

                Proyecto nuevoProyecto = new Proyecto(nombre, descripcion, inversion, estado, tipo);
                emprendedor.registrarProyecto(nuevoProyecto);

                mostrarMensaje("Éxito", "Proyecto agregado correctamente");
                ventana.close();
                cargarTodosProyectos();

            } catch (NumberFormatException ex) {
                mostrarError("Error", "La inversión debe ser un número válido");
            }
        });

        btnCancelar.setOnAction(e -> ventana.close());

        // Layout
        grid.add(new Label("Emprendedor:"), 0, 0);
        grid.add(cbEmprendedor, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(tfNombre, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2);
        grid.add(taDescripcion, 1, 2);
        grid.add(new Label("Tipo:"), 0, 3);
        grid.add(cbTipo, 1, 3);
        grid.add(new Label("Estado:"), 0, 4);
        grid.add(cbEstado, 1, 4);
        grid.add(new Label("Inversión:"), 0, 5);
        grid.add(tfInversion, 1, 5);

        HBox botones = new HBox(10, btnGuardar, btnCancelar);
        grid.add(botones, 0, 6, 2, 1);

        ventana.setScene(new Scene(grid, 400, 350));
        ventana.showAndWait();
    }

    private void editarProyectoSeleccionado() {
        ProyectoConEmprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Error", "Seleccione un proyecto para editar");
            return;
        }

        // Similar a agregar pero con datos pre-llenados
        mostrarMensaje("Info", "Función de edición en desarrollo");
    }

    private void eliminarProyectoSeleccionado() {
        ProyectoConEmprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Error", "Seleccione un proyecto para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Eliminar proyecto?");
        confirmacion.setContentText("Esta acción no se puede deshacer");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            seleccionado.getEmprendedor().getProyectos().remove(seleccionado.getProyecto());
            mostrarMensaje("Éxito", "Proyecto eliminado correctamente");
            cargarTodosProyectos();
        }
    }

    private void mostrarDetallesCompletos() {
        ProyectoConEmprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Error", "Seleccione un proyecto para ver detalles");
            return;
        }

        Alert detalles = new Alert(Alert.AlertType.INFORMATION);
        detalles.setTitle("Detalles Completos del Proyecto");
        detalles.setHeaderText("Información Detallada");
        detalles.setContentText(seleccionado.getProyecto().mostrarInfoCompleta() + 
                               "\n\nEmprendedor: " + seleccionado.getEmprendedor().mostrarInfo());
        detalles.showAndWait();
    }

    // Clase auxiliar para relacionar proyectos con emprendedores
    public static class ProyectoConEmprendedor {
        private final Proyecto proyecto;
        private final Emprendedor emprendedor;

        public ProyectoConEmprendedor(Proyecto proyecto, Emprendedor emprendedor) {
            this.proyecto = proyecto;
            this.emprendedor = emprendedor;
        }

        public Proyecto getProyecto() { return proyecto; }
        public Emprendedor getEmprendedor() { return emprendedor; }
    }
}
