package com.clevel.selos.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserDetail {
    private String userName;
    private String userPassword;
    private String role;
    private String roleType;

    public UserDetail(String userName, String role, String roleType) {
        this.userName = userName;
        this.role = role;
        this.roleType = roleType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("userName", userName).
                append("role", role).
                append("roleType", roleType).
                toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof UserDetail) {
            final UserDetail other = (UserDetail) obj;
            return new EqualsBuilder().append(userName,other.userName).isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(userName).toHashCode();
    }
}
