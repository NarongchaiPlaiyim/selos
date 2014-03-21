package com.clevel.selos.model.db.master;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wrk_case_owner")
public class SearchUserId implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "workcase_id")
    private int workcaseid;

    @Column(name = "user_id")
    private String userid;

    public int getWorkcaseid() {
        return workcaseid;
    }

    public void setWorkcaseid(int workcaseid) {
        this.workcaseid = workcaseid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
