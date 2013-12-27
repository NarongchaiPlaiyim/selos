package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_exist_guarantor_credit")
public class ExistingGuarantorCredit implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXT_GRT_CREDIT_ID", sequenceName = "SEQ_WRK_EXT_GRT_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXT_GRT_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "exist_guarantor_detail_id")
    private ExistingGuarantorDetail existingGuarantorDetail;

    @OneToOne
    @JoinColumn(name = "exist_credit_detal_id")
    private ExistingCreditDetail existingCreditDetail;

    @Column(name = "gurantee_Amount")
    private BigDecimal guaranteeAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExistingGuarantorDetail getExistingGuarantorDetail() {
        return existingGuarantorDetail;
    }

    public void setExistingGuarantorDetail(ExistingGuarantorDetail existingGuarantorDetail) {
        this.existingGuarantorDetail = existingGuarantorDetail;
    }

    public ExistingCreditDetail getExistingCreditDetail() {
        return existingCreditDetail;
    }

    public void setExistingCreditDetail(ExistingCreditDetail existingCreditDetail) {
        this.existingCreditDetail = existingCreditDetail;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
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

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }
}
