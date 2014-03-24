package com.clevel.selos.model.db.master;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_step_status_action_role")
public class StatusIdBasedOnStepId implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "next_step_id")
    private int stepid;

    @Column(name = "next_status_id")
    private int statusid;

    public int getStepid() {
        return stepid;
    }

    public void setStepid(int stepid) {
        this.stepid = stepid;
    }

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
