package com.clevel.selos.model.db.working;

import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_pledge_info")
public class PledgeInfo implements Serializable  {
    private static final long serialVersionUID = 4619737895658750920L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_PLEDGE_INFO_REQ", sequenceName = "SEQ_WRK_PLEDGE_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PLEDGE_INFO_REQ")
    private long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "pledge_signing_date")
    private Date pledgeSigningDate;
    
    @ManyToOne
    @JoinColumn(name="mortgage_type_id")
    private MortgageType pledgeType;
    
    @Column(name="pledge_amount")
    private BigDecimal pledgeAmount;
    
    @Column(name="total_hold_amount")
    private BigDecimal totalHoldAmount;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
    
    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "new_coll_sub_id")
    private NewCollateralSub newCollateralSub;
    
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
    @Enumerated
    private RadioValue confirmed;
    
    public PledgeInfo() {
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getPledgeSigningDate() {
		return pledgeSigningDate;
	}

	public void setPledgeSigningDate(Date pledgeSigningDate) {
		this.pledgeSigningDate = pledgeSigningDate;
	}

	public MortgageType getPledgeType() {
		return pledgeType;
	}

	public void setPledgeType(MortgageType pledgeType) {
		this.pledgeType = pledgeType;
	}

	public BigDecimal getPledgeAmount() {
		return pledgeAmount;
	}

	public void setPledgeAmount(BigDecimal pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}

	public WorkCase getWorkCase() {
		return workCase;
	}

	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}

	public OpenAccount getOpenAccount() {
		return openAccount;
	}

	public void setOpenAccount(OpenAccount openAccount) {
		this.openAccount = openAccount;
	}

	public NewCollateralSub getNewCollateralSub() {
		return newCollateralSub;
	}

	public void setNewCollateralSub(NewCollateralSub newCollateralSub) {
		this.newCollateralSub = newCollateralSub;
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
    
	public BigDecimal getTotalHoldAmount() {
		return totalHoldAmount;
	}
	public void setTotalHoldAmount(BigDecimal totalHoldAmount) {
		this.totalHoldAmount = totalHoldAmount;
	}
    public RadioValue getConfirmed() {
		return confirmed;
	}
    public void setConfirmed(RadioValue confirmed) {
		this.confirmed = confirmed;
	}
}
