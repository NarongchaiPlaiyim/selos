package com.clevel.selos.controller;

import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.view.CheckMandatoryDocView;
import com.clevel.selos.model.view.CheckOptionalDocView;
import com.clevel.selos.model.view.CheckOtherDocView;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    @Config(name = "interface.workplace.address")
    private String address;
    @Inject
    @Config(name = "interface.mandate.doc.objectStore")
    private String objectStore;
    @Inject
    @Config(name = "interface.workplace.username")
    private String username;
    @Inject
    @Config(name = "interface.workplace.password")
    private String password;
    @Inject
    @Config(name = "system.encryption.enable")
    private String encryptionEnable;
    @Inject
    private EncryptionService encryptionService;
    @Inject
    private CESessionToken CESessionToken;
    private String passwordEncrypt;

    private List<CheckMandatoryDocView> mandatoryDocumentsList;
    private List<CheckOptionalDocView> optionalDocumentsList;
    private List<CheckOtherDocView> otherDocumentsList;

    private int rowIndex;
    private String messageHeader;
    private String message;

    @Inject
    private ECMInterface ecmInterface;
    @Inject
    private BPMInterfaceImpl bpmInterface;

    public CheckMandateDoc() {
        init();
    }

    private void init(){
        mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView checkMandatoryDocView = new CheckMandatoryDocView();
        checkMandatoryDocView.setDocumentType("6.8.11 Document Ttype");
        checkMandatoryDocView.setBRMSDocument("Citizen ID");
        checkMandatoryDocView.setOwners("Mr. Narongchai");
        checkMandatoryDocView.setFileName("File_2013107_1501");
        checkMandatoryDocView.setDisabledCompleteFlag(true);
        checkMandatoryDocView.setComplete(1);
        checkMandatoryDocView.setIndistinct(true);
        checkMandatoryDocView.setRemark("test");
        mandatoryDocumentsList.add(checkMandatoryDocView);

        checkMandatoryDocView = new CheckMandatoryDocView();
        checkMandatoryDocView.setDocumentType("6.9.3 Statement");
        checkMandatoryDocView.setBRMSDocument("Citizen ID");
        checkMandatoryDocView.setOwners("Mr. Narongchai2");
        checkMandatoryDocView.setFileName("File_2013107_1502");
        checkMandatoryDocView.setComplete(3);
        checkMandatoryDocView.setIndistinct(true);
        checkMandatoryDocView.setExpire(true);
        checkMandatoryDocView.setRemark("test");
        mandatoryDocumentsList.add(checkMandatoryDocView);

        optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView checkOptionalDocView = new CheckOptionalDocView();
        checkOptionalDocView.setDocumentType("6.11.2 Statement");
        checkOptionalDocView.setOwners("Application");
        checkOptionalDocView.setFileName("File_2013107_1502");
        checkOptionalDocView.setComplete(2);
        checkOptionalDocView.setIndistinct(true);
        checkOptionalDocView.setExpire(true);
        checkOptionalDocView.setRemark("test");
        optionalDocumentsList.add(checkOptionalDocView);

        checkOptionalDocView = new CheckOptionalDocView();
        checkOptionalDocView.setDocumentType("X.X.XXXX StatementXXXXX");
        checkOptionalDocView.setBRMSDocument("XXXXXXXXXXX");
        checkOptionalDocView.setOwners("Application");
        checkOptionalDocView.setFileName("File_2013107_1502");
        checkOptionalDocView.setComplete(2);
        checkOptionalDocView.setIndistinct(true);
        checkOptionalDocView.setExpire(true);
        checkOptionalDocView.setRemark("test");
        optionalDocumentsList.add(checkOptionalDocView);
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
        if (Util.isTrue(encryptionEnable)) {
            passwordEncrypt = encryptionService.decrypt(Base64.decodeBase64(password));
        } else {
            passwordEncrypt = password;
        }
        log.debug("-- User = {}", username);
        log.debug("-- Password = {}", password);
        log.debug("-- Password[Decrypt] = {}", passwordEncrypt);
        otherDocumentsList = new ArrayList<CheckOtherDocView>();
        String result = null;
        try{
            ECMDataResult ecmDataResult = ecmInterface.getECMDataResult("11111111111111111111");
            if(!Util.isNull(ecmDataResult) && ActionResult.SUCCESS.equals(ecmDataResult.getActionResult())){
                List<ECMDetail> ecmDetailList = Util.safetyList(ecmDataResult.getEcmDetailList());
                CheckOtherDocView checkOtherDocView = null;
                String userToken = CESessionToken.getTokenFromSession(username, passwordEncrypt);
                log.debug("-- User Token {}", userToken);
                for (ECMDetail ecmDetail : ecmDetailList) {
                    checkOtherDocView = new CheckOtherDocView();
                    checkOtherDocView.setDocumentType(ecmDetail.getTypeNameTH());
                    checkOtherDocView.setOwners(ecmDetail.getTypeCode());
                    checkOtherDocView.setFileName(ecmDetail.getOrgFileName());
                    result = getURLByFNId(ecmDetail.getFnDocId(), userToken);
                    checkOtherDocView.setLink(result);
                    checkOtherDocView.setComplete(2);
                    checkOtherDocView.setIndistinct(true);
                    checkOtherDocView.setExpire(true);
                    checkOtherDocView.setRemark("test");
                    log.debug("-- Link[{}]", result);
                    otherDocumentsList.add(checkOtherDocView);
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

    public void onSaveCheckMandateDoc(){

    }

    public void onCancelCheckMandateDoc(){

    }


    private String getURLByFNId(final String FNId, final String token){
        return address+"/getContent?objectStoreName="+objectStore+"&id="+FNId+"&objectType=document&ut=" + token;
    }


    public List<CheckMandatoryDocView> getMandatoryDocumentsList() {
        return mandatoryDocumentsList;
    }

    public void setMandatoryDocumentsList(List<CheckMandatoryDocView> mandatoryDocumentsList) {
        this.mandatoryDocumentsList = mandatoryDocumentsList;
    }

    public List<CheckOptionalDocView> getOptionalDocumentsList() {
        return optionalDocumentsList;
    }

    public void setOptionalDocumentsList(List<CheckOptionalDocView> optionalDocumentsList) {
        this.optionalDocumentsList = optionalDocumentsList;
    }

    public List<CheckOtherDocView> getOtherDocumentsList() {
        return otherDocumentsList;
    }

    public void setOtherDocumentsList(List<CheckOtherDocView> otherDocumentsList) {
        this.otherDocumentsList = otherDocumentsList;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
