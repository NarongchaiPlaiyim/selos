package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.UserToAuthorizationDOADAO;
import com.clevel.selos.dao.working.*;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.QueueNameId;
import com.clevel.selos.model.view.*;
import com.clevel.selos.integration.bpm.tool.SQLDBConnection;
import com.clevel.selos.model.db.master.StepLandingPage;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;


import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.clevel.selos.model.db.master.DoaPriorityUserNames;

@Stateless
public class PEDBExecute extends BusinessControl
{
    @Inject
    @SELOS
    Logger log;

    @Inject
    @Config(name = "interface.pe.sql.conn")
    String connPE;

    @Inject
    @Config(name = "interface.pe.sql.username")
    String peUser;

    @Inject
    @Config(name = "interface.pe.sql.password")
    String pePassword;

    @Inject
    @Config(name = "interface.pe.sql.prefix")
    String prefix;

    @Inject
    @Config(name = "interface.pe.sql.orderby")
    String orderbystring;

    @Inject
    @Config(name = "interface.pe.sql.inbox.query")
    String peInboxQuery;

    @Inject
    @Config(name = "interface.pe.sql.selectedColumnsForPeRosterQuery")
    String queryForRosterColumns;


    @Inject
    private UserDAO userDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    PrescreenFacilityDAO prescreenFacilityDAO;
    @Inject
    StepLandingPageDAO stepLandingPageDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;

    @Inject
    CustomerTransform customerTransform;

    @Inject
    SQLDBConnection dbContext;

    @Inject
    @ExceptionMessage
    Message msg;

    Connection conn = null;
    transient ResultSet rs = null;

    @Inject
    InboxBizTransform inboxBizTransform;

    String sqlpequery = null;

    private UserDetail userDetail;

    @Inject
    UserToAuthorizationDOADAO userToAuthorizationDOADAO ;

    @Inject
    DoaPriorityUserNamesDAO doaPriorityUserNamesDAO;


    String userName = null;
    ArrayList<PERoster> rosterViewList = null;
    String tableName = null;
    String query = null;
    String sqlForCreatedByMe = null;
    String sqlQueryForReturnByMe = null;
    PreparedStatement statement = null;
    PERoster peRoster = null;

    @Inject
    ActionDAO actionDAO;

    @Inject
    InboxTableNameDAO inboxTableNameDAO;

    @Inject
    QueueNameIdDAO queueNameIdDAO;

    @Inject
    FetchQueueNameDAO fetchQueueNameDAO;

    @Inject
    public PEDBExecute()
    {

    }

    public List<PEInbox> getPEInbox(String inboxname)
    {
        log.info("controller in getPEInbox method of pedbexecute class");

        List<PEInbox> inboxViewList = new ArrayList<PEInbox>();

        log.info("inboxname in getPEInbox method of pedbexecute class is : {}",inboxname);

        try
        {
            // String inboxname = "My Box";

            String peQuery[] = new String[2];

            peQuery =  getSqlpeQuery(inboxname);

            log.info("sql query is in inbox method is  : {}", peQuery[1]);

            inboxViewList =  getResultSetExecution(peQuery) ;

        }
        catch(Exception e)
        {
            log.info("exception occurred while fetching data from pe database : {}",e);
        }
        finally
        {
            sqlpequery = null;
        }

        return inboxViewList;
    }

