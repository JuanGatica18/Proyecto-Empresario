package principal;

import humanos.Emprendedor;
import java.util.List;
import java.util.ArrayList;

public class Bitacora {
    private String tipo;
    private List<Emprendedor> emprendedores; //SIA1.5: 2da colección anidada
    
    public Bitacora(String tipo) {
        this.tipo = tipo;
        this.emprendedores = new ArrayList<>();
    }

    // 
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public List<Emprendedor> getEmprendedores() { return emprendedores; }

    public void agregarEmprendedor(Emprendedor e) {
        emprendedores.add(e);
    }

    // SIA1.6: SOBRECARGA - buscar por RUT
    public Emprendedor buscar(String rut) {
        for (Emprendedor e : emprendedores) {
            if (e.getRut().equals(rut)) {
                return e;
            }
        }
        return null;
    }

    // SIA1.6: SOBRECARGA - buscar por capital mínimo
    public List<Emprendedor> buscar(double capitalMin) {
        List<Emprendedor> filtrados = new ArrayList<>();
        for (Emprendedor e : emprendedores) {
            if (e.getCapital() >= capitalMin) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }

    //SIA1.6: SOBRECARGA - buscar por cantidad de proyectos
    public List<Emprendedor> buscar(int minProyectos) {
        List<Emprendedor> filtrados = new ArrayList<>();
        for (Emprendedor e : emprendedores) {
            if (e.getProyectos().size() >= minProyectos) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }

}
