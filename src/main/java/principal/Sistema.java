package principal;

import humanos.Emprendedor;
import humanos.Mentor;
import humanos.Inversor;
import principal.Bitacora;
import excepciones.DatosInvalidosException;
import excepciones.ElementoNoEncontradoException;


import java.io.*;
import java.util.*;

public class Sistema {

    private List<Bitacora> bitacoras;
    private Map<String, Emprendedor> mapaEmprendedores;
    private List<Mentor> mentores;
    private List<Inversor> inversores;

    // Constructor CORREGIDO - Inicializa bitácoras (SIA1.4)
    public Sistema() {
        bitacoras = new ArrayList<>();
        mapaEmprendedores = new HashMap<>();
        mentores = new ArrayList<>();
        inversores = new ArrayList<>();
        
        // ✅ INICIALIZAR BITÁCORAS (Datos iniciales requeridos)
        inicializarBitacoras();
    }
    
    private void inicializarBitacoras() {
        String[] tiposBitacora = {"Tecnología", "Salud", "Educación", "Alimentos", "Manufactura"};
        for (String tipo : tiposBitacora) {
            bitacoras.add(new Bitacora(tipo));
        }
    }

    // ================== GETTERS ==================
    public List<Bitacora> getBitacoras() {
        return bitacoras;
    }

    public Map<String, Emprendedor> getMapaEmprendedores() {
        return mapaEmprendedores;
    }

    public List<Mentor> getMentores() {
        return mentores;
    }

    public List<Inversor> getInversores() {
        return inversores;
    }

    // ================== AGREGAR CON VALIDACIÓN ==================
    public void agregarBitacora(Bitacora b) {
        bitacoras.add(b);
    }

    public void agregarEmprendedor(String tipo, Emprendedor e) throws DatosInvalidosException {
        // ✅ Validación de datos (SIA2.8)
        if (e.getRut() == null || e.getRut().trim().isEmpty()) {
            throw new DatosInvalidosException("El RUT no puede estar vacío");
        }
        if (e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío");
        }
        
        boolean bitacoraEncontrada = false;
        for (Bitacora b : bitacoras) {
            if (b.getTipo().equalsIgnoreCase(tipo)) {
                b.agregarEmprendedor(e);
                mapaEmprendedores.put(e.getRut(), e);
                bitacoraEncontrada = true;
                break;
            }
        }
        
        if (!bitacoraEncontrada) {
            throw new DatosInvalidosException("No existe bitácora para el tipo: " + tipo);
        }
    }

    public void agregarMentor(Mentor m) throws DatosInvalidosException {
        if (m.getRut() == null || m.getRut().trim().isEmpty()) {
            throw new DatosInvalidosException("El RUT del mentor no puede estar vacío");
        }
        mentores.add(m);
    }

    public void agregarInversor(Inversor i) throws DatosInvalidosException {
        if (i.getRut() == null || i.getRut().trim().isEmpty()) {
            throw new DatosInvalidosException("El RUT del inversor no puede estar vacío");
        }
        inversores.add(i);
    }

    // ================== ELIMINAR ELEMENTOS (SIA2.4, SIA2.12) ==================
    public void eliminarEmprendedor(String rut) throws ElementoNoEncontradoException {
        Emprendedor e = mapaEmprendedores.remove(rut);
        if (e == null) {
            throw new ElementoNoEncontradoException("Emprendedor con RUT " + rut + " no encontrado");
        }
        
        // Remover de todas las bitácoras
        for (Bitacora b : bitacoras) {
            b.getEmprendedores().removeIf(emp -> emp.getRut().equals(rut));
        }
    }

    public void eliminarMentor(String rut) throws ElementoNoEncontradoException {
        boolean removido = mentores.removeIf(m -> m.getRut().equals(rut));
        if (!removido) {
            throw new ElementoNoEncontradoException("Mentor con RUT " + rut + " no encontrado");
        }
    }

    public void eliminarInversor(String rut) throws ElementoNoEncontradoException {
        boolean removido = inversores.removeIf(i -> i.getRut().equals(rut));
        if (!removido) {
            throw new ElementoNoEncontradoException("Inversor con RUT " + rut + " no encontrado");
        }
    }

    // ================== BUSQUEDAS MEJORADAS (SIA2.13) ==================
    public Emprendedor buscarEmprendedorPorRut(String rut) throws ElementoNoEncontradoException {
        Emprendedor e = mapaEmprendedores.get(rut);
        if (e == null) {
            throw new ElementoNoEncontradoException("Emprendedor con RUT " + rut + " no encontrado");
        }
        return e;
    }

    public Mentor buscarMentorPorRut(String rut) throws ElementoNoEncontradoException {
        for (Mentor m : mentores) {
            if (m.getRut().equals(rut)) return m;
        }
        throw new ElementoNoEncontradoException("Mentor con RUT " + rut + " no encontrado");
    }

    public Inversor buscarInversorPorRut(String rut) throws ElementoNoEncontradoException {
        for (Inversor i : inversores) {
            if (i.getRut().equals(rut)) return i;
        }
        throw new ElementoNoEncontradoException("Inversor con RUT " + rut + " no encontrado");
    }

