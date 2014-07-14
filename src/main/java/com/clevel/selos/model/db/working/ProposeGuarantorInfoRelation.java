package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_new_grt_relation")
public class ProposeGuarantorInfoRelation implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_GRT_CREDIT_ID", sequenceName = "SEQ_WRK_NEW_GRT_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_GRT_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_guarantor_id")
    private ProposeGuarantorInfo proposeGuarantorInfo;

    @ManyToOne
    @JoinColumn(name = "new_credit_detail_id")
    private ProposeCreditInfo proposeCreditInfo;

    @ManyToOne
    @JoinColumn(name = "existing_credit_detail_id")
    private ExistingCreditDetail existingCreditDetail;

    @Column(name = "guarantee_amount")
    private BigDecimal guaranteeAmount;

    @ManyToOne
    @JoinColumn(name = "new_credit_facility_id")
    private ProposeLine proposeLine;

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

    @Column(name = "propose_type")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProposeGuarantorInfo getProposeGuarantorInfo() {
        return proposeGuarantorInfo;
    }

    public void setProposeGuarantorInfo(ProposeGuarantorInfo proposeGuarantorInfo) {
        this.proposeGuarantorInfo = proposeGuarantorInfo;
    }

    public ProposeCreditInfo getProposeCreditInfo() {
        return proposeCreditInfo;
    }

    public void setProposeCreditInfo(ProposeCreditInfo proposeCreditInfo) {
        this.proposeCreditInfo = proposeCreditInfo;
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

    public ProposeLine getProposeLine() {
        return proposeLine;
    }

    public void setProposeLine(ProposeLine proposeLine) {
        this.proposeLine = proposeLine;
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

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("proposeGuarantorInfo", proposeGuarantorInfo).
                append("proposeCreditInfo", proposeCreditInfo).
                append("existingCreditDetail", existingCreditDetail).
                append("guaranteeAmount", guaranteeAmount).
                append("proposeLine", proposeLine).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("proposeType", proposeType).
                toString();
    }
}