    public String[] getSqlpeQuery(String inboxname)
    {
        log.info("controller comes to getSqlpeQuery method is :{}",inboxname);

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String peQuery[] = new String[2];

        try
        {

            int inboxid = inboxTableNameDAO.getInboxId(inboxname);

            log.info("inbox id is :::::::::::::: {}",inboxid);

            int roleid = userDetail.getRoleId();

            log.info("role id is :::: : {}",roleid);

            List<QueueNameId> queuenameidandfilterconditionlist = new ArrayList<QueueNameId>();

            queuenameidandfilterconditionlist = queueNameIdDAO.getQueueNameIdFilterCondition(inboxid,roleid);

            Iterator iterator = queuenameidandfilterconditionlist.iterator();

            int queueid = 0;

            String filterstring = null;

            String doaprioritystringvalue = null;

            while(iterator.hasNext() == true)
            {
                QueueNameId queueNameId = new QueueNameId();

                queueNameId = (QueueNameId)iterator.next();

                queueid = queueNameId.getQueueid();

                filterstring = queueNameId.getFiltercondition();

            }

            log.info("queue id obtained is ::::::::::{}",queueid);

            log.info("filter string obtained is :::::::::::::: {}",filterstring);

            String queuename =  fetchQueueNameDAO.getQueueTableName(queueid);

            peQuery[0] = queuename;

            log.info("queue name obtained is : {}",queuename);

            String username = userDetail.getUserName();

            log.info("username is ::::::::::::::: {}",username);

            int Teamid = userDetail.getTeamid();

            String TeamName = String.valueOf(Teamid);

            log.info("Team Name is ::::::::::",TeamName);

            List<DoaPriorityUserNames> doaPriorityUserNames11 = doaPriorityUserNamesDAO.getDoaPriorityUserNames();

            Iterator iterator2 = doaPriorityUserNames11.iterator();

            boolean flag = false;


            String  doapriorityusername13 = null;

            while(iterator2.hasNext() == true)
            {
                DoaPriorityUserNames doaPriorityUserNames12 = new DoaPriorityUserNames();

                doaPriorityUserNames12 = (DoaPriorityUserNames)iterator2.next();

                doapriorityusername13 =  doaPriorityUserNames12.getUserid();

                // doapriorityusername1 =   doapriorityusername13.substring(0,3);

                if(doapriorityusername13.equalsIgnoreCase(username))
                {
                    flag = true;

                }

            }

            if(flag)
            {

                List<AuthorizationDOA> doaprioritylist =   userToAuthorizationDOADAO.findbyusernameasid(username);

                List<AuthorizationDOA>  doaprioritylist1 = new ArrayList(new HashSet(doaprioritylist));

                log.info("list :::::::{}",doaprioritylist1.size());

                Iterator iterator1 = doaprioritylist1.iterator();

                int doapriority = 0;

                if(iterator1.hasNext())
                {
                    AuthorizationDOA authorizationDOA = new AuthorizationDOA();

                    authorizationDOA = (AuthorizationDOA)iterator1.next();

                    doapriority = authorizationDOA.getDoapriorityorder();

                }

                log.info("doapriority value is :::::::::::::::: : {} ",doapriority);

                doaprioritystringvalue = String.valueOf(doapriority);
            }

            String  sqlpequery1 = "select " + peInboxQuery + " from ";

            if(filterstring.equalsIgnoreCase("NA") || filterstring.equalsIgnoreCase(""))
            {

                String sqlpequery2 = sqlpequery1 +prefix+ "." + queuename +" "+ orderbystring;

                sqlpequery =   sqlpequery2;

                log.info("sqlquery when filter condition is NA : {}",sqlpequery);
            }
            else
            {

                String filterstring1 = null;

                String filterstring2 = null;

                if(filterstring.contains("LOGGEDINUSERNAME"))
                {

                    filterstring1 = filterstring.replaceAll("LOGGEDINUSERNAME",username);

                    log.info("filter string in loop when filtercondition is LOGGEDINUSERNAME :::::::::::::: {}",filterstring1);

                    String sqlpequery2 = sqlpequery1 + prefix +"." + queuename + " where " + filterstring1 +" "+  orderbystring;

                    sqlpequery =   sqlpequery2;

                    log.info("sqlquery when filter condition is LOGGEDINUSERNAME : {}",sqlpequery);

                }
                else if(filterstring.contains("LOGGEDINUSERTEAMNAME"))
                {
                    filterstring1 = filterstring.replaceAll("LOGGEDINUSERTEAMNAME",TeamName);

                    log.info("filter string  in loop when filtercondition is LOGGEDINUSERTEAMNAME :::::::::::::: {}",filterstring1);

                    String sqlpequery2 = sqlpequery1 + prefix +"." + queuename + " where " + filterstring1 +" "+  orderbystring;

                    sqlpequery =   sqlpequery2;

                    log.info("sqlquery when filter condition is LOGGEDINUSERNAME : {}",sqlpequery);
                }
                if(filterstring.contains("LOGGEDINUSERDOAPRIORITYORDER"))
                {
                    filterstring2 = filterstring1.replaceAll("LOGGEDINUSERDOAPRIORITYORDER",doaprioritystringvalue);

                    log.info("filter string  in loop when filtercondition is LOGGEDINUSERDOAPRIORITYORDER :::::::::::::: {}",filterstring2);

                    String sqlpequery2 = sqlpequery1 + prefix +"." + queuename + " where " + filterstring2  +" "+  orderbystring;

                    sqlpequery =   sqlpequery2;

                }

                log.info("sql query is in inbox ::::::::::::::::::::::::::: {}", sqlpequery);

            }

            peQuery[1] = sqlpequery;

            return peQuery;
        }
        catch(Exception e)
        {
            log.info("exception occurred while fetching data from pe database : {}",e);
        }
        finally
        {
            sqlpequery = null;

        }

        peQuery[1] = sqlpequery;

        return peQuery;

    }

