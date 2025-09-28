package packs.pack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import principal.Sistema;
import ventanas.*;
import java.io.File;
import java.util.Optional;

public class Main extends Application {

    private Sistema sistema;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        sistema = new Sistema();
        
        // Inicialización inteligente que preserva datos
        sistema.inicializarSistema();

        // Botones de la interfaz
        Button btnAgregarEmprendedor = new Button("Agregar Emprendedor");
        Button btnGestionEmprendedores = new Button("Gestionar Emprendedores");
        Button btnBuscar = new Button("Buscar por RUT");
        Button btnFiltrar = new Button("Filtrar Emprendedores");
        Button btnMostrar = new Button("Mostrar Listado");
        Button btnInvertir = new Button("Invertir en Proyectos");
        Button btnAgregarInversor = new Button("Agregar Inversor");
        Button btnReporte = new Button("Generar Reporte TXT"); 
        Button btnGestionProyectos = new Button("Gestión de Proyectos");

        // Eventos de botones
        btnAgregarEmprendedor.setOnAction(e -> {
            try {
                VentanaEmprendedor ventana = new VentanaEmprendedor(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana: " + ex.getMessage());
            }
        });

        btnGestionEmprendedores.setOnAction(e -> {
            try {
                VentanaGestionEmprendedores ventana = new VentanaGestionEmprendedores(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana: " + ex.getMessage());
            }
        });
        
        btnBuscar.setOnAction(e -> {
            try {
                VentanaBusqueda ventana = new VentanaBusqueda(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana: " + ex.getMessage());
            }
        });

        btnFiltrar.setOnAction(e -> {
             try {
                VentanaFiltros ventana = new VentanaFiltros(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana: " + ex.getMessage());
            }
        });
        
        btnInvertir.setOnAction(e -> {
             try {
                VentanaInversion ventana = new VentanaInversion(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana: " + ex.getMessage());
            }
        });

        btnMostrar.setOnAction(e -> {
             try {
                VentanaListado ventana = new VentanaListado(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir listado: " + ex.getMessage());
            }
        });
        
        btnAgregarInversor.setOnAction(e -> {
             try {
                VentanaInversor ventana = new VentanaInversor(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana: " + ex.getMessage());
            }
        });
        
        btnReporte.setOnAction(e -> {
            try {
                sistema.generarReporteTxt("reporte_sistema.txt");
                mostrarAlerta("Exito", "Reporte generado: reporte_sistema.txt");
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al generar reporte: " + ex.getMessage());
            }
        });
        btnGestionProyectos.setOnAction(e -> {
            try {
                VentanaProyectos ventana = new VentanaProyectos(sistema);
                ventana.mostrar();
            } catch (Exception ex) {
                mostrarAlerta("Error", "Error al abrir ventana de proyectos: " + ex.getMessage());
            }
        });

        // Layout principal
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(20));
        root.getChildren().addAll(
            btnAgregarEmprendedor,
            btnGestionEmprendedores, 
            btnBuscar,
            btnFiltrar,
            btnGestionProyectos,
            btnInvertir,
            btnAgregarInversor,
            btnMostrar,
            btnReporte
        );

        Scene scene = new Scene(root, 300, 450); 
        primaryStage.setTitle("Sistema de Emprendedores");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Evento de cierre con guardado robusto
        primaryStage.setOnCloseRequest((var event) -> {
            
            try {
                int totalEmprendedores = sistema.getMapaEmprendedores().size();
                int totalInversores = sistema.getInversores().size();
                
                
                if (totalEmprendedores > 0) {
                    sistema.guardarEmprendedores("emprendedores.csv");

                } 
                
                if (totalInversores > 0) {
                    sistema.guardarInversores("inversores.csv");
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                
                // Mostrar alerta crítica al usuario
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Crítico al Guardar");
                alert.setHeaderText("No se pudieron guardar los datos");
                alert.setContentText("Error: " + ex.getMessage() + 
                                    "\n\nLos datos se perderán si cierra la aplicación." +
                                    "\n¿Desea intentar guardar manualmente o cerrar sin guardar?");
                
                ButtonType btnIntentar = new ButtonType("Intentar de Nuevo");
                ButtonType btnCerrar = new ButtonType("Cerrar Sin Guardar");
                alert.getButtonTypes().setAll(btnIntentar, btnCerrar);
                
                Optional<ButtonType> resultado = alert.showAndWait();
                if (resultado.isPresent() && resultado.get() == btnIntentar) {
                    event.consume(); // Cancelar el cierre
                }
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}