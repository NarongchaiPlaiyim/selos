package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_case_insurance")
public class WorkCaseInsurance extends AbstractWorkCase {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_INSURANCE_ID", sequenceName = "SEQ_WRK_CASE_INSURANCE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_INSURANCE_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "total_premium_amount", length = 16, scale = 2)
    private BigDecimal totalPremiumAmount;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    public WorkCaseInsurance(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalPremiumAmount() {
        return totalPremiumAmount;
    }

    public void setTotalPremiumAmount(BigDecimal totalPremiumAmount) {
        this.totalPremiumAmount = totalPremiumAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("appNumber", appNumber)
                .append("wobNumber", wobNumber)
                .append("lock", lock)
                .append("lockUser", lockUser)
                .append("step", step)
                .append("status", status)
                .append("totalPremiumAmount", totalPremiumAmount)
                .toString();
    }
}
