package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.UWResultColor;
import com.clevel.selos.model.db.master.ApprovalAuthority;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.UWRuleName;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExSumDecisionView;
import com.clevel.selos.model.view.ExSumReasonView;
import com.clevel.selos.model.view.ExSummaryView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "executiveSummary")
public class ExecutiveSummary extends BaseController {

    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    private Long workCaseId;
    private Long stepId;
    private Long statusId;

    private String messageHeader;
    private String message;
    private String severity;

    private ExSummaryView exSummaryView;

    @Inject
    private UserDAO userDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ReasonDAO reasonDAO;
    @Inject
    private ApprovalAuthorityDAO approvalAuthorityDAO;
    @Inject
    private UWRuleNameDAO uwRuleNameDAO;

    @Inject
    private ExSummaryControl exSummaryControl;

    @Inject
    CustomerTransform customerTransform;

    //*** Drop down List ***//
    private List<ApprovalAuthority> authorizationDOAList;
    private List<Reason> reasonList;

    //
    private ExSumReasonView reason;
    private ExSumReasonView selectDeviate;

    private List<CustomerInfoView> customerInfoViewList;
    private long customerId;
    private CustomerInfoView selectCustomer;
    private ExSumDecisionView selectDeviateDecision;

    private ExSumDecisionView exSumDecisionView;

    private List<UWRuleName> uwRuleNameList = new ArrayList<UWRuleName>();

    private int rowIndex;

    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;

    public ExecutiveSummary() {
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");

        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");

        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            stepId = Util.parseLong(session.getAttribute("stepId"), 0);
            statusId = Util.parseLong(session.getAttribute("statusId"), 0);

            reasonList = new ArrayList<Reason>();
            authorizationDOAList = approvalAuthorityDAO.getList();

            reason = new ExSumReasonView();

            customerInfoViewList = exSummaryControl.getCustomerList(workCaseId);

            exSumDecisionView = new ExSumDecisionView();

            uwRuleNameList = uwRuleNameDAO.findAll();

            exSummaryView = new ExSummaryView();
            exSummaryView.reset();
            exSummaryView = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId, statusId);

            if(exSummaryView == null){
                exSummaryView = new ExSummaryView();
                exSummaryView.reset();
            }

            exSummaryView.setUwCode("6500000000");

