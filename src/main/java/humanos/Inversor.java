package humanos;

/**
 *
 * @author Leden
 */
import principal.Proyecto;

import java.util.ArrayList;
import java.util.List;

public class Inversor extends Persona{
    private double capitalDisponible;
    private List<Proyecto> historialInversiones;
    
     // Constructor sin parámetros
    public Inversor() {
        super(); 
        this.capitalDisponible = 0.0;
        this.historialInversiones = new ArrayList<>();
    }
        // Constructor con parámetros
    public Inversor(String nombre, String rut, String email, double capitalDisponible) {
        super(nombre, rut, email);
        this.capitalDisponible = capitalDisponible;
        this.historialInversiones = new ArrayList<>();
    }
    
    // Getters y Setters
    public double getCapitalDisponible() {
        return capitalDisponible;
    }

    public void setCapitalDisponible(double capitalDisponible) {
        this.capitalDisponible = capitalDisponible;
    }

    public List<Proyecto> getHistorialInversiones() {
        return historialInversiones;
    }

    
    // Método invertir en un proyecto
    public boolean invertir(Proyecto proyecto, double monto) {
        if (monto > 0 && monto <= capitalDisponible) {
            capitalDisponible -= monto; // descuenta capital
            proyecto.setInversion(proyecto.getInversion() + monto); // suma inversión al proyecto
            historialInversiones.add(proyecto); // guarda en historial
            return true;
        } else {
            System.out.println("No se puede invertir: monto inválido o insuficiente capital.");
            return false;
        }
    }

    // Listar inversiones
    public void listarInversiones() {
        if (historialInversiones.isEmpty()) {
            System.out.println("El inversor " + getNombre() + " no ha realizado inversiones.");
        } else {
            System.out.println("Historial de inversiones de " + getNombre() + ":");
            for (Proyecto p : historialInversiones) {
                System.out.println("- Proyecto: " + p.getNombre() + " | Inversión total: $" + p.getInversion());
            }
        }
    }

    // Sobrescritura de mostrarInfo()
    @Override
    public String mostrarInfo() {
        return "Inversor: " + getNombre() + " | RUT: " + getRut() + " | Email: " + getEmail() +
               " | Capital disponible: $" + capitalDisponible +
               " | Cantidad de proyectos invertidos: " + historialInversiones.size();
    }
    
}