    // ================== FILTRADO (SIA2.5) ==================
    public List<Emprendedor> filtrarEmprendedoresPorCapital(double capitalMinimo) {
        List<Emprendedor> resultado = new ArrayList<>();
        for (Emprendedor e : mapaEmprendedores.values()) {
            if (e.getCapital() >= capitalMinimo) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Emprendedor> filtrarEmprendedoresPorBitacora(String tipoBitacora) {
        for (Bitacora b : bitacoras) {
            if (b.getTipo().equalsIgnoreCase(tipoBitacora)) {
                return b.getEmprendedores();
            }
        }
        return new ArrayList<>();
    }

    // ================== GENERAR REPORTE TXT (SIA2.10) ==================
    public void generarReporteTxt(String rutaArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("=== REPORTE DEL SISTEMA ===");
            pw.println("Generado: " + new Date());
            pw.println();
            
            pw.println("=== EMPRENDEDORES ===");
            for (Bitacora b : bitacoras) {
                pw.println("Bitácora: " + b.getTipo());
                for (Emprendedor e : b.getEmprendedores()) {
                    pw.println("- " + e.getNombre() + " | RUT: " + e.getRut() + 
                              " | Capital: $" + e.getCapital() + " | Proyectos: " + e.getProyectos().size());
                }
                pw.println();
            }
            
            pw.println("=== MENTORES ===");
            for (Mentor m : mentores) {
                pw.println("- " + m.getNombre() + " | Especialidad: " + m.getEspecialidad() + 
                          " | Experiencia: " + m.getAniosExperiencia() + " años");
            }
            pw.println();
            
            pw.println("=== INVERSORES ===");
            for (Inversor i : inversores) {
                pw.println("- " + i.getNombre() + " | Capital disponible: $" + i.getCapitalDisponible() + 
                          " | Inversiones: " + i.getHistorialInversiones().size());
            }
            
            pw.println("\n=== TOTALES ===");
            pw.println("Emprendedores: " + mapaEmprendedores.size());
            pw.println("Mentores: " + mentores.size());
            pw.println("Inversores: " + inversores.size());
            pw.println("Bitácoras: " + bitacoras.size());
            
        } catch (IOException e) {
            System.err.println("Error al generar reporte: " + e.getMessage());
        }
    }

    // ================== CSV: Emprendedores ==================
    public void cargarEmprendedores(String ruta) {
        try (Scanner sc = new Scanner(new File(ruta))) {
            if (sc.hasNextLine()) sc.nextLine(); // saltar cabecera
            while (sc.hasNextLine()) {
                String[] datos = sc.nextLine().split(",");
                if (datos.length >= 5) {
                    String nombre = datos[0];
                    String rut = datos[1];
                    String email = datos[2];
                    double capital = Double.parseDouble(datos[3]);
                    String tipoBitacora = datos[4];

                    Emprendedor e = new Emprendedor(nombre, rut, email, capital);
                    agregarEmprendedor(tipoBitacora, e);
                }
            }
        } catch (Exception ex) {
            System.out.println("⚠ No se pudieron cargar emprendedores: " + ex.getMessage());
        }
    }

    public void guardarEmprendedores(String ruta) {
        try (PrintWriter pw = new PrintWriter(new File(ruta))) {
            pw.println("nombre,rut,email,capital,bitacora");
            for (Bitacora b : bitacoras) {
                for (Emprendedor e : b.getEmprendedores()) {
                    pw.println(e.getNombre() + "," + e.getRut() + "," +
                               e.getEmail() + "," + e.getCapital() + "," +
                               b.getTipo());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error al guardar emprendedores: " + ex.getMessage());
        }
    }

    // ================== CSV: Mentores ==================
    public void cargarMentores(String ruta) {
        try (Scanner sc = new Scanner(new File(ruta))) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String[] datos = sc.nextLine().split(",");
                if (datos.length >= 5) {
                    String nombre = datos[0];
                    String rut = datos[1];
                    String email = datos[2];
                    String especialidad = datos[3];
                    String anos = datos[4];

                    agregarMentor(new Mentor(nombre, rut, email, especialidad, anos));
                }
            }
        } catch (Exception ex) {
            System.out.println("⚠ No se pudieron cargar mentores: " + ex.getMessage());
        }
    }

    public void guardarMentores(String ruta) {
        try (PrintWriter pw = new PrintWriter(new File(ruta))) {
            pw.println("nombre,rut,email,especialidad,experiencia");
            for (Mentor m : mentores) {
                pw.println(m.getNombre() + "," + m.getRut() + "," + m.getEmail() + "," + 
                          m.getEspecialidad() + "," + m.getAniosExperiencia());
            }
        } catch (Exception ex) {
            System.out.println("Error al guardar mentores: " + ex.getMessage());
        }
    }

    // ================== CSV: Inversores ==================
    public void cargarInversores(String ruta) {
        try (Scanner sc = new Scanner(new File(ruta))) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String[] datos = sc.nextLine().split(",");
                if (datos.length >= 4) {
                    String nombre = datos[0];
                    String rut = datos[1];
                    String email = datos[2];
                    double capital = Double.parseDouble(datos[3]);

                    agregarInversor(new Inversor(nombre, rut, email, capital));
                }
            }
        } catch (Exception ex) {
            System.out.println("⚠ No se pudieron cargar inversores: " + ex.getMessage());
        }
    }

    public void guardarInversores(String ruta) {
        try (PrintWriter pw = new PrintWriter(new File(ruta))) {
            pw.println("nombre,rut,email,capitalDisponible");
            for (Inversor i : inversores) {
                pw.println(i.getNombre() + "," + i.getRut() + "," + i.getEmail() + "," + i.getCapitalDisponible());
            }
        } catch (Exception ex) {
            System.out.println("Error al guardar inversores: " + ex.getMessage());
        }
    }
}