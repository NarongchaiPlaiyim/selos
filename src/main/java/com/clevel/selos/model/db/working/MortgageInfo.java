package com.clevel.selos.model.db.working;

import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.MortgageConfirmedType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.MortgageLandOffice;
import com.clevel.selos.model.db.master.MortgageOSCompany;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @Column(name = "mortgage_order",columnDefinition="int default 0")
    private int mortgageOrder;
   
    @Column(name = "attorney_required",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue attorneyRequired;

    @Column(name = "attorney_relation",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private AttorneyRelationType attorneyRelation;

    @OneToOne
    @JoinColumn(name = "customer_attorney_id", nullable = true)
    private CustomerAttorney customerAttorney;
    
    @Column(name="mortgage_amount")
    private BigDecimal mortgageAmount;
    
    @OneToMany(mappedBy = "mortgageInfo", cascade = CascadeType.ALL)
    private List<MortgageInfoMortgage> mortgageTypeList;
    
    //Using for checking update or create new
    //Normal (Main + Join group) -> Store main new collateral sub Id
    //Referred -> Store refer value    
    @Column(name="mortgage_reference_key")
    private String mortgageRefKey;
    
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
    
    @Column(name="confirmed",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private MortgageConfirmedType confirmed;
    
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

	public int getMortgageOrder() {
		return mortgageOrder;
	}

	public void setMortgageOrder(int mortgageOrder) {
		this.mortgageOrder = mortgageOrder;
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
    
	public BigDecimal getMortgageAmount() {
		return mortgageAmount;
	}
	public void setMortgageAmount(BigDecimal mortgageAmount) {
		this.mortgageAmount = mortgageAmount;
	}
	
	public List<MortgageInfoMortgage> getMortgageTypeList() {
		return mortgageTypeList;
	}
	public void setMortgageTypeList(List<MortgageInfoMortgage> mortgageTypeList) {
		this.mortgageTypeList = mortgageTypeList;
	}
	
	public String getMortgageRefKey() {
		return mortgageRefKey;
	}
	public void setMortgageRefKey(String mortgageRefKey) {
		this.mortgageRefKey = mortgageRefKey;
	}
	
	public MortgageConfirmedType getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(MortgageConfirmedType confirmed) {
		this.confirmed = confirmed;
	}
}
