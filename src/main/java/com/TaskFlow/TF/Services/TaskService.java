package com.TaskFlow.TF.Services;

import com.TaskFlow.TF.DTOs.TaskRequest;
import com.TaskFlow.TF.DTOs.TaskResponse;
import com.TaskFlow.TF.DTOs.UpdateTaskRequest;
import com.TaskFlow.TF.Exceptions.ResourceNotFoundException;
import com.TaskFlow.TF.Models.Task;
import com.TaskFlow.TF.Models.User;
import com.TaskFlow.TF.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    // 1. CREATE
    public TaskResponse createTask(TaskRequest request) {
        // Guard clause
        if (request.getUser_id() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userService.findById(request.getUser_id());
        Task task = new Task();
        task.setUser(user);
        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());
        task.setDescription(request.getDescription());

        Task savedTask = taskRepository.save(task);
        return convertToResponse(savedTask);
    }

    // 2. READ (All)
    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToResponse) // Convert each task
                .toList();
    }

    // 3. READ (Single)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return convertToResponse(task);
    }

    // 4. UPDATE (We'll finish this later)
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        // Find the existing task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        // Update only the fields that are provided (not null)
        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        // For boolean, we always update it (since it has a default)
        existingTask.setCompleted(request.isCompleted());

        // Save and convert to DTO
        Task updatedTask = taskRepository.save(existingTask);
        return convertToResponse(updatedTask);
    }


    // 5. DELETE
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", id);
        }
        taskRepository.deleteById(id);
    }

    // 6. HELPER: Convert Entity → DTO (Excludes password & full user object)
    private TaskResponse convertToResponse(Task task) {
        // Handle old tasks that might have null user
        String username = (task.getUser() != null) ? task.getUser().getUsername() : "unknown";

        return new TaskResponse(
                task.getId(),       // Task ID
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                username            // Only the username, no password!
        );
    }
}