package com.clevel.selos.report;

import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.ISAViewReport;
import com.clevel.selos.model.view.ReportView;
import com.clevel.selos.report.template.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "report")
public class GenPDF extends ReportService implements Serializable {

    @Inject
    @Config(name = "report.exsum")
    String pathExsum;

    @Inject
    @Config(name = "report.decision")
    String pathDecision;

    @Inject
    @Config(name = "report.subreport")
    String pathsub;

    @Inject
    @Config(name = "report.rejectletter.policy")
    String pathPolicyRejectLetter;

    @Inject
    @Config(name = "report.rejectletter.ncb")
    String pathNCBRejectLetter;

    @Inject
    @Config(name = "report.rejectletter.income")
    String pathIncomeRejectLetter;

    @Inject
    @Config(name = "report.rejectletter.policyincome")
    String pathPolicyIncomeRejectLetter;


    @Inject
    @Config(name = "report.appraisal")
    String pathAppraisal;

    @Inject
    @Config(name = "report.offerletter")
    String pathOfferLetter;


    @Inject
    private WorkCaseDAO workCaseDAO;

    WorkCase workCase; // ห้าม @Inject

    @Inject
    PDFExecutiveSummaryAndOpSheet pdfExecutiveSummary;

    @Inject
    PDFRejectLetter pdfReject_letter;

    @Inject
    PDFAppraisalAppointment pdfAppraisalAppointment;

    @Inject
    PDFOfferLetter pdfOfferLetter;

    private ReportView reportView;

    long workCaseId;
    private boolean type;

    @Inject
    private STPExecutor stpExecutor;

    public GenPDF() {

    }

