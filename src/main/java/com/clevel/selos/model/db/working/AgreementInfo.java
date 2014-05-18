package com.clevel.selos.model.db.working;

import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.UserZone;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_agreement_info")
public class AgreementInfo implements Serializable {

    private static final long serialVersionUID = -8554828162122084421L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_AGREEMENT_INFO_REQ", sequenceName = "SEQ_WRK_AGREEMENT_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_AGREEMENT_INFO_REQ")
    private long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "loan_contract_date")
    private Date loanContractDate;

    @Column(name = "signing_location")
    @Enumerated(EnumType.ORDINAL)
    private MortgageSignLocationType signingLocation;

    @ManyToOne
    @JoinColumn(name="user_zone_id")
    private UserZone userZone;

    @ManyToOne
    @JoinColumn(name = "bank_branch_id")
    private BankBranch bankBranch;

    @Column(name = "coms_number")
    private String comsNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "first_payment_date")
    private Date firstPaymentDate;

    @Column(name = "pay_date")
    private int payDate;
    
    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;
    
    @Column(name="confirmed",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue confirmed;
    
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
    
    @Column(name="remark")
    private String remark;
    
    public AgreementInfo() {
    	
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getLoanContractDate() {
		return loanContractDate;
	}

	public void setLoanContractDate(Date loanContractDate) {
		this.loanContractDate = loanContractDate;
	}

	public MortgageSignLocationType getSigningLocation() {
		return signingLocation;
	}

	public void setSigningLocation(MortgageSignLocationType signingLocation) {
		this.signingLocation = signingLocation;
	}

	public UserZone getUserZone() {
		return userZone;
	}

	public void setUserZone(UserZone userZone) {
		this.userZone = userZone;
	}

	public BankBranch getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(BankBranch bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getComsNumber() {
		return comsNumber;
	}

	public void setComsNumber(String comsNumber) {
		this.comsNumber = comsNumber;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public int getPayDate() {
		return payDate;
	}

	public void setPayDate(int payDate) {
		this.payDate = payDate;
	}
    
	public WorkCase getWorkCase() {
		return workCase;
	}
	
	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}

	public RadioValue getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(RadioValue confirmed) {
		this.confirmed = confirmed;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
