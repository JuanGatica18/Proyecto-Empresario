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

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
    }

    private TableView<EmprendedorConBitacora> crearTablaEmprendedores() {
        TableView<EmprendedorConBitacora> tableView = new TableView<>();

        TableColumn<EmprendedorConBitacora, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getNombre()));

        TableColumn<EmprendedorConBitacora, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getRut()));

        TableColumn<EmprendedorConBitacora, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmprendedor().getEmail()));

        TableColumn<EmprendedorConBitacora, String> colCapital = new TableColumn<>("Capital");
        colCapital.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("$" + 
                String.format("%.2f", cellData.getValue().getEmprendedor().getCapital())));

        TableColumn<EmprendedorConBitacora, String> colBitacora = new TableColumn<>("Bitácora");
        colBitacora.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipoBitacora()));

        TableColumn<EmprendedorConBitacora, String> colProyectos = new TableColumn<>("N° Proyectos");
        colProyectos.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getEmprendedor().getProyectos().size())));

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colBitacora, colProyectos);

        // Cargar datos con información de bitácora
        for (Bitacora b : sistema.getBitacoras()) {
            for (Emprendedor e : b.getEmprendedores()) {
                tableView.getItems().add(new EmprendedorConBitacora(e, b.getTipo()));
            }
        }

        return tableView;
    }

    private TableView<Inversor> crearTablaInversores() {
        TableView<Inversor> tableView = new TableView<>();

        TableColumn<Inversor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(150);

        TableColumn<Inversor, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));
        colRut.setPrefWidth(100);

        TableColumn<Inversor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(180);

        TableColumn<Inversor, String> colCapital = new TableColumn<>("Capital Disponible");
        colCapital.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty("$" + 
                String.format("%.2f", cellData.getValue().getCapitalDisponible())));
        colCapital.setPrefWidth(120);

        TableColumn<Inversor, String> colInversiones = new TableColumn<>("N° Inversiones");
        colInversiones.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getHistorialInversiones().size())));
        colInversiones.setPrefWidth(100);

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital, colInversiones);

        // ✅ CARGAR INVERSORES CORRECTAMENTE
        if (sistema.getInversores() != null && !sistema.getInversores().isEmpty()) {
            tableView.getItems().addAll(sistema.getInversores());
        } else {
            System.out.println("No hay inversores cargados en el sistema");
        }

        return tableView;
    }

    private VBox crearPanelResumen() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        int totalEmprendedores = sistema.getMapaEmprendedores().size();
        int totalInversores = sistema.getInversores().size();
        int totalBitacoras = sistema.getBitacoras().size();

        Label lblResumen = new Label("RESUMEN DEL SISTEMA");
        lblResumen.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblEmprendedores = new Label("Total Emprendedores: " + totalEmprendedores);
        Label lblInversores = new Label("Total Inversores: " + totalInversores);
        Label lblBitacoras = new Label("Total Bitácoras: " + totalBitacoras);

        panel.getChildren().addAll(lblResumen, lblEmprendedores, lblInversores, lblBitacoras);

        return panel;
    }
    
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