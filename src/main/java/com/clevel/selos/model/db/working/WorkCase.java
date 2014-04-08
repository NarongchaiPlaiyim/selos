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

    @Column(name = "request_appraisal", columnDefinition = "int default 0")
    private int requestAppraisal;

    @Column(name = "request_pricing", columnDefinition = "int default 0")
    private int requestPricing;

    @Column(name = "pricing_doa_level", columnDefinition = "int default 0")
    private int pricingDoaLevel;
	
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("wobNumber", wobNumber)
                .append("stepOwner", stepOwner)
                .append("workCasePrescreen", workCasePrescreen)
                .append("bpmActive", bpmActive)
                .toString();
    }
}
