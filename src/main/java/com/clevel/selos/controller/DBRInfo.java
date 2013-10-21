package com.clevel.selos.controller;

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
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "dbrInfo")
public class DBRInfo extends BaseController implements Serializable {
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
    DBRDAO dbrdao;

    @Inject
    DBRDetailDAO dbrDetailDAO;

    @Inject
    LoanTypeControl loanTypeControl;

    // *** Content ***///
    private DBRView dbr;
    private List<DBRDetailView> dbrDetails;
    private List<LoanTypeView> loanTypes;
    private List<NcbView> ncbViews;

    //**DBR Detail
    private DBRDetailView selectedItem;
    private int rowIndex;

    public DBRInfo(){

    }

    @PostConstruct
    public void onCreation() {
        dbr = new DBRView();
        dbrDetails = new ArrayList<DBRDetailView>();


        loanTypes = loanTypeControl.getListLoanTypeByCus(2);

        ncbViews = new ArrayList<NcbView>(); // HardCode
    }

    public void initAddDBRDetail(){
        selectedItem = new DBRDetailView();
        selectedItem.setLoanTypeView(new LoanTypeView());
    }

    public void onAddDBRDetail(){
        log.debug("onAdd DBR Detail :{}", selectedItem);
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
        dbrDetails.add(selectedItem);
    }

    public void onDeletedDBRDetail(){
        dbrDetails.remove(rowIndex);
    }

    public void initEditDBRDetail(){
        log.debug("initEditDBRDetail :{}", selectedItem);
        log.debug("initEditDBRDetail rowIndex:{}", rowIndex);
    }



    public void onEditDBRDetail(){

    }

    public void onSaveDBR(){

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


}
