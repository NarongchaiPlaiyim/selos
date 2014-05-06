package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.ApprovedCreditOfferLetterReport;
import com.clevel.selos.model.report.CollateralAndGuarantorOfferLetterReport;
import com.clevel.selos.model.report.FollowConditionOfferletterReport;
import com.clevel.selos.model.report.OfferLetterReport;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PDFOfferLetter implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    private DecisionControl decisionControl;

    @Inject
    private DecisionView decisionView;

    @Inject
    private AppHeaderView appHeaderView;

    @Inject
    private HeaderControl headerControl;

    @Inject
    protected BaseRateDAO baseRateDAO;

    private long workCaseId;

    public PDFOfferLetter() {
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
            log.debug("--decisionView. {[]}",decisionView);
        } else {
            log.debug("--workcaseId is Null. {}",workCaseId);
        }

//        decisionView = new DecisionView();
//        decisionView = decisionControl.getDecisionView(1023);
    }

    public List<ApprovedCreditOfferLetterReport> fillApprovedCredit(){
//        init();
        log.debug("--fillApprovedCredit()");
        List<ApprovedCreditOfferLetterReport> reports = new ArrayList<ApprovedCreditOfferLetterReport>();
        List<NewCreditDetailView> newCreditDetailViews = decisionView.getApproveCreditList();

        if(Util.safetyList(newCreditDetailViews).size() > 0){
            log.debug("--ApproveCreditList. [{}],Size. {}",newCreditDetailViews,newCreditDetailViews.size());
            for (NewCreditDetailView view : newCreditDetailViews){
                log.debug("--tierDetailView Size. {}",view.getNewCreditTierDetailViewList().size());
                for (NewCreditTierDetailView tierDetailView : view.getNewCreditTierDetailViewList()){
                    ApprovedCreditOfferLetterReport approvedCredit = new ApprovedCreditOfferLetterReport();
                    approvedCredit.setProductProgramName(Util.checkNullString(view.getProductProgramView().getName()));
                    approvedCredit.setCreditTypeName(Util.checkNullString(view.getCreditTypeView().getName()));
                    approvedCredit.setLimit(Util.convertNullToZERO(view.getLimit()));
                    approvedCredit.setFinalPriceLabel(tierDetailView.getFinalPriceLabel());
                    approvedCredit.setInstallment(Util.convertNullToZERO(tierDetailView.getInstallment()));
                    approvedCredit.setTenor(tierDetailView.getTenor());
                    approvedCredit.setPurpose(view.getLoanPurposeView().getDescription());
                    reports.add(approvedCredit);
                }
            }
        } else {
            log.debug("--ApproveCreditList is Null. [{}]",newCreditDetailViews);
            ApprovedCreditOfferLetterReport approvedCredit = new ApprovedCreditOfferLetterReport();
            reports.add(approvedCredit);
        }

        log.debug("--Data fillApprovedCredit. {}",reports);
        return reports;
    }

    public List<CollateralAndGuarantorOfferLetterReport> fillGuarantor(){
        log.debug("--fillGuarantor");
        List<CollateralAndGuarantorOfferLetterReport> reports = new ArrayList<CollateralAndGuarantorOfferLetterReport>();
        List<NewCollateralView> collateralViews = decisionView.getApproveCollateralList();
        StringBuilder collOwnerUW = null;

        List<NewGuarantorDetailView> guarantorDetailViews = decisionView.getApproveGuarantorList();

        if (Util.safetyList(collateralViews).size() > 0){
            log.debug("--collateralView Size. {}",collateralViews.size());

            for(NewCollateralView view : collateralViews){
                for (NewCollateralHeadView headView : view.getNewCollateralHeadViewList()){
                    for (NewCollateralSubView newCollateralSubView : headView.getNewCollateralSubViewList()){
                        CollateralAndGuarantorOfferLetterReport collateralAndGuarantorOfferLetterReport = new CollateralAndGuarantorOfferLetterReport();
                        collateralAndGuarantorOfferLetterReport.setSubCollateralTypeName(Util.checkNullString(newCollateralSubView.getSubCollateralType().getDescription()));
                        collateralAndGuarantorOfferLetterReport.setTitleDeed(Util.checkNullString(newCollateralSubView.getTitleDeed()));
                        collateralAndGuarantorOfferLetterReport.setMortgageValue(Util.convertNullToZERO(newCollateralSubView.getMortgageValue()));


                        for (CustomerInfoView customerInfoView : newCollateralSubView.getCollateralOwnerUWList()){
                            collOwnerUW = new StringBuilder();
                            collOwnerUW = collOwnerUW.append(customerInfoView.getTitleTh().getTitleTh())
                                    .append(customerInfoView.getFirstNameTh()).append(" ")
                                    .append(customerInfoView.getLastNameTh());
                            collateralAndGuarantorOfferLetterReport.setCollateralOwnerUW(Util.checkNullString(collOwnerUW.toString()));

                            log.debug("--collOwnerUW. {},TitleTh. {},FirstNameTh. {},LastNameTh. {}", collOwnerUW.toString(), customerInfoView.getTitleTh().getTitleTh()
                                    , customerInfoView.getFirstNameTh(), customerInfoView.getLastNameTh());
                        }

                        collateralAndGuarantorOfferLetterReport.setAddress(Util.checkNullString(newCollateralSubView.getAddress()));

                        log.debug("--mortgageTypeView. {},Size. {}",newCollateralSubView.getMortgageList(),newCollateralSubView.getMortgageList().size());
                        for (MortgageTypeView mortgageTypeView : newCollateralSubView.getMortgageList()){
                            collateralAndGuarantorOfferLetterReport.setMortgageList(Util.checkNullString(mortgageTypeView.getMortgage()));
                        }
                        reports.add(collateralAndGuarantorOfferLetterReport);
                    }
                }
            }
        } else {
            log.debug("--CollateralViews is Null. [{}]",collateralViews);
            CollateralAndGuarantorOfferLetterReport collateralAndGuarantorOfferLetterReport = new CollateralAndGuarantorOfferLetterReport();
            reports.add(collateralAndGuarantorOfferLetterReport);
        }

        log.debug("--Data fillGuarantor. {}",reports);
        return reports;
    }

    public List<FollowConditionOfferletterReport> fillFollowCondition(){
        List<FollowConditionOfferletterReport> reports = new ArrayList<FollowConditionOfferletterReport>();
        int count = 1;

        if (Util.safetyList(decisionView.getDecisionFollowConditionViewList()).size() > 0){
            log.debug("--DecisionFollowConditionViewList Size. {}", decisionView.getDecisionFollowConditionViewList().size());

            for (DecisionFollowConditionView followConditionView : decisionView.getDecisionFollowConditionViewList()){
                FollowConditionOfferletterReport conditionOfferletterReport = new FollowConditionOfferletterReport();
                conditionOfferletterReport.setCount(count++);
                conditionOfferletterReport.setName(Util.checkNullString(followConditionView.getConditionView().getName()));
                reports.add(conditionOfferletterReport);
            }
        } else {
            log.debug("--DecisionFollowConditionViewList is Null. {}", decisionView.getDecisionFollowConditionViewList().size());
            FollowConditionOfferletterReport conditionOfferletterReport = new FollowConditionOfferletterReport();
            reports.add(conditionOfferletterReport);

        }

        log.debug("--Data fillFollowCondition. {}",reports);
        return reports;
    }

    //Master Report OfferLetter
    public OfferLetterReport fillMasterOfferLetter(){
        OfferLetterReport report = new OfferLetterReport();

        HttpSession session = FacesUtil.getSession(true);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");

        if (!Util.isNull(appHeaderView)){
            report.setBdmName(Util.checkNullString(appHeaderView.getBdmName()));
            report.setTel(Util.checkNullString(Util.checkNullString(appHeaderView.getBdmPhoneNumber())));
            report.setZone(Util.checkNullString(appHeaderView.getBdmZoneName()));

            //TotalLimit
            report.setTotalLimit(Util.convertNullToZERO(decisionView.getApproveTotalCreditLimit()));

            report.setValueMLR(decisionControl.getMLRValue());
            report.setValueMOR(decisionControl.getMORValue());
            report.setValueMRR(decisionControl.getMRRValue());

            if (Util.safetyList(appHeaderView.getBorrowerHeaderViewList()).size() > 0){
                log.debug("--BorrowerHeaderViewList Size. {}",appHeaderView.getBorrowerHeaderViewList().size());

                for (AppBorrowerHeaderView view : appHeaderView.getBorrowerHeaderViewList()){
                    report.setBorrowName(Util.checkNullString(view.getBorrowerName()));
                }
            }
        }

        return report;
    }
}
