package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_new_coll_credit")
public class NewCollateralCredit implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_CREDIT_ID", sequenceName = "SEQ_WRK_NEW_COLL_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_id")
    private NewCollateral newCollateral;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;

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

    public NewCollateral getNewCollateral() {
        return newCollateral;
    }

    public void setNewCollateral(NewCollateral newCollateral) {
        this.newCollateral = newCollateral;
    }

    public NewCreditDetail getNewCreditDetail() {
        return newCreditDetail;
    }

    public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
        this.newCreditDetail = newCreditDetail;
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
