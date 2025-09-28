package humanos;

/**
 *
 * @author Leden
 */

import principal.Proyecto;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Emprendedor extends Persona{
    
    private double capital;
    private List<Proyecto> proyectos;
    
    // constructor vacío
    public Emprendedor(String nombre, String rut, String email, double capital, String tipoBitacora) { 
        super(nombre, rut, email); 
        this.capital = capital;
        this.proyectos = new ArrayList<>();
    }

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
    // 🆕 Método para exportar a CSV
    public String toCSVString() {
        // Generar la cadena de proyectos separada por |
        String proyectosStr = proyectos.stream()
            .map(Proyecto::toCSVString)
            .collect(Collectors.joining("|"));
        
        // Formato: nombre,rut,email,capital,"proyectos"
        return String.format("%s,%s,%s,%.2f,\"%s\"", 
                             getNombre(), getRut(), getEmail(), capital, proyectosStr);
    }
    // Sobrescritura de mostrarInfo()
    @Override
    public String mostrarInfo() {
        return super.mostrarInfo() +
                ", Capital: " + capital +
                ", Proyectos: " + proyectos.size();
    }
}