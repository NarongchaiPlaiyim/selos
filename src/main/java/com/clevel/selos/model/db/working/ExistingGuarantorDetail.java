package com.clevel.selos.model.db.working;

import com.clevel.selos.model.GuarantorCategory;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_exist_guarantor_detail")
public class ExistingGuarantorDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXT_GRT_DETAIL_ID", sequenceName = "SEQ_WRK_EXT_GRT_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXT_GRT_DETAIL_ID")
    private long id;

    @Column(name = "no")
    private int no ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guarantor_id")
    private Customer guarantorName;

    @Column(name = "tcglg_no")
    private String tcglgNo;

    @Column(name = "total_gurantee_Amount")
    private BigDecimal totalLimitGuaranteeAmount;

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
    @JoinColumn(name = "existing_credit_facility_id")
    private ExistingCreditFacility existingCreditFacility;

    @OneToMany(mappedBy = "existingGuarantorDetail", cascade = CascadeType.ALL)
    private List<ExistingGuarantorCredit> existingGuarantorCreditList;

    @Column(name = "guarantor_category", columnDefinition = "int default 0", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private GuarantorCategory guarantorCategory;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Customer getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(Customer guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getTcglgNo() {
        return tcglgNo;
    }

    public void setTcglgNo(String tcglgNo) {
        this.tcglgNo = tcglgNo;
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
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

    public ExistingCreditFacility getExistingCreditFacility() {
        return existingCreditFacility;
    }

    public void setExistingCreditFacility(ExistingCreditFacility existingCreditFacility) {
        this.existingCreditFacility = existingCreditFacility;
    }

    public List<ExistingGuarantorCredit> getExistingGuarantorCreditList() {
        return existingGuarantorCreditList;
    }

    public void setExistingGuarantorCreditList(List<ExistingGuarantorCredit> existingGuarantorCreditList) {
        this.existingGuarantorCreditList = existingGuarantorCreditList;
    }

    public GuarantorCategory getGuarantorCategory() {
        return guarantorCategory;
    }

    public void setGuarantorCategory(GuarantorCategory guarantorCategory) {
        this.guarantorCategory = guarantorCategory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("no", no).
                append("guarantorName", guarantorName).
                append("tcglgNo", tcglgNo).
                append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("existingCreditFacility", existingCreditFacility).
                append("existingGuarantorCreditList", existingGuarantorCreditList).
                append("guarantorCategory", guarantorCategory).
                toString();
    }
}
