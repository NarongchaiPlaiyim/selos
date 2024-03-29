package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.MortgageType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mortgage_info_mortgage")
public class MortgageInfoMortgage implements Serializable {
	private static final long serialVersionUID = -4758316485719349025L;

	@Id
	@SequenceGenerator(name = "SEQ_WRK_MORT_MOR_ID", sequenceName = "SEQ_WRK_MORT_MOR_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_MOR_ID")
	private long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "mortgage_info_id")
	private MortgageInfo mortgageInfo;

	@OneToOne
	@JoinColumn(name = "mortgage_type_id")
	private MortgageType mortgageType;
	
	public MortgageInfoMortgage() {
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

	public MortgageType getMortgageType() {
		return mortgageType;
	}

	public void setMortgageType(MortgageType mortgageType) {
		this.mortgageType = mortgageType;
	}
	
}
