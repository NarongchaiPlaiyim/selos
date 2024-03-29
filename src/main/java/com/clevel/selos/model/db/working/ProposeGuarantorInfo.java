package com.clevel.selos.model.db.working;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.GuarantorCategory;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_guarantor_detail")
public class ProposeGuarantorInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_GRT_DET_ID", sequenceName = "SEQ_WRK_NEW_GRT_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_GRT_DET_ID")
    private long id;

    @Column(name = "no")
    private int no;

    @Column(name = "propose_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    @Column(name = "uw_decision", columnDefinition = "int default 0", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private DecisionType uwDecision;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer guarantorName;

    @Column(name = "guarantor_category", columnDefinition = "int default 0", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private GuarantorCategory guarantorCategory;

    @Column(name = "tcg_lg_no")
    private String tcgLgNo;

    @Column(name = "total_limit_guarantee_amount")
    private BigDecimal totalLimitGuaranteeAmount;

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

    @OneToMany(mappedBy = "proposeGuarantorInfo")
    private List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList;

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

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public Customer getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(Customer guarantorName) {
        this.guarantorName = guarantorName;
    }

    public GuarantorCategory getGuarantorCategory() {
        return guarantorCategory;
    }

    public void setGuarantorCategory(GuarantorCategory guarantorCategory) {
        this.guarantorCategory = guarantorCategory;
    }

    public String getTcgLgNo() {
        return tcgLgNo;
    }

    public void setTcgLgNo(String tcgLgNo) {
        this.tcgLgNo = tcgLgNo;
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
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

    public List<ProposeGuarantorInfoRelation> getProposeGuarantorInfoRelationList() {
        return proposeGuarantorInfoRelationList;
    }

    public void setProposeGuarantorInfoRelationList(List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList) {
        this.proposeGuarantorInfoRelationList = proposeGuarantorInfoRelationList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("no", no).
                append("proposeType", proposeType).
                append("uwDecision", uwDecision).
                append("guarantorName", guarantorName).
                append("guarantorCategory", guarantorCategory).
                append("tcgLgNo", tcgLgNo).
                append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount).
                append("proposeLine", proposeLine).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("proposeGuarantorInfoRelationList", proposeGuarantorInfoRelationList).
                toString();
    }
}