package proyecto_sia;

import javax.swing.*;       // JFrame, JButton, JOptionPane, JTextArea, JScrollPane
import java.awt.*;          // FlowLayout, Dimension

public class Proyecto_sia extends Excepciones{

    public static void main(String[] args) {
        // Crear bitácoras iniciales
        Bitacora bFarmacia = new Bitacora("Farmacia");
        Bitacora bTecnologia = new Bitacora("Tecnologia");
        Bitacora bComida = new Bitacora("Comida Rapida");

        Proyecto proyecto = new Proyecto("Registro Emprendedores");
        proyecto.agregarBitacora(bFarmacia);
        proyecto.agregarBitacora(bTecnologia);
        proyecto.agregarBitacora(bComida);

        // Crear ventana
        JFrame frame = new JFrame("Sistema de Emprendedores");
        frame.setSize(400, 350);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Botones
        JButton btnInsertar = new JButton("1) Insertar nuevo emprendedor");
        JButton btnMostrarTodo = new JButton("2) Mostrar todas las bitácoras y emprendedores");
        JButton btnMostrarEspecifico = new JButton("3) Mostrar una bitácora específica");
        JButton btnCapitalMin = new JButton("4) Mostrar emprendedores (Capital >= 10000)");
        
        frame.setLayout(null);
        
        //Tamaño y posición de los botones
        btnInsertar.setBounds(10, 50, 370, 50);
        btnMostrarTodo.setBounds(10, 105, 370, 50);
        btnMostrarEspecifico.setBounds(10, 160, 370, 50);
        btnCapitalMin.setBounds(10, 215, 370, 50);
        
        //Se agregan los botones a la ventana
        frame.add(btnInsertar);
        frame.add(btnMostrarTodo);
        frame.add(btnMostrarEspecifico);
        frame.add(btnCapitalMin);

        // --- ACCIONES ---

        // 1) Insertar nuevo emprendedor
        btnInsertar.addActionListener(e -> {
            try {
            	String nombre = JOptionPane.showInputDialog(frame, "Nombre:");
            	if(nombre == null || nombre.isEmpty()) return;
            	
                String rut = JOptionPane.showInputDialog(frame, "RUT:");
                if(rut == null || rut.isEmpty()) return;
                  
                String capStr = JOptionPane.showInputDialog(frame, "Capital:");
                double capital = Double.parseDouble(capStr);

                String tipo = JOptionPane.showInputDialog(frame, "Tipo de emprendimiento (Farmacia/Tecnologia/Comida Rapida):");
                if(tipo == null || tipo.isEmpty()) return;

                Emprendedor eNuevo = new Emprendedor(nombre, rut, capital, tipo);

                boolean agregado = false;
                for(Bitacora b : proyecto.getBitacoras()) {
                    if(b.getTipo().equalsIgnoreCase(tipo)) {
                        b.agregarEmprendedor(eNuevo);
                        agregado = true;
                        break;
                    }
                }
                if(!agregado) JOptionPane.showMessageDialog(frame, "No existe bitácora para ese tipo.");
                else JOptionPane.showMessageDialog(frame, "Emprendedor agregado correctamente.");
            } catch (Excepciones.RutInvalidoException errorRut) {
            	JOptionPane.showMessageDialog(frame, "Rut inválido.");
            	
            } catch(Excepciones.CapitalInvalidoException errorCapital) {
                JOptionPane.showMessageDialog(frame, "Capital inválido.");
            } 
        });

        // 2) Mostrar todas las bitácoras y emprendedores
        btnMostrarTodo.addActionListener(e -> {
            String contenido = proyecto.mostrarBitacorasString();
            JTextArea textArea = new JTextArea(contenido);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            JOptionPane.showMessageDialog(frame, scrollPane, "Todas las bitácoras", JOptionPane.INFORMATION_MESSAGE);
        });

        // 3) Mostrar una bitácora específica
        btnMostrarEspecifico.addActionListener(e -> {
            String tipoBuscado = JOptionPane.showInputDialog(frame, "Ingrese el tipo de bitácora a mostrar:");
            if(tipoBuscado != null && !tipoBuscado.isEmpty()) {
                proyecto.mostrarBitacorasSwing(tipoBuscado, frame);
            }
        });

        // 4) Mostrar emprendedores con capital >= 10000
        btnCapitalMin.addActionListener(e -> {
            double capitalMin = 10000;
            StringBuilder sb = new StringBuilder();
            for(Bitacora b : proyecto.getBitacoras()) {
                sb.append("== ").append(b.getTipo()).append(" ==\n");
                sb.append(b.getEmprendedoresConCapital(capitalMin)).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            JOptionPane.showMessageDialog(frame, scrollPane, "Emprendedores con capital >= 10000", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }
}