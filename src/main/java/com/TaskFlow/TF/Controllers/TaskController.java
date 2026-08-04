package com.TaskFlow.TF.Controllers;

import com.TaskFlow.TF.DTOs.TaskRequest;
import com.TaskFlow.TF.DTOs.TaskResponse;
import com.TaskFlow.TF.DTOs.UpdateTaskRequest;
import com.TaskFlow.TF.Services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Management", description = "Endpoints for creating, reading, updating, and deleting tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // GET all
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns a list of tasks for the authenticated user (USER sees only theirs, ADMIN sees all).")
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    // GET by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Fetches a single task. User can only access their own tasks; ADMIN can access all.")
    @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this task)")
    @ApiResponse(responseCode = "404", description = "Task not found")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // POST create
    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a task for the authenticated user. The user_id is derived from the JWT token, not from the request body.")
    @ApiResponse(responseCode = "400", description = "Validation failed (e.g., empty title)")
    @ApiResponse(responseCode = "401", description = "Unauthorized (missing or invalid JWT)")
    public TaskResponse createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    // PUT update
    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task. User can only update their own tasks; ADMIN can update all.")
    @ApiResponse(responseCode = "403", description = "Forbidden (user does not own this task)")
    @ApiResponse(responseCode = "404", description = "Task not found")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task. Only the task owner or an ADMIN can perform this action.")
    @ApiResponse(responseCode = "403", description = "Forbidden (user does not own the task and is not an ADMIN)")
    @ApiResponse(responseCode = "404", description = "Task not found")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully";
    }
}