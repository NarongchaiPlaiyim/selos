package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mortgage_credit")
public class MortgageInfoCredit implements Serializable {

    private static final long serialVersionUID = 330784460279895370L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_MORT_CREDIT_ID", sequenceName = "SEQ_WRK_MORT_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mortgage_info_id")
    private MortgageInfo mortgageInfo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "new_collateral_credit_id")
    private ProposeCollateralInfoRelation newCollateralCredit;

    public MortgageInfoCredit() {
    	
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MortgageInfo getMortgageInfo() {
		return mortgageInfo;
	}

	public void setMortgageInfo(MortgageInfo mortgageInfo) {
		this.mortgageInfo = mortgageInfo;
	}

    public ProposeCollateralInfoRelation getNewCollateralCredit() {
        return newCollateralCredit;
    }

    public void setNewCollateralCredit(ProposeCollateralInfoRelation newCollateralCredit) {
        this.newCollateralCredit = newCollateralCredit;
    }
}
