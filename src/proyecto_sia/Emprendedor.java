package proyecto_sia;

public class Emprendedor {
    private String nombre;
    private String rut;
    private double capital;
    private String tipoEmprendimiento;

    // Constructor
    public Emprendedor(String nombre, String rut, double capital, String tipoEmprendimiento) {
        setNombre(nombre);
        setRut(rut);
        setCapital(capital);
        setTipoEmprendimiento(tipoEmprendimiento);
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getRut() { return rut; }
    public double getCapital() { return capital; }
    public String getTipoEmprendimiento() { return tipoEmprendimiento; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRut(String rut) { this.rut = rut; }
    public void setCapital(double capital) { if(capital >= 0) this.capital = capital; }
    public void setTipoEmprendimiento(String tipoEmprendimiento) { this.tipoEmprendimiento = tipoEmprendimiento; }

    // Mostrar información
    public void mostrarInfo() {
        System.out.println("Nombre: " + nombre);
        System.out.println("RUT: " + rut);
        System.out.println("Capital: $" + String.format("%.2f", capital));
        System.out.println("Tipo de emprendimiento: " + tipoEmprendimiento);
    }
}
