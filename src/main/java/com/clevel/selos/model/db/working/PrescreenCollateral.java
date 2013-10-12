package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CollateralType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="wrk_prescreen_collateral")
public class PrescreenCollateral implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_PS_COLL_ID", sequenceName="SEQ_WRK_PS_COLL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_PS_COLL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="prescreen_id")
    private Prescreen prescreen;

    @OneToOne
    @JoinColumn(name="collateral_type_id")
    private CollateralType collateralType;

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

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }
}
