package humanos;

/**
 *
 * @author Leden
 */
public class Persona {
    private String nombre;
    private String rut;
    private String email;
    
    // constructor sin parámetros
    public Persona(){
        this.nombre = "NUll";
        this.rut = "99999999-9";
        this.email = "NULL@gmail.com";
    }
    
    
    
    // Constructor con parámetros
    public Persona(String nombre, String rut, String email) {
        this.nombre = nombre;
        this.rut = rut;
        this.email = email;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Métodos principales
    public String mostrarInfo() {
        return "Nombre: " + nombre + ", RUT: " + rut + ", Email: " + email;
    }

    public String saludar() {
        return "Hola, soy " + nombre;
    }
}