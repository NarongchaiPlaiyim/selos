package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_propose_condition_detail")
public class ProposeConditionDetail  implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PROP_COND_DET_ID", sequenceName = "SEQ_WRK_PROP_COND_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PROP_COND_DET_ID")
    private long id;

    @Column(name = "no")
    private int no ;

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
    @JoinColumn(name = "credit_facility_propose_id")
    private CreditFacilityPropose creditFacilityPropose;

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

    public CreditFacilityPropose getCreditFacilityPropose() {
        return creditFacilityPropose;
    }

    public void setCreditFacilityPropose(CreditFacilityPropose creditFacilityPropose) {
        this.creditFacilityPropose = creditFacilityPropose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("loanType", loanType)
                .append("conditionDesc", conditionDesc)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("creditFacilityPropose", creditFacilityPropose)
                .toString();
    }
}
