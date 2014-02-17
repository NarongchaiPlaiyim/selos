package com.clevel.selos.model.db.working;

import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.MortgageLandOffice;
import com.clevel.selos.model.db.master.MortgageOSCompany;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_mortgage_info")
public class MortgageInfo implements Serializable {
    private static final long serialVersionUID = -6072849397102086664L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_MORTGAGE_INFO_REQ", sequenceName = "SEQ_WRK_MORTGAGE_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORTGAGE_INFO_REQ")
    private long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "mortgage_signing_date")
    private Date mortgageSigningDate;

    @ManyToOne
    @JoinColumn(name = "mortgage_os_company_id")
    private MortgageOSCompany mortgageOSCompany;

    @ManyToOne
    @JoinColumn(name = "mortgage_land_office_id")
    private MortgageLandOffice mortgageLandOffice;

    @ManyToOne
    @JoinColumn(name = "mortgage_type_id")
    private MortgageType mortgageType;

    @Column(name = "mortgage_order")
    private int mortgageOrder;
    
    @OneToMany(mappedBy = "mortgageInfo")
    private List<MortgageInfoCredit> mortgageInfoCreditList;

    @OneToMany(mappedBy = "mortgageInfo")
    private List<MortgageInfoCollSub> mortgageInfoCollSubList;

    @OneToMany(mappedBy = "mortgageInfo")
    private List<MortgageInfoCollOwner> mortgageInfoCollOwnerList;

    @Column(name = "attorney_required",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue attorneyRequired;

    @Column(name = "attorney_relation",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private AttorneyRelationType attorneyRelation;

    @OneToOne
    @JoinColumn(name = "poa_customer_id", nullable = true)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "customer_attorney_id", nullable = true)
    private CustomerAttorney customerAttorney;

    @ManyToOne
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
    
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getMortgageSigningDate() {
		return mortgageSigningDate;
	}

	public void setMortgageSigningDate(Date mortgageSigningDate) {
		this.mortgageSigningDate = mortgageSigningDate;
	}

	public MortgageOSCompany getMortgageOSCompany() {
		return mortgageOSCompany;
	}

	public void setMortgageOSCompany(MortgageOSCompany mortgageOSCompany) {
		this.mortgageOSCompany = mortgageOSCompany;
	}

	public MortgageLandOffice getMortgageLandOffice() {
		return mortgageLandOffice;
	}

	public void setMortgageLandOffice(MortgageLandOffice mortgageLandOffice) {
		this.mortgageLandOffice = mortgageLandOffice;
	}

	public MortgageType getMortgageType() {
		return mortgageType;
	}

	public void setMortgageType(MortgageType mortgageType) {
		this.mortgageType = mortgageType;
	}

	public int getMortgageOrder() {
		return mortgageOrder;
	}

	public void setMortgageOrder(int mortgageOrder) {
		this.mortgageOrder = mortgageOrder;
	}

	public List<MortgageInfoCredit> getMortgageInfoCreditList() {
		return mortgageInfoCreditList;
	}

	public void setMortgageInfoCreditList(List<MortgageInfoCredit> mortgageInfoCreditList) {
		this.mortgageInfoCreditList = mortgageInfoCreditList;
	}

	public List<MortgageInfoCollSub> getMortgageInfoCollSubList() {
		return mortgageInfoCollSubList;
	}

	public void setMortgageInfoCollSubList(List<MortgageInfoCollSub> mortgageInfoCollSubList) {
		this.mortgageInfoCollSubList = mortgageInfoCollSubList;
	}

	public List<MortgageInfoCollOwner> getMortgageInfoCollOwnerList() {
		return mortgageInfoCollOwnerList;
	}

	public void setMortgageInfoCollOwnerList(List<MortgageInfoCollOwner> mortgageInfoCollOwnerList) {
		this.mortgageInfoCollOwnerList = mortgageInfoCollOwnerList;
	}

	public RadioValue getAttorneyRequired() {
		return attorneyRequired;
	}
	
	public void setAttorneyRequired(RadioValue attorneyRequired) {
		this.attorneyRequired = attorneyRequired;
	}

	public AttorneyRelationType getAttorneyRelation() {
		return attorneyRelation;
	}
	
	public void setAttorneyRelation(AttorneyRelationType attorneyRelation) {
		this.attorneyRelation = attorneyRelation;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerAttorney getCustomerAttorney() {
		return customerAttorney;
	}

	public void setCustomerAttorney(CustomerAttorney customerAttorney) {
		this.customerAttorney = customerAttorney;
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
	public WorkCase getWorkCase() {
		return workCase;
	}
	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}
    
}
