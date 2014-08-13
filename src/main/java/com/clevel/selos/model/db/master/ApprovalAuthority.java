package com.clevel.selos.model.db.master;

import com.clevel.selos.model.db.relation.UserToAuthorizationDOA;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_approval_authority")
public class ApprovalAuthority implements Serializable
{

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "active")
    private int active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("active", active)
                .toString();
    }
}
