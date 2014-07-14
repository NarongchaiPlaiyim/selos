package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Inject
    private AppHeaderView appHeaderView;

    @Inject
    private WorkCaseDAO workCaseDAO;

    @Inject
    ExSummaryView exSummaryView;

    @Inject
    private ExSummaryControl exSummaryControl;

    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;

    @Inject
    private BizInfoSummaryView bizInfoSummaryView;

    WorkCase workCase;

    private List<ProposeCreditInfoDetailView> newCreditDetailViewList;


    long workCaseId;
    private final String SPACE = " ";



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
            decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);
            workCase = workCaseDAO.findById(workCaseId);
            exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);
            //Business Information
            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            log.debug("--decisionView. {},workCase. {},exsummary. {}",decisionView,workCase,exSummaryView);
        } else {
            log.debug("--workcaseId is Null. {}",workCaseId);
        }
    }

    public List<BorrowerCreditDecisionReport> fillCreditBorrower(String pathsub){
        log.debug("on fillCreditBorrower. {}");
//        init();
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
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append("");
                }


                decisionReport.setAccount(account.toString());
                log.debug("accountLable by creditBorrower. {}",account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    decisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    decisionReport.setProductProgramName("");
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())) {
                    decisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    decisionReport.setCreditTypeName("");
                }


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
                decisionReport.setExistingSplitLineDetailViewList(Util.safetyList(detailView.getExistingSplitLineDetailViewList()));
                log.debug("--ExistingSplitLineDetailViewList. {}",detailView.getExistingSplitLineDetailViewList());

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
//        init();
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
//        init();
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
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append("");
                }
                borrowerRetailDecisionReport.setAccount(account.toString());
                log.debug("accountLable by borrowerRetail. {}",account.toString());

                if(!Util.isNull(detailView.getExistProductProgramView())){
                    borrowerRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    borrowerRetailDecisionReport.setProductProgramName("");
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    borrowerRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    borrowerRetailDecisionReport.setCreditTypeName("");
                }


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

//    public List<BorrowerAppInRLOSDecisionReport> fillAppInRLOS(String pathsub){
////        init();
//        List<BorrowerAppInRLOSDecisionReport> borrowerAppInRLOSDecisionReportList = new ArrayList<BorrowerAppInRLOSDecisionReport>();
//        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerAppInRLOSList();
//
//        int count = 1;
//        if(Util.safetyList(existingConditionDetailViews).size() > 0){
//            log.debug("existingConditionDetailViews by fillAppInRLOS. {}",existingConditionDetailViews);
//            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
//                BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
//                borrowerAppInRLOSDecisionReport.setCount(count++);
//                borrowerAppInRLOSDecisionReport.setPath(pathsub);
//
//                StringBuilder account =new StringBuilder();
//                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
//                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
//                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
//                account = account.append("Acc Status: ");
//                if (!Util.isNull(detailView.getExistAccountStatusView())){
//                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
//                } else {
//                    account = account.append("");
//                }
//
//
//                borrowerAppInRLOSDecisionReport.setAccount(account.toString());
//                log.debug("account by borrowerAppInRLOSDecisionReport. {}",account.toString());
//
//                if (!Util.isNull(detailView.getExistProductProgramView())){
//                    borrowerAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
//                } else {
//                    borrowerAppInRLOSDecisionReport.setProductProgramName("");
//                }
//
//                if (!Util.isNull(detailView.getExistCreditTypeView())){
//                    borrowerAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
//                } else {
//                    borrowerAppInRLOSDecisionReport.setCreditTypeName("");
//                }
//
//                StringBuilder code =new StringBuilder();
//                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
//                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));
//
//                borrowerAppInRLOSDecisionReport.setCode(code.toString());
//                log.debug("codeLable by borrowerAppInRLOSDecisionReport. {}",code.toString());
//
//                borrowerAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
//                borrowerAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
//                borrowerAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
//                borrowerAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
//                borrowerAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
//                borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
//            }
//        } else {
//            BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
//            borrowerAppInRLOSDecisionReport.setPath(pathsub);
//            borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
//            log.debug("existingConditionDetailViews is Null by fillAppInRLOS. {}",existingConditionDetailViews);
//        }
//        return borrowerAppInRLOSDecisionReportList;
//    }

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
                account = account.append("Acc Status: ");

                if(!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append("");
                }

                relatedCommercialDecisionReport.setAccount(account.toString());
                log.debug("account by relatedCommercialDecisionReport. {}",account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    relatedCommercialDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    relatedCommercialDecisionReport.setProductProgramName("");
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    relatedCommercialDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    relatedCommercialDecisionReport.setCreditTypeName("");
                }


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
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());
                relatedCommercialDecisionReport.setExistingSplitLineDetailViewList(Util.safetyList(detailView.getExistingSplitLineDetailViewList()));
                log.debug("--ExistingSplitLineDetailViewList. {}",detailView.getExistingSplitLineDetailViewList());
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
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append("");
                }

                relatedRetailDecisionReport.setAccount(account.toString());
                log.debug("account by relatedRetailDecisionReport. {}",account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    relatedRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    relatedRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    relatedRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    relatedRetailDecisionReport.setCreditTypeName("");
                }


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
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());

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

