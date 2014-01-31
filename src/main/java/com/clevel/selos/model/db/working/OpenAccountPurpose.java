package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BankAccountPurpose;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_open_account_purpose")
public class OpenAccountPurpose implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_PUR_ID", sequenceName = "SEQ_WRK_OPEN_ACC_PUR_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_PUR_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @OneToOne
    @JoinColumn(name = "bank_account_purpose_id")
    private BankAccountPurpose AccountPurpose;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OpenAccount getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(OpenAccount openAccount) {
        this.openAccount = openAccount;
    }

    public BankAccountPurpose getAccountPurpose() {
        return AccountPurpose;
    }

    public void setAccountPurpose(BankAccountPurpose accountPurpose) {
        AccountPurpose = accountPurpose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("openAccount", openAccount)
                .append("AccountPurpose", AccountPurpose)
                .toString();
    }
}
