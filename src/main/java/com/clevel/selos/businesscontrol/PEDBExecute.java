package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.UserToAuthorizationDOADAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.filenet.bpm.util.constants.BPMConstants;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.tool.SQLDBConnection;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.model.view.PERoster;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class PEDBExecute extends BusinessControl
{
    @Inject
    @SELOS
    Logger log;

    @Inject
    @Config(name = "interface.pe.mysql.inbox.tablename")
    String inboxQueryDB;

    @Inject
    @Config(name = "interface.pe.mysql.inbox.bdmsearchtablename")
    String bdmsearchtablename;

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
    @Config(name = "interfac.pe.mysql.inboxreturn.query")
    String peInboxReturnQuery;

    @Inject
    @Config(name = "interface.pe.mysql.bdmuwreturn.query")
    String peBDMReturnQuery;

    @Inject
    @Config(name = "interface.pe.sql.selectedColumnsForPeRosterQuery")
    String queryForRosterColumns;

    @Inject
    @Config(name = "interface.pe.sql.searchrosterquery")
    String searchrosterquery;

    /*@Inject
    @Config(name = "interface.pe.sql.CACancelled")
    String CACancelledStatusid;

    @Inject
    @Config(name = "interface.pe.sql.CARejectedbyUW1")
    String CARejectedbyUW1Statusid;

    @Inject
    @Config(name = "interface.pe.sql.CAApprovedbyUW1")
    String CAApprovedbyUW1Statusid;

    @Inject
    @Config(name = "interface.pe.sql.CARejected")
    String CARejectedStatusid;

    @Inject
    @Config(name = "interface.pe.sql.CAApproved")
    String CAApprovedStatusid;

    @Inject
    @Config(name = "interface.pe.sql.CAApprovedbyUW2")
    String CAApprovedbyUW2Statusid;

    @Inject
    @Config(name = "interface.pe.sql.CARejectedbyUW2")
    String CARejectedbyUW2Statusid;
*/
    @Inject
    private UserDAO userDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
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

    String sqlpequery = null;

    private UserDetail userDetail;

    String userName = null;

    List<PERoster> rosterViewList = null;

    String tableName = null;

    String query = null;

    String sqlForCreatedByMe = null;

    String sqlQueryForReturnByMe = null;

    PreparedStatement statement = null;

    PERoster peRoster = null;

    @Inject
    InboxBizTransform inboxBizTransform;

    @Inject
    UserToAuthorizationDOADAO userToAuthorizationDOADAO ;

    @Inject
    DoaPriorityUserNamesDAO doaPriorityUserNamesDAO;

    @Inject
    ActionDAO actionDAO;

    @Inject
    InboxTableNameDAO inboxTableNameDAO;

    @Inject
    QueueNameIdDAO queueNameIdDAO;

    @Inject
    FetchQueueNameDAO fetchQueueNameDAO;

    @Inject
    SearchUserIdDAO searchUserIdDAO;

    @Inject
    SearchApplicationNoDAO searchApplicationNoDAO;

    @Inject
    WorkCaseIdByCustomerIdDAO workCaseIdByCustomerIdDAO;

    @Inject
    SearchApplicationNoByWorkCasePrescreenIdDAO applicationNoByWorkCasePrescreenIdDAO;

    @Inject
    SearchCitizenIdDAO searchCitizenIdDAO;

    @Inject
    SearchRegistrationIdDAO searchRegistrationIdDAO;

    @Inject
    SearchFirstLastNameDAO searchFirstLastNameDAO;

    @Inject
    CompletedCasesWKItemsDAO completedCasesWKItemsDAO;

    @Inject
    BPMActiveDAO bpmActiveDAO;

    @Inject
    RequestTypeDAO requestTypeDAO;

    @Inject
    ProductGroupDAO productGroupDAO;

    @Inject
    UserTeamDAO userTeamDAO;

    @Inject
    StatusDAO statusDAO;

    @Inject
    StepDAO stepDAO;

    List<String> appnumberlistavoidduplicates = null;

    List<String> SearchApplicationNumberList = null;

    List<CompletedCasesWKItems> cmpltedwrkcaseitemslist = null;

    List<PEInbox> searchViewList = null;

    List<PEInbox> inboxViewList = null;

	String  queryChangeOwner = null;
    @Inject
    public PEDBExecute()
    {

    }

    public List<PEInbox> getPEInbox(String inboxName) throws Exception
    {
        log.info("controller in getPEInbox method of pedbexecute class");

        inboxViewList = new ArrayList<PEInbox>();

        log.info("inboxname in getPEInbox method of pedbexecute class is : {}",inboxName);

        try {
            String peQuery[] = new String[2];

            peQuery =  getSqlpeQuery(inboxName);

            log.info("sql query is in inbox method is  : {}", peQuery[1]);

            inboxViewList =  getResultSetExecution(peQuery) ;
            sqlpequery = null;

        } catch(Exception e) {
            log.error("exception occurred while fetching data from pe database : {}",e);
            sqlpequery = null;
            throw e;
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
            log.error("exception occurred while fetching data from pe database : {}",e);
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

            int fetchType;

            if(inboxName.contains("ROSTER"))
            {
                fetchType = BPMConstants.FETCH_TYPE_ROSTER;
            }

            else {
                fetchType = BPMConstants.FETCH_TYPE_QUEUE;
            }

            log.info("inboxname : {}",inboxName);

            PreparedStatement statement = conn.prepareStatement(sqlquery);

            log.info("statement is : {}",statement);

            rs = statement.executeQuery();

            log.info("resultset is : {}", rs);

            while (rs.next())
            {
                //log.info("in while .. ");
                PEInbox peInbox = new PEInbox();
                //log.info("in while");
                peInbox.setReceiveddate((rs.getObject("ReceivedDate1").toString().trim()));
                peInbox.setAtuserteam(rs.getString("TeamName"));
                peInbox.setApplicationno(rs.getString("AppNumber"));
                peInbox.setName(rs.getString("BorrowerName"));
                peInbox.setProductgroup(rs.getString("ProductGroup"));
                peInbox.setRequestTypeStr(rs.getString("RequestTypeStr"));
                peInbox.setStepId(Long.parseLong(rs.getString("Step_Code")));
                peInbox.setStatus(rs.getString("Status"));
                if(rs.getString("PreviousUser") != null)
                {
                    peInbox.setFromuser(userDAO.getUserNameById(rs.getString("PreviousUser")));
                }
                if(rs.getString("CurrentUser") != null)
                {
                    peInbox.setAtuser(userDAO.getUserNameById(rs.getString("CurrentUser")));
                }
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
                peInbox.setFetchType(fetchType);
                resultQueryList.add(peInbox);

                //log.info("resultQueryList pedbexecute class is : {}",resultQueryList);

                peInbox = null;

            }
            rs.close();
            conn.close();
            conn = null;

        }
        catch(Exception e)
        {
            log.error("Error :",e);
            e.printStackTrace();
        }

        return resultQueryList;
    }

    public List<PERoster> getRosterQuery(String statusType, String descriptionValues)
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

            sqlQueryForReturnByMe = query +prefix+"."+tableName+ " where lower(PreviousUser) = lower('" + userName + "') AND PreviousAction in (" + descriptionValues + ")";

            sqlpequery = sqlQueryForReturnByMe;


        }
        log.info("sql roster query is : {}", sqlpequery);

        try
        {
            conn = dbContext.getConnection(connPE, peUser, pePassword);

            log.info("connection is : {}", conn.toString());

            statement = conn.prepareStatement(sqlpequery);

            rs = statement.executeQuery();

            int fetchType = BPMConstants.FETCH_TYPE_ROSTER;

            String queueName = tableName.substring(tableName.indexOf("_")+1,tableName.length());

            while (rs.next()) {

                peRoster = new PERoster();

                if(rs.getObject("ReceivedDate1")!=null)
                {
                    peRoster.setReceivedDate(rs.getObject("ReceivedDate1").toString().trim()) ;
                }

                else
                {
                    peRoster.setReceivedDate("") ;

                }

                peRoster.setTeamName(rs.getString("TeamName"));
                peRoster.setAppNumber(rs.getString("AppNumber"));
                peRoster.setName(rs.getString("BorrowerName"));
                peRoster.setProductGroup(rs.getString("ProductGroup"));
                peRoster.setRequestType(rs.getString("RequestTypeStr"));
                if(rs.getString("Step_Code")!=null && rs.getString("Step_Code").trim().length()>0)
                {
                    log.info("Step Code : {}", rs.getString("Step_Code"));
                    try{
                        peRoster.setStepId(Integer.parseInt(rs.getString("Step_Code")));
                    }
                    catch (NumberFormatException n)
                    {
                        log.error("Invalid step id, skipping");
                        continue;
                    }

                }

                peRoster.setStatus(rs.getString("Status"));

                if(rs.getString("CurrentUser") != null)
                {
                    peRoster.setCurrentUser(userDAO.getUserNameById(rs.getString("CurrentUser")));
                }

                peRoster.setSlastatus(rs.getString("SLAStatus"));

                if(rs.getObject("SLAEndTime1")!=null)
                {
                    peRoster.setSLAEndTime(rs.getObject("SLAEndTime1").toString().trim()) ;
                }

                else
                {
                    peRoster.setSLAEndTime("") ;

                }
                peRoster.setTotalTimeAtUser(rs.getString("TotalTimeAtUser"));
                peRoster.setTotalTimeAtProcess(rs.getString("TotalTimeAtProcess"));
                peRoster.setF_WobNum(rs.getString("F_WobNum"));
                peRoster.setFetchType(fetchType);
                peRoster.setStep(rs.getString("F_StepName"));
                peRoster.setQueuename(queueName);

                rosterViewList.add(peRoster);
                peRoster = null;
            }

        } catch (Exception e) {

            log.error("Error :",e);

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



    public List<PEInbox> getPESearchList(String statustype,String applicationNo,String userid,String step,String status,String citizenid,String firstname,String lastname,Date date1,Date date2,Date date3,Date date4)
    {
        log.info(":::::::::::::controller in getPESearchList method of PEDBExecute class:::::::::::::::");

        String wherecondition = null;

        boolean flag = false;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        searchViewList = new ArrayList<PEInbox>();

        String stringapplicationnumbers = null;

        String strappnumbers = null;

        boolean appNumberCriteria=false;

        boolean citizenIdFlag = false;

        boolean firstNameFlag = false;

        boolean lastNameFlag = false;

        List totalAppNumberList = new ArrayList();

        if(statustype == "" || statustype.equalsIgnoreCase("InprocessCases"))
        {
            try
            {
                if(applicationNo != null && applicationNo.trim().length() >0)
                {
                    log.info("appnumber if : {}",applicationNo);
                    appNumberCriteria = true;
                }
                if(step != null && step.trim().length()>0)
                {
                    log.info("step :",step);
                    if(flag)
                    {
                        wherecondition += " AND Step_Code = '"+step+"' ";
                    }
                    else
                    {

                        wherecondition = " where Step_Code = '"+step+"' ";
                    }
                    flag = true;

                    log.info("where condition in step :",wherecondition);

                }
                if(status != null && status.trim().length() >0)
                {
                    log.info("Status :",status);
                    if(flag)
                    {
                        wherecondition += " AND StatusCode ='"+status+"' ";
                    }
                    else
                    {

                        wherecondition = " where StatusCode ='"+status+"' ";
                    }
                    flag = true;

                }
                //log.info(date1.toString()+""+date2.toString());

                if(date1 != null && date2 != null)
                {

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date2);
                    cal.set(Calendar.HOUR_OF_DAY,23);
                    cal.set(Calendar.MINUTE,59);
                    cal.set(Calendar.SECOND,59);
                    date2 = cal.getTime();
                     log.info("in date : "+date2.toString());
                    if(flag)
                    {
                        wherecondition += " AND ReceivedTime >=  " +(date1.getTime()/1000)+ " AND  ReceivedTime <=" +(date2.getTime()/1000);
                    }
                    else
                    {
                        wherecondition = " where ReceivedTime >=  " +(date1.getTime()/1000)+ " AND  ReceivedTime <=" +(date2.getTime()/1000);
                    }
                    flag = true;
                }


                if(userid != null && userid.trim().length() >0 )
                {
                    /*appnumberlistavoidduplicates = new ArrayList<String>();

                    appnumberlistavoidduplicates = getApplicationNumbers(userid,null,null,null,1);

                    log.info("app number list :{}",appnumberlistavoidduplicates.toString());

                    totalAppNumberList.addAll(appnumberlistavoidduplicates);*/

                    if(flag)
                    {
                        wherecondition += " AND LOWER(CurrentUser) =LOWER('"+userid+"') ";
                    }
                    else
                    {

                        wherecondition = " where LOWER(CurrentUser) =LOWER('"+userid+"') ";
                    }



                    flag = true;

                }

                if(citizenid != null && citizenid.trim().length() >0)
                {
                    appnumberlistavoidduplicates = new ArrayList<String>();

                    appnumberlistavoidduplicates = getApplicationNumbers(null,citizenid,null,null,1);

                    log.info("app number list 2:{}",appnumberlistavoidduplicates.toString());

                    if(totalAppNumberList.size()>0)
                    {
                        totalAppNumberList.retainAll(appnumberlistavoidduplicates);
                        appnumberlistavoidduplicates = totalAppNumberList;
                    }

                    else
                    {
                        totalAppNumberList.addAll(appnumberlistavoidduplicates);
                    }

                    citizenIdFlag = true;

                }
                if(firstname != null && firstname.trim().length() >0)
                {
                    appnumberlistavoidduplicates = new ArrayList<String>();

                    appnumberlistavoidduplicates = getApplicationNumbers(null,null,firstname,null,1);

                    log.info("app number list 3:{}",appnumberlistavoidduplicates.toString());

                    if(totalAppNumberList.size()>0)
                    {
                        totalAppNumberList.retainAll(appnumberlistavoidduplicates);
                        appnumberlistavoidduplicates = totalAppNumberList;
                    }

                    else if(citizenIdFlag)
                    {
                        return searchViewList;
                    }
                    else
                    {
                        totalAppNumberList.addAll(appnumberlistavoidduplicates);
                    }

                    firstNameFlag = true;

                }

                if(lastname != null && lastname.trim().length() >0)
                {
                    appnumberlistavoidduplicates = new ArrayList<String>();

                    appnumberlistavoidduplicates = getApplicationNumbers(null,null,null,lastname,1);

                    log.info("app number list 4:{}",appnumberlistavoidduplicates.toString());

                    if(totalAppNumberList.size()>0)
                    {
                        totalAppNumberList.retainAll(appnumberlistavoidduplicates);
                        appnumberlistavoidduplicates = totalAppNumberList;
                    }

                    else if(citizenIdFlag || firstNameFlag)
                    {
                        return searchViewList;
                    }

                    else
                    {
                        totalAppNumberList.addAll(appnumberlistavoidduplicates);
                    }

                    lastNameFlag = true;

                }

                if (appNumberCriteria)
                {
                    log.info("app number in if ");
                    if(totalAppNumberList.size()>0)
                    {
                        if(totalAppNumberList.contains(applicationNo))
                        {

                            if(flag)
                            {
                                wherecondition +=  " AND where AppNumber = '" +applicationNo + "'";
                            }
                            else
                            {
                                wherecondition =  " where AppNumber = '" +applicationNo + "'";
                            }

                        }


                        else
                        {
                            return searchViewList;
                        }

                        log.info("where condition :{}"+wherecondition);
                    }

                    else if(citizenIdFlag || lastNameFlag || firstNameFlag)
                    {

                        return searchViewList;

                    }

                    else
                    {

                        if(flag)
                        {
                            wherecondition +=  " AND AppNumber = '" +applicationNo + "'";
                        }
                        else
                        {
                            wherecondition =  " where AppNumber = '" +applicationNo + "'";
                        }

                        log.info("where condition :{}"+wherecondition);
                    }

                }

                else
                {
                    if(totalAppNumberList.size()>0)
                    {

                        stringapplicationnumbers = totalAppNumberList.toString();

                        strappnumbers = stringapplicationnumbers.replace("[","").replace("]","");

                        log.info("strappnumbers if not null :::::::::{}",strappnumbers);

                        if(flag)
                        {
                            wherecondition += " AND AppNumber IN (" +strappnumbers+ ")";
                        }
                        else
                        {
                            wherecondition = " where AppNumber IN (" +strappnumbers+ ")";
                        }
                    }

                    else if(citizenIdFlag || lastNameFlag || firstNameFlag)
                    {

                        return searchViewList;

                    }

                    else if(!flag)
                    {
                       return searchViewList;
                    }

                }

                log.info("where condition final :{}"+wherecondition);

                String rostertableaname = actionDAO.getRosterTableName();

                log.info("rostertablename in getPESearchList method is : {}",rostertableaname);

                String  sqlpequery1 = "select "  +searchrosterquery+ "  from " +prefix+"." +rostertableaname;

                log.info("sqlquery with our where condition is :::::::::::::{}",sqlpequery1);

                sqlpequery = sqlpequery1 + wherecondition;

                log.info("sqlquery in getPESearchList is ::::::::: {}",sqlpequery);

                searchViewList =  getPESearchResultSetList(sqlpequery,rostertableaname);


            }
            catch(Exception e)
            {
                log.error("Error in inprocess search : {}",e);
            }
            finally
            {

            }
            return  searchViewList;
        }
        else if(statustype.equalsIgnoreCase("CompletedCases"))
        {
            log.info("controller entered in to statustype is equal to completedCases condition ");

            List<String> completedCasesAppNoList = new ArrayList<String>();

            List<String> completedCasesAppNoListByUserId = new ArrayList<String>();

            List<String> completedCasesAppNoListByName = new ArrayList<String>();

            List<String> completedCasesAppNoListByCitizenId = new ArrayList<String>();

            int statuscode = 0;

            flag = false;

            searchViewList = new ArrayList<PEInbox>();

            cmpltedwrkcaseitemslist = new ArrayList<CompletedCasesWKItems>();

            try
            {
                if(applicationNo != null && applicationNo.trim().length()>0)
                {
                    //completedCasesAppNoList.add(applicationNo);

                    flag = true;
                }

                boolean userIdFlag = false;

                if(userid != null && userid.trim().length()>0)
                {
                    log.info("user id is not null in Completedcaes ");

                    //completedCasesAppNoList = getApplicationNumbers(userid,null,null,null,0);
                    completedCasesAppNoListByUserId = getApplicationNumbers(userid,null,null,null,0);

                    completedCasesAppNoList.addAll(completedCasesAppNoListByUserId);

                    userIdFlag = true;
                }
                if(citizenid != null && citizenid.trim().length()>0)
                {
                    log.info("citizen id is not null in Completedcaes ");
                    //completedCasesAppNoList = getApplicationNumbers(null,citizenid,null,null,0);
                    completedCasesAppNoListByCitizenId = getApplicationNumbers(null,citizenid,null,null,0);

                    if(completedCasesAppNoList.size()>0)
                    {
                        completedCasesAppNoList.retainAll(completedCasesAppNoListByCitizenId);
                    }

                    else if(userIdFlag)
                    {
                        return searchViewList;
                    }

                    else
                    {
                        completedCasesAppNoList.addAll(completedCasesAppNoListByCitizenId);
                    }

                    citizenIdFlag = true;

                }
                if(firstname != null && firstname.trim().length()>0)
                {
                    log.info("first name and last name is not null in Completedcaes ");
                    //completedCasesAppNoList = getApplicationNumbers(null,null,firstname,lastname,0);
                    completedCasesAppNoListByName = getApplicationNumbers(null,null,firstname,null,0);

                    if(completedCasesAppNoList.size()>0)
                    {
                        completedCasesAppNoList.retainAll(completedCasesAppNoListByName);
                    }

                    else if(citizenIdFlag || userIdFlag)
                    {
                        return searchViewList;
                    }

                    else
                    {
                        completedCasesAppNoList.addAll(completedCasesAppNoListByName);
                    }

                    firstNameFlag = true;
                }

                if(lastname != null && lastname.trim().length()>0)
                {
                    log.info("first name and last name is not null in Completedcaes ");
                    //completedCasesAppNoList = getApplicationNumbers(null,null,firstname,lastname,0);
                    completedCasesAppNoListByName = getApplicationNumbers(null,null,null,lastname,0);

                    if(completedCasesAppNoList.size()>0)
                    {
                        completedCasesAppNoList.retainAll(completedCasesAppNoListByName);
                    }

                    else if(citizenIdFlag || firstNameFlag || userIdFlag)
                    {
                        return searchViewList;
                    }

                    else
                    {
                        completedCasesAppNoList.addAll(completedCasesAppNoListByName);
                    }

                    lastNameFlag = true;
                }
                if(status != null && status.trim().length()>0)
                {

                    statuscode = Integer.parseInt(status);
                    log.info("status code : "+statuscode);
                }

                log.info("App Numbers :{}",completedCasesAppNoList.size());

                /*completedCasesAppNoList.addAll(completedCasesAppNoListByCitizenId);
                completedCasesAppNoList.retainAll(completedCasesAppNoListByName);
                completedCasesAppNoList.retainAll(completedCasesAppNoListByUserId);

                log.info("after :{}",completedCasesAppNoList.size());*/

                if(flag)
                {
                    if(completedCasesAppNoList.size()> 0)
                    {
                        if(completedCasesAppNoList.contains(applicationNo))
                        {
                            completedCasesAppNoList.clear();
                            completedCasesAppNoList.add(applicationNo);
                            cmpltedwrkcaseitemslist = completedCasesWKItemsDAO.getCompletedCasesWKItems(completedCasesAppNoList,statuscode,date1,date2,date3,date4);
                        }
                        else
                        {
                            return searchViewList;
                        }

                    }

                    else if(citizenIdFlag || firstNameFlag || userIdFlag ||lastNameFlag)
                    {
                        return searchViewList;
                    }

                    else
                    {
                        completedCasesAppNoList.clear();
                        completedCasesAppNoList.add(applicationNo);
                        cmpltedwrkcaseitemslist = completedCasesWKItemsDAO.getCompletedCasesWKItems(completedCasesAppNoList,statuscode,date1,date2,date3,date4);
                    }

                }
                else
                {

                    if((citizenIdFlag || firstNameFlag || userIdFlag ||lastNameFlag) && (completedCasesAppNoList==null || completedCasesAppNoList.size()==0))
                    {
                        return searchViewList;
                    }

                    else
                    {
                        if(completedCasesAppNoList!=null && completedCasesAppNoList.size()>0 && completedCasesAppNoList.get(0).length()>5)
                        {
                            cmpltedwrkcaseitemslist = completedCasesWKItemsDAO.getCompletedCasesWKItems(completedCasesAppNoList,statuscode,date1,date2,date3,date4);
                        }
                        else
                        {
                            cmpltedwrkcaseitemslist = completedCasesWKItemsDAO.getCompletedCasesWKItems(null,statuscode,date1,date2,date3,date4);
                        }

                    }

                }

                //cmpltedwrkcaseitemslist = completedCasesWKItemsDAO.getCompletedCasesWKItems(completedCasesAppNoList,statuscode,date1,date2,date3,date4);

                log.info("cmpletedwrkcaseitemslist is : {}",cmpltedwrkcaseitemslist.size());

                Iterator itr = cmpltedwrkcaseitemslist.iterator();



                while(itr.hasNext() == true)
                {
                    PEInbox peInbox = new PEInbox();

                    CompletedCasesWKItems completedCasesWKItems = new CompletedCasesWKItems();

                    completedCasesWKItems = (CompletedCasesWKItems)itr.next();

                    log.info("completedCasewrkitems APPLICATION NO : {}",completedCasesWKItems.getApplicationNo());

                    if(completedCasesWKItems.getCreatedate()!=null)
                    {
                        peInbox.setReceiveddate(completedCasesWKItems.getCreatedate().toString());
                    }
                    else
                    {
                        peInbox.setReceiveddate("");
                    }

                    if(completedCasesWKItems.getCreateBy()!=null)
                    {
                        if(userDAO.findByUserName(completedCasesWKItems.getCreateBy()) != null)
                        {
                            log.info("Create By : {}",completedCasesWKItems.getCreateBy());
                            if( userDAO.findByUserName(completedCasesWKItems.getCreateBy()).getTeam() != null )
                            {
                                peInbox.setAtuserteam(userTeamDAO.teamNameById(userDAO.findByUserName(completedCasesWKItems.getCreateBy()).getTeam().getId()));
                            }

                        }
                    }
                    else
                    {
                        peInbox.setAtuserteam("");
                    }

                    peInbox.setApplicationno(completedCasesWKItems.getApplicationNo());

                    if(peInbox.getApplicationno()!=null)
                    {

                        WorkCase workCase = workCaseDAO.findByAppNumber(peInbox.getApplicationno());

                        String names = "";

                        if(workCase==null)
                        {
                            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(peInbox.getApplicationno());

                            names = searchFirstLastNameDAO.getBorrowerNameByWorkCasePrescreenID(workCasePrescreen.getId());
                        }

                        else
                        {
                            names = searchFirstLastNameDAO.getBorrowerNameByWorkCaseID(workCase.getId());
                        }

                        peInbox.setName(names);

                    }

                    if(completedCasesWKItems.getProductgroupid()!=null)
                    {
                        log.info("in pedbexecute : product group id :"+completedCasesWKItems.getProductgroupid());
                        peInbox.setProductgroup(productGroupDAO.productGroupNameById(completedCasesWKItems.getProductgroupid()));
                    }

                    else
                    {
                        peInbox.setProductgroup("");
                    }

                    if(completedCasesWKItems.getRequesttypeid()!=null)
                    {
                        log.info("in pedbexecute : requesttype id :"+completedCasesWKItems.getRequesttypeid());
                        peInbox.setRequestTypeStr(requestTypeDAO.requestTypeById(completedCasesWKItems.getRequesttypeid()));
                    }

                    else
                    {
                        peInbox.setRequestTypeStr("");
                    }


                    if(completedCasesWKItems.getStepid()!=null)
                    {
                        Integer a = completedCasesWKItems.getStepid();

                        peInbox.setStepId(Long.parseLong(a.toString()));
                    }

                    else
                    {
                        peInbox.setStepId(null);
                    }

                    //if(completedCasesWKItems.gets)

                    peInbox.setStep(stepDAO.stepNameById(0));

                    if(completedCasesWKItems.getStatusid()!=null)
                    {
                        peInbox.setStatus(statusDAO.statusNameById(completedCasesWKItems.getStatusid()));
                    }

                    else
                    {
                        peInbox.setStatus("");
                    }

                    peInbox.setFromuser(completedCasesWKItems.getFromuserid());

                    peInbox.setAtuser(userDAO.getUserNameById(completedCasesWKItems.getCreateBy()));

                    if(completedCasesWKItems.getAppointmentDate()!=null)
                    {
                        peInbox.setAppointmentdate(completedCasesWKItems.getAppointmentDate().toString());
                    }

                    else
                    {
                        peInbox.setAppointmentdate("");
                    }

                    if(completedCasesWKItems.getDoalevelid()!=null)
                    {
                        peInbox.setDoalevel(String.valueOf(completedCasesWKItems.getDoalevelid()));
                    }

                    else
                    {
                        peInbox.setDoalevel("");
                    }

                    peInbox.setAction("");

                    peInbox.setSlastatus("");

                    if(completedCasesWKItems.getSlaenddate()!=null)
                    {
                        peInbox.setSlaenddate(completedCasesWKItems.getSlaenddate().toString());
                    }

                    else
                    {
                        peInbox.setSlaenddate("");
                    }

                    if(completedCasesWKItems.getCompletedate()!=null)
                    {
                        peInbox.setTerminateDate(completedCasesWKItems.getCompletedate().toString());
                    }

                    else
                    {
                        peInbox.setTerminateDate("");
                    }

                    peInbox.setTotaltimespentatprocess(completedCasesWKItems.getTotaltimeatprocess());

                    peInbox.setTotaltimespentatuser(completedCasesWKItems.getTotaltimeatuser());

                    peInbox.setFwobnumber(completedCasesWKItems.getWobnumber());

                    log.info("resultQueryList for completed caseses in pedbexecute class is : {}",searchViewList.size());

                    searchViewList.add(peInbox);

                    peInbox = null;

                }



            }
            catch (Exception e)
            {
                 log.error("Error in Completed casessearch :{}",e);
            }
            finally {

            }
            return  searchViewList;
        }

        return  searchViewList;
    }

    public List<PEInbox> getPESearchResultSetList(String query,String tableName)
    {
        List<PEInbox> peSearchResultSetList = new ArrayList<PEInbox>();

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("query in getPESearchResultSetList is :::::::::{}",query);

        try
        {

            conn = dbContext.getConnection(connPE, peUser, pePassword);

            PreparedStatement statement = conn.prepareStatement(query);

            log.info("statement is : {}",statement);

            rs = statement.executeQuery();

            log.info("resultset is : {}", rs);

            tableName = tableName.substring(tableName.indexOf("_")+1,tableName.length());

            int fetchType;

            if(tableName.contains("ROSTER"))
            {
               fetchType = BPMConstants.FETCH_TYPE_ROSTER;
            }

            else {
                fetchType = BPMConstants.FETCH_TYPE_QUEUE;
            }

            while (rs.next())
            {
                PEInbox peInbox = new PEInbox();

                if(rs.getObject("ReceivedDate1")!=null)
                {
                    peInbox.setReceiveddate((rs.getObject("ReceivedDate1").toString().trim()));
                }

                else
                {
                    peInbox.setReceiveddate("");
                }

                peInbox.setAtuserteam(rs.getString("TeamName"));

                peInbox.setApplicationno(rs.getString("AppNumber"));

                if(peInbox.getApplicationno()!=null)
                {

                    WorkCase workCase = workCaseDAO.findByAppNumber(peInbox.getApplicationno());

                    String names = "";

                    if(workCase==null)
                    {
                        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(peInbox.getApplicationno());

                        if(workCasePrescreen != null)
                        {
                            names = searchFirstLastNameDAO.getBorrowerNameByWorkCasePrescreenID(workCasePrescreen.getId());
                        }

                        else
                        {
                            names = "";
                        }

                    }

                    else
                    {
                        names = searchFirstLastNameDAO.getBorrowerNameByWorkCaseID(workCase.getId());
                    }

                    peInbox.setName(names);

                }
                //peInbox.setName(rs.getString("BorrowerName"));
                peInbox.setProductgroup(rs.getString("ProductGroup"));
                peInbox.setRequestTypeStr(rs.getString("RequestTypeStr"));
                if(rs.getString("Step_Code")!=null)
                {
                    peInbox.setStepId(Long.parseLong(rs.getString("Step_Code")));
                }

                peInbox.setStatus(rs.getString("Status"));

                if(rs.getString("PreviousUser") != null)
                {
                    peInbox.setFromuser(userDAO.getUserNameById(rs.getString("PreviousUser")));
                }

                if(rs.getString("CurrentUser") != null)
                {
                    peInbox.setAtuser(userDAO.getUserNameById(rs.getString("CurrentUser")));
                }

                if((rs.getObject("AppointmentDate1"))!=null)
                {
                    peInbox.setAppointmentdate((rs.getObject("AppointmentDate1").toString().trim()));
                }

                else
                {
                    peInbox.setAppointmentdate("");
                }

                peInbox.setDoalevel(rs.getString("DOALevel"));
                peInbox.setAction(rs.getString("PreviousAction"));
                peInbox.setSlastatus(rs.getString("SLAStatus"));

                if(rs.getObject("SLAEndTime1")!=null)
                {


                    peInbox.setSlaenddate((rs.getObject("SLAEndTime1").toString().trim()));

                }

                peInbox.setTotaltimespentatprocess(rs.getInt("TotalTimeAtProcess"));
                peInbox.setTotaltimespentatuser(rs.getInt("TotalTimeAtUser"));
                peInbox.setFwobnumber(rs.getString("F_WobNum"));
                peInbox.setStep(rs.getString("F_StepName"));
                peInbox.setQueuename(tableName);
                peInbox.setFetchType(fetchType);
                peInbox.setTerminateDate("");
                peSearchResultSetList.add(peInbox);

                log.info("resultQueryList pedbexecute class is : {}",peSearchResultSetList);

                peInbox = null;

            }
            rs.close();
            conn.close();
            conn = null;

        }
        catch (Exception e)
        {
            log.error("exception occurred while fetching data from pe database : {}",e);
        }
        finally
        {
            sqlpequery = null;
        }

        return peSearchResultSetList;
    }

    public List<String> getApplicationNumbers(String userid,String citizenid,String firstname,String lastname,int bpmactive)
    {

        SearchApplicationNumberList = new ArrayList<String>();

        appnumberlistavoidduplicates = new ArrayList<String>();



        log.info("controller in getApplicationNumbers method of pedbexecute java class");

        try
        {
            if(userid != null)
            {
                log.info("controller comes to  userid is not null condition");

                List<SearchUserId> searchUserIdlist = new ArrayList<SearchUserId>();

                searchUserIdlist = searchUserIdDAO.getWorkCaseIdByUserId(userid);

                Iterator iterator = searchUserIdlist.iterator();

                while(iterator.hasNext() == true)
                {
                    SearchUserId searchUserId = new SearchUserId();

                    searchUserId = (SearchUserId)iterator.next();

                    if(searchUserId.getWorkcaseid()!=null)
                    {
                        int useridbasedworkcaseid = searchUserId.getWorkcaseid();

                        List<SearchApplicationNo> applicationNoList = new ArrayList<SearchApplicationNo>();

                        applicationNoList = searchApplicationNoDAO.getApplicationNoByWorkCaseId(useridbasedworkcaseid,bpmactive);

                        Iterator iterator1 = applicationNoList.iterator();

                        while(iterator1.hasNext() == true)
                        {
                            SearchApplicationNo searchApplicationNo = new SearchApplicationNo();

                            searchApplicationNo = (SearchApplicationNo)iterator1.next();

                            String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                            SearchApplicationNumberList.add(applicationnumbervalue);
                        }
                    }

                }
            }
            else if(citizenid != null)
            {
                log.info("controller comes citizenid is not null condition");

                List<SearchCitizenId> searchCitizenIdList = new ArrayList<SearchCitizenId>();

                List<SearchRegistrationId> searchRegistrationIdList = new ArrayList<SearchRegistrationId>();

                searchCitizenIdList = searchCitizenIdDAO.getCitizenId(citizenid);

                searchRegistrationIdList = searchRegistrationIdDAO.getRegistrationId(citizenid);

                if(searchCitizenIdList.size() > 0)
                {
                    Iterator iterator = searchCitizenIdList.iterator();

                    while(iterator.hasNext() == true)
                    {
                        SearchCitizenId searchCitizenId = new SearchCitizenId();

                        searchCitizenId = (SearchCitizenId)iterator.next();

                        int citizenidbasedcustomerid = searchCitizenId.getCustomerId();

                        List<WorkCaseIdByCustomerId> workCaseIdList = new ArrayList<WorkCaseIdByCustomerId>();

                        workCaseIdList =   workCaseIdByCustomerIdDAO.getWorkCaseIdByCustomerId(citizenidbasedcustomerid);

                        Iterator wrkCaseIterator = workCaseIdList.iterator();

                        while(wrkCaseIterator.hasNext() == true)
                        {
                            WorkCaseIdByCustomerId workCaseIdByCustomerId = new WorkCaseIdByCustomerId();

                            workCaseIdByCustomerId = (WorkCaseIdByCustomerId)wrkCaseIterator.next();

                            Integer citizenidbasedworkcaseid = workCaseIdByCustomerId.getWorkCaseId();

                            if(citizenidbasedworkcaseid==null)
                            {
                                citizenidbasedworkcaseid = workCaseIdByCustomerId.getWrokCasePreScreenId();

                                List<SearchApplicationNoByWorkCasePrescreenId> applicationNoList = new ArrayList<SearchApplicationNoByWorkCasePrescreenId>();

                                applicationNoList = applicationNoByWorkCasePrescreenIdDAO.getApplicationNoByWorkCaseId(citizenidbasedworkcaseid, bpmactive);

                                Iterator iterator1 = applicationNoList.iterator();

                                while(iterator1.hasNext() == true)
                                {
                                    SearchApplicationNoByWorkCasePrescreenId searchApplicationNo = new SearchApplicationNoByWorkCasePrescreenId();

                                    searchApplicationNo = (SearchApplicationNoByWorkCasePrescreenId)iterator1.next();

                                    String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                                    SearchApplicationNumberList.add(applicationnumbervalue);
                                }
                            }

                            else {
                                List<SearchApplicationNo> applicationNoList = new ArrayList<SearchApplicationNo>();

                                applicationNoList = searchApplicationNoDAO.getApplicationNoByWorkCaseId(citizenidbasedworkcaseid,bpmactive);

                                Iterator iterator1 = applicationNoList.iterator();

                                while(iterator1.hasNext() == true)
                                {
                                    SearchApplicationNo searchApplicationNo = new SearchApplicationNo();

                                    searchApplicationNo = (SearchApplicationNo)iterator1.next();

                                    String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                                    SearchApplicationNumberList.add(applicationnumbervalue);
                                }

                            }

                        }


                    }
                }
                else if(searchRegistrationIdList.size() > 0)
                {
                    Iterator iterator = searchRegistrationIdList.iterator();

                    while(iterator.hasNext() == true)
                    {
                        SearchRegistrationId searchRegistrationId = new SearchRegistrationId();

                        searchRegistrationId = (SearchRegistrationId)iterator.next();

                        int registrationidbasedcustomerid = searchRegistrationId.getCustomerId();

                        List<WorkCaseIdByCustomerId> workCaseIdList = new ArrayList<WorkCaseIdByCustomerId>();

                        workCaseIdList =   workCaseIdByCustomerIdDAO.getWorkCaseIdByCustomerId(registrationidbasedcustomerid);

                        Iterator wrkCaseIterator = workCaseIdList.iterator();

                        while(wrkCaseIterator.hasNext() == true)
                        {
                            WorkCaseIdByCustomerId workCaseIdByCustomerId = new WorkCaseIdByCustomerId();

                            workCaseIdByCustomerId = (WorkCaseIdByCustomerId)wrkCaseIterator.next();

                            Integer registrationidbasedworkcaseid = workCaseIdByCustomerId.getWorkCaseId();

                            if(registrationidbasedworkcaseid==null)
                            {
                                registrationidbasedworkcaseid = workCaseIdByCustomerId.getWrokCasePreScreenId();

                                List<SearchApplicationNoByWorkCasePrescreenId> applicationNoList = new ArrayList<SearchApplicationNoByWorkCasePrescreenId>();

                                applicationNoList = applicationNoByWorkCasePrescreenIdDAO.getApplicationNoByWorkCaseId(registrationidbasedworkcaseid,bpmactive);

                                Iterator iterator1 = applicationNoList.iterator();

                                while(iterator1.hasNext() == true)
                                {
                                    SearchApplicationNoByWorkCasePrescreenId searchApplicationNo = new SearchApplicationNoByWorkCasePrescreenId();

                                    searchApplicationNo = (SearchApplicationNoByWorkCasePrescreenId)iterator1.next();

                                    String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                                    SearchApplicationNumberList.add(applicationnumbervalue);
                                }
                            }

                            else {
                                List<SearchApplicationNo> applicationNoList = new ArrayList<SearchApplicationNo>();

                                applicationNoList = searchApplicationNoDAO.getApplicationNoByWorkCaseId(registrationidbasedworkcaseid,bpmactive);

                                Iterator iterator1 = applicationNoList.iterator();

                                while(iterator1.hasNext() == true)
                                {
                                    SearchApplicationNo searchApplicationNo = new SearchApplicationNo();

                                    searchApplicationNo = (SearchApplicationNo)iterator1.next();

                                    String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                                    SearchApplicationNumberList.add(applicationnumbervalue);
                                }

                            }

                        }
                    }

                }

            }
            else if(firstname != null)
            {
                log.info("controller comes to  firstname and lastname is not null condition");

                List<SearchFirstLastName> firstlastnameworkcaseidlist = new ArrayList<SearchFirstLastName>();

                firstlastnameworkcaseidlist = searchFirstLastNameDAO.getFirstName(firstname);

                Iterator iterator = firstlastnameworkcaseidlist.iterator();

                while(iterator.hasNext() == true)
                {
                    SearchFirstLastName searchFirstLastName = new SearchFirstLastName();

                    searchFirstLastName = (SearchFirstLastName)iterator.next();

                    Integer firstlastnamebasedworkcaseid = searchFirstLastName.getWorkCaseId();

                    if(firstlastnamebasedworkcaseid==null)
                    {
                        firstlastnamebasedworkcaseid = searchFirstLastName.getWrokCasePreScreenId();

                        List<SearchApplicationNoByWorkCasePrescreenId> applicationNoList = new ArrayList<SearchApplicationNoByWorkCasePrescreenId>();

                        applicationNoList = applicationNoByWorkCasePrescreenIdDAO.getApplicationNoByWorkCaseId(firstlastnamebasedworkcaseid,bpmactive);

                        Iterator iterator1 = applicationNoList.iterator();

                        while(iterator1.hasNext() == true)
                        {
                            SearchApplicationNoByWorkCasePrescreenId searchApplicationNo = new SearchApplicationNoByWorkCasePrescreenId();

                            searchApplicationNo = (SearchApplicationNoByWorkCasePrescreenId)iterator1.next();

                            String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                            SearchApplicationNumberList.add(applicationnumbervalue);
                        }
                    }

                    else {

                        List<SearchApplicationNo> applicationNoList = new ArrayList<SearchApplicationNo>();

                        applicationNoList = searchApplicationNoDAO.getApplicationNoByWorkCaseId(firstlastnamebasedworkcaseid,bpmactive);

                        Iterator iterator1 = applicationNoList.iterator();

                        while(iterator1.hasNext() == true)
                        {
                            SearchApplicationNo searchApplicationNo = new SearchApplicationNo();

                            searchApplicationNo = (SearchApplicationNo)iterator1.next();

                            String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                            SearchApplicationNumberList.add(applicationnumbervalue);
                        }

                    }

                }

            }

            else if(lastname != null)
            {
                log.info("controller comes to  firstname and lastname is not null condition");

                List<SearchFirstLastName> firstlastnameworkcaseidlist = new ArrayList<SearchFirstLastName>();

                firstlastnameworkcaseidlist = searchFirstLastNameDAO.getLastName(lastname);

                Iterator iterator = firstlastnameworkcaseidlist.iterator();

                while(iterator.hasNext() == true)
                {
                    SearchFirstLastName searchFirstLastName = new SearchFirstLastName();

                    searchFirstLastName = (SearchFirstLastName)iterator.next();

                    Integer firstlastnamebasedworkcaseid = searchFirstLastName.getWorkCaseId();

                    if(firstlastnamebasedworkcaseid==null)
                    {
                        firstlastnamebasedworkcaseid = searchFirstLastName.getWrokCasePreScreenId();

                        List<SearchApplicationNoByWorkCasePrescreenId> applicationNoList = new ArrayList<SearchApplicationNoByWorkCasePrescreenId>();

                        applicationNoList = applicationNoByWorkCasePrescreenIdDAO.getApplicationNoByWorkCaseId(firstlastnamebasedworkcaseid,bpmactive);

                        Iterator iterator1 = applicationNoList.iterator();

                        while(iterator1.hasNext() == true)
                        {
                            SearchApplicationNoByWorkCasePrescreenId searchApplicationNo = new SearchApplicationNoByWorkCasePrescreenId();

                            searchApplicationNo = (SearchApplicationNoByWorkCasePrescreenId)iterator1.next();

                            String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                            SearchApplicationNumberList.add(applicationnumbervalue);
                        }
                    }

                    else {

                        List<SearchApplicationNo> applicationNoList = new ArrayList<SearchApplicationNo>();

                        applicationNoList = searchApplicationNoDAO.getApplicationNoByWorkCaseId(firstlastnamebasedworkcaseid,bpmactive);

                        Iterator iterator1 = applicationNoList.iterator();

                        while(iterator1.hasNext() == true)
                        {
                            SearchApplicationNo searchApplicationNo = new SearchApplicationNo();

                            searchApplicationNo = (SearchApplicationNo)iterator1.next();

                            String applicationnumbervalue = searchApplicationNo.getApplicationNo();

                            SearchApplicationNumberList.add(applicationnumbervalue);
                        }

                    }

                }




            }

            /*List<BPMActive> bpmactvelist = new ArrayList<BPMActive>();

            bpmactvelist = bpmActiveDAO.getBPMActiveAppNumbers(bpmactive);

            log.info("bpm active list is : {}",bpmactvelist.size());

            Iterator it = bpmactvelist.iterator();

            while(it.hasNext() == true)
            {
                BPMActive bpmActive = new BPMActive();

                bpmActive = (BPMActive)it.next();

                String bpmappnumber = bpmActive.getApplicationnumber();

                SearchApplicationNumberList.add(bpmappnumber);

            }                */
            log.info("appnumberlist size is :::: {}",SearchApplicationNumberList.size());

            appnumberlistavoidduplicates =    new ArrayList(new HashSet(SearchApplicationNumberList));

            log.info("appnumberlistavoidduplicates size is :::: {}",appnumberlistavoidduplicates.size());


        }
        catch (Exception e)
        {
            log.error("Error :",e);
        }
        finally
        {

        }

        return appnumberlistavoidduplicates;

    }


