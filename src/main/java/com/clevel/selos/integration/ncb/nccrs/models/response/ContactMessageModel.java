package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ContactMessageModel implements Serializable {
    private String email;
    private String phone;

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