    public void init(){
        log.debug("init() {[]}");
        HttpSession session = FacesUtil.getSession(false);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.debug("workCaseId. {}",workCaseId);
        }else{
            log.debug("workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    private void onCreation(){
        init();
        log.debug("GenPDF onCreation ");
    }

    String pdfName;

    public void setNameReport(){
        init();
        log.info("On setNameReport()");
        String date = Util.createDateTime(new Date());
        String[] month = date.split("");
        log.debug("--month. {}",month);

        type = false;

        if(!Util.isNull(workCaseId)){
            workCase = workCaseDAO.findById(workCaseId);
            String appNumber = workCase.getAppNumber();
            reportView = new ReportView();

            StringBuilder nameOpShect =new StringBuilder();
            nameOpShect = nameOpShect.append(appNumber).append("_").append(date).append("_OpSheet.pdf");

            StringBuilder nameExSum =new StringBuilder();
            nameExSum = nameExSum.append(appNumber).append("_").append(date).append("_ExSum.pdf");

            StringBuilder nameRejectLetter =new StringBuilder();
            nameRejectLetter = nameRejectLetter.append(appNumber).append("_").append(date).append("_RejectLetter.pdf");

            StringBuilder nameAppraisal =new StringBuilder();
            nameAppraisal = nameAppraisal.append(appNumber).append("_").append(date).append("_AADRequest.pdf");

            StringBuilder nameOfferLetter =new StringBuilder();
            nameOfferLetter = nameOfferLetter.append(appNumber).append("_").append(date).append("_OfferLetter.pdf");

            reportView.setNameReportOpShect(nameOpShect.toString());
            reportView.setNameReportExSum(nameExSum.toString());

            pdfReject_letter.init();
            reportView.setNameReportRejectLetter(nameRejectLetter.toString());
            reportView.setNameReportAppralsal(nameAppraisal.toString());
            reportView.setNameReportOfferLetter(nameOfferLetter.toString());
        }
    }

    public void onPrintExsumReport() throws Exception {
        log.debug("onPrintExsumReport");

        pdfExecutiveSummary.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("borrower", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("businessLocation", pdfExecutiveSummary.fillBorrower());
        map.put("tradeFinance", pdfExecutiveSummary.fillTradeFinance());
        map.put("borrowerCharacteristic", pdfExecutiveSummary.fillBorrowerCharacteristic());
        map.put("ncbRecord", pdfExecutiveSummary.fillNCBRecord());
        map.put("accountMovement", pdfExecutiveSummary.fillAccountMovement());
        map.put("collateral", pdfExecutiveSummary.fillCollateral());
        map.put("creditRisk", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("bizSupport", pdfExecutiveSummary.fillBizSupport());
        map.put("uwDecision", pdfExecutiveSummary.fillUWDecision());
        map.put("creditRisk", pdfExecutiveSummary.fillCreditRisk());
        map.put("fillDecision", pdfExecutiveSummary.fillDecision());
        map.put("fillHeader",pdfExecutiveSummary.fillHeader());
        map.put("fillFooter",pdfExecutiveSummary.fillFooter());
        map.put("fillCreditBorrower",pdfExecutiveSummary.fillCreditBorrower(pathsub));
        map.put("fillCondition",pdfExecutiveSummary.fillCondition());
        map.put("fillBorrowerRetail",pdfExecutiveSummary.fillBorrowerRetail(pathsub));
        map.put("fillAppInRLOS",pdfExecutiveSummary.fillAppInRLOS(pathsub));
        map.put("fillRelatedCommercial",pdfExecutiveSummary.fillRelatedCommercial(pathsub));
        map.put("fillRelatedRetail",pdfExecutiveSummary.fillRelatedRetail(pathsub));
        map.put("fillRelatedAppInRLOS",pdfExecutiveSummary.fillRelatedAppInRLOS(pathsub));
        map.put("fillExistingCollateralBorrower",pdfExecutiveSummary.fillExistingCollateralBorrower(pathsub));
        map.put("fillExistingCollateralRelated",pdfExecutiveSummary.fillExistingCollateralRelated(pathsub));
        map.put("fillGuarantorBorrower",pdfExecutiveSummary.fillGuarantorBorrower(pathsub));
        map.put("fillProposedCredit",pdfExecutiveSummary.fillProposedCredit(pathsub));
        map.put("fillApprovedCredit",pdfExecutiveSummary.fillExSumApprovedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfExecutiveSummary.fillProposeFeeInformation());
        map.put("fillProposedCollateral",pdfExecutiveSummary.fillProposedCollateral(pathsub));
        map.put("fillApprovedCollaterral",pdfExecutiveSummary.fillExSumApprovedCollaterral(pathsub));
        map.put("fillProposedGuarantor",pdfExecutiveSummary.fillProposedGuarantor(pathsub));
        map.put("fillApprovedGuarantor",pdfExecutiveSummary.fillExSumApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfExecutiveSummary.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfExecutiveSummary.fillTotalMasterReport("all"));
        map.put("fillFollowDetail",pdfExecutiveSummary.fillFollowDetail());
        map.put("fillPriceFee",pdfExecutiveSummary.fillPriceFee());

        generatePDF(pathExsum, map, reportView.getNameReportExSum(),null);
    }

    public void onPrintDecisionReport() throws Exception {
        log.debug("onPrintDecisionReport");

        pdfExecutiveSummary.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("borrower", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("businessLocation", pdfExecutiveSummary.fillBorrower());
        map.put("tradeFinance", pdfExecutiveSummary.fillTradeFinance());
        map.put("borrowerCharacteristic", pdfExecutiveSummary.fillBorrowerCharacteristic());
        map.put("ncbRecord", pdfExecutiveSummary.fillNCBRecord());
        map.put("accountMovement", pdfExecutiveSummary.fillAccountMovement());
        map.put("collateral", pdfExecutiveSummary.fillCollateral());
        map.put("creditRisk", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("uwDecision", pdfExecutiveSummary.fillUWDecision());
        map.put("creditRisk", pdfExecutiveSummary.fillCreditRisk());
        map.put("fillHeader",pdfExecutiveSummary.fillHeader());
        map.put("fillFooter",pdfExecutiveSummary.fillFooter());
        map.put("fillCreditBorrower",pdfExecutiveSummary.fillCreditBorrower(pathsub));
        map.put("fillCondition",pdfExecutiveSummary.fillCondition());
        map.put("fillBorrowerRetail",pdfExecutiveSummary.fillBorrowerRetail(pathsub));
        map.put("fillRelatedCommercial",pdfExecutiveSummary.fillRelatedCommercial(pathsub));
        map.put("fillRelatedRetail",pdfExecutiveSummary.fillRelatedRetail(pathsub));
        map.put("fillExistingCollateralBorrower",pdfExecutiveSummary.fillExistingCollateralBorrower(pathsub));
        map.put("fillExistingCollateralRelated",pdfExecutiveSummary.fillExistingCollateralRelated(pathsub));
        map.put("fillGuarantorBorrower",pdfExecutiveSummary.fillGuarantorBorrower(pathsub));
        map.put("fillApprovedCredit",pdfExecutiveSummary.fillApprovedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfExecutiveSummary.fillProposeFeeInformation());
        map.put("fillApprovedCollaterral",pdfExecutiveSummary.fillApprovedCollaterral(pathsub));
        map.put("fillApprovedGuarantor",pdfExecutiveSummary.fillApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfExecutiveSummary.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfExecutiveSummary.fillTotalMasterReport("opsheet"));
        map.put("fillFollowDetail",pdfExecutiveSummary.fillFollowDetail());
        map.put("fillPriceFee",pdfExecutiveSummary.fillPriceFee());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());

        generatePDF(pathDecision, map, reportView.getNameReportOpShect(),null);
    }

    public void onPrintRejectLetter() throws Exception {
        log.debug("--onPrintRejectLetter");
        pdfReject_letter.init();
        String pathReportReject = null;
        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);

        if (!Util.isNull( pdfReject_letter.typeReport())){
            if (!Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome()) ||
                    Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject =  pathPolicyIncomeRejectLetter;
                log.debug("--path4. {}",pathReportReject);
            } else if (!Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome()) ||
                    Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject =  pathIncomeRejectLetter;
                log.debug("--path3. {}",pathReportReject);
            } else if(Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome()) ||
                    !Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject =  pathPolicyRejectLetter;
                log.debug("--path2. {}",pathReportReject);
            } else if (!Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject = pathNCBRejectLetter;//NCBRejectLetter wait it
                log.debug("--path1. {}",pathReportReject);
            }
            map.put("fillAllNameReject", pdfReject_letter.fillAllNameReject());
            map.put("fillRejectLetter",pdfReject_letter.fillRejectLetter());

            generatePDF(pathReportReject,map,reportView.getNameReportRejectLetter(),null);
        } else {
            log.debug("--RejectGroup is Null");
        }
    }
    public void onPrintAppraisal() throws Exception {
        log.debug("--onPrintAppraisal");
        pdfAppraisalAppointment.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillAppraisalDetailReport",pdfAppraisalAppointment.fillAppraisalDetailReport());
        map.put("fillAppraisalDetailViewReport",pdfAppraisalAppointment.fillAppraisalDetailViewReport(pathsub));
        map.put("fillAppraisalContactDetailViewReport",pdfAppraisalAppointment.fillAppraisalContactDetailViewReport());
        map.put("fillContactRecordDetailViewReport",pdfAppraisalAppointment.fillContactRecordDetailViewReport());

        generatePDF(pathAppraisal,map,reportView.getNameReportAppralsal(),null);
    }

