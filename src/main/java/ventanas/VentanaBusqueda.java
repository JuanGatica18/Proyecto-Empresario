package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import principal.Sistema;
import humanos.Emprendedor;
import humanos.Mentor;
import humanos.Inversor;
import excepciones.ElementoNoEncontradoException;

public class VentanaBusqueda extends VentanaBase {

    /**
     * Constructor de la ventana de búsqueda.
     * @param sistema La instancia principal del sistema donde se realiza la búsqueda.
     */
    public VentanaBusqueda(Sistema sistema) {
        super(sistema, "Búsqueda por RUT");
        configurarVentana();
    }

    /**
     * Configura los elementos visuales y la lógica de la ventana.
     */
    @Override
    protected void configurarVentana() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        VBox panelBusqueda = new VBox(10);
        panelBusqueda.setPadding(new Insets(10));

        Label lblTitulo = new Label("BÚSQUEDA DE PERSONAS POR RUT");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox panelInput = new HBox(10);
        TextField tfRut = new TextField();
        tfRut.setPromptText("Ingrese RUT a buscar...");
        tfRut.setPrefWidth(200);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        panelInput.getChildren().addAll(new Label("RUT:"), tfRut, btnBuscar);

        TextArea taResultado = new TextArea();
        taResultado.setEditable(false);
        taResultado.setPrefHeight(300);
        taResultado.setStyle("-fx-font-family: monospace;"); // Para mejor formato de texto

        panelBusqueda.getChildren().addAll(lblTitulo, panelInput, taResultado);
        root.setCenter(panelBusqueda);

        // Lógica del botón Buscar
        btnBuscar.setOnAction(e -> {
            String rut = tfRut.getText().trim();
            if (rut.isEmpty()) {
                mostrarError("Error", "Por favor, ingrese un RUT para realizar la búsqueda.");
                return;
            }
            // Llamada al método que realiza y muestra la búsqueda
            realizarBusqueda(rut, taResultado);
        });

        Scene scene = new Scene(root, 500, 450);
        stage.setScene(scene); // CORRECCIÓN APLICADA AQUÍ
    }
    
    /**
     * Realiza la búsqueda del RUT en el sistema para Emprendedores, Mentores e Inversores.
     * @param rut El RUT a buscar.
     * @param taResultado El TextArea donde se mostrarán los resultados.
     */
    private void realizarBusqueda(String rut, TextArea taResultado) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== RESULTADOS DE BÚSQUEDA PARA RUT: ").append(rut).append(" ===\n\n");
        boolean encontrado = false;

        // 1. Buscar Emprendedor
        try {
            Emprendedor emp = sistema.buscarEmprendedorPorRut(rut);
            resultado.append("--- TIPO: EMPRENDEDOR ---\n");
            resultado.append(emp.mostrarInfo()).append("\n\n"); // MEJORA DE VISUALIZACIÓN
            encontrado = true;
        } catch (ElementoNoEncontradoException ex) {
            // No se hace nada aquí, se sigue buscando
        }
        
        // 2. Buscar Mentor
        try {
            Mentor men = sistema.buscarMentorPorRut(rut);
            resultado.append("--- TIPO: MENTOR ---\n");
            resultado.append(men.mostrarInfo()).append("\n\n"); // MEJORA DE VISUALIZACIÓN
            encontrado = true;
        } catch (ElementoNoEncontradoException ex) {
            // No se hace nada aquí, se sigue buscando
        }

        // 3. Buscar Inversor
        try {
            Inversor inv = sistema.buscarInversorPorRut(rut);
            resultado.append("--- TIPO: INVERSOR ---\n");
            resultado.append(inv.mostrarInfo()).append("\n\n"); // MEJORA DE VISUALIZACIÓN
            encontrado = true;
        } catch (ElementoNoEncontradoException ex) {
            // No se hace nada aquí, se sigue buscando
        }
        
        // 4. Mensaje final si no se encontró en ninguna categoría
        if (!encontrado) {
             resultado.setLength(0); // Limpiar el StringBuilder
             resultado.append("--- NO ENCONTRADO ---\n\n");
             resultado.append("El RUT '").append(rut).append("' no corresponde a ningún Emprendedor, \nMentor o Inversor registrado en el sistema.");
        }
        
        taResultado.setText(resultado.toString());
    }
}