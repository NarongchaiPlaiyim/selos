package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.CheckMandatoryDocView;
import com.clevel.selos.model.view.CheckOptionalDocView;
import com.clevel.selos.model.view.CheckOtherDocView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @SELOS
    Logger log;

    private List<CheckMandatoryDocView> mandatoryDocumentsList;
    private List<CheckOptionalDocView> optionalDocumentsList;
    private List<CheckOtherDocView> otherDocumentsList;

    private int rowIndex;
    private String messageHeader;
    private String message;

    public CheckMandateDoc() {
        init();
    }

    private void init(){
        mandatoryDocumentsList = new ArrayList<CheckMandatoryDocView>();
        CheckMandatoryDocView checkMandatoryDocView = new CheckMandatoryDocView();
        checkMandatoryDocView.setDocumentType("6.8.11 Document Ttype");
        checkMandatoryDocView.setBRMSDocument("Citizen ID");
        checkMandatoryDocView.setOwners("Mr. Narongchai");
        checkMandatoryDocView.setFileName("File_2013107_1501");
        checkMandatoryDocView.setDisabledCompleteFlag(true);
        checkMandatoryDocView.setComplete(1);
        checkMandatoryDocView.setIndistinct(true);
        checkMandatoryDocView.setRemark("test");
        mandatoryDocumentsList.add(checkMandatoryDocView);

        checkMandatoryDocView = new CheckMandatoryDocView();
        checkMandatoryDocView.setDocumentType("6.9.3 Statement");
        checkMandatoryDocView.setBRMSDocument("Citizen ID");
        checkMandatoryDocView.setOwners("Mr. Narongchai2");
        checkMandatoryDocView.setFileName("File_2013107_1502");
        checkMandatoryDocView.setComplete(3);
        checkMandatoryDocView.setIndistinct(true);
        checkMandatoryDocView.setExpire(true);
        checkMandatoryDocView.setRemark("test");
        mandatoryDocumentsList.add(checkMandatoryDocView);

        optionalDocumentsList = new ArrayList<CheckOptionalDocView>();
        CheckOptionalDocView checkOptionalDocView = new CheckOptionalDocView();
        checkOptionalDocView.setDocumentType("6.11.2 Statement");
        checkOptionalDocView.setOwners("Application");
        checkOptionalDocView.setFileName("File_2013107_1502");
        checkOptionalDocView.setComplete(2);
        checkOptionalDocView.setIndistinct(true);
        checkOptionalDocView.setExpire(true);
        checkOptionalDocView.setRemark("test");
        optionalDocumentsList.add(checkOptionalDocView);

        checkOptionalDocView = new CheckOptionalDocView();
        checkOptionalDocView.setDocumentType("X.X.XXXX StatementXXXXX");
        checkOptionalDocView.setBRMSDocument("XXXXXXXXXXX");
        checkOptionalDocView.setOwners("Application");
        checkOptionalDocView.setFileName("File_2013107_1502");
        checkOptionalDocView.setComplete(2);
        checkOptionalDocView.setIndistinct(true);
        checkOptionalDocView.setExpire(true);
        checkOptionalDocView.setRemark("test");
        optionalDocumentsList.add(checkOptionalDocView);


        otherDocumentsList = new ArrayList<CheckOtherDocView>();
        CheckOtherDocView checkOtherDocView = new CheckOtherDocView();
        checkOtherDocView.setDocumentType("6.9.3 Statement");
        checkOtherDocView.setOwners("Application");
        checkOtherDocView.setFileName("File_2013107_1502");
        checkOtherDocView.setComplete(2);
        checkOtherDocView.setIndistinct(true);
        checkOtherDocView.setExpire(true);
        checkOtherDocView.setRemark("test");
        otherDocumentsList.add(checkOtherDocView);
    }

    public void onCreation() {
        log.info("-- onCreation.");

        //if found the documents matched in ECM completed = Y and will not allow user to change it;
        //if not found the documents matched in ECM completed = N and it will be changed to Yes when user upload the document into ECM;

        //query ECM for list of documents uploaded by user via ECM linkage by Application number.
    }

    public void onSaveCheckMandateDoc(){

    }

    public void onCancelCheckMandateDoc(){

    }

    public List<CheckMandatoryDocView> getMandatoryDocumentsList() {
        return mandatoryDocumentsList;
    }

    public void setMandatoryDocumentsList(List<CheckMandatoryDocView> mandatoryDocumentsList) {
        this.mandatoryDocumentsList = mandatoryDocumentsList;
    }

    public List<CheckOptionalDocView> getOptionalDocumentsList() {
        return optionalDocumentsList;
    }

    public void setOptionalDocumentsList(List<CheckOptionalDocView> optionalDocumentsList) {
        this.optionalDocumentsList = optionalDocumentsList;
    }

    public List<CheckOtherDocView> getOtherDocumentsList() {
        return otherDocumentsList;
    }

    public void setOtherDocumentsList(List<CheckOtherDocView> otherDocumentsList) {
        this.otherDocumentsList = otherDocumentsList;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
