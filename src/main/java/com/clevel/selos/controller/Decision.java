package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.Disbursement;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "decision")
public class Decision implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    //Business logic
    @Inject
    DecisionControl decisionControl;

    //DAO

    //Transform

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //View
    private ExistingCreditView existingCreditView;
    private List<ProposeConditionDetailView> existingConditionCommCrdList;
    private ExistingCollateralView existingColBorrower;
    private ExistingCollateralView existingColRelated;

    private CreditFacProposeView creditFacProposeView;

    public Decision() {
    }

    @PostConstruct
    public void onCreation() {
        //========================================= Existing =========================================//

        //========================================= Propose =========================================//
        List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();

        ProductProgram productProgram = new ProductProgram();
        productProgram.setDescription("TMB SME SmartBiz");
        CreditType creditType = new CreditType();
        creditType.setDescription("Loan");
        Disbursement disbursement = new Disbursement();
        disbursement.setDisbursement("Normal Disbursement");

        //----------------------------------------- 1 ---------------------------------------//
        List<CreditTierDetailView> tierDetailViewList1 = new ArrayList<CreditTierDetailView>();
        CreditTierDetailView tierDetailView1 = new CreditTierDetailView();
        tierDetailView1.setStandardPrice(BigDecimal.valueOf(1.00));
        tierDetailView1.setSuggestPrice(BigDecimal.valueOf(2.00));
        tierDetailView1.setFinalPriceRate(BigDecimal.valueOf(2.00));
        tierDetailView1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView1.setTenor(BigDecimal.valueOf(3));
        tierDetailViewList1.add(tierDetailView1);

        ProposeCreditDetailView proposeCreditDetailView1 = new ProposeCreditDetailView();
        proposeCreditDetailView1.setProductProgram(productProgram);
        proposeCreditDetailView1.setCreditType(creditType);
        proposeCreditDetailView1.setProductCode("EAC1");
        proposeCreditDetailView1.setProjectCode("90074");
        proposeCreditDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView1.setFrontEndFee(BigDecimal.valueOf(1.75));
        proposeCreditDetailView1.setCreditTierDetailViewList(tierDetailViewList1);
        proposeCreditDetailView1.setRequestType(0);
        proposeCreditDetailView1.setRefinance(0);
        proposeCreditDetailView1.setRemark("Purpose Detail Example");
        proposeCreditDetailView1.setDisbursement(disbursement);
        proposeCreditDetailView1.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 2 ---------------------------------------//
        List<CreditTierDetailView> tierDetailViewList2 = new ArrayList<CreditTierDetailView>();
        CreditTierDetailView tierDetailView2_1 = new CreditTierDetailView();
        tierDetailView2_1.setStandardPrice(BigDecimal.valueOf(1.00));
        tierDetailView2_1.setSuggestPrice(BigDecimal.valueOf(1.50));
        tierDetailView2_1.setFinalPriceRate(BigDecimal.valueOf(1.50));
        tierDetailView2_1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_1.setTenor(BigDecimal.valueOf(6));
        tierDetailViewList2.add(tierDetailView2_1);

        CreditTierDetailView tierDetailView2_2 = new CreditTierDetailView();
        tierDetailView2_2.setStandardPrice(BigDecimal.valueOf(1.25));
        tierDetailView2_2.setSuggestPrice(BigDecimal.valueOf(2.00));
        tierDetailView2_2.setFinalPriceRate(BigDecimal.valueOf(2.00));
        tierDetailView2_2.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_2.setTenor(BigDecimal.valueOf(6));
        tierDetailViewList2.add(tierDetailView2_2);

        CreditTierDetailView tierDetailView2_3 = new CreditTierDetailView();
        tierDetailView2_3.setStandardPrice(BigDecimal.valueOf(2.00));
        tierDetailView2_3.setSuggestPrice(BigDecimal.valueOf(2.50));
        tierDetailView2_3.setFinalPriceRate(BigDecimal.valueOf(2.50));
        tierDetailView2_3.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_3.setTenor(BigDecimal.valueOf(24));
        tierDetailViewList2.add(tierDetailView2_3);

        ProposeCreditDetailView proposeCreditDetailView2 = new ProposeCreditDetailView();
        proposeCreditDetailView2.setProductProgram(productProgram);
        proposeCreditDetailView2.setCreditType(creditType);
        proposeCreditDetailView2.setProductCode("EAC2");
        proposeCreditDetailView2.setProjectCode("90078");
        proposeCreditDetailView2.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView2.setFrontEndFee(BigDecimal.valueOf(1.75));
        proposeCreditDetailView2.setCreditTierDetailViewList(tierDetailViewList2);
        proposeCreditDetailView2.setRequestType(1);
        proposeCreditDetailView2.setRefinance(1);
        proposeCreditDetailView2.setRemark("Purpose Detail Example 2");
        proposeCreditDetailView2.setDisbursement(disbursement);
        proposeCreditDetailView2.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 3 ---------------------------------------//
        ProposeCreditDetailView proposeCreditDetailView3 = new ProposeCreditDetailView();
        proposeCreditDetailView3.setProductProgram(productProgram);
        proposeCreditDetailView3.setCreditType(creditType);
        proposeCreditDetailView3.setProductCode("EAC3");
        proposeCreditDetailView3.setProjectCode("90082");
        proposeCreditDetailView3.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView3.setFrontEndFee(BigDecimal.valueOf(2.25));
        proposeCreditDetailView3.setCreditTierDetailViewList(null);
        proposeCreditDetailView3.setRequestType(0);
        proposeCreditDetailView3.setRefinance(1);
        proposeCreditDetailView3.setRemark("Purpose Detail Example 3");
        proposeCreditDetailView3.setDisbursement(disbursement);
        proposeCreditDetailView3.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 4 ---------------------------------------//
        ProposeCreditDetailView proposeCreditDetailView4 = new ProposeCreditDetailView();
        proposeCreditDetailView4.setProductProgram(productProgram);
        proposeCreditDetailView4.setCreditType(creditType);
        proposeCreditDetailView4.setProductCode("EAC4");
        proposeCreditDetailView4.setProjectCode("90086");
        proposeCreditDetailView4.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView4.setFrontEndFee(BigDecimal.valueOf(2.75));
        proposeCreditDetailView4.setCreditTierDetailViewList(new ArrayList<CreditTierDetailView>());
        proposeCreditDetailView4.setRequestType(1);
        proposeCreditDetailView4.setRefinance(0);
        proposeCreditDetailView4.setRemark("Purpose Detail Example 4");
        proposeCreditDetailView4.setDisbursement(disbursement);
        proposeCreditDetailView4.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        // Add Propose Credit Detail Rows
        proposeCreditDetailViewList.add(proposeCreditDetailView1);
        proposeCreditDetailViewList.add(proposeCreditDetailView2);
        proposeCreditDetailViewList.add(proposeCreditDetailView3);
        proposeCreditDetailViewList.add(proposeCreditDetailView4);

        creditFacProposeView = new CreditFacProposeView();
        creditFacProposeView.setProposeCreditDetailViewList(proposeCreditDetailViewList);
    }

    public ExistingCreditView getExistingCreditView() {
        return existingCreditView;
    }

    public void setExistingCreditView(ExistingCreditView existingCreditView) {
        this.existingCreditView = existingCreditView;
    }

    public List<ProposeConditionDetailView> getExistingConditionCommCrdList() {
        return existingConditionCommCrdList;
    }

    public void setExistingConditionCommCrdList(List<ProposeConditionDetailView> existingConditionCommCrdList) {
        this.existingConditionCommCrdList = existingConditionCommCrdList;
    }

    public CreditFacProposeView getCreditFacProposeView() {
        return creditFacProposeView;
    }

    public void setCreditFacProposeView(CreditFacProposeView creditFacProposeView) {
        this.creditFacProposeView = creditFacProposeView;
    }
}
