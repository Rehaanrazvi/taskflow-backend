package com.TaskFlow.TF.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Title cannot be empty")
    @Schema(description = "Title of the task", example = "Finish Spring Boot project", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Detailed description", example = "Implement Swagger and push to GitHub")
    private  String description;

    @Schema(description = "Task completion status", example = "false")
    private boolean completed;

}
