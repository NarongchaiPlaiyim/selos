package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.businesscontrol.LoanAccountTypeControl;
import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "dbrInfo")
public class DBRInfo implements Serializable {
    @Inject
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

    // message //
    private String messageHeader;
    private String message;

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
    private long stepId;
    private String userId;

    private boolean isComplete;


    public DBRInfo() {

    }

    public void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);
        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        try{
            selectedItem = new DBRDetailView();
            dbr = new DBRView();
            dbr = dbrControl.getDBRByWorkCase(workCaseId);
            dbrDetails = new ArrayList<DBRDetailView>();
            if (dbr.getDbrDetailViews() != null && !dbr.getDbrDetailViews().isEmpty()) {
                dbrDetails = dbr.getDbrDetailViews();
            }
            loanAccountTypes = new ArrayList<LoanAccountTypeView>();
            loanAccountTypes = loanAccountTypeControl.getListLoanTypeByCus(1);
            ncbDetails = new ArrayList<NCBDetailView>();
            ncbDetails = ncbInfoControl.getNCBForCalDBR(workCaseId);

        }catch (Exception e){

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
                selectedItem.setLoanAccountTypeView(loanAccountType);
                break;
            }
        }

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

    public void onEditDBRDetail() {
        log.debug("initEditDBRDetail :{}", selectedItem);
        log.debug("initEditDBRDetail rowIndex:{}", rowIndex);
        dbrDetails.set(rowIndex, selectedItem);
    }

    public void onDeletedDBRDetail() {
        dbrDetails.remove(rowIndex);

    }

    public void onSaveDBRInfo() {
        try {
            dbr.setDbrDetailViews(dbrDetails);
            dbrControl.saveDBRInfo(dbr, workCaseId, userId);
            messageHeader = "Save Basic Info Success.";
            message = "Save Basic Info data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

            //update Display
            dbr = new DBRView();
            dbr = dbrControl.getDBRByWorkCase(workCaseId);
            dbrDetails = new ArrayList<DBRDetailView>();
            if (dbr.getDbrDetailViews() != null && !dbr.getDbrDetailViews().isEmpty()) {
                dbrDetails = dbr.getDbrDetailViews();
            }
        } catch (Exception e) {
            if (e.getCause() != null) {
                message = "Save Basic Info data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Basic Info data failed. Cause : " + e.getMessage();
            }
        }
    }

    public BigDecimal getOutStandingNCBTotal(){
        BigDecimal outStandingNCB = BigDecimal.ZERO;
        if(ncbDetails != null && !ncbDetails.isEmpty()){
            for(NCBDetailView ncbDetailView : ncbDetails){
                outStandingNCB = outStandingNCB.add(ncbDetailView.getOutstanding());
            }
        }
        return outStandingNCB;
    }

    public BigDecimal getOutStandingDBRTotal(){
        BigDecimal outStandingDBR = BigDecimal.ZERO;
        if(dbrDetails != null && !dbrDetails.isEmpty()){
            for(DBRDetailView dbrDetailView : dbrDetails){
                outStandingDBR = outStandingDBR.add(dbrDetailView.getDebtForCalculate());
            }
        }
        return outStandingDBR;
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


}
