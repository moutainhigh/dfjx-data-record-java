package com.workbench.exception.runtime;

public class WorkbenchRuntimeException extends RuntimeException {

    public WorkbenchRuntimeException(String custMsg,Exception e){
        super(custMsg,e);
    }

}
