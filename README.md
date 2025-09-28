Sistema de Emprendedores - Guía de Instalación Windows
Requisitos del Sistema

Windows 10/11
Java 11 (JDK)
Apache Maven
JavaFX 13

Instalación Paso a Paso
1. Instalar Java 11 JDK

Descargar desde: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
Ejecutar el instalador
Verificar instalación:

cmd   java -version
   javac -version
2. Instalar Apache Maven

Descargar desde: https://maven.apache.org/download.cgi
Descomprimir en C:\Program Files\Apache\maven
Configurar variables de entorno:

Agregar MAVEN_HOME = C:\Program Files\Apache\maven
Agregar %MAVEN_HOME%\bin al PATH


Verificar instalación:

cmd   mvn -version
3. Descargar JavaFX 13

IR A: https://gluonhq.com/products/javafx/
Descargar JavaFX Windows SDK 11.0.2
Descomprimir en C:\javafx-11.0.2
La estructura debe ser:

   C:\javafx-11.0.2\
   ├── bin\
   ├── lib\
   └── legal\
4. Configurar Variables de Entorno
Abrir Propiedades del Sistema > Variables de entorno:
Variables del sistema:

JAVA_HOME = C:\Program Files\Java\jdk-11.0.X (donde X es tu versión)
JAVAFX_HOME = C:\javafx-11.0.2
PATH agregar: %JAVA_HOME%\bin y %MAVEN_HOME%\bin

Verificar configuración:
cmdecho %JAVA_HOME%
echo %JAVAFX_HOME%
5. Preparar el Proyecto
Estructura de carpetas esperada:
tu-proyecto\
├── src\
│   └── main\
│       └── java\
│           ├── packs\pack\Main.java
│           ├── principal\
│           ├── humanos\
│           ├── ventanas\
│           └── excepciones\
├── pom.xml
└── README.md
6. Compilar y Ejecutar
Abrir Símbolo del sistema en la carpeta del proyecto:
Opción 1 - Con Maven (Recomendado):
cmdmvn clean compile
mvn javafx:run
Opción 2 - Con parámetros JavaFX explícitos:
cmdmvn clean compile

java --module-path "C:\javafx-11.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp target/classes packs.pack.Main
Opción 3 - Compilación manual:
cmdjavac --module-path "C:\javafx-11.0.2\lib" --add-modules javafx.controls -d target/classes -cp "target/classes" src/main/java/packs/pack/*.java src/main/java/principal/*.java src/main/java/humanos/*.java src/main/java/ventanas/*.java src/main/java/excepciones/*.java

java --module-path "C:\javafx-11.0.2\lib" --add-modules javafx.controls -cp target/classes packs.pack.Main
Solución de Problemas Comunes
Error: "javafx.application.Application not found"
Causa: JavaFX no está en el classpath
Solución:
cmdset PATH=%PATH%;C:\javafx-11.0.2\lib
mvn clean javafx:run
Error: "JAVA_HOME not found"
Causa: Variable de entorno mal configurada
Solución:

Verificar que JAVA_HOME apunte a la carpeta JDK (no JRE)
Reiniciar el símbolo del sistema
Ejecutar: echo %JAVA_HOME%

Error de compilación Maven
Causa: Versión incorrecta de JavaFX en pom.xml
Solución: Tu pom.xml usa JavaFX 13, cambiar a:
xml<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>11.0.2</version>
</dependency>
Error: "Module javafx.controls not found"
Causa: JavaFX no está instalado correctamente
Solución:

Verificar que C:\javafx-11.0.2\lib contenga archivos .jar
Ejecutar con path absoluto:

cmdjava --module-path "C:\javafx-11.0.2\lib" --add-modules javafx.controls -cp target/classes packs.pack.Main
Comandos de Desarrollo
Limpiar proyecto:
cmdmvn clean
Solo compilar:
cmdmvn compile
Ejecutar con debug:
cmdmvn clean javafx:run@debug
Generar JAR (sin JavaFX incluido):
cmdmvn package
Archivos Generados
El sistema creará automáticamente:

emprendedores.csv - Datos de emprendedores
inversores.csv - Datos de inversores
reporte_sistema.txt - Reportes generados

Verificación Final
Si todo está correcto, deberías ver:

Ventana JavaFX del sistema
Botones funcionales
Sin errores en consola
Archivos CSV generándose al cerrar

Comando de prueba rápida:
cmdcd tu-proyecto
mvn clean javafx:run
