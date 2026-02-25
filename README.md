# Sistema de Gestión de Tareas — Arquitectura Monolítica

**Grupo 1 | Ingeniería de Software I | Universidad Invenio**

Alejandro Zamora · Alejandro Luna · César Ubau · Saúl Ramírez  
Profesor: Darin Mauricio Gamboa

---

## ¿Qué es este proyecto?

Un Sistema de Gestión de Tareas construido en Java que demuestra la Arquitectura Monolítica.  
Todas las capas — modelo, repositorio, servicio e interfaz — viven en un solo proceso JVM y se comunican mediante llamadas directas en memoria, sin red ni HTTP entre ellas.

El objetivo del proyecto es evidenciar cómo funciona un sistema monolítico estructurado por capas internas, manteniendo separación lógica pero sin separación física.

---

## Arquitectura

```
TaskApp (Interfaz Gráfica)
        │
        ▼  llamada en memoria
TaskService (Lógica de Negocio)
        │
        ▼  llamada en memoria
TaskRepository (Acceso a Datos)
        │
        ▼
HashMap<String, Task> (Almacenamiento en memoria - PoC)
```

Todo en un solo proceso.  
Un solo `.jar`.  
Una sola JVM.  

Eso es el monolito.

---

## Estructura del proyecto

```
task-monolith/
├── src/main/java/com/taskmonolith/
│   ├── model/
│   │   └── Task.java
│   ├── repository/
│   │   └── TaskRepository.java
│   ├── service/
│   │   └── TaskService.java
│   ├── TaskApp.java
│   └── TaskMonolith.java
└── pom.xml
```

Cada capa tiene una responsabilidad claramente definida:

- Model: Entidades de dominio.
- Repository: Acceso a datos.
- Service: Reglas de negocio.
- Presentación: Interfaz gráfica y consola.

---

## Requisitos

- JDK 22  
- Apache Maven 3.8+  
- NetBeans 22 (recomendado)

---

## Compilación con Maven

Desde la carpeta raíz del proyecto (donde se encuentra el `pom.xml`):

```bash
mvn clean package
```

Esto generará el archivo:

```
target/task-monolith-1.0.jar
```

El nombre puede variar según la versión definida en el `pom.xml`.

---

## Cómo ejecutar

### Opción 1: Desde NetBeans (modo académico recomendado)

Versión gráfica:

1. Abrir el proyecto en NetBeans  
2. Click derecho sobre el proyecto → Properties → Run  
3. Main Class: com.taskmonolith.TaskApp  
4. Click derecho → Run  

Versión consola:

1. Cambiar Main Class a: com.taskmonolith.TaskMonolith  
2. Click derecho → Run  

---

### Opción 2: Desde consola (CLI)

Versión gráfica (si el JAR tiene configurado TaskApp como Main-Class):

```bash
java -jar target/task-monolith-1.0.jar
```

Versión consola:

```bash
java -cp target/task-monolith-1.0.jar com.taskmonolith.TaskMonolith
```

---

## Las 3 reglas de negocio implementadas

1. Una tarea CANCELADA no puede modificarse (estado terminal).
2. Una tarea COMPLETADA no puede volver a PENDING o IN_PROGRESS.
3. Solo las tareas COMPLETADAS pueden eliminarse.

Estas reglas viven exclusivamente en TaskService, garantizando separación de responsabilidades.  
Si se intenta violarlas desde la interfaz, el sistema muestra el mensaje de error correspondiente sin romper la aplicación.

---

## ¿Por qué es un monolito?

- Un solo comando despliega todo el sistema.
- Todas las capas se ejecutan dentro del mismo proceso JVM.
- No existen URLs ni puertos entre componentes internos.
- No hay comunicación HTTP interna.
- Un único artefacto de despliegue (.jar).
- Base de código unificada en un único repositorio.

La separación por capas es lógica, no física.

---

## Persistencia (Proof of Concept)

El sistema utiliza HashMap<String, Task> como almacenamiento en memoria.

- Los datos se pierden al cerrar la aplicación.
- En un entorno de producción, TaskRepository podría reemplazarse por una base de datos real sin modificar la capa de presentación ni la lógica de negocio.

Esto demuestra encapsulamiento y bajo acoplamiento.

---

## Tecnologías

- Java 22
- Swing (incluido en el JDK)
- Maven
- HashMap (persistencia en memoria — PoC)

---

## Referencias

- Richards, M. (2015). Software Architecture Patterns. O'Reilly.
- Fowler, M. (2002). Patterns of Enterprise Application Architecture.
- Newman, S. (2015). Building Microservices. O'Reilly.
- Martin, R. C. (2017). Clean Architecture. Prentice Hall.

---

Repositorio oficial:

https://github.com/HumanoidCat/task-monolith
