package humanos;

import principal.Proyecto;
import java.util.ArrayList;
import java.util.List;

public class Inversor extends Persona{
    private double capitalDisponible;
    private List<Proyecto> historialInversiones; 
    
    public Inversor(String nombre, String rut, String email, double capitalDisponible) {
        super(nombre, rut, email);
        this.capitalDisponible = capitalDisponible;
        this.historialInversiones = new ArrayList<>();
    }
    
    public Inversor(String nombre, String rut, String email, double capitalDisponible, List<Proyecto> historial) {
        super(nombre, rut, email);
        this.capitalDisponible = capitalDisponible;
        this.historialInversiones = historial != null ? historial : new ArrayList<>();
    }
    
    public double getCapitalDisponible() { return capitalDisponible; }
    public void setCapitalDisponible(double capitalDisponible) { this.capitalDisponible = capitalDisponible; }
    public List<Proyecto> getHistorialInversiones() { return historialInversiones; }

    
    public boolean invertir(Proyecto proyecto, double monto) {
        if (monto > 0 && monto <= capitalDisponible) {
            capitalDisponible -= monto; // Reducir capital del inversor
            proyecto.setInversion(proyecto.getInversion() + monto); // Aumentar inversión del proyecto
            historialInversiones.add(proyecto);
            return true;
        }
        return false;
    }

    // 🆕 Método para exportar a CSV
    public String toCSVString() {
        // Formato: nombre,rut,email,capitalDisponible
        return String.format("%s,%s,%s,%.2f", 
                             getNombre(), getRut(), getEmail(), capitalDisponible);
    }
    
    @Override
    public String toString() {
        // Utilizado en ComboBox de VentanaInversion
        return getNombre() + " (RUT: " + getRut() + " - Capital: $" + String.format("%,.2f", capitalDisponible) + ")";
    }

    @Override
    public String mostrarInfo() {
        return "Inversor: " + getNombre() + " | RUT: " + getRut() + 
               " | Email: " + getEmail() + " | Capital disponible: $" + String.format("%,.2f", capitalDisponible);
    }
}