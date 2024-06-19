package com.luizmarinho.literalura.exception;

public class ExceptionOnApi extends Exception{
    private String message;

    public ExceptionOnApi() {
        super();
    }

    public ExceptionOnApi(String message) {
        super(message);
        this.message = message;
    }


}
