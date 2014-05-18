package com.clevel.selos.model.db.relation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "rel_user_authorization_doa")
public class UserToAuthorizationDOA implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "doa_id")
    private long doa_id;

    public UserToAuthorizationDOA()
    {
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    public long getDoa_id() {
        return doa_id;
    }

    public void setDoa_id(long doa_id) {
        this.doa_id = doa_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("user_id", userId).

                toString();
    }
}
