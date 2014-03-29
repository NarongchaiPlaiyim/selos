package com.clevel.selos.model.view;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 3/28/14
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeOwnerView implements Serializable {
    private String user;
    private String id;
    private String roleName;
    private String roleNumber;
    public String getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(String roleNumber) {
        this.roleNumber = roleNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
