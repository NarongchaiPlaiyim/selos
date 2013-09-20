package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.master.TCGCollateralType;

import javax.persistence.*;

@Entity
@Table(name = "rel_potential_tcgcollateral")
public class PotentialColToTCGCol {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name="potential_id")
    private PotentialCollateral potentialCollateral;

    @OneToOne
    @JoinColumn(name="tcgcollateral_id")
    private TCGCollateralType tcgCollateralType;

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

    public TCGCollateralType getTcgCollateralType() {
        return tcgCollateralType;
    }

    public void setTcgCollateralType(TCGCollateralType tcgCollateralType) {
        this.tcgCollateralType = tcgCollateralType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
