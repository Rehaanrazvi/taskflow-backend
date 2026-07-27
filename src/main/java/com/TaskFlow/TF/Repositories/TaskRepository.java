package com.TaskFlow.TF.Repositories;

import com.TaskFlow.TF.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {

}
