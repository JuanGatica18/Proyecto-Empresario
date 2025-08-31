package proyecto_sia;

import java.util.ArrayList;

public class Proyecto {
    private String nombreProyecto;
    private ArrayList<Bitacora> bitacoras;

    // Constructor
    public Proyecto(String nombreProyecto) {
        setNombreProyecto(nombreProyecto);
        setBitacoras(new ArrayList<Bitacora>());
    }

    // Getters
    public String getNombreProyecto() { return nombreProyecto; }
    public ArrayList<Bitacora> getBitacoras() { return bitacoras; }

    // Setters
    public void setNombreProyecto(String nombreProyecto) { this.nombreProyecto = nombreProyecto; }
    public void setBitacoras(ArrayList<Bitacora> bitacoras) { this.bitacoras = bitacoras; }

    // Agregar bitácora
    public void agregarBitacora(Bitacora b) { getBitacoras().add(b); }

    // Mostrar todas las bitácoras
    public void mostrarBitacoras() {
        System.out.println("Proyecto: " + getNombreProyecto());
        System.out.println("==============================");
        int i = 0;
        while(i < getBitacoras().size()) {
            getBitacoras().get(i).mostrarEmprendedores();
            i++;
        }
    }

    // Mostrar solo una bitácora específica
    public void mostrarBitacoras(String tipo) {
        System.out.println("Proyecto: " + getNombreProyecto() + " (filtrado por " + tipo + ")");
        System.out.println("==============================");
        int i = 0;
        while(i < getBitacoras().size()) {
            if(getBitacoras().get(i).getTipo().equals(tipo)) {
                getBitacoras().get(i).mostrarEmprendedores();
            }
            i++;
        }
    }
}
