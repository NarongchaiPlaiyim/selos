package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.DoaPriorityUserNamesDAO;
import com.clevel.selos.dao.master.FetchQueueNameDAO;
import com.clevel.selos.dao.master.InboxTableNameDAO;
import com.clevel.selos.dao.master.QueueNameIdDAO;
import com.clevel.selos.dao.relation.UserToAuthorizationDOADAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.tool.SQLDBConnection;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.DoaPriorityUserNames;
import com.clevel.selos.model.db.master.QueueNameId;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.Config;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@ManagedBean(name = "peDBCount")
public class PEDBTableCount
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
    @Config(name = "interface.pe.sql.count")
    String peCount;

    @Inject
    SQLDBConnection dbContext;

    Connection conn = null;
    transient ResultSet rs = null;

    String sqlpequery = null;

    String inboxname;

    private UserDetail userDetail;

    PEDBExecute pedbExecute;

    @Inject
    InboxTableNameDAO inboxTableNameDAO;

    @Inject
    QueueNameIdDAO queueNameIdDAO;

    @Inject
    FetchQueueNameDAO fetchQueueNameDAO;

    @Inject
    UserToAuthorizationDOADAO userToAuthorizationDOADAO ;

    @Inject
    DoaPriorityUserNamesDAO doaPriorityUserNamesDAO;


    public PEDBTableCount()
    {


    }

    public int PEInboxCount(String inboxname)
    {

        int inboxcount = 0;

        try
        {

            String sqlpequery = getSqlQuery(inboxname);

            log.info("sql count query in peInboxCount method is : {}",sqlpequery);

            if(sqlpequery != "" && sqlpequery.length() > 0)
            {

            inboxcount = resultSetExcution(sqlpequery);
            }
            else
            {
                inboxcount = 0;
            }

        }
        catch(Exception e)
        {

        }
        finally
        {
            sqlpequery = null;
        }

        return inboxcount;

    }


    public String getSqlQuery(String inboxname)
    {
        log.info("controller comes to getSqlpeQuery method OF petablecount is :{}",inboxname);

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try
        {
            log.info("controller in getSqlQuery method of peDBTable Count class : {}",inboxname);

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

            log.info("queue name obtained is ::::::::::::: {}",queuename);

            String username = userDetail.getUserName();

            log.info("username is :::::::::::::::{}",username);

            int Teamid = userDetail.getTeamid();

            String TeamName = String.valueOf(Teamid);

            log.info("TeamName is ::::::::::  {}",TeamName);

            List<DoaPriorityUserNames> doaPriorityUserNames11 = doaPriorityUserNamesDAO.getDoaPriorityUserNames();

            Iterator iterator2 = doaPriorityUserNames11.iterator();

            boolean flag = false;



            String  doapriorityusername13 = null;

            while(iterator2.hasNext() == true)
            {
                DoaPriorityUserNames doaPriorityUserNames12 = new DoaPriorityUserNames();

                doaPriorityUserNames12 = (DoaPriorityUserNames)iterator2.next();

                doapriorityusername13 =  doaPriorityUserNames12.getUserid();

                if(doapriorityusername13.equalsIgnoreCase(username))
                {
                    flag = true;
                }

            }

            if(flag)
            {

                List<AuthorizationDOA> doaprioritylist =   userToAuthorizationDOADAO.findbyusernameasid(username);

                List<AuthorizationDOA>  doaprioritylist1 = new ArrayList(new HashSet(doaprioritylist)) ;

                log.info("list :::::::{}",doaprioritylist1.size());

                Iterator iterator1 = doaprioritylist1.iterator();

                int doapriority = 0;

                if(iterator1.hasNext())
                {
                    AuthorizationDOA authorizationDOA = new AuthorizationDOA();

                    authorizationDOA = (AuthorizationDOA)iterator1.next();

                    doapriority = authorizationDOA.getDoapriorityorder();

                }

                doaprioritystringvalue = String.valueOf(doapriority);

                log.info("doaprioritystringvalue value is :::::::::::::::: : {} ",doaprioritystringvalue);
            }

            String  sqlpequery1 = "select " + peCount + " from ";

            if(filterstring.equalsIgnoreCase("NA") || filterstring.equalsIgnoreCase(""))
            {

                String sqlpequery2 = sqlpequery1 +prefix+ "." + queuename ;

                sqlpequery =   sqlpequery2;

                log.info("sqlquery when filter condition is NA(count) : {}",sqlpequery);
            }
            else
            {

                String filterstring1 = null;

                String filterstring2 = null;

                if(filterstring.contains("LOGGEDINUSERNAME"))
                {

                    filterstring1 = filterstring.replaceAll("LOGGEDINUSERNAME",username);

                    log.info("filter string in loop when filtercondition is LOGGEDINUSERNAME (count):::::::::::::: {}",filterstring1);

                    String sqlpequery2 = sqlpequery1 + prefix +"." + queuename + " where " + filterstring1 ;

                    sqlpequery =   sqlpequery2;

                    log.info("sqlquery when filter condition is LOGGEDINUSERNAME (count): {}",sqlpequery);

                }
                else if(filterstring.contains("LOGGEDINUSERTEAMNAME"))
                {
                    filterstring1 = filterstring.replaceAll("LOGGEDINUSERTEAMNAME",TeamName);

                    log.info("filter string  in loop when filtercondition is LOGGEDINUSERTEAMNAME(count) :::::::::::::: {}",filterstring1);

                    String sqlpequery2 = sqlpequery1 + prefix +"." + queuename + " where " + filterstring1  ;

                    sqlpequery =   sqlpequery2;

                    log.info("sqlquery when filter condition is LOGGEDINUSERNAME(count) : {}",sqlpequery);
                }
                if(filterstring.contains("LOGGEDINUSERDOAPRIORITYORDER"))
                {
                    filterstring2 = filterstring1.replaceAll("LOGGEDINUSERDOAPRIORITYORDER",doaprioritystringvalue);

                    log.info("filter string  in loop when filtercondition is LOGGEDINUSERDOAPRIORITYORDER(Count) :::::::::::::: {}",filterstring2);

                    String sqlpequery2 = sqlpequery1 + prefix +"." + queuename + " where " + filterstring2  ;

                    sqlpequery =   sqlpequery2;

                }

                log.info("sql query is in inbox : {}", sqlpequery);


            }
            return sqlpequery;

        }
        catch(Exception e)
        {

        }
        finally
        {
            sqlpequery = null;
        }

        return sqlpequery;
    }

    public int resultSetExcution(String sqlquery)
    {
        int rowcount = 0;

        try
        {
            log.info("controller entered in to getResultSetExecution method of pedbexcecute class");
            log.info("connection url from properties file :{}",connPE);

            conn = dbContext.getConnection(connPE, peUser, pePassword);

            log.info("connection is : {}", conn.toString());

            Statement  statement = conn.createStatement();

            log.info("statement is : {}",statement);

            log.info("sqlquery is : {}",sqlquery);

            if(sqlquery != "" && sqlquery.length() > 0)
            {

            rs = statement.executeQuery(sqlquery);

            log.info("resultset is : {}", rs);

            log.info("result set row data type is : {}", rs.getMetaData().getColumnClassName(1));
            }

            try
            {

                while(rs.next())
                {
                    rowcount = rs.getInt(1);
                }

            }
            catch (Exception e)
            {

            }

            return rowcount;
        }
        catch(Exception e)
        {
            log.info("exception occured in getting count value : {}",e);

            return 0;
        }


    }

}
