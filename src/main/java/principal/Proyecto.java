package principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Proyecto {
    private String nombre;
    private String descripcion;
    private double inversion;
    private String estado;
    private String tipo;
    private LocalDate fechaCreacion;
    private LocalDate fechaEstimadaFinalizacion;
    private String sector;
    private int empleadosRequeridos;
    private String ubicacion;
    private double porcentajeAvance;
    
    // Constructor completo
    public Proyecto(String nombre, String descripcion, double inversion, String estado, String tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inversion = inversion;
        this.estado = estado;
        this.tipo = tipo;
        this.fechaCreacion = LocalDate.now();
        this.fechaEstimadaFinalizacion = LocalDate.now().plusMonths(12); // 1 año por defecto
        this.sector = tipo; // Por defecto igual al tipo
        this.empleadosRequeridos = calcularEmpleadosSegunInversion();
        this.ubicacion = "Por definir";
        this.porcentajeAvance = calcularAvanceSegunEstado();
    }

    // Constructor con datos adicionales
    public Proyecto(String nombre, String descripcion, double inversion, String estado, String tipo,
                    String sector, int empleadosRequeridos, String ubicacion) {
        this(nombre, descripcion, inversion, estado, tipo);
        this.sector = sector;
        this.empleadosRequeridos = empleadosRequeridos;
        this.ubicacion = ubicacion;
    }

    public Proyecto(String csvString) {
        String[] parts = csvString.split("~");
        if (parts.length >= 5) {
            this.nombre = parts[0];
            this.descripcion = parts[1];
            try {
                this.inversion = Double.parseDouble(parts[2]);
            } catch (NumberFormatException e) {
                this.inversion = 0.0;
            }
            this.estado = parts[3];
            this.tipo = parts[4];
            
            // Campos adicionales opcionales
            this.fechaCreacion = LocalDate.now();
            this.fechaEstimadaFinalizacion = LocalDate.now().plusMonths(12);
            this.sector = tipo;
            this.empleadosRequeridos = calcularEmpleadosSegunInversion();
            this.ubicacion = "Por definir";
            this.porcentajeAvance = calcularAvanceSegunEstado();
        }
    }

    // Métodos auxiliares para calcular valores automáticos
    private int calcularEmpleadosSegunInversion() {
        if (inversion < 5000) return 1;
        else if (inversion < 15000) return 3;
        else if (inversion < 30000) return 5;
        else return 8;
    }
    
    private double calcularAvanceSegunEstado() {
        switch (estado.toLowerCase()) {
            case "planificacion": case "planificación": return 10.0;
            case "en desarrollo": return 45.0;
            case "beta": case "pruebas": return 75.0;
            case "activo": case "finalizado": return 100.0;
            default: return 0.0;
        }
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public double getInversion() { return inversion; }
    public void setInversion(double inversion) { 
        this.inversion = inversion;
        this.empleadosRequeridos = calcularEmpleadosSegunInversion(); // Recalcular
    }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { 
        this.estado = estado;
        this.porcentajeAvance = calcularAvanceSegunEstado(); // Recalcular
    }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDate getFechaEstimadaFinalizacion() { return fechaEstimadaFinalizacion; }
    public void setFechaEstimadaFinalizacion(LocalDate fechaEstimadaFinalizacion) { 
        this.fechaEstimadaFinalizacion = fechaEstimadaFinalizacion; 
    }
    
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    
    public int getEmpleadosRequeridos() { return empleadosRequeridos; }
    public void setEmpleadosRequeridos(int empleadosRequeridos) { this.empleadosRequeridos = empleadosRequeridos; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public double getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(double porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public String toCSVString() {
        return nombre + "~" + descripcion + "~" + inversion + "~" + estado + "~" + tipo;
    }
    
    public String calcularRiesgo() {
        // Riesgo basado en múltiples factores
        double factorInversion = inversion < 1000 ? 3 : (inversion < 10000 ? 2 : 1);
        double factorAvance = porcentajeAvance < 25 ? 3 : (porcentajeAvance < 75 ? 2 : 1);
        double factorEmpleados = empleadosRequeridos > 5 ? 2 : 1;
        
        double riesgoTotal = (factorInversion + factorAvance + factorEmpleados) / 3.0;
        
        if (riesgoTotal >= 2.5) return "Alto";
        else if (riesgoTotal >= 1.7) return "Medio";
        else return "Bajo";
    }
    
    public String mostrarResumen() {
        return String.format("%s (%s) - Inv: $%,.2f - Avance: %.1f%%", 
                           nombre, estado, inversion, porcentajeAvance);
    }
    
    public String mostrarInfoCompleta() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format(
            "=== INFORMACIÓN DETALLADA DEL PROYECTO ===\n" +
            "Nombre: %s\n" +
            "Descripción: %s\n" +
            "Tipo/Sector: %s\n" +
            "Estado: %s (%.1f%% completado)\n" +
            "Inversión: $%,.2f\n" +
            "Riesgo: %s\n" +
            "Empleados requeridos: %d\n" +
            "Ubicación: %s\n" +
            "Fecha creación: %s\n" +
            "Fecha estimada finalización: %s\n",
            nombre, descripcion, sector, estado, porcentajeAvance, 
            inversion, calcularRiesgo(), empleadosRequeridos, ubicacion,
            fechaCreacion.format(formatter), 
            fechaEstimadaFinalizacion.format(formatter)
        );
    }
    
    @Override
    public String toString() {
        return String.format("%s - $%,.0f (%s)", nombre, inversion, estado);
    }
}