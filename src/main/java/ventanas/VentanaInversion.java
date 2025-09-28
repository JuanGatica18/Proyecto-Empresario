package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import principal.Sistema;
import humanos.Inversor;
import humanos.Emprendedor;
import principal.Proyecto;

public class VentanaInversion extends VentanaBase {
    
    public VentanaInversion(Sistema sistema) {
        super(sistema, "Realizar Inversion en Proyectos");
        configurarVentana();
    }

    @Override
    protected void configurarVentana() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label lblTitulo = new Label("INVERSIÓN EN PROYECTOS DE EMPRENDEDORES");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Panel de selección con mejor layout
        GridPane gridSeleccion = new GridPane();
        gridSeleccion.setHgap(10);
        gridSeleccion.setVgap(15);
        gridSeleccion.setPadding(new Insets(15));
        gridSeleccion.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-color: #f8f9fa; -fx-background-radius: 8;");

        // Selección de inversor
        Label lblInversor = new Label("Seleccionar Inversor:");
        lblInversor.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        ComboBox<Inversor> cbInversores = new ComboBox<>();
        cbInversores.setPrefWidth(350);
        // Cargar datos reales de Inversores
        sistema.getInversores().forEach(cbInversores.getItems()::add);
        cbInversores.setPromptText("Seleccione un Inversor...");

        // Selección de emprendedor
        Label lblEmprendedor = new Label("Seleccionar Emprendedor:");
        lblEmprendedor.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        ComboBox<Emprendedor> cbEmprendedores = new ComboBox<>();
        cbEmprendedores.setPrefWidth(350);
        sistema.getMapaEmprendedores().values().forEach(cbEmprendedores.getItems()::add);
        cbEmprendedores.setPromptText("Seleccione un Emprendedor...");

        // Selección de proyecto
        Label lblProyecto = new Label("Seleccionar Proyecto:");
        lblProyecto.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        ComboBox<Proyecto> cbProyectos = new ComboBox<>();
        cbProyectos.setPrefWidth(350);
        cbProyectos.setPromptText("Primero seleccione un emprendedor...");
        cbProyectos.setDisable(true);
        
        // Organizar grid de selección
        gridSeleccion.add(lblInversor, 0, 0);
        gridSeleccion.add(cbInversores, 1, 0);
        gridSeleccion.add(lblEmprendedor, 0, 1);
        gridSeleccion.add(cbEmprendedores, 1, 1);
        gridSeleccion.add(lblProyecto, 0, 2);
        gridSeleccion.add(cbProyectos, 1, 2);

        // Panel de información del proyecto
        VBox panelInfo = new VBox(10);
        panelInfo.setPadding(new Insets(15));
        panelInfo.setStyle("-fx-border-color: #3498db; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-color: #ecf7ff; -fx-background-radius: 8;");
        
        Label lblInfoTitulo = new Label("INFORMACIÓN DETALLADA DEL PROYECTO");
        lblInfoTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2980b9;");
        
        TextArea taInfoProyecto = new TextArea();
        taInfoProyecto.setEditable(false);
        taInfoProyecto.setPromptText("Seleccione un proyecto para ver información detallada...");
        taInfoProyecto.setPrefHeight(160);
        taInfoProyecto.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-font-size: 12px;");
        
        panelInfo.getChildren().addAll(lblInfoTitulo, taInfoProyecto);

        // Panel de inversión
        VBox panelInversion = new VBox(10);
        panelInversion.setPadding(new Insets(15));
        panelInversion.setStyle("-fx-border-color: #27ae60; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-color: #f0fff4; -fx-background-radius: 8;");
        
        Label lblInversionTitulo = new Label("REALIZAR INVERSIÓN");
        lblInversionTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #27ae60;");

