package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "rel_return_actions")
public class Relretunactions implements Serializable
{

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "action_code")
    private long actionCode;

    public long getActionCode() {
        return actionCode;
    }

    public void setActionCode(long actionCode) {
        this.actionCode = actionCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("actionCode",actionCode).
                toString();
    }

}
