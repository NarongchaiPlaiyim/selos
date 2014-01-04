package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_account_info_account_name")
public class AccountInfoDetailAccountName implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ACCOUNT_INFO_AC_NAME", sequenceName = "SEQ_WRK_ACCOUNT_INFO_AC_NAME", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ACCOUNT_INFO_AC_NAME")
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_info_detail_id")
    private AccountInfoDetail accountInfoDetail;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "account_name")
    private String accountName;

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
