package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CompletedCasesWKItems;
import com.clevel.selos.model.db.master.StatusIdBasedOnStepId;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.hibernate.Criteria;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompletedCasesWKItemsDAO extends GenericDAO<CompletedCasesWKItems,String>
{
    @Inject
    @SELOS
    Logger log;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    public CompletedCasesWKItemsDAO()
    {

    }

    public List<CompletedCasesWKItems> getCompletedCasesWKItems(List appnumberlist,int statusid,Date startfromdate,Date starttodate,Date terminatefromdate,Date terminatetodate)
    {
        List<CompletedCasesWKItems> completedCasesWKItemsList = new ArrayList<CompletedCasesWKItems>();

        log.info("controller entered in to getCompletedCasesWKItems method of CompletedCasesWKItemsDAO class");

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS aa");

        try
        {

            Criteria criteria = getSession().createCriteria(CompletedCasesWKItems.class);

            //criteria.setProjection(Projections.projectionList().add(Projections.property("receiveddate"),"receiveddate")) ;

            criteria.setProjection(Projections.projectionList()
                    .add(Projections.property("applicationNo"), "applicationNo")
                    .add(Projections.property("atUserTeamId"), "atUserTeamId")
                    .add(Projections.property("receiveddate"),"receiveddate")
                    .add(Projections.property("productgroupid"),"productgroupid")
                    .add(Projections.property("requesttypeid"),"requesttypeid")
                    .add(Projections.property("stepid"),"stepid")
                    .add(Projections.property("fromuserid"),"fromuserid")
                    .add(Projections.property("wobnumber"),"wobnumber")
                    .add(Projections.property("doalevelid"),"doalevelid")
                    .add(Projections.property("totaltimeatprocess"),"totaltimeatprocess")
                    .add(Projections.property("totaltimeatuser"),"totaltimeatuser")
                    .add(Projections.property("appointmentDate"),"appointmentDate")
                    .add(Projections.property("slaenddate"), "slaenddate")
                    .add(Projections.property("statusid"), "statusid")
                    .add(Projections.property("createBy"), "createBy")
                    .add(Projections.property("completedate"), "completedate")
                    .add(Projections.property("createdate"),"createdate"));

            if(appnumberlist != null)
            {

                criteria.add(Restrictions.in("applicationNo", appnumberlist)) ;  //application number
            }
            if(statusid != 0)
            {

                criteria.add(Restrictions.eq("statusid",statusid))  ; //status id
            }


            if(startfromdate != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startfromdate);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,1);
                startfromdate = cal.getTime();
                cal=null;
                criteria.add(Restrictions.ge("createdate",formatter.parse(formatter.format(startfromdate))))  ; //formatter.format(startfromdate)start from date
            }
            if(starttodate != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(starttodate);
                cal.set(Calendar.HOUR_OF_DAY,23);
                cal.set(Calendar.MINUTE,59);
                cal.set(Calendar.SECOND,59);
                starttodate = cal.getTime();
                cal=null;

                criteria.add(Restrictions.le("createdate",formatter.parse(formatter.format(starttodate))));//formatter.format(starttodate)))  ; //start to date
            }
            if(terminatefromdate != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(terminatefromdate);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,1);
                terminatefromdate = cal.getTime();
                cal=null;
                criteria.add(Restrictions.ge("completedate",formatter.parse(formatter.format(terminatefromdate))))  ; // terminate from date
            }
            if(terminatetodate != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(terminatetodate);
                cal.set(Calendar.HOUR_OF_DAY,23);
                cal.set(Calendar.MINUTE,59);
                cal.set(Calendar.SECOND,59);
                terminatetodate = cal.getTime();
                cal=null;

                criteria.add(Restrictions.le("completedate",formatter.parse(formatter.format(terminatetodate))))  ; // terminate to date
            }

            criteria.add(Restrictions.eq("bpmActive",0))  ;

            log.info("criteria is :: {}", criteria.toString());

            criteria.setResultTransformer(Transformers.aliasToBean(CompletedCasesWKItems.class));

            completedCasesWKItemsList = criteria.list();

            log.info("completedCasesWKItemsList ::::::::::::{}", completedCasesWKItemsList.size()) ;


            log.info("completedCasesWorkItemsList elements are ::::::::: {}", completedCasesWKItemsList.toString());

            Iterator iterator = completedCasesWKItemsList.iterator();

            Set appNumbersSet = new HashSet();

            while(iterator.hasNext() == true)
            {
                CompletedCasesWKItems completedCasesWKItems = (CompletedCasesWKItems)iterator.next();

                appNumbersSet.add(completedCasesWKItems.getApplicationNo());

                if(appnumberlist!=null && appnumberlist.size()>0)
                {
                    appnumberlist.remove(completedCasesWKItems.getApplicationNo());

                    log.info("Remaining AppNumbers list size : {}, app numbers : {}"+appnumberlist.size(),appnumberlist.toString());
                }

                log.info("applicatin number :::::: is : {} ",completedCasesWKItems.getApplicationNo());

                /*log.info("create date::::: is : {} ",completedCasesWKItems.getCreatedate());

                log.info("at userteam id :::::: is : {} ",completedCasesWKItems.getAtUserTeamId());

                log.info("total time at user :::::: is : {} ",completedCasesWKItems.getTotaltimeatuser());

                log.info("total time at process :::::: is : {} ",completedCasesWKItems.getTotaltimeatprocess());

                log.info("status id :::::: is : {} ",completedCasesWKItems.getStatusid());

                log.info("step  :::::: is : {} ",completedCasesWKItems.getStepid());

                log.info("doavlevel id :::::: is : {} ",completedCasesWKItems.getDoalevelid());

                log.info("from user id :::::: is : {} ",completedCasesWKItems.getFromuserid());

                log.info("product group id :::::: is : {} ",completedCasesWKItems.getProductgroupid());

                log.info("requesttype id :::::: is : {} ",completedCasesWKItems.getRequesttypeid());

                log.info(" wobnumber :::::: is : {} ",completedCasesWKItems.getWobnumber());
                log.info(" complete date :::::: is : {} ",completedCasesWKItems.getCompletedate());*/

            }

            log.info("completedCasesWKItemsList ::::::::::::{}", completedCasesWKItemsList.size()) ;

            //log.info("completedCasesWorkItemsList elements are ::::::::: {}", completedCasesWKItemsList.toString());

            //search in Prescreen
            List<CompletedCasesWKItems> preScreenCases =  workCasePrescreenDAO.getCompletedCases(appnumberlist,statusid,startfromdate,starttodate,terminatefromdate,terminatetodate,appNumbersSet);

            completedCasesWKItemsList.addAll(preScreenCases);

            log.info("completedCasesWKItemsList after pre screen ::::::::::::{}", completedCasesWKItemsList.size()) ;

        }

        catch(Exception exception)
        {
              log.error("error : ",exception);
        }
        finally
        {

        }

        return completedCasesWKItemsList;

    }

}
