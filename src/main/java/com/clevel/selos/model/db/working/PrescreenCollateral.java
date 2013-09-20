package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CollateralType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="wrk_prescreen_collateral")
public class PrescreenCollateral implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_PRESCREEN_COLLATERAL_ID", sequenceName="SEQ_WRK_PRESCREEN_COLLATERAL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_PRESCREEN_COLLATERAL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="prescreen_id")
    private Prescreen prescreen;

    @OneToOne
    @JoinColumn(name="collateral_type_id")
    private CollateralType collateralType;
}
