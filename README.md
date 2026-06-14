# LoadMaster

## Descripción y Objetivos
**LoadMaster** es una aplicación de software desarrollada en Java y gestionada con Maven, diseñada para optimizar y administrar de manera eficiente procesos de carga y logística. El objetivo principal del proyecto es proporcionar una herramienta robusta bajo la arquitectura Modelo-Vista-Controlador (MVC), que permita a los usuarios gestionar flujos de datos, automatizar tareas operativas y asegurar la consistencia de la información a través de una interfaz clara y estructurada.

## Justificación del Flujo de Trabajo
[cite_start]Para el desarrollo colaborativo de este proyecto, el equipo eligió implementar el flujo de trabajo **GitHub Flow**, esta elección se justifica debido a la rapidez, sencillez de su flujo y la menor complejidad que ofrece en la gestión de ramas en comparación con otros modelos. [cite_start]Al basarse en ramas descriptivas de corta duración (como `feature/`) que se integran directamente a la rama `main` mediante *Pull Requests* y revisiones de código, nos permite mantener un repositorio limpio, evitar conflictos graves y asegurar un ritmo de entrega continuo y ágil.

## Requisitos e Instalación
Para ejecutar este proyecto, necesitas contar con el siguiente entorno de desarrollo:
* **Java:** (Ej. JDK 17 o la versión que usen)
* **Maven:** (Herramienta de construcción)

**Pasos para la instalación:**
1. Clonar el repositorio localmente.
2. Abrir el proyecto en tu IDE preferido (como NetBeans o IntelliJ).
3. Dejar que Maven descargue las dependencias necesarias.

## Ejemplos de Ejecución
Para ejecutar el proyecto **LoadMaster** de forma local, asegúrese de cumplir con los requisitos previos (Java y Maven instalados) y siga estos pasos:

1. **Compilar el proyecto:** Limpie y compile los archivos utilizando Maven para descargar las dependencias necesarias:
   ```bash
   mvn clean install

2. **Ejecutar la aplicación:** Levante la interfaz principal del programa ejecutando el siguiente comando en la terminal:
   ```bash
   mvn exec:java -Dexec.mainClass="com.tuempresa.loadmaster.Main"
   ```
   *(Nota: Reemplace `com.tuempresa.loadmaster.Main` por la ruta exacta de la clase principal de su proyecto si es distinta).*

## Estructura de Directorios
A continuación, se detalla la organización lógica de las carpetas de LoadMaster:

├── assets/          # Recursos gráficos, logotipos y diagramas UML
├── docs/            # Documentación complementaria y arquitectura
├── src/             # Código fuente principal del programa
│   └── main/
└── pom.xml          # Archivo de configuración de Maven

## Autores
* Luis Fernando Licona Castillo 
* Hugo Jael Jarquin Cruz
* Leonel Omar Ramirez Hernández 
* Hector Josue Lopez Ascencion 