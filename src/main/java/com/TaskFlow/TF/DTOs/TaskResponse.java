package com.TaskFlow.TF.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponse {
    private Long id;          // Changed from 'user_id' to 'id'
    private String title;
    private String description;
    private boolean completed;
    private String username;
}