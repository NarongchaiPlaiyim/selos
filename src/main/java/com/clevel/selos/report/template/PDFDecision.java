package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.controller.Decision;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
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

    @Inject
    Decision decision;

    private List<NewCreditDetailView> newCreditDetailViewList;


//    long workCaseId;



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
        long workCaseId = 147;

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
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerAppInRLOSList();

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

    public List<RelatedCommercialDecisionReport> fillRelatedCommercial(){
        List<RelatedCommercialDecisionReport> relatedCommercialDecisionReports = new ArrayList<RelatedCommercialDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedComCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedCommercial. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
                relatedCommercialDecisionReport.setCount(count++);
                relatedCommercialDecisionReport.setAccountName(detailView.getAccountName());
                if (Util.isNull(detailView.getAccountName())){
                    relatedCommercialDecisionReport.setAccountName("-");
                }

                relatedCommercialDecisionReport.setAccountNumber(detailView.getAccountNumber());
                if (Util.isNull(detailView.getAccountNumber())){
                    relatedCommercialDecisionReport.setAccountNumber("-");
                }

                relatedCommercialDecisionReport.setAccountSuf(detailView.getAccountSuf());
                if (Util.isNull(detailView.getAccountSuf())){
                    relatedCommercialDecisionReport.setAccountSuf("-");
                }

                relatedCommercialDecisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
                if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                    relatedCommercialDecisionReport.setDescription("-");
                }
                relatedCommercialDecisionReport.setProductProgramName(detailView.getExistProductProgramView().getName());
                relatedCommercialDecisionReport.setCreditTypeName(detailView.getExistCreditTypeView().getName());
                relatedCommercialDecisionReport.setProductCode(detailView.getProductCode());
                relatedCommercialDecisionReport.setProjectCode(detailView.getProjectCode());
                relatedCommercialDecisionReport.setLimit(detailView.getLimit());
                relatedCommercialDecisionReport.setPcePercent(detailView.getPceLimit());
                relatedCommercialDecisionReport.setPceLimit(detailView.getLimit());
                relatedCommercialDecisionReport.setOutstanding(detailView.getOutstanding());
                relatedCommercialDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                relatedCommercialDecisionReports.add(relatedCommercialDecisionReport);
            }
        } else {
            RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
            relatedCommercialDecisionReports.add(relatedCommercialDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedCommercial. {}",existingConditionDetailViews);
        }

        return relatedCommercialDecisionReports;
    }

    public List<RelatedRetailDecisionReport> fillRelatedRetail(){
        List<RelatedRetailDecisionReport> relatedRetailDecisionReportList = new ArrayList<RelatedRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedRetailCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedRetail. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
                relatedRetailDecisionReport.setCount(count++);
                relatedRetailDecisionReport.setAccountName(detailView.getAccountName());
                if (Util.isNull(detailView.getAccountName())){
                    relatedRetailDecisionReport.setAccountName("-");
                }

                relatedRetailDecisionReport.setAccountNumber(detailView.getAccountNumber());
                if (Util.isNull(detailView.getAccountNumber())){
                    relatedRetailDecisionReport.setAccountNumber("-");
                }

                relatedRetailDecisionReport.setAccountSuf(detailView.getAccountSuf());
                if (Util.isNull(detailView.getAccountSuf())){
                    relatedRetailDecisionReport.setAccountSuf("-");
                }

                relatedRetailDecisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
                if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                    relatedRetailDecisionReport.setDescription("-");
                }
                relatedRetailDecisionReport.setProductProgramName(detailView.getExistProductProgramView().getName());
                relatedRetailDecisionReport.setCreditTypeName(detailView.getExistCreditTypeView().getName());
                relatedRetailDecisionReport.setProductCode(detailView.getProductCode());
                relatedRetailDecisionReport.setProjectCode(detailView.getProjectCode());
                relatedRetailDecisionReport.setLimit(detailView.getLimit());
                relatedRetailDecisionReport.setPcePercent(detailView.getPceLimit());
                relatedRetailDecisionReport.setPceLimit(detailView.getLimit());
                relatedRetailDecisionReport.setOutstanding(detailView.getOutstanding());
                relatedRetailDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            }
        } else {
            RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
            relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedRetail. {}",existingConditionDetailViews);
        }

        return relatedRetailDecisionReportList;
    }

    public List<RelatedAppInRLOSDecisionReport> fillRelatedAppInRLOS(){
        List<RelatedAppInRLOSDecisionReport> relatedAppInRLOSDecisionReportArrayList = new ArrayList<RelatedAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedAppInRLOSList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : decisionView.getExtRelatedAppInRLOSList()){
                RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
                relatedAppInRLOSDecisionReport.setCount(count++);
                relatedAppInRLOSDecisionReport.setAccountName(detailView.getAccountName());
                if (Util.isNull(detailView.getAccountName())){
                    relatedAppInRLOSDecisionReport.setAccountName("-");
                }

                relatedAppInRLOSDecisionReport.setAccountNumber(detailView.getAccountNumber());
                if (Util.isNull(detailView.getAccountNumber())){
                    relatedAppInRLOSDecisionReport.setAccountNumber("-");
                }

                relatedAppInRLOSDecisionReport.setAccountSuf(detailView.getAccountSuf());
                if (Util.isNull(detailView.getAccountSuf())){
                    relatedAppInRLOSDecisionReport.setAccountSuf("-");
                }

                relatedAppInRLOSDecisionReport.setDescription(detailView.getExistAccountStatusView().getDescription());
                if (Util.isNull(detailView.getExistAccountStatusView().getDescription())){
                    relatedAppInRLOSDecisionReport.setDescription("-");
                }
                relatedAppInRLOSDecisionReport.setProductProgramName(detailView.getExistProductProgramView().getName());
                relatedAppInRLOSDecisionReport.setCreditTypeName(detailView.getExistCreditTypeView().getName());
                relatedAppInRLOSDecisionReport.setProductCode(detailView.getProductCode());
                relatedAppInRLOSDecisionReport.setProjectCode(detailView.getProjectCode());
                relatedAppInRLOSDecisionReport.setLimit(detailView.getLimit());
                relatedAppInRLOSDecisionReport.setPcePercent(detailView.getPceLimit());
                relatedAppInRLOSDecisionReport.setPceLimit(detailView.getLimit());
                relatedAppInRLOSDecisionReport.setOutstanding(detailView.getOutstanding());
                relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            }
        } else {
            RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
            relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
        }

        return relatedAppInRLOSDecisionReportArrayList;
    }

    public List<ExistingCollateralBorrowerDecisionReport> fillExistingCollateralBorrower(){
        init();
        List<ExistingCollateralBorrowerDecisionReport> collateralBorrowerDecisionReportList = new ArrayList<ExistingCollateralBorrowerDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtBorrowerCollateralList();

        int count = 1;
        if (Util.safetyList(conditionDetailViews).size() > 0){
            log.debug("conditionDetailViews by fillExistingCollateralBorrower. {}",conditionDetailViews);
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
                collateralBorrowerDecisionReport.setCount(count++);
                collateralBorrowerDecisionReport.setPotentialCollateral(detailView.getPotentialCollateral().getDescription());
                collateralBorrowerDecisionReport.setCollateralType(detailView.getCollateralType().getDescription());
                collateralBorrowerDecisionReport.setOwner(detailView.getOwner());
                collateralBorrowerDecisionReport.setRelation(detailView.getRelation().getDescription());
                collateralBorrowerDecisionReport.setAppraisalDate(detailView.getAppraisalDate());
                collateralBorrowerDecisionReport.setCollateralNumber(detailView.getCollateralNumber());
                collateralBorrowerDecisionReport.setCollateralLocation(detailView.getCollateralLocation());
                collateralBorrowerDecisionReport.setRemark(detailView.getRemark());
                collateralBorrowerDecisionReport.setCusName(detailView.getCusName());
                collateralBorrowerDecisionReport.setAccountNumber(detailView.getAccountNumber());
                collateralBorrowerDecisionReport.setAccountSuffix(detailView.getAccountSuffix());
                collateralBorrowerDecisionReport.setProductProgram(detailView.getProductProgram());
                collateralBorrowerDecisionReport.setCreditFacility(detailView.getCreditFacility());
                collateralBorrowerDecisionReport.setLimit(detailView.getLimit());
                collateralBorrowerDecisionReport.setMortgageType(detailView.getMortgageType().getMortgage());
                collateralBorrowerDecisionReport.setAppraisalValue(detailView.getAppraisalValue());
                collateralBorrowerDecisionReport.setMortgageValue(detailView.getMortgageValue());
                collateralBorrowerDecisionReportList.add(collateralBorrowerDecisionReport);
            }
        } else {
            ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
            collateralBorrowerDecisionReportList.add(collateralBorrowerDecisionReport);
            log.debug("conditionDetailViews is Null by fillExistingCollateralBorrower. {}",conditionDetailViews);
        }

        return collateralBorrowerDecisionReportList;
    }

    public List<ExistingCollateralRelatedDecisionReport> fillExistingCollateralRelated(){
        init();
        List<ExistingCollateralRelatedDecisionReport> collateralRelatedDecisionReportArrayList = new ArrayList<ExistingCollateralRelatedDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtRelatedCollateralList();

        int count = 1;
        if (Util.safetyList(conditionDetailViews).size() > 0){
            log.debug("conditionDetailViews by fillExistingCollateralRelated. {}",conditionDetailViews);
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralRelatedDecisionReport collateralRelatedDecisionReport = new ExistingCollateralRelatedDecisionReport();
                collateralRelatedDecisionReport.setCount(count++);
                collateralRelatedDecisionReport.setPotentialCollateral(detailView.getPotentialCollateral().getDescription());
                collateralRelatedDecisionReport.setCollateralType(detailView.getCollateralType().getDescription());
                collateralRelatedDecisionReport.setOwner(detailView.getOwner());
                collateralRelatedDecisionReport.setRelation(detailView.getRelation().getDescription());
                collateralRelatedDecisionReport.setAppraisalDate(detailView.getAppraisalDate());
                collateralRelatedDecisionReport.setCollateralNumber(detailView.getCollateralNumber());
                collateralRelatedDecisionReport.setCollateralLocation(detailView.getCollateralLocation());
                collateralRelatedDecisionReport.setRemark(detailView.getRemark());
                collateralRelatedDecisionReport.setCusName(detailView.getCusName());
                collateralRelatedDecisionReport.setAccountNumber(detailView.getAccountNumber());
                collateralRelatedDecisionReport.setAccountSuffix(detailView.getAccountSuffix());
                collateralRelatedDecisionReport.setProductProgram(detailView.getProductProgram());
                collateralRelatedDecisionReport.setCreditFacility(detailView.getCreditFacility());
                collateralRelatedDecisionReport.setLimit(detailView.getLimit());
                collateralRelatedDecisionReport.setMortgageType(detailView.getMortgageType().getMortgage());
                collateralRelatedDecisionReport.setAppraisalValue(detailView.getAppraisalValue());
                collateralRelatedDecisionReport.setMortgageValue(detailView.getMortgageValue());
                collateralRelatedDecisionReportArrayList.add(collateralRelatedDecisionReport);
            }
        } else {
            ExistingCollateralRelatedDecisionReport collateralRelatedDecisionReport = new ExistingCollateralRelatedDecisionReport();
            collateralRelatedDecisionReportArrayList.add(collateralRelatedDecisionReport);
            log.debug("conditionDetailViews is Null by fillExistingCollateralRelated. {}",conditionDetailViews);
        }

        return collateralRelatedDecisionReportArrayList;
    }

    public List<GuarantorBorrowerDecisionReport> fillGuarantorBorrower(){
        init();
        List<GuarantorBorrowerDecisionReport> guarantorBorrowerDecisionReportList = new ArrayList<GuarantorBorrowerDecisionReport>();
        List<ExistingGuarantorDetailView> extGuarantorList = decisionView.getExtGuarantorList();
        int count = 1;
        if (Util.safetyList(extGuarantorList).size() > 0){
            log.debug("extGuarantorList by fillGuarantorBorrower. {}",extGuarantorList);
            for (ExistingGuarantorDetailView detailView : extGuarantorList){
                GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
                guarantorBorrowerDecisionReport.setCount(count++);
                guarantorBorrowerDecisionReport.setGuarantorName(detailView.getGuarantorName().getFirstNameTh()+"  "+detailView.getGuarantorName().getLastNameTh());
                guarantorBorrowerDecisionReport.setTcgLgNo(detailView.getTcgLgNo());
                guarantorBorrowerDecisionReport.setExistingCreditTypeDetailViewList(detailView.getExistingCreditTypeDetailViewList());
                guarantorBorrowerDecisionReport.setTotalLimitGuaranteeAmount(detailView.getTotalLimitGuaranteeAmount());
                guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            }
        } else {
            log.debug("extGuarantorList is Null by fillGuarantorBorrower. {}",extGuarantorList);
            GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
            guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
        }

        return guarantorBorrowerDecisionReportList;
    }

    public List<ProposedCreditDecisionReport> fillProposedCredit(){
        log.debug("on fillProposedCredit. {}");
        init();
        newCreditDetailViewList = decisionView.getProposeCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        int count = 1;
        if (Util.safetyList(newCreditDetailViewList).size() > 0){
            log.debug("newCreditDetailViewList by fillProposedCredit. {}",newCreditDetailViewList);
            for (NewCreditDetailView detailView : newCreditDetailViewList){
                ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
                proposedView.setCount(count++);
                proposedView.setProdName(detailView.getProductProgramView().getName());

                if ((detailView.getUwDecision()) == decision.getDecisionAPPROVED()){
                    proposedView.setUwDecision("APPROVED");
                } else if (detailView.getUwDecision() == decision.getDecisionREJECTED()){
                    proposedView.setUwDecision("REJECTED");
                } else {
                    proposedView.setUwDecision("");
                }

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

                if (detailView.getRequestType() == decision.getRequestTypeNEW().value()){
                    proposedView.setRequestType("New");
                } else if (detailView.getRequestType() == decision.getRequestTypeCHANGE().value()){
                    proposedView.setRequestType("Change");
                }

                if (detailView.getRefinance() == decision.getYesValue()){
                    proposedView.setRefinance("Yes");
                } else if (detailView.getRefinance() == decision.getNoValue()){
                    proposedView.setRefinance("No");
                }

                if (!Util.isNull(detailView.getLoanPurposeView())){
                    proposedView.setPurposeDescription(detailView.getLoanPurposeView().getDescription());
                } else {
                    proposedView.setPurposeDescription("");
                }

                if (!Util.isNull(detailView.getDisbursementTypeView())){
                    proposedView.setDisbursement(detailView.getDisbursementTypeView().getDisbursement());
                } else {
                    proposedView.setDisbursement("");
                }
                if (!Util.isNull(detailView.getRemark())){

                    proposedView.setRemark(detailView.getRemark());
                }
                proposedView.setHoldLimitAmount(detailView.getHoldLimitAmount());

                proposedCreditDecisionReportList.add(proposedView);
            }
        } else {
            ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
            proposedCreditDecisionReportList.add(proposedView);
            log.debug("newCreditDetailViewList is Null by fillProposedCredit. {}",newCreditDetailViewList);
        }
        return proposedCreditDecisionReportList;
    }

    public List<ProposeFeeInformationDecisionReport> fillProposeFeeInformation(){
        init();
        List<ProposeFeeInformationDecisionReport> proposeFeeInformationDecisionReportList = new ArrayList<ProposeFeeInformationDecisionReport>();
        List<NewFeeDetailView> feeDetailViewList = decisionView.getProposeFeeInfoList();

        int count = 1;
        if (Util.safetyList(feeDetailViewList).size() > 0){
            log.debug("feeDetailViewList by fillProposeFeeInformation. {}",feeDetailViewList);
            for (NewFeeDetailView view : feeDetailViewList){
                ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
                proposeFeeInformationDecisionReport.setCount(count++);
                proposeFeeInformationDecisionReport.setProductProgram(view.getProductProgram());
                proposeFeeInformationDecisionReport.setStandardFrontEndFee(view.getStandardFrontEndFee().getFeeAmount());
                proposeFeeInformationDecisionReport.setStandardFrontEndFee(view.getStandardFrontEndFee().getFeeYear());
                proposeFeeInformationDecisionReport.setCommitmentFee(view.getCommitmentFee().getFeeAmount());
                proposeFeeInformationDecisionReport.setCommitmentFeeYear(view.getCommitmentFee().getFeeYear());
                proposeFeeInformationDecisionReport.setExtensionFee(view.getExtensionFee().getFeeAmount());
                proposeFeeInformationDecisionReport.setExtensionFeeYear(view.getExtensionFee().getFeeYear());
                proposeFeeInformationDecisionReport.setPrepaymentFee(view.getPrepaymentFee().getFeeAmount());
                proposeFeeInformationDecisionReport.setPrepaymentFeeYear(view.getPrepaymentFee().getFeeYear());
                proposeFeeInformationDecisionReport.setCancellationFee(view.getCancellationFee().getFeeAmount());
                proposeFeeInformationDecisionReport.setCancellationFeeYear(view.getCancellationFee().getFeeYear());
                proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            }
        } else {
            log.debug("feeDetailViewList is Null by fillProposeFeeInformation. {}",feeDetailViewList);
            ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
            proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
        }


        return proposeFeeInformationDecisionReportList;
    }

    public List<ProposedCollateralDecisionReport> fillProposedCollateral(){
        init();
        List<ProposedCollateralDecisionReport> proposedCollateralDecisionReportList = new ArrayList<ProposedCollateralDecisionReport>();
        List<NewCollateralView> newCollateralViews = decisionView.getProposeCollateralList();
        List<NewCollateralHeadView> collateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        if (Util.safetyList(newCollateralViews).size() > 0){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (NewCollateralView view : newCollateralViews){
                ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
                collateralDecisionReport.setJobID(view.getJobID());
                collateralDecisionReport.setAppraisalDate(view.getAppraisalDate());
                collateralDecisionReport.setAadDecision(view.getAadDecision());
                collateralDecisionReport.setAadDecisionReason(view.getAadDecisionReason());
                collateralDecisionReport.setAadDecisionReasonDetail(view.getAadDecisionReasonDetail());
                collateralDecisionReport.setUsage(view.getUsage());
                collateralDecisionReport.setTypeOfUsage(view.getTypeOfUsage());
                collateralDecisionReport.setMortgageCondition(view.getMortgageCondition());
                collateralDecisionReport.setMortgageConditionDetail(view.getMortgageConditionDetail());

                if (Util.safetyList(view.getProposeCreditDetailViewList()).size() > 0) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditDetailViewList());
                    collateralDecisionReport.setProposeCreditDetailViewList(view.getProposeCreditDetailViewList());
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditDetailViewList());
                    collateralDecisionReport.setProposeCreditDetailViewList(collateralDecisionReport.getProposeCreditDetailViewList());
                }

                collateralHeadViewList = view.getNewCollateralHeadViewList();
                if (Util.safetyList(collateralHeadViewList).size() > 0){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                    for (NewCollateralHeadView headView : collateralHeadViewList){
                        collateralDecisionReport.setCollateralDescription(headView.getPotentialCollateral().getDescription());
                        collateralDecisionReport.setPercentLTVDescription(headView.getCollTypePercentLTV().getDescription());
                        collateralDecisionReport.setExistingCredit(headView.getExistingCredit());
                        collateralDecisionReport.setTitleDeed(headView.getTitleDeed());
                        collateralDecisionReport.setCollateralLocation(headView.getCollateralLocation());
                        collateralDecisionReport.setAppraisalValue(headView.getAppraisalValue());
                        collateralDecisionReport.setHeadCollTypeDescription(headView.getHeadCollType().getDescription());
                        if (headView.getInsuranceCompany() == decision.getYesValue()){
                            collateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == decision.getNoValue()){
                            collateralDecisionReport.setInsuranceCompany("Non Partner");
                        } else {
                            collateralDecisionReport.setInsuranceCompany("");
                        }
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}",collateralHeadViewList);
                }

                if (Util.safetyList(view.getNewCollateralHeadViewList()).size() > 0) {
                    log.debug("getNewCollateralHeadViewList. {}",view.getNewCollateralHeadViewList());
                    collateralDecisionReport.setNewCollateralHeadViews(view.getNewCollateralHeadViewList());
                } else {
                    log.debug("getNewCollateralHeadViewList is Null. {}",view.getProposeCreditDetailViewList());
                    collateralDecisionReport.setNewCollateralHeadViews(collateralDecisionReport.getNewCollateralHeadViews());
                }
                proposedCollateralDecisionReportList.add(collateralDecisionReport);
            }

        } else {
            log.debug("newCollateralViews is Null by fillProposedCollateral. {}",newCollateralViews);
            ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
            proposedCollateralDecisionReportList.add(collateralDecisionReport);
        }
        return proposedCollateralDecisionReportList;
    }
}
