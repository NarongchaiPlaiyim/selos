package com.clevel.selos.model.db.relation;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Prathyusha
 * Date: 2/26/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "rel_role_inbox_queue")
public class RelRoleBasedInbox implements Serializable{

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "inbox_id")
    private int inboxId;

    @Column(name = "queue_id")
    private int queueId;

    @Column(name = "filter_condition")
    private String filterCondition;

    public int getInboxId() {
        return inboxId;
    }

    public void setInboxId(int inboxId) {
        this.inboxId = inboxId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }



    public String getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
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
                append("roleId", roleId).
                append("inboxId",inboxId).
                toString();
    }
}

