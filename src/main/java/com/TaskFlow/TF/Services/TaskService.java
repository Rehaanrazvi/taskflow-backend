package com.TaskFlow.TF.Services;

import com.TaskFlow.TF.DTOs.TaskRequest;
import com.TaskFlow.TF.DTOs.TaskResponse;
import com.TaskFlow.TF.DTOs.UpdateTaskRequest;
import com.TaskFlow.TF.Exceptions.ResourceNotFoundException;
import com.TaskFlow.TF.Models.Task;
import com.TaskFlow.TF.Models.User;
import com.TaskFlow.TF.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        String loggedInUser = authentication.getName();

        User user = userService.findByUsername(loggedInUser);
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        boolean isAdmin = auth
                .getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        List<Task> tasks;
        if (isAdmin) {
            tasks = taskRepository.findAll();
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            tasks = taskRepository.findByUserUsername(username);
        }
        return tasks.stream()
                .map(this::convertToResponse) // Convert each task
                .toList();
    }

    // 3. READ (Single)

    public TaskResponse getTaskById(Long id) {
        // 1. Fetch the task
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        // 2. Get logged-in user & check admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        String loggedInUser = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        // 3. Enforce ownership
        boolean isOwner = task.getUser() != null && task.getUser().getUsername().equals(loggedInUser);
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not own this task.");
        }

        // 4. Return the task
        return convertToResponse(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        // 1. Fetch the existing task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        // 2. Get logged-in user & check admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        String loggedInUser = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));

        boolean isOwner = existingTask.getUser() != null && existingTask.getUser().getUsername().equals(loggedInUser);
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not own this task.");
        }


        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        existingTask.setCompleted(request.isCompleted());

        // 5. Save and return
        Task updatedTask = taskRepository.save(existingTask);
        return convertToResponse(updatedTask);
    }


    // 5. DELETE
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Task", id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("User is not authenticated.");
        }
        String loggedInUser = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        boolean isOwner = task.getUser() != null && task.getUser().getUsername().equals(loggedInUser);
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not own this task or lack admin rights.");
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