//    public List<RelatedAppInRLOSDecisionReport> fillRelatedAppInRLOS(String pathsub){
//        List<RelatedAppInRLOSDecisionReport> relatedAppInRLOSDecisionReportArrayList = new ArrayList<RelatedAppInRLOSDecisionReport>();
//        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedAppInRLOSList();
//
//        int count = 1;
//        if(Util.safetyList(existingConditionDetailViews).size() > 0){
//            log.debug("existingConditionDetailViews by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
//            for (ExistingCreditDetailView detailView : decisionView.getExtRelatedAppInRLOSList()){
//                RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
//                relatedAppInRLOSDecisionReport.setCount(count++);
//                relatedAppInRLOSDecisionReport.setPath(pathsub);
//
//                StringBuilder account =new StringBuilder();
//                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
//                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
//                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
//                account = account.append("Acc Status: ");
//
//                if (!Util.isNull(detailView.getExistAccountStatusView())){
//                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
//                } else {
//                    account = account.append("");
//                }
//
//
//                relatedAppInRLOSDecisionReport.setAccount(account.toString());
//                log.debug("account by relatedAppInRLOSDecisionReport. {}",account.toString());
//
//                relatedAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
//                relatedAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
//
//                StringBuilder code =new StringBuilder();
//                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
//                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));
//
//                relatedAppInRLOSDecisionReport.setCode(code.toString());
//                log.debug("codeLable by relatedAppInRLOSDecisionReport. {}",code.toString());
//
//                relatedAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
//                relatedAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
//                relatedAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
//                relatedAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
//                relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
//                relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
//            }
//        } else {
//            RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
//            relatedAppInRLOSDecisionReport.setPath(pathsub);
//            relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
//            log.debug("existingConditionDetailViews is Null by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
//        }
//
//        return relatedAppInRLOSDecisionReportArrayList;
//    }

    public List<ExistingCollateralBorrowerDecisionReport> fillExistingCollateralBorrower(String path) throws UnsupportedEncodingException {
//        init();
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
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((Util.checkNullString(!Util.isNull(detailView.getPotentialCollateral()) ? detailView.getPotentialCollateral().getDescription() : ""))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((Util.checkNullString(!Util.isNull(detailView.getCollateralType()) ? detailView.getCollateralType().getDescription() : ""))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((Util.checkNullString(!Util.isNull(detailView.getRelation()) ? detailView.getRelation().getDescription() : ""))).append("\n");
                    collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append(DateTimeUtil.getCurrentDateTH((detailView.getAppraisalDate())) == null ? "" : DateTimeUtil.getCurrentDateTH(detailView.getAppraisalDate())).append("\n");
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
                collateralBorrowerDecisionReport.setMortgageType(Util.checkNullString(!Util.isNull(detailView.getMortgageType()) ? detailView.getMortgageType().getMortgage() : ""));
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
//        init();
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
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((Util.checkNullString(!Util.isNull(detailView.getPotentialCollateral()) ? detailView.getPotentialCollateral().getDescription() : ""))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((Util.checkNullString(!Util.isNull(detailView.getCollateralType()) ? detailView.getCollateralType().getDescription() : ""))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((Util.checkNullString(!Util.isNull(detailView.getRelation()) ? detailView.getRelation().getDescription() : ""))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append(DateTimeUtil.getCurrentDateTH((detailView.getAppraisalDate())) == null ? "" : DateTimeUtil.getCurrentDateTH(detailView.getAppraisalDate())).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark())));

                collateralRelatedDecisionReport.setCollateralType(collateralType.toString());
                log.debug("--CollateralType. {}",collateralType.toString());

                collateralRelatedDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));
                collateralRelatedDecisionReport.setExistingCreditTypeDetailViews(Util.safetyList(detailView.getExistingCreditTypeDetailViewList()));
                collateralRelatedDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralRelatedDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralRelatedDecisionReport.setMortgageType(Util.checkNullString(!Util.isNull(detailView.getMortgageType()) ? detailView.getMortgageType().getMortgage() : ""));
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
//        init();
        List<GuarantorBorrowerDecisionReport> guarantorBorrowerDecisionReportList = new ArrayList<GuarantorBorrowerDecisionReport>();
        List<ExistingGuarantorDetailView> extGuarantorList = decisionView.getExtGuarantorList();
        int count = 1;
        if (Util.safetyList(extGuarantorList).size() > 0){
            log.debug("extGuarantorList by fillGuarantorBorrower. {}",extGuarantorList);
            for (ExistingGuarantorDetailView detailView : extGuarantorList){
                GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
                guarantorBorrowerDecisionReport.setCount(count++);
                guarantorBorrowerDecisionReport.setPath(pathsub);

                guarantorBorrowerDecisionReport.setGuarantorName(Util.checkNullString(detailView.getGuarantorName().getTitleTh().getTitleTh() +
                        detailView.getGuarantorName().getFirstNameTh()+"  "+detailView.getGuarantorName().getLastNameTh()));

                StringBuilder name = new StringBuilder();
                name = name.append(Util.checkNullString(!Util.isNull(detailView.getGuarantorName()) ? !Util.isNull(detailView.getGuarantorName().getTitleTh()) ?
                        detailView.getGuarantorName().getTitleTh().getTitleTh() : "" : ""))
                        .append(!Util.isNull(detailView.getGuarantorName()) ? detailView.getGuarantorName().getFirstNameTh() : "")
                        .append(SPACE).append(!Util.isNull(detailView.getGuarantorName()) ? detailView.getGuarantorName().getLastNameTh() : "");
                guarantorBorrowerDecisionReport.setGuarantorName(name.toString());


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
//        init();
        newCreditDetailViewList = decisionView.getProposeCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        int count = 1;
        if (Util.safetyList(newCreditDetailViewList).size() > 0){
            log.debug("newCreditDetailViewList by fillProposedCredit. {}",newCreditDetailViewList);
            for (ProposeCreditInfoDetailView detailView : newCreditDetailViewList){
                ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
                proposedView.setPath(pathsub);
                proposedView.setCount(count++);

                proposedView.setProdName(Util.checkNullString(!Util.isNull(detailView.getProductProgramView()) ? detailView.getProductProgramView().getName() : ""));

                if (detailView.getUwDecision() == DecisionType.APPROVED){
                    proposedView.setUwDecision("APPROVED");
                }
//                    else if (detailView.getUwDecision() == DecisionType.REJECTED){
//                        proposedView.setUwDecision("REJECTED");
//                    } else {
//                        proposedView.setUwDecision("-");
//                    }
                proposedView.setCredittypeName(Util.checkNullString(!Util.isNull(detailView.getCreditTypeView()) ? detailView.getCreditTypeView().getName() : ""));
                proposedView.setProdCode(Util.checkNullString(detailView.getProductCode()));
                proposedView.setProjectCode(Util.checkNullString(detailView.getProjectCode()));
                proposedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                proposedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                proposedView.setNewCreditTierDetailViews(Util.safetyList(detailView.getProposeCreditInfoTierDetailViewList()));

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
                    builder = builder.append("Purpose : ").append(Util.checkNullString(!Util.isNull(detailView.getLoanPurposeView()) ? detailView.getLoanPurposeView().getDescription() : "")).append("\n");
                } else {
                    builder =builder.append("Purpose : ").append("\n");
                }

                builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append("\n");

                if (!Util.isNull(detailView.getDisbursementTypeView())){
                    builder = builder.append("Disbursement : ").append(Util.checkNullString(!Util.isNull(detailView.getDisbursementTypeView()) ? detailView.getDisbursementTypeView().getDisbursement() : ""))
                            .append(SPACE);
                } else {
                    builder = builder.append("Disbursement : ").append(SPACE);
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

    public List<ProposedCreditDecisionReport> fillApprovedCredit(String pathsub){
        log.debug("on fillProposedCredit. {}");
//        init();
        newCreditDetailViewList = decisionView.getApproveCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        int count = 1;
        if (Util.safetyList(newCreditDetailViewList).size() > 0){
            log.debug("newCreditDetailViewList by fillProposedCredit. {}",newCreditDetailViewList);
            for (ProposeCreditInfoDetailView detailView : newCreditDetailViewList){
                ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
                approvedView.setPath(pathsub);

                if (detailView.getUwDecision() == DecisionType.APPROVED){
                    log.debug("fillProposedCredit to APPROVED. {}",detailView.getUwDecision());
                    approvedView.setCount(count++);

                    approvedView.setProdName(Util.checkNullString(!Util.isNull(detailView.getProductProgramView()) ? detailView.getProductProgramView().getName() : ""));

                    if (detailView.getUwDecision() == DecisionType.APPROVED){
                        approvedView.setUwDecision("APPROVED");
                    }

                    approvedView.setCredittypeName(Util.checkNullString(!Util.isNull(detailView.getCreditTypeView()) ? detailView.getCreditTypeView().getName() : ""));
                    approvedView.setProdCode(Util.checkNullString(detailView.getProductCode()));
                    approvedView.setProjectCode(Util.checkNullString(detailView.getProjectCode()));
                    approvedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                    approvedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                    approvedView.setNewCreditTierDetailViews(Util.safetyList(detailView.getProposeCreditInfoTierDetailViewList()));

                    StringBuilder builder = new StringBuilder();

                    if (detailView.getRequestType() == RequestTypes.NEW.value()){
                        builder = builder.append("Request Type : New    ");
                    } else if (detailView.getRequestType() == RequestTypes.CHANGE.value()){
                        builder = builder.append("Request Type : Change    ");
                    }

                    if (detailView.getRefinance() == RadioValue.YES.value()){
                        builder = builder.append("Refinance : Yes").append("\n");
                        approvedView.setRefinance("Yes");
                    } else if (detailView.getRefinance() == RadioValue.NO.value()){
                        builder = builder.append("Refinance : No").append("\n");
                    }

                    if (!Util.isNull(detailView.getLoanPurposeView())){
                        builder = builder.append("Purpose : ").append(Util.checkNullString(!Util.isNull(detailView.getLoanPurposeView()) ? detailView.getLoanPurposeView().getDescription() : "")).append("\n");
                    } else {
                        builder =builder.append("Purpose : ").append("\n");
                    }

                    builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append("\n");

                    if (!Util.isNull(detailView.getDisbursementTypeView())){
                        builder = builder.append("Disbursement : ").append(Util.checkNullString(!Util.isNull(detailView.getDisbursementTypeView()) ? detailView.getDisbursementTypeView().getDisbursement() : ""))
                                .append(SPACE);
                    } else {
                        builder = builder.append("Disbursement : ").append(SPACE);
                    }
                    builder = builder.append("Hold Amount : ").append(Util.convertNullToZERO(detailView.getHoldLimitAmount()));

                    approvedView.setProposedDetail(builder.toString());
                    log.debug("--ApprovedView. {}",builder.toString());
                    proposedCreditDecisionReportList.add(approvedView);
                } else {
                    proposedCreditDecisionReportList.add(approvedView);
                    log.debug("fillApprovedCredit to REJECTED. {}",detailView.getUwDecision());
                }
            }
        } else {
            ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
            approvedView.setPath(pathsub);
            proposedCreditDecisionReportList.add(approvedView);
            log.debug("newCreditDetailViewList is Null by fillProposedCredit. {}",newCreditDetailViewList);
        }
        return proposedCreditDecisionReportList;
    }

    public List<ProposeFeeInformationDecisionReport> fillProposeFeeInformation(){
//        init();
        List<ProposeFeeInformationDecisionReport> proposeFeeInformationDecisionReportList = new ArrayList<ProposeFeeInformationDecisionReport>();
        List<ProposeFeeDetailView> feeDetailViewList = decisionView.getApproveFeeDetailViewList();

        int count = 1;
        if (Util.safetyList(feeDetailViewList).size() > 0){
            log.debug("feeDetailViewList by fillProposeFeeInformation. {}",feeDetailViewList);
            for (ProposeFeeDetailView view : feeDetailViewList){
                ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
                proposeFeeInformationDecisionReport.setCount(count++);
                proposeFeeInformationDecisionReport.setProductProgram(Util.checkNullString(view.getProductProgram()));

                StringBuilder standard = new StringBuilder();

                if (!Util.isNull(view.getStandardFrontEndFee())){
                    standard = standard.append(Util.convertNullToZERO(view.getStandardFrontEndFee().getFeeAmount())).append(" % ")
                            .append(Util.convertNullToZERO(view.getStandardFrontEndFee().getFeeYear())).append(" Year");
                    proposeFeeInformationDecisionReport.setStandardFront(standard.toString());
                    log.debug("--StandardFront. {}",standard.toString());
                } else {
                    proposeFeeInformationDecisionReport.setStandardFront("0% Year");
                }

                if (!Util.isNull(view.getCommitmentFee())){
                    StringBuilder commit = new StringBuilder();
                    commit = commit.append(Util.convertNullToZERO(view.getCommitmentFee().getFeeAmount())).append(" % ")
                            .append(Util.convertNullToZERO(view.getCommitmentFee().getFeeYear())).append(" Year");
                    proposeFeeInformationDecisionReport.setCommitmentFee(commit.toString());
                    log.debug("--CommitmentFee. {}",commit.toString());
                } else {
                    proposeFeeInformationDecisionReport.setCommitmentFee("0% Year");
                }

                if (!Util.isNull(view.getExtensionFee())){
                    StringBuilder extension = new StringBuilder();
                    extension = extension.append(Util.convertNullToZERO(view.getExtensionFee().getFeeAmount())).append(" % ")
                            .append(Util.convertNullToZERO(view.getExtensionFee().getFeeYear())).append(" Year");
                    proposeFeeInformationDecisionReport.setExtensionFee(extension.toString());
                    log.debug("--ExtensionFee. {}",extension.toString());
                } else {
                    proposeFeeInformationDecisionReport.setExtensionFee("0% Year");
                }

                if (!Util.isNull(view.getPrepaymentFee())){
                    StringBuilder prepayment = new StringBuilder();
                    prepayment = prepayment.append(Util.convertNullToZERO(view.getPrepaymentFee().getFeeAmount())).append(" % ")
                            .append(Util.convertNullToZERO(view.getPrepaymentFee().getFeeYear())).append(" Year");
                    proposeFeeInformationDecisionReport.setPrepaymentFee(prepayment.toString());
                    log.debug("--PrepaymentFee. {}",prepayment.toString());
                } else {
                    proposeFeeInformationDecisionReport.setPrepaymentFee("0% Year");
                }

                if (!Util.isNull(view.getCancellationFee())) {
                    StringBuilder cancellation = new StringBuilder();
                    cancellation = cancellation.append(Util.convertNullToZERO(view.getCancellationFee().getFeeAmount())).append(" % ")
                            .append(Util.convertNullToZERO(view.getCancellationFee().getFeeYear())).append(" Year");
                    proposeFeeInformationDecisionReport.setCancellationFee(cancellation.toString());
                    log.debug("--PrepaymentFee. {}",cancellation.toString());
                } else {
                    proposeFeeInformationDecisionReport.setCancellationFee("0% Year");
                }

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
//        init();
        List<ProposedCollateralDecisionReport> proposedCollateralDecisionReportList = new ArrayList<ProposedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getProposeCollateralList();
        List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();

        if (Util.safetyList(newCollateralViews).size() > 0){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (ProposeCollateralInfoView view : newCollateralViews){
                ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
                collateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                collateralDecisionReport.setPath(pathsub);
                collateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                collateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                collateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                collateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                collateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                collateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                collateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                collateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));
                collateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                if (Util.safetyList(view.getProposeCreditInfoDetailViewList()).size() > 0) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditInfoDetailViewList());
                    collateralDecisionReport.setDetailViewList(view.getProposeCreditInfoDetailViewList());
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditInfoDetailViewList());
                }

                collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                if (Util.safetyList(collateralHeadViewList).size() > 0){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                    for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                        collateralDecisionReport.setCollateralDescription(Util.checkNullString(!Util.isNull(headView.getPotentialCollateral()) ? headView.getPotentialCollateral().getDescription() : SPACE));
                        collateralDecisionReport.setPercentLTVDescription(Util.checkNullString(!Util.isNull(headView.getTcgCollateralType()) ? headView.getTcgCollateralType().getDescription() : SPACE));
                        collateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        collateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        collateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        collateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        collateralDecisionReport.setHeadCollTypeDescription(Util.checkNullString(!Util.isNull(headView.getHeadCollType()) ? headView.getHeadCollType().getDescription() : SPACE));
                        if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                            collateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                            collateralDecisionReport.setInsuranceCompany("Non Partner");
                        } else {
                            collateralDecisionReport.setInsuranceCompany("");
                        }
                        collateralDecisionReport.setCollateralSubViewList(headView.getProposeCollateralInfoSubViewList());
                        log.debug("-------------------- {},+++++++++++{}",collateralDecisionReport.getCollateralSubViewList(),headView.getProposeCollateralInfoSubViewList());
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

    public List<ApprovedCollateralDecisionReport> fillApprovedCollaterral(final String pathsub){
//        init();
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = Util.safetyList(decisionView.getApproveCollateralList());
        List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();

        if (!Util.isZero(newCollateralViews.size())){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (ProposeCollateralInfoView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                approvedCollateralDecisionReport.setPath(pathsub);
                log.debug("--Path. {}",pathsub);
                if("APPROVED".equals(view.getUwDecision())){
                    log.debug("fillApprovedCollaterral to APPROVED. {}",view.getUwDecision());

                    approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                    approvedCollateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                    approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                    approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                    approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                    approvedCollateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                    approvedCollateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                    approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));
//                approvedCollateralDecisionReport.setUwDecision(Util.checkNullString(view.getUwDecision().getValue()));
                    if ("APPROVED".equals(view.getUwDecision())){
                        approvedCollateralDecisionReport.setApproved("Approved");
                    }
//                else if(view.getUwDecision().equals("REJECTED")){
//                    approvedCollateralDecisionReport.setApproved("Rejected");
//                } else {
//                    approvedCollateralDecisionReport.setApproved("");
//                }

                    approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                    approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));

                    if (Util.safetyList(view.getProposeCreditInfoDetailViewList()).size() > 0) {
                        log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditInfoDetailViewList());
                        approvedCollateralDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditInfoDetailViewList()));
                    } else {
                        log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditInfoDetailViewList());
                    }

                    collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                    if (Util.safetyList(collateralHeadViewList).size() > 0){
                        log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                        for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                            approvedCollateralDecisionReport.setCollateralDescription(Util.checkNullString(!Util.isNull(headView.getPotentialCollateral()) ? headView.getPotentialCollateral().getDescription() : SPACE));
                            approvedCollateralDecisionReport.setPercentLTVDescription(Util.checkNullString(!Util.isNull(headView.getTcgCollateralType()) ? headView.getTcgCollateralType().getDescription() : SPACE));
                            approvedCollateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                            approvedCollateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                            approvedCollateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                            approvedCollateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                            approvedCollateralDecisionReport.setHeadCollTypeDescription(Util.checkNullString(!Util.isNull(headView.getHeadCollType()) ? headView.getHeadCollType().getDescription() : SPACE));
                            if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                approvedCollateralDecisionReport.setInsuranceCompany("Partner");
                            } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                approvedCollateralDecisionReport.setInsuranceCompany("Non Partner");
                            } else {
                                approvedCollateralDecisionReport.setInsuranceCompany("");
                            }
                            approvedCollateralDecisionReport.setSubViewList(Util.safetyList(headView.getProposeCollateralInfoSubViewList()));
                        }
                    } else {
                        log.debug("collateralHeadViewList is Null. {}",collateralHeadViewList);
                    }
                    approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
                } else {
                    approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
                    log.debug("fillApprovedCollaterral to Rejected. {}",view.getUwDecision());
                }
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
//        init();
        List<ProposedGuarantorDecisionReport> proposedGuarantorDecisionReportList = new ArrayList<ProposedGuarantorDecisionReport>();
        List<ProposeGuarantorInfoView> detailViews = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(detailViews).size() > 0){
            log.debug("detailViews by fillProposedGuarantor. {}",detailViews);
            for (ProposeGuarantorInfoView view : detailViews){
                ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
                guarantorDecisionReport.setCount(count++);
                guarantorDecisionReport.setPath(pathsub);

                StringBuffer name = new StringBuffer();
                name = name.append((Util.checkNullString(!Util.isNull(view.getGuarantorName()) ? !Util.isNull(view.getGuarantorName().getTitleTh()) ?
                        view.getGuarantorName().getTitleTh().getTitleTh() : "" : "")))
                        .append(Util.checkNullString(!Util.isNull(view.getGuarantorName()) ? view.getGuarantorName().getFirstNameTh() : ""))
                        .append(SPACE).append(Util.checkNullString(!Util.isNull(view.getGuarantorName()) ? view.getGuarantorName().getLastNameTh() : ""));

                guarantorDecisionReport.setName(name.toString());
                guarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                guarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditInfoDetailViewList()));
                guarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));
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
//        init();
        List<ApprovedGuarantorDecisionReport> approvedGuarantorDecisionReportList = new ArrayList<ApprovedGuarantorDecisionReport>();
        List<ProposeGuarantorInfoView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(newGuarantorDetails).size() > 0){
            log.debug("newGuarantorDetails by fillApprovedGuarantor. {}",newGuarantorDetails);
            for (ProposeGuarantorInfoView view : newGuarantorDetails){
                ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();

                approvedGuarantorDecisionReport.setPath(pathsub);
                if (view.getUwDecision().equals("APPROVED")) {
                    log.debug("fillApprovedGuarantor to APPROVED. {}",view.getUwDecision());

                    approvedGuarantorDecisionReport.setCount(count++);

                    StringBuffer name = new StringBuffer();
                    name = name.append(Util.checkNullString(!Util.isNull(view.getGuarantorName()) ? !Util.isNull(view.getGuarantorName().getTitleTh()) ? view.getGuarantorName().getTitleTh().getTitleTh()+view.getGuarantorName().getFirstNameTh() : SPACE : SPACE))
                            .append(SPACE).append(Util.checkNullString(!Util.isNull(view.getGuarantorName()) ? view.getGuarantorName().getLastNameTh() : SPACE ));

                    approvedGuarantorDecisionReport.setName(name.toString());
                    approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                    approvedGuarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditInfoDetailViewList()));
                    approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));
                    if (view.getUwDecision().equals("APPROVED")){
                        approvedGuarantorDecisionReport.setUwDecision("Approved");
                    }
