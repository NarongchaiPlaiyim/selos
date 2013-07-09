package com.clevel.selos.exception.handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class ViewExpiredHandlerFactory extends ExceptionHandlerFactory {
    private ExceptionHandlerFactory parent;

    public ViewExpiredHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = parent.getExceptionHandler();
        result = new ViewExpiredHandler(result);
        return result;
    }
}
