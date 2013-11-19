package com.clevel.selos.integration.bpm;

import com.clevel.selos.exception.BPMInterfaceException;
import com.clevel.selos.filenet.bpm.connection.dto.UserDTO;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.filenet.bpm.services.impl.BPMServiceImpl;
import com.clevel.selos.filenet.bpm.util.constants.BPMConstants;
import com.clevel.selos.filenet.bpm.util.resources.BPMConfigurationsDTO;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import com.clevel.selos.ws.WSDataPersist;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Default
public class BPMInterfaceImpl implements BPMInterface, Serializable {
    @Inject
    @BPM
    Logger log;

    @Inject
    @Config(name = "interface.bpm.ce.uri")
    String ceURI;
    @Inject
    @Config(name = "interface.bpm.jaas.name")
    String jaasName;
    @Inject
    @Config(name = "interface.bpm.objectStore")
    String objectStore;
    @Inject
    @Config(name = "interface.bpm.pe.connectionPoint")
    String connectionPoint;
    @Inject
    @Config(name = "interface.bpm.workflow.name")
    String workflowName;
    @Inject
    @Config(name = "interface.bpm.username")
    String bpmUsername;
    @Inject
    @Config(name = "interface.bpm.password")
    String bpmPassword;

    @Inject
    EncryptionService encryptionService;
    @Inject
    @Config(name = "system.encryption.enable")
    String encryptionEnable;

    @Inject
    WSDataPersist wsDataPersist;
    @Inject
    @BPM
    SystemAuditor bpmAuditor;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    public BPMInterfaceImpl() {
    }

