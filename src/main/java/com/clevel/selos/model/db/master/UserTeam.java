package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_user_team")
public class UserTeam implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "active")
    private int active;

    @Column(name = "team_name")
    private String team_name;

    @Column(name = "team_code")
    private String team_code;

    @Column(name = "team_type")
    private int team_type;

    /*@Column(name = "role")
    private String role;*/

    @Column(name = "role_id")
    private String roleId;

    public UserTeam() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_code() {
        return team_code;
    }

    public void setTeam_code(String team_code) {
        this.team_code = team_code;
    }

    public int getTeam_type() {
        return team_type;
    }

    public void setTeam_type(int team_type) {
        this.team_type = team_type;
    }

    /*public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }*/

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("description", description).
                append("active", active).
                append("team_name", team_name).
                append("team_type", team_type).
                append("team_code", team_code).
                //append("role", role).
                append("roleId",roleId).
                toString();
    }
}
