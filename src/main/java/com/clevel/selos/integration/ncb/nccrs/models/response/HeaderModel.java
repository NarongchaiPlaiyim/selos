package com.clevel.selos.integration.ncb.nccrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("header")
public class HeaderModel implements Serializable {

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
