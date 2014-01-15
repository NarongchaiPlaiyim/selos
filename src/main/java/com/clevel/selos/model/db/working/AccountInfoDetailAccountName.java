package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private AccountInfoDetail accountInfoDetailAccountName;

    @Column(name = "account_name_id")
    private long accountNameId;

    @Column(name = "account_name")
    private String accountName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountInfoDetail getAccountInfoDetailAccountName() {
        return accountInfoDetailAccountName;
    }

    public void setAccountInfoDetailAccountName(AccountInfoDetail accountInfoDetailAccountName) {
        this.accountInfoDetailAccountName = accountInfoDetailAccountName;
    }

    public long getAccountNameId() {
        return accountNameId;
    }

    public void setAccountNameId(long accountNameId) {
        this.accountNameId = accountNameId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountInfoDetailAccountName", accountInfoDetailAccountName)
                .append("accountNameId", accountNameId)
                .append("accountName", accountName)
                .toString();
    }
}
