package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.controller.Decision;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
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
                decisionReport.setAccountName(Util.checkNullStrint(detailView.getAccountName()));
                decisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                decisionReport.setAccountSuf(Util.checkNullStrint(detailView.getAccountSuf()));
                decisionReport.setDescription(Util.checkNullStrint(detailView.getExistAccountStatusView().getDescription()));
                decisionReport.setProductProgramName(Util.checkNullStrint(detailView.getExistProductProgramView().getName()));
                decisionReport.setCreditTypeName(Util.checkNullStrint(detailView.getExistCreditTypeView().getName()));
                decisionReport.setProductCode(Util.checkNullStrint(detailView.getProductCode()));
                decisionReport.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                decisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                decisionReport.setPcePercent(Util.converNullToZERO(detailView.getPceLimit()));
                decisionReport.setPceLimit(Util.converNullToZERO(detailView.getLimit()));
                decisionReport.setOutstanding(Util.converNullToZERO(detailView.getOutstanding()));
                decisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
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
                conditionDecisionReport.setLoanType(Util.checkNullStrint(view.getLoanType()));
                conditionDecisionReport.setConditionDesc(Util.checkNullStrint(view.getConditionDesc()));
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
                borrowerRetailDecisionReport.setAccountName(Util.checkNullStrint(detailView.getAccountName()));
                borrowerRetailDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                borrowerRetailDecisionReport.setAccountSuf(Util.checkNullStrint(detailView.getAccountSuf()));
                borrowerRetailDecisionReport.setDescription(Util.checkNullStrint(detailView.getExistAccountStatusView().getDescription()));
                borrowerRetailDecisionReport.setProductProgramName(Util.checkNullStrint(detailView.getExistProductProgramView().getName()));
                borrowerRetailDecisionReport.setCreditTypeName(Util.checkNullStrint(detailView.getExistCreditTypeView().getName()));
                borrowerRetailDecisionReport.setProductCode(Util.checkNullStrint(detailView.getProductCode()));
                borrowerRetailDecisionReport.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                borrowerRetailDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setPcePercent(Util.converNullToZERO(detailView.getPceLimit()));
                borrowerRetailDecisionReport.setPceLimit(Util.converNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setOutstanding(Util.converNullToZERO(detailView.getOutstanding()));
                borrowerRetailDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
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
                borrowerAppInRLOSDecisionReport.setAccountName(Util.checkNullStrint(detailView.getAccountName()));
                borrowerAppInRLOSDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                borrowerAppInRLOSDecisionReport.setAccountSuf(Util.checkNullStrint(detailView.getAccountSuf()));
                borrowerAppInRLOSDecisionReport.setDescription(Util.checkNullStrint(detailView.getExistAccountStatusView().getDescription()));
                borrowerAppInRLOSDecisionReport.setProductProgramName(Util.checkNullStrint(detailView.getExistProductProgramView().getName()));
                borrowerAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullStrint(detailView.getExistCreditTypeView().getName()));
                borrowerAppInRLOSDecisionReport.setProductCode(Util.checkNullStrint(detailView.getProductCode()));
                borrowerAppInRLOSDecisionReport.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                borrowerAppInRLOSDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                borrowerAppInRLOSDecisionReport.setPcePercent(Util.converNullToZERO(detailView.getPceLimit()));
                borrowerAppInRLOSDecisionReport.setPceLimit(Util.converNullToZERO(detailView.getLimit()));
                borrowerAppInRLOSDecisionReport.setOutstanding(Util.converNullToZERO(detailView.getOutstanding()));
                borrowerAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
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
        List<RelatedCommercialDecisionReport> relatedCommercialDecisionReportList = new ArrayList<RelatedCommercialDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedComCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedCommercial. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
                relatedCommercialDecisionReport.setCount(count++);
                relatedCommercialDecisionReport.setAccountName(Util.checkNullStrint(detailView.getAccountName()));
                relatedCommercialDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                relatedCommercialDecisionReport.setAccountSuf(Util.checkNullStrint(detailView.getAccountSuf()));
                relatedCommercialDecisionReport.setDescription(Util.checkNullStrint(detailView.getExistAccountStatusView().getDescription()));
                relatedCommercialDecisionReport.setProductProgramName(Util.checkNullStrint(detailView.getExistProductProgramView().getName()));
                relatedCommercialDecisionReport.setCreditTypeName(Util.checkNullStrint(detailView.getExistCreditTypeView().getName()));
                relatedCommercialDecisionReport.setProductCode(Util.checkNullStrint(detailView.getProductCode()));
                relatedCommercialDecisionReport.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                relatedCommercialDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setPcePercent(Util.converNullToZERO(detailView.getPceLimit()));
                relatedCommercialDecisionReport.setPceLimit(Util.converNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setOutstanding(Util.converNullToZERO(detailView.getOutstanding()));
                relatedCommercialDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            }
        } else {
            RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
            relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedCommercial. {}",existingConditionDetailViews);
        }

        return relatedCommercialDecisionReportList;
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
                relatedRetailDecisionReport.setAccountName(Util.checkNullStrint(detailView.getAccountName()));
                relatedRetailDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                relatedRetailDecisionReport.setAccountSuf(Util.checkNullStrint(detailView.getAccountSuf()));
                relatedRetailDecisionReport.setDescription(Util.checkNullStrint(detailView.getExistAccountStatusView().getDescription()));
                relatedRetailDecisionReport.setProductProgramName(Util.checkNullStrint(detailView.getExistProductProgramView().getName()));
                relatedRetailDecisionReport.setCreditTypeName(Util.checkNullStrint(detailView.getExistCreditTypeView().getName()));
                relatedRetailDecisionReport.setProductCode(Util.checkNullStrint(detailView.getProductCode()));
                relatedRetailDecisionReport.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                relatedRetailDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setPcePercent(Util.converNullToZERO(detailView.getPceLimit()));
                relatedRetailDecisionReport.setPceLimit(Util.converNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setOutstanding(Util.converNullToZERO(detailView.getOutstanding()));
                relatedRetailDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
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
                relatedAppInRLOSDecisionReport.setAccountName(Util.checkNullStrint(detailView.getAccountName()));
                relatedAppInRLOSDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                relatedAppInRLOSDecisionReport.setAccountSuf(Util.checkNullStrint(detailView.getAccountSuf()));
                relatedAppInRLOSDecisionReport.setDescription(Util.checkNullStrint(detailView.getExistAccountStatusView().getDescription()));
                relatedAppInRLOSDecisionReport.setProductProgramName(Util.checkNullStrint(detailView.getExistProductProgramView().getName()));
                relatedAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullStrint(detailView.getExistCreditTypeView().getName()));
                relatedAppInRLOSDecisionReport.setProductCode(Util.checkNullStrint(detailView.getProductCode()));
                relatedAppInRLOSDecisionReport.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                relatedAppInRLOSDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setPcePercent(Util.converNullToZERO(detailView.getPceLimit()));
                relatedAppInRLOSDecisionReport.setPceLimit(Util.converNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setOutstanding(Util.converNullToZERO(detailView.getOutstanding()));
                relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
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
                collateralBorrowerDecisionReport.setPotentialCollateral(Util.checkNullStrint(detailView.getPotentialCollateral().getDescription()));
                collateralBorrowerDecisionReport.setCollateralType(Util.checkNullStrint(detailView.getCollateralType().getDescription()));
                collateralBorrowerDecisionReport.setOwner(Util.checkNullStrint(detailView.getOwner()));
                collateralBorrowerDecisionReport.setRelation(Util.checkNullStrint(detailView.getRelation().getDescription()));

                collateralBorrowerDecisionReport.setAppraisalDate(detailView.getAppraisalDate());

                collateralBorrowerDecisionReport.setCollateralNumber(Util.checkNullStrint(detailView.getCollateralNumber()));
                collateralBorrowerDecisionReport.setCollateralLocation(Util.checkNullStrint(detailView.getCollateralLocation()));
                collateralBorrowerDecisionReport.setRemark(Util.checkNullStrint(detailView.getRemark()));
                collateralBorrowerDecisionReport.setCusName(Util.checkNullStrint(detailView.getCusName()));
                collateralBorrowerDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                collateralBorrowerDecisionReport.setAccountSuffix(Util.checkNullStrint(detailView.getAccountSuffix()));
                collateralBorrowerDecisionReport.setProductProgram(Util.checkNullStrint(detailView.getProductProgram()));
                collateralBorrowerDecisionReport.setCreditFacility(Util.checkNullStrint(detailView.getCreditFacility()));
                collateralBorrowerDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                collateralBorrowerDecisionReport.setMortgageType(Util.checkNullStrint(detailView.getMortgageType().getMortgage()));
                collateralBorrowerDecisionReport.setAppraisalValue(Util.converNullToZERO(detailView.getAppraisalValue()));
                collateralBorrowerDecisionReport.setMortgageValue(Util.converNullToZERO(detailView.getMortgageValue()));
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
                collateralRelatedDecisionReport.setPotentialCollateral(Util.checkNullStrint(detailView.getPotentialCollateral().getDescription()));
                collateralRelatedDecisionReport.setCollateralType(Util.checkNullStrint(detailView.getCollateralType().getDescription()));
                collateralRelatedDecisionReport.setOwner(Util.checkNullStrint(detailView.getOwner()));
                collateralRelatedDecisionReport.setRelation(Util.checkNullStrint(detailView.getRelation().getDescription()));

                collateralRelatedDecisionReport.setAppraisalDate(detailView.getAppraisalDate());

                collateralRelatedDecisionReport.setCollateralNumber(Util.checkNullStrint(detailView.getCollateralNumber()));
                collateralRelatedDecisionReport.setCollateralLocation(Util.checkNullStrint(detailView.getCollateralLocation()));
                collateralRelatedDecisionReport.setRemark(Util.checkNullStrint(detailView.getRemark()));
                collateralRelatedDecisionReport.setCusName(Util.checkNullStrint(detailView.getCusName()));
                collateralRelatedDecisionReport.setAccountNumber(Util.checkNullStrint(detailView.getAccountNumber()));
                collateralRelatedDecisionReport.setAccountSuffix(Util.checkNullStrint(detailView.getAccountSuffix()));
                collateralRelatedDecisionReport.setProductProgram(Util.checkNullStrint(detailView.getProductProgram()));
                collateralRelatedDecisionReport.setCreditFacility(Util.checkNullStrint(detailView.getCreditFacility()));
                collateralRelatedDecisionReport.setLimit(Util.converNullToZERO(detailView.getLimit()));
                collateralRelatedDecisionReport.setMortgageType(Util.checkNullStrint(detailView.getMortgageType().getMortgage()));
                collateralRelatedDecisionReport.setAppraisalValue(Util.converNullToZERO(detailView.getAppraisalValue()));
                collateralRelatedDecisionReport.setMortgageValue(Util.converNullToZERO(detailView.getMortgageValue()));
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
                guarantorBorrowerDecisionReport.setGuarantorName(Util.checkNullStrint(detailView.getGuarantorName().getFirstNameTh()+"  "+detailView.getGuarantorName().getLastNameTh()));
                guarantorBorrowerDecisionReport.setTcgLgNo(Util.checkNullStrint(detailView.getTcgLgNo()));
                guarantorBorrowerDecisionReport.setExistingCreditTypeDetailViewList(Util.safetyList(detailView.getExistingCreditTypeDetailViewList()));
                guarantorBorrowerDecisionReport.setTotalLimitGuaranteeAmount(Util.converNullToZERO(detailView.getTotalLimitGuaranteeAmount()));
                guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            }
        } else {
            GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
            guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            log.debug("extGuarantorList is Null by fillGuarantorBorrower. {}",extGuarantorList);
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
                proposedView.setProdName(Util.checkNullStrint(detailView.getProductProgramView().getName()));

                if ((detailView.getUwDecision()) == DecisionType.APPROVED){
                    proposedView.setUwDecision("APPROVED");
                } else if (detailView.getUwDecision() == DecisionType.REJECTED){
                    proposedView.setUwDecision("REJECTED");
                } else {
                    proposedView.setUwDecision("");
                }
                proposedView.setCredittypeName(Util.checkNullStrint(detailView.getCreditTypeView().getName()));
                proposedView.setProdCode(Util.checkNullStrint(detailView.getProductCode()));
                proposedView.setProjectCode(Util.checkNullStrint(detailView.getProjectCode()));
                proposedView.setLimit(Util.converNullToZERO(detailView.getLimit()));
                proposedView.setFrontEndFee(Util.converNullToZERO(detailView.getFrontEndFee()));
                proposedView.setNewCreditTierDetailViews(Util.safetyList(detailView.getNewCreditTierDetailViewList()));

                if (detailView.getRequestType() == RequestTypes.NEW.value()){
                    proposedView.setRequestType("New");
                } else if (detailView.getRequestType() == RequestTypes.CHANGE.value()){
                    proposedView.setRequestType("Change");
                }

                if (detailView.getRefinance() == RadioValue.YES.value()){
                    proposedView.setRefinance("Yes");
                } else if (detailView.getRefinance() == RadioValue.NO.value()){
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
                proposedView.setRemark(Util.checkNullStrint(detailView.getRemark()));
                proposedView.setHoldLimitAmount(Util.converNullToZERO(detailView.getHoldLimitAmount()));

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
                proposeFeeInformationDecisionReport.setProductProgram(Util.checkNullStrint(view.getProductProgram()));
                proposeFeeInformationDecisionReport.setStandardFrontEndFee(Util.converNullToZERO(view.getStandardFrontEndFee().getFeeAmount()));
                proposeFeeInformationDecisionReport.setStandardFrontEndFee(Util.converNullToZERO(view.getStandardFrontEndFee().getFeeYear()));
                proposeFeeInformationDecisionReport.setCommitmentFee(Util.converNullToZERO(view.getCommitmentFee().getFeeAmount()));
                proposeFeeInformationDecisionReport.setCommitmentFeeYear(Util.converNullToZERO(view.getCommitmentFee().getFeeYear()));
                proposeFeeInformationDecisionReport.setExtensionFee(Util.converNullToZERO(view.getExtensionFee().getFeeAmount()));
                proposeFeeInformationDecisionReport.setExtensionFeeYear(Util.converNullToZERO(view.getExtensionFee().getFeeYear()));
                proposeFeeInformationDecisionReport.setPrepaymentFee(Util.converNullToZERO(view.getPrepaymentFee().getFeeAmount()));
                proposeFeeInformationDecisionReport.setPrepaymentFeeYear(Util.converNullToZERO(view.getPrepaymentFee().getFeeYear()));
                proposeFeeInformationDecisionReport.setCancellationFee(Util.converNullToZERO(view.getCancellationFee().getFeeAmount()));
                proposeFeeInformationDecisionReport.setCancellationFeeYear(Util.converNullToZERO(view.getCancellationFee().getFeeYear()));
                proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            }
        } else {
            ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
            proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            log.debug("feeDetailViewList is Null by fillProposeFeeInformation. {}",feeDetailViewList);
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
                collateralDecisionReport.setJobID(Util.checkNullStrint(view.getJobID()));
                collateralDecisionReport.setAppraisalDate(view.getAppraisalDate());
                collateralDecisionReport.setAadDecision(Util.checkNullStrint(view.getAadDecision()));
                collateralDecisionReport.setAadDecisionReason(Util.checkNullStrint(view.getAadDecisionReason()));
                collateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullStrint(view.getAadDecisionReasonDetail()));
                collateralDecisionReport.setUsage(Util.checkNullStrint(view.getUsage()));
                collateralDecisionReport.setTypeOfUsage(Util.checkNullStrint(view.getTypeOfUsage()));
                collateralDecisionReport.setMortgageCondition(Util.checkNullStrint(view.getMortgageCondition()));
                collateralDecisionReport.setMortgageConditionDetail(Util.checkNullStrint(view.getMortgageConditionDetail()));

                if (Util.safetyList(view.getProposeCreditDetailViewList()).size() > 0) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditDetailViewList());
                    collateralDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditDetailViewList());
                }

                collateralHeadViewList = view.getNewCollateralHeadViewList();
                if (Util.safetyList(collateralHeadViewList).size() > 0){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                    for (NewCollateralHeadView headView : collateralHeadViewList){
                        collateralDecisionReport.setCollateralDescription(Util.checkNullStrint(headView.getPotentialCollateral().getDescription()));
                        collateralDecisionReport.setPercentLTVDescription(Util.checkNullStrint(headView.getCollTypePercentLTV().getDescription()));
                        collateralDecisionReport.setExistingCredit(Util.converNullToZERO(headView.getExistingCredit()));
                        collateralDecisionReport.setTitleDeed(Util.checkNullStrint(headView.getTitleDeed()));
                        collateralDecisionReport.setCollateralLocation(Util.checkNullStrint(headView.getCollateralLocation()));
                        collateralDecisionReport.setAppraisalValue(Util.converNullToZERO(headView.getAppraisalValue()));
                        collateralDecisionReport.setHeadCollTypeDescription(Util.checkNullStrint(headView.getHeadCollType().getDescription()));
                        if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                            collateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
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
                    for (NewCollateralHeadView headView : view.getNewCollateralHeadViewList()){
                        collateralDecisionReport.setSubViewList(Util.safetyList(headView.getNewCollateralSubViewList()));
                    }
                } else {
                    log.debug("getNewCollateralHeadViewList is Null. {}",view.getNewCollateralHeadViewList());
                    collateralDecisionReport.setSubViewList(collateralDecisionReport.getSubViewList());
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

    public List<ApprovedCollateralDecisionReport> fillApprovedCollaterral(){
        init();
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<NewCollateralView> newCollateralViews = decisionView.getApproveCollateralList();
        List<NewCollateralHeadView> collateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        if (Util.safetyList(newCollateralViews).size() > 0){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (NewCollateralView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                approvedCollateralDecisionReport.setJobID(Util.checkNullStrint(view.getJobID()));
                approvedCollateralDecisionReport.setAppraisalDate(view.getAppraisalDate());
                approvedCollateralDecisionReport.setAadDecision(Util.checkNullStrint(view.getAadDecision()));
                approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullStrint(view.getAadDecisionReason()));
                approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullStrint(view.getAadDecisionReasonDetail()));
                approvedCollateralDecisionReport.setUsage(Util.checkNullStrint(view.getUsage()));
                approvedCollateralDecisionReport.setTypeOfUsage(Util.checkNullStrint(view.getTypeOfUsage()));
                approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullStrint(view.getMortgageCondition()));
                approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullStrint(view.getMortgageConditionDetail()));

                if (Util.safetyList(view.getProposeCreditDetailViewList()).size() > 0) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditDetailViewList());
                    approvedCollateralDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditDetailViewList());
                }

                collateralHeadViewList = view.getNewCollateralHeadViewList();
                if (Util.safetyList(collateralHeadViewList).size() > 0){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                    for (NewCollateralHeadView headView : collateralHeadViewList){
                        approvedCollateralDecisionReport.setCollateralDescription(Util.checkNullStrint(headView.getPotentialCollateral().getDescription()));
                        approvedCollateralDecisionReport.setPercentLTVDescription(Util.checkNullStrint(headView.getCollTypePercentLTV().getDescription()));
                        approvedCollateralDecisionReport.setExistingCredit(Util.converNullToZERO(headView.getExistingCredit()));
                        approvedCollateralDecisionReport.setTitleDeed(Util.checkNullStrint(headView.getTitleDeed()));
                        approvedCollateralDecisionReport.setCollateralLocation(Util.checkNullStrint(headView.getCollateralLocation()));
                        approvedCollateralDecisionReport.setAppraisalValue(Util.converNullToZERO(headView.getAppraisalValue()));
                        approvedCollateralDecisionReport.setHeadCollTypeDescription(Util.checkNullStrint(headView.getHeadCollType().getDescription()));
                        if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                            approvedCollateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                            approvedCollateralDecisionReport.setInsuranceCompany("Non Partner");
                        } else {
                            approvedCollateralDecisionReport.setInsuranceCompany("");
                        }
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}",collateralHeadViewList);
                }

                if (Util.safetyList(view.getNewCollateralHeadViewList()).size() > 0) {
                    log.debug("getNewCollateralHeadViewList. {}",view.getNewCollateralHeadViewList());
                    for (NewCollateralHeadView headView : view.getNewCollateralHeadViewList()){
                        approvedCollateralDecisionReport.setSubViewList(Util.safetyList(headView.getNewCollateralSubViewList()));
                    }
                } else {
                    log.debug("getNewCollateralHeadViewList is Null. {}",view.getNewCollateralHeadViewList());
                    approvedCollateralDecisionReport.setSubViewList(approvedCollateralDecisionReport.getSubViewList());
                }
                approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            }

        } else {
            ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
            approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            log.debug("newCollateralViews is Null by fillProposedCollateral. {}",newCollateralViews);
        }
        return approvedCollateralDecisionReportArrayList;
    }

    public List<ProposedGuarantorDecisionReport> fillProposedGuarantor(){
        init();
        List<ProposedGuarantorDecisionReport> proposedGuarantorDecisionReportList = new ArrayList<ProposedGuarantorDecisionReport>();
        List<NewGuarantorDetailView> detailViews = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(detailViews).size() > 0){
            log.debug("detailViews by fillProposedGuarantor. {}",detailViews);
            for (NewGuarantorDetailView view : detailViews){
                ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
                guarantorDecisionReport.setCount(count++);
                guarantorDecisionReport.setName(Util.checkNullStrint(view.getGuarantorName().getTitleTh().getTitleTh()+view.getGuarantorName().getFirstNameTh()+" "+view.getGuarantorName().getLastNameTh()));
                guarantorDecisionReport.setTcgLgNo(Util.checkNullStrint(view.getTcgLgNo()));
                guarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                guarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.converNullToZERO(view.getTotalLimitGuaranteeAmount()));
                proposedGuarantorDecisionReportList.add(guarantorDecisionReport);
            }
        } else {
            log.debug("detailViews is Null by fillProposedGuarantor. {}",detailViews);
            ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
            proposedGuarantorDecisionReportList.add(guarantorDecisionReport);
        }
        return proposedGuarantorDecisionReportList;
    }

    public List<ApprovedGuarantorDecisionReport> fillApprovedCollateral(){
        init();
        List<ApprovedGuarantorDecisionReport> approvedGuarantorDecisionReportList = new ArrayList<ApprovedGuarantorDecisionReport>();
        List<NewGuarantorDetailView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(newGuarantorDetails).size() > 0){
            log.debug("newGuarantorDetails by fillApprovedCollateral. {}",newGuarantorDetails);
            for (NewGuarantorDetailView view : newGuarantorDetails){
                ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
                approvedGuarantorDecisionReport.setCount(count++);
                approvedGuarantorDecisionReport.setName(Util.checkNullStrint(view.getGuarantorName().getTitleTh().getTitleTh()+view.getGuarantorName().getFirstNameTh()+" "+view.getGuarantorName().getLastNameTh()));
                approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullStrint(view.getTcgLgNo()));
                approvedGuarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.converNullToZERO(view.getTotalLimitGuaranteeAmount()));
                if (view.getUwDecision().equals("APPROVED")){
                    approvedGuarantorDecisionReport.setUwDecision("Approved");
                } else if (view.getUwDecision().equals("REJECTED")){
                    approvedGuarantorDecisionReport.setUwDecision("Rejected");
                } else {
                    approvedGuarantorDecisionReport.setUwDecision("");
                }
                approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
            }
        } else {
            log.debug("newGuarantorDetails is Null by fillApprovedCollateral. {}",newGuarantorDetails);
            ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
            approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
        }

        return approvedGuarantorDecisionReportList;
    }

    public List<FollowUpConditionDecisionReport> fillFollowUpCondition(){
        init();
        List<FollowUpConditionDecisionReport> followUpConditionDecisionReportList = new ArrayList<FollowUpConditionDecisionReport>();
        List<DecisionFollowConditionView> decisionFollowConditionViews = decisionView.getDecisionFollowConditionViewList();

        int count = 1;
        if (Util.safetyList(decisionFollowConditionViews).size() > 0){
            log.debug("decisionFollowConditionViews by fillFollowUpCondition. {}",decisionFollowConditionViews);
            for (DecisionFollowConditionView view : decisionFollowConditionViews){
                FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();
                followUpConditionDecisionReport.setCount(count++);
                followUpConditionDecisionReport.setConditionView(Util.checkNullStrint(view.getConditionView().getName()));
                followUpConditionDecisionReport.setDetail(Util.checkNullStrint(view.getDetail()));
                followUpConditionDecisionReport.setFollowDate(view.getFollowDate());
                followUpConditionDecisionReportList.add(followUpConditionDecisionReport);
            }
        } else {
            log.debug("decisionFollowConditionViews is Null by fillFollowUpCondition. {}",decisionFollowConditionViews);
            FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();
            followUpConditionDecisionReportList.add(followUpConditionDecisionReport);
        }
        return followUpConditionDecisionReportList;
    }

    public List<ApprovalHistoryDecisionReport> fillApprovalHistory(){
        init();
        List<ApprovalHistoryDecisionReport> approvalHistoryDecisionReportArrayList = new ArrayList<ApprovalHistoryDecisionReport>();
        List<ApprovalHistoryView> approvalHistoryViews = decisionView.getApprovalHistoryList();

        int count = 1;
        if (Util.safetyList(approvalHistoryViews).size() > 0){
            log.debug("approvalHistoryViews by fillApprovalHistory. {}",approvalHistoryViews);
            for (ApprovalHistoryView view : approvalHistoryViews){
                ApprovalHistoryDecisionReport approvalHistoryDecisionReport = new ApprovalHistoryDecisionReport();
                approvalHistoryDecisionReport.setCount(count++);
                approvalHistoryDecisionReport.setDescription(Util.checkNullStrint(view.getStepView().getDescription()));
                approvalHistoryDecisionReport.setUserName(Util.checkNullStrint(view.getUserView().getUserName()));
                approvalHistoryDecisionReport.setRoleDescription(Util.checkNullStrint(view.getUserView().getRoleDescription()));
                approvalHistoryDecisionReport.setTitleName(Util.checkNullStrint(view.getUserView().getTitleName()));
                approvalHistoryDecisionReport.setSubmitDate(view.getSubmitDate());
                approvalHistoryDecisionReport.setComments(Util.checkNullStrint(view.getComments()));
                approvalHistoryDecisionReportArrayList.add(approvalHistoryDecisionReport);
            }
        } else {
            log.debug("approvalHistoryViews is Null by fillApprovalHistory. {}",approvalHistoryViews);
            ApprovalHistoryDecisionReport approvalHistoryDecisionReport = new ApprovalHistoryDecisionReport();
            approvalHistoryDecisionReportArrayList.add(approvalHistoryDecisionReport);
        }

        return approvalHistoryDecisionReportArrayList;
    }
}
