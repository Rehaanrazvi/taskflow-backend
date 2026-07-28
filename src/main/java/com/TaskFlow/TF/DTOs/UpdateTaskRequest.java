package com.TaskFlow.TF.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {
    private String title;
    private String description;
    private boolean completed;
}