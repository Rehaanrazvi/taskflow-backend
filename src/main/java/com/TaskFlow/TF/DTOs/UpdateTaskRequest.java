package com.TaskFlow.TF.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;
    private String description;
    private boolean completed;
}