package com.TaskFlow.TF.Exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(String resourse, String id){
        super(String.format("%s : %s , Already Exists ",resourse,id));
    }
}
