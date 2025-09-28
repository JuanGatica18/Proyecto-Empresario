package principal;

import humanos.Emprendedor;
import principal.Bitacora;
import principal.Proyecto;
import humanos.Inversor;
import excepciones.DatosInvalidosException;
import excepciones.ElementoNoEncontradoException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Sistema {

    private List<Bitacora> bitacoras;
    private Map<String, Emprendedor> mapaEmprendedores;
    private List<Inversor> inversores;
    
    public Sistema() {
        bitacoras = new ArrayList<>();
        mapaEmprendedores = new HashMap<>();
        inversores = new ArrayList<>();
        cargarEstructuraInicialBitacoras();
    }
    
    private void cargarEstructuraInicialBitacoras() {
        bitacoras.clear();
        String[] tiposBitacora = {"Tecnologia", "Salud", "Educacion", "Alimentos"};
        for (String tipo : tiposBitacora) {
            bitacoras.add(new Bitacora(tipo));
        }
    }

    // MÉTODO PRINCIPAL DE INICIALIZACIÓN - VERSIÓN ROBUSTA CORREGIDA
    public void inicializarSistema() {
        System.out.println("=== INICIALIZANDO SISTEMA ===");
        
        // 1. SIEMPRE cargar estructura de bitácoras primero
        if (bitacoras.isEmpty()) {
            cargarEstructuraInicialBitacoras();
            System.out.println("Estructura de bitácoras inicializada");
        }
        
        // 2. Intentar cargar emprendedores existentes
        boolean emprendedoresExistentes = intentarCargarEmprendedores();
        
        // 3. Intentar cargar inversores existentes  
        boolean inversoresExistentes = intentarCargarInversores();
        
        // 4. Si no hay datos, cargar por defecto
        if (!emprendedoresExistentes && !inversoresExistentes) {
            System.out.println("No hay datos previos, cargando datos de ejemplo");
            cargarDatosIniciales();
        } else if (!emprendedoresExistentes) {
            System.out.println("Cargando emprendedores de ejemplo");
            cargarEmprendedoresEjemplo();
        } else if (!inversoresExistentes) {
            System.out.println("Cargando inversores de ejemplo");
            cargarInversoresEjemplo();
        }
        
        // ✅ CORRECCIÓN CRÍTICA: Sincronizar datos después de la carga
        sincronizarDatos();
        
        System.out.println("=== SISTEMA INICIALIZADO ===");
        System.out.println("Emprendedores totales: " + mapaEmprendedores.size());
        System.out.println("Inversores totales: " + inversores.size());
        mostrarEstadoBitacoras();
    }
    
    // ✅ NUEVO MÉTODO: Sincronizar datos entre mapa y bitácoras
    private void sincronizarDatos() {
        System.out.println("=== SINCRONIZANDO DATOS ===");
        
        // Si hay emprendedores en el mapa pero no en bitácoras, distribuirlos
        if (!mapaEmprendedores.isEmpty()) {
            int emprendedoresEnBitacoras = 0;
            for (Bitacora b : bitacoras) {
                emprendedoresEnBitacoras += b.getEmprendedores().size();
            }
            
            System.out.println("Emprendedores en mapa: " + mapaEmprendedores.size());
            System.out.println("Emprendedores en bitácoras: " + emprendedoresEnBitacoras);
            
            if (emprendedoresEnBitacoras == 0) {
                // Redistribuir emprendedores del mapa a las bitácoras
                redistribuirEmprendedores();
            }
        }
    }
    
    // ✅ NUEVO MÉTODO: Redistribuir emprendedores cuando están solo en el mapa
    private void redistribuirEmprendedores() {
        System.out.println("Redistribuyendo emprendedores a bitácoras...");
        
        String[] tiposBitacora = {"Tecnologia", "Salud", "Educacion", "Alimentos"};
        int indice = 0;
        
        for (Emprendedor emp : mapaEmprendedores.values()) {
            String tipoBitacora = tiposBitacora[indice % tiposBitacora.length];
            
            // Buscar la bitácora correspondiente
            for (Bitacora b : bitacoras) {
                if (b.getTipo().equals(tipoBitacora)) {
                    if (!b.getEmprendedores().contains(emp)) {
                        b.agregarEmprendedor(emp);
                        System.out.println("Emprendedor " + emp.getNombre() + " agregado a bitácora " + tipoBitacora);
                    }
                    break;
                }
            }
            indice++;
        }
    }
    
    // ✅ MÉTODO AUXILIAR: Mostrar estado de las bitácoras
    private void mostrarEstadoBitacoras() {
        System.out.println("--- Estado de Bitácoras ---");
        for (Bitacora b : bitacoras) {
            System.out.println(b.getTipo() + ": " + b.getEmprendedores().size() + " emprendedores");
        }
    }

    // Método auxiliar para intentar cargar emprendedores CORREGIDO
    private boolean intentarCargarEmprendedores() {
        try {
            File archivo = new File("emprendedores.csv");
            if (!archivo.exists() || archivo.length() == 0) {
                System.out.println("emprendedores.csv no existe o está vacío");
                return false;
            }

            // ✅ CORRECCIÓN: No limpiar datos existentes hasta confirmar carga exitosa
            Map<String, Emprendedor> tempMapa = new HashMap<>(mapaEmprendedores);

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                String encabezado = br.readLine(); // Saltar encabezado
                if (encabezado == null) {
                    System.out.println("Archivo CSV vacío");
                    return false;
                }
                
                int contadorCargados = 0;

                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    
                    String[] datos = linea.split(",");
                    if (datos.length >= 4) { // Mínimo 4 campos requeridos
                        try {
                            String nombre = datos[0].trim();
                            String rut = datos[1].trim();
                            String email = datos[2].trim();
                            double capital = Double.parseDouble(datos[3].trim());
                            
                            // TipoBitacora opcional (campo 5)
                            String tipoBitacora = "Tecnologia"; // Por defecto
                            if (datos.length >= 5) {
                                tipoBitacora = datos[4].trim();
                            }

                            Emprendedor emp = new Emprendedor(nombre, rut, email, capital);

                            // ✅ CRÍTICO: Agregar proyectos por defecto
                            agregarProyectosPorDefecto(emp, tipoBitacora);

                            tempMapa.put(rut, emp);
                            contadorCargados++;
                            
                            System.out.println("Cargado: " + nombre + " (" + rut + ")");
                        } catch (NumberFormatException ex) {
                            System.err.println("Error de formato en línea: " + linea);
                        }
                    }
                }

                // Solo reemplazar si se cargaron datos válidos
                if (contadorCargados > 0) {
                    mapaEmprendedores.clear();
                    mapaEmprendedores.putAll(tempMapa);

                    System.out.println("Emprendedores cargados desde CSV: " + contadorCargados);
                    return true;
                }
            }

        } catch (IOException ex) {
            System.err.println("Error leyendo emprendedores.csv: " + ex.getMessage());
        }

        return false;
    }

    // Método auxiliar para intentar cargar inversores SIN limpiar datos existentes
    private boolean intentarCargarInversores() {
        try {
            File archivo = new File("inversores.csv");
            if (!archivo.exists() || archivo.length() == 0) {
                System.out.println("inversores.csv no existe o está vacío");
                return false;
            }
            
            List<Inversor> tempInversores = new ArrayList<>(inversores);
            
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                br.readLine(); // Saltar encabezado
                int contadorCargados = 0;
                
                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    
                    String[] datos = linea.split(",");
                    if (datos.length >= 4) {
                        try {
                            String nombre = datos[0].trim();
                            String rut = datos[1].trim();
                            String email = datos[2].trim();
                            double capital = Double.parseDouble(datos[3].trim());

                            Inversor inv = new Inversor(nombre, rut, email, capital);
                            tempInversores.add(inv);
                            contadorCargados++;
                        } catch (NumberFormatException ex) {
                            System.err.println("Error de formato en línea inversor: " + linea);
                        }
                    }
                }
                
                // Solo reemplazar si se cargaron datos válidos
                if (contadorCargados > 0) {
                    inversores.clear();
                    inversores.addAll(tempInversores);
                    System.out.println("Inversores cargados desde CSV: " + contadorCargados);
                    return true;
                }
            }
            
        } catch (IOException ex) {
            System.err.println("Error leyendo inversores.csv: " + ex.getMessage());
        }
        
        return false;
    }

    // Cargar datos iniciales completos
    private void cargarDatosIniciales() {
        cargarEmprendedoresEjemplo();
        cargarInversoresEjemplo();
    }

    // ✅ CORRECCIÓN: Cargar emprendedores de ejemplo Y agregarlos a bitácoras
    private void cargarEmprendedoresEjemplo() {
        try {
            System.out.println("Cargando emprendedores de ejemplo...");
            
            // Limpiar datos existentes
            mapaEmprendedores.clear();
            for (Bitacora b : bitacoras) {
                b.getEmprendedores().clear();
            }
            
            Emprendedor emp1 = new Emprendedor("Ema Tecnologia", "11111111-1", "ema@tec.cl", 10000.0);
            agregarProyectosPorDefecto(emp1, "Tecnologia");
            
            Emprendedor emp2 = new Emprendedor("Salva Salud", "22222222-2", "salva@salud.cl", 20000.0);
            agregarProyectosPorDefecto(emp2, "Salud");

            Emprendedor emp3 = new Emprendedor("Educa Chile", "33333333-3", "edu@edu.cl", 5000.0);
            agregarProyectosPorDefecto(emp3, "Educacion");
            
            Emprendedor emp4 = new Emprendedor("Ali Mentos", "44444444-4", "ali@alimentos.cl", 15000.0);
            agregarProyectosPorDefecto(emp4, "Alimentos");

            // ✅ CORRECCIÓN CRÍTICA: Usar el método correcto que agrega a AMBAS estructuras
            agregarEmprendedor(emp1, "Tecnologia");
            agregarEmprendedor(emp2, "Salud");
            agregarEmprendedor(emp3, "Educacion");
            agregarEmprendedor(emp4, "Alimentos");
            
            System.out.println("Emprendedores de ejemplo cargados: " + mapaEmprendedores.size());
            
        } catch (DatosInvalidosException ex) {
            System.err.println("Error cargando emprendedores de ejemplo: " + ex.getMessage());
        }
    }

    // Cargar solo inversores de ejemplo
    private void cargarInversoresEjemplo() {
        inversores.clear();
        inversores.add(new Inversor("Inversor A", "44444444-4", "inv.a@mail.com", 50000.0));
        inversores.add(new Inversor("Inversor B", "55555555-5", "inv.b@mail.com", 100000.0));
        System.out.println("Inversores de ejemplo cargados: " + inversores.size());
    }
    
    // =========================================================================
    // MÉTODOS DE NEGOCIO
    // =========================================================================
    
    public void agregarEmprendedor(Emprendedor emprendedor, String tipoBitacora) throws DatosInvalidosException {
        if (mapaEmprendedores.containsKey(emprendedor.getRut())) {
            throw new DatosInvalidosException("El emprendedor con RUT " + emprendedor.getRut() + " ya existe.");
        }
        
        Bitacora bitacoraDestino = null;
        for (Bitacora b : bitacoras) {
            if (b.getTipo().equalsIgnoreCase(tipoBitacora)) {
                bitacoraDestino = b;
                break;
            }
        }
        
        if (bitacoraDestino != null) {
            // ✅ CRÍTICO: Agregar a AMBAS estructuras
            bitacoraDestino.agregarEmprendedor(emprendedor);
            mapaEmprendedores.put(emprendedor.getRut(), emprendedor);
            
            System.out.println("Emprendedor " + emprendedor.getNombre() + " agregado a " + tipoBitacora);
        } else {
            throw new DatosInvalidosException("El tipo de bitácora '" + tipoBitacora + "' no existe.");
        }
    }
    
    public void eliminarEmprendedor(String rut) throws ElementoNoEncontradoException {
        Emprendedor emprendedor = mapaEmprendedores.remove(rut);
        
        if (emprendedor == null) {
            throw new ElementoNoEncontradoException("Emprendedor con RUT " + rut + " no encontrado.");
        }
        
        // También eliminarlo de la bitácora
        for (Bitacora b : bitacoras) {
            if (b.getEmprendedores().remove(emprendedor)) {
                break;
            }
        }
    }

    public Emprendedor buscarEmprendedorPorRut(String rut) throws ElementoNoEncontradoException {
        Emprendedor e = mapaEmprendedores.get(rut);
        if (e == null) {
            throw new ElementoNoEncontradoException("Emprendedor con RUT " + rut + " no encontrado.");
        }
        return e;
    }

    public Inversor buscarInversorPorRut(String rut) throws ElementoNoEncontradoException {
        for (Inversor i : inversores) {
            if (i.getRut().equals(rut)) {
                return i;
            }
        }
        throw new ElementoNoEncontradoException("Inversor con RUT " + rut + " no encontrado.");
    }
    
    public void agregarInversor(Inversor inversor) throws DatosInvalidosException {
        if (inversores.stream().anyMatch(i -> i.getRut().equals(inversor.getRut()))) {
             throw new DatosInvalidosException("El inversor con RUT " + inversor.getRut() + " ya existe.");
        }
        inversores.add(inversor);
    }

    // MÉTODOS DE FILTROS
    public List<Emprendedor> filtrarPorCapital(double capitalMinimo) {
        List<Emprendedor> resultado = new ArrayList<>();
        for (Emprendedor e : mapaEmprendedores.values()) {
            if (e.getCapital() >= capitalMinimo) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Emprendedor> filtrarPorNroProyectos(int nroMinimo) {
        List<Emprendedor> resultado = new ArrayList<>();
        for (Emprendedor e : mapaEmprendedores.values()) {
            if (e.getProyectos().size() >= nroMinimo) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public List<Emprendedor> filtrarPorBitacora(String tipoBitacora) {
        List<Emprendedor> resultado = new ArrayList<>();
        for (Bitacora b : bitacoras) {
            if (b.getTipo().equalsIgnoreCase(tipoBitacora)) {
                resultado.addAll(b.getEmprendedores());
            }
        }
        return resultado;
    }
    
    // =========================================================================
    // MÉTODOS DE PERSISTENCIA (CSV/TXT)
    // =========================================================================

    public void guardarEmprendedores(String nombreArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("Nombre,RUT,Email,Capital,TipoBitacora");
            
            for (Emprendedor e : mapaEmprendedores.values()) {
                String tipoBitacora = obtenerTipoBitacoraDeEmprendedor(e.getRut());
                pw.printf("%s,%s,%s,%.2f,%s%n",
                          e.getNombre(), e.getRut(), e.getEmail(), e.getCapital(), tipoBitacora);
            }
            System.out.println("Emprendedores guardados correctamente en " + nombreArchivo);

        } catch (IOException ex) {
            System.err.println("Error al guardar Emprendedores: " + ex.getMessage());
        }
    }
    
    private String obtenerTipoBitacoraDeEmprendedor(String rut) {
        for (Bitacora b : bitacoras) {
            for (Emprendedor e : b.getEmprendedores()) {
                if (e.getRut().equals(rut)) {
                    return b.getTipo(); 
                }
            }
        }
        return "Tecnologia"; 
    }
    
    public void guardarInversores(String nombreArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("Nombre,RUT,Email,CapitalDisponible");
            
            for (Inversor i : inversores) {
                pw.printf("%s,%s,%s,%.2f%n",
                          i.getNombre(), i.getRut(), i.getEmail(), i.getCapitalDisponible());
            }
            System.out.println("Inversores guardados correctamente en " + nombreArchivo);

        } catch (IOException ex) {
            System.err.println("Error al guardar Inversores: " + ex.getMessage());
        }
    }

    public void generarReporteTxt(String nombreArchivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("====================================================\n");
            writer.write("          REPORTE GENERAL DEL SISTEMA DE INVERSIÓN\n");
            writer.write("====================================================\n");
            writer.write("Fecha de Reporte: " + new Date() + "\n");
            writer.write("\n");

            writer.write("--- RESUMEN GENERAL ---\n");
            writer.write("Total Emprendedores: " + mapaEmprendedores.size() + "\n");
            writer.write("Total Inversores: " + inversores.size() + "\n");
            writer.write("Total Bitácoras: " + bitacoras.size() + "\n");
            writer.write("\n");

            writer.write("--- DETALLE DE EMPRENDEDORES POR BITÁCORA ---\n");
            for (Bitacora b : bitacoras) {
                writer.write(">>> Bitácora: " + b.getTipo() + " (Emprendedores: " + b.getEmprendedores().size() + ")\n");
                if (b.getEmprendedores().isEmpty()) {
                    writer.write("  (Sin emprendedores)\n");
                }
                for (Emprendedor e : b.getEmprendedores()) {
                    writer.write("  - " + e.mostrarInfo() + "\n");
                    writer.write("    Proyectos:\n");
                    if (e.getProyectos().isEmpty()) {
                        writer.write("      (No tiene proyectos)\n");
                    } else {
                        for (Proyecto p : e.getProyectos()) {
                            writer.write("      * " + p.getNombre() + " (Inversión: $" + p.getInversion() + " | Riesgo: " + p.calcularRiesgo() + ")\n");
                        }
                    }
                }
                writer.write("\n");
            }
            
            writer.write("--- DETALLE DE INVERSORES ---\n");
            for (Inversor i : inversores) {
                writer.write("- " + i.mostrarInfo() + "\n");
            }
            writer.write("====================================================\n");
            System.out.println("Reporte generado: " + nombreArchivo);

        } catch (IOException ex) {
            System.err.println("Error al generar reporte TXT: " + ex.getMessage());
            throw ex;
        }
    }
    
    // Getters
    public List<Bitacora> getBitacoras() { return bitacoras; }
    public Map<String, Emprendedor> getMapaEmprendedores() { return mapaEmprendedores; }
    public List<Inversor> getInversores() { return inversores; }
    
    
    private void agregarProyectosPorDefecto(Emprendedor emprendedor, String tipoBitacora) {
        emprendedor.getProyectos().clear();

        switch (tipoBitacora.toLowerCase()) {
            case "tecnologia":
                emprendedor.registrarProyecto(new Proyecto(
                    "App Móvil Innovadora", 
                    "Aplicación móvil para conectar emprendedores con inversores, incluye chat en tiempo real, sistema de matching y análisis de mercado",
                    5000.0, 
                    "En desarrollo", 
                    "Tecnologia",
                    "Tecnología Financiera",
                    3,
                    "Santiago, Chile"
                ));
                emprendedor.registrarProyecto(new Proyecto(
                    "Plataforma E-Commerce", 
                    "Sistema web completo para ventas online con inteligencia artificial para recomendaciones personalizadas",
                    8000.0, 
                    "Beta", 
                    "Tecnologia",
                    "Comercio Electrónico",
                    5,
                    "Valparaíso, Chile"
                ));
                break;

            case "salud":
                emprendedor.registrarProyecto(new Proyecto(
                    "Telemedicina Avanzada", 
                    "Plataforma de consultas médicas virtuales con diagnóstico asistido por IA y seguimiento de pacientes crónicos",
                    15000.0, 
                    "Activo", 
                    "Salud",
                    "Salud Digital",
                    8,
                    "Las Condes, Santiago"
                ));
                emprendedor.registrarProyecto(new Proyecto(
                    "Monitor de Signos Vitales IoT", 
                    "Dispositivo wearable que monitorea signos vitales 24/7 y envía alertas médicas automáticas",
                    12000.0, 
                    "En desarrollo", 
                    "Salud",
                    "Biotecnología",
                    6,
                    "Viña del Mar, Chile"
                ));
                break;

            case "educacion":
                emprendedor.registrarProyecto(new Proyecto(
                    "Plataforma E-Learning Interactiva", 
                    "Sistema educativo online con realidad virtual, gamificación y seguimiento personalizado del progreso estudiantil",
                    6000.0, 
                    "Beta", 
                    "Educacion",
                    "EdTech",
                    4,
                    "Concepción, Chile"
                ));
                emprendedor.registrarProyecto(new Proyecto(
                    "Tutor Virtual con IA", 
                    "Asistente educativo basado en inteligencia artificial que se adapta al estilo de aprendizaje de cada estudiante",
                    4500.0, 
                    "Planificacion", 
                    "Educacion",
                    "Inteligencia Artificial Educativa",
                    3,
                    "Temuco, Chile"
                ));
                break;

            case "alimentos":
                emprendedor.registrarProyecto(new Proyecto(
                    "Restaurante Sustentable", 
                    "Cadena de restaurantes con enfoque en ingredientes orgánicos locales, cero desperdicio y energías renovables",
                    18000.0, 
                    "En desarrollo", 
                    "Alimentos",
                    "Gastronomía Sostenible",
                    7,
                    "Providencia, Santiago"
                ));
                emprendedor.registrarProyecto(new Proyecto(
                    "App Delivery Saludable", 
                    "Plataforma de delivery especializada en comida saludable con análisis nutricional y planes alimentarios personalizados",
                    10000.0, 
                    "Planificacion", 
                    "Alimentos",
                    "FoodTech",
                    4,
                    "La Serena, Chile"
                ));
                emprendedor.registrarProyecto(new Proyecto(
                    "Huerto Urbano Inteligente", 
                    "Sistema automatizado de cultivo urbano con sensores IoT para optimizar riego, luz y nutrientes",
                    7500.0, 
                    "Beta", 
                    "Alimentos",
                    "AgriTech",
                    3,
                    "Rancagua, Chile"
                ));
                break;

            default:
                emprendedor.registrarProyecto(new Proyecto(
                    "Proyecto Genérico", 
                    "Proyecto de prueba sin categoría específica para demostración del sistema",
                    1000.0, 
                    "Planificacion", 
                    "Generico",
                    "Sin Clasificar",
                    1,
                    "Por definir"
                ));
                break;
        }
    }
}