package com.clevel.selos.model.db.working;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;

@Entity
@Table(name = "wrk_guarantor_info")
public class GuarantorInfo implements Serializable  {
    private static final long serialVersionUID = -8123946645456450962L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_GUARANTOR_INFO_REQ", sequenceName = "SEQ_WRK_GUARANTOR_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_GUARANTOR_INFO_REQ")
    private long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "guarantor_signing_date")
    private Date guarantorSigningDate;

    @ManyToOne
    @JoinColumn(name="mortgage_type_id")
    private MortgageType guarantorType;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;
    
    @OneToOne
    @JoinColumn(name="new_guarantor_id")
    private NewGuarantorDetail newGuarantorDetail;
    
    public GuarantorInfo() {
    	
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getGuarantorSigningDate() {
		return guarantorSigningDate;
	}

	public void setGuarantorSigningDate(Date guarantorSigningDate) {
		this.guarantorSigningDate = guarantorSigningDate;
	}

	public MortgageType getGuarantorType() {
		return guarantorType;
	}

	public void setGuarantorType(MortgageType guarantorType) {
		this.guarantorType = guarantorType;
	}

	public WorkCase getWorkCase() {
		return workCase;
	}

	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public User getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}
	
	public NewGuarantorDetail getNewGuarantorDetail() {
		return newGuarantorDetail;
	}
	
	public void setNewGuarantorDetail(NewGuarantorDetail newGuarantorDetail) {
		this.newGuarantorDetail = newGuarantorDetail;
	}
}
