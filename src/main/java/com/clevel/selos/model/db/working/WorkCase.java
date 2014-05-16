package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_case")
public class WorkCase extends AbstractWorkCase{
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_ID", sequenceName = "SEQ_WRK_CASE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "step_owner")
    private User stepOwner;

    @OneToOne
    @JoinColumn(name = "workcaseprescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @Column(name = "request_appraisal_require", columnDefinition = "int default 0")
    private int requestAppraisalRequire;

    @Column(name = "request_appraisal", columnDefinition = "int default 0")
    private int requestAppraisal;

    @Column(name = "request_pricing", columnDefinition = "int default 0")
    private int requestPricing;

    @Column(name = "pricing_doa_level", columnDefinition = "int default 0")
    private int pricingDoaLevel;

    @OneToOne
    @JoinColumn(name = "uw_doa1")
    private AuthorizationDOA uwDOA1;

    @OneToOne
    @JoinColumn(name = "uw_doa2")
    private AuthorizationDOA uwDOA2;

    @Column(name = "ref_app_number")
    private String refAppNumber;

    public String getRefAppNumber() {
        return refAppNumber;
    }

    public void setRefAppNumber(String refAppNumber) {
        this.refAppNumber = refAppNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWobNumber() {
        return wobNumber;
    }

    public void setWobNumber(String wobNumber) {
        this.wobNumber = wobNumber;
    }

    public User getStepOwner() {
        return stepOwner;
    }

    public void setStepOwner(User stepOwner) {
        this.stepOwner = stepOwner;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public int getRequestAppraisal() {
        return requestAppraisal;
    }

    public void setRequestAppraisal(int requestAppraisal) {
        this.requestAppraisal = requestAppraisal;
    }

    public int getRequestPricing() {
        return requestPricing;
    }

    public void setRequestPricing(int requestPricing) {
        this.requestPricing = requestPricing;
    }

    public int getPricingDoaLevel() {
        return pricingDoaLevel;
    }

    public void setPricingDoaLevel(int pricingDoaLevel) {
        this.pricingDoaLevel = pricingDoaLevel;
    }

    public AuthorizationDOA getUwDOA1() {
        return uwDOA1;
    }

    public void setUwDOA1(AuthorizationDOA uwDOA1) {
        this.uwDOA1 = uwDOA1;
    }

    public AuthorizationDOA getUwDOA2() {
        return uwDOA2;
    }

    public void setUwDOA2(AuthorizationDOA uwDOA2) {
        this.uwDOA2 = uwDOA2;
    }

    public int getRequestAppraisalRequire() {
        return requestAppraisalRequire;
    }

    public void setRequestAppraisalRequire(int requestAppraisalRequire) {
        this.requestAppraisalRequire = requestAppraisalRequire;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("stepOwner", stepOwner)
                .append("workCasePrescreen", workCasePrescreen)
                .append("requestAppraisal", requestAppraisal)
                .append("requestPricing", requestPricing)
                .append("pricingDoaLevel", pricingDoaLevel)
                .append("uwDOA1", uwDOA1)
                .append("uwDOA2", uwDOA2)
                .append("refAppNumber",refAppNumber)
                .toString();
    }
}
