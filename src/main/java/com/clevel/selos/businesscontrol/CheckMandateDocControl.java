package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.MandateDocAccessControl;
import com.clevel.selos.businesscontrol.master.StepControl;
import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.db.ECMTypeName;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MandateDocDetail;
import com.clevel.selos.model.db.working.MandateDocSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.MandateDocAccessView;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.transform.CheckMandateDocTransform;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Stateless
public class CheckMandateDocControl extends BusinessControl {
    //ECM Configuration
    @Inject
    @Config(name = "interface.workplace.username")
    private String username;
    @Inject
    @Config(name = "interface.workplace.password")
    private String password;
    @Inject
    @Config(name = "system.encryption.enable")
    private String encryptionEnable;

    //ECM Services
    @Inject
    private EncryptionService encryptionService;
    @Inject
    private CESessionToken CESessionToken;

    @Inject
    @SELOS
    private Logger log;

    @Inject
    private StepControl stepControl;
    @Inject
    private BRMSControl brmsControl;
    @Inject
    private MandateDocAccessControl mandateDocAccessControl;

    @Inject
    private WorkCaseDAO workCaseDAO;

    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    private ECMInterface ecmInterface;

    @Inject
    private CheckMandateDocTransform checkMandateDocTransform;

    @Inject
    private MandateDocFileNameDAO mandateDocFileNameDAO;
    @Inject
    private MandateDocBRMSDAO mandateDocBRMSDAO;
    @Inject
    private MandateDocCustDAO mandateDocCustDAO;
    @Inject
    private MandateDocSummaryDAO mandateDocSummaryDAO;
    @Inject
    private MandateDocDetailDAO mandateDocDetailDAO;
    @Inject
    private RoleDAO roleDAO;

    private String passwordEncrypt;
    private String userToken;

    @Inject
    public CheckMandateDocControl() {
    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        getToken();
        log.debug("-- UserToken = {}", userToken);

    }