    public List<PEInbox> getResultSetExecution(String[] peQuery)
    {
        List<PEInbox> resultQueryList = new ArrayList<PEInbox>();

        try
        {
            log.info("controller entered in to getResultSetExecution method of pedbexcecute class");
            log.info("connection url from properties file :{}",connPE);

            conn = dbContext.getConnection(connPE, peUser, pePassword);

            log.info("connection is : {}", conn.toString());

            String sqlquery = peQuery[1];

            log.info("sqlpequery : {}",sqlquery);

            String inboxName = peQuery[0];

            log.info("inboxname : {}",inboxName);

            if(inboxName.contains("_Inbox"))
            {
                inboxName = "Inbox(0)";
            }

            else
            {
                inboxName = inboxName.substring(inboxName.indexOf("_")+1,inboxName.length());
            }
            log.info("inboxname : {}",inboxName);

            PreparedStatement statement = conn.prepareStatement(sqlquery);

            log.info("statement is : {}",statement);

            rs = statement.executeQuery();

            log.info("resultset is : {}", rs);

            while (rs.next())
            {
                PEInbox peInbox = new PEInbox();

                peInbox.setReceiveddate((rs.getObject("ReceivedDate1").toString().trim()));
                peInbox.setAtuserteam(rs.getString("TeamName"));
                peInbox.setApplicationno(rs.getString("AppNumber"));
                peInbox.setName(rs.getString("BorrowerName"));
                peInbox.setProductgroup(rs.getString("ProductGroup"));
                peInbox.setRequestTypeStr(rs.getString("RequestTypeStr"));
                peInbox.setStepId(Integer.parseInt(rs.getString("Step_Code")));
                peInbox.setStatus(rs.getString("Status"));
                peInbox.setFromuser(rs.getString("PreviousUser"));
                peInbox.setAtuser(rs.getString("CurrentUser"));
                peInbox.setAppointmentdate((rs.getObject("AppointmentDate1").toString().trim()));
                peInbox.setDoalevel(rs.getString("DOALevel"));
                peInbox.setAction(rs.getString("PreviousAction"));
                peInbox.setSlastatus(rs.getString("SLAStatus"));
                peInbox.setSlaenddate((rs.getObject("SLAEndTime1").toString().trim()));
                peInbox.setTotaltimespentatprocess(rs.getInt("TotalTimeAtProcess"));
                peInbox.setTotaltimespentatuser(rs.getInt("TotalTimeAtUser"));
                peInbox.setStatuscode(rs.getString("StatusCode"));
                peInbox.setFwobnumber(rs.getString("F_WobNum"));
                peInbox.setQueuename(inboxName);
                peInbox.setStep(rs.getString("F_StepName"));
                resultQueryList.add(peInbox);

                log.info("resultQueryList pedbexecute class is : {}",resultQueryList);

                peInbox = null;

            }
            rs.close();
            conn.close();
            conn = null;

        }
        catch(Exception e)
        {

        }

        return resultQueryList;
    }

    //Tempory to remove
    public List<WorkCase> getWorkCase() {
        List<WorkCase> workCases = workCaseDAO.findAll();

        return workCases;
    }

    //Tempory to remove
    public List<WorkCasePrescreen> getWorkCasePreScreen() {
        List<WorkCasePrescreen> workCasePrescreenList = workCasePrescreenDAO.findAll();

        return workCasePrescreenList;
    }

