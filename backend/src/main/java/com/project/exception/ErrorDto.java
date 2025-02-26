package com.project.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    private String errorCode;
    private String message;
}
