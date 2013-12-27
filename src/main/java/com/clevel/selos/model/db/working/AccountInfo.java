package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_account_info")
public class AccountInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ACCOUNT_INFO_ID", sequenceName = "SEQ_WRK_ACCOUNT_INFO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ACCOUNT_INFO_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @Column(name = "last_review_date")
    private Date lastReviewDate;

    @Column(name = "extended_review_date")
    private Date extendedReviewDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToMany(mappedBy = "accountInfo")
    private List<AccountInfoDetail> accountInfoDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
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

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
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

    public List<AccountInfoDetail> getAccountInfoDetailList() {
        return accountInfoDetailList;
    }

    public void setAccountInfoDetailList(List<AccountInfoDetail> accountInfoDetailList) {
        this.accountInfoDetailList = accountInfoDetailList;
    }
}