/*    public List<PEInbox> getInboxResults(String selectedTeamId,String userName)
    {
        List<PEInbox> inboxViewList = new ArrayList<PEInbox>();

        log.info("selectedTeamId---------- ",selectedTeamId);
        log.info("userName---------- ",userName);
        log.info("inboxQueryDB---------- ",inboxQueryDB);

        String  inboxQuery = "select " + peInboxQuery + " from ";
        String inboxQuery1 = inboxQuery + inboxQueryDB +" where"+ " CurrentUser = '" +userName+ "' and TeamName = '"+selectedTeamId+"'";

        sqlpequery = inboxQuery1;

        log.info("sql query for reassign search ::::::::::::::::::::::::::: {}", sqlpequery);

        //   List<PEInbox> resultQueryList = new ArrayList<PEInbox>();

        try
        {
            log.info("controller entered in to getResultSetExecution method of pedbexcecute class");
            log.info("connection url from properties file :{}",connPE);

            conn = dbContext.getConnection(connPE, peUser, pePassword);

            log.info("connection is : {}", conn.toString());

            PreparedStatement statement = conn.prepareStatement(sqlpequery);

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
                peInbox.setStep(rs.getString("Step_Code"));
                peInbox.setStatus(rs.getString("Status"));
                if(rs.getString("CurrentUser") != null)
                {
                    peInbox.setAtuser(userDAO.getUserNameById(rs.getString("CurrentUser")));
                }
                peInbox.setSlaenddate((rs.getObject("SLAEndTime1").toString().trim()));
                peInbox.setTotaltimespentatprocess(rs.getInt("TotalTimeAtProcess"));
                peInbox.setTotaltimespentatuser(rs.getInt("TotalTimeAtUser"));
                peInbox.setFwobnumber(rs.getString("F_WobNum"));

                inboxViewList.add(peInbox);

                log.info("resultQueryList for search pedbexecute class is : {}",inboxViewList);

                peInbox = null;

            }
            rs.close();
            conn.close();
            conn = null;

        }
        catch(Exception e)
        {
           log.error("Error :",e);
        }

        return inboxViewList;
    }*/

    public List<PEInbox> getReassignSearch(String teamname,String username)
    {
        log.info("controller comes to getReassignSearch method of PEDBExecute.java {}",teamname);
        log.info("controller comes to getReassignSearch method of PEDBExecute.java {}",username);

        int teamid = Integer.parseInt(teamname);

        inboxViewList = new ArrayList<PEInbox>();

        String  peSqlQuery[] = new String[2];

        String sqlquery1 = "select "+peInboxQuery+" from "+inboxQueryDB;

        String sqlquery2 = "select "+peBDMReturnQuery+" from "+bdmsearchtablename;

        /*peSqlQuery[0] = sqlquery1 + " where TeamName = '"+teamname+"'  AND CurrentUser IN ( "+username+ " ) ";

        peSqlQuery[1] = sqlquery2 + " where TeamName = '"+teamname+"'  AND CurrentUser IN ( "+username+ ") ";*/

        peSqlQuery[0] = sqlquery1 + " where CurrentUser IN ( "+username+ " ) ";

        peSqlQuery[1] = sqlquery2 + " where CurrentUser IN ( "+username+ ") ";

        log.info("sqlquery is ::::::::::::: {}",peSqlQuery[0]);
        log.info("sqlquery is ::::::::::::: {}",peSqlQuery[1]);

        try
        {
            conn = dbContext.getConnection(connPE, peUser, pePassword);

            for(int i = 0; i<2; i++)
            {
                log.info("i value is :::::: {}",i);

                if(peSqlQuery[i] != null)
                {
                    log.info("peSqlQuery is ::::::::: {}",peSqlQuery[i]);

                    log.info("connection is : {}", conn.toString());

                    PreparedStatement statement = conn.prepareStatement(peSqlQuery[i]);

                    log.info("statement is :::::::: (1): {}",statement.toString());

                    rs = statement.executeQuery();

                    log.info("resultset is ::::::::::::(1) {}", rs.getRow());

                    while (rs.next())
                    {
                        PEInbox peInbox = new PEInbox();

                        peInbox.setReceiveddate((rs.getObject("ReceivedDate1").toString().trim()));
                        peInbox.setAtuserteam(rs.getString("TeamName"));
                        peInbox.setApplicationno(rs.getString("AppNumber"));
                        peInbox.setName(rs.getString("BorrowerName"));
                        peInbox.setProductgroup(rs.getString("ProductGroup"));
                        peInbox.setRequestTypeStr(rs.getString("RequestTypeStr"));
                        peInbox.setStepId(Long.parseLong(rs.getString("Step_Code")));
                        peInbox.setStatus(rs.getString("Status"));
                        if(rs.getString("PreviousUser") != null)
                        {
                            peInbox.setFromuser(userDAO.getUserNameById(rs.getString("PreviousUser")));
                        }

                        if(rs.getString("CurrentUser") != null)
                        {
                            peInbox.setAtuser(userDAO.getUserNameById(rs.getString("CurrentUser")));
                        }
                        peInbox.setAppointmentdate((rs.getObject("AppointmentDate1").toString().trim()));
                        peInbox.setDoalevel(rs.getString("DOALevel"));
                        peInbox.setAction(rs.getString("PreviousAction"));
                        peInbox.setSlastatus(rs.getString("SLAStatus"));
                        peInbox.setSlaenddate((rs.getObject("SLAEndTime1").toString().trim()));
                        peInbox.setTotaltimespentatprocess(rs.getInt("TotalTimeAtProcess"));
                        peInbox.setTotaltimespentatuser(rs.getInt("TotalTimeAtUser"));
                        peInbox.setStatuscode(rs.getString("StatusCode"));
                        peInbox.setFwobnumber(rs.getString("F_WobNum"));
                        peInbox.setLocked(rs.getInt("F_Locked"));
                        peInbox.setStep(rs.getString("F_StepName"));
                        inboxViewList.add(peInbox);

                        log.info("resultQueryList pedbexecute class is(1) : {}",inboxViewList.size());

                        peInbox = null;

                    }


                }
            }
            rs.close();
            conn.close();
            conn = null;

        }
        catch (Exception e)
        {
            log.error("Error :",e);
        }
        finally
        {

        }

        return inboxViewList;
    }



    public List<PERoster> queryForChangeOwner(String applicationNos)
    {
        List<PERoster> changeOwerViewList = new ArrayList<PERoster>();
        tableName = actionDAO.getRosterTableName();
        query = " select "+queryForRosterColumns+" from ";

        queryChangeOwner = query +prefix+"."+tableName+ " where AppNumber in(" + applicationNos + ")";

        sqlpequery = queryChangeOwner;
        log.info("sql query for Changeowner search ::::::::::::::::::::::::::: {}", sqlpequery);

        try
        {
            conn = dbContext.getConnection(connPE, peUser, pePassword);
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

                if(rs.getString("CurrentUser") != null)
                {
                    peRoster.setCurrentUser(userDAO.getUserNameById(rs.getString("CurrentUser")));
                }

                peRoster.setSlastatus(rs.getString("SLAStatus"));
                peRoster.setSLAEndTime(rs.getObject("SLAEndTime1").toString().trim());
                peRoster.setTotalTimeAtUser(rs.getString("TotalTimeAtUser"));
                peRoster.setTotalTimeAtProcess(rs.getString("TotalTimeAtProcess"));
                peRoster.setF_WobNum(rs.getString("F_WobNum"));
                peRoster.setStep(rs.getString("F_StepName"));
                changeOwerViewList.add(peRoster);
                peRoster = null;


            }

        } catch (Exception e) {

            log.error("Error :",e);

        } finally {

            peRoster = null;
            sqlpequery = null;
            userName = null;
            tableName = null;
            query = null;

            statement = null;
        }
        log.info("changeOwerViewList::::{}",changeOwerViewList);
        return changeOwerViewList;
    }



}
