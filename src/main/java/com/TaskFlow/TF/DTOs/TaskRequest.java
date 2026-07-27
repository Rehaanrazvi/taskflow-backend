package com.TaskFlow.TF.DTOs;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotNull(message = "User ID is required")
    private Long user_id;
    private String title;
    private  String description;
    private boolean completed;

}
