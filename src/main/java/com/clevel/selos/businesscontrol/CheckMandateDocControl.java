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
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.model.view.MandateDocResponseView;
import com.clevel.selos.model.view.MandateDocView;
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
    private com.clevel.selos.integration.filenet.ce.connection.CESessionToken CESessionToken;
    private String passwordEncrypt;

    private Map<String,List<ECMDetail>> listECMDetailMap;
    private Map<String, MandateDocView> mandateDocViewMap;
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











        if(!Util.isNull(mandateDoc)){
            checkMandateDocView = checkMandateDocTransform.transformToView(mandateDoc);

        } else {
            log.debug("-- Find by work case id = {} MandateDoc is {}   ", workCaseId, checkMandateDocView);
        }
        return checkMandateDocView;
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

    private String getToken() throws Exception{
        if (Util.isTrue(encryptionEnable)) {
            passwordEncrypt = encryptionService.decrypt(Base64.decodeBase64(password));
        } else {
            passwordEncrypt = password;
        }
        String userToken = CESessionToken.getTokenFromSession(username, passwordEncrypt);
        return userToken;
    }

    private String getURLByFNId(final String FNId, final String token){
        return address+"/getContent?objectStoreName="+objectStore+"&id="+FNId+"&objectType=document&ut=" + token;
    }


}
