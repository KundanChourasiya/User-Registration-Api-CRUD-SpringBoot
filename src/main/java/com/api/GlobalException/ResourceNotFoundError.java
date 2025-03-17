package com.api.GlobalException;

public class ResourceNotFoundError extends  RuntimeException{
    public ResourceNotFoundError() {
    }

    public ResourceNotFoundError(String message) {
        super(message);
    }
}