    @Override
    public boolean createCase(CaseCreationHistory caseCreationHistory) {
        log.debug("createCase. (detail: {})", caseCreationHistory);
        boolean success = true;
        Date now = new Date();
        log.debug("CE URI: {}, username: {}, password: {}", ceURI, bpmUsername, bpmPassword);

        HashMap<String, String> caseParameter = new HashMap<String, String>();
        caseParameter.put("JobName", caseCreationHistory.getJobName());
        caseParameter.put("CANumber", caseCreationHistory.getCaNumber());
        caseParameter.put("RefCANumber", caseCreationHistory.getOldCaNumber() == null ? "" : caseCreationHistory.getOldCaNumber()); //Optional for crs
        caseParameter.put("AccountNo1", caseCreationHistory.getAccountNo1() == null ? "" : caseCreationHistory.getAccountNo1()); //Optional for crs
        caseParameter.put("CustomerId", caseCreationHistory.getCustomerId());
        caseParameter.put("CustomerName", caseCreationHistory.getCustomerName());
        caseParameter.put("CitizenId", caseCreationHistory.getCitizenId());
        caseParameter.put("RequestType", Integer.toString(caseCreationHistory.getRequestType()));
        caseParameter.put("CustomerType", Integer.toString(caseCreationHistory.getCustomerType()));
        caseParameter.put("BDMUserName", caseCreationHistory.getBdmId());
        caseParameter.put("HubCode", caseCreationHistory.getHubCode());
        caseParameter.put("RegionCode", caseCreationHistory.getRegionCode());
        caseParameter.put("AppInDateBDM", caseCreationHistory.getAppInDateBDM());
        caseParameter.put("AppNumber", caseCreationHistory.getAppNumber());
        caseParameter.put("RefAppNumber", "");

        String linkKey = Util.getLinkKey(bpmUsername);
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getSystemUserDTO(), getConfigurationDTO());
            bpmService.launchCase(caseParameter);
            log.debug("[{}] BPM launch case successful.", linkKey);
            bpmAuditor.add(bpmUsername, "createCase", "", now, ActionResult.SUCCESS, "", linkKey);
        } catch (SELOSBPMException e) {
            success = false;
            caseCreationHistory.setStatus(ActionResult.FAILED);
            caseCreationHistory.setStatusDetail(msg.get(ExceptionMapping.BPM_NEW_CASE_EXCEPTION));
            log.error("[{}] {}", linkKey, msg.get(ExceptionMapping.BPM_NEW_CASE_EXCEPTION), e);
            bpmAuditor.add(bpmUsername, "createCase", "", now, ActionResult.FAILED, msg.get(ExceptionMapping.BPM_NEW_CASE_EXCEPTION), linkKey);
        }

        wsDataPersist.addNewCase(caseCreationHistory);
        return success;
    }

    @Override
    public void authenticate(String userName, String password) {
        log.debug("BPM authentication (userName: {}, password: [HIDDEN])", userName);
        Date now = new Date();
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userName);
        userDTO.setPassword(password);
        String linkKey = Util.getLinkKey(userName);
        try {
            new BPMServiceImpl(userDTO, getConfigurationDTO());
            log.debug("[{}] BPM authentication success.", linkKey);
            bpmAuditor.add(userName, "Authenticate", "", now, ActionResult.SUCCESS, "", linkKey);
        } catch (SELOSBPMException e) {
            log.error("[{}] Exception while authentication with BPM!", linkKey, e);
            bpmAuditor.add(userName, "Authenticate", "", now, ActionResult.FAILED, msg.get(ExceptionMapping.BPM_AUTHENTICATION_FAILED), linkKey);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_AUTHENTICATION_FAILED, msg.get(ExceptionMapping.BPM_AUTHENTICATION_FAILED, userName));
        } catch (Exception e) {
            log.error("[{}] Exception while authentication with BPM!", linkKey, e);
            bpmAuditor.add(userName, "Authenticate", "", now, ActionResult.FAILED, e.getMessage(), linkKey);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_AUTHENTICATION_FAILED, msg.get(ExceptionMapping.BPM_AUTHENTICATION_FAILED, userName));
        }
    }

    @Override
    public List<CaseDTO> getInboxList() {
        log.debug("getInboxList.");
        Date now = new Date();
        List<CaseDTO> caseDTOs;
        String linkKey = Util.getLinkKey(getUserDTO().getUserName());
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getUserDTO(), getConfigurationDTO());
            caseDTOs = bpmService.getCases(BPMConstants.BPM_QUEUE_PERSONAL_INBOX_NAME, BPMConstants.BPM_QUEUE_TYPE_PERSONALQ, null, null);
            log.debug("[{}] getInboxList success.", linkKey);
            bpmAuditor.add(getUserDTO().getUserName(), "getInboxList", "", now, ActionResult.SUCCESS, "", linkKey);
        } catch (Exception e) {
            log.error("[{}] Exception while get inbox list in BPM!", linkKey, e);
            bpmAuditor.add(getUserDTO().getUserName(), "getInboxList", "", now, ActionResult.FAILED, e.getMessage(), linkKey);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_EXCEPTION, msg.get(ExceptionMapping.BPM_GET_INBOX_EXCEPTION));
        }

        log.debug("getInboxList. (result size: {})", caseDTOs.size());
        return caseDTOs;
    }

    @Override
    public void dispatchCase(String queueName, String wobNumber, HashMap<String, String> fields) {
        log.debug("dispatchCase. (queueName: {}, wobNumber: {})", queueName, wobNumber);
        Date now = new Date();
        Util.listFields(fields);
        String linkKey = Util.getLinkKey(getUserDTO().getUserName());
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getUserDTO(), getConfigurationDTO());
            bpmService.dispatchCase(queueName, wobNumber, fields);
            log.debug("[{}] dispatchCase success.", linkKey);
            bpmAuditor.add(getUserDTO().getUserName(), "dispatchCase", "", now, ActionResult.SUCCESS, "", linkKey);
        } catch (SELOSBPMException e) {
            log.error("[{}] Exception while dispatch case in BPM!", linkKey, e);
            bpmAuditor.add(getUserDTO().getUserName(), "dispatchCase", "", now, ActionResult.FAILED, msg.get(ExceptionMapping.BPM_DISPATCH_EXCEPTION), linkKey);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_DISPATCH_EXCEPTION, msg.get(ExceptionMapping.BPM_DISPATCH_EXCEPTION));
        }
    }

    @Override
    public void lockCase(String queueName, String wobNumber) {
        log.debug("lockCase. (queueName: {}, wobNumber: {})", queueName, wobNumber);
        Date now = new Date();
        String linkKey = Util.getLinkKey(getUserDTO().getUserName());
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getUserDTO(), getConfigurationDTO());
            bpmService.lockCase(queueName, wobNumber);
            log.debug("[{}] lockCase success.", linkKey);
            bpmAuditor.add(getUserDTO().getUserName(), "lockCase", "", now, ActionResult.SUCCESS, "", linkKey);
        } catch (SELOSBPMException e) {
            log.error("[{}] Exception while locking case in BPM!", linkKey, e);
            bpmAuditor.add(getUserDTO().getUserName(), "lockCase", "", now, ActionResult.FAILED, msg.get(ExceptionMapping.BPM_LOCK_CASE_EXCEPTION), linkKey);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_LOCK_CASE_EXCEPTION, msg.get(ExceptionMapping.BPM_LOCK_CASE_EXCEPTION));
        }
    }

    @Override
    public void unLockCase(String queueName, String wobNumber) {
        log.debug("unLockCase. (queueName: {}, wobNumber: {})", queueName, wobNumber);
        Date now = new Date();
        String linkKey = Util.getLinkKey(getUserDTO().getUserName());
        try {
            BPMServiceImpl bpmService = new BPMServiceImpl(getUserDTO(), getConfigurationDTO());
            bpmService.unLockCase(queueName, wobNumber);
            log.debug("[{}] unLockCase success.", linkKey);
            bpmAuditor.add(getUserDTO().getUserName(), "unLockCase", "", now, ActionResult.SUCCESS, "", linkKey);
        } catch (SELOSBPMException e) {
            log.error("[{}] Exception while unlocking case in BPM!", linkKey, e);
            bpmAuditor.add(getUserDTO().getUserName(), "unLockCase", "", now, ActionResult.FAILED, msg.get(ExceptionMapping.BPM_UNLOCK_CASE_EXCEPTION), linkKey);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_UNLOCK_CASE_EXCEPTION, msg.get(ExceptionMapping.BPM_UNLOCK_CASE_EXCEPTION));
        }
    }

    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDTO.setUserName(userDetail.getUserName());
        String password;
        if (Util.isTrue(encryptionEnable)) {
            password = encryptionService.decrypt(Base64.decodeBase64(userDetail.getPassword()));
        } else {
            password = userDetail.getPassword();
        }
        userDTO.setPassword(password);
        log.debug("getUserDTO username: {}, password: [HIDDEN]", userDetail.getUserName());
        return userDTO;
    }

    private UserDTO getSystemUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(bpmUsername);
        String password;
        if (Util.isTrue(encryptionEnable)) {
            password = encryptionService.decrypt(Base64.decodeBase64(bpmPassword));
        } else {
            password = bpmPassword;
        }
        userDTO.setPassword(password);
        return userDTO;
    }

    private BPMConfigurationsDTO getConfigurationDTO() {
        BPMConfigurationsDTO bpmConfigurationsDTO = new BPMConfigurationsDTO();
        bpmConfigurationsDTO.setCEURI(ceURI);
        bpmConfigurationsDTO.setCaseWorkflowName(workflowName);
        bpmConfigurationsDTO.setConnectionPointName(connectionPoint);
        bpmConfigurationsDTO.setJassName(jaasName);
        bpmConfigurationsDTO.setObjectStoreName(objectStore);
        return bpmConfigurationsDTO;
    }
}
