package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.MessageDialogSeverity;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.DBRDetailView;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.model.view.LoanAccountTypeView;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "dbrInfo")
public class DBRInfo extends BaseController {
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

    @Inject
    DBRControl dbrControl;

    @Inject
    LoanAccountTypeControl loanAccountTypeControl;

    @Inject
    NCBInfoControl ncbInfoControl;
    @Inject
    private CalculationControl calculationControl;
    @Inject
    private ProposeLineControl proposeLineControl;

    // message //
    private String messageHeader;
    private String message;
    private String severity;

    // *** Content ***///
    private DBRView dbr;
    private List<DBRDetailView> dbrDetails;
    private List<LoanAccountTypeView> loanAccountTypes;
    private List<NCBDetailView> ncbDetails;

    //**DBR Detail
    private DBRDetailView selectedItem;
    private int rowIndex;
    private boolean isUpdate;


    //session
    private long workCaseId;

    private boolean isComplete;

    public DBRInfo() {
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if( (Long)session.getAttribute("workCaseId") != 0){
            checkSession = true;
        }

        return checkSession;
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
            workCaseId = (Long)session.getAttribute("workCaseId");
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

            loadFieldControl(workCaseId, Screen.DBR_INFO, ownerCaseUserId);

            selectedItem = new DBRDetailView();

            dbr = new DBRView();
            dbr = dbrControl.getDBRByWorkCase(workCaseId);

            dbrDetails = new ArrayList<DBRDetailView>();
            if (dbr.getDbrDetailViews() != null && !dbr.getDbrDetailViews().isEmpty()) {
                dbrDetails = dbr.getDbrDetailViews();
            }

            loanAccountTypes = loanAccountTypeControl.getListLoanTypeByWorkCaseId(workCaseId);
            ncbDetails = ncbInfoControl.getNCBForCalDBR(workCaseId);
        }
    }

    public void initAddDBRDetail() {
        isComplete = false;
        selectedItem = new DBRDetailView();
    }

    public void onAddDBRDetail() {
        log.debug("onAdd DBR Detail :{}", selectedItem);
        RequestContext context = RequestContext.getCurrentInstance();
        if (selectedItem == null || loanAccountTypes.isEmpty()) {
            return;
        }
        for (LoanAccountTypeView loanAccountTypeView : loanAccountTypes) {
            if (loanAccountTypeView.getId() == selectedItem.getLoanAccountTypeView().getId()) {
                LoanAccountTypeView loanAccountType = new LoanAccountTypeView();
                loanAccountType.setId(selectedItem.getLoanAccountTypeView().getId());
                loanAccountType.setName(loanAccountTypeView.getName());
                loanAccountType.setCalculateType(loanAccountTypeView.getCalculateType());
                selectedItem.setLoanAccountTypeView(loanAccountType);
                break;
            }
        }

        selectedItem.setDebtForCalculate(BigDecimal.ZERO);
        if (isUpdate) {
            dbrDetails.set(rowIndex, selectedItem);
        } else {
            dbrDetails.add(selectedItem);
        }

        isComplete = true;
        context.addCallbackParam("functionComplete", isComplete);

    }

    public void initEditDBRDetail() {
        isComplete = false;
        log.debug("initEditDBRDetail :{}", selectedItem);
        log.debug("initEditDBRDetail rowIndex:{}", rowIndex);
    }

    public void onDeletedDBRDetail() {
        dbrDetails.remove(rowIndex);
    }

    public void onSaveDBRInfo() {
        try {
            dbr.setDbrDetailViews(dbrDetails);
            dbr.setWorkCaseId(workCaseId);
            dbrControl.saveDBRInfo(dbr, ncbDetails);

            calculationControl.calForDBR(workCaseId);
            calculationControl.calWC(workCaseId);
            calculationControl.calculateTotalProposeAmount(workCaseId);
            calculationControl.calculateFinalDBR(workCaseId);

            //update Display
            onCreation();

            messageHeader = msg.get("app.header.save.success");
            message = msg.get("app.dbr.message.save");
            severity = MessageDialogSeverity.INFO.severity();
        } catch (Exception e) {
            messageHeader = "Exception.";
            message = Util.getMessageException(e);
            severity = MessageDialogSeverity.ALERT.severity();
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public BigDecimal getTotalMonthDebtBorrower(){
        BigDecimal totalMonthDebtBorrower = BigDecimal.ZERO;
        if(ncbDetails != null && !ncbDetails.isEmpty()){
            for(NCBDetailView ncbDetailView : ncbDetails){
                totalMonthDebtBorrower = totalMonthDebtBorrower.add(ncbDetailView.getDebtForCalculate());
            }
        }
        return totalMonthDebtBorrower;
    }

    public BigDecimal getTotalMonthDebtRelated(){
        BigDecimal totalMonthDebtRelated = BigDecimal.ZERO;
        if(dbrDetails != null && !dbrDetails.isEmpty()){
            for(DBRDetailView dbrDetailView : dbrDetails){
                totalMonthDebtRelated = totalMonthDebtRelated.add(dbrDetailView.getDebtForCalculate());
            }
        }
        return totalMonthDebtRelated;
    }

    public DBRView getDbr() {
        return dbr;
    }

    public void setDbr(DBRView dbr) {
        this.dbr = dbr;
    }

    public List<DBRDetailView> getDbrDetails() {
        return dbrDetails;
    }

    public void setDbrDetails(List<DBRDetailView> dbrDetails) {
        this.dbrDetails = dbrDetails;
    }

    public DBRDetailView getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(DBRDetailView selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<LoanAccountTypeView> getLoanAccountTypes() {
        return loanAccountTypes;
    }

    public void setLoanAccountTypes(List<LoanAccountTypeView> loanAccountTypes) {
        this.loanAccountTypes = loanAccountTypes;
    }

    public List<NCBDetailView> getNcbDetails() {
        return ncbDetails;
    }

    public void setNcbDetails(List<NCBDetailView> ncbDetails) {
        this.ncbDetails = ncbDetails;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
