package com.clevel.selos.model.db.working;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "wrk_mortgage_coll_sub")
public class MortgageInfoCollSub implements Serializable {
    private static final long serialVersionUID = 8887661496113137965L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_MORT_COLL_SUB_ID", sequenceName = "SEQ_WRK_MORT_COLL_SUB_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_COLL_SUB_ID")
    private long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "mortgage_info_id")
    private MortgageInfo mortgageInfo;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "new_coll_sub_id")
    private NewCollateralSub newCollateralSub;
    
    @Column(name="is_main",columnDefinition="int default 0")
    private boolean main;
    
    public MortgageInfoCollSub() {
    	
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

	public NewCollateralSub getNewCollateralSub() {
		return newCollateralSub;
	}

	public void setNewCollateralSub(NewCollateralSub newCollateralSub) {
		this.newCollateralSub = newCollateralSub;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}
    
}
