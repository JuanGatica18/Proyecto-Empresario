package proyecto_sia;

public class Emprendedor {
    private String nombre;
    private String rut;
    private double capital;
    private String tipoEmprendimiento;

    // Constructor
    public Emprendedor(String nombre, String rut, double capital, String tipoEmprendimiento) 
    		throws Excepciones.RutInvalidoException, Excepciones.CapitalInvalidoException {
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
    
    public void setRut(String rut) throws Excepciones.RutInvalidoException {
    	int rutInt = Integer.parseInt(rut);
    	if (rutInt < 0) {
            throw new Excepciones.RutInvalidoException("El RUT no puede ser negativo");
    	}
    	if (rut.length() == 8 || rut.length() == 9) this.rut = rut;
    	else throw new Excepciones.RutInvalidoException("El rut no puede tener menos de 8 o más 9 dígitos.");
    	}
    
    public void setCapital(double capital) throws Excepciones.CapitalInvalidoException { 
    	if(capital > 0) this.capital = capital; 
    	else throw new Excepciones.CapitalInvalidoException("El capital no puede ser negativo.");
    	}
    
    public void setTipoEmprendimiento(String tipoEmprendimiento) { this.tipoEmprendimiento = tipoEmprendimiento; }

    // Para GUI
    @Override
    public String toString() {
        return "Nombre: " + nombre +
               "\nRUT: " + rut +
               "\nCapital: $" + String.format("%.2f", capital) +
               "\nTipo de emprendimiento: " + tipoEmprendimiento;
    }
}