//                    else if (view.getUwDecision().equals("REJECTED")){
//                        approvedGuarantorDecisionReport.setUwDecision("Rejected");
//                    } else {
//                        approvedGuarantorDecisionReport.setUwDecision("");
//                    }

                    if(Util.isNull(view.getGuaranteeAmount())){
                        approvedGuarantorDecisionReport.setGuarantorType(msg.get("report.lessamt"));
                    } else {
                        approvedGuarantorDecisionReport.setGuarantorType(msg.get("report.moreamt"));
                    }

                    approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
                } else {
                    approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
                    log.debug("fillApprovedGuarantor to REJECTED. {}",view.getUwDecision());
                }
            }
        } else {
            log.debug("newGuarantorDetails is Null by fillApprovedGuarantor. {}",newGuarantorDetails);
            ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
            approvedGuarantorDecisionReport.setPath(pathsub);
            approvedGuarantorDecisionReport.setProposeCreditDetailViewList(approvedGuarantorDecisionReport.getProposeCreditDetailViewList());
            approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
        }

        return approvedGuarantorDecisionReportList;
    }

    public List<FollowUpConditionDecisionReport> fillFollowUpCondition(){
//        init();
        List<FollowUpConditionDecisionReport> followUpConditionDecisionReportList = new ArrayList<FollowUpConditionDecisionReport>();
        List<DecisionFollowConditionView> decisionFollowConditionViews = decisionView.getDecisionFollowConditionViewList();

        int count = 1;
        if (Util.safetyList(decisionFollowConditionViews).size() > 0){
            log.debug("decisionFollowConditionViews by fillFollowUpCondition. {}",decisionFollowConditionViews);
            for (DecisionFollowConditionView view : decisionFollowConditionViews){
                FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();
                followUpConditionDecisionReport.setCount(count++);
                followUpConditionDecisionReport.setConditionView(Util.checkNullString(!Util.isNull(view.getConditionView()) ? view.getConditionView().getName() : SPACE));
                followUpConditionDecisionReport.setDetail(Util.checkNullString(view.getDetail()));
                followUpConditionDecisionReport.setFollowDate(DateTimeUtil.getCurrentDateTH(view.getFollowDate()));
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
//        init();
        List<ApprovalHistoryDecisionReport> approvalHistoryDecisionReportArrayList = new ArrayList<ApprovalHistoryDecisionReport>();
        List<ApprovalHistoryView> approvalHistoryViews = decisionView.getApprovalHistoryList();

        int count = 1;
        if (Util.safetyList(approvalHistoryViews).size() > 0){
            log.debug("approvalHistoryViews by fillApprovalHistory. {}",approvalHistoryViews);
            for (ApprovalHistoryView view : approvalHistoryViews){
                ApprovalHistoryDecisionReport approvalHistoryDecisionReport = new ApprovalHistoryDecisionReport();
                approvalHistoryDecisionReport.setCount(count++);
                approvalHistoryDecisionReport.setDescription(Util.checkNullString(!Util.isNull(view.getStepView()) ? view.getStepView().getDescription() : SPACE));
                approvalHistoryDecisionReport.setUserName(Util.checkNullString(!Util.isNull(view.getUserView()) ? view.getUserView().getUserName() : SPACE));
                approvalHistoryDecisionReport.setRoleDescription(Util.checkNullString(!Util.isNull(view.getUserView()) ? view.getUserView().getRoleDescription() : SPACE));
                approvalHistoryDecisionReport.setTitleName(Util.checkNullString(!Util.isNull(view.getUserView()) ? view.getUserView().getTitleName() : SPACE));
                approvalHistoryDecisionReport.setSubmitDate(DateTimeUtil.getCurrentDateTH(view.getSubmitDate()));
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
//        init();
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
//        totalDecisionReport.setCrdRequestTypeName(Util.checkNullString(decisionView.getLoanRequestType().getName()));
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
        List<ProposeGuarantorInfoView> newGuarantorDetails = Util.safetyList(decisionView.getApproveGuarantorList());

        for (ProposeGuarantorInfoView view : newGuarantorDetails){
            if (("APPROVED").equals(view.getUwDecision())) {
                totalDecisionReport.setApproveTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getApproveTotalGuaranteeAmt()));
            } else {
                totalDecisionReport.setApproveTotalGuaranteeAmt(BigDecimal.ZERO);
            }
        }

        //BizInfo Address
        if (!Util.isNull(bizInfoSummaryView)){
            totalDecisionReport.setBizLocationName(Util.checkNullString(bizInfoSummaryView.getBizLocationName()));
            totalDecisionReport.setRental(bizInfoSummaryView.getRental());
            totalDecisionReport.setOwnerName(Util.checkNullString(bizInfoSummaryView.getOwnerName()));
            totalDecisionReport.setExpiryDate(DateTimeUtil.getCurrentDateTH(bizInfoSummaryView.getExpiryDate()));
            totalDecisionReport.setAddressNo(Util.checkNullString(bizInfoSummaryView.getAddressNo()));
            totalDecisionReport.setAddressMoo(Util.checkNullString(bizInfoSummaryView.getAddressMoo()));
            totalDecisionReport.setAddressBuilding(Util.checkNullString(bizInfoSummaryView.getAddressBuilding()));
            totalDecisionReport.setAddressStreet(Util.checkNullString(bizInfoSummaryView.getAddressStreet()));
            totalDecisionReport.setProvinceName(Util.checkNullString(!Util.isNull(bizInfoSummaryView.getProvince()) ? bizInfoSummaryView.getProvince().getName() : SPACE));
            totalDecisionReport.setDistrictName(Util.checkNullString(!Util.isNull(bizInfoSummaryView.getDistrict()) ? bizInfoSummaryView.getDistrict().getName() : SPACE));
            totalDecisionReport.setSubDisName(Util.checkNullString(!Util.isNull(bizInfoSummaryView.getSubDistrict()) ? bizInfoSummaryView.getSubDistrict().getName() : SPACE));
            totalDecisionReport.setPostCode(Util.checkNullString(bizInfoSummaryView.getPostCode()));
            totalDecisionReport.setCountryBizName(Util.checkNullString(!Util.isNull(bizInfoSummaryView.getCountry()) ? bizInfoSummaryView.getCountry().getName() : SPACE));
            totalDecisionReport.setAddressEng(Util.checkNullString(bizInfoSummaryView.getAddressEng()));
        }

        return totalDecisionReport;
    }

    public FollowUpConditionDecisionReport fillFollowDetail(){
//        init();
        FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();

        for (DecisionFollowConditionView view : decisionView.getDecisionFollowConditionViewList()){
            followUpConditionDecisionReport.setConditionView(Util.checkNullString(!Util.isNull(view.getConditionView()) ? view.getConditionView().getName() : SPACE));
            followUpConditionDecisionReport.setDetail(Util.checkNullString(view.getDetail()));
            followUpConditionDecisionReport.setFollwDateDetaill(DateTimeUtil.getCurrentDateTH(view.getFollowDate()));
        }

        return followUpConditionDecisionReport;
    }

    public PriceFeeDecisionReport fillPriceFee(){
//        init();
        PriceFeeDecisionReport priceFeeDecisionReport = new PriceFeeDecisionReport();
        priceFeeDecisionReport.setIntFeeDOA(Util.convertNullToZERO(decisionView.getIntFeeDOA()));
        priceFeeDecisionReport.setFrontendFeeDOA(Util.convertNullToZERO(decisionView.getFrontendFeeDOA()));
        priceFeeDecisionReport.setGuarantorBA(Util.convertNullToZERO(decisionView.getGuarantorBA()));
        priceFeeDecisionReport.setReasonForReduction(Util.checkNullString(decisionView.getReasonForReduction()));
        return priceFeeDecisionReport;
    }

    public HeaderAndFooterReport fillHeader(){
        HeaderAndFooterReport report = new HeaderAndFooterReport();

        HttpSession session = FacesUtil.getSession(true);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");

        if (!Util.isNull(appHeaderView)){
            log.debug("--Header. {}",appHeaderView);
            report.setCaseStatus(Util.checkNullString(appHeaderView.getCaseStatus()));
            report.setBdmName(Util.checkNullString(appHeaderView.getBdmName()));
            report.setBdmPhoneNumber(Util.checkNullString(appHeaderView.getBdmPhoneNumber()));
            report.setBdmPhoneExtNumber(Util.checkNullString(appHeaderView.getBdmPhoneExtNumber()));
            report.setBdmZoneName(Util.checkNullString(appHeaderView.getBdmZoneName()));
            report.setBdmRegionName(Util.checkNullString(appHeaderView.getBdmRegionName()));
            report.setSubmitDate(Util.checkNullString(appHeaderView.getSubmitDate()));
            report.setUwName(Util.checkNullString(appHeaderView.getUwName()));
            report.setUwPhoneNumber(Util.checkNullString(appHeaderView.getUwPhoneNumber()));
            report.setUwPhoneExtNumber(Util.checkNullString(appHeaderView.getUwPhoneExtNumber()));
            report.setUwTeamName(Util.checkNullString(appHeaderView.getUwTeamName()));
            report.setRequestType(Util.checkNullString(appHeaderView.getRequestType()));
            report.setAppNo(Util.checkNullString(appHeaderView.getAppNo()));
            report.setAppRefNo(Util.checkNullString(appHeaderView.getAppRefNo()));
            report.setAppRefDate(Util.checkNullString(appHeaderView.getAppRefDate()));
            report.setProductGroup(Util.checkNullString(appHeaderView.getProductGroup()));
            report.setRefinance(Util.checkNullString(appHeaderView.getRefinance()));

            log.debug("--getBorrowerHeaderViewList Size. {}",appHeaderView.getBorrowerHeaderViewList().size());

            for (int i = 0;i < appHeaderView.getBorrowerHeaderViewList().size() && i < 5; i++){
                switch (i){
                    case 0 : report.setBorrowerName(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName() : SPACE));
                             report.setPersonalId(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId() : SPACE));
                        break;
                    case 1 : report.setBorrowerName(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName() : SPACE));
                             report.setPersonalId(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId() : SPACE));
                        break;
                    case 2 : report.setBorrowerName(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName() : SPACE));
                             report.setPersonalId(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId() : SPACE));
                        break;
                    case 3 : report.setBorrowerName(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName() : SPACE));
                             report.setPersonalId(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId() : SPACE));
                        break;
                    case 4 : report.setBorrowerName(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName() : SPACE));
                             report.setPersonalId(Util.checkNullString(!Util.isNull(appHeaderView.getBorrowerHeaderViewList().get(i)) ? appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId() : SPACE));
                        break;
                }

                report.setCreditDecision(Util.checkNullString(appHeaderView.getProductGroup()));
                report.setApprovedDate(workCase.getCompleteDate());
            }
        } else {
            log.debug("--Header is Null. {}",appHeaderView);
        }

        return report;
    }

    public HeaderAndFooterReport fillFooter(){
        HeaderAndFooterReport report = new HeaderAndFooterReport();

        String date = Util.createDateAndTimeTh(new Date());
        log.debug("--Date. {}",date);

        String userName =  decisionControl.getCurrentUserID();
        log.debug("---------- {}",userName);

        StringBuilder genFooter = new StringBuilder();
        genFooter = genFooter.append(userName).append(SPACE).append(date);
        report.setGenFooter(genFooter.toString());




        return report;
    }


    //Exsummary
    public BorrowerCharacteristicExSumReport fillBorrowerCharacteristic(){
//        init();
        BorrowerCharacteristicExSumReport characteristicExSumReport = new BorrowerCharacteristicExSumReport();
        ExSumCharacteristicView exSumCharacteristicView = exSummaryView.getExSumCharacteristicView();
        log.debug("exSumCharacteristicView: {}",exSumCharacteristicView);

        if(!Util.isNull(exSumCharacteristicView)){
            characteristicExSumReport.setCustomer(Util.checkNullString(exSumCharacteristicView.getCustomer()));
            characteristicExSumReport.setCurrentDBR(Util.convertNullToZERO(exSumCharacteristicView.getCurrentDBR()));
            characteristicExSumReport.setFinalDBR(Util.convertNullToZERO(exSumCharacteristicView.getFinalDBR()));
            characteristicExSumReport.setIncome(Util.convertNullToZERO(exSumCharacteristicView.getIncome()));
            characteristicExSumReport.setRecommendedWCNeed(Util.convertNullToZERO(exSumCharacteristicView.getRecommendedWCNeed()));
            characteristicExSumReport.setActualWC(Util.convertNullToZERO(exSumCharacteristicView.getActualWC()));
            characteristicExSumReport.setStartBusinessDate(exSumCharacteristicView.getStartBusinessDate());
            characteristicExSumReport.setYearInBusiness(Util.checkNullString(exSumCharacteristicView.getYearInBusiness()));
            characteristicExSumReport.setSalePerYearBDM(Util.convertNullToZERO(exSumCharacteristicView.getSalePerYearBDM()));
            characteristicExSumReport.setSalePerYearUW(Util.convertNullToZERO(exSumCharacteristicView.getSalePerYearUW()));
            characteristicExSumReport.setGroupSaleBDM(Util.convertNullToZERO(exSumCharacteristicView.getGroupSaleBDM()));
            characteristicExSumReport.setGroupSaleUW(Util.convertNullToZERO(exSumCharacteristicView.getGroupSaleUW()));
            characteristicExSumReport.setGroupExposureBDM(Util.convertNullToZERO(exSumCharacteristicView.getGroupExposureBDM()));
            characteristicExSumReport.setGroupExposureUW(Util.convertNullToZERO(exSumCharacteristicView.getGroupExposureUW()));
        } else {
            log.debug("exSumCharacteristicView in Method fillBorrowerCharacteristic is Null. {}",exSumCharacteristicView);
        }


        ExSumBusinessInfoView exSumBusinessInfoView = exSummaryView.getExSumBusinessInfoView();

        if (!Util.isNull(exSumBusinessInfoView)){
            characteristicExSumReport.setNetFixAsset(Util.convertNullToZERO(exSumBusinessInfoView.getNetFixAsset()));
            characteristicExSumReport.setNoOfEmployee(Util.convertNullToZERO(exSumBusinessInfoView.getNoOfEmployee()));
            characteristicExSumReport.setBizProvince(Util.checkNullString(exSumBusinessInfoView.getBizProvince()));
            characteristicExSumReport.setBizType(Util.checkNullString(exSumBusinessInfoView.getBizType()));
            characteristicExSumReport.setBizGroup(Util.checkNullString(exSumBusinessInfoView.getBizGroup()));
            characteristicExSumReport.setBizCode(Util.checkNullString(exSumBusinessInfoView.getBizCode()));
            characteristicExSumReport.setBizDesc(Util.checkNullString(exSumBusinessInfoView.getBizDesc()));
            characteristicExSumReport.setQualitativeClass(Util.checkNullString(exSumBusinessInfoView.getQualitativeClass()));
            characteristicExSumReport.setBizSize(Util.convertNullToZERO(exSumBusinessInfoView.getBizSize()));
            characteristicExSumReport.setBDM(Util.convertNullToZERO(exSumBusinessInfoView.getBDM()));
            characteristicExSumReport.setUW(Util.convertNullToZERO(exSumBusinessInfoView.getUW()));
            characteristicExSumReport.setAR(Util.convertNullToZERO(exSumBusinessInfoView.getAR()));
            characteristicExSumReport.setAP(Util.convertNullToZERO(exSumBusinessInfoView.getAP()));
            characteristicExSumReport.setINV(Util.convertNullToZERO(exSumBusinessInfoView.getINV()));
        } else {
            log.debug("exSumBusinessInfoView in Mrthod fillBorrowerCharacteristic is Null. {}",exSumBusinessInfoView);
        }

        if(!Util.isNull(exSummaryView)){
            characteristicExSumReport.setBusinessOperationActivity(Util.checkNullString(exSummaryView.getBusinessOperationActivity()));
            characteristicExSumReport.setBusinessPermission(Util.checkNullString(exSummaryView.getBusinessPermission()));
            characteristicExSumReport.setExpiryDate(exSummaryView.getExpiryDate());
        } else {
            log.debug("exSummaryView in Method fillBorrowerCharacteristic is Null. {}",exSummaryView);
        }


        log.info("fillBorrowerCharacteristic: {}",characteristicExSumReport.toString());
        return characteristicExSumReport;
    }

