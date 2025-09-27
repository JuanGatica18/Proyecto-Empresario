package ventanas;

import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import principal.Sistema;

public abstract class VentanaBase {
    protected Sistema sistema;
    protected Stage stage;

    public VentanaBase(Sistema sistema, String titulo) {
        this.sistema = sistema;
        this.stage = new Stage();
        this.stage.setTitle(titulo);
    }

    public void mostrar() {
        stage.show();
    }

    protected void cerrar() {
        stage.close();
    }

    protected void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    protected void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    protected abstract void configurarVentana();
}