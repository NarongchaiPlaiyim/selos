package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
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

    private List<String> mandatoryDocumentsList;
    private List<String> optionalDocumentsList;
    private List<String> otherDocumentsList;

    public CheckMandateDoc() {
        init();
    }

    private void init(){
        mandatoryDocumentsList = new ArrayList<String>();
        mandatoryDocumentsList.add("test");
        mandatoryDocumentsList.add("test2");
        mandatoryDocumentsList.add("test3");

        optionalDocumentsList = new ArrayList<String>();
        optionalDocumentsList.add("test");
        optionalDocumentsList.add("test2");
        optionalDocumentsList.add("test3");

        otherDocumentsList = new ArrayList<String>();
        otherDocumentsList.add("test");
        otherDocumentsList.add("test2");
        otherDocumentsList.add("test3");
    }

    public void onSaveCheckMandateDoc(){

    }

    public void onCancelCheckMandateDoc(){

    }

    public List<String> getMandatoryDocumentsList() {
        return mandatoryDocumentsList;
    }

    public void setMandatoryDocumentsList(List<String> mandatoryDocumentsList) {
        this.mandatoryDocumentsList = mandatoryDocumentsList;
    }

    public List<String> getOptionalDocumentslList() {
        return optionalDocumentsList;
    }

    public void setOptionalDocumentslList(List<String> optionalDocumentsList) {
        this.optionalDocumentsList = optionalDocumentsList;
    }

    public List<String> getOtherDocumentsList() {
        return otherDocumentsList;
    }

    public void setOtherDocumentsList(List<String> otherDocumentsList) {
        this.otherDocumentsList = otherDocumentsList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("mandatoryDocumentsList", mandatoryDocumentsList)
                .append("optionalDocumentslList", optionalDocumentsList)
                .append("otherDocumentsList", otherDocumentsList)
                .toString();
    }
}
