package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BAResultHC;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_bapa_customer")
public class BAPAInfoCustomer implements Serializable {

    private static final long serialVersionUID = 3735544757612215913L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_BAPA_CUSTOMER_REQ", sequenceName = "SEQ_WRK_BAPA_CUSTOMER_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAPA_CUSTOMER_REQ")
    private long id;

    @ManyToOne
    @JoinColumn(name = "bapa_info_id")
    private BAPAInfo bapaInfo;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "ba_result_hc_id")
    private BAResultHC baResultHC;

    @Temporal(TemporalType.DATE)
    @Column(name = "health_check_date")
    private Date healthCheckDate;

    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BAPAInfo getBapaInfo() {
		return bapaInfo;
	}

	public void setBapaInfo(BAPAInfo bapaInfo) {
		this.bapaInfo = bapaInfo;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BAResultHC getBaResultHC() {
		return baResultHC;
	}

	public void setBaResultHC(BAResultHC baResultHC) {
		this.baResultHC = baResultHC;
	}

	public Date getHealthCheckDate() {
		return healthCheckDate;
	}

	public void setHealthCheckDate(Date healthCheckDate) {
		this.healthCheckDate = healthCheckDate;
	}

}
