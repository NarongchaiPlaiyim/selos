package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.businesscontrol.LoanTypeControl;
import com.clevel.selos.dao.working.DBRDAO;
import com.clevel.selos.dao.working.DBRDetailDAO;
import com.clevel.selos.model.view.DBRDetailView;
import com.clevel.selos.model.view.DBRView;
import com.clevel.selos.model.view.LoanTypeView;
import com.clevel.selos.model.view.NcbView;
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
    LoanTypeControl loanTypeControl;

    // message //
    private String messageHeader ;
    private String message;

    // *** Content ***///
    private DBRView dbr;
    private List<DBRDetailView> dbrDetails;
    private List<LoanTypeView> loanTypes;
    private List<NcbView> ncbViews;

    //**DBR Detail
    private DBRDetailView selectedItem;
    private int rowIndex;
    private boolean isUpdate;


    //session
    private long workCaseId;
    private long stepId;
    private String userId;

    private boolean isComplete;


    public DBRInfo(){

    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);
        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        }else{
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        selectedItem = new DBRDetailView();
        dbr = new DBRView();
        dbr = dbrControl.getDBRByWorkCase(workCaseId);
        dbrDetails = new ArrayList<DBRDetailView>();
        if(dbr.getDbrDetailViews() != null && !dbr.getDbrDetailViews().isEmpty()){
            dbrDetails = dbr.getDbrDetailViews();
        }
        loanTypes = new ArrayList<LoanTypeView>();
        loanTypes = loanTypeControl.getListLoanTypeByCus(2);
        ncbViews = new ArrayList<NcbView>(); // HardCode
    }

    public void initAddDBRDetail(){
        isComplete = false;
        selectedItem = new DBRDetailView();


    }

    public void onAddDBRDetail(){
        log.debug("onAdd DBR Detail :{}", selectedItem);
        RequestContext context = RequestContext.getCurrentInstance();
        if(selectedItem == null || loanTypes.isEmpty()){
            return;
        }
        for(LoanTypeView loanTypeView : loanTypes){
             if(loanTypeView.getId() == selectedItem.getLoanTypeView().getId()){
                 LoanTypeView loanType = new LoanTypeView();
                 loanType.setId(selectedItem.getLoanTypeView().getId());
                 loanType.setName(loanTypeView.getName());
                 selectedItem.setLoanTypeView(loanType);
                 break;
             }
         }

        if(isUpdate){
            dbrDetails.set(rowIndex, selectedItem);
        }else{
            dbrDetails.add(selectedItem);
        }

        isComplete = true;
        context.addCallbackParam("functionComplete", isComplete);

    }


    public void initEditDBRDetail(){
        isComplete = false;
        log.debug("initEditDBRDetail :{}", selectedItem);
        log.debug("initEditDBRDetail rowIndex:{}", rowIndex);
    }

    public void onEditDBRDetail(){
        log.debug("initEditDBRDetail :{}", selectedItem);
        log.debug("initEditDBRDetail rowIndex:{}", rowIndex);
        dbrDetails.set(rowIndex, selectedItem);
    }

    public void onDeletedDBRDetail(){
        dbrDetails.remove(rowIndex);

    }

    public void onSaveDBRInfo(){
        try{
            dbr.setDbrDetailViews(dbrDetails);
            dbrControl.saveDBRInfo(dbr, workCaseId, userId);
            messageHeader = "Save Basic Info Success.";
            message = "Save Basic Info data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

            //update Display
            dbr = new DBRView();
            dbr = dbrControl.getDBRByWorkCase(workCaseId);
            dbrDetails = new ArrayList<DBRDetailView>();
            if(dbr.getDbrDetailViews() != null && !dbr.getDbrDetailViews().isEmpty()){
                dbrDetails = dbr.getDbrDetailViews();
            }
        }catch (Exception e){
            if(e.getCause() != null){
                message = "Save Basic Info data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Basic Info data failed. Cause : " + e.getMessage();
            }
        }

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

    public List<LoanTypeView> getLoanTypes() {
        return loanTypes;
    }

    public void setLoanTypes(List<LoanTypeView> loanTypes) {
        this.loanTypes = loanTypes;
    }

    public List<NcbView> getNcbViews() {
        return ncbViews;
    }

    public void setNcbViews(List<NcbView> ncbViews) {
        this.ncbViews = ncbViews;
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
