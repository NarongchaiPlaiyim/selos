package com.clevel.selos.controller;


import com.clevel.selos.model.view.NCBSummaryView;
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
@ManagedBean(name = "ncbSummary")
public class NCBSummary implements Serializable {


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

    private List<NCBSummaryView> ncbSumList;
    private NCBSummaryView ncbSum;

    public NCBSummary() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        if(ncbSumList == null){
            ncbSumList = new ArrayList<NCBSummaryView>();
        }

    }

    // onclick edit button
    public void onEdit(){

    }

    public NCBSummaryView getNcbSum() {
        return ncbSum;
    }

    public void setNcbSum(NCBSummaryView ncbSum) {
        this.ncbSum = ncbSum;
    }

    public List<NCBSummaryView> getNcbSumList() {
        return ncbSumList;
    }

    public void setNcbSumList(List<NCBSummaryView> ncbSumList) {
        this.ncbSumList = ncbSumList;
    }
}
