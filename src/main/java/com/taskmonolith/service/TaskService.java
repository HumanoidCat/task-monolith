package com.taskmonolith.service;

import com.taskmonolith.model.Task;
import com.taskmonolith.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskRepository repository;

    // Constructor injection — D de SOLID
    // TaskService recibe el repositorio, no lo crea
    public TaskService(TaskRepository repository) {
        if (repository == null)
            throw new IllegalArgumentException("El repositorio no puede ser nulo.");
        this.repository = repository;
    }

    // Crea una tarea nueva
    public Task createTask(String title, String description,
                           Task.Priority priority, String assignedTo) {
        Task task = new Task(title, description, priority, assignedTo);
        repository.save(task); // llamada en memoria — no es HTTP
        System.out.println("[SERVICE] Tarea creada: " + task);
        return task;
    }

    // Cambia el estado de una tarea
    public Task updateStatus(String id, Task.Status newStatus) {

        // Busca la tarea o falla inmediatamente
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));

        // Regla 1 CANCELLED no se puede modificar
        if (task.getStatus() == Task.Status.CANCELLED)
            throw new IllegalStateException("No se puede modificar una tarea CANCELADA.");

        // Regla 2 COMPLETED no puede volver atrás
        if (task.getStatus() == Task.Status.COMPLETED
                && (newStatus == Task.Status.PENDING
                    || newStatus == Task.Status.IN_PROGRESS))
            throw new IllegalStateException("Una tarea COMPLETADA no puede volver a " + newStatus);

        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());
        repository.save(task);
        System.out.println("[SERVICE] Estado actualizado: " + task);
        return task;
    }

    // Eliminar una tarea
    public boolean deleteTask(String id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));

        // Regla 3: solo COMPLETED puede eliminarse
        if (task.getStatus() != Task.Status.COMPLETED)
            throw new IllegalStateException(
                "Solo tareas COMPLETADAS pueden eliminarse. Estado actual: "
                + task.getStatus());

        System.out.println("[SERVICE] Tarea eliminada: " + task.getTitle());
        return repository.delete(id);
    }

    // Consultas — sin reglas de negocio, solo delegan al repositorio
    public List<Task> getAllTasks()                    { return repository.findAll(); }
    public List<Task> getTasksByStatus(Task.Status s) { return repository.findByStatus(s); }
    public List<Task> getCriticalTasks()              { return repository.findByPriority(Task.Priority.CRITICAL); }
    public int        getTotalCount()                 { return repository.count(); }
}
