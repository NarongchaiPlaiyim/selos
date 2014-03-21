package com.clevel.selos.model.db.master;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wrk_case")
public class BPMActive implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "bpm_active")
    private int bpmactive;

    @Column(name = "app_number")
    private String applicationnumber;


    public String getApplicationnumber() {
        return applicationnumber;
    }

    public void setApplicationnumber(String applicationnumber) {
        this.applicationnumber = applicationnumber;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBpmactive() {
        return bpmactive;
    }

    public void setBpmactive(int bpmactive) {
        this.bpmactive = bpmactive;
    }


}