    public String getLandingPage(long stepId){
        StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepId(stepId);
        String landingPage = "";
        if(stepLandingPage != null){
            landingPage = stepLandingPage.getPageName();
        } else {
            landingPage = "LANDING_PAGE_NOT_FOUND";
        }
        return landingPage;
    }

    //TODO:: To review Application Header.
    public AppHeaderView getHeaderInformation(String stepId, String wobNumber) {
        log.info("getHeaderInformation ::: StepId : {} , WOBNumber : {}", stepId, wobNumber);
        AppHeaderView appHeaderView = new AppHeaderView();
        appHeaderView.setBorrowerHeaderViewList(new ArrayList<AppBorrowerHeaderView>());
        String bdmUserId;
        String uwUserId = "";

        int intStepId = Integer.parseInt(stepId);

        List<Customer> customerList = new ArrayList<Customer>();
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        if(intStepId == StepValue.PRESCREEN_INITIAL.value() || intStepId == StepValue.PRESCREEN_CHECKER.value() || intStepId == StepValue.PRESCREEN_MAKER.value())
        {
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(wobNumber);
            log.info("getHeaderInformation ::: workCasePreScreen : {}", workCasePrescreen);
            bdmUserId = ((User)workCasePrescreen.getCreateBy()).getId();

            appHeaderView.setCaNo(workCasePrescreen.getCaNumber());
            appHeaderView.setAppNo(workCasePrescreen.getAppNumber());
            //appHeaderView.setAppRefNo(workCase.getAppN);
            //appHeaderView.setAppRefDate();
            appHeaderView.setCaseStatus(workCasePrescreen.getStatus().getDescription());

            customerList = customerDAO.findBorrowerByWorkCasePreScreenId(workCasePrescreen.getId());
            customerInfoViewList = customerTransform.transformToViewList(customerList);
            log.debug("customerInfo size : {}", customerInfoViewList.size());

        }

        else
        {

            WorkCase workCase = workCaseDAO.findByWobNumber(wobNumber);
            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCase.getId());
            bdmUserId = workCase.getCreateBy().getId();

            appHeaderView.setCaNo(basicInfo.getCaNumber());
            appHeaderView.setAppNo(workCase.getAppNumber());
            appHeaderView.setCaseStatus(workCase.getStatus().getDescription());

            customerList = customerDAO.findBorrowerByWorkCaseId(workCase.getId());
            customerInfoViewList = customerTransform.transformToViewList(customerList);
            log.debug("customerInfo size : {}", customerInfoViewList.size());

        }

        log.info("getHeaderInformation ::: customerInfoViewList : {}", customerInfoViewList);
        if (customerInfoViewList != null) {
            List<AppBorrowerHeaderView> appBorrowerHeaderViewList = new ArrayList<AppBorrowerHeaderView>();
            for (CustomerInfoView item : customerInfoViewList) {
                AppBorrowerHeaderView appBorrowerHeaderView = new AppBorrowerHeaderView();
                if (item.getTitleTh() != null) {
                    appBorrowerHeaderView.setBorrowerName(item.getTitleTh().getTitleTh() + "" + item.getFirstNameTh() + " " + item.getLastNameTh());
                } else {
                    appBorrowerHeaderView.setBorrowerName(item.getFirstNameTh() + " " + item.getLastNameTh());
                }
                if (item.getCustomerEntity().getId() == 1) {
                    appBorrowerHeaderView.setPersonalId(item.getCitizenId());
                } else if (item.getCustomerEntity().getId() == 2) {
                    appBorrowerHeaderView.setPersonalId(item.getRegistrationId());
                }
                appBorrowerHeaderViewList.add(appBorrowerHeaderView);
            }
            appHeaderView.setBorrowerHeaderViewList(appBorrowerHeaderViewList);
        }

