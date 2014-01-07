package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_account_info_purpose")
public class AccountInfoDetailPurpose implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ACCOUNT_INFO_PURPOSE", sequenceName = "SEQ_WRK_ACCOUNT_INFO_PURPOSE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ACCOUNT_INFO_PURPOSE")
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_info_detail_id")
    private AccountInfoDetail accountInfoDetailPurpose;

    @Column(name = "purpose_id")
    private long purposeId;

    @Column(name = "purpose_name")
    private String purposeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountInfoDetail getAccountInfoDetailPurpose() {
        return accountInfoDetailPurpose;
    }

    public void setAccountInfoDetailPurpose(AccountInfoDetail accountInfoDetailPurpose) {
        this.accountInfoDetailPurpose = accountInfoDetailPurpose;
    }

    public long getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(long purposeId) {
        this.purposeId = purposeId;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountInfoDetailPurpose", accountInfoDetailPurpose)
                .append("purposeId", purposeId)
                .append("purposeName", purposeName)
                .toString();
    }
}
