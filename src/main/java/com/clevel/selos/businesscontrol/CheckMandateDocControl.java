package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.MandateDocDAO;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.*;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.transform.CheckMandateDocTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class CheckMandateDocControl extends BusinessControl{
    @Inject
    @NCB
    private Logger log;
    @Inject
    private MandateDocDAO mandateDocDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    private BasicInfo basicInfo;
    @Inject
    private ECMInterface ecmInterface;
    private ECMDataResult ecmDataResult;
    @Inject
    private BRMSControl brmsControl;
    private MandateDocResponseView mandateDocResponseView;
    @Inject
    private CheckMandateDocTransform checkMandateDocTransform;

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
    private com.clevel.selos.integration.filenet.ce.connection.CESessionToken CESessionToken;
    private String passwordEncrypt;
    private String userToken;

    private Map<String, MandateDocView> mandateDocViewMap;
    private MandateDocView mandateDocView;
    private Map<String,List<ECMDetail>> listECMDetailMap;
    private List<ECMDetail> ecmDetailList;
    @Inject
    public CheckMandateDocControl() {
        init();
    }

    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
        mandateDoc = null;
        mandateDocResponseView = null;
        basicInfo = null;
    }

    public CheckMandateDocView  getMandateDocView(final long workCaseId) throws Exception{
        log.info("-- getMandateDoc WorkCaseId : {}", workCaseId);
        init();

        //DB
        mandateDoc = mandateDocDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(mandateDoc)){
            log.debug("-- MandateDoc.id[{}]", mandateDoc.getId());
        } else {
            log.debug("-- Find by work case id = {} MandateDoc is {}   ", workCaseId, mandateDoc);
            mandateDoc = new MandateDoc();
            log.debug("-- MandateDoc[New] created");
        }

        //BRMS
        mandateDocResponseView = brmsControl.getDocCustomer(workCaseId);
        if(!Util.isNull(mandateDocResponseView) && ActionResult.SUCCESS.equals(mandateDocResponseView.getActionResult())){
            log.debug("-- ActionResult is {}", ecmDataResult.getActionResult());
            mandateDocViewMap =  mandateDocResponseView.getMandateDocViewMap();
        } else {
            log.debug("-- Find by work case id = {} ActionResult is {} and reason is {}  ", workCaseId, mandateDocResponseView.getActionResult(), mandateDocResponseView.getReason());
        }

        //ECM
        //Getting basicInfo first and than get the CANumber for call ECM
        basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(basicInfo)){
            log.debug("-- BasicInfo.id[{}]", basicInfo.getId());
            log.debug("-- BasicInfo.CANumber[{}]", basicInfo.getCaNumber());
            ecmDataResult = ecmInterface.getECMDataResult(basicInfo.getCaNumber());
            if(!Util.isNull(ecmDataResult) && ActionResult.SUCCESS.equals(ecmDataResult.getActionResult())){
                log.debug("-- ActionResult is {}", ecmDataResult.getActionResult());
                List<ECMDetail> ecmDetailList = Util.safetyList(ecmDataResult.getEcmDetailList());
                if(!Util.isZero(ecmDetailList.size())){
                    log.debug("-- EcmDetailList.size()[{}]", ecmDetailList.size());
                    listECMDetailMap = createMapByECM(ecmDetailList);
                } else {
                    log.debug("-- EcmDetailList.size()[{}]", ecmDetailList.size());
                }
            } else {
                log.debug("-- Find by CA Number = {} ActionResult is {} and reason is {}  ", basicInfo.getCaNumber(), ecmDataResult.getActionResult(), ecmDataResult.getReason());
            }
        } else {
            log.debug("-- Find by work case id = {} BasicInfo is {}   ", workCaseId, basicInfo);
        }

        if(!Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            getToken();
            checkMap();
        } else {
            log.debug("-- MandateDocViewMap is {} ListECMDetailMap is {}", mandateDocViewMap, listECMDetailMap);
        }

        return checkMandateDocView;
    }

    private void checkMap(){
        log.debug("-- checkMap()");
        checkMandateDocView = new CheckMandateDocView();
        List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView checkMandatoryDocView = null;
        List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView optionalDocView = null;
        List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
        CheckOtherDocView checkOtherDocView = null;
        List<String> keyBRMSList = new ArrayList<String>();
        List<String> keyECMList = new ArrayList<String>();
        String keyBRMS = null;
        String keyECM = null;

        for (Map.Entry<String, MandateDocView> BRMSentry : mandateDocViewMap.entrySet()) {
            keyBRMS = BRMSentry.getKey();
            log.debug("-- The key of BRMS map is {}", keyBRMS);
            for (Map.Entry<String, List<ECMDetail>> ECMentry : listECMDetailMap.entrySet()) {
                keyECM = ECMentry.getKey();
                log.debug("-- The key of ECM map is {}", keyECM);
                if(keyBRMS.equals(keyECM)){
                    log.debug("-- Matched");
                    mandateDocView = (MandateDocView)BRMSentry.getValue();
                    if(!Util.isNull(mandateDocView)){
                        log.debug("-- MandateDocView is not null.");
                    }
                    ecmDetailList = Util.safetyList((List<ECMDetail>)ECMentry.getValue());
                    if(!Util.isZero(ecmDetailList.size())){
                        log.debug("-- Size of ecmDetailList more than zero.");
                    }
                    int isComplete = 2;
                    try {
                        if(mandateDocView.getNumberOfDoc() == ecmDetailList.size()){
                            isComplete = 1;
                        }
                    } catch (Exception e){
                        isComplete = 3;
                    }
                    log.debug("-- The NumberOfDoc of BRMS is {}", mandateDocView.getNumberOfDoc());
                    log.debug("-- The NumberOfDoc of ECM  is {}", ecmDetailList.size());
                    log.debug("-- IsCompleted {}", isComplete);

                    if(DocMandateType.MANDATE.value() == mandateDocView.getDocMandateType().value()){
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Mandatory Documents");
                        checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(mandateDocView, ecmDetailList, isComplete, userToken);
                        mandatoryDocumentsList.add(checkMandatoryDocView);
                    } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Optional Documents");
                        optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(mandateDocView, ecmDetailList, isComplete, userToken);
                        optionalDocumentsList.add(optionalDocView);
                    } else {
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Other Documents");
                        checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(mandateDocView, ecmDetailList, isComplete, userToken);
                        otherDocumentsList.add(checkOtherDocView);
                    }
                    keyBRMSList.add(keyBRMS);
                    keyECMList.add(keyECM);
                    break;
                } else {
                    continue;
                }
            }
        }

        for(String key : keyBRMSList){
            mandateDocViewMap.remove(key);
            log.debug("-- BRMS key {} was removed.", key);
        }
        for (Map.Entry<String, MandateDocView> BRMSentry : mandateDocViewMap.entrySet()) {
            mandateDocView = (MandateDocView)BRMSentry.getValue();
            if(DocMandateType.MANDATE.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Mandatory Documents");
                checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(mandateDocView, 3);
                mandatoryDocumentsList.add(checkMandatoryDocView);
            } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Optional Documents");
                optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(mandateDocView, 3);
                optionalDocumentsList.add(optionalDocView);
            } else {
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Other Documents");
                checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(mandateDocView, 3);
                otherDocumentsList.add(checkOtherDocView);
            }
        }

        for(String key : keyECMList){
            listECMDetailMap.remove(key);
            log.debug("-- ECM key {} was removed.", key);
        }
        for (Map.Entry<String, List<ECMDetail>> ECMentry : listECMDetailMap.entrySet()) {
            ecmDetailList = Util.safetyList((List<ECMDetail>)ECMentry.getValue());
            log.debug("-- ECMDocType {} = {}.", ECMentry.getKey(), "Other Documents");
            checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(ecmDetailList, 3, userToken);
            otherDocumentsList.add(checkOtherDocView);
        }

        checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
        checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
        checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
    }

    private void onSaveMandateDoc(final CheckMandateDocView checkMandateDocView, final long workCaseId){
        log.info("-- onSaveMandateDoc ::: workCaseId : {}", workCaseId);
        checkMandateDocTransform.transformToModel(checkMandateDocView);
    }

    private Map<String,List<ECMDetail>> createMapByECM(final List<ECMDetail> ecmDetailList){
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
        }
        return ecmMap;
    }

    private void getToken() {
        if (Util.isTrue(encryptionEnable)) {
            passwordEncrypt = encryptionService.decrypt(Base64.decodeBase64(password));
        } else {
            passwordEncrypt = password;
        }
        try {
            userToken = CESessionToken.getTokenFromSession(username, passwordEncrypt);
        } catch (Exception e) {
            log.error("-- Error while get Token reason is {}", e.getMessage());
        }
    }

}