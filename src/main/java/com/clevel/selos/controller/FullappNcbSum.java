package com.clevel.selos.controller;


import com.clevel.selos.model.view.NcbSumView;
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
@ManagedBean(name = "fullappNcbSum")
public class FullappNcbSum implements Serializable {


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

    private List<NcbSumView> ncbSumList;
    private NcbSumView ncbSum;

    public FullappNcbSum() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        if(ncbSumList == null){
            ncbSumList = new ArrayList<NcbSumView>();
        }

    }

    // onclick edit button
    public void onEdit(){

    }

    public NcbSumView getNcbSum() {
        return ncbSum;
    }

    public void setNcbSum(NcbSumView ncbSum) {
        this.ncbSum = ncbSum;
    }

    public List<NcbSumView> getNcbSumList() {
        return ncbSumList;
    }

    public void setNcbSumList(List<NcbSumView> ncbSumList) {
        this.ncbSumList = ncbSumList;
    }
}
