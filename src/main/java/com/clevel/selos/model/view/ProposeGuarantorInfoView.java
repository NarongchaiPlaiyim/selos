package com.clevel.selos.model.view;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.GuarantorCategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProposeGuarantorInfoView implements Serializable {
    private long id;
    private CustomerInfoView guarantorName;
    private String tcgLgNo;
    private DecisionType uwDecision;
    private GuarantorCategory guarantorCategory;
    private List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList;
    private BigDecimal guaranteeAmount;

    public ProposeGuarantorInfoView() {
        reset();
    }

    public void reset() {
        this.guarantorName = new CustomerInfoView();
        this.tcgLgNo = "";
        this.uwDecision = DecisionType.NO_DECISION;
        this.guarantorCategory = GuarantorCategory.NA;
        this.proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
        this.guaranteeAmount = BigDecimal.ZERO;
    }

    public GuarantorCategory getGuarantorCategory() {
        return guarantorCategory;
    }

    public void setGuarantorCategory(GuarantorCategory guarantorCategory) {
        this.guarantorCategory = guarantorCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CustomerInfoView getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(CustomerInfoView guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getTcgLgNo() {
        return tcgLgNo;
    }

    public void setTcgLgNo(String tcgLgNo) {
        this.tcgLgNo = tcgLgNo;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditInfoDetailViewList() {
        return proposeCreditInfoDetailViewList;
    }

    public void setProposeCreditInfoDetailViewList(List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList) {
        this.proposeCreditInfoDetailViewList = proposeCreditInfoDetailViewList;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }
}
