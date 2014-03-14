package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.BorrowerRetailDecisionReport;
import com.clevel.selos.model.report.ConditionDecisionReport;
import com.clevel.selos.model.report.CreditBorrowerDecisionReport;
import com.clevel.selos.model.report.ProposedCreditDecisionReport;
import com.clevel.selos.model.view.DecisionView;
import com.clevel.selos.model.view.ExistingConditionDetailView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PDFDecision implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    DecisionControl decisionControl;

    @Inject
    DecisionView decisionView;

    private List<NewCreditDetailView> newCreditDetailViewList;


//    long workCaseId;

    long workCaseId = 147;

    public PDFDecision() {
    }

    public void init(){
//        HttpSession session = FacesUtil.getSession(true);
//
//        if(session.getAttribute("workCaseId") != null){
//            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//        }else{
//            log.debug("onCreation ::: workCaseId is null.");
//            try{
//                FacesUtil.redirect("/site/inbox.jsf");
//            }catch (Exception ex){
//                log.error("Exception :: {}",ex);
//            }
//        }

        decisionView = decisionControl.getDecisionView(workCaseId);

        log.info("workCaseID: {}",workCaseId);
    }

    public List<CreditBorrowerDecisionReport> fillCreditBorrower(){
        init();
        List<ExistingCreditDetailView> existingCreditDetailViews = decisionView.getExtBorrowerComCreditList();
        List<CreditBorrowerDecisionReport> creditBorrowerDecisionReportList = new ArrayList<CreditBorrowerDecisionReport>();

        int count = 1;
        for (ExistingCreditDetailView detailView : existingCreditDetailViews){
            CreditBorrowerDecisionReport decisionReport = new CreditBorrowerDecisionReport();
            decisionReport.setCount(count++);
            decisionReport.setAccountName(detailView.getAccountName());
            if (Util.isNull(detailView.getAccountName())){
                decisionReport.setAccountName("");
            }

            decisionReport.setAccountNumber(detailView.getAccountNumber());
            if (Util.isNull(detailView.getAccountNumber())){
                decisionReport.setAccountNumber("");
            }

            decisionReport.setAccountSuf(detailView.getAccountSuf());
            if (Util.isNull(detailView.getAccountSuf())){
                decisionReport.setAccountSuf("");
            }

            decisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
            if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                decisionReport.setDescription("");
            }
            decisionReport.setProductProgramName(detailView.getExistProductProgramView().getName());
            decisionReport.setCreditTypeName(detailView.getExistCreditTypeView().getName());
            decisionReport.setProductCode(detailView.getProductCode());
            decisionReport.setProjectCode(detailView.getProjectCode());
            decisionReport.setLimit(detailView.getLimit());
            decisionReport.setPcePercent(detailView.getPceLimit());
            decisionReport.setPceLimit(detailView.getLimit());
            decisionReport.setOutstanding(detailView.getOutstanding());
            decisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
            creditBorrowerDecisionReportList.add(decisionReport);
        }

        return creditBorrowerDecisionReportList;
    }

    public List<ConditionDecisionReport> fillCondition(){
        init();
        List<ConditionDecisionReport> conditionDecisionReportList = new ArrayList<ConditionDecisionReport>();
        List<ExistingConditionDetailView> existingConditionDetailViews = decisionView.getExtConditionComCreditList();
        int count =1;

        if(!Util.isNull(existingConditionDetailViews)){
            for (ExistingConditionDetailView view : existingConditionDetailViews){
                ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
                conditionDecisionReport.setCount(count++);
                conditionDecisionReport.setLoanType(view.getLoanType());
                conditionDecisionReport.setConditionDesc(view.getConditionDesc());
                conditionDecisionReportList.add(conditionDecisionReport);
            }
        } else {
            ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
//            conditionDecisionReport.setCount("");
            conditionDecisionReport.setLoanType("");
            conditionDecisionReport.setConditionDesc("");
            conditionDecisionReportList.add(conditionDecisionReport);
        }

        log.debug("conditionDecisionReportList: {}",conditionDecisionReportList);
        return conditionDecisionReportList;
    }

    public List<BorrowerRetailDecisionReport> fillBorrowerRetail(){
        init();
        List<BorrowerRetailDecisionReport> retailDecisionReportList = new ArrayList<BorrowerRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerRetailCreditList();

        int count = 1;
        for (ExistingCreditDetailView detailView : existingConditionDetailViews){
            BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
            borrowerRetailDecisionReport.setCount(count++);
            borrowerRetailDecisionReport.setAccountName(detailView.getAccountName());
            if (Util.isNull(detailView.getAccountName())){
                borrowerRetailDecisionReport.setAccountName("");
            }

            borrowerRetailDecisionReport.setAccountNumber(detailView.getAccountNumber());
            if (Util.isNull(detailView.getAccountNumber())){
                borrowerRetailDecisionReport.setAccountNumber("");
            }

            borrowerRetailDecisionReport.setAccountSuf(detailView.getAccountSuf());
            if (Util.isNull(detailView.getAccountSuf())){
                borrowerRetailDecisionReport.setAccountSuf("");
            }

            borrowerRetailDecisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
            if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                borrowerRetailDecisionReport.setDescription("");
            }
            borrowerRetailDecisionReport.setProductProgramName(detailView.getExistProductProgramView().getName());
            borrowerRetailDecisionReport.setCreditTypeName(detailView.getExistCreditTypeView().getName());
            borrowerRetailDecisionReport.setProductCode(detailView.getProductCode());
            borrowerRetailDecisionReport.setProjectCode(detailView.getProjectCode());
            borrowerRetailDecisionReport.setLimit(detailView.getLimit());
            borrowerRetailDecisionReport.setPcePercent(detailView.getPceLimit());
            borrowerRetailDecisionReport.setPceLimit(detailView.getLimit());
            borrowerRetailDecisionReport.setOutstanding(detailView.getOutstanding());
            borrowerRetailDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
            retailDecisionReportList.add(borrowerRetailDecisionReport);
        }

        return retailDecisionReportList;
    }

    public List<ProposedCreditDecisionReport> fillProposedCredit(){
        init();
        newCreditDetailViewList = decisionView.getProposeCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        for (NewCreditDetailView detailView : newCreditDetailViewList){
            ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
            proposedView.setProdName(detailView.getProductProgramView().getName());

            if (Util.isNull(detailView.getProductProgramView().getName())){
                proposedView.setProdName("");
            }

            proposedView.setCredittypeName(detailView.getCreditTypeView().getName());

            if (Util.isNull(detailView.getCreditTypeView().getName())){
                proposedView.setCredittypeName("");
            }

            proposedView.setProdCode(detailView.getProductCode());

            if (Util.isNull(detailView.getProductCode())){
                proposedView.setProdCode("");
            }

            proposedView.setProjectCode(detailView.getProjectCode());
            proposedView.setLimit(detailView.getLimit());
            proposedView.setFrontEndFee(detailView.getFrontEndFee());
            proposedView.setNewCreditTierDetailViews(detailView.getNewCreditTierDetailViewList());
            proposedCreditDecisionReportList.add(proposedView);
        }
        for (ProposedCreditDecisionReport a : proposedCreditDecisionReportList){
            log.debug("tier detail : {}", a.getNewCreditTierDetailViews());
        }
        return proposedCreditDecisionReportList;
    }
}
