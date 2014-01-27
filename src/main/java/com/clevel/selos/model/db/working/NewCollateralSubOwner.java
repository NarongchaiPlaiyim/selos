package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_coll_sub_owner")
public class NewCollateralSubOwner implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_CUS_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_CUS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_CUS_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private NewCollateralSub newCollateralSub;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NewCollateralSub getNewCollateralSub() {
        return newCollateralSub;
    }

    public void setNewCollateralSub(NewCollateralSub newCollateralSub) {
        this.newCollateralSub = newCollateralSub;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
