package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "rel_potential_tcgcollateral")
public class PotentialColToTCGCol implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "potential_id")
    private PotentialCollateral potentialCollateral;

    @OneToOne
    @JoinColumn(name = "tcgcollateral_id")
    private TCGCollateralType tcgCollateralType;

    @Column(name = "percent_ltv")
    private BigDecimal percentLTV;

    @Column(name = "ten_percent_ltv")
    private BigDecimal tenPercentLTV;

    @Column(name = "retention_ltv")
    private BigDecimal retentionLTV;

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

    public BigDecimal getPercentLTV() {
        return percentLTV;
    }

    public void setPercentLTV(BigDecimal percentLTV) {
        this.percentLTV = percentLTV;
    }

    public BigDecimal getTenPercentLTV() {
        return tenPercentLTV;
    }

    public void setTenPercentLTV(BigDecimal tenPercentLTV) {
        this.tenPercentLTV = tenPercentLTV;
    }

    public BigDecimal getRetentionLTV() {
        return retentionLTV;
    }

    public void setRetentionLTV(BigDecimal retentionLTV) {
        this.retentionLTV = retentionLTV;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("potentialCollateral", potentialCollateral)
                .append("tcgCollateralType", tcgCollateralType)
                .append("percentLTV", percentLTV)
                .append("tenPercentLTV", tenPercentLTV)
                .append("retentionLTV", retentionLTV)
                .append("active", active)
                .toString();
    }
}
