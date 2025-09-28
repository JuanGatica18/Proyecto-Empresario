package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import principal.Sistema;
import humanos.Emprendedor;
import principal.Bitacora;
import excepciones.ElementoNoEncontradoException;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VentanaGestionEmprendedores extends VentanaBase {

    private TableView<Emprendedor> tableView;
    private ComboBox<String> cbBitacoras;

    public VentanaGestionEmprendedores(Sistema sistema) {
        super(sistema, "Gestion de Emprendedores");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Panel superior con controles
        VBox panelSuperior = new VBox(10);
        panelSuperior.setPadding(new Insets(10));

        Label lblTitulo = new Label("GESTION DE EMPRENDEDORES");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox panelFiltros = new HBox(10);
        cbBitacoras = new ComboBox<>();
        cbBitacoras.getItems().add("Todos");
        sistema.getBitacoras().forEach(b -> cbBitacoras.getItems().add(b.getTipo()));
        cbBitacoras.setValue("Todos");

        Button btnFiltrar = new Button("Filtrar");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");
        
        btnFiltrar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnEditar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnRefrescar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        panelFiltros.getChildren().addAll(
            new Label("Filtrar por bitacora:"), cbBitacoras, btnFiltrar,
            btnEditar, btnEliminar, btnRefrescar
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
        btnRefrescar.setOnAction(e -> cargarTodosEmprendedores()); // ✅ NUEVO EVENTO

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }

    private void configurarTabla() {
        TableColumn<Emprendedor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(150);

        TableColumn<Emprendedor, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));
        colRut.setPrefWidth(100);

        TableColumn<Emprendedor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(180);

        TableColumn<Emprendedor, String> colCapital = new TableColumn<>("Capital");
        colCapital.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("$" + 
                String.format("%.2f", cellData.getValue().getCapital())));
        colCapital.setPrefWidth(120);

        TableColumn<Emprendedor, String> colBitacora = new TableColumn<>("Bitacora");
        colBitacora.setCellValueFactory(cellData -> {
            String bitacora = buscarBitacoraDelEmprendedor(cellData.getValue());
            return new javafx.beans.property.SimpleStringProperty(bitacora);
        });
        colBitacora.setPrefWidth(120);

        TableColumn<Emprendedor, String> colProyectos = new TableColumn<>("Proyectos");
        colProyectos.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getProyectos().size())));
        colProyectos.setPrefWidth(80);

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colBitacora, colProyectos);
        cargarTodosEmprendedores();
    }

    private void cargarTodosEmprendedores() {
        tableView.getItems().clear();
        
        
        if (!sistema.getMapaEmprendedores().isEmpty()) {
            for (Emprendedor emp : sistema.getMapaEmprendedores().values()) {
                if (!tableView.getItems().contains(emp)) {
                    tableView.getItems().add(emp);
                }
            }
        }
        
        for (Bitacora b : sistema.getBitacoras()) {
            for (Emprendedor emp : b.getEmprendedores()) {
                if (!tableView.getItems().contains(emp)) {
                    tableView.getItems().add(emp);
                }
            }
        }
    }
    
    private int contarEmprendedoresEnBitacoras() {
        int total = 0;
        for (Bitacora b : sistema.getBitacoras()) {
            total += b.getEmprendedores().size();
        }
        return total;
    }

    private void filtrarEmprendedores() {
        String filtro = cbBitacoras.getValue();
        if ("Todos".equals(filtro)) {
            cargarTodosEmprendedores();
        } else {
            tableView.getItems().clear();
            
            List<Emprendedor> emprendedoresFiltrados = sistema.filtrarPorBitacora(filtro);
            tableView.getItems().addAll(emprendedoresFiltrados);
            
        }
    }

    private void editarEmprendedor() {
        Emprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Error", "Seleccione un emprendedor para editar.");
            return;
        }

        // Crear ventana de edicion
        Stage ventanaEdicion = new Stage();
        ventanaEdicion.setTitle("Editar Emprendedor: " + seleccionado.getNombre());
        ventanaEdicion.initOwner(stage);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        // Campos pre-llenados con datos actuales
        TextField tfNombre = new TextField(seleccionado.getNombre());
        TextField tfEmail = new TextField(seleccionado.getEmail());
        TextField tfCapital = new TextField(String.valueOf(seleccionado.getCapital()));

        Label lblRut = new Label(seleccionado.getRut() + " (No editable)");
        lblRut.setStyle("-fx-text-fill: gray;");

        ComboBox<String> cbBitacora = new ComboBox<>();
        sistema.getBitacoras().forEach(b -> cbBitacora.getItems().add(b.getTipo()));

        // Encontrar bitacora actual
        final String bitacoraActual = buscarBitacoraDelEmprendedor(seleccionado);
        cbBitacora.setValue(bitacoraActual);

        Button btnGuardar = new Button("Guardar Cambios");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        // Eventos
        btnGuardar.setOnAction(e -> {
            try {
                // Validar campos
                String nuevoNombre = tfNombre.getText().trim();
                String nuevoEmail = tfEmail.getText().trim();
                double nuevoCapital = Double.parseDouble(tfCapital.getText().trim());
                String nuevaBitacora = cbBitacora.getValue();

                if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty() || !nuevoEmail.contains("@")) {
                    mostrarError("Error", "Por favor complete todos los campos correctamente.");
                    return;
                }

                // Actualizar datos del emprendedor
                seleccionado.setNombre(nuevoNombre);
                seleccionado.setEmail(nuevoEmail);
                seleccionado.setCapital(nuevoCapital);

                if (!nuevaBitacora.equals(bitacoraActual)) {
                    moverEmprendedorDeBitacora(seleccionado, bitacoraActual, nuevaBitacora);
                }

                mostrarMensaje("Exito", "Emprendedor actualizado correctamente.");
                ventanaEdicion.close();
                cargarTodosEmprendedores(); 

            } catch (NumberFormatException ex) {
                mostrarError("Error", "El capital debe ser un numero valido.");
            } catch (Exception ex) {
                mostrarError("Error", "Error al actualizar: " + ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> ventanaEdicion.close());

        // Layout
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(tfNombre, 1, 0);
        grid.add(new Label("RUT:"), 0, 1);
        grid.add(lblRut, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(new Label("Capital:"), 0, 3);
        grid.add(tfCapital, 1, 3);
        grid.add(new Label("Bitacora:"), 0, 4);
        grid.add(cbBitacora, 1, 4);

        javafx.scene.layout.HBox botones = new javafx.scene.layout.HBox(10);
        botones.getChildren().addAll(btnGuardar, btnCancelar);
        grid.add(botones, 0, 5, 2, 1);

        ventanaEdicion.setScene(new javafx.scene.Scene(grid, 400, 300));
        ventanaEdicion.showAndWait();
    }

    // Metodo auxiliar para encontrar la bitacora del emprendedor
    private String buscarBitacoraDelEmprendedor(Emprendedor emprendedor) {
        for (Bitacora b : sistema.getBitacoras()) {
            if (b.getEmprendedores().contains(emprendedor)) {
                return b.getTipo();
            }
        }
        return "Tecnologia"; // Por defecto si no se encuentra
    }

    // Metodo auxiliar para mover emprendedor entre bitacoras
    private void moverEmprendedorDeBitacora(Emprendedor emprendedor, String bitacoraOrigen, String bitacoraDestino) {
        // Remover de bitacora actual
        for (Bitacora b : sistema.getBitacoras()) {
            if (b.getTipo().equals(bitacoraOrigen)) {
                b.getEmprendedores().remove(emprendedor);
                break;
            }
        }

        // Agregar a nueva bitacora
        for (Bitacora b : sistema.getBitacoras()) {
            if (b.getTipo().equals(bitacoraDestino)) {
                b.agregarEmprendedor(emprendedor);
                break;
            }
        }

    }

    private void eliminarEmprendedor() {
        Emprendedor seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Confirmacion antes de eliminar
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminacion");
            confirmacion.setHeaderText("¿Esta seguro de eliminar este emprendedor?");
            confirmacion.setContentText("Esta accion no se puede deshacer.");
            
            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                try {
                    sistema.eliminarEmprendedor(seleccionado.getRut());
                    mostrarMensaje("Exito", "Emprendedor eliminado correctamente.");
                    cargarTodosEmprendedores(); // 
                } catch (ElementoNoEncontradoException ex) {
                    mostrarError("Error", ex.getMessage());
                }
            }
        } else {
            mostrarError("Error", "Seleccione un emprendedor para eliminar.");
        }
    }
}