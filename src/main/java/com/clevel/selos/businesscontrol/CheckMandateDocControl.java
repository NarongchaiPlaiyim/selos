package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.MandateDocDAO;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
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
    @SELOS
    private Logger log;
    @Inject
    private MandateDocDAO mandateDocDAO;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    @Inject
    private ECMInterface ecmInterface;
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
    @Inject
    public CheckMandateDocControl() {

    }

    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
        mandateDoc = null;
    }

    private CheckMandateDocView  getMandateDoc(final long workCaseId){
        log.info("-- getMandateDoc WorkCaseId : {}", workCaseId);
        mandateDoc = mandateDocDAO.findByWorkCaseId(workCaseId);
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
