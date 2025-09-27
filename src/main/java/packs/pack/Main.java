package packs.pack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import principal.Sistema;
import ventanas.VentanaListado;
import ventanas.VentanaInversor;
import ventanas.VentanaMentor;
import ventanas.VentanaEmprendedor;
import ventanas.VentanaGestionEmprendedores;
import ventanas.VentanaBusqueda;
import excepciones.DatosInvalidosException;

public class Main extends Application {

    private Sistema sistema;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 1️⃣ Crear el sistema con manejo de errores
        sistema = new Sistema();
        
        // Cargar datos con manejo de excepciones
        try {
            sistema.cargarEmprendedores("emprendedores.csv");
            sistema.cargarMentores("mentores.csv");
            sistema.cargarInversores("inversores.csv");
        } catch (Exception e) {
            mostrarAlerta("Advertencia", "Algunos archivos CSV no se pudieron cargar: " + e.getMessage());
        }

        // 2️⃣ Botones principales MEJORADOS
        Button btnAgregarEmprendedor = new Button("Agregar Emprendedor");
        Button btnAgregarMentor = new Button("Agregar Mentor");
        Button btnAgregarInversor = new Button("Agregar Inversor");
        Button btnGestionEmprendedores = new Button("Gestionar Emprendedores");
        Button btnBuscar = new Button("Buscar por RUT");
        Button btnMostrar = new Button("Mostrar Listado");
        Button btnExportar = new Button("Exportar CSV");
        Button btnReporte = new Button("Generar Reporte TXT");
        Button btnFiltrar = new Button("Filtrar por Capital");

        // 3️⃣ Eventos MEJORADOS con manejo de excepciones
        btnAgregarEmprendedor.setOnAction(e -> {
            try {
                VentanaEmprendedor ventana = new VentanaEmprendedor(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "No se pudo abrir la ventana: " + ex.getMessage());
            }
        });

        btnAgregarMentor.setOnAction(e -> {
            try {
                VentanaMentor ventana = new VentanaMentor(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "No se pudo abrir la ventana: " + ex.getMessage());
            }
        });

        btnAgregarInversor.setOnAction(e -> {
            try {
                VentanaInversor ventana = new VentanaInversor(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "No se pudo abrir la ventana: " + ex.getMessage());
            }
        });

        btnGestionEmprendedores.setOnAction(e -> {
            try {
                VentanaGestionEmprendedores ventana = new VentanaGestionEmprendedores(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "No se pudo abrir la ventana de gestión: " + ex.getMessage());
            }
        });

        btnBuscar.setOnAction(e -> {
            try {
                VentanaBusqueda ventana = new VentanaBusqueda(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "No se pudo abrir la ventana de búsqueda: " + ex.getMessage());
            }
        });

        btnMostrar.setOnAction(e -> {
            try {
                VentanaListado ventana = new VentanaListado(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "No se pudo abrir el listado: " + ex.getMessage());
            }
        });

        btnExportar.setOnAction(e -> {
            try {
                sistema.guardarEmprendedores("src/main/resources/emprendedores.csv");
                sistema.guardarMentores("src/main/resources/mentores.csv");
                sistema.guardarInversores("src/main/resources/inversores.csv");
                mostrarAlerta("Éxito", "Datos exportados correctamente a CSV");
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al exportar: " + ex.getMessage());
            }
        });

        btnReporte.setOnAction(e -> {
            try {
                sistema.generarReporteTxt("reporte_sistema.txt");
                mostrarAlerta("Éxito", "Reporte TXT generado: reporte_sistema.txt");
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al generar reporte: " + ex.getMessage());
            }
        });

        btnFiltrar.setOnAction(e -> {
            try {
                // Ejemplo de uso del filtrado (SIA2.5)
                var emprendedoresFiltrados = sistema.filtrarEmprendedoresPorCapital(5000);
                mostrarAlerta("Filtrado", 
                    "Emprendedores con capital >= $5000: " + emprendedoresFiltrados.size());
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al filtrar: " + ex.getMessage());
            }
        });

        // 4️⃣ Layout principal MEJORADO
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(20));
        
        // Agrupar botones por funcionalidad
        root.getChildren().addAll(
            new javafx.scene.control.Label("Gestión Básica:"),
            btnAgregarEmprendedor, btnAgregarMentor, btnAgregarInversor,
            new javafx.scene.control.Label("Gestión Avanzada:"),
            btnGestionEmprendedores, btnBuscar, btnFiltrar,
            new javafx.scene.control.Label("Reportes:"),
            btnMostrar, btnExportar, btnReporte
        );

        Scene scene = new Scene(root, 350, 550);
        primaryStage.setTitle("Sistema de Gestión de Emprendedores - SIA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}