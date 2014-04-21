package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ProposeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_coll_sub_related")
public class NewCollateralSubRelated implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_COLL_SUB_REL_ID", sequenceName = "SEQ_WRK_NEW_COLL_SUB_REL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_COLL_SUB_REL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private NewCollateralSub newCollateralSub;

    @OneToOne
    @JoinColumn(name = "relate_collateral_sub_id")
    private NewCollateralSub newCollateralSubRelated;

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

    public NewCollateralSub getNewCollateralSub() {
        return newCollateralSub;
    }

    public void setNewCollateralSub(NewCollateralSub newCollateralSub) {
        this.newCollateralSub = newCollateralSub;
    }

    public NewCollateralSub getNewCollateralSubRelated() {
        return newCollateralSubRelated;
    }

    public void setNewCollateralSubRelated(NewCollateralSub newCollateralSubRelated) {
        this.newCollateralSubRelated = newCollateralSubRelated;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("newCollateralSub", newCollateralSub).
                append("newCollateralSubRelated", newCollateralSubRelated).
                append("workCase", workCase).
                append("proposeType", proposeType).
                toString();
    }
}
