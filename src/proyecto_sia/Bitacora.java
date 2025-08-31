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
        if (e.getTipoEmprendimiento().equals(tipo)) {
            emprendedores.put(e.getRut(), e);
            System.out.println("El emprendedor " + e.getNombre() + " fue agregado a la bitácora de " + tipo);
        } else {
            System.out.println("Error: El tipo del emprendedor no coincide con la bitácora de " + tipo);
        }
    }

    // Mostrar todos los emprendedores
    public void mostrarEmprendedores() {
        System.out.println("Bitácora: " + tipo);
        Object[] claves = emprendedores.keySet().toArray();
        int i = 0;
        while(i < claves.length) {
            Emprendedor e = emprendedores.get(claves[i]);
            e.mostrarInfo();
            System.out.println("------");
            i++;
        }
    }

    // Mostrar solo con capital mínimo
    public void mostrarEmprendedores(double capitalMinimo) {
        System.out.println("Bitácora: " + tipo + " (solo con capital >= $" + capitalMinimo + ")");
        Object[] claves = emprendedores.keySet().toArray();
        int i = 0;
        while(i < claves.length) {
            Emprendedor e = emprendedores.get(claves[i]);
            if(e.getCapital() >= capitalMinimo) {
                e.mostrarInfo();
                System.out.println("------");
            }
            i++;
        }
    }
}
