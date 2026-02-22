package com.taskmonolith.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    
    // Estados posibles de una tarea
    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    // Niveles de prioridad
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    //Final = no se puede cambiar despues de crearla 
    private final String id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedTo;
    

    //Constructor: se ejecuta cuando se escribe new task
    public Task(String title, String description,
            Priority priority, String assignedTo) {
        
        //Validaciones: Si esta mal, falla aqui y no se crea
        if (title == null || title.isBlank())
             throw new IllegalArgumentException("El título no puede estar vacío.");
        if (priority == null)
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        if (assignedTo == null || assignedTo.isBlank())
            throw new IllegalArgumentException("La tarea debe tener un responsable.");
    
    // Valores que se generan automáticamente
        this.id        = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;

        // Valores que vienen del exterior
        this.title       = title.trim();
        this.description = description != null ? description.trim() : "";
        this.priority    = priority;
        this.assignedTo  = assignedTo.trim();

        // El estado inicial SIEMPRE es PENDING
        this.status = Status.PENDING;
    }
    
    // Getters: permitiran leer los valores desde otras clases
    public String        getId()          { return id; }
    public String        getTitle()       { return title; }
    public String        getDescription() { return description; }
    public Status        getStatus()      { return status; }
    public Priority      getPriority()    { return priority; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public LocalDateTime getUpdatedAt()   { return updatedAt; }
    public String        getAssignedTo()  { return assignedTo; }
    
    //Setters: permite modificar solo los campos que se pueden cambiar
    public void setTitle(String title) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("El título no puede estar vacío.");
        this.title = title.trim();
    }
    
     public void setStatus(Status status) {
        if (status == null)
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        this.status = status;
    }
     
    public void setPriority(Priority priority) {
        if (priority == null)
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        this.priority = priority;
    }

    public void setAssignedTo(String assignedTo) {
        if (assignedTo == null || assignedTo.isBlank())
            throw new IllegalArgumentException("El responsable no puede estar vacío.");
        this.assignedTo = assignedTo.trim();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    //toString: muestra una tarea cuando se imprime
    @Override
    public String toString() {
        return String.format("[%s][%s] %s -> %s",
            priority, status, title, assignedTo);
    }     
}