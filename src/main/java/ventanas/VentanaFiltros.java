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
import humanos.Emprendedor;
import java.util.List;

public class VentanaFiltros extends VentanaBase {

    private TableView<Emprendedor> tableView;
    private Label lblResultados;

    public VentanaFiltros(Sistema sistema) {
        super(sistema, "Filtros Avanzados");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Panel superior con filtros
        VBox panelFiltros = new VBox(15);
        panelFiltros.setPadding(new Insets(10));
        panelFiltros.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-border-radius: 5;");

        Label lblTitulo = new Label("FILTROS AVANZADOS DEL SISTEMA");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Filtro 1: Por Capital
        GridPane gridCapital = new GridPane();
        gridCapital.setHgap(10);
        gridCapital.setVgap(5);
        
        Label lblCapital = new Label("Filtrar por Capital Minimo:");
        lblCapital.setStyle("-fx-font-weight: bold;");
        TextField tfCapitalMin = new TextField();
        tfCapitalMin.setPromptText("Ej: 10000");
        Button btnFiltrarCapital = new Button("Filtrar por Capital");
        btnFiltrarCapital.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        gridCapital.add(lblCapital, 0, 0, 3, 1);
        gridCapital.add(new Label("Capital minimo:"), 0, 1);
        gridCapital.add(tfCapitalMin, 1, 1);
        gridCapital.add(btnFiltrarCapital, 2, 1);

        // Filtro 2: Por Cantidad de Proyectos
        GridPane gridProyectos = new GridPane();
        gridProyectos.setHgap(10);
        gridProyectos.setVgap(5);
        
        Label lblProyectos = new Label("Filtrar por Cantidad de Proyectos:");
        lblProyectos.setStyle("-fx-font-weight: bold;");
        Spinner<Integer> spMinProyectos = new Spinner<>(0, 10, 1);
        Button btnFiltrarProyectos = new Button("Filtrar por Proyectos");
        btnFiltrarProyectos.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        gridProyectos.add(lblProyectos, 0, 0, 3, 1);
        gridProyectos.add(new Label("Min. proyectos:"), 0, 1);
        gridProyectos.add(spMinProyectos, 1, 1);
        gridProyectos.add(btnFiltrarProyectos, 2, 1);

        // Filtro 3: Por Bitacora
        GridPane gridBitacora = new GridPane();
        gridBitacora.setHgap(10);
        gridBitacora.setVgap(5);
        
        Label lblBitacora = new Label("Filtrar por Tipo de Bitacora:");
        lblBitacora.setStyle("-fx-font-weight: bold;");
        ComboBox<String> cbBitacoras = new ComboBox<>();
        cbBitacoras.getItems().add("Todas");
        sistema.getBitacoras().forEach(b -> cbBitacoras.getItems().add(b.getTipo()));
        cbBitacoras.setValue("Todas");
        Button btnFiltrarBitacora = new Button("Filtrar por Bitacora");
        btnFiltrarBitacora.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");

        gridBitacora.add(lblBitacora, 0, 0, 3, 1);
        gridBitacora.add(new Label("Tipo:"), 0, 1);
        gridBitacora.add(cbBitacoras, 1, 1);
        gridBitacora.add(btnFiltrarBitacora, 2, 1);

        // Botones de control
        HBox panelControl = new HBox(10);
        Button btnMostrarTodos = new Button("Mostrar Todos");
        Button btnLimpiar = new Button("Limpiar Filtros");
        btnMostrarTodos.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnLimpiar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        panelControl.getChildren().addAll(btnMostrarTodos, btnLimpiar);

        // Label de resultados
        lblResultados = new Label("Resultados: 0 emprendedores encontrados");
        lblResultados.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");

        panelFiltros.getChildren().addAll(
            lblTitulo,
            new Separator(),
            gridCapital,
            new Separator(),
            gridProyectos, 
            new Separator(),
            gridBitacora,
            new Separator(),
            panelControl,
            lblResultados
        );

        root.setTop(panelFiltros);

        // Tabla de resultados
        tableView = new TableView<>();
        configurarTabla();
        root.setCenter(tableView);

        // EVENTOS DE FILTROS - CORREGIDOS
        btnFiltrarCapital.setOnAction(e -> {
            try {
                double capitalMin = Double.parseDouble(tfCapitalMin.getText().trim());
                List<Emprendedor> filtrados = sistema.filtrarPorCapital(capitalMin);
                actualizarTabla(filtrados);
                lblResultados.setText("Filtro por capital >= $" + capitalMin + 
                                     ": " + filtrados.size() + " emprendedores");
            } catch (NumberFormatException ex) {
                mostrarError("Error", "Ingrese un numero valido para el capital");
            }
        });

        btnFiltrarProyectos.setOnAction(e -> {
            int minProyectos = spMinProyectos.getValue();
            List<Emprendedor> filtrados = sistema.filtrarPorNroProyectos(minProyectos);
            actualizarTabla(filtrados);
            lblResultados.setText("Filtro por proyectos >= " + minProyectos + 
                                 ": " + filtrados.size() + " emprendedores");
        });

        btnFiltrarBitacora.setOnAction(e -> {
            String tipoBitacora = cbBitacoras.getValue();
            if ("Todas".equals(tipoBitacora)) {
                mostrarTodosEmprendedores();
            } else {
                List<Emprendedor> filtrados = sistema.filtrarPorBitacora(tipoBitacora);
                actualizarTabla(filtrados);
                lblResultados.setText("Filtro por bitacora '" + tipoBitacora + 
                                     "': " + filtrados.size() + " emprendedores");
            }
        });

        btnMostrarTodos.setOnAction(e -> mostrarTodosEmprendedores());

        btnLimpiar.setOnAction(e -> {
            tfCapitalMin.clear();
            spMinProyectos.getValueFactory().setValue(1);
            cbBitacoras.setValue("Todas");
            mostrarTodosEmprendedores();
        });

        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        
        // Cargar datos iniciales
        mostrarTodosEmprendedores();
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
        colCapital.setPrefWidth(100);

        TableColumn<Emprendedor, String> colProyectos = new TableColumn<>("No Proyectos");
        colProyectos.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getProyectos().size())));
        colProyectos.setPrefWidth(90);

        TableColumn<Emprendedor, String> colBitacora = new TableColumn<>("Bitacora");
        colBitacora.setCellValueFactory(cellData -> {
            // Buscar en que bitacora esta el emprendedor
            String bitacora = "No encontrada";
            for (var b : sistema.getBitacoras()) {
                if (b.getEmprendedores().contains(cellData.getValue())) {
                    bitacora = b.getTipo();
                    break;
                }
            }
            return new javafx.beans.property.SimpleStringProperty(bitacora);
        });
        colBitacora.setPrefWidth(120);

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colProyectos, colBitacora);
    }

    private void actualizarTabla(List<Emprendedor> emprendedores) {
        tableView.getItems().clear();
        tableView.getItems().addAll(emprendedores);
    }

    private void mostrarTodosEmprendedores() {
        actualizarTabla(List.copyOf(sistema.getMapaEmprendedores().values()));
        lblResultados.setText("Mostrando todos los emprendedores: " + 
                             sistema.getMapaEmprendedores().size());
    }
}