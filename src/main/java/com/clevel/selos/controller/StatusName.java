package com.clevel.selos.controller;

import com.clevel.selos.dao.master.StatusIdBasedOnStepIdDAO;
import com.clevel.selos.dao.master.StatusNameDescriptionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StatusIdBasedOnStepId;
import org.slf4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@ViewScoped
@ManagedBean(name = "statusname")
public class StatusName implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    List<String> statusnamelist;

    String statustype = null;

    String statusnamechange = null;

    @Inject
    StatusNameDescriptionDAO statusNameDescriptionDAO;

    @Inject
    StatusIdBasedOnStepIdDAO statusIdBasedOnStepIdDAO;

    @Inject
    PESearch peSearch;

    public String getStatustype() {
        return statustype;
    }

    public void setStatustype(String statustype) {
        this.statustype = statustype;
    }

    public List<String> getStatusnamelist() {
        return statusnamelist;
    }

    public void setStatusnamelist(List<String> statusnamelist) {
        this.statusnamelist = statusnamelist;
    }

    @PostConstruct
    public void init()
    {

    }

    public List<String> valueChangeMethod(ValueChangeEvent e)
    {
        log.info("step id value is  ::::::::::: {}",e.getNewValue().toString() );

        statusnamelist = new ArrayList<String>();

        statustype =   e.getNewValue().toString();

         int stepid = 0;

            try
            {

                if(statustype.equalsIgnoreCase("-Please Select-"))
                {

                }

                else
                {
                    stepid = Integer.parseInt(statustype);
                }

                log.info("step id is : {}",stepid);

                List<StatusIdBasedOnStepId> statusIdBasedOnStepIdList = new ArrayList<StatusIdBasedOnStepId>();

                if(stepid > 0)
                {
                    statusIdBasedOnStepIdList =   statusIdBasedOnStepIdDAO.getStatusidbasedonstepid(stepid);

                    log.info("statusStepIdList is :::::: {}",statusIdBasedOnStepIdList.size());

                    Iterator iterator = statusIdBasedOnStepIdList.iterator();

                    while(iterator.hasNext() == true)
                    {
                        StatusIdBasedOnStepId statusIdBasedOnStepId = new StatusIdBasedOnStepId();

                        statusIdBasedOnStepId = (StatusIdBasedOnStepId)iterator.next();

                        int statusid = statusIdBasedOnStepId.getStatusid();

                        log.info("status id is :::::::::::::::::::::::::: {}",statusid);

                        String statusdescriptionname  =  statusNameDescriptionDAO.getStatusNameDescriptions(statusid);

                        if(statusdescriptionname != "" && statusdescriptionname.length() > 0 && !statusnamelist.contains(statusdescriptionname))
                        {
                          statusnamelist.add(statusdescriptionname);
                        }
                    }
                }

            }
            catch(Exception exception)
            {
              log.info("exception occured :{}",exception);
            }
            finally
            {

            }
            return statusnamelist;


    }

    public List<String> valueChangeMethod1(ValueChangeEvent e)
    {
        log.info("controller comes to valueChangeMethod1");

        statusnamelist = new ArrayList<String>();

        statusnamechange = e.getNewValue().toString();

        log.info("statusnamechange is :::::: {}",statusnamechange);

        if(statusnamechange.equalsIgnoreCase("CompletedCases"))
        {
            log.info("statusnamechange is completedCases method ");

            statusnamelist.clear();

            try
            {
                statusnamelist.add("CA Cancelled");
                statusnamelist.add("CA Rejected by UW1");
                statusnamelist.add("CA Approved by UW1");
                statusnamelist.add("CA Rejected");
                statusnamelist.add("CA Approved");
                statusnamelist.add("CA Approved by UW2");
                statusnamelist.add("CA Rejected by UW2");
            }
            catch(Exception exception)
            {

            }
            finally {

            }

            return statusnamelist;
        }
        else if(statusnamechange.equalsIgnoreCase("InprocessCases"))
        {
           statusnamelist.clear();
        }

        return statusnamelist;
    }


}
