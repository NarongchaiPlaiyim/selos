package com.clevel.selos.controller;

import com.clevel.selos.dao.ext.map.RMTitleDAO;
import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.dao.stp.STPExecutor;
import com.clevel.selos.exception.ApplicationRuntimeException;
import com.clevel.selos.integration.*;
import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;
import com.clevel.selos.integration.brms.service.EndPointImp;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.integration.email.EmailService;
import com.clevel.selos.integration.email.Template1;
import com.clevel.selos.integration.rlos.appin.model.AppInProcessResult;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.ext.map.RMTitle;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "welcomePage")
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
    Logger rlosLog;
    @Inject
    @BRMS
    Logger brmsLog;
    @Inject
    @COMS
    Logger comsLog;

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
    @Inject
    RLOSInterface rlos;
    @Inject
    DWHInterface dwh;

    @Inject
    EndPointImp endPointImp;

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
    RMTitleDAO rmTitleDAO;

//    @Inject
//    @Config(name = "system.name")
//    String system;

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
//        log.debug("system: {}",system);
        userAuditor.addSucceed("user1", "test", "test action");
        rmAuditor.add("user1", "test", "test RM", new Date(), ActionResult.SUCCESS, "", new Date(), "12345");
        ncbAuditor.add("user1", "test", "test NCB", new Date(), ActionResult.SUCCESS, "", new Date(), "67890");
    }

    public void testBRMS() {
        try {
            List<PreScreenResponse> preScreenResponseList = brms.checkPreScreenRule(new PreScreenRequest());
            log.debug("{}", preScreenResponseList);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testRLOSCSI() {
        try {
            CSIResult csiResult;
            CSIInputData csiInputData = new CSIInputData();

            AccountInfoId idModel = new AccountInfoId();
            idModel.setDocumentType(DocumentType.CITIZEN_ID);
            idModel.setIdNumber("3100203117607");
            List<AccountInfoId> idModelList = new ArrayList<AccountInfoId>();
            idModelList.add(idModel);

            AccountInfoName nameModel = new AccountInfoName();
            nameModel.setNameEn("CHUTIMA");
            nameModel.setSurnameEn("JITBANTHAO");
            List<AccountInfoName> nameModelList = new ArrayList<AccountInfoName>();
            nameModelList.add(nameModel);

            csiInputData.setIdModelList(idModelList);
            csiInputData.setNameModelList(nameModelList);
            csiResult = rlos.getCSIData("10001", csiInputData);
            log.debug("csi result : {}", csiResult);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testDWHObligation() {
        try {
            ObligationResult obligationResult = new ObligationResult();
            List<String> customerList = new ArrayList<String>();
            customerList.add("1234");
            customerList.add("1235");
            obligationResult = dwh.getObligationData("BDM001", customerList);
            log.debug("obligation result : {}", obligationResult);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testRLOSAppIn() {
        try {
            AppInProcessResult appInProcessResult = new AppInProcessResult();
            List<String> citizenIdList = new ArrayList<String>();
            citizenIdList.add("11111");
            citizenIdList.add("22222");
            appInProcessResult = rlos.getAppInProcessData("BDM001", citizenIdList);
            log.debug("testRLOSAppIn result : {}", appInProcessResult);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testBankStatement() {
        try {
            DWHBankStatementResult bankStatementResult = new DWHBankStatementResult();
            Date fromDate = Util.strToDateFormat("082013", "MMyyyy");
            bankStatementResult = dwh.getBankStatementData("BDM001", "3042582720", fromDate, 12);
            log.debug("BankStatement result : {}", bankStatementResult);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testRMTitle() {
        try {
            RMTitle rmTitle = rmTitleDAO.findOneByCriteria(Restrictions.eq("rmTitle", "MR"));
            log.debug("rmTitle : {}",rmTitle);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
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
        dateTh = new Date();
        dateEn = new Date();
    }

    public void on001() {
        log.debug("on001");
        validationStr = validationMsg.get("001");
        exceptionStr = exceptionMsg.get("001");
        log.debug("v: {}, e: {}", validationStr, exceptionStr);
    }

    public void on002() {
        log.debug("on002");
        validationStr = validationMsg.get("002");
        exceptionStr = exceptionMsg.get("501");
        log.debug("v: {}, e: {}", validationStr, exceptionStr);
    }

    public void testException() {
        log.debug("testException");
        try {
            bpmInterface.getInboxList();
//            throw new BPMInterfaceException(null,"101","test BPM exception!");
//            throw new EmailInterfaceException(null,"XXX","test EMAIL exception!");
        } catch (ApplicationRuntimeException e) {
            log.error("", e);
            log.debug("cause stack: {}", ExceptionUtils.getStackTrace(e.getCause()));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Runtime exception!", "Exception Code: " + e.getCode() + ", Message: " + e.getMessage() + ", stack trace: " + ExceptionUtils.getMessage(e.getCause()));
//            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    @Inject
    @Template1
    EmailService email;

    public void testEmail() {
        log.debug("testEmail.");
        email.sendMail("user1@test.local", "test subject", "", new HashMap<String, String>());
    }

    RadioValue radioValue;

    public RadioValue getRadioValue() {
        return radioValue;
    }

    UserStatus userStatus;

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setRadioValue(RadioValue radioValue) {
        this.radioValue = radioValue;
    }

    public void reloadConfig() {
        log.debug("reloadConfig.");
    }

    public void onActionRM() {
        rmLog.debug("test RM log. ({})", new Date());
    }

    public void onActionNCB() {
        ncbLog.debug("test NCB log. ({})", new Date());
    }

    public void onActionSW() {
        swLog.debug("test SW log. ({})", new Date());
    }

    public void onActionEmail() {
        emailLog.debug("test Email log. ({})", new Date());
    }

    public void onActionRLOS() {
        rlosLog.debug("test RLOS log. ({})", new Date());
    }

    public void onActionBRMS() {
        brmsLog.debug("test BRMS log. ({})", new Date());
        try {
            com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceResponse response = endPointImp.callStandardPricingInterestRulesService(null);
            if (null != response) {
                brmsLog.debug("Response in not null");
            } else {
                brmsLog.debug("Response in null");
            }

            endPointImp.callDocumentAppraisalRulesService(null);
            endPointImp.callDocumentCustomerRulesService(null);
            endPointImp.callFullApplicationUnderwritingRulesService(null);
            endPointImp.callPrescreenUnderwritingRulesService(null);
            endPointImp.callStandardPricingFeeRulesService(null);
            endPointImp.callStandardPricingInterestRulesService(null);

        } catch (Exception e) {
            brmsLog.error("Exception : {}", e);
        }

    }

    public void onActionCOMS() {
        comsLog.debug("test COMS log. ({})", new Date());
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
        log.debug("group size: {}", businessGroups.size());
        selectedBusinessGroup = businessGroups.get(0);
        businessDescriptions = businessDescriptionDAO.getListByBusinessGroup(businessGroups.get(0));
        selectedBusinessDescription = businessDescriptions.get(0);
    }

    public void onChangeGroup() {
        log.debug("onChangeGroup. (selected: {})", selectedBusinessGroup);
        BusinessGroup businessGroup = businessGroupDAO.findById(selectedBusinessGroup.getId());
        log.debug("{}", businessGroup);
        businessDescriptions = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        selectedText = "GROUP: " + businessGroup.getName();
    }

    public void onChangeDescription() {
        log.debug("onChangeDescription.");
        BusinessDescription businessDescription = businessDescriptionDAO.findById(selectedBusinessDescription.getId());
        log.debug("{}", businessDescription);
        selectedText = "DESCRIPTION: " + businessDescription.getName();
    }

    @Inject
    STPExecutor STPExecutor;

    public void testStoredProcedure() {
        log.debug("testStoredProcedure");
        STPExecutor.getApplicationNumber("XX");
    }

    @Inject
    BPMInterface bpmInterface;

    public void testBPM() {
        log.debug("testBPM");
//        UserDTO userDTO = bpmInterface.getUserDTO();
//        log.debug("user: {}, password: {}",userDTO.getUserName(),userDTO.getPassword());
    }

    Date dateTh;
    Date dateEn;

    public void testCalendar() {
        log.debug("test Calendar.");
        log.debug("dateTh: {}, dateEn: {}",dateTh,dateEn);

        log.debug("getDateTimeString dateTh: {}",DateTimeUtil.getDateTimeStr(dateTh));
        log.debug("getDateTimeString dateEn: {}",DateTimeUtil.getDateTimeStr(dateEn));
    }

    public Date getDateTh() {
        return dateTh;
    }

    public void setDateTh(Date dateTh) {
        this.dateTh = dateTh;
    }

    public Date getDateEn() {
        return dateEn;
    }

    public void setDateEn(Date dateEn) {
        this.dateEn = dateEn;
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
