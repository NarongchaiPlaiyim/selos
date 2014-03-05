package com.clevel.selos.model.db.master;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "rel_role_inbox_queue")
public class QueueNameId implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "role_id")
    private int roleid;

    @Column(name = "inbox_id")
    private int inboxid;

    @Column(name = "queue_id")
    private int queueid;

    @Column(name = "filter_condition")
    private String filtercondition;

    public int getInboxid() {
        return inboxid;
    }

    public void setInboxid(int inboxid) {
        this.inboxid = inboxid;
    }

    public String getFiltercondition() {
        return filtercondition;
    }

    public void setFiltercondition(String filtercondition) {
        this.filtercondition = filtercondition;
    }

    public int getQueueid() {
        return queueid;
    }

    public void setQueueid(int queueid) {
        this.queueid = queueid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }


}