            //for reasonList
            if(exSummaryView.getDecision() == 2){ //1 approve , 2 deviate , 3 reject
                reasonList = reasonDAO.getDeviateList();
            } else if(exSummaryView.getDecision() == 3){
                reasonList = reasonDAO.getRejectList();
            } else {
                reasonList = new ArrayList<Reason>();
            }
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            loadFieldControl(workCaseId, Screen.EXECUTIVE_SUMMARY, ownerCaseUserId);
            onChangeRM008();
            onChangeRM020();
            onChangeRM204();
        }
    }

    public void onSaveExecutiveSummary() {
        log.debug("onSaveExecutiveSummary :::");
        if(exSummaryView.getApplicationColorResult() != UWResultColor.RED) {
            if(!getUWResultColorRed()) {
                if (exSummaryView.getDecision() == 2 || exSummaryView.getDecision() == 3) { //1 approve , 2 deviate , 3 reject
                    if (exSummaryView.getDeviateCode() == null || exSummaryView.getDeviateCode().size() < 1) {
                        messageHeader = msg.get("app.header.information");
                        message = "Save Ex Summary Failed. " +
                                "<br/><br/> Cause : Reason is null.";
                        severity = "info";
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
        }

        try {
            exSummaryControl.saveExSummary(exSummaryView, workCaseId);

            messageHeader = msg.get("app.header.information");
            message = "Save Ex Summary data success.";
            severity = "info";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.error");
            if (ex.getCause() != null) {
                message = "Save Ex Summary data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Ex Summary data failed. Cause : " + ex.getMessage();
            }
            severity = "alert";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    private boolean getUWResultColorRed(){
        boolean colorRed = false;
        List<ExSumDecisionView> exSumDecisionViewList = exSummaryView.getExSumDecisionListView();
        for(ExSumDecisionView item : exSumDecisionViewList){
            if(item.getFlag() == UWResultColor.RED){
                colorRed = true;
                break;
            }
        }

        return colorRed;
    }
    public void onChangeDeviate(){
        exSummaryView.setDeviateCode(new ArrayList<ExSumReasonView>());
        if(exSummaryView.getDecision() == 2){ //1 approve , 2 deviate , 3 reject
            reasonList = reasonDAO.getDeviateList();
        } else if(exSummaryView.getDecision() == 3){
            reasonList = reasonDAO.getRejectList();
        } else {
            reasonList = new ArrayList<Reason>();
        }
    }

    public void onAddReason() {
        if(reason.getId() != 0){
            Reason findReason = reasonDAO.findById(reason.getId());
            if(findReason != null){
                ExSumReasonView exSumReasonView = new ExSumReasonView();
                exSumReasonView.setCode(findReason.getCode());
                exSumReasonView.setDescription(findReason.getDescription());
                exSummaryView.getDeviateCode().add(exSumReasonView);
            }
        }
    }

    public void onDeleteDeviate(){
        exSummaryView.getDeviateCode().remove(selectDeviate);
    }

    public void onChangeRM008(){
        if(exSummaryView.getRm008Code() == 0){
            exSummaryView.setRm008Remark("");
            setDisabledValue("RM008Remark",true);
        } else {
            setDisabledValue("RM008Remark",false);
        }
    }

    public void onChangeRM204(){
        if(exSummaryView.getRm204Code() == 0){
            exSummaryView.setRm204Remark("");
            setDisabledValue("RM024Remark",true);
        } else {
            setDisabledValue("RM024Remark",false);
        }
    }

    public void onChangeRM020(){
        if(exSummaryView.getRm020Code() == 0){
            exSummaryView.setRm020Remark("");
            setDisabledValue("RM020Remark",true);
        } else {
            setDisabledValue("RM020Remark",false);
        }
    }

    public void onInitAddDeviate(){
        exSumDecisionView = new ExSumDecisionView();
        customerId = 0;
        modeForButton = ModeForButton.ADD;
    }

    public void onSelectEditDeviate(){
        try {
            Cloner cloner = new Cloner();
            exSumDecisionView = cloner.deepClone(selectDeviateDecision);
            customerId = exSumDecisionView.getCustomerId();
            onChangeRuleName();
            modeForButton = ModeForButton.EDIT;
        } catch (Exception e) {
            log.error("onSelectEditDeviate Exception : {}",e);
        }
    }

    public void onAddDeviate(){
        exSumDecisionView.setCanEdit(true);

        if(customerId != 0){
            exSumDecisionView.setCustomerId(customerId);
            exSumDecisionView.setCusName(customerDAO.findById(customerId).getDisplayName());
        } else {
            exSumDecisionView.setCustomerId(0);
            exSumDecisionView.setCusName("Application");
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            exSummaryView.getExSumDecisionListView().add(exSumDecisionView);
        }else{
            exSummaryView.getExSumDecisionListView().set(rowIndex, exSumDecisionView);
        }

        boolean complete = true;        //Change only failed to save
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteDeviateDecision() {
        if(selectDeviateDecision.getId() != 0){
            exSummaryView.getDeleteTmpList().add(selectDeviateDecision.getId());
        }
        exSummaryView.getExSumDecisionListView().remove(selectDeviateDecision);
    }

    public void onChangeRuleName(){
        if(exSumDecisionView.getUwRuleNameId() == 0){
            exSumDecisionView.setGroup("");
        } else {
            UWRuleName uwRuleName = uwRuleNameDAO.findById(exSumDecisionView.getUwRuleNameId());
            if(uwRuleName != null && uwRuleName.getId() != 0 && uwRuleName.getRuleGroup() != null && uwRuleName.getRuleGroup().getId() != 0){
                exSumDecisionView.setGroup(uwRuleName.getRuleGroup().getName());
                exSumDecisionView.setRuleName(uwRuleName.getName());
            } else {
                exSumDecisionView.setGroup("");
                exSumDecisionView.setRuleName("");
            }
        }
    }

    //GET SET
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public ExSummaryView getExSummaryView() {
        return exSummaryView;
    }

    public void setExSummaryView(ExSummaryView exSummaryView) {
        this.exSummaryView = exSummaryView;
    }

    public List<Reason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<Reason> reasonList) {
        this.reasonList = reasonList;
    }

    public List<ApprovalAuthority> getAuthorizationDOAList() {
        return authorizationDOAList;
    }

    public void setAuthorizationDOAList(List<ApprovalAuthority> authorizationDOAList) {
        this.authorizationDOAList = authorizationDOAList;
    }

    public ExSumReasonView getReason() {
        return reason;
    }

    public void setReason(ExSumReasonView reason) {
        this.reason = reason;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public CustomerInfoView getSelectCustomer() {
        return selectCustomer;
    }

    public void setSelectCustomer(CustomerInfoView selectCustomer) {
        this.selectCustomer = selectCustomer;
    }

    public ExSumDecisionView getExSumDecisionView() {
        return exSumDecisionView;
    }

    public void setExSumDecisionView(ExSumDecisionView exSumDecisionView) {
        this.exSumDecisionView = exSumDecisionView;
    }

    public List<UWRuleName> getUwRuleNameList() {
        return uwRuleNameList;
    }

    public void setUwRuleNameList(List<UWRuleName> uwRuleNameList) {
        this.uwRuleNameList = uwRuleNameList;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public ExSumDecisionView getSelectDeviateDecision() {
        return selectDeviateDecision;
    }

    public void setSelectDeviateDecision(ExSumDecisionView selectDeviateDecision) {
        this.selectDeviateDecision = selectDeviateDecision;
    }

    public ExSumReasonView getSelectDeviate() {
        return selectDeviate;
    }

    public void setSelectDeviate(ExSumReasonView selectDeviate) {
        this.selectDeviate = selectDeviate;
    }
}

