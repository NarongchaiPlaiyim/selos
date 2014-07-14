package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_coll_sub_owner")
public class ProposeCollateralSubOwner implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_CUS_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_CUS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_CUS_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private ProposeCollateralInfoSub proposeCollateralSub;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "propose_type")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProposeCollateralInfoSub getProposeCollateralSub() {
        return proposeCollateralSub;
    }

    public void setProposeCollateralSub(ProposeCollateralInfoSub proposeCollateralSub) {
        this.proposeCollateralSub = proposeCollateralSub;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }
}
