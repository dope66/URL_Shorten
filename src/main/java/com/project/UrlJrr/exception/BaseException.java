package com.project.UrlJrr.exception;

public abstract class BaseException extends RuntimeException{
    public abstract BaseExceptionType getExceptionType();
}
