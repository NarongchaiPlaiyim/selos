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
@Table(name = "wrk_propose_credit_tier_detail")
public class ProposeCreditTierDetail  implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PROPOSE_CREDIT_TIER_DET_ID", sequenceName = "SEQ_WRK_PROPOSE_CREDIT_TIER_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PROPOSE_CREDIT_TIER_DET_ID")
    private long id;

    @Column(name = "final_interest")
    private BigDecimal finalInterest;

    @Column(name = "no")
    private int no ;

    @Column(name = "final_price_rate")
    private String finalPriceRate ;

    @Column(name = "installment")
    private BigDecimal installment ;

    @Column(name = "standard_price")
    private String standardPrice ;

    @Column(name = "standard_interest")
    private BigDecimal standardInterest;

    @Column(name = "suggest_price")
    private String suggestPrice ;

    @Column(name = "suggest_interest")
    private BigDecimal suggestInterest ;

    @Column(name = "tenor")
    private int tenor ;

    @Column(name = "edit_flag")
    private int editFlag ;

    @OneToOne
    @JoinColumn(name = "final_rate_id")
    private BaseRate finalBasePrice ;

    @OneToOne
    @JoinColumn(name = "standard_rate_id")
    private BaseRate standardBasePrice ;

    @OneToOne
    @JoinColumn(name = "suggest_rate_id")
    private BaseRate suggestBasePrice ;


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
    private NewCreditDetail newCreditDetail;

    public NewCreditDetail getNewCreditDetail() {
        return newCreditDetail;
    }

    public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
        this.newCreditDetail = newCreditDetail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getFinalPriceRate() {
        return finalPriceRate;
    }

    public void setFinalPriceRate(String finalPriceRate) {
        this.finalPriceRate = finalPriceRate;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public String getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(String standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getStandardInterest() {
        return standardInterest;
    }

    public void setStandardInterest(BigDecimal standardInterest) {
        this.standardInterest = standardInterest;
    }

    public String getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(String suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public BigDecimal getSuggestInterest() {
        return suggestInterest;
    }

    public void setSuggestInterest(BigDecimal suggestInterest) {
        this.suggestInterest = suggestInterest;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public int getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(int editFlag) {
        this.editFlag = editFlag;
    }

    public BaseRate getFinalBasePrice() {
        return finalBasePrice;
    }

    public void setFinalBasePrice(BaseRate finalBasePrice) {
        this.finalBasePrice = finalBasePrice;
    }

    public BaseRate getStandardBasePrice() {
        return standardBasePrice;
    }

    public void setStandardBasePrice(BaseRate standardBasePrice) {
        this.standardBasePrice = standardBasePrice;
    }

    public BaseRate getSuggestBasePrice() {
        return suggestBasePrice;
    }

    public void setSuggestBasePrice(BaseRate suggestBasePrice) {
        this.suggestBasePrice = suggestBasePrice;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("finalInterest", finalInterest)
                .append("no", no)
                .append("finalPriceRate", finalPriceRate)
                .append("installment", installment)
                .append("standardPrice", standardPrice)
                .append("standardInterest", standardInterest)
                .append("suggestPrice", suggestPrice)
                .append("suggestInterest", suggestInterest)
                .append("tenor", tenor)
                .append("editFlag", editFlag)
                .append("finalBasePrice", finalBasePrice)
                .append("standardBasePrice", standardBasePrice)
                .append("suggestBasePrice", suggestBasePrice)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
