package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_account_info_detail_purpos")
public class AccountInfoDetailPurpose implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ACCOUNT_INFO_DETAIL_PURPOSE_ID", sequenceName = "SEQ_WRK_ACCOUNT_INFO_DETAIL_PURPOSE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ACCOUNT_INFO_DETAIL_PURPOSE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_info_detail_id")
    private AccountInfoDetail accountInfoDetail;

    @Column(name = "purpose_name")
    private String purposeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountInfoDetail getAccountInfoDetail() {
        return accountInfoDetail;
    }

    public void setAccountInfoDetail(AccountInfoDetail accountInfoDetail) {
        this.accountInfoDetail = accountInfoDetail;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }
}
