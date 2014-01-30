package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "wrk_open_account_name")
public class OpenAccountName {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_NAME_ID", sequenceName = "SEQ_WRK_OPEN_ACC_NAME_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_NAME_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("openAccount", openAccount).
                append("customer", customer).
                toString();
    }
}
