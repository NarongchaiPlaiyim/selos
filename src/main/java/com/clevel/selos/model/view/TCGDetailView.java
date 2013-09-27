package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 9/9/2556
 * Time: 10:28 น.
 * To change this template use File | Settings | File Templates.
 */

public class TCGDetailView {
    private BigDecimal appraisalAmount;
    private BigDecimal ltvValue;
    private String isProposeInThisRequest;

    private PotentialCollateral  potentialCollateral;
    private TCGCollateralType tcgCollateralType;
    private PotentialColToTCGCol potentialColToTCGCol;

    public TCGDetailView(){
        reset();
    }

    public void reset(){
       this.appraisalAmount = new BigDecimal(0);
       this.ltvValue = new BigDecimal(0);
       this.isProposeInThisRequest ="";
       this.potentialCollateral = new PotentialCollateral();
       this.tcgCollateralType = new TCGCollateralType();
       this.potentialColToTCGCol = new PotentialColToTCGCol();
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

    public String getProposeInThisRequest() {
        return isProposeInThisRequest;
    }

    public void setProposeInThisRequest(String proposeInThisRequest) {
        isProposeInThisRequest = proposeInThisRequest;
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


    public PotentialColToTCGCol getPotentialColToTCGCol() {
        return potentialColToTCGCol;
    }

    public void setPotentialColToTCGCol(PotentialColToTCGCol potentialColToTCGCol) {
        this.potentialColToTCGCol = potentialColToTCGCol;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appraisalAmount", appraisalAmount)
                .append("ltvValue", ltvValue)
                .append("isProposeInThisRequest", isProposeInThisRequest)
                .append("potentialCollateral", potentialCollateral)
                .append("tcgCollateralType", tcgCollateralType)
                .append("potentialColToTCGCol", potentialColToTCGCol)
                .toString();
    }
}
