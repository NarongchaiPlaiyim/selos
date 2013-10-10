package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.WarningCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_customer_csi")
public class CustomerCSI implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_CUST_CSI_ID", sequenceName="SEQ_WRK_CUST_CSI_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CUST_CSI_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    Customer customer;

    @OneToOne
    @JoinColumn(name="warning_id")
    WarningCode warningCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WarningCode getWarningCode() {
        return warningCode;
    }

    public void setWarningCode(WarningCode warningCode) {
        this.warningCode = warningCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("warningCode", warningCode)
                .toString();
    }
}
