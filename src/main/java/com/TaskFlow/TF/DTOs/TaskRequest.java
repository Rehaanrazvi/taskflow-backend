package com.TaskFlow.TF.DTOs;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    private String title;
    private  String description;
    private boolean completed;

}
