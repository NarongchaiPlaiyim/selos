package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CompletedCasesWKItems;
import com.clevel.selos.model.db.master.StatusIdBasedOnStepId;
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
                    .add(Projections.property("statusid"), "statusid")) ;

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

                criteria.add(Restrictions.ge("receiveddate",startfromdate))  ; //formatter.format(startfromdate)start from date
            }
            if(starttodate != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(starttodate);
                cal.set(Calendar.HOUR_OF_DAY,23);
                cal.set(Calendar.MINUTE,59);
                cal.set(Calendar.SECOND,59);
                starttodate = cal.getTime();

                criteria.add(Restrictions.le("receiveddate",starttodate));//formatter.format(starttodate)))  ; //start to date
            }
            if(terminatefromdate != null)
            {

                criteria.add(Restrictions.ge("createdate",terminatefromdate))  ; // terminate from date
            }
            if(terminatetodate != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(terminatetodate);
                cal.set(Calendar.HOUR_OF_DAY,23);
                cal.set(Calendar.MINUTE,59);
                cal.set(Calendar.SECOND,59);
                terminatetodate = cal.getTime();

                criteria.add(Restrictions.le("createdate",terminatetodate))  ; // terminate to date
            }

            log.info("criteria is :: {}", criteria.toString());

            criteria.setResultTransformer(Transformers.aliasToBean(CompletedCasesWKItems.class));

            completedCasesWKItemsList = criteria.list();

            log.info("completedCasesWKItemsList ::::::::::::{}", completedCasesWKItemsList.size()) ;


            log.info("completedCasesWorkItemsList elements are ::::::::: {}", completedCasesWKItemsList.toString());

            Iterator iterator = completedCasesWKItemsList.iterator();

            while(iterator.hasNext() == true)
            {
                CompletedCasesWKItems completedCasesWKItems = new CompletedCasesWKItems();

                completedCasesWKItems = (CompletedCasesWKItems)iterator.next();

                log.info("applicatin number :::::: is : {} ",completedCasesWKItems.getCreatedate());

                log.info("applicatin number :::::: is : {} ",completedCasesWKItems.getApplicationNo());

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

            }

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
