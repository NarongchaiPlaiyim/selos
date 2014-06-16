package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisbursementInfoView implements Serializable{

	private long id;
    private int approvedType;
    private List<DisbursementSummaryView> disburse;
    private List<DisbursementMcDetailView> disburseMcList = new ArrayList<DisbursementMcDetailView>();
    private int numberOfCheque;
    private BigDecimal totalMCDisburse;
    private List<DisbursementDepositBaDetailView> disburseDepositList = new ArrayList<DisbursementDepositBaDetailView>();
    private int numberOfDeposit;
    private BigDecimal totalDepositDisburse;
    private List<DisbursementBahtnetDetailView> disbursementBahtnetList = new ArrayList<DisbursementBahtnetDetailView>();
    private int numberOfBahtnet;
    private BigDecimal totalBahtnetDisburse;
    private List<DisbursementDepositBaDetailView> disbursementBaList = new ArrayList<DisbursementDepositBaDetailView>();
    private int numberOfBA;
    private BigDecimal totalBADisburse;
        
    private User modifyBy;
    private Date modifyDate;
    
    public int getNumberOfDeposit() {
        return numberOfDeposit;
    }

    public void setNumberOfDeposit(int numberOfDeposit) {
        this.numberOfDeposit = numberOfDeposit;
    }

    public BigDecimal getTotalDepositDisburse() {
        return totalDepositDisburse;
    }

    public void setTotalDepositDisburse(BigDecimal totalDepositDisburse) {
        this.totalDepositDisburse = totalDepositDisburse;
    }

    public int getNumberOfCheque() {
        return numberOfCheque;
    }

    public void setNumberOfCheque(int numberofCheque) {
        this.numberOfCheque = numberofCheque;
    }

    public BigDecimal getTotalMCDisburse() {
        return totalMCDisburse;
    }

    public void setTotalMCDisburse(BigDecimal totalMCDisburse) {
        this.totalMCDisburse = totalMCDisburse;
    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    public List<DisbursementSummaryView> getDisburse() {
        return disburse;
    }

    public void setDisburse(List<DisbursementSummaryView> disburse) {
        this.disburse = disburse;
    }

    public List<DisbursementMcDetailView> getDisburseMcList() {
        return disburseMcList;
    }

    public void setDisburseMcList(List<DisbursementMcDetailView> disburseMcList) {
        this.disburseMcList = disburseMcList;
    }

    public List<DisbursementDepositBaDetailView> getDisburseDepositList() {
        return disburseDepositList;
    }

    public void setDisburseDepositList(List<DisbursementDepositBaDetailView> disburseDepositList) {
        this.disburseDepositList = disburseDepositList;
    }

    public int getNumberOfBahtnet() {
        return numberOfBahtnet;
    }

    public void setNumberOfBahtnet(int numberOfBahtnet) {
        this.numberOfBahtnet = numberOfBahtnet;
    }

    public BigDecimal getTotalBahtnetDisburse() {
        return totalBahtnetDisburse;
    }

    public void setTotalBahtnetDisburse(BigDecimal totalBahtnetDisburse) {
        this.totalBahtnetDisburse = totalBahtnetDisburse;
    }

    public List<DisbursementBahtnetDetailView> getDisbursementBahtnetList() {
        return disbursementBahtnetList;
    }

    public void setDisbursementBahtnetList(List<DisbursementBahtnetDetailView> disbursementBahtnetList) {
        this.disbursementBahtnetList = disbursementBahtnetList;
    }

    public List<DisbursementDepositBaDetailView> getDisbursementBaList() {
        return disbursementBaList;
    }

    public void setDisbursementBaList(List<DisbursementDepositBaDetailView> disbursementBaList) {
        this.disbursementBaList = disbursementBaList;
    }

    public int getNumberOfBA() {
        return numberOfBA;
    }

    public void setNumberOfBA(int numberOfBA) {
        this.numberOfBA = numberOfBA;
    }

    public BigDecimal getTotalBADisburse() {
        return totalBADisburse;
    }

    public void setTotalBADisburse(BigDecimal totalBADisburse) {
        this.totalBADisburse = totalBADisburse;
    }

	public User getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
