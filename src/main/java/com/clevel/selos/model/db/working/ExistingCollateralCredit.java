package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_exist_collateral_credit")
public class ExistingCollateralCredit implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXT_COLL_CREDIT_ID", sequenceName = "SEQ_WRK_EXT_COLL_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXT_COLL_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "exist_collateral_detail_id")
    private ExistingCollateralDetail existingCollateralDetail;

    @OneToOne
    @JoinColumn(name = "exist_credit_detal_id")
    private ExistingCreditDetail existingCreditDetail;

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

    public ExistingCollateralDetail getExistingCollateralDetail() {
        return existingCollateralDetail;
    }

    public void setExistingCollateralDetail(ExistingCollateralDetail existingCollateralDetail) {
        this.existingCollateralDetail = existingCollateralDetail;
    }

    public ExistingCreditDetail getExistingCreditDetail() {
        return existingCreditDetail;
    }

    public void setExistingCreditDetail(ExistingCreditDetail existingCreditDetail) {
        this.existingCreditDetail = existingCreditDetail;
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
