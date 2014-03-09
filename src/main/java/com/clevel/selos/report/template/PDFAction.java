package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.report.BorrowerExsumReport;
import com.clevel.selos.model.report.NCBRecordExsumReport;
import com.clevel.selos.model.report.TradeFinanceExsumReport;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PDFAction implements Serializable {
    @Inject
    private ExSummaryControl exSummaryControl;

    @Inject
    CustomerInfoControl customerInfoControl;

    @Inject
    private TitleDAO titleDAO;

    @Inject
    CustomerDAO customerDAO;

    Logger log;

    long workCaseId;

    @Inject
    ExSummaryView exSummaryView;

    @Inject
    public PDFAction() {
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
        exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);
    }

    public List<BorrowerExsumReport> fillBorrowerRelatedProfile(){
        init();
        List<BorrowerExsumReport> reports = new ArrayList<BorrowerExsumReport>();
        List<CustomerInfoView> customerInfoViewList = exSummaryView.getBorrowerListView();

        int count = 1 ;
        for (CustomerInfoView view:customerInfoViewList){
            BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();
            borrowerExsumReport.setNo(count++);
            borrowerExsumReport.setTitleTh(view.getTitleTh().getTitleTh());
            borrowerExsumReport.setFirstNameTh(view.getFirstNameTh());
            borrowerExsumReport.setLastNameTh(view.getLastNameTh());
            borrowerExsumReport.setCitizenId(view.getCitizenId());
            borrowerExsumReport.setRegistrationId(view.getRegistrationId());
            borrowerExsumReport.setTmbCustomerId(view.getTmbCustomerId());
            borrowerExsumReport.setRelation(view.getRelation().getDescription());
            if(view.getCollateralOwner() == 2){
                borrowerExsumReport.setCollateralOwner("Y");
            } else {
                borrowerExsumReport.setCollateralOwner("N");
            }

            borrowerExsumReport.setIndLv(view.getIndLv());
            borrowerExsumReport.setJurLv(view.getJurLv());
            borrowerExsumReport.setPercentShare(view.getPercentShare());
            borrowerExsumReport.setAge(view.getAge());
            borrowerExsumReport.setKycLevel(view.getKycLevel().getKycLevel());
            List<CustomerCSIView> customerCSIList = view.getCustomerCSIList();

            for(CustomerCSIView csiView:customerCSIList){
                borrowerExsumReport.setCustomerCSIList(csiView.getWarningCode().getCode());
            }

            if(view.getWorthiness() == 3){
                borrowerExsumReport.setWorthiness("Pass");
            } else if (view.getWorthiness() == 4){
                borrowerExsumReport.setWorthiness("Fail");
            } else if (view.getWorthiness() == 5){
                borrowerExsumReport.setWorthiness("N/A");
            } else {
                borrowerExsumReport.setWorthiness("-");
            }

            reports.add(borrowerExsumReport);
        }

        return reports;
    }

    public List<TradeFinanceExsumReport> fillTradeFinance(){
        init();
        List<TradeFinanceExsumReport> financeExsumReports = new ArrayList<TradeFinanceExsumReport>();
        NewCreditFacilityView creditFacilityViews = exSummaryView.getTradeFinance();
        List<NewCreditFacilityView> creditFacilityViews1 = new ArrayList<NewCreditFacilityView>();
        creditFacilityViews1.add(creditFacilityViews);

        for (NewCreditFacilityView facilityView:creditFacilityViews1){
            TradeFinanceExsumReport tradeFinanceExsumReport = new TradeFinanceExsumReport();
            tradeFinanceExsumReport.setContactName(facilityView.getContactName());
            tradeFinanceExsumReport.setContactPhoneNo(facilityView.getContactPhoneNo());
            tradeFinanceExsumReport.setInterService(facilityView.getInterService());
            tradeFinanceExsumReport.setCurrentAddress(facilityView.getCurrentAddress());
            tradeFinanceExsumReport.setImportMail(facilityView.getImportMail());
            tradeFinanceExsumReport.setExportMail(facilityView.getExportMail());
            tradeFinanceExsumReport.setDepositBranchCode(facilityView.getDepositBranchCode());
            tradeFinanceExsumReport.setOwnerBranchCode(facilityView.getOwnerBranchCode());
            System.out.println(tradeFinanceExsumReport);
            financeExsumReports.add(tradeFinanceExsumReport);
        }
        return financeExsumReports;
    }

    public List<NCBRecordExsumReport> fillNCBRecord(){
        init();
        List<NCBRecordExsumReport> recordExsumReports = new ArrayList<NCBRecordExsumReport>();
        List<NCBInfoView> ncbInfoViewList = exSummaryView.getNcbInfoListView();

        for (NCBInfoView ncbInfoView:ncbInfoViewList){
            NCBRecordExsumReport ncbRecordExsumReport = new NCBRecordExsumReport();
            ncbRecordExsumReport.setNcbCusName(ncbInfoView.getNcbCusName());
            ncbRecordExsumReport.setCheckIn6Month(ncbInfoView.getCheckIn6Month());
            ncbRecordExsumReport.setCurrentPaymentType(ncbInfoView.getCurrentPaymentType());
            ncbRecordExsumReport.setHistoryPaymentType(ncbInfoView.getHistoryPaymentType());
            ncbRecordExsumReport.setNplFlagText(ncbInfoView.getNplFlagText());
            ncbRecordExsumReport.setTdrFlagText(ncbInfoView.getTdrFlagText());
            ncbRecordExsumReport.setDescription(ncbInfoView.getTdrCondition().getDescription());
            ncbRecordExsumReport.setPaymentClass(ncbInfoView.getPaymentClass());

            recordExsumReports.add(ncbRecordExsumReport);
        }

        return recordExsumReports;
    }

//    public BorrowerExsumReport
}
