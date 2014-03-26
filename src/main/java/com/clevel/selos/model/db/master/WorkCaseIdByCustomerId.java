package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Prashanth Reddy B
 * Date: 3/25/14
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "wrk_customer")
public class WorkCaseIdByCustomerId implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "workcase_id")
    private Integer workCaseId;

    @Column(name = "workcase_prescreen_id")
    private Integer wrokCasePreScreenId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(Integer workCaseId) {
        this.workCaseId = workCaseId;
    }

    public Integer getWrokCasePreScreenId() {
        return wrokCasePreScreenId;
    }

    public void setWrokCasePreScreenId(Integer wrokCasePreScreenId) {
        this.wrokCasePreScreenId = wrokCasePreScreenId;
    }
}
