package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_inbox")
public class InboxTableName implements Serializable
{

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "inbox_name")
    private String inboxname;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQueuename() {
        return inboxname;
    }

    public void setQueuename(String inboxname) {
        this.inboxname = inboxname;
    }


}
