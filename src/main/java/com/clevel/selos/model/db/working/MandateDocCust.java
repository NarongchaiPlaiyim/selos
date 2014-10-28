package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mandate_doc_cust")
public class MandateDocCust implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_CUST_ID", sequenceName = "SEQ_WRK_MANDATE_CUST_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_CUST_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mandate_doc_id")
    private MandateDocDetail mandateDocDetail;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateDocDetail getMandateDocDetail() {
        return mandateDocDetail;
    }

    public void setMandateDocDetail(MandateDocDetail mandateDocDetail) {
        this.mandateDocDetail = mandateDocDetail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .toString();
    }
}
