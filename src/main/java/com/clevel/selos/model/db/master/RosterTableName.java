package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 2/25/14
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "mst_roster_name")
public class RosterTableName implements Serializable {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "roster_name")
    private String rosterName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRosterName() {
        return rosterName;
    }

    public void setRosterName(String rosterName) {
        this.rosterName = rosterName;
    }


}

