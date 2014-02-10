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
public class NewCreditTierDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_CRD_TIER_DET_ID", sequenceName = "SEQ_WRK_NEW_CRD_TIER_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_CRD_TIER_DET_ID")
    private long id;

    @Column(name = "final_interest")
    private BigDecimal finalInterest;

    @Column(name = "no")
    private int no ;

    @Column(name = "installment")
    private BigDecimal installment ;

    @Column(name = "tenor")
    private int tenor ;

    @Column(name = "edit_flag")
    private int editFlag ;

    @OneToOne
    @JoinColumn(name = "final_rate_id")
    private BaseRate finalBasePrice ;

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
                .append("installment", installment)
                .append("tenor", tenor)
                .append("editFlag", editFlag)
                .append("finalBasePrice", finalBasePrice)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
