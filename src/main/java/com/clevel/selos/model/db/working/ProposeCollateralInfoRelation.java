package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_new_coll_credit")
public class ProposeCollateralInfoRelation implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_CREDIT_ID", sequenceName = "SEQ_WRK_NEW_COLL_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_id")
    private ProposeCollateralInfo proposeCollateral;

    @ManyToOne
    @JoinColumn(name = "new_credit_detail_id")
    private ProposeCreditInfo proposeCreditInfo;

    @ManyToOne
    @JoinColumn(name = "existing_credit_detail_id")
    private ExistingCreditDetail existingCreditDetail;

    @ManyToOne
    @JoinColumn(name = "new_credit_facility_id")
    private ProposeLine proposeLine;

    @Column(name = "propose_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private User createBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProposeCollateralInfo getProposeCollateral() {
        return proposeCollateral;
    }

    public void setProposeCollateral(ProposeCollateralInfo proposeCollateral) {
        this.proposeCollateral = proposeCollateral;
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
