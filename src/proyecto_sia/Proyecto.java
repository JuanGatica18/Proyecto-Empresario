package proyecto_sia;

import javax.swing.*;       // Para JFrame, JOptionPane, JTextArea, JScrollPane
import java.awt.*;          // Para Dimension
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

    // Devuelve todas las bitácoras como String
    public String mostrarBitacorasString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proyecto: ").append(getNombreProyecto()).append("\n==============================\n");
        for(Bitacora b : getBitacoras()) {
            sb.append("== ").append(b.getTipo()).append(" ==\n");
            sb.append(b.getEmprendedoresString()).append("\n");
        }
        return sb.toString();
    }

    // Devuelve solo una bitácora específica como String
    public String mostrarBitacorasString(String tipo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Proyecto: ").append(getNombreProyecto()).append(" (filtrado por ").append(tipo).append(")\n");
        sb.append("==============================\n");
        for(Bitacora b : getBitacoras()) {
            if(b.getTipo().equalsIgnoreCase(tipo)) {
                sb.append(b.getEmprendedoresString()).append("\n");
            }
        }
        return sb.toString();
    }

    // Muestra bitácora en un JOptionPane
    public void mostrarBitacorasSwing(String tipo, JFrame frame) {
        String contenido = mostrarBitacorasString(tipo);
        if(contenido.trim().isEmpty()) contenido = "No se encontró la bitácora " + tipo;
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(frame, scrollPane, "Bitácora: " + tipo, JOptionPane.INFORMATION_MESSAGE);
    }
}