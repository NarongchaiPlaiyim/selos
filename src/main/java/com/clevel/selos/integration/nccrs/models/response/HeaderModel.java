package com.clevel.selos.integration.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("header")
public class HeaderModel {
    
    @XStreamAlias("user")
    private String user;
    
    @XStreamAlias("password")
    private String password;
    
    @XStreamAlias("command")
    private String command;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getCommand() {
        return command;
    }
    
}
