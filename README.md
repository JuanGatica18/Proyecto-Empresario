# Sistema de Emprendedores - Guía de Instalación Windows

## Requisitos del Sistema
- Windows 10/11
- Java 11 (JDK)
- Apache Maven
- JavaFx 13

## Instalación Paso a Paso

### 1. Instalar Java 11 JDK
1. Descargar desde: https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
2. Ejecutar el instalador
3. Verificar instalación:
   ```cmd
   java -version
   javac -version
   ```

### 2. Instalar Apache Maven
1. Descargar desde: https://maven.apache.org/download.cgi
2. Descomprimir en `C:\Program Files\Apache\maven`
3. Configurar variables de entorno:
   - Agregar `MAVEN_HOME = C:\Program Files\Apache\maven`
   - Agregar `%MAVEN_HOME%\bin` al PATH
4. Verificar instalación:
   ```cmd
   mvn -version
   ```

### 3. Descargar JavaFx 13
1. IR A: https://gluonhq.com/products/javafx/
2. Descargar **JavaFX Windows SDK 13**
3. Descomprimir en `C:\javafx-13`
4. La estructura debe ser:
   ```
   C:\javafx-13\
   ├── bin\
   ├── lib\
   └── legal\
   ```

### 4. Configurar Variables de Entorno
Abrir **Propiedades del Sistema > Variables de entorno**:

**Variables del sistema:**
- `JAVA_HOME` = `C:\Program Files\Java\jdk-11.0.X` (donde X es tu versión)
- `JAVAFX_HOME` = `C:\javafx-13`
- `PATH` agregar: `%JAVA_HOME%\bin` y `%MAVEN_HOME%\bin`

**Verificar configuración:**
```cmd
echo %JAVA_HOME%
echo %JAVAFX_HOME%
```

### 5. Preparar el Proyecto

**Estructura de carpetas esperada:**
```
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
```

### 6. Compilar y Ejecutar

Abrir **Símbolo del sistema** en la carpeta del proyecto:

**Opción 1 - Con Maven (Recomendado):**
```cmd
mvn clean compile
mvn javafx:run
```

**Opción 2 - Con parámetros JavaFX explícitos:**
```cmd
mvn clean compile

java --module-path "C:\javafx-13\lib" --add-modules javafx.controls,javafx.fxml -cp target/classes packs.pack.Main
```

**Opción 3 - Compilación manual:**
```cmd
javac --module-path "C:\javafx-13\lib" --add-modules javafx.controls -d target/classes -cp "target/classes" src/main/java/packs/pack/*.java src/main/java/principal/*.java src/main/java/humanos/*.java src/main/java/ventanas/*.java src/main/java/excepciones/*.java

java --module-path "C:\javafx-13\lib" --add-modules javafx.controls -cp target/classes packs.pack.Main
```

## Solución de Problemas Comunes

### Error: "javafx.application.Application not found"
**Causa:** JavaFX no está en el classpath
**Solución:**
```cmd
set PATH=%PATH%;C:\javafx-13\lib
mvn clean javafx:run
```

### Error: "JAVA_HOME not found"
**Causa:** Variable de entorno mal configurada
**Solución:**
1. Verificar que JAVA_HOME apunte a la carpeta JDK (no JRE)
2. Reiniciar el símbolo del sistema
3. Ejecutar: `echo %JAVA_HOME%`

### Error de compilación Maven
**Causa:** Versión incorrecta de JavaFX en pom.xml
**Solución:** Tu pom.xml usa JavaFX 13, cambiar a:
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>13</version>
</dependency>
```

### Error: "Module javafx.controls not found"
**Causa:** JavaFX no está instalado correctamente
**Solución:**
1. Verificar que `C:\javafx-13\lib` contenga archivos .jar
2. Ejecutar con path absoluto:
```cmd
java --module-path "C:\javafx-13\lib" --add-modules javafx.controls -cp target/classes packs.pack.Main
```

## Archivos Generados
El sistema creará automáticamente:
- `emprendedores.csv` - Datos de emprendedores
- `inversores.csv` - Datos de inversores  
- `reporte_sistema.txt` - Reportes generados

## Verificación Final

Si todo está correcto, deberías ver:
1. Ventana JavaFX del sistema
2. Botones funcionales
3. Sin errores en consola
4. Archivos CSV generándose al cerrar

