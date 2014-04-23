package com.clevel.selos.model.view;

import com.clevel.selos.model.RoleTypeName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class RoleTypeView implements Serializable {
    private int id;
    private RoleTypeName roleTypeName;
    private String description;
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleTypeName getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(RoleTypeName roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("roleTypeName", roleTypeName)
                .append("description", description)
                .append("active", active)
                .toString();
    }
}
