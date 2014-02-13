package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_new_grt_relation")
public class NewGuarantorCredit implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_GRT_CREDIT_ID", sequenceName = "SEQ_WRK_NEW_GRT_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_GRT_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_guarantor_id")
    private NewGuarantorDetail newGuarantorDetail;

    @ManyToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;

    @OneToOne
    @JoinColumn(name = "existing_credit_detail_id")
    private ExistingCreditDetail existingCreditDetail;

    @Column(name = "guarantee_amount")
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

    public NewGuarantorDetail getNewGuarantorDetail() {
        return newGuarantorDetail;
    }

    public void setNewGuarantorDetail(NewGuarantorDetail newGuarantorDetail) {
        this.newGuarantorDetail = newGuarantorDetail;
    }

    public NewCreditDetail getNewCreditDetail() {
        return newCreditDetail;
    }

    public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
        this.newCreditDetail = newCreditDetail;
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

    public ExistingCreditDetail getExistingCreditDetail() {
        return existingCreditDetail;
    }

    public void setExistingCreditDetail(ExistingCreditDetail existingCreditDetail) {
        this.existingCreditDetail = existingCreditDetail;
    }
}
