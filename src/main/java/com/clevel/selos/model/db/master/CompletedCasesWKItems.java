package com.clevel.selos.model.db.master;


import javax.persistence.*;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

@Entity
@Table(name = "wrk_case")
public class CompletedCasesWKItems implements Serializable
{

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "app_number")
    private String applicationNo;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;

    @Column(name = "wob_number")
    private String wobnumber;

    @Column(name = "status_id")
    private Integer statusid;

    @Column(name = "step_id")
    private Integer stepid;

    @Column(name = "appointment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate;

    @Column(name = "bpm_active")
    private Integer bpmActive;


    public Date getReceiveddate() {
        return receiveddate;
    }

    public void setReceiveddate(Date receiveddate) {
        this.receiveddate = receiveddate;
    }

    @Column(name = "received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveddate;

    @Column(name = "sla_end_date")
    private Date slaenddate;

    @Column(name = "total_time_at_process")
    private Integer totaltimeatprocess;

    @Column(name = "total_time_at_user")
    private Integer totaltimeatuser;

    @Column(name = "at_user_id")
    private Integer atUserTeamId;

    @Column(name = "product_group_id")
    private Integer productgroupid;

    @Column(name = "request_type_id")
    private Integer requesttypeid;

    @Column(name = "from_user_id")
    private String fromuserid;


    @Column(name = "doa_level_id")
    private Integer doalevelid;


    public Integer getProductgroupid() {
        return productgroupid;
    }

    public void setProductgroupid(Integer productgroupid) {
        this.productgroupid = productgroupid;
    }

    public Integer getRequesttypeid() {
        return requesttypeid;
    }

    public void setRequesttypeid(Integer requesttypeid) {
        this.requesttypeid = requesttypeid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getBpmActive() {
        return bpmActive;
    }

    public void setBpmActive(Integer bpmActive) {
        this.bpmActive = bpmActive;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }


    public Integer getAtUserTeamId() {
        return atUserTeamId;
    }

    public void setAtUserTeamId(Integer atUserTeamId) {
        this.atUserTeamId = atUserTeamId;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Integer getStepid() {
        return stepid;
    }

    public void setStepid(Integer stepid) {
        this.stepid = stepid;
    }

    public Integer getStatusid() {
        return statusid;
    }

    public void setStatusid(Integer statusid) {
        this.statusid = statusid;
    }

    public String getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(String fromuserid) {
        this.fromuserid = fromuserid;
    }



    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getDoalevelid() {
        return doalevelid;
    }

    public void setDoalevelid(Integer doalevelid) {
        this.doalevelid = doalevelid;
    }

    public Date getSlaenddate() {
        return slaenddate;
    }

    public void setSlaenddate(Date slaenddate) {
        this.slaenddate = slaenddate;
    }

    public Integer getTotaltimeatprocess() {
        return totaltimeatprocess;
    }

    public void setTotaltimeatprocess(Integer totaltimeatprocess) {
        this.totaltimeatprocess = totaltimeatprocess;
    }

    public Integer getTotaltimeatuser() {
        return totaltimeatuser;
    }

    public void setTotaltimeatuser(Integer totaltimeatuser) {
        this.totaltimeatuser = totaltimeatuser;
    }

    public String getWobnumber() {
        return wobnumber;
    }

    public void setWobnumber(String wobnumber) {
        this.wobnumber = wobnumber;
    }

}
