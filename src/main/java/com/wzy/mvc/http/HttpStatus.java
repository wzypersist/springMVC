package com.wzy.mvc.http;

public enum HttpStatus {
    OK(200,"OK");

    private final int code;

    private final String responseCode;

    HttpStatus(int code, String responseCode) {
        this.code = code;
        this.responseCode = responseCode;
    }

    public int getCode() {
        return code;
    }

    public String getResponseCode() {
        return responseCode;
    }
}
