package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Prashanth Reddy B
 * Date: 3/25/14
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "wrk_case_prescreen")
public class SearchApplicationNoByWorkCasePrescreenId {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "app_number")
    private String applicationNo;

    @Column(name = "bpm_active")
    private Integer bpmActive;

    public Integer getBpmActive() {
        return bpmActive;
    }

    public void setBpmActive(Integer bpmActive) {
        this.bpmActive = bpmActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }
}
