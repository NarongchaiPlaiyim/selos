package com.clevel.selos.model;

public enum ParallelAppraisalStatus {
    NO_REQUEST(0),
    REQUESTING_PARALLEL(1),
    REQUESTED_PARALLEL(2),
    PARALLEL_COMPLETED(3);

    int value;

    ParallelAppraisalStatus(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
