package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.PotentialCollateral;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_prescreen_collateral")
public class PrescreenCollateral implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PS_COLL_ID", sequenceName = "SEQ_WRK_PS_COLL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PS_COLL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "prescreen_id")
    private Prescreen prescreen;

    @OneToOne
    @JoinColumn(name = "potential_collateral_id")
    private PotentialCollateral potentialCollateral;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Prescreen getPrescreen() {
        return prescreen;
    }

    public void setPrescreen(Prescreen prescreen) {
        this.prescreen = prescreen;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }
}
