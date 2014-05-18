package com.clevel.selos.controller;

import com.clevel.selos.dao.master.StatusIdBasedOnStepIdDAO;
import com.clevel.selos.dao.master.StatusNameDescriptionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StatusIdBasedOnStepId;
import com.clevel.selos.model.db.master.StatusNameDescription;
import com.clevel.selos.system.Config;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;


@ViewScoped
@ManagedBean(name = "statusname")
public class StatusName implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    List<StatusNameDescription> statusnamelist;

    String statustype = null;

    String statusnamechange = null;

    @Inject
    StatusNameDescriptionDAO statusNameDescriptionDAO;

    @Inject
    StatusIdBasedOnStepIdDAO statusIdBasedOnStepIdDAO;

    @Inject
    @Config(name = "interface.pe.sql.completedstatus")
    String completedStatus;

    @Inject
    PESearch peSearch;

    public String getStatustype() {
        return statustype;
    }

    public void setStatustype(String statustype) {
        this.statustype = statustype;
    }

    public List<StatusNameDescription> getStatusnamelist() {
        return statusnamelist;
    }

    public void setStatusnamelist(List<StatusNameDescription> statusnamelist) {
        this.statusnamelist = statusnamelist;
    }

    @PostConstruct
    public void init()
    {

    }

    public List<StatusNameDescription> valueChangeMethod(ValueChangeEvent e)
    {
        log.info("step id value is  ::::::::::: {}",e.getNewValue().toString() );

        statusnamelist = new ArrayList<StatusNameDescription>();

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

                    statusIdBasedOnStepIdList = new ArrayList(new HashSet(statusIdBasedOnStepIdList));

                    log.info("statusStepIdList is after :::::: {}",statusIdBasedOnStepIdList.size());

                    Iterator iterator = statusIdBasedOnStepIdList.iterator();

                    Set<Integer> setStatus = new HashSet<Integer>();

                    while(iterator.hasNext() == true)
                    {
                        StatusIdBasedOnStepId statusIdBasedOnStepId = new StatusIdBasedOnStepId();

                        statusIdBasedOnStepId = (StatusIdBasedOnStepId)iterator.next();

                        int statusid = statusIdBasedOnStepId.getStatusid();

                        log.info("status id is :::::::::::::::::::::::::: {}",statusid);

                        log.info(" size ** "+setStatus.size());

                        if(!setStatus.contains(statusid))
                        {
                            log.info("in if "+statusid);
                            List<StatusNameDescription> statusdescriptionname  =  statusNameDescriptionDAO.getStatusNameDescriptions(statusid);

                            if(statusdescriptionname != null && statusdescriptionname.size() > 0)
                            {
                                statusnamelist.add(statusdescriptionname.get(0));
                            }
                        }

                        setStatus.add(statusid);

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

    public List<StatusNameDescription> valueChangeMethod1(ValueChangeEvent e)
    {
        log.info("controller comes to valueChangeMethod1");

        statusnamelist = new ArrayList<StatusNameDescription>();

        statusnamechange = e.getNewValue().toString();

        log.info("statusnamechange is :::::: {}",statusnamechange);

        if(statusnamechange.equalsIgnoreCase("CompletedCases"))
        {
            log.info("statusnamechange is completedCases method ");

            statusnamelist.clear();

            try
            {

                String[] completedStatusArr = completedStatus.split(",");
                int len = completedStatusArr.length;
                log.info("status from property file : "+completedStatus);
                //log.info("array len :"+len);
                int i =0;
                while(i<len)
                {
                    List<StatusNameDescription> statusdescriptionname  =  statusNameDescriptionDAO.getStatusNameDescriptions(Integer.parseInt(completedStatusArr[i]));
                    //log.info("List in while : "+statusdescriptionname);
                    //log.info("descname :"+statusdescriptionname.get(0).getName());
                    statusnamelist.add(statusdescriptionname.get(0));
                    i++;
                }
                /*statusnamelist.add("CA Cancelled");
                statusnamelist.add("CA Rejected by UW1");
                statusnamelist.add("CA Approved by UW1");
                statusnamelist.add("CA Rejected");
                statusnamelist.add("CA Approved");
                statusnamelist.add("CA Approved by UW2");
                statusnamelist.add("CA Rejected by UW2");*/
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
