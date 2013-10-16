package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class TCGDetailView implements Serializable {

    private long id;
    private BigDecimal appraisalAmount;
    private BigDecimal ltvValue;
    private int isProposeInThisRequest;
    private String  proposeInThis;


    private PotentialCollateral  potentialCollateral;
    private TCGCollateralType tcgCollateralType;
    private PotentialColToTCGCol potentialColToTCGCol;


    public TCGDetailView(){
        reset();
    }

    public void reset(){
       this.appraisalAmount = new BigDecimal(0);
       this.ltvValue = new BigDecimal(0);
       this.isProposeInThisRequest = 0;
       this.potentialCollateral = new PotentialCollateral();
       this.tcgCollateralType = new TCGCollateralType();
       this.potentialColToTCGCol = new PotentialColToTCGCol();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getProposeInThisRequest() {
        return isProposeInThisRequest;
    }

    public void setProposeInThisRequest(int proposeInThisRequest) {
        isProposeInThisRequest = proposeInThisRequest;
    }

    public String getProposeInThis() {
        if(isProposeInThisRequest == 1){
             proposeInThis = "Y";
        }else if(isProposeInThisRequest == 0){
             proposeInThis = "N";
        }
        return proposeInThis;
    }

    public void setProposeInThis(String proposeInThis) {
        this.proposeInThis = proposeInThis;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("appraisalAmount", appraisalAmount)
                .append("ltvValue", ltvValue)
                .append("isProposeInThisRequest", isProposeInThisRequest)
                .append("potentialCollateral", potentialCollateral)
                .append("tcgCollateralType", tcgCollateralType)
                .append("potentialColToTCGCol", potentialColToTCGCol)
                .toString();
    }
}
