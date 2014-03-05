package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_inbox_queue")
public class FetchQueueName implements Serializable
{


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQueuename() {
        return queuename;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "queue_name")
    private String queuename;

}
