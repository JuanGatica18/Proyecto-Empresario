package ventanas;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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

        Label lblTitulo = new Label("INVERSION EN PROYECTOS DE EMPRENDEDORES");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Seleccion de inversor
        Label lblInversor = new Label("Seleccionar Inversor:");
        ComboBox<Inversor> cbInversores = new ComboBox<>();
        // Cargar datos reales de Inversores
        sistema.getInversores().forEach(cbInversores.getItems()::add);
        cbInversores.setPromptText("Seleccione un Inversor");

        // Seleccion de emprendedor
        Label lblEmprendedor = new Label("Seleccionar Emprendedor:");
        ComboBox<Emprendedor> cbEmprendedores = new ComboBox<>();
        sistema.getMapaEmprendedores().values().forEach(cbEmprendedores.getItems()::add);
        cbEmprendedores.setPromptText("Seleccione un Emprendedor");

        // Seleccion de proyecto
        Label lblProyecto = new Label("Seleccionar Proyecto:");
        ComboBox<Proyecto> cbProyectos = new ComboBox<>();
        cbProyectos.setPromptText("Seleccione un Proyecto");
        
        TextArea taInfoProyecto = new TextArea();
        taInfoProyecto.setEditable(false);
        taInfoProyecto.setPromptText("Detalles del Proyecto seleccionado");
        taInfoProyecto.setPrefHeight(100);

        // Logica para actualizar ComboBox de Proyectos (Depende de Emprendedor)
        cbEmprendedores.setOnAction(e -> {
            Emprendedor seleccionado = cbEmprendedores.getSelectionModel().getSelectedItem();
            cbProyectos.getItems().clear();
            taInfoProyecto.clear();
            if (seleccionado != null) {
                cbProyectos.getItems().addAll(seleccionado.getProyectos());
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

        Label lblMonto = new Label("Monto a Invertir:");
        TextField tfMonto = new TextField();
        tfMonto.setPromptText("Ingrese el monto (ej: 1000.0)");

        Button btnInvertir = new Button("Realizar Inversion");
        btnInvertir.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white;");
        Label lblResultado = new Label("");

        btnInvertir.setOnAction(e -> {
            try {
                Inversor inversor = cbInversores.getSelectionModel().getSelectedItem();
                Proyecto proyecto = cbProyectos.getSelectionModel().getSelectedItem();
                String montoTexto = tfMonto.getText().trim();

                if (inversor == null || proyecto == null) {
                    mostrarError("Error", "Debe seleccionar un inversor y un proyecto");
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
                    lblResultado.setText("Inversion exitosa! " + 
                        inversor.getNombre() + " invirtio $" + String.format("%,.2f", monto) + " en " + proyecto.getNombre());
                    mostrarMensaje("Exito", "Inversion realizada correctamente.\nCapital restante: $" + String.format("%,.2f", inversor.getCapitalDisponible()));
                    
                    // Forzar actualizacion de las listas para reflejar el nuevo capital
                    cbInversores.getSelectionModel().clearSelection();
                    cbInversores.getItems().clear();
                    sistema.getInversores().forEach(cbInversores.getItems()::add);
                    
                    tfMonto.clear();
                    taInfoProyecto.setText("Inversion actualizada. Seleccione nuevamente el proyecto para ver el nuevo monto.");
                    
                } else {
                    mostrarError("Error", "No se pudo realizar la inversion. Capital disponible insuficiente: $" + String.format("%,.2f", inversor.getCapitalDisponible()));
                }

            } catch (NumberFormatException ex) {
                mostrarError("Error", "Ingrese un monto valido");
            } catch (Exception ex) {
                mostrarError("Error", "Error inesperado: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(
            lblTitulo, lblInversor, cbInversores, 
            lblEmprendedor, cbEmprendedores, lblProyecto, cbProyectos,
            taInfoProyecto, lblMonto, tfMonto, btnInvertir, lblResultado
        );

        Scene scene = new Scene(root, 500, 650);
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