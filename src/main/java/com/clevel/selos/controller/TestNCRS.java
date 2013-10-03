package com.clevel.selos.controller;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.service.NCCRSService;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncb.ncrs.service.NCRSService;
import com.clevel.selos.integration.ncbi.service.NCBIImp;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

@ViewScoped
@ManagedBean(name="TestNCRS")
public class TestNCRS implements Serializable {

    @Inject
    @NCB
    Logger log;

    @Inject
    NCRSService ncrsService;

    @Inject
    NCCRSService nccrsService;

    @Inject
    NCBIImp ncbiImp;
    //NCRS
    private String result;
    private String memberref = "123456789";
    private String enqpurpose = "01";
    private String enqamount = "01";
    private String consent = "Y";
    private String disputeenquiry;

    private String idNumber = "3100300390029";
    private String idNumber2= "3100504308002";
    private String appRefNumber = "0420140301000101";
    private String userId = "SERID";
    private String CANumber ="01234567890123456789";
    private String referenceTel = "0800000000";

    //NCCRS
    private String registType ="1140002";
    private String registId ="1000000000001";
    private String companyName ="ทดสอบสอง";
    private String inqPurose ="1170001";
    private String productType ="2030001";
    private String memberRef ="TEST20120918";
    private String confirmConsent ="Y";
    private String language ="2060002";
    private String historicalBalanceReport ="Y";

    @Inject
    public TestNCRS() {
    }

    public void onClickNCRS(){
        log.info("========================================= onClickNCRS");
        NCRSModel ncrsModel = null;
        ArrayList<NCRSModel> ncrsModelArrayList = new ArrayList<NCRSModel>();

        ncrsModel = new NCRSModel();
        ncrsModel.setIdTypeCitizen();
        ncrsModel.setCitizenId(idNumber);
        ncrsModel.setFirstName("พสุธร");
        ncrsModel.setLastName("กุญชร");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdTypeCitizen();
        ncrsModel.setCitizenId(idNumber2);
        ncrsModel.setFirstName("ขวัญชัย");
        ncrsModel.setLastName("อานุภาพ");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdTypeCitizen();
        ncrsModel.setCitizenId("3101403233750");
        ncrsModel.setFirstName("ประดิษฐ์");
        ncrsModel.setLastName("ภัทรประสิทธิ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        ncrsModel = new NCRSModel();
        ncrsModel.setIdTypeCitizen();
        ncrsModel.setCitizenId("3149900124956");
        ncrsModel.setFirstName("วิลาวัณย ");
        ncrsModel.setLastName("อุปริกชาติพงษ์");
        ncrsModel.setMemberref(memberref);
        ncrsModel.setEnqpurpose(enqpurpose);
        ncrsModel.setEnqamount(enqamount);
        ncrsModel.setConsent(consent);
        ncrsModelArrayList.add(ncrsModel);

        NCRSInputModel inputModel = new NCRSInputModel(userId, appRefNumber, CANumber, referenceTel, ncrsModelArrayList);
        //ncrsService.process(inputModel);
        ncbiImp.process();
    }
    public void onClickNCCRS(){
        log.info("========================================= onClickNCCRS");
        ArrayList<NCCRSModel> modelArrayList = new ArrayList<NCCRSModel>();
        NCCRSModel nccrsModel = null;
        nccrsModel = new NCCRSModel();
        nccrsModel.setRegistId(registId);
        nccrsModel.setRegistTypeCompanyLimited();
        nccrsModel.setCompanyName(companyName);
        nccrsModel.setInqPurose(inqPurose);
        nccrsModel.setProductType(productType);
        nccrsModel.setMemberRef(memberRef);
        nccrsModel.setConfirmConsent(confirmConsent);
        nccrsModel.setLanguage(language);
        nccrsModel.setHistoricalBalanceReport(historicalBalanceReport);
        log.debug("_______________________________________ Model : ", nccrsModel.toString());
        modelArrayList.add(nccrsModel);

        nccrsModel = new NCCRSModel();
        nccrsModel.setRegistId("0115530002071");
        nccrsModel.setRegistTypeCompanyLimited();
        nccrsModel.setCompanyName(companyName);
        nccrsModel.setInqPurose(inqPurose);
        nccrsModel.setProductType(productType);
        nccrsModel.setMemberRef(memberRef);
        nccrsModel.setConfirmConsent(confirmConsent);
        nccrsModel.setLanguage(language);
        nccrsModel.setHistoricalBalanceReport(historicalBalanceReport);
        log.debug("_______________________________________ Model : ", nccrsModel.toString());
        modelArrayList.add(nccrsModel);

        nccrsModel = new NCCRSModel();
        nccrsModel.setRegistId("0105532021260");
        nccrsModel.setRegistTypeCompanyLimited();
        nccrsModel.setCompanyName(companyName);
        nccrsModel.setInqPurose(inqPurose);
        nccrsModel.setProductType(productType);
        nccrsModel.setMemberRef(memberRef);
        nccrsModel.setConfirmConsent(confirmConsent);
        nccrsModel.setLanguage(language);
        nccrsModel.setHistoricalBalanceReport(historicalBalanceReport);
        log.debug("_______________________________________ Model : ", nccrsModel.toString());
        modelArrayList.add(nccrsModel);

        NCCRSInputModel inputModel = new NCCRSInputModel("55555", "0123456789012345", CANumber, referenceTel, modelArrayList);
        nccrsService.process(inputModel);
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
}
