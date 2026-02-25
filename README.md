# Sistema de Gestión de Tareas — Arquitectura Monolítica

**Grupo 1 | Ingeniería de Software I | Universidad Invenio **

Alejandro Zamora · Alejandro Luna · César Ubau · Saúl Ramírez
Profesor: Darin Mauricio Gamboa

---

## ¿Qué es este proyecto?

Un Sistema de Gestión de Tareas construido en Java que demuestra
la Arquitectura Monolítica. Todas las capas — modelo, repositorio,
servicio e interfaz — viven en un solo proceso JVM y se comunican
mediante llamadas directas en memoria, sin red ni HTTP entre ellas.

---

## Arquitectura

    TaskApp (Interfaz Gráfica)
        │
        ▼  llamada en memoria
    TaskService (Lógica de Negocio)
        │
        ▼  llamada en memoria
    TaskRepository (Acceso a Datos)
        │
        ▼
    HashMap<String, Task> (Almacenamiento en memoria)

Todo en un solo proceso. Un solo .jar. Eso es el monolito.

---

## Estructura del proyecto

    task-monolith/
    ├── src/main/java/com/taskmonolith/
    │   ├── model/
    │   │   └── Task.java              <- Entidad de dominio
    │   ├── repository/
    │   │   └── TaskRepository.java    <- Acceso a datos
    │   ├── service/
    │   │   └── TaskService.java       <- Reglas de negocio
    │   ├── TaskApp.java               <- Interfaz gráfica (Swing)
    │   └── TaskMonolith.java          <- Versión consola
    └── pom.xml

---

## Requisitos

- JDK 22
- Apache Maven 3.8+
- NetBeans 22 (recomendado)

---

## Cómo ejecutar

Versión con interfaz gráfica (recomendada):
1. Abrir el proyecto en NetBeans
2. Click derecho sobre el proyecto -> Properties -> Run
3. Main Class: com.taskmonolith.TaskApp
4. Click derecho -> Run

Versión consola:
1. Cambiar Main Class a: com.taskmonolith.TaskMonolith
2. Click derecho -> Run

---

## Las 3 reglas de negocio implementadas

1. Una tarea CANCELADA no puede modificarse — estado terminal
2. Una tarea COMPLETADA no puede volver a PENDING o IN_PROGRESS
3. Solo las tareas COMPLETADAS pueden eliminarse

Estas reglas viven únicamente en TaskService. Si intentas
violarlas desde la interfaz, el sistema muestra el error
en rojo sin romper la aplicación.

---

## ¿Por qué es un monolito?

- Un solo comando despliega todo el sistema
- Las capas se llaman directamente en memoria (nanosegundos)
- No existe ninguna URL ni puerto entre componentes internos
- Un solo proceso JVM ejecuta toda la aplicación

---

## Tecnologías

- Java 22
- Swing (interfaz gráfica, incluida en el JDK)
- Maven (construcción del proyecto)
- HashMap (persistencia en memoria — PoC)

---

## Referencias

- Richards, M. (2015). Software Architecture Patterns. O'Reilly.
- Fowler, M. (2002). Patterns of Enterprise Application Architecture.
- Newman, S. (2015). Building Microservices. O'Reilly.
- Martin, R. C. (2017). Clean Architecture. Prentice Hall.