        HBox panelMonto = new HBox(10);
        Label lblMonto = new Label("Monto a Invertir:");
        lblMonto.setStyle("-fx-font-weight: bold;");
        TextField tfMonto = new TextField();
        tfMonto.setPromptText("Ej: 5000.00");
        tfMonto.setPrefWidth(150);
        panelMonto.getChildren().addAll(lblMonto, tfMonto);

        Button btnInvertir = new Button(" REALIZAR INVERSIÓN");
        btnInvertir.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 20px;");
        btnInvertir.setPrefWidth(200);
        
        Label lblResultado = new Label("");
        lblResultado.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        
        panelInversion.getChildren().addAll(lblInversionTitulo, panelMonto, btnInvertir, lblResultado);

        // Logica para actualizar ComboBox de Proyectos
        cbEmprendedores.setOnAction(e -> {
            Emprendedor seleccionado = cbEmprendedores.getSelectionModel().getSelectedItem();
            cbProyectos.getItems().clear();
            taInfoProyecto.clear();
            lblResultado.setText("");
            
            if (seleccionado != null) {
                if (seleccionado.getProyectos().isEmpty()) {
                    cbProyectos.setPromptText("Este emprendedor no tiene proyectos");
                    cbProyectos.setDisable(true);
                } else {
                    cbProyectos.getItems().addAll(seleccionado.getProyectos());
                    cbProyectos.setPromptText("Seleccione un proyecto...");
                    cbProyectos.setDisable(false);
                }
            } else {
                cbProyectos.setPromptText("Primero seleccione un emprendedor...");
                cbProyectos.setDisable(true);
            }
        });
        
        // Logica para mostrar info del Proyecto seleccionado
        cbProyectos.setOnAction(e -> {
            Proyecto proyecto = cbProyectos.getSelectionModel().getSelectedItem();
            if (proyecto != null) {
                taInfoProyecto.setText(
                    "=== INFORMACIÓN DETALLADA DEL PROYECTO ===\n" +
                    "Nombre: " + proyecto.getNombre() + "\n" +
                    "Descripción: " + proyecto.getDescripcion() + "\n" +
                    "Tipo/Sector: " + proyecto.getTipo() + "\n" +
                    "Estado: " + proyecto.getEstado() + "\n" +
                    "Avance: " + String.format("%.1f%%", proyecto.getPorcentajeAvance()) + "\n" +
                    "Inversión actual: $" + String.format("%,.2f", proyecto.getInversion()) + "\n" +
                    "Riesgo: " + proyecto.calcularRiesgo() + "\n" +
                    "Empleados requeridos: " + proyecto.getEmpleadosRequeridos() + "\n" +
                    "Ubicación: " + proyecto.getUbicacion() + "\n" +
                    "Fecha creación: " + proyecto.getFechaCreacion() + "\n" +
                    "\n--- ANÁLISIS DE INVERSIÓN ---\n" +
                    "ROI Estimado: " + calcularROIEstimado(proyecto) + "\n" +
                    "Tiempo estimado recuperación: " + calcularTiempoRecuperacion(proyecto) + "\n" +
                    "Recomendación: " + generarRecomendacion(proyecto)
                );
            } else {
                taInfoProyecto.clear();
            }
        });

