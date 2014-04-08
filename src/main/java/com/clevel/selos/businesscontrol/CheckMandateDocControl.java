package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.*;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.transform.CheckMandateDocTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
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
    private CESessionToken CESessionToken;
    @Inject
    private MandateDocFileNameDAO mandateDocFileNameDAO;
    @Inject
    private MandateDocBRMSDAO mandateDocBRMSDAO;
    @Inject
    private MandateDocCustDAO mandateDocCustDAO;

    private String passwordEncrypt;
    private String userToken;
    private Map<String, MandateDocView> mandateDocViewMap;
    private MandateDocView mandateDocView;
    private Map<String,List<ECMDetail>> listECMDetailMap;
    private List<ECMDetail> ecmDetailList;

    private final String ABDM = "ABDM";
    private final String BDM = "BDM";
    private final String UW = "UW";

    private final String SYSTEM = "SYSTEM";
    private final String ISA = "ISA";
    private final String ZM = "ZM";
    private final String RGM = "RGM";
    private final String GH = "GH";
    private final String CSSO = "CSSO";
    private final String AAD_ADMIN = "AAD ADMIN";
    private final String AAD_COMMITTEE = "AAD COMMITTEE";
    private User user;
    @Inject
    public CheckMandateDocControl() {
//        init();
    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
        mandateDoc = null;
        mandateDocResponseView = null;
        basicInfo = null;
    }

    public CheckMandateDocView  getMandateDocViewByWorkCasePreScreenId(final long workCasePreScreenId){
        log.debug("-- getMandateDocViewByWorkCasePreScreenId WorkCasePreScreenId : {}", workCasePreScreenId);
        try {
            mandateDocResponseView = brmsControl.getDocCustomerForPrescreen(workCasePreScreenId);
            if(!Util.isNull(mandateDocResponseView) && ActionResult.SUCCESS.equals(mandateDocResponseView.getActionResult())){
                log.debug("-- ActionResult is {}", mandateDocResponseView.getActionResult());
                checkMandateDocView = new CheckMandateDocView();
                List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
                CheckMandatoryDocView checkMandatoryDocView = null;
                List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
                CheckOptionalDocView optionalDocView = null;
                List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
                CheckOtherDocView checkOtherDocView = null;

                mandateDocViewMap =  mandateDocResponseView.getMandateDocViewMap();
                log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());

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

                //to update userToken for open The document.
                getToken();
                log.debug("-- UserToken[{}]", userToken);
                for(CheckMandatoryDocView view : mandatoryDocumentsList){
                    List<MandateDocFileNameView> fileNameViewList = Util.safetyList(view.getFileNameViewList());
                    for(MandateDocFileNameView fileNameView : fileNameViewList){
                        fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                    }
                }
                for(CheckOptionalDocView view : optionalDocumentsList){
                    List<MandateDocFileNameView> fileNameViewList = Util.safetyList(view.getFileNameViewList());
                    for(MandateDocFileNameView fileNameView : fileNameViewList){
                        fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                    }
                }
                for(CheckOtherDocView view : otherDocumentsList){
                    List<MandateDocFileNameView> fileNameViewList = Util.safetyList(view.getFileNameViewList());
                    for(MandateDocFileNameView fileNameView : fileNameViewList){
                        fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                    }
                }

                checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
                log.debug("-- MandatoryDocumentsList.size()[{}] added to CheckMandateDocView", mandatoryDocumentsList.size());
                checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
                log.debug("-- OptionalDocumentsList.size()[{}] added to CheckMandateDocView", optionalDocumentsList.size());
                checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
                log.debug("-- OtherDocumentsList.size()[{}] added to CheckMandateDocView", otherDocumentsList.size());
            } else {
                log.debug("-- Find by work pre screen case id = {} ActionResult is {} and reason is {}  ", workCasePreScreenId, mandateDocResponseView.getActionResult(), mandateDocResponseView.getReason());
            }
        } catch (Exception e){
            log.error("-- Exception while call BRMS {}", e.getMessage());
//            throw new Exception(e);
        }
        return checkMandateDocView;
    }

    public CheckMandateDocView  getMandateDocView(final long workCaseId) throws Exception{
        log.debug("-- getMandateDoc WorkCaseId : {}", workCaseId);

        int roleId = 0;
        user = getCurrentUser();
        log.debug("-- User.id[{}]", user.getId());
        if(!Util.isNull(user)){
            roleId = user.getRole().getId();
            log.debug("-- User.Role.id[{}]", roleId);
            if( SYSTEM.equalsIgnoreCase(user.getRole().getName())       ||
                    ISA.equalsIgnoreCase(user.getRole().getName())      ||
                    ZM.equalsIgnoreCase(user.getRole().getName())       ||
                    RGM.equalsIgnoreCase(user.getRole().getName())      ||
                    GH.equalsIgnoreCase(user.getRole().getName())       ||
                    CSSO.equalsIgnoreCase(user.getRole().getName())     ||
                    AAD_ADMIN.equalsIgnoreCase(user.getRole().getName())||
                    AAD_COMMITTEE.equalsIgnoreCase(user.getRole().getName())
                    ){
                //view [DB]
                log.debug("-- {} Role[read]", user.getRole().getName());
                return getObjectFromDB(workCaseId, roleId);
            }
        }

        //ECM
        log.debug("-- ECM");
        try {
            basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if(!Util.isNull(basicInfo)){
                log.debug("-- BasicInfo.id[{}]", basicInfo.getId());
                log.debug("-- BasicInfo.CANumber[{}]", basicInfo.getCaNumber());
                ecmDataResult = ecmInterface.getECMDataResult(basicInfo.getCaNumber());
                if(!Util.isNull(ecmDataResult) && ActionResult.SUCCESS.equals(ecmDataResult.getActionResult())){
                    log.debug("-- ActionResult is {}", ecmDataResult.getActionResult());
                    List<ECMDetail> ecmDetailList = Util.safetyList(ecmDataResult.getEcmDetailList());
                    if(!Util.isZero(ecmDetailList.size())){
                        log.debug("-- ECM EcmDetailList.size()[{}]", ecmDetailList.size());
                        createMapByECM(ecmDetailList);
                        log.debug("-- ECM ListECMDetailMap.size.[{}])", listECMDetailMap.size());
                    } else {
                        log.debug("-- EcmDetailList.size()[{}]", ecmDetailList.size());
                    }
                } else {
                    log.debug("-- Find by CA Number = {} ActionResult is {} and reason is {}  ", basicInfo.getCaNumber(), ecmDataResult.getActionResult(), ecmDataResult.getReason());
                }
            } else {
                log.debug("-- Find by work case id = {} BasicInfo is {}   ", workCaseId, basicInfo);
            }
        } catch (Exception e){
            log.error("-- Exception while call ECM {}", e);
        }

        //BRMS
        log.debug("-- BRMS");
        try {
            mandateDocResponseView = brmsControl.getDocCustomerForFullApp(workCaseId);
            if(!Util.isNull(mandateDocResponseView) && ActionResult.SUCCESS.equals(mandateDocResponseView.getActionResult())){
                log.debug("-- ActionResult is {}", ecmDataResult.getActionResult());
                mandateDocViewMap =  mandateDocResponseView.getMandateDocViewMap();
                log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
            } else {
                log.debug("-- Find by work case id = {} ActionResult is {} and reason is {}  ", workCaseId, mandateDocResponseView.getActionResult(), mandateDocResponseView.getReason());
            }
        } catch (Exception e){
            log.error("-- Exception while call BRMS {}", e.getMessage());
//            throw new Exception(e);
        }

        if(!Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            getToken();
            log.debug("-- UserToken = {}", userToken);
            log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
            log.debug("-- ECM ListECMDetailMap.size()[{}]", listECMDetailMap.size());
            checkMap();
        } else {
            log.debug("-- MandateDocViewMap is {} and ListECMDetailMap is {}", mandateDocViewMap, listECMDetailMap);
        }

        if(ABDM.equalsIgnoreCase(user.getRole().getName())){
            //view [ABDM]
            log.debug("-- {} Role[read]", ABDM);
            checkMandateDocView.setReasonFlag(true);
            checkMandateDocView.setCompleteFlag(true);
            checkMandateDocView.setRemarkFlag(true);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        } else if(BDM.equalsIgnoreCase(user.getRole().getName())){
            //view [BDM]
            log.debug("-- {} Role[read]", BDM);
            checkMandateDocView.setReasonFlag(true);
            checkMandateDocView.setCompleteFlag(true);
            checkMandateDocView.setRemarkFlag(true);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        } else if(UW.equalsIgnoreCase(user.getRole().getName())){
            //view or edit [UW Edit]
            log.debug("-- {} Role[read or edit]", UW);
            checkMandateDocView.setReasonFlag(false);
            checkMandateDocView.setCompleteFlag(false);
            checkMandateDocView.setRemarkFlag(false);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        }
        return checkMandateDocView;
    }

    private CheckMandateDocView getObjectFromDB(final long workCaseId,final  int roleId){
        log.debug("-- getObjectFromDB(workCaseId {}, roleId {})", workCaseId, roleId);
        List<MandateDoc> mandateDocList = Util.safetyList(mandateDocDAO.findByWorkCaseIdAndRoleForReturn(workCaseId, roleId));
        if(!Util.isZero(mandateDocList.size())){
            log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
            checkMandateDocView = new CheckMandateDocView();
            checkMandateDocView = checkMandateDocTransform.transformToView(mandateDocList);
            getToken();
            log.debug("-- UserToken[{}]", userToken);
            List<CheckMandatoryDocView> mandatoryDocumentsList = checkMandateDocView.getMandatoryDocumentsList();
            for(CheckMandatoryDocView view : mandatoryDocumentsList){
                List<MandateDocFileNameView> fileNameViewList = view.getFileNameViewList();
                for(MandateDocFileNameView fileNameView : fileNameViewList){
                    fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                }
            }
            List<CheckOptionalDocView> optionalDocumentsList = checkMandateDocView.getOptionalDocumentsList();
            for(CheckOptionalDocView view : optionalDocumentsList){
                List<MandateDocFileNameView> fileNameViewList = view.getFileNameViewList();
                for(MandateDocFileNameView fileNameView : fileNameViewList){
                    fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                }
            }
            List<CheckOtherDocView> otherDocumentsList = checkMandateDocView.getOtherDocumentsList();
            for(CheckOtherDocView view : otherDocumentsList){
                List<MandateDocFileNameView> fileNameViewList = view.getFileNameViewList();
                for(MandateDocFileNameView fileNameView : fileNameViewList){
                    fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                }
            }

            checkMandateDocView.readOnly();
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
            return checkMandateDocView;
        } else {
            log.debug("-- Find by work case id = {} MandateDoc is {}   ", workCaseId, mandateDoc);
            return null;
        }
    }

    private String updateToken(String oldUrl){
        String newUrl = null;
        final String PARAM = "document&ut=";
        if(!Util.isNull(oldUrl) && !Util.isZero(oldUrl.length())){
            newUrl = oldUrl.substring(0, oldUrl.indexOf(PARAM))+PARAM+userToken;
            log.debug("-- [BEFORE] {}", oldUrl);
            log.debug("-- [AFTER] {}", newUrl);
            return newUrl;
        } else {
            return oldUrl;
        }
    }

    private void checkMap(){
        log.debug("-- checkMap start");
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

        log.debug("-- checkMap MandateDocViewMap.size[{}]", mandateDocViewMap.size());
        log.debug("-- checkMap ListECMDetailMap.size[{}]", listECMDetailMap.size());

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
                        if(isComplete == 1){
                            checkMandatoryDocView.readOnly();
                            checkMandatoryDocView.setCompleteFlag(true);
                            log.debug("-- CheckMandatoryDocView key[{}] readOnly", keyECM);
                        }
                        mandatoryDocumentsList.add(checkMandatoryDocView);
                    } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Optional Documents");
                        optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(mandateDocView, ecmDetailList, isComplete, userToken);
                        if(isComplete == 1){
                            optionalDocView.readOnly();
                            optionalDocView.setCompleteFlag(true);
                            log.debug("-- OptionalDocView key[{}] readOnly", keyECM);
                        }
                        optionalDocumentsList.add(optionalDocView);
                    } else {
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Other Documents");
                        checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(mandateDocView, ecmDetailList, isComplete, userToken);
                        if(isComplete == 1){
                            checkOtherDocView.readOnly();
                            checkOtherDocView.setCompleteFlag(true);
                            log.debug("-- CheckOtherDocView key[{}] readOnly", keyECM);
                        }
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
        log.debug("-- checkMap stop");

        log.debug("-- [BEFORE] Remove MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
        log.debug("-- KeyBRMSList.size()[{}]", keyBRMSList.size());
        for(String key : keyBRMSList){
            mandateDocViewMap.remove(key);
            log.debug("-- BRMS key {} was removed.", key);
        }
        log.debug("-- [AFTER] Remove MandateDocViewMap.size()[{}]", mandateDocViewMap.size());

        //BRMS
        for (Map.Entry<String, MandateDocView> BRMSentry : mandateDocViewMap.entrySet()) {
            mandateDocView = (MandateDocView)BRMSentry.getValue();
            if(DocMandateType.MANDATE.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Mandatory Documents");
                checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(mandateDocView, 2);
                mandatoryDocumentsList.add(checkMandatoryDocView);
            } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Optional Documents");
                optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(mandateDocView, 2);
                optionalDocumentsList.add(optionalDocView);
            } else {
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Other Documents");
                checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(mandateDocView, 2);
                otherDocumentsList.add(checkOtherDocView);
            }
        }
        log.debug("-- [BEFORE] Remove ListECMDetailMap.size()[{}]", listECMDetailMap.size());
        log.debug("-- KeyECMList.size()[{}]", keyECMList.size());
        for(String key : keyECMList){
            listECMDetailMap.remove(key);
            log.debug("-- ECM key {} was removed.", key);
        }
        log.debug("-- [AFTER] Remove ListECMDetailMap.size()[{}]", listECMDetailMap.size());

        //ECM
        for (Map.Entry<String, List<ECMDetail>> ECMentry : listECMDetailMap.entrySet()) {
            ecmDetailList = Util.safetyList((List<ECMDetail>)ECMentry.getValue());
            log.debug("-- ECMDocType {} = {}.", ECMentry.getKey(), "Other Documents");
            checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(ecmDetailList, 2, userToken);
            otherDocumentsList.add(checkOtherDocView);
        }
        checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
        log.debug("-- MandatoryDocumentsList.size()[{}] added to CheckMandateDocView", mandatoryDocumentsList.size());
        checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
        log.debug("-- OptionalDocumentsList.size()[{}] added to CheckMandateDocView", optionalDocumentsList.size());
        checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
        log.debug("-- OtherDocumentsList.size()[{}] added to CheckMandateDocView", otherDocumentsList.size());
    }

    public void onSaveMandateDoc(final CheckMandateDocView checkMandateDocView, final long workCaseId, final long workCasePreScreenId){
        List<MandateDoc> mandateDocList = null;
        if(!Util.isZero(workCaseId)){
            log.info("-- onSaveMandateDoc ::: workCaseId : {}", workCaseId);
            mandateDocList = Util.safetyList(mandateDocDAO.findByWorkCaseIdAndRole(workCaseId, user.getRole().getId()));
        } else {
            log.info("-- onSaveMandateDoc ::: workCasePreScreenId : {}", workCasePreScreenId);
            mandateDocList = Util.safetyList(mandateDocDAO.findByWorkCasePreScreenIdAndRole(workCasePreScreenId, user.getRole().getId()));
        }

        delete(mandateDocList);
        mandateDocList = null;
        mandateDocList = Util.safetyList(checkMandateDocTransform.transformToModel(checkMandateDocView, workCaseId, user.getRole()));
        save(mandateDocList);
    }

    private void save(final List<MandateDoc> mandateDocList){
        log.debug("-- save start");
        log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
        try {
            mandateDocDAO.persist(mandateDocList);
            log.debug("-- save stop");
        } catch (Exception e) {
            log.error("-- Exception wile save data reason is {}", e.getMessage());
        }
    }

    private void delete(final List<MandateDoc> mandateDocList){
        log.debug("-- delete start");
        log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
        try {
            mandateDocDAO.delete(mandateDocList);
            log.debug("-- delete stop");
        } catch (Exception e) {
            log.error("-- Exception wile delete data reason is {}", e.getMessage());
        }
    }

    private void createMapByECM(final List<ECMDetail> ecmDetailList){
        log.debug("-- createMapByECM(EcmDetailList.size.[{}])", ecmDetailList.size());
        listECMDetailMap = new HashMap<String, List<ECMDetail>>();

        //for test
//        for (ECMDetail ecmDetail : ecmDetailList) {
//            if("10634".equalsIgnoreCase(ecmDetail.getEcmDocId())){
//                ecmDetail.setEcmDocId("10052");
//            }
//            if("10668".equalsIgnoreCase(ecmDetail.getEcmDocId())){
//                ecmDetail.setEcmDocId("10151");
//            }
//            if("11148".equalsIgnoreCase(ecmDetail.getEcmDocId())){
//                ecmDetail.setEcmDocId("10097");
//            }
//            if("11149".equalsIgnoreCase(ecmDetail.getEcmDocId())){
//                ecmDetail.setEcmDocId("10053");
//            }
//            if("11285".equalsIgnoreCase(ecmDetail.getEcmDocId())){
//                ecmDetail.setEcmDocId("10121");
//            }
//            if("11296".equalsIgnoreCase(ecmDetail.getEcmDocId())){
//                ecmDetail.setEcmDocId("10100");
//            }
//        }

        for (ECMDetail ecmDetail : ecmDetailList) {
            log.debug("-- ECMDtail.EcmDocId[{}]", ecmDetail.getEcmDocId());
            List<ECMDetail> ecmListTmp = null;
            if(!listECMDetailMap.containsKey(ecmDetail.getEcmDocId())){
                log.debug("-- add key {} to map", ecmDetail.getEcmDocId());
                ecmListTmp = new ArrayList<ECMDetail>();
                ecmListTmp.add(ecmDetail);
                listECMDetailMap.put(ecmDetail.getEcmDocId(), ecmListTmp);
            } else {
                ecmListTmp = listECMDetailMap.get(ecmDetail.getEcmDocId());
                log.debug("-- get value from key {}", ecmDetail.getEcmDocId());
                ecmListTmp.add(ecmDetail);
                log.debug("-- add data to {}", ecmListTmp.size());
                listECMDetailMap.remove(ecmDetail.getEcmDocId());
                log.debug("-- remove value form map {}", ecmDetail.getEcmDocId());
                listECMDetailMap.put(ecmDetail.getEcmDocId(), ecmListTmp);
                log.debug("-- added key {} to map", ecmDetail.getEcmDocId());
            }
        }
        log.debug("-- ListECMDetailMap.size()[{}]", listECMDetailMap.size());
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
