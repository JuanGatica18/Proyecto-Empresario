package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import principal.Sistema;
import humanos.Emprendedor;
import humanos.Inversor;
import principal.Bitacora;

import java.util.List;

public class VentanaListado extends VentanaBase {

    private TabPane tabPane;

    public VentanaListado(Sistema sistema) {
        super(sistema, "Listado General del Sistema");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        tabPane = new TabPane();
        
        Tab tabEmprendedores = new Tab("Emprendedores");
        tabEmprendedores.setContent(crearTablaEmprendedores());
        tabEmprendedores.setClosable(false);

        Tab tabInversores = new Tab("Inversores");
        tabInversores.setContent(crearTablaInversores());
        tabInversores.setClosable(false);

        Tab tabResumen = new Tab("Resumen");
        tabResumen.setContent(crearPanelResumen());
        tabResumen.setClosable(false);

        tabPane.getTabs().addAll(tabEmprendedores, tabInversores, tabResumen);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 650);
        stage.setScene(scene);
    }

    private VBox crearTablaEmprendedores() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        
        // Título y botón refrescar
        Label lblTitulo = new Label("LISTADO DE EMPRENDEDORES");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button btnRefrescar = new Button("Refrescar Datos");
        btnRefrescar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        
        TableView<EmprendedorConBitacora> tableView = new TableView<>();

        TableColumn<EmprendedorConBitacora, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getNombre()));
        colNombre.setPrefWidth(150);

        TableColumn<EmprendedorConBitacora, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getRut()));
        colRut.setPrefWidth(100);

        TableColumn<EmprendedorConBitacora, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getEmail()));
        colEmail.setPrefWidth(200);

        TableColumn<EmprendedorConBitacora, String> colCapital = new TableColumn<>("Capital");
        colCapital.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("$" + 
                String.format("%,.2f", cellData.getValue().getEmprendedor().getCapital())));
        colCapital.setPrefWidth(120);

        TableColumn<EmprendedorConBitacora, String> colBitacora = new TableColumn<>("Bitácora");
        colBitacora.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoBitacora()));
        colBitacora.setPrefWidth(120);

        TableColumn<EmprendedorConBitacora, String> colProyectos = new TableColumn<>("N° Proyectos");
        colProyectos.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getEmprendedor().getProyectos().size())));
        colProyectos.setPrefWidth(90);

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colBitacora, colProyectos);

        Runnable cargarDatos = () -> {
            tableView.getItems().clear();
            
            // Opción 1: Cargar desde mapa principal
            if (!sistema.getMapaEmprendedores().isEmpty()) {
                for (Emprendedor emp : sistema.getMapaEmprendedores().values()) {
                    String bitacora = buscarBitacoraDelEmprendedor(emp);
                    tableView.getItems().add(new EmprendedorConBitacora(emp, bitacora));
                }
            } else {
                // Opción 2: Cargar desde bitácoras si el mapa está vacío
                for (Bitacora b : sistema.getBitacoras()) {
                    for (Emprendedor e : b.getEmprendedores()) {
                        tableView.getItems().add(new EmprendedorConBitacora(e, b.getTipo()));
                    }
                }
            }
            
        };
        
        // Cargar datos iniciales
        cargarDatos.run();
        
        // Evento del botón refrescar
        btnRefrescar.setOnAction(e -> cargarDatos.run());
        
        // Label de información
        Label lblInfo = new Label();
        lblInfo.textProperty().bind(javafx.beans.binding.Bindings.format(
            "Emprendedores mostrados: %d", tableView.getItems().size()));
        lblInfo.setStyle("-fx-text-fill: #666;");

        container.getChildren().addAll(lblTitulo, btnRefrescar, tableView, lblInfo);
        return container;
    }
    
    // ✅ MÉTODO AUXILIAR: Buscar bitácora del emprendedor
    private String buscarBitacoraDelEmprendedor(Emprendedor emprendedor) {
        for (Bitacora b : sistema.getBitacoras()) {
            if (b.getEmprendedores().contains(emprendedor)) {
                return b.getTipo();
            }
        }
        return "No asignada"; // Si no se encuentra en ninguna bitácora
    }

    // ✅ MÉTODO CORREGIDO: Crear tabla de inversores
    private VBox crearTablaInversores() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        
        Label lblTitulo = new Label("LISTADO DE INVERSORES");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button btnRefrescar = new Button("Refrescar Datos");
        btnRefrescar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        
        TableView<Inversor> tableView = new TableView<>();

        TableColumn<Inversor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(150);

        TableColumn<Inversor, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));
        colRut.setPrefWidth(100);

        TableColumn<Inversor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(200);

        TableColumn<Inversor, String> colCapital = new TableColumn<>("Capital Disponible");
        colCapital.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("$" + 
                String.format("%,.2f", cellData.getValue().getCapitalDisponible())));
        colCapital.setPrefWidth(140);

        TableColumn<Inversor, String> colInversiones = new TableColumn<>("N° Inversiones");
        colInversiones.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getHistorialInversiones().size())));
        colInversiones.setPrefWidth(100);

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colInversiones);

        Runnable cargarInversores = () -> {
            tableView.getItems().clear();
            
            if (sistema.getInversores() != null && !sistema.getInversores().isEmpty()) {
                tableView.getItems().addAll(sistema.getInversores());
            } else {
            }
        };
        
        // Cargar datos iniciales
        cargarInversores.run();
        
        // Evento del botón refrescar
        btnRefrescar.setOnAction(e -> cargarInversores.run());
        
        // Label de información
        Label lblInfo = new Label();
        lblInfo.textProperty().bind(javafx.beans.binding.Bindings.format(
            "Inversores mostrados: %d", tableView.getItems().size()));
        lblInfo.setStyle("-fx-text-fill: #666;");

        container.getChildren().addAll(lblTitulo, btnRefrescar, tableView, lblInfo);
        return container;
    }

    // ✅ MÉTODO MEJORADO: Panel de resumen con más detalles
    private VBox crearPanelResumen() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        Label lblResumen = new Label("RESUMEN DEL SISTEMA");
        lblResumen.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Crear labels que se actualizan dinámicamente
        Label lblEmprendedores = new Label();
        Label lblInversores = new Label();
        Label lblBitacoras = new Label();
        Label lblProyectos = new Label();
        
        // Botón para actualizar resumen
        Button btnActualizar = new Button("Actualizar Resumen");
        btnActualizar.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        
        Runnable actualizarResumen = () -> {
            int totalEmprendedores = sistema.getMapaEmprendedores().size();
            int totalInversores = sistema.getInversores().size();
            int totalBitacoras = sistema.getBitacoras().size();
            
            // Contar proyectos totales
            int totalProyectos = 0;
            for (Emprendedor emp : sistema.getMapaEmprendedores().values()) {
                totalProyectos += emp.getProyectos().size();
            }
            
            lblEmprendedores.setText(" Total Emprendedores: " + totalEmprendedores);
            lblInversores.setText(" Total Inversores: " + totalInversores);
            lblBitacoras.setText(" Total Bitácoras: " + totalBitacoras);
            lblProyectos.setText(" Total Proyectos: " + totalProyectos);
            
            // Detalle por bitácoras
            StringBuilder detalleBitacoras = new StringBuilder();
            detalleBitacoras.append("\n--- DETALLE POR BITÁCORAS ---\n");
            for (Bitacora b : sistema.getBitacoras()) {
                detalleBitacoras.append("• ").append(b.getTipo())
                                .append(": ").append(b.getEmprendedores().size())
                                .append(" emprendedores\n");
            }
            
        };
        
        // Actualizar inicialmente
        actualizarResumen.run();
        
        btnActualizar.setOnAction(e -> actualizarResumen.run());
        
        // Estilo para los labels
        String labelStyle = "-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #ecf0f1; -fx-background-radius: 5px;";
        lblEmprendedores.setStyle(labelStyle);
        lblInversores.setStyle(labelStyle);
        lblBitacoras.setStyle(labelStyle);
        lblProyectos.setStyle(labelStyle);

        panel.getChildren().addAll(
            lblResumen, 
            new Separator(),
            lblEmprendedores, 
            lblInversores, 
            lblBitacoras, 
            lblProyectos,
            new Separator(),
            btnActualizar
        );

        return panel;
    }
    
    // ✅ CLASE AUXILIAR: Mantener la clase EmprendedorConBitacora
    public static class EmprendedorConBitacora {
        private final Emprendedor emprendedor;
        private final String tipoBitacora;

        public EmprendedorConBitacora(Emprendedor emprendedor, String tipoBitacora) {
            this.emprendedor = emprendedor;
            this.tipoBitacora = tipoBitacora;
        }

        public Emprendedor getEmprendedor() { return emprendedor; }
        public String getTipoBitacora() { return tipoBitacora; }
    }
}