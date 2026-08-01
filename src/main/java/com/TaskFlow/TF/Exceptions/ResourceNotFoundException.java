package com.TaskFlow.TF.Exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName,Long id){
        super(String.format("%s not found with id: %d",resourceName,id));
    }

    // Constructor 2: For String identifiers (e.g., User not found with username: 'john')  <-- NEW
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(resourceName + " not found with identifier: " + identifier);
    }

    // Constructor 3: For generic messages (optional)
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
