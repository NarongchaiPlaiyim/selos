package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="wrk_prescreen")
public class Prescreen implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_PRESCREEN_ID", sequenceName="SEQ_WRK_PRESCREEN_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_PRESCREEN_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @Temporal(TemporalType.DATE)
    @Column(name="expected_submit_date")
    private Date expectedSubmitDate;

    @OneToOne
    @JoinColumn(name="business_location_id")
    private Province businessLocation;

    @Temporal(TemporalType.DATE)
    @Column(name="register_date")
    private Date registerDate;

    @Column(name="tcg")
    private boolean tcg;

    @Column(name="refinance")
    private boolean refinance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public Province getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(Province businessLocation) {
        this.businessLocation = businessLocation;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isTcg() {
        return tcg;
    }

    public void setTcg(boolean tcg) {
        this.tcg = tcg;
    }

    public boolean isRefinance() {
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }
}
