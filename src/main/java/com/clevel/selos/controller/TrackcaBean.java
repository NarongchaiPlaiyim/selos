package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.PERoster;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@ManagedBean(name = "trackBean")
public class TrackcaBean implements Serializable
{

    @Inject
    @SELOS
    Logger log;

    @Inject
    PEDBExecute pedbExecute;

    @Inject
    ActionDAO actionDAO;

    private List<PERoster> rosterViewList;

    private PERoster rosterViewSelectItem;

    private String statusType;

    public List<PERoster> getRosterViewList() {
        return rosterViewList;
    }

    public void setRosterViewList(List<PERoster> rosterViewList) {
        this.rosterViewList = rosterViewList;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public PERoster getRosterViewSelectItem() {
        return rosterViewSelectItem;
    }

    public void setRosterViewSelectItem(PERoster rosterViewSelectItem) {
        this.rosterViewSelectItem = rosterViewSelectItem;
    }

    @PostConstruct
    public void onCreation()
    {
        statusType = "CreatedByMe";

        log.info("in controller");

    }

    public List<PERoster> peRosterQueryForTrackCa()
    {
        try
        {

            String description = "";

            log.info("before call the method::::");

            description = actionDAO.getDescripationFromAction();

            rosterViewList =  pedbExecute.getRosterQuery(statusType,description);

            log.info("List Size : {}",rosterViewList.size());

        }

        catch(Exception e)
        {

            log.error("Error im track Ca *** ",e);

        }

        log.info("rosterViewList is : {}",rosterViewList.size());

        return  rosterViewList;

    }

}