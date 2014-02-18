package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_potential_collateral")
public class PotentialToCollateral implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "potential_id")
    private PotentialCollateral potentialCollateral;

    @OneToOne
    @JoinColumn(name = "collateral_id")
    private CollateralType collateralType;

    @Column(name = "active")
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("potentialCollateral", potentialCollateral)
                .append("collateralType", collateralType)
                .append("active", active)
                .toString();
    }
}
