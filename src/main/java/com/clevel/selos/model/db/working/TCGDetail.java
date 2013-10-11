package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="wrk_tcg_detail")
public class TCGDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_TCG_DETAIL_ID", sequenceName="SEQ_WRK_TCG_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_TCG_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="tcg_id")
    private TCG tcg;

    @OneToOne
    @JoinColumn(name="potential_collateral_id")
    private PotentialCollateral potentialCollateral;

    @OneToOne
    @JoinColumn(name="tcg_collateral_type_id")
    private TCGCollateralType tcgCollateralType;

    @Column(name="appraisal_amount")
    private BigDecimal appraisalAmount;

    @Column(name="ltv_value")
    private BigDecimal ltvValue;

    @Column(name="propose_in_this_request_flag")
    private boolean isProposeInThisRequest;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TCG getTcg() {
        return tcg;
    }

    public void setTcg(TCG tcg) {
        this.tcg = tcg;
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

    public BigDecimal getAppraisalAmount() {
        return appraisalAmount;
    }

    public void setAppraisalAmount(BigDecimal appraisalAmount) {
        this.appraisalAmount = appraisalAmount;
    }

    public BigDecimal getLtvValue() {
        return ltvValue;
    }

    public void setLtvValue(BigDecimal ltvValue) {
        this.ltvValue = ltvValue;
    }

    public boolean isProposeInThisRequest() {
        return isProposeInThisRequest;
    }

    public void setProposeInThisRequest(boolean proposeInThisRequest) {
        isProposeInThisRequest = proposeInThisRequest;
    }


}
