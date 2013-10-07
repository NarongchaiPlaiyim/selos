package com.clevel.selos.integration;

import java.util.Map;

public interface EmailInterface {
    public void sendMail(String toAddress,String subject,String ccAddress,Map<String,String> valuesMap);
}
