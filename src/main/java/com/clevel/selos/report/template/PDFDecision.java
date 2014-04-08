package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.controller.Decision;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.working.NewGuarantorDetail;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PDFDecision implements Serializable {
    @NormalMessage
    @Inject
    Message msg;

    @Inject
    @SELOS
    Logger log;

    @Inject
    DecisionControl decisionControl;

    @Inject
    DecisionView decisionView;


    private List<NewCreditDetailView> newCreditDetailViewList;


    long workCaseId;



    public PDFDecision() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.debug("onCreation ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }

        decisionView = new DecisionView();

        if(!Util.isNull(workCaseId)){
            decisionView = decisionControl.getDecisionView(workCaseId);
            log.debug("--decisionView. {}",decisionView);
        } else {
            log.debug("--workcaseId is Null. {}",workCaseId);
        }
    }

    public List<BorrowerCreditDecisionReport> fillCreditBorrower(String pathsub){
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
                decisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                decisionReport.setAccount(account.toString());
                log.debug("accountLable by creditBorrower. {}",account.toString());

                decisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                decisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                decisionReport.setCode(code.toString());
                log.debug("codeLable by creditBorrower. {}", code.toString());

                decisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                decisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                decisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());

                borrowerCreditDecisionReportList.add(decisionReport);
            }
        } else {
            BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
            decisionReport.setPath(pathsub);
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
                conditionDecisionReport.setLoanType(Util.checkNullString(view.getLoanType()));
                conditionDecisionReport.setConditionDesc(Util.checkNullString(view.getConditionDesc()));
                conditionDecisionReportList.add(conditionDecisionReport);
            }
        } else {
            ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
            conditionDecisionReportList.add(conditionDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillCondition. {}",existingConditionDetailViews);
        }
        return conditionDecisionReportList;
    }

    public List<BorrowerRetailDecisionReport> fillBorrowerRetail(String pathsub){
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
                borrowerRetailDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                borrowerRetailDecisionReport.setAccount(account.toString());
                log.debug("accountLable by borrowerRetail. {}",account.toString());

                borrowerRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                borrowerRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));
                borrowerRetailDecisionReport.setCode(code.toString());
                log.debug("codeLable by borrowerRetail. {}",code.toString());

                borrowerRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                borrowerRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                borrowerRetailDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());
                retailDecisionReportList.add(borrowerRetailDecisionReport);
            }
        } else {
            BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
            borrowerRetailDecisionReport.setPath(pathsub);
            retailDecisionReportList.add(borrowerRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillBorrowerRetail. {}",existingConditionDetailViews);
        }
        return retailDecisionReportList;
    }

    public List<BorrowerAppInRLOSDecisionReport> fillAppInRLOS(String pathsub){
        init();
        List<BorrowerAppInRLOSDecisionReport> borrowerAppInRLOSDecisionReportList = new ArrayList<BorrowerAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerAppInRLOSList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillAppInRLOS. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
                borrowerAppInRLOSDecisionReport.setCount(count++);
                borrowerAppInRLOSDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                borrowerAppInRLOSDecisionReport.setAccount(account.toString());
                log.debug("account by borrowerAppInRLOSDecisionReport. {}",account.toString());

                borrowerAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                borrowerAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                borrowerAppInRLOSDecisionReport.setCode(code.toString());
                log.debug("codeLable by borrowerAppInRLOSDecisionReport. {}",code.toString());

                borrowerAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                borrowerAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                borrowerAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
            }
        } else {
            BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
            borrowerAppInRLOSDecisionReport.setPath(pathsub);
            borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillAppInRLOS. {}",existingConditionDetailViews);
        }
        return borrowerAppInRLOSDecisionReportList;
    }

    public List<RelatedCommercialDecisionReport> fillRelatedCommercial(String pathsub){
        List<RelatedCommercialDecisionReport> relatedCommercialDecisionReportList = new ArrayList<RelatedCommercialDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedComCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedCommercial. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
                relatedCommercialDecisionReport.setCount(count++);
                relatedCommercialDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                relatedCommercialDecisionReport.setAccount(account.toString());
                log.debug("account by relatedCommercialDecisionReport. {}",account.toString());

                relatedCommercialDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedCommercialDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedCommercialDecisionReport.setCode(code.toString());
                log.debug("codeLable by relatedCommercialDecisionReport. {}",code.toString());

                relatedCommercialDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedCommercialDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                relatedCommercialDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            }
        } else {
            RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
            relatedCommercialDecisionReport.setPath(pathsub);
            relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedCommercial. {}",existingConditionDetailViews);
        }

        return relatedCommercialDecisionReportList;
    }

    public List<RelatedRetailDecisionReport> fillRelatedRetail(String pathsub){
        List<RelatedRetailDecisionReport> relatedRetailDecisionReportList = new ArrayList<RelatedRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedRetailCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedRetail. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
                relatedRetailDecisionReport.setCount(count++);
                relatedRetailDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                relatedRetailDecisionReport.setAccount(account.toString());
                log.debug("account by relatedRetailDecisionReport. {}",account.toString());

                relatedRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedRetailDecisionReport.setCode(code.toString());
                log.debug("codeLable by relatedRetailDecisionReport. {}",code.toString());

                relatedRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                relatedRetailDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            }
        } else {
            RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
            relatedRetailDecisionReport.setPath(pathsub);
            relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedRetail. {}",existingConditionDetailViews);
        }

        return relatedRetailDecisionReportList;
    }

    public List<RelatedAppInRLOSDecisionReport> fillRelatedAppInRLOS(String pathsub){
        List<RelatedAppInRLOSDecisionReport> relatedAppInRLOSDecisionReportArrayList = new ArrayList<RelatedAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedAppInRLOSList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : decisionView.getExtRelatedAppInRLOSList()){
                RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
                relatedAppInRLOSDecisionReport.setCount(count++);
                relatedAppInRLOSDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                relatedAppInRLOSDecisionReport.setAccount(account.toString());
                log.debug("account by relatedAppInRLOSDecisionReport. {}",account.toString());

                relatedAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedAppInRLOSDecisionReport.setCode(code.toString());
                log.debug("codeLable by relatedAppInRLOSDecisionReport. {}",code.toString());

                relatedAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            }
        } else {
            RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
            relatedAppInRLOSDecisionReport.setPath(pathsub);
            relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
        }

        return relatedAppInRLOSDecisionReportArrayList;
    }

    public List<ExistingCollateralBorrowerDecisionReport> fillExistingCollateralBorrower(String path) throws UnsupportedEncodingException {
        init();
        List<ExistingCollateralBorrowerDecisionReport> collateralBorrowerDecisionReportList = new ArrayList<ExistingCollateralBorrowerDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtBorrowerCollateralList();

        int count = 1;
        if (Util.safetyList(conditionDetailViews).size() > 0){
            log.debug("conditionDetailViews by fillExistingCollateralBorrower. {}",conditionDetailViews);
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
                collateralBorrowerDecisionReport.setCount(count++);
                collateralBorrowerDecisionReport.setPath(path);

                StringBuilder collateralType = new StringBuilder();
                try {
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((Util.checkNullString(detailView.getPotentialCollateral().getDescription()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((Util.checkNullString(detailView.getCollateralType().getDescription()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((Util.checkNullString(detailView.getRelation().getDescription()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append((detailView.getAppraisalDate()) == null ? "" : detailView.getAppraisalDate()).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark())));
                } catch (Exception e){
                    log.debug("Exception while convert BYTE to UTF-8");
                }


                collateralBorrowerDecisionReport.setCollateralType(collateralType.toString());
                log.debug("--CollateralType. {}",collateralType.toString());

                collateralBorrowerDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));
                collateralBorrowerDecisionReport.setExistingCreditTypeDetailViews(Util.safetyList(detailView.getExistingCreditTypeDetailViewList()));
                collateralBorrowerDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralBorrowerDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralBorrowerDecisionReport.setMortgageType(Util.checkNullString(detailView.getMortgageType().getMortgage()));
                collateralBorrowerDecisionReport.setAppraisalValue(Util.convertNullToZERO(detailView.getAppraisalValue()));
                collateralBorrowerDecisionReport.setMortgageValue(Util.convertNullToZERO(detailView.getMortgageValue()));
                collateralBorrowerDecisionReportList.add(collateralBorrowerDecisionReport);
            }
        } else {
            ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
            collateralBorrowerDecisionReport.setPath(path);
            collateralBorrowerDecisionReportList.add(collateralBorrowerDecisionReport);
            log.debug("conditionDetailViews is Null by fillExistingCollateralBorrower. {}",conditionDetailViews);
        }

        return collateralBorrowerDecisionReportList;
    }

    public List<ExistingCollateralRelatedDecisionReport> fillExistingCollateralRelated(String path){
        init();
        List<ExistingCollateralRelatedDecisionReport> collateralRelatedDecisionReportArrayList = new ArrayList<ExistingCollateralRelatedDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtRelatedCollateralList();

        int count = 1;
        if (Util.safetyList(conditionDetailViews).size() > 0){
            log.debug("conditionDetailViews by fillExistingCollateralRelated. {}",conditionDetailViews);
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralRelatedDecisionReport collateralRelatedDecisionReport = new ExistingCollateralRelatedDecisionReport();
                collateralRelatedDecisionReport.setCount(count++);
                collateralRelatedDecisionReport.setPath(path);

                StringBuilder collateralType = new StringBuilder();
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((Util.checkNullString(detailView.getPotentialCollateral().getDescription()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((Util.checkNullString(detailView.getCollateralType().getDescription()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((Util.checkNullString(detailView.getRelation().getDescription()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append((detailView.getAppraisalDate()) == null ? "" : detailView.getAppraisalDate()).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark())));

                collateralRelatedDecisionReport.setCollateralType(collateralType.toString());
                log.debug("--CollateralType. {}",collateralType.toString());

                collateralRelatedDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));
                collateralRelatedDecisionReport.setExistingCreditTypeDetailViews(Util.safetyList(detailView.getExistingCreditTypeDetailViewList()));
                collateralRelatedDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralRelatedDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralRelatedDecisionReport.setMortgageType(Util.checkNullString(detailView.getMortgageType().getMortgage()));
                collateralRelatedDecisionReport.setAppraisalValue(Util.convertNullToZERO(detailView.getAppraisalValue()));
                collateralRelatedDecisionReport.setMortgageValue(Util.convertNullToZERO(detailView.getMortgageValue()));
                collateralRelatedDecisionReportArrayList.add(collateralRelatedDecisionReport);
            }
        } else {
            ExistingCollateralRelatedDecisionReport collateralRelatedDecisionReport = new ExistingCollateralRelatedDecisionReport();
            collateralRelatedDecisionReport.setPath(path);
            collateralRelatedDecisionReportArrayList.add(collateralRelatedDecisionReport);
            log.debug("conditionDetailViews is Null by fillExistingCollateralRelated. {}",conditionDetailViews);
        }

        return collateralRelatedDecisionReportArrayList;
    }

    public List<GuarantorBorrowerDecisionReport> fillGuarantorBorrower(String pathsub){
        init();
        List<GuarantorBorrowerDecisionReport> guarantorBorrowerDecisionReportList = new ArrayList<GuarantorBorrowerDecisionReport>();
        List<ExistingGuarantorDetailView> extGuarantorList = decisionView.getExtGuarantorList();
        int count = 1;
        if (Util.safetyList(extGuarantorList).size() > 0){
            log.debug("extGuarantorList by fillGuarantorBorrower. {}",extGuarantorList);
            for (ExistingGuarantorDetailView detailView : extGuarantorList){
                GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
                guarantorBorrowerDecisionReport.setCount(count++);
                guarantorBorrowerDecisionReport.setPath(pathsub);
                guarantorBorrowerDecisionReport.setGuarantorName(Util.checkNullString(detailView.getGuarantorName().getTitleTh().getTitleTh()+detailView.getGuarantorName().getFirstNameTh()+"  "+detailView.getGuarantorName().getLastNameTh()));
                guarantorBorrowerDecisionReport.setTcgLgNo(Util.checkNullString(detailView.getTcgLgNo()));
                guarantorBorrowerDecisionReport.setExistingCreditTypeDetailViewList(Util.safetyList(detailView.getExistingCreditTypeDetailViewList()));
                guarantorBorrowerDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(detailView.getTotalLimitGuaranteeAmount()));
                guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            }
        } else {
            GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
            guarantorBorrowerDecisionReport.setPath(pathsub);
            guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            log.debug("extGuarantorList is Null by fillGuarantorBorrower. {}",extGuarantorList);
        }

        return guarantorBorrowerDecisionReportList;
    }

    public List<ProposedCreditDecisionReport> fillProposedCredit(String pathsub){
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
                proposedView.setPath(pathsub);
                proposedView.setProdName(Util.checkNullString(detailView.getProductProgramView().getName()));

                if ((detailView.getUwDecision()) == DecisionType.APPROVED){
                    proposedView.setUwDecision("APPROVED");
                } else if (detailView.getUwDecision() == DecisionType.REJECTED){
                    proposedView.setUwDecision("REJECTED");
                } else {
                    proposedView.setUwDecision("-");
                }
                proposedView.setCredittypeName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                proposedView.setProdCode(Util.checkNullString(detailView.getProductCode()));
                proposedView.setProjectCode(Util.checkNullString(detailView.getProjectCode()));
                proposedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                proposedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                proposedView.setNewCreditTierDetailViews(Util.safetyList(detailView.getNewCreditTierDetailViewList()));

                StringBuilder builder = new StringBuilder();

                if (detailView.getRequestType() == RequestTypes.NEW.value()){
                    builder = builder.append("Request Type : New    ");
                } else if (detailView.getRequestType() == RequestTypes.CHANGE.value()){
                    builder = builder.append("Request Type : Change    ");
                }

                if (detailView.getRefinance() == RadioValue.YES.value()){
                    builder = builder.append("Refinance : Yes").append("\n");
                    proposedView.setRefinance("Yes");
                } else if (detailView.getRefinance() == RadioValue.NO.value()){
                    builder = builder.append("Refinance : No").append("\n");
                }

                if (!Util.isNull(detailView.getLoanPurposeView())){
                    builder = builder.append("Purpose : ").append(Util.checkNullString(detailView.getLoanPurposeView().getDescription())).append("\n");
                } else {
                    builder =builder.append("Purpose : ").append("\n");
                }

                builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getRemark())).append("\n");

                if (!Util.isNull(detailView.getDisbursementTypeView())){
                    builder = builder.append("Disbursement : ").append(Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement())).append("   ");
                } else {
                    builder = builder.append("Disbursement : ").append("   ");
                }
                builder = builder.append("Hold Amount : ").append(Util.convertNullToZERO(detailView.getHoldLimitAmount()));

                proposedView.setProposedDetail(builder.toString());
                log.debug("--ProposedDetail. {}",builder.toString());
                proposedCreditDecisionReportList.add(proposedView);
            }
        } else {
            ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
            proposedView.setPath(pathsub);
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
                proposeFeeInformationDecisionReport.setProductProgram(Util.checkNullString(view.getProductProgram()));

                StringBuilder standard = new StringBuilder();
                standard = standard.append(Util.convertNullToZERO(view.getStandardFrontEndFee().getFeeAmount())).append(" % ")
                        .append(Util.convertNullToZERO(view.getStandardFrontEndFee().getFeeYear())).append(" Year");
                proposeFeeInformationDecisionReport.setStandardFront(standard.toString());
                log.debug("--StandardFront. {}",standard.toString());

                StringBuilder commit = new StringBuilder();
                commit = commit.append(Util.convertNullToZERO(view.getCommitmentFee().getFeeAmount())).append(" % ")
                        .append(Util.convertNullToZERO(view.getCommitmentFee().getFeeYear())).append(" Year");
                proposeFeeInformationDecisionReport.setCommitmentFee(commit.toString());
                log.debug("--CommitmentFee. {}",commit.toString());

                StringBuilder extension = new StringBuilder();
                extension = extension.append(Util.convertNullToZERO(view.getExtensionFee().getFeeAmount())).append(" % ")
                        .append(Util.convertNullToZERO(view.getExtensionFee().getFeeYear())).append(" Year");
                proposeFeeInformationDecisionReport.setExtensionFee(extension.toString());
                log.debug("--ExtensionFee. {}",extension.toString());

                StringBuilder prepayment = new StringBuilder();
                prepayment = prepayment.append(Util.convertNullToZERO(view.getPrepaymentFee().getFeeAmount())).append(" % ")
                        .append(Util.convertNullToZERO(view.getPrepaymentFee().getFeeYear())).append(" Year");
                proposeFeeInformationDecisionReport.setPrepaymentFee(prepayment.toString());
                log.debug("--PrepaymentFee. {}",prepayment.toString());

                StringBuilder cancellation = new StringBuilder();
                cancellation = cancellation.append(Util.convertNullToZERO(view.getCancellationFee().getFeeAmount())).append(" % ")
                        .append(Util.convertNullToZERO(view.getCancellationFee().getFeeYear())).append(" Year");
                proposeFeeInformationDecisionReport.setCancellationFee(cancellation.toString());
                log.debug("--PrepaymentFee. {}",cancellation.toString());

                proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            }
        } else {
            ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
            proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            log.debug("feeDetailViewList is Null by fillProposeFeeInformation. {}",feeDetailViewList);
        }


        return proposeFeeInformationDecisionReportList;
    }

    public List<ProposedCollateralDecisionReport> fillProposedCollateral(String pathsub){
        init();
        List<ProposedCollateralDecisionReport> proposedCollateralDecisionReportList = new ArrayList<ProposedCollateralDecisionReport>();
        List<NewCollateralView> newCollateralViews = decisionView.getProposeCollateralList();
        List<NewCollateralHeadView> collateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        if (Util.safetyList(newCollateralViews).size() > 0){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (NewCollateralView view : newCollateralViews){
                ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
                collateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                collateralDecisionReport.setPath(pathsub);
                collateralDecisionReport.setAppraisalDate(view.getAppraisalDate());
                collateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                collateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                collateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                collateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                collateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                collateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                collateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));
                collateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                if (Util.safetyList(view.getProposeCreditDetailViewList()).size() > 0) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditDetailViewList());
                    collateralDecisionReport.setDetailViewList(view.getProposeCreditDetailViewList());
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditDetailViewList());
                }

                collateralHeadViewList = view.getNewCollateralHeadViewList();
                if (Util.safetyList(collateralHeadViewList).size() > 0){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                    for (NewCollateralHeadView headView : collateralHeadViewList){
                        collateralDecisionReport.setCollateralDescription(Util.checkNullString(headView.getPotentialCollateral().getDescription()));
                        collateralDecisionReport.setPercentLTVDescription(Util.checkNullString(headView.getCollTypePercentLTV().getDescription()));
                        collateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        collateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        collateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        collateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        collateralDecisionReport.setHeadCollTypeDescription(Util.checkNullString(headView.getHeadCollType().getDescription()));
                        if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                            collateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                            collateralDecisionReport.setInsuranceCompany("Non Partner");
                        } else {
                            collateralDecisionReport.setInsuranceCompany("");
                        }
                        collateralDecisionReport.setCollateralSubViewList(headView.getNewCollateralSubViewList());
                        log.debug("-------------------- {},+++++++++++{}",collateralDecisionReport.getCollateralSubViewList(),headView.getNewCollateralSubViewList());
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}",collateralHeadViewList);
                }

                proposedCollateralDecisionReportList.add(collateralDecisionReport);
            }

        } else {
            log.debug("newCollateralViews is Null by fillProposedCollateral. {}",newCollateralViews);
            ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
            collateralDecisionReport.setPath(pathsub);
            proposedCollateralDecisionReportList.add(collateralDecisionReport);
        }
        return proposedCollateralDecisionReportList;
    }

    public List<ApprovedCollateralDecisionReport> fillApprovedCollaterral(String pathsub){
        init();
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<NewCollateralView> newCollateralViews = decisionView.getApproveCollateralList();
        List<NewCollateralHeadView> collateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        if (Util.safetyList(newCollateralViews).size() > 0){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (NewCollateralView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                approvedCollateralDecisionReport.setPath(pathsub);
                approvedCollateralDecisionReport.setAppraisalDate(view.getAppraisalDate());
                approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                approvedCollateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                approvedCollateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));
//                approvedCollateralDecisionReport.setUwDecision(Util.checkNullString(view.getUwDecision().getValue()));
                if (view.getUwDecision().equals("APPROVED")){
                    approvedCollateralDecisionReport.setApproved("Approved");
                } else if(view.getUwDecision().equals("REJECTED")){
                    approvedCollateralDecisionReport.setApproved("Rejected");
                } else {
                    approvedCollateralDecisionReport.setApproved("");
                }

                approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));

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
                        approvedCollateralDecisionReport.setCollateralDescription(Util.checkNullString(headView.getPotentialCollateral().getDescription()));
                        approvedCollateralDecisionReport.setPercentLTVDescription(Util.checkNullString(headView.getCollTypePercentLTV().getDescription()));
                        approvedCollateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        approvedCollateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        approvedCollateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        approvedCollateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        approvedCollateralDecisionReport.setHeadCollTypeDescription(Util.checkNullString(headView.getHeadCollType().getDescription()));
                        if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                            approvedCollateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                            approvedCollateralDecisionReport.setInsuranceCompany("Non Partner");
                        } else {
                            approvedCollateralDecisionReport.setInsuranceCompany("");
                        }
                        approvedCollateralDecisionReport.setSubViewList(Util.safetyList(headView.getNewCollateralSubViewList()));
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}",collateralHeadViewList);
                }

                approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            }

        } else {
            ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
            approvedCollateralDecisionReport.setPath(pathsub);
            approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            log.debug("newCollateralViews is Null by fillProposedCollateral. {}",newCollateralViews);
        }
        return approvedCollateralDecisionReportArrayList;
    }

    public List<ProposedGuarantorDecisionReport> fillProposedGuarantor(String pathsub){
        init();
        List<ProposedGuarantorDecisionReport> proposedGuarantorDecisionReportList = new ArrayList<ProposedGuarantorDecisionReport>();
        List<NewGuarantorDetailView> detailViews = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(detailViews).size() > 0){
            log.debug("detailViews by fillProposedGuarantor. {}",detailViews);
            for (NewGuarantorDetailView view : detailViews){
                ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
                guarantorDecisionReport.setCount(count++);
                guarantorDecisionReport.setPath(pathsub);
                guarantorDecisionReport.setName(Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh()+view.getGuarantorName().getFirstNameTh()+" "+view.getGuarantorName().getLastNameTh()));
                guarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                guarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                guarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getTotalLimitGuaranteeAmount()));
                proposedGuarantorDecisionReportList.add(guarantorDecisionReport);
            }
        } else {
            log.debug("detailViews is Null by fillProposedGuarantor. {}",detailViews);
            ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
            guarantorDecisionReport.setPath(pathsub);
            proposedGuarantorDecisionReportList.add(guarantorDecisionReport);
        }
        return proposedGuarantorDecisionReportList;
    }

    public List<ApprovedGuarantorDecisionReport> fillApprovedGuarantor(String pathsub){
        init();
        List<ApprovedGuarantorDecisionReport> approvedGuarantorDecisionReportList = new ArrayList<ApprovedGuarantorDecisionReport>();
        List<NewGuarantorDetailView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(newGuarantorDetails).size() > 0){
            log.debug("newGuarantorDetails by fillApprovedGuarantor. {}",newGuarantorDetails);
            for (NewGuarantorDetailView view : newGuarantorDetails){
                ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
                approvedGuarantorDecisionReport.setCount(count++);
                approvedGuarantorDecisionReport.setPath(pathsub);
                approvedGuarantorDecisionReport.setName(Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh()+view.getGuarantorName().getFirstNameTh()+" "+view.getGuarantorName().getLastNameTh()));
                approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                approvedGuarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getTotalLimitGuaranteeAmount()));
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
            log.debug("newGuarantorDetails is Null by fillApprovedGuarantor. {}",newGuarantorDetails);
            ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
            approvedGuarantorDecisionReport.setPath(pathsub);
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
                followUpConditionDecisionReport.setConditionView(Util.checkNullString(view.getConditionView().getName()));
                followUpConditionDecisionReport.setDetail(Util.checkNullString(view.getDetail()));
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
                approvalHistoryDecisionReport.setDescription(Util.checkNullString(view.getStepView().getDescription()));
                approvalHistoryDecisionReport.setUserName(Util.checkNullString(view.getUserView().getUserName()));
                approvalHistoryDecisionReport.setRoleDescription(Util.checkNullString(view.getUserView().getRoleDescription()));
                approvalHistoryDecisionReport.setTitleName(Util.checkNullString(view.getUserView().getTitleName()));
                approvalHistoryDecisionReport.setSubmitDate(view.getSubmitDate());
                approvalHistoryDecisionReport.setComments(Util.checkNullString(view.getComments()));
                approvalHistoryDecisionReportArrayList.add(approvalHistoryDecisionReport);
            }
        } else {
            log.debug("approvalHistoryViews is Null by fillApprovalHistory. {}",approvalHistoryViews);
            ApprovalHistoryDecisionReport approvalHistoryDecisionReport = new ApprovalHistoryDecisionReport();
            approvalHistoryDecisionReportArrayList.add(approvalHistoryDecisionReport);
        }

        return approvalHistoryDecisionReportArrayList;
    }

    public TotalDecisionReport fillTotalMasterReport(){
        init();
        TotalDecisionReport totalDecisionReport = new TotalDecisionReport();

        //Existing Credit Borrower
        //Commercial Credit
        totalDecisionReport.setExtBorrowerTotalComLimit(Util.convertNullToZERO(decisionView.getExtBorrowerTotalComLimit()));
        //Retail Credit
        totalDecisionReport.setExtBorrowerTotalRetailLimit(Util.convertNullToZERO(decisionView.getExtBorrowerTotalRetailLimit()));
        //App in RLOS Process
        totalDecisionReport.setExtBorrowerTotalAppInRLOSLimit(Util.convertNullToZERO(decisionView.getExtBorrowerTotalAppInRLOSLimit()));
        //Borrower
        totalDecisionReport.setExtBorrowerTotalCommercial(Util.convertNullToZERO(decisionView.getExtBorrowerTotalCommercial()));
        totalDecisionReport.setExtBorrowerTotalComAndOBOD(Util.convertNullToZERO(decisionView.getExtBorrowerTotalComAndOBOD()));
        totalDecisionReport.setExtBorrowerTotalExposure(Util.convertNullToZERO(decisionView.getExtBorrowerTotalExposure()));

        //Existing Credit Related Person
        //Commercial Credit
        totalDecisionReport.setExtRelatedTotalComLimit(Util.convertNullToZERO(decisionView.getExtRelatedTotalComLimit()));
        //Retail Credit
        totalDecisionReport.setExtRelatedTotalRetailLimit(Util.convertNullToZERO(decisionView.getExtRelatedTotalRetailLimit()));
        //App in RLOS Process
        totalDecisionReport.setExtRelatedTotalAppInRLOSLimit(Util.convertNullToZERO(decisionView.getExtRelatedTotalAppInRLOSLimit()));

        //Total Related
        totalDecisionReport.setExtRelatedTotalCommercial(Util.convertNullToZERO(decisionView.getExtRelatedTotalCommercial()));
        totalDecisionReport.setExtRelatedTotalComAndOBOD(Util.convertNullToZERO(decisionView.getExtRelatedTotalComAndOBOD()));
        totalDecisionReport.setExtRelatedTotalExposure(Util.convertNullToZERO(decisionView.getExtRelatedTotalExposure()));

        //Total Group
        totalDecisionReport.setExtGroupTotalCommercial(Util.convertNullToZERO(decisionView.getExtGroupTotalCommercial()));
        totalDecisionReport.setExtGroupTotalComAndOBOD(Util.convertNullToZERO(decisionView.getExtGroupTotalComAndOBOD()));
        totalDecisionReport.setExtGroupTotalExposure(Util.convertNullToZERO(decisionView.getExtGroupTotalExposure()));

        //Existing Collateral Borrower
        totalDecisionReport.setExtBorrowerTotalAppraisalValue(Util.convertNullToZERO(decisionView.getExtBorrowerTotalAppraisalValue()));
        totalDecisionReport.setExtBorrowerTotalMortgageValue(Util.convertNullToZERO(decisionView.getExtBorrowerTotalMortgageValue()));

        //Existing Related Person
        totalDecisionReport.setExtRelatedTotalAppraisalValue(Util.convertNullToZERO(decisionView.getExtRelatedTotalAppraisalValue()));
        totalDecisionReport.setExtRelatedTotalMortgageValue(Util.convertNullToZERO(decisionView.getExtRelatedTotalMortgageValue()));

        //Existing Guarantor
        //Borrower
        totalDecisionReport.setExtTotalGuaranteeAmount(Util.convertNullToZERO(decisionView.getExtTotalGuaranteeAmount()));
        //Propose Credit
        totalDecisionReport.setProposeTotalCreditLimit(Util.convertNullToZERO(decisionView.getProposeTotalCreditLimit()));

        //Approved Propose Credit
        totalDecisionReport.setApproveTotalCreditLimit(Util.convertNullToZERO(decisionView.getApproveTotalCreditLimit()));
        if (decisionView.getCreditCustomerType() == CreditCustomerType.NORMAL){
            totalDecisionReport.setCreditCusType(1);
        } else if (decisionView.getCreditCustomerType() == CreditCustomerType.PRIME){
            totalDecisionReport.setCreditCusType(2);
        } else {
            totalDecisionReport.setCreditCusType(0);
        }
        totalDecisionReport.setCrdRequestTypeName(Util.checkNullString(decisionView.getLoanRequestType().getName()));
        totalDecisionReport.setCountryName(Util.checkNullString(decisionView.getInvestedCountry().getName()));
        totalDecisionReport.setExistingSMELimit(Util.convertNullToZERO(decisionView.getExistingSMELimit()));
        totalDecisionReport.setMaximumSMELimit(Util.convertNullToZERO(decisionView.getMaximumSMELimit()));

        //Total Borrower
        totalDecisionReport.setApproveBrwTotalCommercial(Util.convertNullToZERO(decisionView.getApproveBrwTotalCommercial()));
        totalDecisionReport.setApproveBrwTotalComAndOBOD(Util.convertNullToZERO(decisionView.getApproveBrwTotalComAndOBOD()));
        totalDecisionReport.setApproveTotalExposure(Util.convertNullToZERO(decisionView.getApproveTotalExposure()));

        //Proposed Guarantor
        totalDecisionReport.setProposeTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getProposeTotalGuaranteeAmt()));

        //Approved Guarantor
        totalDecisionReport.setApproveTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getApproveTotalGuaranteeAmt()));

        return totalDecisionReport;
    }

    public FollowUpConditionDecisionReport fillFollowDetail(){
        init();
        FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();

        for (DecisionFollowConditionView view : decisionView.getDecisionFollowConditionViewList()){
            followUpConditionDecisionReport.setConditionView(Util.checkNullString(view.getConditionView().getName()));
            followUpConditionDecisionReport.setDetail(Util.checkNullString(view.getDetail()));
            followUpConditionDecisionReport.setFollwDateDetaill(view.getFollowDate());
        }

        return followUpConditionDecisionReport;
    }

    public PriceFeeDecisionReport fillPriceFee(){
        init();
        PriceFeeDecisionReport priceFeeDecisionReport = new PriceFeeDecisionReport();
        priceFeeDecisionReport.setIntFeeDOA(Util.convertNullToZERO(decisionView.getIntFeeDOA()));
        priceFeeDecisionReport.setFrontendFeeDOA(Util.convertNullToZERO(decisionView.getFrontendFeeDOA()));
        priceFeeDecisionReport.setGuarantorBA(Util.convertNullToZERO(decisionView.getGuarantorBA()));
        priceFeeDecisionReport.setReasonForReduction(Util.checkNullString(decisionView.getReasonForReduction()));
        return priceFeeDecisionReport;
    }
}
