package com.clevel.selos.model.db.working;

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
    private AccountInfoDetail accountInfoDetail;

    @Column(name = "purpose_id")
    private String purposeId;

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

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }
}
