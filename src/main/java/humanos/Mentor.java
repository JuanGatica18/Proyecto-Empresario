package humanos;

/**
 *
 * @author Leden
 */

import java.util.List;
import java.util.ArrayList;

public class Mentor extends Persona{
    private String especialidad;
    private List<Emprendedor> emprendedoresAsesorados;
    private String aniosExperiencia;
    
    
        // Constructor vacío
    public Mentor() {
        super(); 
        this.especialidad = "Sin especialidad";
        this.aniosExperiencia = "0";
        this.emprendedoresAsesorados = new ArrayList<>();
    }
    
        // Constructor con parámetros
    public Mentor(String nombre, String rut, String email, String especialidad,String aniosExperiencia) {
        super(nombre, rut, email);
        this.especialidad = especialidad;
        this.aniosExperiencia = aniosExperiencia;
        this.emprendedoresAsesorados = new ArrayList<>();
    }
    
        // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(String aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public List<Emprendedor> getEmprendedoresAsesorados() {
        return emprendedoresAsesorados;
    }
    
        // Métodos para manejar emprendedores asesorados
    public void agregarEmprendedor(Emprendedor e) {
        if (!emprendedoresAsesorados.contains(e)) {
            emprendedoresAsesorados.add(e);
        }
    }

    public void listarEmprendedores() {
        System.out.println("Mentor " + getNombre() + " asesora a:");
        for (Emprendedor e : emprendedoresAsesorados) {
            System.out.println("- " + e.getNombre() + " (RUT: " + e.getRut() + ")");
        }
    }

    // Sobrescritura de mostrarInfo()
    @Override
    public String mostrarInfo() {
        return "Mentor: " + getNombre() + " | RUT: " + getRut() + " | Email: " + getEmail() +
               " | Especialidad: " + especialidad +
               " | Años de experiencia: " + aniosExperiencia +
               " | Emprendedores asesorados: " + emprendedoresAsesorados.size();
    }
}