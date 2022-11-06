package com.wzy.mvc.exception;

import lombok.Getter;

import javax.servlet.ServletException;

@Getter
public class MissingServletRequestParameterException extends ServletException {

    private String parameterName;
    private String parameterType;

    public MissingServletRequestParameterException(String parameterName, String parameterType) {
        super("Required " + parameterType + " parameter '" + parameterName + "' is not present");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

}
