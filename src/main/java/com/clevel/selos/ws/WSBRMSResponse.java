package com.clevel.selos.ws;

import java.io.Serializable;

public enum WSBRMSResponse implements Serializable {
    NOT_REJECT(0), REJECT(1);
    int code;

    WSBRMSResponse(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
