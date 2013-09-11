package com.clevel.selos.integration.ncrs.models.response;
public class HeaderModel {
    private String user;
    private String password;
    private String command;
    public HeaderModel(String user,String password,String command){
        this.user = user;
        this.password = password;
        this.command = command;
    }

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