\# Arquitectura MVC de LoadMaster Engineering Suite



\## Introducción



LoadMaster Engineering Suite es una aplicación desarrollada en JavaFX para la ejecución de pruebas de carga y monitoreo de servidores. Para mantener una estructura organizada y facilitar el mantenimiento del código, el proyecto implementa el patrón de diseño MVC (Modelo-Vista-Controlador).



Este patrón permite separar la representación visual, la lógica de negocio y la administración de datos en componentes independientes.



\---



\## Modelo (Model)



El modelo contiene las clases responsables de representar y almacenar la información utilizada por el sistema.



\### Clases identificadas



\* `Prueba.java`

\* `RegistroLatencia.java`

\* `Servidor.java`



\### Responsabilidades



\* Almacenar la información de las pruebas ejecutadas.

\* Gestionar registros de latencia.

\* Representar los servidores monitoreados.

\* Servir como fuente de datos para los controladores.



\---



\## Vista (View)



La vista corresponde a la interfaz gráfica desarrollada mediante JavaFX y archivos FXML.



\### Archivos identificados



\* `MenuView.fxml`

\* `SysAdminView.fxml`

\* `TesterView.fxml`



\### Responsabilidades



\* Mostrar información al usuario.

\* Permitir la interacción mediante formularios y controles gráficos.

\* Presentar resultados de pruebas y métricas.



\---



\## Controlador (Controller)



Los controladores gestionan la interacción entre la interfaz gráfica y las clases del modelo.



\### Clases identificadas



\* `MenuController.java`

\* `SysAdminController.java`

\* `TesterController.java`



\### Responsabilidades



\* Procesar eventos generados por los usuarios.

\* Coordinar el intercambio de información entre la vista y el modelo.

\* Ejecutar acciones relacionadas con las pruebas de carga.



\---



\## Capa de Acceso a Datos (DAO)



La aplicación utiliza el patrón DAO para encapsular el acceso a la base de datos.



\### Clases identificadas



\* `PruebaDAO.java`

\* `ServidorDAO.java`



\### Responsabilidades



\* Consultar información almacenada.

\* Registrar resultados de pruebas.

\* Gestionar operaciones de lectura y escritura.



\---



\## Conexión a Base de Datos



\### Clase identificada



\* `ConexionBD.java`



\### Responsabilidades



\* Establecer la conexión con la base de datos SQLite.

\* Gestionar la disponibilidad de la conexión.

\* Centralizar el acceso a los datos.



\---



\## Motor de Procesamiento



Las operaciones principales de generación de carga y procesamiento de métricas se encuentran dentro del paquete engine.



\### Clases identificadas



\* `MotorHttp.java`

\* `ProcesadorMetricas.java`

\* `StressTask.java`



\### Responsabilidades



\* Ejecutar solicitudes HTTP.

\* Procesar estadísticas de rendimiento.

\* Administrar tareas de pruebas de carga concurrentes.



\---



\## Ventajas de la Arquitectura MVC



\* Separación clara de responsabilidades.

\* Mayor facilidad de mantenimiento.

\* Escalabilidad del proyecto.

\* Reutilización de componentes.

\* Mejor organización del código fuente.



\---



\## Conclusión



La arquitectura MVC implementada en LoadMaster Engineering Suite permite una estructura modular y organizada, facilitando tanto el desarrollo como el mantenimiento de la aplicación. La combinación de JavaFX, DAO y procesamiento especializado proporciona una solución adecuada para la ejecución y análisis de pruebas de carga.



