package com.ing.brokagefirm.exception;

import lombok.Data;

@Data
public class AppExceptionEntity {

    private String errorMessage;
    private String requestURL;

    public AppExceptionEntity(String message, String string) {
        this.errorMessage = message;
        this.requestURL = string;
    }
}
