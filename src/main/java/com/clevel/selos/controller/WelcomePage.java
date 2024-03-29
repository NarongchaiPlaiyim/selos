package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.UWRuleResultControl;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.ext.map.RMTitleDAO;
import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.exception.ApplicationRuntimeException;
import com.clevel.selos.integration.*;
import com.clevel.selos.integration.bpm.model.BPMInbox;
import com.clevel.selos.integration.bpm.model.FieldName;
import com.clevel.selos.integration.bpm.model.OrderType;
import com.clevel.selos.integration.brms.service.EndPoint;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
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
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.model.view.UWRuleResultDetailView;
import com.clevel.selos.model.view.UWRuleResultSummaryView;
import com.clevel.selos.report.ReportService;
import com.clevel.selos.report.SimpleReport;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.business.CollateralBizTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import com.ilog.rules.decisionservice.DecisionServiceResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

@ViewScoped
@ManagedBean(name = "welcomePage")
public class WelcomePage implements Serializable {
    @Inject
    @SELOS
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
    COMSInterface coms;
    @Inject
    BPMInterface bpmInterface;
    @Inject
    NCBInterface ncbInterface;

    @Inject
    EndPoint endPointImp;

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

    @Inject
    CollateralBizTransform callateralBizTransform;

    @Inject
    private UWRuleResultControl uwRuleResultControl;

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
            //List<PreScreenResponse> preScreenResponseList = brms.checkPreScreenRule(new BRMSApplicationInfo());
            //log.debug("{}", preScreenResponseList);
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

    public void testCOMS() {
        try {
            AppraisalDataResult appraisalDataResult = new AppraisalDataResult();
            appraisalDataResult = coms.getAppraisalData("BDM001","PR5401-036-00001");
            log.debug("appraisalDataResult result : {}", appraisalDataResult);
            ProposeCollateralInfoView collateralDetailView = callateralBizTransform.transformAppraisalToProposeCollateralView(appraisalDataResult);
            log.debug("collateralDetailView result : {}", collateralDetailView);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testMyBox() {
        try {
            List<BPMInbox> bpmInboxList = bpmInterface.getMyBoxList("BDM001", FieldName.APPNUMBER, OrderType.ASCENDING, 5, 4);
            log.debug("bpmInboxList result : {}", bpmInboxList);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testReturnBox() {
        try {
            List<BPMInbox> bpmInboxList = bpmInterface.getReturnBoxList("BDM001", FieldName.APPNUMBER, OrderType.ASCENDING, 5, 1);
            log.debug("bpmInboxList result : {}", bpmInboxList);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testBDMUWBox() {
        try {
            List<BPMInbox> bpmInboxList = bpmInterface.getBDMUWBoxList("BDM001", FieldName.APPNUMBER, OrderType.ASCENDING, 5, 1);
            log.debug("bpmInboxList result : {}", bpmInboxList);
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
//        log.debug("DateTh: {}",dateTh);
//        Calendar calendar = Calendar.getInstance(new Locale("th", "TH"));
//        dateTh = calendar.getTime();
//        log.debug("DateTh: {}",dateTh);
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
//                .withChronology(BuddhistChronology.getInstance()).withLocale(defaultLocale);
//        DateTime dt = new DateTime(dateTh,BuddhistChronology.getInstance());
//        log.debug("dt: {}",dt);
//        dateTh = dt.toDate();
//        log.debug("DateTh2: {}",dateTh);

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
            DecisionServiceResponse response = endPointImp.callStandardPricingInterestRulesService(null);
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

    public void testCOMSAgreement() {
        try {
            log.debug("testCOMSAgreement.");
            coms.generateAgreement("10001",2020);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    public void testRejectLetter() {
        try {
            log.debug("testRejectLetter.");
            ncbInterface.generateRejectedLetter("10001",2,0);
        } catch (Exception e) {
            log.error("", e);
        }
//        log.debug("system: {}",system);
    }

    @Inject
    STPExecutor STPExecutor;

    public void testStoredProcedure() {
        log.debug("testStoredProcedure");
        STPExecutor.getApplicationNumber("XX", "REFXX", 1);
    }

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

        log.debug("getDateTimeString dateTh: {}",dateTh);
        log.debug("getDateTimeString dateEn: {}",dateEn);
    }

    Date endOfMonthTh;
    Date endOfMonthEn;

    public void testEndOfMonth() {
        endOfMonthTh = DateTimeUtil.getLastDayOfMonth(dateTh);
        endOfMonthEn = DateTimeUtil.getLastDayOfMonth(dateEn);
        System.out.println("endOfMonthTh : "+endOfMonthTh);
        System.out.println("endOfMonthEn : "+endOfMonthEn);
    }

    public void testNCBResult(){
        UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultControl.getUWRuleResultByWorkCasePrescreenId(3);
        ncbResultValidation(uwRuleResultSummaryView);
    }

    public void ncbResultValidation(UWRuleResultSummaryView uwRuleResultSummaryView){
        log.debug("ncbResultValidation()");
        if(uwRuleResultSummaryView!=null){
            Map<String, UWRuleResultDetailView> uwResultDetailMap = uwRuleResultSummaryView.getUwRuleResultDetailViewMap();
            if(uwResultDetailMap!=null){
               // canCheckPreScreen = true;
                for (Map.Entry<String, UWRuleResultDetailView> entry : uwResultDetailMap.entrySet())
                {
                    UWRuleResultDetailView uwRuleResultDetailView = entry.getValue();
                    if(uwRuleResultDetailView.getUwRuleNameView()!=null
                            && uwRuleResultDetailView.getUwRuleNameView().getUwRuleGroupView()!=null
                            && uwRuleResultDetailView.getUwRuleNameView().getUwRuleGroupView().getName()!=null
                            && uwRuleResultDetailView.getUwRuleNameView().getUwRuleGroupView().getName().equalsIgnoreCase("NCB")){
                        if(uwRuleResultDetailView.getRuleColorResult() == UWResultColor.RED){
                            log.debug("NCB Result is RED, auto reject case!");
                            try {
                                ncbInterface.generateRejectedLetter("10001",2,0);
                                //canCheckPreScreen = false;
                            } catch (Exception e) {
                                log.error("", e);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void calculateAge(){
        outputAge = Util.calYear(inputDate);
    }

    private Date inputDate;
    private int outputAge;

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public int getOutputAge() {
        return outputAge;
    }

    public void setOutputAge(int outputAge) {
        this.outputAge = outputAge;
    }

    @Inject
    @SimpleReport
    ReportService reportService;

    /*public StreamedContent genReport() {
        return reportService.getReportFile(new HashMap<String, Object>());
    }*/

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

    public Date getEndOfMonthTh() {
        return endOfMonthTh;
    }

    public void setEndOfMonthTh(Date endOfMonthTh) {
        this.endOfMonthTh = endOfMonthTh;
    }

    public Date getEndOfMonthEn() {
        return endOfMonthEn;
    }

    public void setEndOfMonthEn(Date endOfMonthEn) {
        this.endOfMonthEn = endOfMonthEn;
    }
}
