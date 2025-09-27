package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import principal.Sistema;
import humanos.Emprendedor;
import humanos.Mentor;
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

        Tab tabMentores = new Tab("Mentores");
        tabMentores.setContent(crearTablaMentores());
        tabMentores.setClosable(false);

        Tab tabInversores = new Tab("Inversores");
        tabInversores.setContent(crearTablaInversores());
        tabInversores.setClosable(false);

        Tab tabResumen = new Tab("Resumen");
        tabResumen.setContent(crearPanelResumen());
        tabResumen.setClosable(false);

        tabPane.getTabs().addAll(tabEmprendedores, tabMentores, tabInversores, tabResumen);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
    }

    private TableView<Emprendedor> crearTablaEmprendedores() {
        TableView<Emprendedor> tableView = new TableView<>();

        TableColumn<Emprendedor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Emprendedor, String> colRut = new TableColumn<>("RUT");
        colRut.setCellValueFactory(new PropertyValueFactory<>("rut"));

        TableColumn<Emprendedor, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Emprendedor, Double> colCapital = new TableColumn<>("Capital");
        colCapital.setCellValueFactory(new PropertyValueFactory<>("capital"));

        TableColumn<Emprendedor, String> colBitacora = new TableColumn<>("Bitácora");
        // Esta columna requiere lógica especial

        tableView.getColumns().addAll(colNombre, colRut, colEmail, colCapital);

        // Cargar datos
        for (Bitacora b : sistema.getBitacoras()) {
            tableView.getItems().addAll(b.getEmprendedores());
        }

        return tableView;
    }

    private TableView<Mentor> crearTablaMentores() {
        TableView<Mentor> tableView = new TableView<>();

        TableColumn<Mentor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Mentor, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        TableColumn<Mentor, String> colExperiencia = new TableColumn<>("Experiencia");
        colExperiencia.setCellValueFactory(new PropertyValueFactory<>("aniosExperiencia"));

        tableView.getColumns().addAll(colNombre, colEspecialidad, colExperiencia);
        tableView.getItems().addAll(sistema.getMentores());

        return tableView;
    }

    private TableView<Inversor> crearTablaInversores() {
        TableView<Inversor> tableView = new TableView<>();

        TableColumn<Inversor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Inversor, Double> colCapital = new TableColumn<>("Capital Disponible");
        colCapital.setCellValueFactory(new PropertyValueFactory<>("capitalDisponible"));

        TableColumn<Inversor, Integer> colInversiones = new TableColumn<>("N° Inversiones");
        colInversiones.setCellValueFactory(new PropertyValueFactory<>("historialInversiones"));

        tableView.getColumns().addAll(colNombre, colCapital, colInversiones);
        tableView.getItems().addAll(sistema.getInversores());

        return tableView;
    }

    private VBox crearPanelResumen() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        int totalEmprendedores = sistema.getMapaEmprendedores().size();
        int totalMentores = sistema.getMentores().size();
        int totalInversores = sistema.getInversores().size();
        int totalBitacoras = sistema.getBitacoras().size();

        Label lblResumen = new Label("RESUMEN DEL SISTEMA");
        lblResumen.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblEmprendedores = new Label("Total Emprendedores: " + totalEmprendedores);
        Label lblMentores = new Label("Total Mentores: " + totalMentores);
        Label lblInversores = new Label("Total Inversores: " + totalInversores);
        Label lblBitacoras = new Label("Total Bitácoras: " + totalBitacoras);

        panel.getChildren().addAll(lblResumen, lblEmprendedores, lblMentores, lblInversores, lblBitacoras);

        return panel;
    }
}