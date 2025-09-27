package principal;

/**
 *
 * @author Leden
 */
public class Proyecto {
    private String nombre;
    private String descripcion;
    private double inversion;
    private String estado;
    private String tipo;
    
    
    // Constructor vacío
    public Proyecto() {
        this.nombre = "Sin nombre";
        this.descripcion = "Sin descripcion";
        this.inversion = 0.0;
        this.estado = "En progreso";
        this.tipo = "General";
    }

    // Constructor con parámetros
    public Proyecto(String nombre, String descripcion, double inversion, String estado, String tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inversion = inversion;
        this.estado = estado;
        this.tipo = tipo;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getInversion() {
        return inversion;
    }

    public void setInversion(double inversion) {
        this.inversion = inversion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Método que calcula el riesgo en base al capital invertido
    public String calcularRiesgo() {
        if (inversion < 1000) {
            return "Alto";
        } else if (inversion < 10000) {
            return "Medio";
        } else {
            return "Bajo";
        }
    }

    // Mostrar resumen
    public String mostrarResumen() {
        return "Proyecto: " + nombre + 
               " | Tipo: " + tipo +
               " | Estado: " + estado +
               " | Inversión total: $" + inversion +
               " | Riesgo: " + calcularRiesgo();
    }
}
