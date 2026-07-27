package com.TaskFlow.TF.Controllers;

import com.TaskFlow.TF.DTOs.TaskRequest;
import com.TaskFlow.TF.DTOs.TaskResponse;
import com.TaskFlow.TF.Models.Task;
import com.TaskFlow.TF.Repositories.TaskRepository;
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
    // GET all tasks - READ from database
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    // GET single task
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // POST create task - SAVE to database
    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

//    @PutMapping("/{id}")
//    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
//        if (taskService.getTaskById(id) != null) { // Reuse the service!
//            updatedTask.setId(id);
//            return taskService.updateTask(updatedTask); // We need to add this method next
//        }
//        return null;
//    }
//
//    // DELETE
//    @DeleteMapping("/{id}")
//    public String deleteTask(@PathVariable Long id) {
//        if (taskService.getTaskById(id) != null) {
//            taskService.deleteTask(id); // We need to add this method next
//            return "Task deleted";
//        }
//        return "Task not found";
//    }
}