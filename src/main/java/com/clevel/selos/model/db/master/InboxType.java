package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Prathyusha
 * Date: 2/26/14
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "mst_inbox")
public class InboxType implements Serializable{

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "inbox_name")
    private String inbox_name;

     public String getInbox_name() {
        return inbox_name;
    }

    public void setInbox_name(String inbox_name) {
        this.inbox_name = inbox_name;
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
                append("inbox_name", inbox_name).
                toString();
    }

}