    public CheckMandateDocView checkMandateDoc(long stepId, long caseId){
        log.debug("-- begin checkMandateDoc: stepId {}, caseId: {}", stepId, caseId);
        StepView stepView = stepControl.getStepView(stepId);
        StageView stageView = stepView.getStageView();
        User user = getCurrentUser();
        Role role = user.getRole();

        //Prepare internal Default Value;
        int stageId = stageView.getId();
        List<String> ecmDocTypeIDList = new ArrayList<String>();


        CheckMandateDocView checkMandateDocView = new CheckMandateDocView();

        //1. Get Access Right for Document Access.
        MandateDocAccessView mandateDocAccessView = mandateDocAccessControl.getMandateDocAccessView(stepId, role.getId());
        log.debug("mandateDocAccessView: {}", mandateDocAccessView);

        //2. Retrieve Data from SLOS-DB to Map with BRMS.
        log.debug("get Mandate Doc list from DB");
        MandateDocSummary mandateDocSummary = null;
        try{
            //When the mandateDocSummary is not Found for each step, stage, role. Then only query once again for user who has AccessType.VIEWER
            if(stageView.getId() == StageValue.PRESCREEN.value()){
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(caseId);
                if(workCasePrescreen.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                    mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(caseId, stepId, StageValue.APPRAISAL.value(), role.getId());
                    if(mandateDocSummary == null && AccessType.VIEWER.equals(mandateDocAccessView.getAccessType())) {
                        mandateDocSummary = mandateDocSummaryDAO.findLatestInfoByWorkCasePrescreenId(caseId, StageValue.APPRAISAL);
                    }
                    stageId = StageValue.APPRAISAL.value();
                } else {
                    mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(caseId, stepId, StageValue.PRESCREEN.value(), role.getId());
                    if(mandateDocSummary == null && AccessType.VIEWER.equals(mandateDocAccessView.getAccessType())) {
                        mandateDocSummary = mandateDocSummaryDAO.findLatestInfoByWorkCasePrescreenId(caseId, StageValue.PRESCREEN);
                    }
                }
            } else if(stageView.getId() == StageValue.APPRAISAL.value()){
                mandateDocSummary = mandateDocSummaryDAO.findLatestInfoByWorkCaseId(caseId, StageValue.APPRAISAL);
            } else {
                WorkCase workCase = workCaseDAO.findById(caseId);
                if(workCase.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                    mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(caseId, stepId, StageValue.APPRAISAL.value(), role.getId());
                    if(mandateDocSummary == null && AccessType.VIEWER.equals(mandateDocAccessView.getAccessType())) {
                        mandateDocSummary = mandateDocSummaryDAO.findLatestInfoByWorkCaseId(caseId, StageValue.APPRAISAL);
                    }
                    stageId = StageValue.APPRAISAL.value();
                } else {
                    mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(caseId, stepId, StageValue.FULL_APPLICATION.value(), role.getId());
                    if(mandateDocSummary == null && AccessType.VIEWER.equals(mandateDocAccessView.getAccessType())) {
                        mandateDocSummary = mandateDocSummaryDAO.findLatestInfoByWorkCaseId(caseId, StageValue.FULL_APPLICATION);
                    }
                }
            }
        }catch (Exception ex){
            if(mandateDocAccessView.getAccessType() == AccessType.VIEWER){
                //When there is No data from
                checkMandateDocView.setMessage("There is No Mandate Document listed in Database.");
                checkMandateDocView.setPassMandateList(false);
                return checkMandateDocView;
            }
        }
        checkMandateDocView = checkMandateDocTransform.transformToView(mandateDocSummary, userToken);

        //3. Check to load Mandatory List from BRMS or the latest on in SELOS-DB
        if(mandateDocAccessView != null) {
            checkMandateDocView.setPassMandateList(Boolean.TRUE);
            if(Util.isTrue(stepView.getDocCheck()) && mandateDocAccessView.isCheckBRMS() && !mandateDocAccessView.getAccessType().equals(AccessType.VIEWER)){
                MandateDocResponseView mandateDocResponseView = null;
                log.debug("get Mandate Doc from BRMS --");
                if(stageView.getId() == StageValue.PRESCREEN.value()){
                    WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(caseId);
                    if(workCasePrescreen.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                        mandateDocResponseView = brmsControl.getDocAppraisal(stepId, caseId);
                        stageId = StageValue.APPRAISAL.value();
                    } else {
                        mandateDocResponseView = brmsControl.getDocCustomerForPrescreen(caseId);
                    }
                } else if(stageView.getId() == StageValue.APPRAISAL.value()){
                    mandateDocResponseView = brmsControl.getDocAppraisal(stepId, caseId);
                } else {
                    WorkCase workCase = workCaseDAO.findById(caseId);
                    if(workCase.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                        mandateDocResponseView = brmsControl.getDocAppraisal(stepId, caseId);
                        stageId = StageValue.APPRAISAL.value();
                    } else {
                        mandateDocResponseView = brmsControl.getDocCustomerForFullApp(caseId);
                    }
                }

                log.debug("mandateDocResponseView: {}", mandateDocResponseView);
                if(mandateDocResponseView.getActionResult() == ActionResult.FAILED){
                    checkMandateDocView.setPassMandateList(Boolean.FALSE);
                    checkMandateDocView.setMessage(mandateDocResponseView.getReason());
                    return checkMandateDocView;
                }

                checkMandateDocView.setMandatoryDocumentsMap(_mapDBWithBRMS(checkMandateDocView.getMandatoryDocumentsMap(), mandateDocResponseView.getMandateDocViewMap()));
                checkMandateDocView.setOptionalDocumentsMap(_mapDBWithBRMS(checkMandateDocView.getOptionalDocumentsMap(), mandateDocResponseView.getOptionalDocViewMap()));

                ecmDocTypeIDList = mandateDocResponseView.getEcmDocTypeIDList();
            } else {

                checkMandateDocView.setPassMandateList(true);
            }


            //3. Retrieve documents from ECM
            if(mandateDocAccessView.isCheckECM()){
                String appNumber = null;
                if(stageView.getId() == StageValue.PRESCREEN.value()){
                    WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(caseId);
                    appNumber = workCasePrescreen.getAppNumber();
                } else {
                    WorkCase workCase = workCaseDAO.findById(caseId);
                    appNumber = workCase.getAppNumber();
                }

                List<ECMDetail> ecmDetailList = new ArrayList<ECMDetail>();
                Map<String, ECMTypeName> ecmTypeNameMap = new ConcurrentHashMap<String, ECMTypeName>();
                try {
                    ECMDataResult ecmDataResult = ecmInterface.getECMDataResult(appNumber);
                    ecmDetailList = ecmDataResult.getEcmDetailList();
                    checkMandateDocView.setPassECMList(Boolean.TRUE);
                }catch (Exception ex){
                    checkMandateDocView.setPassECMList(Boolean.FALSE);
                }
                try{
                    ECMDataResult docTypeDataResult = ecmInterface.getECMTypeName(ecmDocTypeIDList);
                    ecmTypeNameMap = docTypeDataResult.getEcmTypeNameMap();
                } catch(Exception ex){
                    checkMandateDocView.setPassECMList(Boolean.FALSE);
                }
                checkMandateDocView = _mapECMDocumentToMandateDoc(ecmDetailList, checkMandateDocView, ecmTypeNameMap);
                log.debug("after map with ECM : {}", checkMandateDocView);
            } else {
                ECMDataResult docTypeDataResult = ecmInterface.getECMTypeName(ecmDocTypeIDList);
                _updateECMDocTypeDesc(checkMandateDocView, docTypeDataResult.getEcmTypeNameMap());
                checkMandateDocView.setPassECMList(Boolean.TRUE);
            }

            //4. Set Workcase or WorkcasePrescreenId into CheckMandateDocView
            if(stageView.getId() == StageValue.PRESCREEN.value()){
                checkMandateDocView.setWorkCasePrescreenId(caseId);
            } else {
                checkMandateDocView.setWorkCaseId(caseId);
            }

            //5. Map Access Right with function on Screen.
            if(mandateDocAccessView.getAccessType().equals(AccessType.VIEWER)){
                checkMandateDocView.setAllowCheckComplete(false);
                checkMandateDocView.setAllowUpdateReason(false);
                checkMandateDocView.setAllowUpdateRemark(false);
            } else if(mandateDocAccessView.getAccessType().equals(AccessType.MAKER)){
                _updateCompletedFlag(checkMandateDocView);
                if(checkMandateDocView.isPassECMList()){
                    checkMandateDocView.setAllowCheckComplete(false);
                    checkMandateDocView.setAllowUpdateReason(false);
                    checkMandateDocView.setAllowUpdateRemark(false);
                } else {
                    checkMandateDocView.setAllowCheckComplete(true);
                    checkMandateDocView.setMessage("ECM is down, or the Application Number is NOT in ECM. Please check Mandate Doc manually");
                }
            } else if(mandateDocAccessView.getAccessType().equals(AccessType.CHECKER)) {
                checkMandateDocView.setAllowCheckComplete(true);
                checkMandateDocView.setAllowUpdateReason(true);
                checkMandateDocView.setAllowUpdateRemark(true);
            }

            checkMandateDocView.setRoleId(role.getId());
            checkMandateDocView.setStepId(stepId);
            checkMandateDocView.setForStageId(stageId);
        } else {
            checkMandateDocView.setMessage("You don't have an access right for this step, please contact admin.");
        }
        log.debug("-- return chechMandateDocView: {}", checkMandateDocView);
        return checkMandateDocView;
    }

