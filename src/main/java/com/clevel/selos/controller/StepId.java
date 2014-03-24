package com.clevel.selos.controller;

import com.clevel.selos.dao.master.StepNameIdDAO;
import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.clevel.selos.model.db.master.StepNameId;

@ViewScoped
@ManagedBean(name = "stepid")
public class StepId implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    @Inject
    StepNameIdDAO stepNameIdDAO;

    public List<StepNameId> getStepnameidlist() {
        return stepnameidlist;
    }

    public void setStepnameidlist(List<StepNameId> stepnameidlist) {
        this.stepnameidlist = stepnameidlist;
    }

    List<StepNameId> stepnameidlist;

    @PostConstruct
    public void init()
    {
        stepnameidlist = new ArrayList<StepNameId>();

        stepnameidlist =  stepNameIdDAO.getStepNameids();

        log.info("stepnameidlist is :::::::::::::::::: {}",stepnameidlist);
    }


}
