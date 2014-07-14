package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.MortgageType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_coll_sub_mortgage")
public class ProposeCollateralSubMortgage implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_MOR_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_MOR_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_MOR_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private ProposeCollateralInfoSub proposeCollateralSub;

    @OneToOne
    @JoinColumn(name = "mortgage_type_id")
    private MortgageType mortgageType;

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

    public MortgageType getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(MortgageType mortgageType) {
        this.mortgageType = mortgageType;
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
