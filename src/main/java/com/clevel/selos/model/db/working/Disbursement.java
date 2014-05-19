package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_disbursement")
public class Disbursement {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_DISBURSE_REQ", sequenceName = "SEQ_WRK_DISBURSE_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DISBURSE_REQ")
    private long id;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementCredit> disbursementSummaryList;

    @Column(name = "number_of_mc", columnDefinition = "int default 0")
    private int numberOfCheque;

    @Column(name = "total_mc_disburse")
    private BigDecimal totalMCDisbursement;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementMC> disbursementMCList;

    @Column(name = "number_of_tr", columnDefinition = "int default 0")
    private int numberOfTR;

    @Column(name = "total_tr_disburse")
    private BigDecimal totalTRDisburse;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementTR> disbursementTRList;

    @Column(name = "number_of_bahtnet")
    private int numberOfBahtnet;

    @Column(name = "total_bahtnet_disburse")
    private BigDecimal totalBahtnetDisbursement;

    @OneToMany(mappedBy = "disbursement")
    private List<DisbursementBahtnet> disbursementBahtnetList;
    
    @OneToOne
    @JoinColumn(name = "workcase_id")
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

	public List<DisbursementCredit> getDisbursementSummaryList() {
		return disbursementSummaryList;
	}

	public void setDisbursementSummaryList(List<DisbursementCredit> disbursementSummaryList) {
		this.disbursementSummaryList = disbursementSummaryList;
	}

	public int getNumberOfCheque() {
		return numberOfCheque;
	}

	public void setNumberOfCheque(int numberOfCheque) {
		this.numberOfCheque = numberOfCheque;
	}

	public BigDecimal getTotalMCDisbursement() {
		return totalMCDisbursement;
	}

	public void setTotalMCDisbursement(BigDecimal totalMCDisbursement) {
		this.totalMCDisbursement = totalMCDisbursement;
	}

	public List<DisbursementMC> getDisbursementMCList() {
		return disbursementMCList;
	}

	public void setDisbursementMCList(List<DisbursementMC> disbursementMCList) {
		this.disbursementMCList = disbursementMCList;
	}

	public int getNumberOfTR() {
		return numberOfTR;
	}

	public void setNumberOfTR(int numberOfTR) {
		this.numberOfTR = numberOfTR;
	}

	public BigDecimal getTotalTRDisburse() {
		return totalTRDisburse;
	}

	public void setTotalTRDisburse(BigDecimal totalTRDisburse) {
		this.totalTRDisburse = totalTRDisburse;
	}

	public List<DisbursementTR> getDisbursementTRList() {
		return disbursementTRList;
	}

	public void setDisbursementTRList(List<DisbursementTR> disbursementTRList) {
		this.disbursementTRList = disbursementTRList;
	}

	public int getNumberOfBahtnet() {
		return numberOfBahtnet;
	}

	public void setNumberOfBahtnet(int numberOfBahtnet) {
		this.numberOfBahtnet = numberOfBahtnet;
	}

	public BigDecimal getTotalBahtnetDisbursement() {
		return totalBahtnetDisbursement;
	}

	public void setTotalBahtnetDisbursement(BigDecimal totalBahtnetDisbursement) {
		this.totalBahtnetDisbursement = totalBahtnetDisbursement;
	}

	public List<DisbursementBahtnet> getDisbursementBahtnetList() {
		return disbursementBahtnetList;
	}

	public void setDisbursementBahtnetList(List<DisbursementBahtnet> disbursementBahtnetList) {
		this.disbursementBahtnetList = disbursementBahtnetList;
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


}
