package principal;


/**
 *
 * @author Leden
 */
import humanos.Emprendedor;
import java.util.List;
import java.util.ArrayList;

public class Bitacora {
    private String tipo;
    private List<Emprendedor> emprendedores;
    
    
    
    // Constructor
    public Bitacora(String tipo) {
        this.tipo = tipo;
        this.emprendedores = new ArrayList<>();
    }

    // Getters y setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Emprendedor> getEmprendedores() {
        return emprendedores;
    }

    // Métodos principales
    public void agregarEmprendedor(Emprendedor e) {
        emprendedores.add(e);
    }

    public void listarEmprendedores() {
        System.out.println("Bitácora de tipo: " + tipo);
        for (Emprendedor e : emprendedores) {
            System.out.println("- " + e.mostrarInfo());
        }
    }

    // Sobrecarga: buscar por RUT
    public Emprendedor buscar(String rut) {
        for (Emprendedor e : emprendedores) {
            if (e.getRut().equals(rut)) {
                return e;
            }
        }
        return null;
    }

    // Sobrecarga: buscar por capital mínimo
    public List<Emprendedor> buscar(double capitalMin) {
        List<Emprendedor> filtrados = new ArrayList<>();
        for (Emprendedor e : emprendedores) {
            if (e.getCapital() >= capitalMin) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }

    // Método extra: obtener todos con capital mínimo
    public List<Emprendedor> getEmprendedoresConCapital(double min) {
        return buscar(min);
    }
    
    
}