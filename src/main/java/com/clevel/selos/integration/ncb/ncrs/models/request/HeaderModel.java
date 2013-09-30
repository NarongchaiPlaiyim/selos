package com.clevel.selos.integration.ncb.ncrs.models.request;

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
            
    public HeaderModel(String user,String password,String command){
        this.user = user;
        this.password = password;
        this.command = command;
    }
    
}
