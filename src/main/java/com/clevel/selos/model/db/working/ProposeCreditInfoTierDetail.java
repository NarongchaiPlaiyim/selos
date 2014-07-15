package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_new_credit_tier_detail")
public class ProposeCreditInfoTierDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_CRD_TIER_DET_ID", sequenceName = "SEQ_WRK_NEW_CRD_TIER_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_CRD_TIER_DET_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "final_rate_id")
    private BaseRate finalBasePrice;

    @Column(name = "final_interest")
    private BigDecimal finalInterest;

    @Column(name = "final_interest_original")
    private BigDecimal finalInterestOriginal;

    @Column(name = "no", columnDefinition = "int default 0")
    private int no;

    @Column(name = "installment")
    private BigDecimal installment;

    @Column(name = "installment_original")
    private BigDecimal installmentOriginal;

    @Column(name = "tenor", columnDefinition = "int default 0")
    private int tenor;

    @Column(name = "brms_flag", columnDefinition = "int default 0")
    private int brmsFlag;

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

    @ManyToOne
    @JoinColumn(name = "new_credit_detail_id")
    private ProposeCreditInfo proposeCreditInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BaseRate getFinalBasePrice() {
        return finalBasePrice;
    }

    public void setFinalBasePrice(BaseRate finalBasePrice) {
        this.finalBasePrice = finalBasePrice;
    }

    public BigDecimal getFinalInterest() {
        return finalInterest;
    }

    public void setFinalInterest(BigDecimal finalInterest) {
        this.finalInterest = finalInterest;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public int getBrmsFlag() {
        return brmsFlag;
    }

    public void setBrmsFlag(int brmsFlag) {
        this.brmsFlag = brmsFlag;
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

    public ProposeCreditInfo getProposeCreditInfo() {
        return proposeCreditInfo;
    }

    public void setProposeCreditInfo(ProposeCreditInfo proposeCreditInfo) {
        this.proposeCreditInfo = proposeCreditInfo;
    }

    public BigDecimal getFinalInterestOriginal() {
        return finalInterestOriginal;
    }

    public void setFinalInterestOriginal(BigDecimal finalInterestOriginal) {
        this.finalInterestOriginal = finalInterestOriginal;
    }

    public BigDecimal getInstallmentOriginal() {
        return installmentOriginal;
    }

    public void setInstallmentOriginal(BigDecimal installmentOriginal) {
        this.installmentOriginal = installmentOriginal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("finalBasePrice", finalBasePrice).
                append("finalInterest", finalInterest).
                append("finalInterestOriginal", finalInterestOriginal).
                append("no", no).
                append("installment", installment).
                append("installmentOriginal", installmentOriginal).
                append("tenor", tenor).
                append("brmsFlag", brmsFlag).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("proposeCreditInfo", proposeCreditInfo).
                toString();
    }
}
