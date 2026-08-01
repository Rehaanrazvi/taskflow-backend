package com.TaskFlow.TF.Repositories;

import com.TaskFlow.TF.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUserUsername(String username);
}
