package com.taskmonolith;

import com.taskmonolith.model.Task;
import com.taskmonolith.repository.TaskRepository;
import com.taskmonolith.service.TaskService;

public class TaskMonolith {

    public static void main(String[] args) {

        System.out.println(" Sistema de Gestión de Tareas - Monolito v1.0   ");
        System.out.println(" Grupo 1 | Ingeniería de Software I | Invenio   ");

        // CONEXIÓN DE CAPAS
        // Todo en el mismo proceso JVM — esto es el monolito.
        // No hay URLs, no hay puertos, no hay red entre estas líneas.
        TaskRepository repository = new TaskRepository();
        TaskService    service    = new TaskService(repository);

        // ── FASE 1: CREAR TAREAS
        System.out.println("\nFASE 1: Creando tareas");

        Task t1 = service.createTask(
            "Implementar módulo de login",
            "Autenticación con usuario y contraseña",
            Task.Priority.HIGH,
            "Alejandro Zamora"
        );

        Task t2 = service.createTask(
            "Corregir bug crítico en pagos",
            "NullPointerException al procesar tarjeta",
            Task.Priority.CRITICAL,
            "Cesar Ubau"
        );

        Task t3 = service.createTask(
            "Escribir pruebas unitarias",
            "Cobertura mínima del 80% en TaskService",
            Task.Priority.MEDIUM,
            "Saul Ramirez"
        );

        Task t4 = service.createTask(
            "Actualizar documentación",
            "Swagger con los nuevos endpoints",
            Task.Priority.LOW,
            "Alejandro Luna"
        );

        System.out.println("\nTotal de tareas creadas: " + service.getTotalCount());

        // FASE 2: ACTUALIZAR ESTADOS
        System.out.println("\nFASE 2: Actualizando estados");

        service.updateStatus(t1.getId(), Task.Status.IN_PROGRESS);
        service.updateStatus(t2.getId(), Task.Status.IN_PROGRESS);
        service.updateStatus(t3.getId(), Task.Status.COMPLETED);
        service.updateStatus(t4.getId(), Task.Status.CANCELLED);

        // FASE 3: DEMOSTRAR REGLAS DE NEGOCIO
        System.out.println("\nFASE 3: Demostrando reglas de negocio");

        // Regla 1: no se puede modificar una tarea CANCELADA
        System.out.println("\nIntentando modificar tarea CANCELADA...");
        try {
            service.updateStatus(t4.getId(), Task.Status.IN_PROGRESS);
        } catch (IllegalStateException e) {
            System.out.println("[REGLA CUMPLIDA] " + e.getMessage());
        }

        // Regla 2: una tarea COMPLETED no puede volver atrás
        System.out.println("\nIntentando regresar tarea COMPLETADA a PENDING...");
        try {
            service.updateStatus(t3.getId(), Task.Status.PENDING);
        } catch (IllegalStateException e) {
            System.out.println("[REGLA CUMPLIDA] " + e.getMessage());
        }

        // Regla 3: solo COMPLETED puede eliminarse
        System.out.println("\nIntentando eliminar tarea IN_PROGRESS...");
        try {
            service.deleteTask(t1.getId());
        } catch (IllegalStateException e) {
            System.out.println("[REGLA CUMPLIDA] " + e.getMessage());
        }

        // ── FASE 4: LISTAR Y FILTRAR
        System.out.println("\nFASE 4: Listado de tareas");

        System.out.println("\nTodas las tareas:");
        service.getAllTasks().forEach(t ->
            System.out.println("  " + t)
        );

        System.out.println("\nTareas CRÍTICAS:");
        service.getCriticalTasks().forEach(t ->
            System.out.println("  CRITICA -> " + t.getTitle()
                + " [" + t.getAssignedTo() + "]")
        );

        System.out.println("\nTareas en progreso:");
        service.getTasksByStatus(Task.Status.IN_PROGRESS).forEach(t ->
            System.out.println("  EN PROGRESO -> " + t.getTitle())
        );

        // ── FASE 5: ELIMINAR 
        System.out.println("\nFASE 5: Eliminando tarea completada");

        service.deleteTask(t3.getId());
        System.out.println("Tareas restantes: " + service.getTotalCount());

        System.out.println("\nDemo completada. Todas las capas ejecutadas     ");
        System.out.println(" en un solo proceso JVM — Arquitectura Monolítica");
    }
}
