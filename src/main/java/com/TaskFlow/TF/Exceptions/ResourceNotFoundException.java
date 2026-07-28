package com.TaskFlow.TF.Exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName,Long id){
        super(String.format("%s not found with id: %d",resourceName,id));
    }
}
