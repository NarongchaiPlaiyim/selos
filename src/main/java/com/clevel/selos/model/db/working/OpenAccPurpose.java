package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_open_account_purpose")
public class OpenAccPurpose implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_PUR_ID", sequenceName = "SEQ_WRK_OPEN_ACC_PUR_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_PUR_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @Column(name = "purpose_name")
    private String purposeName;

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

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("openAccount", openAccount).
                append("purposeName", purposeName).
                toString();
    }
}
