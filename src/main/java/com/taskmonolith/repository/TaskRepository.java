package com.taskmonolith.repository;

import com.taskmonolith.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskRepository {

    // archivo — clave: ID de la tarea, valor: la tarea
    private final Map<String, Task> store = new HashMap<>();

    // Guardar o actualizar una tarea
    public void save(Task task) {
        store.put(task.getId(), task);
    }

    // Buscar por ID — retorna Optional porque puede no existir
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    // Devuelve todas — copia para proteger el store
    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    // Filtrar por estado usando Streams (equivalente a SELECT WHERE en SQL)
    public List<Task> findByStatus(Task.Status status) {
        return store.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    // Filtrar por prioridad
    public List<Task> findByPriority(Task.Priority priority) {
        return store.values().stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    // Eliminar por ID — retorna true si existía, false si no
    public boolean delete(String id) {
        return store.remove(id) != null;
    }

    // Cuenta cuántas tareas hay
    public int count() {
        return store.size();
    }
}