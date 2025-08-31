package proyecto_sia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Proyecto_sia {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Bitácoras iniciales
        Bitacora bFarmacia = new Bitacora("Farmacia");
        Bitacora bTecnologia = new Bitacora("Tecnologia");
        Bitacora bComida = new Bitacora("Comida Rapida");

        // Proyecto
        Proyecto proyecto = new Proyecto("Registro Emprendedores");
        proyecto.agregarBitacora(bFarmacia);
        proyecto.agregarBitacora(bTecnologia);
        proyecto.agregarBitacora(bComida);

        int opcion = 0;

        while(opcion != 5) {
            System.out.println("\n=== MENÚ SISTEMA DE EMPRENDEDORES ===");
            System.out.println("1) Insertar nuevo emprendedor");
            System.out.println("2) Mostrar todas las bitácoras y emprendedores");
            System.out.println("3) Mostrar una bitácora específica");
            System.out.println("4) Mostrar emprendedores con capital mínimo (>= 10000)");
            System.out.println("5) Salir");
            System.out.print("Ingrese opción: ");

            opcion = Integer.parseInt(br.readLine());

            switch(opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = br.readLine();
                    System.out.print("RUT: ");
                    String rut = br.readLine();
                    System.out.print("Capital: ");
                    double capital = Double.parseDouble(br.readLine());
                    System.out.print("Tipo de emprendimiento (Farmacia/Tecnologia/Comida Rapida): ");
                    String tipo = br.readLine();

                    Emprendedor e = new Emprendedor(nombre, rut, capital, tipo);

                    boolean agregado = false;
                    int i = 0;
                    while(i < proyecto.getBitacoras().size()) {
                        Bitacora b = proyecto.getBitacoras().get(i);
                        if(b.getTipo().equalsIgnoreCase(tipo)) {
                            b.agregarEmprendedor(e);
                            agregado = true;
                            break;
                        }
                        i++;
                    }
                    if(!agregado) System.out.println("No existe bitácora para ese tipo.");
                    break;

                case 2:
                    int j = 0;
                    while(j < proyecto.getBitacoras().size()) {
                        proyecto.getBitacoras().get(j).mostrarEmprendedores();
                        j++;
                    }
                    break;

                case 3:
                    System.out.print("Ingrese el tipo de bitácora a mostrar: ");
                    String tipoBuscado = br.readLine();
                    proyecto.mostrarBitacoras(tipoBuscado);
                    break;

                case 4:
                    double capitalMinimo = 10000;
                    int k = 0;
                    while(k < proyecto.getBitacoras().size()) {
                        proyecto.getBitacoras().get(k).mostrarEmprendedores(capitalMinimo);
                        k++;
                    }
                    break;

                case 5:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción inválida, intente nuevamente.");
                    break;
            }
        }
    }
}