        //Find product program from WorkCasePreScreenId
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePrescreenDAO.findIdByWobNumber(wobNumber));
        if (prescreen != null) {
            List<PrescreenFacility> prescreenFacilityList = prescreenFacilityDAO.findByPreScreenId(prescreen.getId());
            log.info("getHeaderInformation ::: prescreenFacilityList : {}", prescreenFacilityList);
            if (prescreenFacilityList != null) {
                List<String> productProgram = new ArrayList<String>();
                for (PrescreenFacility item : prescreenFacilityList) {
                    String prdPrg = item.getProductProgram().getDescription();
                    productProgram.add(prdPrg);
                }
                appHeaderView.setProductProgramList(productProgram);
            }
        }



        if (!Util.isEmpty(bdmUserId)) {
            User bdmUser = userDAO.findById(bdmUserId);
            if (bdmUser != null) {
                appHeaderView.setBdmName(bdmUser.getUserName());
                appHeaderView.setBdmPhoneNumber(bdmUser.getPhoneNumber());
                appHeaderView.setBdmPhoneExtNumber(bdmUser.getPhoneExt());
                if (bdmUser.getZone() != null) {
                    appHeaderView.setBdmZoneName(bdmUser.getZone().getName());
                }
                if (bdmUser.getRegion() != null) {
                    appHeaderView.setBdmRegionName(bdmUser.getRegion().getName());
                }
            }
        }

        if (!Util.isEmpty(uwUserId)) {
            User uwUser = userDAO.findById(uwUserId);
            if (uwUser != null) {
                appHeaderView.setUwName(uwUser.getUserName());
                appHeaderView.setUwPhoneNumber(uwUser.getPhoneExt());
                appHeaderView.setUwTeamName(uwUser.getTeam().getName());
            }
        }
        return appHeaderView;
    }

    public ArrayList<PERoster> getRosterQuery(String statusType, String descriptionValues)
    {
        rosterViewList = new ArrayList<PERoster>();
        log.info("controler in getRoserQuery of pedbexecute class");

        UserDetail userDetail;
        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userName = userDetail.getUserName();
        tableName = actionDAO.getRosterTableName();

        log.info("table name in peDBExecute::{}" ,tableName);

        if (statusType.equalsIgnoreCase("CreatedByMe"))
        {


            query = " select "+queryForRosterColumns+" from ";

            sqlForCreatedByMe = query +prefix+"."+tableName+ " where BDMUserName = '" + userName + "'";

            sqlpequery = sqlForCreatedByMe;
        }
        else if(statusType.equalsIgnoreCase("ReturnedByMe"))
        {
            log.info("::::::::::::::::in else block for return by me execute query::::::::::");

            query = " select "+queryForRosterColumns+" from ";

            sqlQueryForReturnByMe = query +prefix+"."+tableName+ " where PreviousUser = '" + userName + "' AND PreviousAction in (" + descriptionValues + ")";

            sqlpequery = sqlQueryForReturnByMe;


        }
        log.info("sql roster query is : {}", sqlpequery);

        try
        {
            conn = dbContext.getConnection(connPE, peUser, pePassword);

            log.info("connection is : {}", conn.toString());

            statement = conn.prepareStatement(sqlpequery);

            rs = statement.executeQuery();

            while (rs.next()) {

                peRoster = new PERoster();

                peRoster.setReceivedDate(rs.getObject("ReceivedDate1").toString().trim()) ;
                peRoster.setTeamName(rs.getString("TeamName"));
                peRoster.setAppNumber(rs.getString("AppNumber"));
                peRoster.setName(rs.getString("BorrowerName"));
                peRoster.setProductGroup(rs.getString("ProductGroup"));
                peRoster.setRequestType(rs.getString("RequestTypeStr"));
                peRoster.setStepId(Integer.parseInt(rs.getString("Step_Code")));
                peRoster.setStatus(rs.getString("Status"));
                peRoster.setCurrentUser(rs.getString("CurrentUser"));

                peRoster.setSLAEndTime(rs.getObject("SLAEndTime1").toString().trim());
                peRoster.setTotalTimeAtUser(rs.getString("TotalTimeAtUser"));
                peRoster.setTotalTimeAtProcess(rs.getString("TotalTimeAtProcess"));
                peRoster.setF_WobNum(rs.getString("F_WobNum"));
                peRoster.setStep(rs.getString("F_StepName"));
                rosterViewList.add(peRoster);
                peRoster = null;
            }

        } catch (Exception e) {

        } finally {


            peRoster = null;
            sqlpequery = null;
            userName = null;
            tableName = null;
            query = null;
            sqlForCreatedByMe = null;
            sqlQueryForReturnByMe = null;
            statement = null;
        }

        return rosterViewList;
    }

}
