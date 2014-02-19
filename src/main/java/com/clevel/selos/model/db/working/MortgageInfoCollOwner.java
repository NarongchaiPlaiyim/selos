package com.clevel.selos.model.db.working;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "wrk_mortgage_coll_owner")
public class MortgageInfoCollOwner implements Serializable {
    private static final long serialVersionUID = -1727978562742958147L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_MORT_COLL_OWNER_ID", sequenceName = "SEQ_WRK_MORT_COLL_OWNER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORT_COLL_OWNER_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mortgage_info_id")
    private MortgageInfo mortgageInfo;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(name="is_poa",columnDefinition="int default 0")
    private boolean poa;
    
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

    public boolean isPoa() {
		return poa;
	}
    public void setPoa(boolean poa) {
		this.poa = poa;
	}
}
