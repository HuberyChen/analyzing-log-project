package com.quidsi.log.analyzing.util;


public abstract class Decorator implements IFileScanOperation {

    protected IFileScanOperation operation;

    public void setOperation(IFileScanOperation operation) {
        this.operation = operation;
    }

    public void getTime() {
        operation.getTime();
    }
}
