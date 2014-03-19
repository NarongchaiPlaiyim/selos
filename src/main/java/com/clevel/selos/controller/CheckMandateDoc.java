package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CheckMandateDocControl;
import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.view.CheckMandateDocView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    private CheckMandateDocControl checkMandateDocControl;
    private CheckMandateDocView checkMandateDocView;
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
        checkMandateDocView = new CheckMandateDocView();
    }

    @PostConstruct
    public void onCreation() {

        log.info("-- onCreation.");
//        if (Util.isTrue(encryptionEnable)) {
//            passwordEncrypt = encryptionService.decrypt(Base64.decodeBase64(password));
//        } else {
//            passwordEncrypt = password;
//        }
//        log.debug("-- User = {}", username);
//        log.debug("-- Password = {}", password);
//        log.debug("-- Password[Decrypt] = {}", passwordEncrypt);
//        otherDocumentsList = new ArrayList<CheckOtherDocView>();
        String result = null;
        String caNumber = "11111111111111111111";

        try{
            //Calling ECM
            ECMDataResult ecmDataResult = ecmInterface.getECMDataResult(caNumber);



            if(!Util.isNull(ecmDataResult) && ActionResult.SUCCESS.equals(ecmDataResult.getActionResult())){
                List<ECMDetail> ecmDetailList = Util.safetyList(ecmDataResult.getEcmDetailList());
                CheckOtherDocView checkOtherDocView = null;
//                String userToken = CESessionToken.getTokenFromSession(username, passwordEncrypt);
//                log.debug("-- User Token {}", userToken);

                Map<String,List<ECMDetail>> ecmMap = new HashMap<String, List<ECMDetail>>();


                for (ECMDetail ecmDetail : ecmDetailList) {

                    if(ecmMap.containsKey(ecmDetail.getEcmDocId())){
                        List<ECMDetail> ecmListTmp = ecmMap.get(ecmDetail.getEcmDocId());
                        ecmListTmp.add(ecmDetail);
                        ecmMap.put(ecmDetail.getEcmDocId(),ecmListTmp);
                    } else {
                        List<ECMDetail> ecmListTmp = new ArrayList<ECMDetail>();
                        ecmListTmp.add(ecmDetail);
                        ecmMap.put(ecmDetail.getEcmDocId(),ecmListTmp);
                    }



                    checkOtherDocView = new CheckOtherDocView();
                    checkOtherDocView.setDocumentType(ecmDetail.getTypeNameTH());
                    checkOtherDocView.setOwners(ecmDetail.getTypeCode());
                    checkOtherDocView.setFileName(ecmDetail.getOrgFileName());
//                    result = getURLByFNId(ecmDetail.getFnDocId(), userToken);
                    checkOtherDocView.setLink(result);
                    checkOtherDocView.setComplete(2);
                    checkOtherDocView.setIndistinct(true);
                    checkOtherDocView.setExpire(true);
                    checkOtherDocView.setRemark("test");
                    log.debug("-- Link[{}]", result);
//                    otherDocumentsList.add(checkOtherDocView);
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

    public CheckMandateDocView getCheckMandateDocView() {
        return checkMandateDocView;
    }

    public void setCheckMandateDocView(CheckMandateDocView checkMandateDocView) {
        this.checkMandateDocView = checkMandateDocView;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
