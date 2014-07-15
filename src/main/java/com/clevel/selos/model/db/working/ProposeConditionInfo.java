package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_new_condition_detail")
public class ProposeConditionInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COND_DETAIL_ID", sequenceName = "SEQ_WRK_NEW_COND_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COND_DETAIL_ID")
    private long id;

    @Column(name = "loan_type")
    private String loanType ;

    @Column(name = "condition_desc")
    private String conditionDesc;

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
    @JoinColumn(name = "new_credit_facility_id")
    private ProposeLine proposeLine;

    @Column(name = "propose_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
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

    public ProposeLine getProposeLine() {
        return proposeLine;
    }

    public void setProposeLine(ProposeLine proposeLine) {
        this.proposeLine = proposeLine;
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
                append("loanType", loanType).
                append("conditionDesc", conditionDesc).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("proposeLine", proposeLine).
                append("proposeType", proposeType).
                toString();
    }
}
