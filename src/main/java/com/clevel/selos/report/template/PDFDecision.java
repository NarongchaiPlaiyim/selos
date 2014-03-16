package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.*;
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

    public List<BorrowerCreditDecisionReport> fillCreditBorrower(){
        log.debug("on fillCreditBorrower. {}");
        init();
        List<ExistingCreditDetailView> existingCreditDetailViews = decisionView.getExtBorrowerComCreditList();
        List<BorrowerCreditDecisionReport> borrowerCreditDecisionReportList = new ArrayList<BorrowerCreditDecisionReport>();

        int count = 1;
        if (Util.safetyList(existingCreditDetailViews).size() > 0){
            log.debug("existingCreditDetailViews by fillCreditBorrower. {}",existingCreditDetailViews);
            for (ExistingCreditDetailView detailView : existingCreditDetailViews){
                BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
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
                borrowerCreditDecisionReportList.add(decisionReport);
            }
        } else {
            BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
            borrowerCreditDecisionReportList.add(decisionReport);
            log.debug("existingCreditDetailViews is Null by fillCreditBorrower. {}",existingCreditDetailViews);
        }
        return borrowerCreditDecisionReportList;
    }

    public List<ConditionDecisionReport> fillCondition(){
        log.debug("on fillCondition. {}");
        init();
        List<ConditionDecisionReport> conditionDecisionReportList = new ArrayList<ConditionDecisionReport>();
        List<ExistingConditionDetailView> existingConditionDetailViews = decisionView.getExtConditionComCreditList();
        int count =1;

        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillCondition. {}",existingConditionDetailViews);
            for (ExistingConditionDetailView view : existingConditionDetailViews){
                ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
                conditionDecisionReport.setCount(count++);
                conditionDecisionReport.setLoanType(view.getLoanType());
                conditionDecisionReport.setConditionDesc(view.getConditionDesc());
                conditionDecisionReportList.add(conditionDecisionReport);
            }
        } else {
            ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
            conditionDecisionReportList.add(conditionDecisionReport);
         log.debug("existingConditionDetailViews is Null by fillCondition. {}",existingConditionDetailViews);
        }
        return conditionDecisionReportList;
    }

    public List<BorrowerRetailDecisionReport> fillBorrowerRetail(){
        log.debug("on fillBorrowerRetail. {}");
        init();
        List<BorrowerRetailDecisionReport> retailDecisionReportList = new ArrayList<BorrowerRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerRetailCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillBorrowerRetail. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
                borrowerRetailDecisionReport.setCount(count++);
                borrowerRetailDecisionReport.setAccountName(detailView.getAccountName());
                if (Util.isNull(detailView.getAccountName())){
                    borrowerRetailDecisionReport.setAccountName("-");
                }

                borrowerRetailDecisionReport.setAccountNumber(detailView.getAccountNumber());
                if (Util.isNull(detailView.getAccountNumber())){
                    borrowerRetailDecisionReport.setAccountNumber("-");
                }

                borrowerRetailDecisionReport.setAccountSuf(detailView.getAccountSuf());
                if (Util.isNull(detailView.getAccountSuf())){
                    borrowerRetailDecisionReport.setAccountSuf("-");
                }

                borrowerRetailDecisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
                if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                    borrowerRetailDecisionReport.setDescription("-");
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
        } else {
            BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
            retailDecisionReportList.add(borrowerRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillBorrowerRetail. {}",existingConditionDetailViews);
        }
        return retailDecisionReportList;
    }

    public List<BorrowerAppInRLOSDecisionReport> fillAppInRLOS(){
        init();
        List<BorrowerAppInRLOSDecisionReport> borrowerAppInRLOSDecisionReportList = new ArrayList<BorrowerAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = new ArrayList<ExistingCreditDetailView>();
        existingConditionDetailViews = decisionView.getExtBorrowerAppInRLOSList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillAppInRLOS. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
                borrowerAppInRLOSDecisionReport.setCount(count++);
                borrowerAppInRLOSDecisionReport.setAccountName(detailView.getAccountName());
                if (Util.isNull(detailView.getAccountName())){
                    borrowerAppInRLOSDecisionReport.setAccountName("-");
                }

                borrowerAppInRLOSDecisionReport.setAccountNumber(detailView.getAccountNumber());
                if (Util.isNull(detailView.getAccountNumber())){
                    borrowerAppInRLOSDecisionReport.setAccountNumber("-");
                }

                borrowerAppInRLOSDecisionReport.setAccountSuf(detailView.getAccountSuf());
                if (Util.isNull(detailView.getAccountSuf())){
                    borrowerAppInRLOSDecisionReport.setAccountSuf("-");
                }

                borrowerAppInRLOSDecisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
                if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                    borrowerAppInRLOSDecisionReport.setDescription("-");
                }
                borrowerAppInRLOSDecisionReport.setProductProgramName(detailView.getExistProductProgramView().getName());
                borrowerAppInRLOSDecisionReport.setCreditTypeName(detailView.getExistCreditTypeView().getName());
                borrowerAppInRLOSDecisionReport.setProductCode(detailView.getProductCode());
                borrowerAppInRLOSDecisionReport.setProjectCode(detailView.getProjectCode());
                borrowerAppInRLOSDecisionReport.setLimit(detailView.getLimit());
                borrowerAppInRLOSDecisionReport.setPcePercent(detailView.getPceLimit());
                borrowerAppInRLOSDecisionReport.setPceLimit(detailView.getLimit());
                borrowerAppInRLOSDecisionReport.setOutstanding(detailView.getOutstanding());
                borrowerAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
            }
        } else {
            BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
            borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillAppInRLOS. {}",existingConditionDetailViews);
        }
        return borrowerAppInRLOSDecisionReportList;
    }

    public List<ProposedCreditDecisionReport> fillProposedCredit(){
        log.debug("on fillProposedCredit. {}");
        init();
        newCreditDetailViewList = decisionView.getProposeCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        if (Util.safetyList(newCreditDetailViewList).size() > 0){
            log.debug("newCreditDetailViewList by fillProposedCredit. {}",newCreditDetailViewList);
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
        } else {
            ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
            proposedCreditDecisionReportList.add(proposedView);
            log.debug("newCreditDetailViewList is Null by fillProposedCredit. {}",newCreditDetailViewList);
        }
        return proposedCreditDecisionReportList;
    }
}
