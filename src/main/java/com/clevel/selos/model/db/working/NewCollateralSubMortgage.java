package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.MortgageType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_collateral_mortgage")
public class NewCollateralSubMortgage implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_SUB_COL_MOR_ID", sequenceName = "SEQ_WRK_NEW_SUB_COL_MOR_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_SUB_COL_MOR_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private NewCollateralSubDetail newCollateralSubDetail;

    @OneToOne
    @JoinColumn(name = "mortgage_id")
    private MortgageType mortgageType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NewCollateralSubDetail getNewCollateralSubDetail() {
        return newCollateralSubDetail;
    }

    public void setNewCollateralSubDetail(NewCollateralSubDetail newCollateralSubDetail) {
        this.newCollateralSubDetail = newCollateralSubDetail;
    }

    public MortgageType getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(MortgageType mortgageType) {
        this.mortgageType = mortgageType;
    }
}
