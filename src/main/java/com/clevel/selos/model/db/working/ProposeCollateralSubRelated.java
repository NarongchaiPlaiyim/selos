package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_coll_sub_related")
public class ProposeCollateralSubRelated implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_REL_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_REL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_REL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private ProposeCollateralInfoSub proposeCollateralSub;

    @OneToOne
    @JoinColumn(name = "relate_collateral_sub_id")
    private ProposeCollateralInfoSub proposeCollateralSubRelated;

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

    public ProposeCollateralInfoSub getProposeCollateralSubRelated() {
        return proposeCollateralSubRelated;
    }

    public void setProposeCollateralSubRelated(ProposeCollateralInfoSub proposeCollateralSubRelated) {
        this.proposeCollateralSubRelated = proposeCollateralSubRelated;
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