    public void onPrintOfferletter() throws Exception {
        log.debug("--onPrintOfferletter");
        pdfOfferLetter.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path",pathsub);
        map.put("fillApprovedCredit",pdfOfferLetter.fillApprovedCredit());
        map.put("fillGuarantor",pdfOfferLetter.fillGuarantor(pathsub));
        map.put("fillFollowCondition",pdfOfferLetter.fillFollowCondition());
        map.put("fillMasterOfferLetter",pdfOfferLetter.fillMasterOfferLetter());

        generatePDF(pathOfferLetter, map, reportView.getNameReportOfferLetter(),null);
    }

    public void onPrintLogonOver90(){
        log.debug("--on onPrintLogonOver90.");
        HashMap map = new HashMap<String, Object>();
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();

        ResultSet rs = stpExecutor.getLogonOver90();
        int i = 1;
        String jasper = "LogonOver90";

        try {
            while (rs.next()){
                ISAViewReport viewReport = new ISAViewReport();
                viewReport.setRow(i++);
                viewReport.setUserId(rs.getString("USER_ID"));
                viewReport.setUserName(rs.getString("USER_NAME"));
                viewReport.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                viewReport.setLogin(rs.getTimestamp("LAST_LOGIN_DATE"));
                viewReport.setStatus(rs.getString("STATUS"));
                viewReport.setNumberOfDay(rs.getString("NUMBER_OF_DAY"));
                viewReportList.add(viewReport);
            }
            exportPDF(map,viewReportList,jasper);
        } catch (SQLException e) {
            log.debug("on getLogonOver90. {}",e);
        } catch (Exception e) {
            log.debug("onPrintLogonOver90. {}",e);
        }
    }

    public void onPrintViolation(){
        log.debug("--on onPrintViolation.");
        HashMap map = new HashMap<String, Object>();
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
//        String jasper = "C:/Users/pakorn/Desktop/ISAREPORT/Violation";

        try {
            String jasper = "C:/Users/pakorn/Desktop/ISAREPORT/Violation.jrxml";
            ResultSet rs = stpExecutor.getViolation("30/05/2014","24/06/2014");
            log.debug("--rs in onPrintViolation. {}",rs);
            ISAViewReport viewReport = null;
            int round = 0 ;

            while (rs.next());{
                log.debug("round {}", round++);
                round++;
//                viewReport = new ISAViewReport();
////                viewReport.setUserId(rs.getString("USER_ID"));
////                log.debug("getUserId. {}",viewReport.getUserId());
////                rs.getObject("USER_ID");
////                rs.getObject("IP_ADDRESS");
////                rs.getObject("LOGIN_DATE");
////                rs.getObject("STATUS");
////                rs.getObject("DESCRIPTI");
//
//                viewReport.setUserId("test");
//                viewReport.setIpAddress("11111");
//                viewReport.setLogin(new Date());
//                viewReport.setStatus("22222");
//                viewReport.setDescrition("33333");
//                viewReportList.add(viewReport);
//                log.debug("---------------------333333. {}",viewReportList.size());

                viewReport.setUserId(rs.getString("USER_ID"));
                viewReport.setIpAddress(rs.getString("IP_ADDRESS"));
                viewReport.setLogin(rs.getTimestamp("LOGIN_DATE"));
                viewReport.setStatus(rs.getString("STATUS"));
                viewReport.setDescrition(rs.getString("DESCRIPTION"));
                viewReportList.add(viewReport);
            }
            log.debug("--Path Report. {}",jasper);
            generatePDF(jasper, map, "1111111111111",viewReportList);
//            exportPDF(map,viewReportList,jasper);
        } catch (SQLException e) {
            log.debug("----on getViolation. {}",e);
        } catch (Exception e) {
            log.debug("onPrintLogonOver90. {}",e);
        }
    }

    public ReportView getReportView() {
        return reportView;
    }

    public void setReportView(ReportView reportView) {
        this.reportView = reportView;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
