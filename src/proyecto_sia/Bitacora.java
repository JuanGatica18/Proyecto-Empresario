package proyecto_sia;

import java.util.HashMap;

public class Bitacora {
    private String tipo;
    private HashMap<String, Emprendedor> emprendedores;

    // Contructor
    public Bitacora(String tipo) {
        setTipo(tipo);
        setEmprendedores(new HashMap<String, Emprendedor>());
    }

    // Getters
    public String getTipo() { return tipo; }
    public HashMap<String, Emprendedor> getEmprendedores() { return emprendedores; }

    // Setters
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setEmprendedores(HashMap<String, Emprendedor> emprendedores) { this.emprendedores = emprendedores; }

    // Agregar emprendedor
    public void agregarEmprendedor(Emprendedor e) {
        if (e.getTipoEmprendimiento().equalsIgnoreCase(tipo)) {
            emprendedores.put(e.getRut(), e);
            System.out.println("El emprendedor " + e.getNombre() + " fue agregado a la bitácora de " + tipo);
        } else {
            System.out.println("Error: El tipo del emprendedor no coincide con la bitácora de " + tipo);
        }
    }

    // Devuelve todos los emprendedores como String
    public String getEmprendedoresString() {
        if(emprendedores.isEmpty()) return "No hay emprendedores registrados.\n";
        StringBuilder sb = new StringBuilder();
        for(Emprendedor e : emprendedores.values()) {
            sb.append(e.toString()).append("\n------\n");
        }
        return sb.toString();
    }

    // Devuelve solo emprendedores con capital mínimo como String
    public String getEmprendedoresConCapital(double capitalMinimo) {
        StringBuilder sb = new StringBuilder();
        for(Emprendedor e : emprendedores.values()) {
            if(e.getCapital() >= capitalMinimo) {
                sb.append(e.toString()).append("\n------\n");
            }
        }
        if(sb.length() == 0) sb.append("No hay emprendedores con capital >= " + capitalMinimo + "\n");
        return sb.toString();
    }
}