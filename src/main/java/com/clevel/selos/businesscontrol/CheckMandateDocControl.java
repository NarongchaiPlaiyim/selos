package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
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
    @SELOS
    private Logger log;
    @Inject
    private MandateDocDAO mandateDocDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    @Inject
    private WorkCaseDAO workCaseDAO;
    private WorkCase workCase;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    private WorkCasePrescreen workCasePrescreen;
    @Inject
    private ECMInterface ecmInterface;
    private ECMDataResult ecmDataResult;
    @Inject
    private BRMSControl brmsControl;
    private MandateDocResponseView mandateDocResponseView;
    @Inject
    private CheckMandateDocTransform checkMandateDocTransform;
    private List<MandateDoc> mandateDocList;
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

    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
        mandateDoc = null;
        mandateDocResponseView = null;
    }

    public CheckMandateDocView getMandateDocViewByChecker(final long workCasePreScreenId) throws Exception{
        log.debug("-- getMandateDocViewByChecker WorkCasePreScreenId : {}", workCasePreScreenId);
        listECMDetailMap = callECMByWorkCasePreScreenId(workCasePreScreenId);
        mandateDocViewMap = callBRMSByWorkCasePreScreenId(workCasePreScreenId);

        int roleId = 0;
        String roleName = "";
        user = getCurrentUser();
        if(!Util.isNull(user)){
            roleId = user.getRole().getId();
            roleName = user.getRole().getName();
            log.debug("-- User.id[{}]", user.getId());
            log.debug("-- User.Role[{}]", roleName);
            mandateDocList = Util.safetyList(getMandateDocByWorkCasePreScreenIdAndRoleId(workCasePreScreenId, roleId));
        }

        getToken();
        log.debug("-- UserToken = {}", userToken);
        if(!Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
            log.debug("-- ECM ListECMDetailMap.size()[{}]", listECMDetailMap.size());
            checkMandateDocView = checkMap();
        } else if(!Util.isNull(mandateDocViewMap)) {
            checkMandateDocView = createCheckMandateDocViewByBRMS();
        } else if(!Util.isNull(listECMDetailMap)) {
            checkMandateDocView = createCheckMandateDocViewByECM();
        } else {
            log.debug("-- MandateDocViewMap is {} and ListECMDetailMap is {}", mandateDocViewMap, listECMDetailMap);
        }

        log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
        if(!Util.isZero(mandateDocList.size()) && !Util.isNull(mandateDocViewMap) || !Util.isNull(listECMDetailMap)){
            checkMandateDocView = compareToCheckMandateDocView(checkMandateDocView, mandateDocList);
        } else if(!Util.isZero(mandateDocList.size()) && Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            checkMandateDocView = createCheckMandateDocViewByDB();
        }
        return setReadOnlyByRole(checkMandateDocView, roleName);
    }
    public CheckMandateDocView getMandateDocViewByMaker(final long workCasePreScreenId) throws Exception{
        log.debug("-- getMandateDocViewByMaker WorkCasePreScreenId : {}", workCasePreScreenId);
        listECMDetailMap = callECMByWorkCasePreScreenId(workCasePreScreenId);
        mandateDocViewMap = callBRMSByWorkCasePreScreenId(workCasePreScreenId);

        int roleId = 0;
        String roleName = "";
        user = getCurrentUser();
        if(!Util.isNull(user)){
            roleId = user.getRole().getId();
            roleName = user.getRole().getName();
            log.debug("-- User.id[{}]", user.getId());
            log.debug("-- User.Role[{}]", roleName);
            mandateDocList = Util.safetyList(getMandateDocByWorkCasePreScreenIdAndRoleId(workCasePreScreenId, roleId));
        }

        getToken();
        log.debug("-- UserToken = {}", userToken);
        if(!Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
            log.debug("-- ECM ListECMDetailMap.size()[{}]", listECMDetailMap.size());
            checkMandateDocView = checkMap();
        } else if(!Util.isNull(mandateDocViewMap)) {
            checkMandateDocView = createCheckMandateDocViewByBRMS();
        } else if(!Util.isNull(listECMDetailMap)) {
            checkMandateDocView = createCheckMandateDocViewByECM();
        } else {
            log.debug("-- MandateDocViewMap is {} and ListECMDetailMap is {}", mandateDocViewMap, listECMDetailMap);
        }

        log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
        if(!Util.isZero(mandateDocList.size()) && !Util.isNull(mandateDocViewMap) || !Util.isNull(listECMDetailMap)){
            checkMandateDocView = compareToCheckMandateDocView(checkMandateDocView, mandateDocList);
        } else if(!Util.isZero(mandateDocList.size()) && Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            checkMandateDocView = createCheckMandateDocViewByDB();
        }
        return setReadOnlyByRole(checkMandateDocView, roleName);
    }
    public CheckMandateDocView getMandateDocViewByFullApp(final long workCaseId) throws Exception{
        log.debug("-- getMandateDocViewByFullApp WorkCaseId : {}", workCaseId);
        listECMDetailMap = callECMByWorkCaseId(workCaseId);
        mandateDocViewMap = callBRMSByWorkCaseId(workCaseId);

        int roleId = 0;
        String roleName = "";
        user = getCurrentUser();
        log.debug("-- User.id[{}]", user.getId());
        if(!Util.isNull(user)){
            roleId = user.getRole().getId();
            roleName = user.getRole().getName();
            log.debug("-- User.Role.id[{}]", roleId);
            mandateDocList = Util.safetyList(getMandateDocByWorkCaseIdAndRoleId(workCaseId, roleId));

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

        getToken();
        log.debug("-- UserToken = {}", userToken);
        if(!Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
            log.debug("-- ECM ListECMDetailMap.size()[{}]", listECMDetailMap.size());
            checkMandateDocView = checkMap();
        } else if(!Util.isNull(mandateDocViewMap)) {
            checkMandateDocView = createCheckMandateDocViewByBRMS();
        } else if(!Util.isNull(listECMDetailMap)) {
            checkMandateDocView = createCheckMandateDocViewByECM();
        } else {
            log.debug("-- MandateDocViewMap is {} and ListECMDetailMap is {}", mandateDocViewMap, listECMDetailMap);
        }

        log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
        if(!Util.isZero(mandateDocList.size()) && !Util.isNull(mandateDocViewMap) || !Util.isNull(listECMDetailMap)){
            checkMandateDocView = compareToCheckMandateDocView(checkMandateDocView, mandateDocList);
        } else if(!Util.isZero(mandateDocList.size()) && Util.isNull(mandateDocViewMap) && !Util.isNull(listECMDetailMap)){
            checkMandateDocView = createCheckMandateDocViewByDB();
        }
        return setReadOnlyByRole(checkMandateDocView, roleName);
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

        if(!Util.isZero(mandateDocList.size())){
            delete(mandateDocList);
        }
        mandateDocList = null;
        mandateDocList = Util.safetyList(checkMandateDocTransform.transformToModel(checkMandateDocView, workCaseId, user.getRole()));
        save(mandateDocList);
    }
    private CheckMandateDocView getObjectFromDB(final long workCaseId,final  int roleId){
        log.debug("-- getObjectFromDB(workCaseId {}, roleId {})", workCaseId, roleId);
        checkMandateDocView = new CheckMandateDocView();
        List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView checkMandatoryDocView = null;
        List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView optionalDocView = null;
        List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
        CheckOtherDocView checkOtherDocView = null;
        List<MandateDoc> mandateDocList = null;

        listECMDetailMap = callECMByWorkCaseId(workCaseId);

        String keyECM = null;
        getToken();
        log.debug("-- UserToken = {}", userToken);
        List<String> keyECMList = new ArrayList<String>();
        if(!Util.isNull(listECMDetailMap)){
            log.debug("-- ListECMDetailMap.size()[{}]", listECMDetailMap.size());
            mandateDocList = Util.safetyList(mandateDocDAO.findByWorkCaseIdAndRole(workCaseId, roleId));
            if(!Util.isZero(mandateDocList.size())){
                log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
                for (Map.Entry<String, List<ECMDetail>> ECMentry : listECMDetailMap.entrySet()) {
                    keyECM = ECMentry.getKey();
                    log.debug("-- The key of ECM map is {}", keyECM);

                    ecmDetailList = Util.safetyList((List<ECMDetail>)ECMentry.getValue());
                    log.debug("-- The NumberOfDoc of ECM  is {}", ecmDetailList.size());
                    for(ECMDetail ecmDetail : ecmDetailList){
                        for(MandateDoc mandateDoc :  mandateDocList){
                            if(ecmDetail.getEcmDocId().equals(mandateDoc.getEcmDocType())){

                                int isComplete = 2;
                                try {
                                    if(!Util.isNull(mandateDoc.getMandateDocFileNameList())){
                                        if(mandateDoc.getMandateDocFileNameList().size() == ecmDetailList.size()){
                                            isComplete = 1;
                                        }
                                    }
                                } catch (Exception e){
                                    isComplete = 3;
                                }
                                log.debug("-- MandateDocFileNameList.size()[{}]", mandateDoc.getMandateDocFileNameList().size());
                                log.debug("-- EcmDetailList.size()[{}]", ecmDetailList.size());
                                log.debug("-- IsCompleted {}", isComplete);

                                if(DocMandateType.MANDATE.value() == mandateDoc.getMandateType()){
                                    log.debug("-- ECMDocType {} = {}.", ECMentry.getKey(), "Mandatory Documents");
                                    checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(ECMentry.getKey(), mandateDoc, ecmDetailList, isComplete, userToken);
                                    mandatoryDocumentsList.add(checkMandatoryDocView);
                                } else if(DocMandateType.OPTIONAL.value() == mandateDoc.getMandateType()){
                                    log.debug("-- ECMDocType {} = {}.", ECMentry.getKey(), "Optional Documents");
                                    optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(       ECMentry.getKey(), mandateDoc, ecmDetailList, isComplete, userToken);
                                    optionalDocumentsList.add(optionalDocView);
                                } else {
                                    log.debug("-- ECMDocType {} = {}.", ECMentry.getKey(), "Other Documents");
                                    checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(        ECMentry.getKey(), mandateDoc, ecmDetailList, isComplete, userToken);
                                    otherDocumentsList.add(checkOtherDocView);
                                }
                                mandateDocList.remove(mandateDoc);
                                keyECMList.add(keyECM);
                            } else {
                                continue;
                            }
                        }
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
                    checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(ECMentry.getKey(), ecmDetailList, 2, userToken);
                    otherDocumentsList.add(checkOtherDocView);
                }

                checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
                log.debug("-- MandatoryDocumentsList.size()[{}] added to CheckMandateDocView", mandatoryDocumentsList.size());
                checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
                log.debug("-- OptionalDocumentsList.size()[{}] added to CheckMandateDocView", optionalDocumentsList.size());
                checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
                log.debug("-- OtherDocumentsList.size()[{}] added to CheckMandateDocView", otherDocumentsList.size());
            } else {
                log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
                checkMandateDocView = createCheckMandateDocViewByECM();
            }
        } else {
            log.debug("-- ECM ListECMDetailMap[{}]", listECMDetailMap);
            mandateDocList = Util.safetyList(mandateDocDAO.findByWorkCaseIdAndRole(workCaseId, roleId));
            if(!Util.isZero(mandateDocList.size())){
                log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
                checkMandateDocView = new CheckMandateDocView();
                checkMandateDocView = checkMandateDocTransform.transformToView(mandateDocList);
                getToken();
                log.debug("-- UserToken[{}]", userToken);
                mandatoryDocumentsList = checkMandateDocView.getMandatoryDocumentsList();
                for(CheckMandatoryDocView view : mandatoryDocumentsList){
                    List<MandateDocFileNameView> fileNameViewList = view.getFileNameViewList();
                    for(MandateDocFileNameView fileNameView : fileNameViewList){
                        fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                    }
                }
                optionalDocumentsList = checkMandateDocView.getOptionalDocumentsList();
                for(CheckOptionalDocView view : optionalDocumentsList){
                    List<MandateDocFileNameView> fileNameViewList = view.getFileNameViewList();
                    for(MandateDocFileNameView fileNameView : fileNameViewList){
                        fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                    }
                }
                otherDocumentsList = checkMandateDocView.getOtherDocumentsList();
                for(CheckOtherDocView view : otherDocumentsList){
                    List<MandateDocFileNameView> fileNameViewList = view.getFileNameViewList();
                    for(MandateDocFileNameView fileNameView : fileNameViewList){
                        fileNameView.setUrl(updateToken(fileNameView.getUrl()));
                    }
                }
                checkMandateDocView.readOnly();
                log.debug("-- [READ ONLY]CheckMandateDocView");
                log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
                log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
                log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
            } else {
                log.debug("-- MandateDocList.size()[{}]", mandateDocList.size());
            }
        }
        return checkMandateDocView;
    }







    private Map<String, List<ECMDetail>> createMapByECM(final List<ECMDetail> ecmDetailList){
        log.debug("-- createMapByECM(EcmDetailList.size.[{}])", ecmDetailList.size());
        Map<String,List<ECMDetail>> listECMDetailMap = new HashMap<String, List<ECMDetail>>();
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
        return listECMDetailMap;
    }
    private Map<String, List<ECMDetail>> getECMByAppNumber(final String appNumber){
        Map<String,List<ECMDetail>> listECMDetailMap = null;
        ecmDataResult = ecmInterface.getECMDataResult(appNumber);
        if(!Util.isNull(ecmDataResult) && ActionResult.SUCCESS.equals(ecmDataResult.getActionResult())){
            listECMDetailMap = new HashMap<String, List<ECMDetail>>();
            log.debug("-- ActionResult is {}", ecmDataResult.getActionResult());
            List<ECMDetail> ecmDetailList = Util.safetyList(ecmDataResult.getEcmDetailList());
            if(!Util.isZero(ecmDetailList.size())){
                log.debug("-- ECM EcmDetailList.size()[{}]", ecmDetailList.size());
                listECMDetailMap = createMapByECM(ecmDetailList);
                log.debug("-- ECM ListECMDetailMap.size.[{}])", listECMDetailMap.size());
            } else {
                log.debug("-- EcmDetailList.size()[{}]", ecmDetailList.size());
            }
        } else {
            log.debug("-- Find by CA Number = {} ActionResult is {} and reason is {}  ", appNumber, ecmDataResult.getActionResult(), ecmDataResult.getReason());
        }
        return listECMDetailMap;
    }
    private Map<String, List<ECMDetail>> callECMByWorkCaseId(final long workCaseId){
        log.debug("-- callECMByWorkCasePreScreenId(workCaseId : {})", workCaseId);
        Map<String,List<ECMDetail>> listECMDetailMap = null;
        try {
            workCase = workCaseDAO.findById(workCaseId);
            if(!Util.isNull(workCase)){
                log.debug("-- WorkCase.id[{}]", workCase.getId());
                log.debug("-- WorkCase.AppNumber[{}]", workCase.getAppNumber());
                listECMDetailMap = getECMByAppNumber(workCase.getAppNumber());
            } else {
                log.debug("-- Find by work case id = {} ", workCase);
            }
        } catch (Exception e){
            log.error("-- Exception while call ECM {}", e);
        }
        return listECMDetailMap;
    }
    private Map<String, List<ECMDetail>> callECMByWorkCasePreScreenId(final long workCasePreScreenId){
        log.debug("-- callECMByWorkCasePreScreenId(workCasePreScreenId : {})", workCasePreScreenId);
        Map<String,List<ECMDetail>> listECMDetailMap = null;
        try {
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            if(!Util.isNull(workCasePrescreen)){
                log.debug("-- WorkCasePrescreen.id[{}]", workCasePrescreen.getId());
                log.debug("-- WorkCasePrescreen.AppNumber[{}]", workCasePrescreen.getAppNumber());
                listECMDetailMap = getECMByAppNumber(workCasePrescreen.getAppNumber());
            } else {
                log.debug("-- Find by work case prescreen id = {} ", workCasePrescreen);
            }
        } catch (Exception e){
            log.error("-- Exception while call ECM {}", e);
        }
        return listECMDetailMap;
    }
    private Map<String, MandateDocView>  callBRMSByWorkCasePreScreenId(final long workCasePreScreenId){
        log.debug("-- callBRMSByWorkCasePreScreenId(workCasePreScreenId : {})", workCasePreScreenId);
        Map<String, MandateDocView> mandateDocViewMap = null;
        try {
            mandateDocResponseView = brmsControl.getDocCustomerForPrescreen(workCasePreScreenId);
            if(!Util.isNull(mandateDocResponseView) && ActionResult.SUCCESS.equals(mandateDocResponseView.getActionResult())){
                log.debug("-- ActionResult is {}", mandateDocResponseView.getActionResult());
                mandateDocViewMap =  mandateDocResponseView.getMandateDocViewMap();
                log.debug("-- BRMS MandateDocViewMap.size()[{}]", mandateDocViewMap.size());
            } else {
                log.debug("-- Find by work case perscreen id = {} ActionResult is {} and reason is {}  ", workCasePreScreenId, mandateDocResponseView.getActionResult(), mandateDocResponseView.getReason());
            }
        } catch (Exception e){
            log.error("-- Exception while call BRMS ", e);
        }
        return mandateDocViewMap;
    }
    private Map<String, MandateDocView>  callBRMSByWorkCaseId(final long workCaseId){
        log.debug("-- callBRMSByWorkCaseId(workCaseId : {})", workCaseId);
        Map<String, MandateDocView> mandateDocViewMap = null;
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
            log.error("-- Exception while call BRMS ", e);
        }
        return mandateDocViewMap;
    }
    private List<MandateDoc> getMandateDocByWorkCaseIdAndRoleId(final long workCaseId, final  int roleId){
        return mandateDocDAO.findByWorkCaseIdAndRole(workCaseId, roleId);
    }
    private List<MandateDoc> getMandateDocByWorkCasePreScreenIdAndRoleId(final long workCasePreScreenId, final  int roleId){
        return mandateDocDAO.findByWorkCasePreScreenIdAndRole(workCasePreScreenId, roleId);
    }
    private CheckMandateDocView createCheckMandateDocViewByBRMS(){
        log.debug("-- got data from BRMS");
        CheckMandateDocView checkMandateDocView = new CheckMandateDocView();
        List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView checkMandatoryDocView = null;
        List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView optionalDocView = null;
        List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
        CheckOtherDocView checkOtherDocView = null;

        //BRMS
        for (Map.Entry<String, MandateDocView> BRMSentry : mandateDocViewMap.entrySet()) {
            mandateDocView = (MandateDocView)BRMSentry.getValue();
            if(DocMandateType.MANDATE.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Mandatory Documents");
                checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(BRMSentry.getKey(), mandateDocView, 2);
                mandatoryDocumentsList.add(checkMandatoryDocView);
            } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Optional Documents");
                optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(BRMSentry.getKey(), mandateDocView, 2);
                optionalDocumentsList.add(optionalDocView);
            } else {
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Other Documents");
                checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(BRMSentry.getKey(), mandateDocView, 2);
                otherDocumentsList.add(checkOtherDocView);
            }
        }

        checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
        log.debug("-- MandatoryDocumentsList.size()[{}] added to CheckMandateDocView", mandatoryDocumentsList.size());
        checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
        log.debug("-- OptionalDocumentsList.size()[{}] added to CheckMandateDocView", optionalDocumentsList.size());
        checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
        log.debug("-- OtherDocumentsList.size()[{}] added to CheckMandateDocView", otherDocumentsList.size());
        return checkMandateDocView;
    }
    private CheckMandateDocView createCheckMandateDocViewByECM(){
        log.debug("-- got data from ECM");
        CheckMandateDocView checkMandateDocView = new CheckMandateDocView();
        List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView checkMandatoryDocView = null;
        List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView optionalDocView = null;
        List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
        CheckOtherDocView checkOtherDocView = null;

        getToken();
        log.debug("-- UserToken = {}", userToken);
        log.debug("-- ECM ListECMDetailMap.size()[{}]", listECMDetailMap.size());

        //ECM
        for (Map.Entry<String, List<ECMDetail>> ECMentry : listECMDetailMap.entrySet()) {
            ecmDetailList = Util.safetyList((List<ECMDetail>)ECMentry.getValue());
            log.debug("-- ECMDocType {} = {}.", ECMentry.getKey(), "Other Documents");
            checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(ECMentry.getKey(), ecmDetailList, 2, userToken);
            checkOtherDocView.readOnly();
            otherDocumentsList.add(checkOtherDocView);
        }
        checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
        log.debug("-- MandatoryDocumentsList.size()[{}] added to CheckMandateDocView", mandatoryDocumentsList.size());
        checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
        log.debug("-- OptionalDocumentsList.size()[{}] added to CheckMandateDocView", optionalDocumentsList.size());
        checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
        log.debug("-- OtherDocumentsList.size()[{}] added to CheckMandateDocView", otherDocumentsList.size());
        return checkMandateDocView;
    }
    private CheckMandateDocView createCheckMandateDocViewByDB(){
        log.debug("-- got data from DB");
        CheckMandateDocView checkMandateDocView = new CheckMandateDocView();
        List<CheckMandatoryDocView> mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        List<CheckOptionalDocView> optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        List<CheckOtherDocView> otherDocumentsList = new ArrayList<CheckOtherDocView>();
        getToken();
        log.debug("-- UserToken[{}]", userToken);
        mandatoryDocumentsList = checkMandateDocView.getMandatoryDocumentsList();
        for(CheckMandatoryDocView view : mandatoryDocumentsList){
            List<MandateDocFileNameView> fileNameViewList = Util.safetyList(view.getFileNameViewList());
            for(MandateDocFileNameView fileNameView : fileNameViewList){
                fileNameView.setUrl(updateToken(fileNameView.getUrl()));
            }
        }
        optionalDocumentsList = checkMandateDocView.getOptionalDocumentsList();
        for(CheckOptionalDocView view : optionalDocumentsList){
            List<MandateDocFileNameView> fileNameViewList = Util.safetyList(view.getFileNameViewList());
            for(MandateDocFileNameView fileNameView : fileNameViewList){
                fileNameView.setUrl(updateToken(fileNameView.getUrl()));
            }
        }
        otherDocumentsList = checkMandateDocView.getOtherDocumentsList();
        for(CheckOtherDocView view : otherDocumentsList){
            List<MandateDocFileNameView> fileNameViewList = Util.safetyList(view.getFileNameViewList());
            for(MandateDocFileNameView fileNameView : fileNameViewList){
                fileNameView.setUrl(updateToken(fileNameView.getUrl()));
            }
        }
        checkMandateDocView = checkMandateDocTransform.transformToView(mandateDocList);
        return checkMandateDocView;
    }
    private CheckMandateDocView setReadOnlyByRole(final CheckMandateDocView checkMandateDocView, final String role){
        if(Util.isNull(checkMandateDocView)){
            return checkMandateDocView;
        }
        if(Util.isNull(role)){
            return checkMandateDocView;
        }
        if(ABDM.equalsIgnoreCase(role)){
            //view [ABDM]
            log.debug("-- {} Role[read]", ABDM);
            checkMandateDocView.setReasonFlag(true);
            checkMandateDocView.setCompleteFlag(true);
            checkMandateDocView.setRemarkFlag(true);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        } else if(BDM.equalsIgnoreCase(role)){
            //view [BDM]
            log.debug("-- {} Role[read]", BDM);
            checkMandateDocView.setReasonFlag(true);
            checkMandateDocView.setCompleteFlag(true);
            checkMandateDocView.setRemarkFlag(true);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        } else if(UW.equalsIgnoreCase(role)){
            //view or edit [UW Edit]
            log.debug("-- {} Role[read or edit]", UW);
            checkMandateDocView.setReasonFlag(false);
            checkMandateDocView.setCompleteFlag(false);
            checkMandateDocView.setRemarkFlag(false);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        } else {
            //view or edit [Other Edit]
            log.debug("-- {} Role[read or edit]", "Other");
            checkMandateDocView.setReasonFlag(false);
            checkMandateDocView.setCompleteFlag(false);
            checkMandateDocView.setRemarkFlag(false);
            log.debug("-- isReasonFlag {}", checkMandateDocView.isReasonFlag());
            log.debug("-- isCompleteFlag {}", checkMandateDocView.isCompleteFlag());
            log.debug("-- isRemarkFlag {}", checkMandateDocView.isRemarkFlag());
        }
        return checkMandateDocView;
    }
    private CheckMandateDocView checkMap(){
        log.debug("-- checkMap start");
        CheckMandateDocView checkMandateDocView = new CheckMandateDocView();
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
                        checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(keyECM, mandateDocView, ecmDetailList, isComplete, userToken);
                        if(isComplete == 1){
                            checkMandatoryDocView.readOnly();
                            checkMandatoryDocView.setCompleteFlag(true);
                            log.debug("-- CheckMandatoryDocView key[{}] readOnly", keyECM);
                        }
                        mandatoryDocumentsList.add(checkMandatoryDocView);
                    } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Optional Documents");
                        optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(keyECM, mandateDocView, ecmDetailList, isComplete, userToken);
                        if(isComplete == 1){
                            optionalDocView.readOnly();
                            optionalDocView.setCompleteFlag(true);
                            log.debug("-- OptionalDocView key[{}] readOnly", keyECM);
                        }
                        optionalDocumentsList.add(optionalDocView);
                    } else {
                        log.debug("-- ECMDocType {} = {}.", keyECM, "Other Documents");
                        checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(keyECM, mandateDocView, ecmDetailList, isComplete, userToken);
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
                checkMandatoryDocView = checkMandateDocTransform.transformToCheckMandatoryDocView(BRMSentry.getKey(), mandateDocView, 2);
                mandatoryDocumentsList.add(checkMandatoryDocView);
            } else if(DocMandateType.OPTIONAL.value() == mandateDocView.getDocMandateType().value()){
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Optional Documents");
                optionalDocView = checkMandateDocTransform.transformToCheckOptionalDocView(BRMSentry.getKey(), mandateDocView, 2);
                optionalDocumentsList.add(optionalDocView);
            } else {
                log.debug("-- BRMSDocType {} = {}.", BRMSentry.getKey(), "Other Documents");
                checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(BRMSentry.getKey(), mandateDocView, 2);
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
            checkOtherDocView = checkMandateDocTransform.transformToCheckOtherDocView(ECMentry.getKey(), ecmDetailList, 2, userToken);
            otherDocumentsList.add(checkOtherDocView);
        }
        checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);
        log.debug("-- MandatoryDocumentsList.size()[{}] added to CheckMandateDocView", mandatoryDocumentsList.size());
        checkMandateDocView.setOptionalDocumentsList(optionalDocumentsList);
        log.debug("-- OptionalDocumentsList.size()[{}] added to CheckMandateDocView", optionalDocumentsList.size());
        checkMandateDocView.setOtherDocumentsList(otherDocumentsList);
        log.debug("-- OtherDocumentsList.size()[{}] added to CheckMandateDocView", otherDocumentsList.size());
        return checkMandateDocView;
    }
    private CheckMandateDocView compareToCheckMandateDocView(final CheckMandateDocView checkMandateDocView, final List<MandateDoc> mandateDocList){
        log.debug("-- compareToCheckMandateDocView()");


        List<CheckMandatoryDocView> mandatoryDocumentsList = null;
        List<CheckOptionalDocView> optionalDocumentsList = null;
        List<CheckOtherDocView> otherDocumentsList = null;

        mandatoryDocumentsList = Util.safetyList(checkMandateDocView.getMandatoryDocumentsList());
        optionalDocumentsList = Util.safetyList(checkMandateDocView.getOptionalDocumentsList());
        otherDocumentsList = Util.safetyList(checkMandateDocView.getOtherDocumentsList());

        String key = "";
        pointer :
        for (MandateDoc mandateDoc : Util.safetyList(mandateDocList)){
            key = mandateDoc.getEcmDocType();
            for (CheckMandatoryDocView checkMandatoryDocView : mandatoryDocumentsList){
                if(key.equals(checkMandatoryDocView.getKey())){
                    log.debug("-- KEY[{}] Mandatory", key);
                    if(!Util.isNull(mandateDoc.getReasonIncomplete())){
                        checkMandatoryDocView.setIncomplete(Util.isTrue(mandateDoc.getReasonIncomplete()));
                        log.debug("-- CheckMandatoryDocView.Incomplete[{}]", checkMandatoryDocView.isIncomplete());
                    }
                    if(!Util.isNull(mandateDoc.getReasonIndistinct())){
                        checkMandatoryDocView.setIndistinct(Util.isTrue(mandateDoc.getReasonIndistinct()));
                        log.debug("-- CheckMandatoryDocView.Indistinct[{}]", checkMandatoryDocView.isIndistinct());
                    }
                    if(!Util.isNull(mandateDoc.getReasonIncorrect())){
                        checkMandatoryDocView.setIncorrect(Util.isTrue(mandateDoc.getReasonIncorrect()));
                        log.debug("-- CheckMandatoryDocView.Incorrect[{}]", checkMandatoryDocView.isIncorrect());
                    }
                    if(!Util.isNull(mandateDoc.getReasonExpire())){
                        checkMandatoryDocView.setExpire(Util.isTrue(mandateDoc.getReasonExpire()));
                        log.debug("-- CheckMandatoryDocView.Expire[{}]", checkMandatoryDocView.isExpire());
                    }
                    if(!Util.isNull(mandateDoc.getRemark())){
                        checkMandatoryDocView.setRemark(mandateDoc.getRemark());
                        log.debug("-- CheckMandatoryDocView.Remark[{}]", checkMandatoryDocView.getRemark());
                    }
                    mandateDocList.remove(mandateDoc);
                    continue pointer;
                } else {
                    continue;
                }
            }
            for (CheckOptionalDocView checkOptionalDocView : optionalDocumentsList){
                if(key.equals(checkOptionalDocView.getKey())){
                    log.debug("-- KEY[{}] Optional", key);
                    if(!Util.isNull(mandateDoc.getReasonIncomplete())){
                        checkOptionalDocView.setIncomplete(Util.isTrue(mandateDoc.getReasonIncomplete()));
                        log.debug("-- CheckOptionalDocView.Incomplete[{}]", checkOptionalDocView.isIncomplete());
                    }
                    if(!Util.isNull(mandateDoc.getReasonIndistinct())){
                        checkOptionalDocView.setIndistinct(Util.isTrue(mandateDoc.getReasonIndistinct()));
                        log.debug("-- CheckOptionalDocView.Indistinct[{}]", checkOptionalDocView.isIndistinct());
                    }
                    if(!Util.isNull(mandateDoc.getReasonIncorrect())){
                        checkOptionalDocView.setIncorrect(Util.isTrue(mandateDoc.getReasonIncorrect()));
                        log.debug("-- CheckOptionalDocView.Incorrect[{}]", checkOptionalDocView.isIncorrect());
                    }
                    if(!Util.isNull(mandateDoc.getReasonExpire())){
                        checkOptionalDocView.setExpire(Util.isTrue(mandateDoc.getReasonExpire()));
                        log.debug("-- CheckOptionalDocView.Expire[{}]", checkOptionalDocView.isExpire());
                    }
                    if(!Util.isNull(mandateDoc.getRemark())){
                        checkOptionalDocView.setRemark(mandateDoc.getRemark());
                        log.debug("-- CheckOptionalDocView.Remark[{}]", checkOptionalDocView.getRemark());
                    }
                    mandateDocList.remove(mandateDoc);
                    continue pointer;
                } else {
                    continue;
                }
            }
            for (CheckOtherDocView checkOtherDocView : otherDocumentsList){
                if(key.equals(checkOtherDocView.getKey())){
                    log.debug("-- KEY[{}] Other", key);
                    log.debug("-- KEY[{}] Optional", key);
                    if(!Util.isNull(mandateDoc.getReasonIncomplete())){
                        checkOtherDocView.setIncomplete(Util.isTrue(mandateDoc.getReasonIncomplete()));
                        log.debug("-- CheckOtherDocView.Incomplete[{}]", checkOtherDocView.isIncomplete());
                    }
                    if(!Util.isNull(mandateDoc.getReasonIndistinct())){
                        checkOtherDocView.setIndistinct(Util.isTrue(mandateDoc.getReasonIndistinct()));
                        log.debug("-- CheckOtherDocView.Indistinct[{}]", checkOtherDocView.isIndistinct());
                    }
                    if(!Util.isNull(mandateDoc.getReasonIncorrect())){
                        checkOtherDocView.setIncorrect(Util.isTrue(mandateDoc.getReasonIncorrect()));
                        log.debug("-- CheckOtherDocView.Incorrect[{}]", checkOtherDocView.isIncorrect());
                    }
                    if(!Util.isNull(mandateDoc.getReasonExpire())){
                        checkOtherDocView.setExpire(Util.isTrue(mandateDoc.getReasonExpire()));
                        log.debug("-- CheckOtherDocView.Expire[{}]", checkOtherDocView.isExpire());
                    }
                    if(!Util.isNull(mandateDoc.getRemark())){
                        checkOtherDocView.setRemark(mandateDoc.getRemark());
                        log.debug("-- CheckOtherDocView.Remark[{}]", checkOtherDocView.getRemark());
                    }
                    mandateDocList.remove(mandateDoc);
                    continue pointer;
                } else {
                    continue;
                }
            }
        }
        return checkMandateDocView;
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
