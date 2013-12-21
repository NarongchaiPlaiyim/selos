package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_collateral_relation")
public class NewCollateralRelCredit implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_CREDIT_ID", sequenceName = "SEQ_WRK_NEW_COLL_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_id")
    private NewCollateralDetail newCollateralDetail;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NewCollateralDetail getNewCollateralDetail() {
        return newCollateralDetail;
    }

    public void setNewCollateralDetail(NewCollateralDetail newCollateralDetail) {
        this.newCollateralDetail = newCollateralDetail;
    }

    public NewCreditDetail getNewCreditDetail() {
        return newCreditDetail;
    }

    public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
        this.newCreditDetail = newCreditDetail;
    }
}
