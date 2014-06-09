package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CompletedCasesWKItems;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

public class WorkCasePrescreenDAO extends GenericDAO<WorkCasePrescreen, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public WorkCasePrescreenDAO() {
    }

    public long findIdByWobNumber(String wobNumber) {
        log.info("findIdByWobNum : {}", wobNumber);
        long workCasePreScreenId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCasePrescreen workCasePrescreen = (WorkCasePrescreen) criteria.uniqueResult();
        log.info("workcaseprescreen object : {}",workCasePrescreen);
        if (workCasePrescreen != null) {
            workCasePreScreenId = workCasePrescreen.getId();
            log.info("findIdByWobNum : {}", workCasePreScreenId);
        }

        return workCasePreScreenId;
    }

    public WorkCasePrescreen findByWobNumber(String wobNumber) {
        log.info("findByWobNumber : {}", wobNumber);
        WorkCasePrescreen workCasePrescreen;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));


        workCasePrescreen = (WorkCasePrescreen) criteria.uniqueResult();
        return workCasePrescreen;
    }

    public WorkCasePrescreen findByAppNumber(String appNumber){
        log.info("findByAppNumber : {}", appNumber);
        WorkCasePrescreen workCasePrescreen;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appNumber", appNumber));


        workCasePrescreen = (WorkCasePrescreen) criteria.uniqueResult();
        return workCasePrescreen;
    }

    //Function for AppHeader
    public WorkCasePrescreen getWorkCasePreScreenById(long id){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", id));
        criteria.setFetchMode("borrowerType", FetchMode.SELECT);
        criteria.setFetchMode("customerList", FetchMode.SELECT);
        criteria.setFetchMode("stepOwner", FetchMode.SELECT);
        criteria.setFetchMode("atUserTeam", FetchMode.SELECT);
        criteria.setFetchMode("productGroup", FetchMode.SELECT);
        criteria.setFetchMode("requestType", FetchMode.SELECT);
        criteria.setFetchMode("fromUser", FetchMode.SELECT);
        criteria.setFetchMode("atUser", FetchMode.SELECT);
        criteria.setFetchMode("authorizationDOA", FetchMode.SELECT);
        criteria.setFetchMode("step", FetchMode.SELECT);
        criteria.setFetchMode("modifyBy", FetchMode.SELECT);

        WorkCasePrescreen workCasePrescreen = (WorkCasePrescreen)criteria.uniqueResult();

        return workCasePrescreen;
    }

    //find number of appeals
    public Integer getAppealResubmitCount(String refAppNumber, Integer requestType)
    {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("refAppNumber",refAppNumber)).add(Restrictions.eq("requestType",requestType));

        Integer caseCount = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

        return caseCount;

    }

    public List<CompletedCasesWKItems> getCompletedCases(List appnumberlist,int statusid,Date startfromdate,Date starttodate,Date terminatefromdate,Date terminatetodate, Set appNumbersSet)
    {

        List<CompletedCasesWKItems> completedCasesWKItemsList = new ArrayList<CompletedCasesWKItems>();

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS aa");

        try
        {

            Criteria criteriaPreScreen = createCriteria();

            if(appnumberlist != null && appnumberlist.size()>0)
            {

                criteriaPreScreen.add(Restrictions.in("appNumber", appnumberlist)) ;  //application number
            }

            if(statusid != 0)
            {

                criteriaPreScreen.add(Restrictions.eq("status.id",statusid))  ; //status id
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
                criteriaPreScreen.add(Restrictions.ge("createDate",formatter.parse(formatter.format(startfromdate))))  ; //formatter.format(startfromdate)start from date
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

                criteriaPreScreen.add(Restrictions.le("createDate",formatter.parse(formatter.format(starttodate))));//formatter.format(starttodate)))  ; //start to date
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
                criteriaPreScreen.add(Restrictions.ge("completeDate",formatter.parse(formatter.format(terminatefromdate))))  ; // terminate from date
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

                criteriaPreScreen.add(Restrictions.le("completeDate",formatter.parse(formatter.format(terminatetodate))))  ; // terminate to date
            }

            criteriaPreScreen.add(Restrictions.eq("bpmActive",0))  ;

            log.info("criteria for pre screen is :: {}", criteriaPreScreen.toString());

            //criteriaPreScreen.setResultTransformer(Transformers.aliasToBean(WorkCasePrescreen.class));

            List workCasePreScreens = criteriaPreScreen.list();

            Iterator workCasePrescreenIterator = workCasePreScreens.iterator();

            while (workCasePrescreenIterator.hasNext())
            {

                WorkCasePrescreen workCasePrescreen = (WorkCasePrescreen) workCasePrescreenIterator.next();

                if(!appNumbersSet.contains(workCasePrescreen.getAppNumber()))
                {

                    CompletedCasesWKItems completedCasesWKItems = new CompletedCasesWKItems();

                    completedCasesWKItems.setId(Integer.parseInt(Long.toString(workCasePrescreen.getId())));

                    completedCasesWKItems.setApplicationNo(workCasePrescreen.getAppNumber());

                    completedCasesWKItems.setAppointmentDate(workCasePrescreen.getAppointmentDate());

                    completedCasesWKItems.setCompletedate(workCasePrescreen.getCompleteDate());

                    completedCasesWKItems.setAtUserTeamId(workCasePrescreen.getAtUserTeam()!=null ? workCasePrescreen.getAtUserTeam().getId():null);

                    completedCasesWKItems.setWobnumber(workCasePrescreen.getWobNumber());

                    completedCasesWKItems.setDoalevelid(workCasePrescreen.getAuthorizationDOA() != null ? workCasePrescreen.getAuthorizationDOA().getDoapriorityorder():null);

                    completedCasesWKItems.setSlaenddate(workCasePrescreen.getCompleteDate());

                    completedCasesWKItems.setFromuserid(workCasePrescreen.getCreateBy().getId());

                    completedCasesWKItems.setCreateBy(workCasePrescreen.getCreateBy().getId());

                    completedCasesWKItems.setCreatedate(workCasePrescreen.getCreateDate());

                    completedCasesWKItems.setProductgroupid(workCasePrescreen.getProductGroup()!=null ? workCasePrescreen.getProductGroup().getId():null);

                    completedCasesWKItems.setRequesttypeid(workCasePrescreen.getRequestType()!=null ? workCasePrescreen.getRequestType().getId():null);

                    completedCasesWKItems.setStatusid(workCasePrescreen.getStatus()!=null ? Integer.parseInt(Long.toString(workCasePrescreen.getStatus().getId())) : null);

                    completedCasesWKItems.setTotaltimeatprocess(workCasePrescreen.getTotalTimeAtProcess());

                    completedCasesWKItems.setTotaltimeatuser(workCasePrescreen.getTotalTimeAtUser());

                    completedCasesWKItemsList.add(completedCasesWKItems);

                }

            }

            log.info("completedCasesWKItemsList in PreScreen ::::::::::::{}", completedCasesWKItemsList.size()) ;

        }
        catch (Exception e)
        {
            log.error("Error : {}",e);
        }

        return completedCasesWKItemsList;
    }
}
