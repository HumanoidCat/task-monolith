package com.taskmonolith;

import com.taskmonolith.model.Task;
import com.taskmonolith.repository.TaskRepository;
import com.taskmonolith.service.TaskService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TaskApp extends JFrame {

    // Las mismas capas del monolito — ahora conectadas a la interfaz
    private final TaskRepository repository = new TaskRepository();
    private final TaskService    service    = new TaskService(repository);

    // Campos de entrada
    private final JTextField    titleField       = new JTextField(20);
    private final JTextField    descField        = new JTextField(20);
    private final JTextField    assignedField    = new JTextField(20);
    private final JComboBox<Task.Priority> priorityBox =
            new JComboBox<>(Task.Priority.values());

    // Lista donde se muestran las tareas
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String>            taskList  = new JList<>(listModel);

    // Guardamos las tareas para poder operar sobre ellas
    private List<Task> currentTasks;

    public TaskApp() {
        setTitle("Sistema de Gestión de Tareas - Monolito v1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // centrar en pantalla
        buildUI();
    }

    private void buildUI() {

        // ── PANEL DE CREACIÓN ─────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nueva Tarea"));

        formPanel.add(new JLabel("Título:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(descField);

        formPanel.add(new JLabel("Prioridad:"));
        formPanel.add(priorityBox);

        formPanel.add(new JLabel("Asignado a:"));
        formPanel.add(assignedField);

        JButton createBtn = new JButton("Crear Tarea");
        formPanel.add(new JLabel(""));
        formPanel.add(createBtn);

        // ── LISTA DE TAREAS ───────────────────────────────────────────────
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tareas"));

        // ── PANEL DE ACCIONES ─────────────────────────────────────────────
        JPanel actionPanel = new JPanel(new FlowLayout());

        JButton completeBtn = new JButton("✓ Completar");
        JButton inProgressBtn = new JButton("▶ En Progreso");
        JButton cancelBtn   = new JButton("✗ Cancelar");
        JButton deleteBtn   = new JButton("🗑 Eliminar");

        completeBtn.setBackground(new Color(144, 238, 144));
        inProgressBtn.setBackground(new Color(173, 216, 230));
        cancelBtn.setBackground(new Color(255, 200, 200));
        deleteBtn.setBackground(new Color(255, 160, 160));

        actionPanel.add(inProgressBtn);
        actionPanel.add(completeBtn);
        actionPanel.add(cancelBtn);
        actionPanel.add(deleteBtn);

        // ── ÁREA DE MENSAJES ──────────────────────────────────────────────
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(new Color(0, 100, 0));

        // ── LAYOUT PRINCIPAL ──────────────────────────────────────────────
        setLayout(new BorderLayout(10, 10));
        add(formPanel,   BorderLayout.NORTH);
        add(scrollPane,  BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(actionPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // ── ACCIONES DE BOTONES ───────────────────────────────────────────

        // Crear tarea
        createBtn.addActionListener(e -> {
            try {
                String title    = titleField.getText();
                String desc     = descField.getText();
                String assigned = assignedField.getText();
                Task.Priority priority =
                    (Task.Priority) priorityBox.getSelectedItem();

                service.createTask(title, desc, priority, assigned);

                // Limpiar campos
                titleField.setText("");
                descField.setText("");
                assignedField.setText("");

                refreshList();
                statusLabel.setText("✓ Tarea creada correctamente.");
                statusLabel.setForeground(new Color(0, 120, 0));

            } catch (Exception ex) {
                statusLabel.setText("✗ Error: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        });

        // Marcar en progreso
        inProgressBtn.addActionListener(e -> {
            Task selected = getSelectedTask();
            if (selected == null) return;
            try {
                service.updateStatus(selected.getId(), Task.Status.IN_PROGRESS);
                refreshList();
                statusLabel.setText("✓ Tarea marcada como EN PROGRESO.");
                statusLabel.setForeground(new Color(0, 120, 0));
            } catch (Exception ex) {
                statusLabel.setText("✗ Regla: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        });

        // Completar tarea
        completeBtn.addActionListener(e -> {
            Task selected = getSelectedTask();
            if (selected == null) return;
            try {
                service.updateStatus(selected.getId(), Task.Status.COMPLETED);
                refreshList();
                statusLabel.setText("✓ Tarea marcada como COMPLETADA.");
                statusLabel.setForeground(new Color(0, 120, 0));
            } catch (Exception ex) {
                statusLabel.setText("✗ Regla: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        });

        // Cancelar tarea
        cancelBtn.addActionListener(e -> {
            Task selected = getSelectedTask();
            if (selected == null) return;
            try {
                service.updateStatus(selected.getId(), Task.Status.CANCELLED);
                refreshList();
                statusLabel.setText("✓ Tarea cancelada.");
                statusLabel.setForeground(new Color(0, 120, 0));
            } catch (Exception ex) {
                statusLabel.setText("✗ Regla: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        });

        // Eliminar tarea
        deleteBtn.addActionListener(e -> {
            Task selected = getSelectedTask();
            if (selected == null) return;
            try {
                service.deleteTask(selected.getId());
                refreshList();
                statusLabel.setText("✓ Tarea eliminada.");
                statusLabel.setForeground(new Color(0, 120, 0));
            } catch (Exception ex) {
                statusLabel.setText("✗ Regla: " + ex.getMessage());
                statusLabel.setForeground(Color.RED);
            }
        });
    }

    // Actualiza la lista visual con las tareas actuales
    private void refreshList() {
        currentTasks = service.getAllTasks();
        listModel.clear();
        for (Task t : currentTasks) {
            listModel.addElement(
                String.format("[%s][%s] %s — %s",
                    t.getPriority(),
                    t.getStatus(),
                    t.getTitle(),
                    t.getAssignedTo())
            );
        }
    }

    // Obtiene la tarea seleccionada en la lista
    private Task getSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona una tarea de la lista primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return currentTasks.get(index);
    }

    // Punto de entrada — reemplaza al main anterior
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TaskApp().setVisible(true);
        });
    }
}