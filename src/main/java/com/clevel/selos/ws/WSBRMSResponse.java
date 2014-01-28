package com.clevel.selos.ws;

import java.io.Serializable;

public enum WSBRMSResponse implements Serializable {
    APPROVE("A"), REJECT("R");
    String code;

    WSBRMSResponse(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }
}