    private Map<String, MandateDocView> _mapDBWithBRMS(Map<String, MandateDocView> mandateDocViewMap, Map<String, MandateDocView> brmsMandateViewMap){
        Map<String, MandateDocView> _finalMandateViewMap = new ConcurrentHashMap<String, MandateDocView>();
        if(mandateDocViewMap != null && mandateDocViewMap.size() > 0){

            for(MandateDocView mandateDocView : mandateDocViewMap.values()){
                MandateDocView brmsMandateDocView = brmsMandateViewMap.get(mandateDocView.getEcmDocTypeId());
                if(brmsMandateDocView == null){
                    continue;
                }
                mandateDocView.setCustomerInfoSimpleViewList(brmsMandateDocView.getCustomerInfoSimpleViewList());
                mandateDocView.setDisplay(brmsMandateDocView.isDisplay());
                mandateDocView.setDocLevel(brmsMandateDocView.getDocLevel());
                mandateDocView.setNumberOfDoc(brmsMandateDocView.getNumberOfDoc());
                _finalMandateViewMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);
            }

            for(MandateDocView brmsMandateDocView : brmsMandateViewMap.values()){
                if(_finalMandateViewMap.get(brmsMandateDocView.getEcmDocTypeId()) == null){
                    _finalMandateViewMap.put(brmsMandateDocView.getEcmDocTypeId(), brmsMandateDocView);
                }
            }
            return _finalMandateViewMap;

        } else {
            return brmsMandateViewMap;
        }
    }

    private CheckMandateDocView _mapECMDocumentToMandateDoc(List<ECMDetail> ecmDetailList, CheckMandateDocView checkMandateDocView, Map<String, ECMTypeName> ecmTypeNameMap){
        log.debug("-- begin _mapECMDocumentToMandateDoc --");
        Map<String, MandateDocView> mandateDocMap = checkMandateDocView.getMandatoryDocumentsMap();
        if(mandateDocMap == null)
            mandateDocMap = new ConcurrentHashMap<String, MandateDocView>();
        Map<String, MandateDocView> optionalDocMap = checkMandateDocView.getOptionalDocumentsMap();
        if(optionalDocMap == null)
            optionalDocMap = new ConcurrentHashMap<String, MandateDocView>();
        Map<String, MandateDocView> otherDocMap = new ConcurrentHashMap<String, MandateDocView>();
        //Add ECM Document into ManadateDoc;
        for(ECMDetail ecmDetail : ecmDetailList){
            log.debug("map ecmDetail: {}", ecmDetail);
            if(ecmDetail.getEcmDocId() != null){

                boolean isOthers = true;
                String ecmDocTypeCode = ecmDetail.getTypeCode();
                MandateDocFileNameView mandateDocFileNameView = checkMandateDocTransform.transformToView(ecmDetail, userToken);

                //Set ECM File into Mandate Doc Map
                MandateDocView mandateDocView = mandateDocMap.get(ecmDocTypeCode);
                if(!Util.isNull(mandateDocView)){
                    isOthers = false;
                    mandateDocView.setEcmDocTypeDesc(ecmDetail.getTypeNameTH());

                    //Set File into MandateDocView
                    List<MandateDocFileNameView> mandateFileNameList = mandateDocView.getMandateDocFileNameViewList();
                    if(mandateFileNameList == null){
                        mandateFileNameList = new ArrayList<MandateDocFileNameView>();
                    }
                    MandateDocFileNameView _tmpMandateDocFileView = new MandateDocFileNameView();
                    _tmpMandateDocFileView.updateValues(mandateDocFileNameView);
                    mandateFileNameList.add(_tmpMandateDocFileView);
                    mandateDocView.setMandateDocFileNameViewList(mandateFileNameList);

                    //Add Mandate Doc View back to _tmp
                    log.debug("put in MandateList: {}", mandateDocView);
                    mandateDocMap.put(mandateDocView.getEcmDocTypeId(), mandateDocView);
                }

                //Set ECM File into Optional Doc Map
                MandateDocView optionalDocView = optionalDocMap.get(ecmDocTypeCode);
                if(!Util.isNull(optionalDocView)){
                    isOthers = false;
                    optionalDocView.setEcmDocTypeDesc(ecmDetail.getTypeNameTH());
                    //Set File into MandateDocView
                    List<MandateDocFileNameView> optionalFileNameList = optionalDocView.getMandateDocFileNameViewList();
                    if(optionalFileNameList == null){
                        optionalFileNameList = new ArrayList<MandateDocFileNameView>();
                    }
                    MandateDocFileNameView _tmpOptionalDocFileView = new MandateDocFileNameView();
                    _tmpOptionalDocFileView.updateValues(mandateDocFileNameView);
                    optionalFileNameList.add(_tmpOptionalDocFileView);
                    optionalDocView.setMandateDocFileNameViewList(optionalFileNameList);

                    log.debug("put in OptionalList: {}", optionalDocView);
                    optionalDocMap.put(optionalDocView.getEcmDocTypeId(), optionalDocView);
                }

                if(isOthers){
                    log.debug("is others document");
                    MandateDocView otherDocView = otherDocMap.get(ecmDocTypeCode);
                    if(otherDocView == null) {
                        otherDocView = new MandateDocView();
                        otherDocView.setDocLevel(DocLevel.NA);
                        otherDocView.setNumberOfDoc(0);
                        otherDocView.setDisplay(true);
                        otherDocView.setBrmsDescList(null);
                        otherDocView.setCustomerInfoSimpleViewList(null);
                        otherDocView.setDocMandateType(DocMandateType.OTHER);
                        otherDocView.setEcmDocTypeId(ecmDocTypeCode);
                        otherDocView.setEcmDocTypeDesc(ecmDetail.getTypeNameTH());
                    }

                    otherDocView.setNumberOfDoc(otherDocView.getNumberOfDoc() + 1);

                    //Set File into MandateDocView
                    List<MandateDocFileNameView> otherFileNameList = otherDocView.getMandateDocFileNameViewList();
                    if(otherFileNameList == null){
                        otherFileNameList = new ArrayList<MandateDocFileNameView>();
                    }
                    MandateDocFileNameView otherDocFileView = new MandateDocFileNameView();
                    otherDocFileView.updateValues(mandateDocFileNameView);
                    otherFileNameList.add(otherDocFileView);

                    otherDocView.setMandateDocFileNameViewList(otherFileNameList);
                    log.debug("put in otherList: {}", otherDocView);
                    otherDocMap.put(otherDocView.getEcmDocTypeId(), otherDocView);
                }
            }
        }

        for(MandateDocView mandateDocView : mandateDocMap.values()){
            if(Util.isEmpty(mandateDocView.getEcmDocTypeDesc())){
                ECMTypeName ecmTypeName = ecmTypeNameMap.get(mandateDocView.getEcmDocTypeId());
                if(ecmTypeName != null)
                    mandateDocView.setEcmDocTypeDesc(ecmTypeName.getTypeNameTH());

            }
        }

        for(MandateDocView mandateDocView : optionalDocMap.values()){
            if(Util.isEmpty(mandateDocView.getEcmDocTypeDesc())){
                ECMTypeName ecmTypeName = ecmTypeNameMap.get(mandateDocView.getEcmDocTypeId());
                if(ecmTypeName != null)
                    mandateDocView.setEcmDocTypeDesc(ecmTypeName.getTypeNameTH());
            }
        }

        checkMandateDocView.setMandatoryDocumentsMap(mandateDocMap);
        checkMandateDocView.setOptionalDocumentsMap(optionalDocMap);
        checkMandateDocView.setOtherDocumentsMap(otherDocMap);

        return checkMandateDocView;
    }

    private void _updateECMDocTypeDesc(CheckMandateDocView checkMandateDocView, Map<String, ECMTypeName> ecmTypeNameMap){
        log.debug("-- begin _updateECMDocTypeDesc:");
        for(MandateDocView mandateDocView : checkMandateDocView.getMandatoryDocumentsMap().values()){
            if(Util.isEmpty(mandateDocView.getEcmDocTypeDesc())){
                ECMTypeName ecmTypeName = ecmTypeNameMap.get(mandateDocView.getEcmDocTypeId());
                if(ecmTypeName != null)
                    mandateDocView.setEcmDocTypeDesc(ecmTypeName.getTypeNameTH());

            }
        }

        for(MandateDocView mandateDocView : checkMandateDocView.getOptionalDocumentsMap().values()){
            if(Util.isEmpty(mandateDocView.getEcmDocTypeDesc())){
                ECMTypeName ecmTypeName = ecmTypeNameMap.get(mandateDocView.getEcmDocTypeId());
                if(ecmTypeName != null)
                    mandateDocView.setEcmDocTypeDesc(ecmTypeName.getTypeNameTH());
            }
        }
    }

    private void _updateCompletedFlag(CheckMandateDocView checkMandateDocView){
        for(MandateDocView mandateDocView : checkMandateDocView.getMandatoryDocumentsMap().values()){
            if(mandateDocView.getNumberOfDoc() > mandateDocView.getMandateDocFileNameViewList().size()){
                mandateDocView.setCompletedFlag(RadioValue.NO);
            } else {
                mandateDocView.setCompletedFlag(RadioValue.YES);
            }
        }

        for(MandateDocView optionalDocView : checkMandateDocView.getOptionalDocumentsMap().values()) {
            optionalDocView.setCompletedFlag(RadioValue.NA);
        }
    }

    //Save Check Mandate Doc View//
    public void saveMandateDoc(long stepId, long caseId, CheckMandateDocView checkMandateDocView){
        log.debug("-- begin saveMandateDoc: stepId {}, caseId: {}", stepId, caseId);
        StepView stepView = stepControl.getStepView(stepId);
        User user = getCurrentUser();
        Role role = user.getRole();

        //1. Get Access Right for Document Access.
        MandateDocAccessView mandateDocAccessView = mandateDocAccessControl.getMandateDocAccessView(stepId, role.getId());
        log.debug("mandateDocAccessView: {}", mandateDocAccessView);

        if(mandateDocAccessView != null) {
            if(AccessType.MAKER.equals(mandateDocAccessView.getAccessType()) || AccessType.CHECKER.equals(mandateDocAccessView.getAccessType())) {
                log.debug("AccessType: {}", mandateDocAccessView.getAccessType());
                deleteMandateDoc(stepId, caseId);
                MandateDocSummary mandateDocSummary = checkMandateDocTransform.transformToModel(checkMandateDocView, user);
                mandateDocSummaryDAO.persist(mandateDocSummary);
                mandateDocDetailDAO.persist(mandateDocSummary.getMandateDocDetailList());
            }
        }
    }

    public void deleteMandateDoc(long stepId, long caseId){
        log.debug("-- begin deleteMandateDoc: stepId: {} caseId: {}", stepId, caseId);
        StepView stepView = stepControl.getStepView(stepId);
        StageView stageView = stepView.getStageView();
        User user = getCurrentUser();
        Role role = user.getRole();

        if(StageValue.PRESCREEN.value() == stageView.getId()){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(caseId);
            MandateDocSummary mandateDocSummary = null;
            if(workCasePrescreen.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(caseId, stepId, StageValue.APPRAISAL.value(), role.getId());
            } else {
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(caseId, stepId, StageValue.PRESCREEN.value(), role.getId());
            }

            if(mandateDocSummary != null){
                if(mandateDocSummary.getMandateDocDetailList() != null && mandateDocSummary.getMandateDocDetailList().size() > 0){
                    mandateDocDetailDAO.delete(mandateDocSummary.getMandateDocDetailList());
                    mandateDocSummary.setMandateDocDetailList(null);
                }
            }
        } else {
            WorkCase workCase = workCaseDAO.findById(caseId);
            MandateDocSummary mandateDocSummary = null;
            if(workCase.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(caseId, stepId, StageValue.APPRAISAL.value(), role.getId());
            } else {
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(caseId, stepId, StageValue.FULL_APPLICATION.value(), role.getId());
            }
            if(mandateDocSummary != null){
                if(mandateDocSummary.getMandateDocDetailList() != null && mandateDocSummary.getMandateDocDetailList().size() > 0){
                    mandateDocDetailDAO.delete(mandateDocSummary.getMandateDocDetailList());
                    mandateDocSummary.setMandateDocDetailList(null);
                }
            }
        }
    }

    public boolean isMandateDocCompleted(long workCaseId, long workCasePrescreenId, long stepId){
        log.debug("-- begin isMandateDocCompleted --");

        StepView stepView = stepControl.getStepView(stepId);
        User user = getCurrentUser();
        Role role = user.getRole();

        MandateDocSummary mandateDocSummary = null;
        if(workCasePrescreenId > 0){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            if(workCasePrescreen.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(workCasePrescreenId, stepId, StageValue.APPRAISAL.value(), role.getId());
            } else {
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCasePrescreenIdForStepRole(workCasePrescreenId, stepId, StageValue.PRESCREEN.value(), role.getId());
            }
        }
        else {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(workCase.getParallelAppraisalFlag() == ParallelAppraisalStatus.REQUESTING_PARALLEL.value()){
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(workCaseId, stepId, StageValue.APPRAISAL.value(),user.getRole().getId());
            } else {
                mandateDocSummary = mandateDocSummaryDAO.findByWorkCaseIdForStepRole(workCaseId, stepId, StageValue.FULL_APPLICATION.value(),user.getRole().getId());
            }
        }

        MandateDocAccessView mandateDocAccessView = mandateDocAccessControl.getMandateDocAccessView(stepId, role.getId());
        if(mandateDocSummary == null){
            if(mandateDocAccessView!=null && mandateDocAccessView.getAccessType()!=null){
                if(AccessType.MAKER.equals(mandateDocAccessView.getAccessType()) || AccessType.CHECKER.equals(mandateDocAccessView.getAccessType())){
                    return false;
                }
            }
        }
        if(mandateDocSummary != null && mandateDocSummary.getMandateDocDetailList() != null){

            for(MandateDocDetail mandateDocDetail : mandateDocSummary.getMandateDocDetailList()){
                if(RadioValue.NO.equals(mandateDocDetail.getCompletedFlag())){
                    if(AccessType.MAKER.equals(mandateDocAccessView.getAccessType())){
                        if(DocMandateType.MANDATE.equals(mandateDocDetail.getMandateType())){
                            return false;
                        }
                    } else if(AccessType.CHECKER.equals(mandateDocAccessView.getAccessType())){
                        return false;
                    }
                }
            }
        }

        return true;
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
            log.error("-- Error while get Token reason is {}", e);
        }
    }
}