        btnInvertir.setOnAction(e -> {
            try {
                Inversor inversor = cbInversores.getSelectionModel().getSelectedItem();
                Proyecto proyecto = cbProyectos.getSelectionModel().getSelectedItem();
                String montoTexto = tfMonto.getText().trim();

                if (inversor == null) {
                    mostrarError("Error", "Debe seleccionar un inversor");
                    return;
                }
                
                if (proyecto == null) {
                    mostrarError("Error", "Debe seleccionar un proyecto");
                    return;
                }

                if (montoTexto.isEmpty()) {
                    mostrarError("Error", "Debe ingresar un monto");
                    return;
                }

                double monto = Double.parseDouble(montoTexto);

                if (monto <= 0) {
                    mostrarError("Error", "El monto debe ser mayor a 0");
                    return;
                }
                
                if (inversor.invertir(proyecto, monto)) {
                    lblResultado.setText(" INVERSIÓN EXITOSA!");
                    lblResultado.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 14px;");
                    
                    mostrarMensaje("Éxito", 
                        String.format("Inversión realizada correctamente:\n\n" +
                                    "• Inversor: %s\n" +
                                    "• Proyecto: %s\n" +
                                    "• Monto invertido: $%,.2f\n" +
                                    "• Capital restante: $%,.2f",
                                    inversor.getNombre(),
                                    proyecto.getNombre(),
                                    monto,
                                    inversor.getCapitalDisponible()));
                    
                    // Limpiar y actualizar datos
                    tfMonto.clear();
                    cbInversores.getSelectionModel().clearSelection();
                    cbInversores.getItems().clear();
                    sistema.getInversores().forEach(cbInversores.getItems()::add);
                    
                    // Actualizar información del proyecto
                    taInfoProyecto.setText(taInfoProyecto.getText().replace(
                        "Inversión actual: $" + String.format("%,.2f", proyecto.getInversion() - monto),
                        "Inversión actual: $" + String.format("%,.2f", proyecto.getInversion())
                    ));
                    
                } else {
                    lblResultado.setText("INVERSIÓN FALLIDA - Capital insuficiente");
                    lblResultado.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 14px;");
                    
                    mostrarError("Error", 
                        String.format("No se pudo realizar la inversión.\n\n" +
                                    "Capital disponible: $%,.2f\n" +
                                    "Monto solicitado: $%,.2f\n" +
                                    "Déficit: $%,.2f",
                                    inversor.getCapitalDisponible(),
                                    monto,
                                    monto - inversor.getCapitalDisponible()));
                }

            } catch (NumberFormatException ex) {
                lblResultado.setText("ERROR: Monto inválido");
                lblResultado.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                mostrarError("Error", "Ingrese un monto válido (solo números)");
            } catch (Exception ex) {
                lblResultado.setText("ERROR INESPERADO");
                lblResultado.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                mostrarError("Error", "Error inesperado: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(
            lblTitulo,
            new Separator(),
            gridSeleccion,
            panelInfo,
            panelInversion
        );

        Scene scene = new Scene(root, 600, 750);
        stage.setScene(scene);
    }
    
    private String calcularROIEstimado(Proyecto proyecto) {
        double roi;
        switch (proyecto.calcularRiesgo().toLowerCase()) {
            case "bajo": roi = 15.0; break;
            case "medio": roi = 25.0; break;
            case "alto": roi = 40.0; break;
            default: roi = 20.0;
        }
        
        // Ajustar según el avance
        roi += (proyecto.getPorcentajeAvance() / 100.0) * 10;
        
        return String.format("%.1f%%", roi);
    }

    private String calcularTiempoRecuperacion(Proyecto proyecto) {
        int meses;
        switch (proyecto.getEstado().toLowerCase()) {
            case "activo":
            case "finalizado": meses = 6; break;
            case "beta": meses = 12; break;
            case "en desarrollo": meses = 18; break;
            case "planificacion": meses = 24; break;
            default: meses = 18;
        }

        return meses + " meses";
    }

    private String generarRecomendacion(Proyecto proyecto) {
        String riesgo = proyecto.calcularRiesgo().toLowerCase();
        double avance = proyecto.getPorcentajeAvance();

        if (riesgo.equals("bajo") && avance > 75) {
            return "ALTAMENTE RECOMENDADO - Proyecto maduro con bajo riesgo";
        } else if (riesgo.equals("medio") && avance > 50) {
            return "RECOMENDADO - Buen balance riesgo/retorno";
        } else if (riesgo.equals("alto") && avance < 25) {
            return "ALTO RIESGO - Solo para inversores experimentados";
        } else {
            return "EVALUAR CUIDADOSAMENTE - Revisar todos los factores";
        }
    }
}