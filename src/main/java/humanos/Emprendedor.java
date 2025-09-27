package humanos;

/**
 *
 * @author Leden
 */

import principal.Proyecto;
import java.util.List;
import java.util.ArrayList;

public class Emprendedor extends Persona{
    
    private double capital;
    private List<Proyecto> proyectos;
    
    // constructor vacío
    public Emprendedor() {
        super();
        this.capital = 0.0;
        this.proyectos = new ArrayList<>();
    }

    // Constructor con parámetros
    public Emprendedor(String nombre, String rut, String email, double capital) {
        super(nombre, rut, email); 
        this.capital = capital;
        this.proyectos = new ArrayList<>();
    }

    // Getters y Setters
    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    // Métodos propios
    public void registrarProyecto(Proyecto p) {
        proyectos.add(p);
    }

    public String listarProyectos() {
        if (proyectos.isEmpty()) {
            return "No tiene proyectos registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Proyecto p : proyectos) {
            sb.append(p.mostrarResumen()).append("\n");
        }
        return sb.toString();
    }

    // Sobrescritura de mostrarInfo()
    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() +
                ", Capital: " + capital +
                ", Proyectos: " + proyectos.size();
    }
}