//    public TotalDecisionReport fillBizInfoSum(){
//
//        TotalDecisionReport totalDecisionReport = new TotalDecisionReport();
//
//        if (!Util.isNull(bizInfoSummaryView)) {
//            totalDecisionReport.setBizLocationName(Util.checkNullString(bizInfoSummaryView.getBizLocationName()));
//            totalDecisionReport.setRental(bizInfoSummaryView.getRental());
//            totalDecisionReport.setOwnerName(Util.checkNullString(bizInfoSummaryView.getOwnerName()));
//            totalDecisionReport.setExpiryDate(DateTimeUtil.getCurrentDateTH(bizInfoSummaryView.getExpiryDate()));
//            totalDecisionReport.setAddressNo(Util.checkNullString(bizInfoSummaryView.getAddressNo()));
//            totalDecisionReport.setAddressMoo(Util.checkNullString(bizInfoSummaryView.getAddressMoo()));
//            totalDecisionReport.setAddressBuilding(Util.checkNullString(bizInfoSummaryView.getAddressBuilding()));
//            totalDecisionReport.setAddressStreet(Util.checkNullString(bizInfoSummaryView.getAddressStreet()));
//            totalDecisionReport.setProvinceName(Util.checkNullString(bizInfoSummaryView.getProvince().getName()));
//            totalDecisionReport.setDistrictName(Util.checkNullString(bizInfoSummaryView.getDistrict().getName()));
//            totalDecisionReport.setSubDisName(Util.checkNullString(bizInfoSummaryView.getSubDistrict().getName()));
//            totalDecisionReport.setPostCode(Util.checkNullString(bizInfoSummaryView.getPostCode()));
//            totalDecisionReport.setCountryBizName(Util.checkNullString(bizInfoSummaryView.getCountry().getName()));
//            totalDecisionReport.setAddressEng(Util.checkNullString(bizInfoSummaryView.getAddressEng()));
//
//            log.debug("fillBizInfoSum is not null. {}",totalDecisionReport);
//        } else {
//            log.debug("fillBizInfoSum null. {}",totalDecisionReport);
//        }
//
//        return totalDecisionReport;
//    }
}
