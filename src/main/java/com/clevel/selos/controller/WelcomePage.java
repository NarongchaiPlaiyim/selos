package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.integration.*;
import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name="welcomePage")
public class WelcomePage implements Serializable {
    @Inject
    Logger log;
    @Inject
    @RM
    Logger rmLog;
    @Inject
    @NCB
    Logger ncbLog;
    @Inject
    @SafeWatch
    Logger swLog;
    @Inject
    @Email
    Logger emailLog;
    @Inject
    @RLOS
    Logger dwhLog;
    @Inject
    @BRMS
    Logger brmsLog;

    @Inject
    @NormalMessage
    Message normalMsg;
    @Inject
    @ValidationMessage
    Message validationMsg;
    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    String normalStr;
    String validationStr;
    String exceptionStr;

    @Inject
    RMInterface rm;
    @Inject
    BRMSInterface brms;

    //user auditor
    @Inject
    UserAuditor userAuditor;
    //system auditor
    @Inject
    @RM
    SystemAuditor rmAuditor;
    @Inject
    @NCB
    SystemAuditor ncbAuditor;

    @Inject
    @Config(name = "system.name")
    String system;

    private Date now;

    public WelcomePage() {
    }

    public void testRM() {
//        try {
//            CustomerInfo customerInfo = rm.getIndividualInfo("101","CI","3100300390029", RMInterface.DocumentType.CITIZEN_ID);
//            log.debug("{}",customerInfo);
//        } catch (Exception e) {
//            log.error("",e);
//        }
        log.debug("system: {}",system);
        userAuditor.addSucceed("user1","test","test action");
        rmAuditor.add("user1","test","test RM",new Date(), ActionResult.SUCCEED,"",new Date(),"12345");
        ncbAuditor.add("user1","test","test NCB",new Date(), ActionResult.SUCCEED,"",new Date(),"67890");
    }

    public void testBRMS() {
        try {
            List<PreScreenResponse> preScreenResponseList = brms.checkPreScreenRule(new PreScreenRequest());
            log.debug("{}",preScreenResponseList);
        } catch (Exception e) {
            log.error("",e);
        }
        log.debug("system: {}",system);
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation.");
        now = new Date();
//        reloadConfig();
//        onLoadDescription();
        normalStr = normalMsg.get("app.name");
        validationStr = validationMsg.get("001");
        exceptionStr = exceptionMsg.get("001");
    }

    public void on001() {
        log.debug("on001");
        validationStr = validationMsg.get("001");
        exceptionStr = exceptionMsg.get("001");
        log.debug("v: {}, e: {}",validationStr,exceptionStr);
    }

    public void on002() {
        log.debug("on002");
        validationStr = validationMsg.get("002");
        exceptionStr = exceptionMsg.get("501");
        log.debug("v: {}, e: {}",validationStr,exceptionStr);
    }

    public void reloadConfig() {
        log.debug("reloadConfig.");
    }

    public void onActionRM() {
        rmLog.debug("test RM log. ({})",new Date());
    }

    public void onActionNCB() {
        ncbLog.debug("test NCB log. ({})",new Date());
    }

    public void onActionSW() {
        swLog.debug("test SW log. ({})",new Date());
    }
    public void onActionEmail() {
        emailLog.debug("test Email log. ({})",new Date());
    }

    public void onActionRLOS() {
        dwhLog.debug("test RLOS log. ({})",new Date());
    }

    public void onActionBRMS() {
        brmsLog.debug("test BRMS log. ({})",new Date());
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Inject
    BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    BusinessGroupDAO businessGroupDAO;
    List<BusinessGroup> businessGroups;
    BusinessGroup selectedBusinessGroup;
    List<BusinessDescription> businessDescriptions;
    BusinessDescription selectedBusinessDescription;
    String selectedText;


    public void onLoadDescription() {
        log.debug("onLoadDescription.");
        businessGroups = businessGroupDAO.findAll();
        log.debug("group size: {}",businessGroups.size());
        selectedBusinessGroup = businessGroups.get(0);
        businessDescriptions = businessDescriptionDAO.getListByBusinessGroup(businessGroups.get(0));
        selectedBusinessDescription = businessDescriptions.get(0);
    }

    public void onChangeGroup() {
        log.debug("onChangeGroup. (selected: {})",selectedBusinessGroup);
        BusinessGroup businessGroup = businessGroupDAO.findById(selectedBusinessGroup.getId());
        log.debug("{}",businessGroup);
        businessDescriptions = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        selectedText = "GROUP: "+businessGroup.getName();
    }

    public void onChangeDescription() {
        log.debug("onChangeDescription.");
        BusinessDescription businessDescription = businessDescriptionDAO.findById(selectedBusinessDescription.getId());
        log.debug("{}",businessDescription);
        selectedText = "DESCRIPTION: "+businessDescription.getName();
    }

    public List<BusinessGroup> getBusinessGroups() {
        return businessGroups;
    }

    public void setBusinessGroups(List<BusinessGroup> businessGroups) {
        this.businessGroups = businessGroups;
    }

    public List<BusinessDescription> getBusinessDescriptions() {
        return businessDescriptions;
    }

    public void setBusinessDescriptions(List<BusinessDescription> businessDescriptions) {
        this.businessDescriptions = businessDescriptions;
    }

    public BusinessGroup getSelectedBusinessGroup() {
        return selectedBusinessGroup;
    }

    public void setSelectedBusinessGroup(BusinessGroup selectedBusinessGroup) {
        this.selectedBusinessGroup = selectedBusinessGroup;
    }

    public BusinessDescription getSelectedBusinessDescription() {
        return selectedBusinessDescription;
    }

    public void setSelectedBusinessDescription(BusinessDescription selectedBusinessDescription) {
        this.selectedBusinessDescription = selectedBusinessDescription;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public String getNormalStr() {
        return normalStr;
    }

    public void setNormalStr(String normalStr) {
        this.normalStr = normalStr;
    }

    public String getValidationStr() {
        return validationStr;
    }

    public void setValidationStr(String validationStr) {
        this.validationStr = validationStr;
    }

    public String getExceptionStr() {
        return exceptionStr;
    }

    public void setExceptionStr(String exceptionStr) {
        this.exceptionStr = exceptionStr;
    }
}
