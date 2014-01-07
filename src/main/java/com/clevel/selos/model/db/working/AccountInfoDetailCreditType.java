package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_account_info_credit_type")
public class AccountInfoDetailCreditType implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ACCOUNT_INFO_CREDIT", sequenceName = "SEQ_WRK_ACCOUNT_INFO_CREDIT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ACCOUNT_INFO_CREDIT")
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_info_detail_id")
    private AccountInfoDetail accountInfoDetailCreditType;

    @Column(name = "credit_type_id")
    private long creditTypeId;

    @Column(name = "credit_type_name")
    private String productProgram;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "credit_facility")
    private String CreditFacility;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountInfoDetail getAccountInfoDetailCreditType() {
        return accountInfoDetailCreditType;
    }

    public void setAccountInfoDetailCreditType(AccountInfoDetail accountInfoDetailCreditType) {
        this.accountInfoDetailCreditType = accountInfoDetailCreditType;
    }

    public long getCreditTypeId() {
        return creditTypeId;
    }

    public void setCreditTypeId(long creditTypeId) {
        this.creditTypeId = creditTypeId;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getCreditFacility() {
        return CreditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        CreditFacility = creditFacility;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountInfoDetailCreditType", accountInfoDetailCreditType)
                .append("creditTypeId", creditTypeId)
                .append("productProgram", productProgram)
                .append("limit", limit)
                .append("CreditFacility", CreditFacility)
                .toString();
    }
}
