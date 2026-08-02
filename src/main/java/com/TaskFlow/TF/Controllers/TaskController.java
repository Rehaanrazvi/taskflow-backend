package com.TaskFlow.TF.Controllers;

import com.TaskFlow.TF.DTOs.TaskRequest;
import com.TaskFlow.TF.DTOs.TaskResponse;
import com.TaskFlow.TF.DTOs.UpdateTaskRequest;
import com.TaskFlow.TF.Services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // GET all
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    // GET by ID
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // POST create
    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    // PUT update
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully";
    }
}