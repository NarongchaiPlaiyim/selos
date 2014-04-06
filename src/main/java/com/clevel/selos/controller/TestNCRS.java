package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.AppraisalResultControl;
import com.clevel.selos.businesscontrol.BRMSControl;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.ncb.NCBInterfaceImpl;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.RegistType;
import com.clevel.selos.integration.ncb.nccrs.service.NCCRSService;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.IdType;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.service.NCRSService;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.MandateDocResponseView;
import com.clevel.selos.model.view.MandateDocView;
import com.clevel.selos.model.view.NewCollateralView;
import com.clevel.selos.transform.business.CollateralBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "TestNCRS")
public class TestNCRS implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    private AppraisalResultControl appraisalResultControl;

    @Inject
    NCRSService ncrsService;

    @Inject
    NCCRSService nccrsService;

    @Inject
    NCBInterfaceImpl ncbInterface;

    @Inject
    private COMSInterface comsInterface;
    @Inject
    private ECMInterface ecmInterface;
    @Inject
    private BRMSInterface brmsInterface;
    private long workCaseId = 481;

    @Inject
    private BRMSControl brmsControl;

    private String appNumber;
    private String crsCustName;

    //    @Inject
//    NCBIService ncbiService;
    //NCRS
    private String result;
    private String memberref = "123456789";
    private String enqpurpose = "01";
    private String enqamount = "01";
    private String consent = "Y";
    private String disputeenquiry;

    private String idNumber = "3100300390029";
    private String idNumber2 = "3100504308002";
    private String appRefNumber = "0420140301000101";
    private String userId = "SERID";
    private String CANumber = "01234567890123456789";
    private String referenceTel = "0800000000";

    //NCCRS
    private String registType = "1140002";
    private String registId = "1000000000001";
    private String companyName = "ทดสอบสอง";
    private String inqPurose = "1170001";
    private String productType = "2030001";
    private String memberRef = "TEST20120918";
    private String confirmConsent = "Y";
    private String language = "2060002";
    private String historicalBalanceReport = "Y";


    //CALL-COM_S
    private String jobId;
    private String userIdForComS = "10001";
    @Inject
    private CollateralBizTransform collateralBizTransform;
    private NewCollateralView newCollateralView;

    //Call ECM
    private String caNumberECM = "04621809124082010060";


    @Inject
    public TestNCRS() {
    }

    public void onClickNCRS() {
        log.info("========================================= onClickNCRS");
        NCRSModel ncrsModel = null;
        ArrayList<NCRSModel> ncrsModelArrayList = new ArrayList<NCRSModel>();

        /*ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId(idNumber);
        ncrsModel.setFirstName("พสุธร");
        ncrsModel.setLastName("กุญชร");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId(idNumber2);
        ncrsModel.setFirstName("ขวัญชัย");
        ncrsModel.setLastName("อานุภาพ");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);*/

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("11111");
        ncrsModel.setFirstName("ประดิษฐ์");
        ncrsModel.setLastName("ภัทรประสิทธิ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("22222");
        ncrsModel.setFirstName("วิลาวัณย ");
        ncrsModel.setLastName("อุปริกชาติพงษ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("33333");
        ncrsModel.setFirstName("ประดิษฐ์");
        ncrsModel.setLastName("ภัทรประสิทธิ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("44444");
        ncrsModel.setFirstName("วิลาวัณย ");
        ncrsModel.setLastName("อุปริกชาติพงษ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("55555");
        ncrsModel.setFirstName("น้องใหม่ ");
        ncrsModel.setLastName("อุปริกชาติพงษ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("66666");
        ncrsModel.setFirstName("น้องใหม6่ ");
        ncrsModel.setLastName("อุปริกชาติพงษ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdType(IdType.CITIZEN);
        ncrsModel.setCitizenId("77777");
        ncrsModel.setFirstName("น้องใหม7่ ");
        ncrsModel.setLastName("อุปริกชาติพงษ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        NCRSInputModel inputModel = new NCRSInputModel(userId, "1111", CANumber, referenceTel, ncrsModelArrayList);
        try {
            ArrayList<NCRSOutputModel> ncrsOutputModelArrayList = ncbInterface.request(inputModel);
            for (NCRSOutputModel ncrsOutputModel : ncrsOutputModelArrayList) {
                log.info("NCRS response : {}", ncrsOutputModel.toString());
            }
        } catch (Exception e) {
            log.info("NCRS Exception : {}", e.getMessage());
        }

    }

    public void onClickNCCRS() {
        log.info("========================================= onClickNCCRS");
        ArrayList<NCCRSModel> modelArrayList = new ArrayList<NCCRSModel>();
        NCCRSModel nccrsModel = null;
        nccrsModel = new NCCRSModel();
        nccrsModel.setRegistId(registId);
        nccrsModel.setRegistType(RegistType.CompanyLimited);
        nccrsModel.setCompanyName(companyName);
        nccrsModel.setInqPurose(inqPurose);
        nccrsModel.setProductType(productType);
        nccrsModel.setMemberRef(memberRef);
        nccrsModel.setConfirmConsent(confirmConsent);
        nccrsModel.setLanguage(language);
        nccrsModel.setHistoricalBalanceReport(historicalBalanceReport);
        log.debug("Model : {}", nccrsModel.toString());
        modelArrayList.add(nccrsModel);

        nccrsModel = new NCCRSModel();
        nccrsModel.setRegistId("0115530002071");
        nccrsModel.setRegistType(RegistType.CompanyLimited);
        nccrsModel.setCompanyName(companyName);
        nccrsModel.setInqPurose(inqPurose);
        nccrsModel.setProductType(productType);
        nccrsModel.setMemberRef(memberRef);
        nccrsModel.setConfirmConsent(confirmConsent);
        nccrsModel.setLanguage(language);
        nccrsModel.setHistoricalBalanceReport(historicalBalanceReport);
        log.debug("Model : {}", nccrsModel.toString());
        modelArrayList.add(nccrsModel);

        nccrsModel = new NCCRSModel();
        nccrsModel.setRegistId("0105532021260");
        nccrsModel.setRegistType(RegistType.CompanyLimited);
        nccrsModel.setCompanyName(companyName);
        nccrsModel.setInqPurose(inqPurose);
        nccrsModel.setProductType(productType);
        nccrsModel.setMemberRef(memberRef);
        nccrsModel.setConfirmConsent(confirmConsent);
        nccrsModel.setLanguage(language);
        nccrsModel.setHistoricalBalanceReport(historicalBalanceReport);
        log.debug("Model : {}", nccrsModel.toString());
        modelArrayList.add(nccrsModel);

        NCCRSInputModel inputModel = new NCCRSInputModel("55555", "0123456789012345", CANumber, referenceTel, modelArrayList);
        try {
            ArrayList<NCCRSOutputModel> nccrsOutputModelArrayList = ncbInterface.request(inputModel);
            for (NCCRSOutputModel nccrsOutputModel : nccrsOutputModelArrayList) {
                log.info("NCCRS response : {}", nccrsOutputModel.toString());
            }
        } catch (Exception e) {
            log.info("NCCRS Exception : {}", e.getMessage());
        }
    }

    public void onClickCallComS(){

        try{                                                                           //10001
             AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(userIdForComS, jobId);
            if(!Util.isNull(appraisalDataResult) && ActionResult.SUCCESS.equals(appraisalDataResult.getActionResult())){
                newCollateralView = collateralBizTransform.transformCollateral(appraisalDataResult);
                result = newCollateralView.toString();
            } else {
                result = "FAILED";
            }
        } catch (COMSInterfaceException e) {
            log.error("-- COMSInterfaceException : {}", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }
    }

    public void onClickCallECM(){
        log.debug("-- onClickCallECM()");
        System.out.println("-- onClickCallECM");

        log.debug("----------------------------");
        String date = Util.createDateTh(new Date());
        log.debug("--date {}",date);
        String[] month = date.split("");
        log.debug("--month {}",month);

        try{
            ECMDataResult ecmDataResult = ecmInterface.getECMDataResult(caNumberECM);
            if(!Util.isNull(ecmDataResult) && ActionResult.SUCCESS.equals(ecmDataResult.getActionResult())){
                List<ECMDetail> ecmDetailList = ecmDataResult.getEcmDetailList();
                for (ECMDetail ecmDetail : ecmDetailList) {
                    log.debug("-- ECMDetail : [{}]", ecmDetail.toString());
                    System.out.println("-- ECMDetail "+ ecmDetail.toString());
                }
                result = ecmDetailList.toString();
            } else {
                result = "FAILED";
            }
        } catch (ECMInterfaceException e) {
            log.error("-- ECMInterfaceException : {}", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }
    }

    public void onClickECMCAPShareUpdate(){
        log.debug("-- onClickECMCAPShare()");
        ECMCAPShare ecmcapShare = null;
        try{
            ecmcapShare = new ECMCAPShare();
            ecmcapShare.setCrsUKCANumber(appNumber);
            ecmcapShare.setCrsCancelCA("Y");
            ecmcapShare.setCrsLastUpdate(new java.sql.Date(new Date().getTime()));
            log.debug("-- LastUpdate DATE[{}]", ecmcapShare.getCrsLastUpdate());

            log.debug("Model [{}]", ecmcapShare.toString());
            if(ecmInterface.update(ecmcapShare)){
                result = "SUCCESS";
            } else {
                result = "FAILED";
            }
        } catch (ECMInterfaceException e) {
            log.error("-- ECMInterfaceException : {}", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }
    }

    public void onClickECMCAPShare(){
        log.debug("-- onClickECMCAPShare()");
        ECMCAPShare ecmcapShare = null;
        try{
            ecmcapShare = new ECMCAPShare();
            ecmcapShare.setCrsUKCANumber(appNumber);
            ecmcapShare.setCrsCustName(crsCustName);
            ecmcapShare.setCrsCreateDate(new java.sql.Date(new Date().getTime()));
            log.debug("-- Create DATE[{}]", ecmcapShare.getCrsCreateDate());
            ecmcapShare.setCrsLastUpdate(new java.sql.Date(new Date().getTime()));
            log.debug("-- LastUpdate DATE[{}]", ecmcapShare.getCrsLastUpdate());

            log.debug("Model [{}]", ecmcapShare.toString());
            if(ecmInterface.insert(ecmcapShare)){
                result = "SUCCESS";
            } else {
                result = "FAILED";
            }
        } catch (ECMInterfaceException e) {
            log.error("-- ECMInterfaceException : {}", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }
    }

    public void onClickCallBRMS(){
        log.debug("-- onClickCallBRMS()");
        log.debug("-- workCaseId is {}", workCaseId);
        MandateDocView mandateDocView = null;
        try{
            MandateDocResponseView mandateDocResponseView = brmsControl.getDocCustomer(workCaseId);
            if(!Util.isNull(mandateDocResponseView) && ActionResult.SUCCESS.equals(mandateDocResponseView.getActionResult())){
                log.debug("-- ActionResult = ", mandateDocResponseView.getActionResult());
                Map<String, MandateDocView> mandateDocViewMap =  mandateDocResponseView.getMandateDocViewMap();
                if(!Util.isNull(mandateDocViewMap)){
                    for ( String key : mandateDocViewMap.keySet() ) {
                        log.debug("-- Key is {}", key);
                        log.debug("-- Got value from key {} value is {}", key, mandateDocViewMap.get(key).toString());
                        mandateDocView = mandateDocViewMap.get(key);
                        if(!Util.isNull(mandateDocView)){
                            log.debug("-- EcmDocTypeId = {}", mandateDocView.getEcmDocTypeId());
                            log.debug("-- DocLevel     = {}", mandateDocView.getDocLevel().value());
                            List<String> brmsList = Util.safetyList(mandateDocView.getBrmsDescList());
                            for(String s : brmsList){
                                log.debug("-- BrmsDesc     = {}", s);
                            }
                            List<CustomerInfoSimpleView> customerInfoSimpleViewList = Util.safetyList(mandateDocView.getCustomerInfoSimpleViewList());
                            for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
                                log.debug("-- CustomerInfoSimpleView = {}", customerInfoSimpleView.toString());
                            }
                        } else {
                            log.debug("-- MandateDocView is null");
                        }
                    }
                } else {
                    log.debug("-- Map is null.");
                }
                result = mandateDocResponseView.toString();
            } else {
                result = "FAILED";
            }
        } catch (Exception e) {
            System.err.println("Exception = "+e.getMessage());
            System.err.println("Exception = "+e);
            result = e.getMessage();
        }
    }


    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public String getUserIdForComS() {
        return userIdForComS;
    }

    public void setUserIdForComS(String userIdForComS) {
        this.userIdForComS = userIdForComS;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public void setAppRefNumber(String appRefNumber) {
        this.appRefNumber = appRefNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMemberref() {
        return memberref;
    }

    public String getCaNumberECM() {
        return caNumberECM;
    }

    public void setCaNumberECM(String caNumberECM) {
        this.caNumberECM = caNumberECM;
    }

    public void setMemberref(String memberref) {
        this.memberref = memberref;
    }

    public String getEnqpurpose() {
        return enqpurpose;
    }

    public void setEnqpurpose(String enqpurpose) {
        this.enqpurpose = enqpurpose;
    }

    public String getEnqamount() {
        return enqamount;
    }

    public void setEnqamount(String enqamount) {
        this.enqamount = enqamount;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getDisputeenquiry() {
        return disputeenquiry;
    }

    public void setDisputeenquiry(String disputeenquiry) {
        this.disputeenquiry = disputeenquiry;
    }

    public String getRegistType() {
        return registType;
    }

    public void setRegistType(String registType) {
        this.registType = registType;
    }

    public String getRegistId() {
        return registId;
    }

    public void setRegistId(String registId) {
        this.registId = registId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInqPurose() {
        return inqPurose;
    }

    public void setInqPurose(String inqPurose) {
        this.inqPurose = inqPurose;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getMemberRef() {
        return memberRef;
    }

    public void setMemberRef(String memberRef) {
        this.memberRef = memberRef;
    }

    public String getConfirmConsent() {
        return confirmConsent;
    }

    public void setConfirmConsent(String confirmConsent) {
        this.confirmConsent = confirmConsent;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHistoricalBalanceReport() {
        return historicalBalanceReport;
    }

    public void setHistoricalBalanceReport(String historicalBalanceReport) {
        this.historicalBalanceReport = historicalBalanceReport;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumber2() {
        return idNumber2;
    }

    public void setIdNumber2(String idNumber2) {
        this.idNumber2 = idNumber2;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getCrsCustName() {
        return crsCustName;
    }

    public void setCrsCustName(String crsCustName) {
        this.crsCustName = crsCustName;
    }
}
