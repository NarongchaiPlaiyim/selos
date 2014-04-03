package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UserTeam;
import com.clevel.selos.model.db.relation.RelTeamUserDetails;
import com.clevel.selos.model.view.ReassignTeamNameId;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import com.clevel.selos.model.db.master.User;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserTeamDAO extends GenericDAO<UserTeam, Integer>
{
    @Inject
    @SELOS
    Logger log;

    int teamTypeValue = 0;

    List<ReassignTeamNameId> matcheduserteamslist;

    List<ReassignTeamNameId> popUpUserTeamList;

    List<String> matchedusernames;

    List<String> popUpMatchUserNames;

    @Inject
    public UserTeamDAO()
    {
    }

    public List<ReassignTeamNameId> getUserteams(int teamId,String rasearchcase)
    {
        matcheduserteamslist = new ArrayList<ReassignTeamNameId>();

        List<UserTeam> userteamslist = new ArrayList<UserTeam>();

        log.info("controller comes to getUserteams method of UserTeamDAO class");

        Criteria criteriaTeamType = getSession().createCriteria(UserTeam.class).setProjection(Projections.projectionList().add(Projections.property("team_type"),"team_type"));

        criteriaTeamType.add(Restrictions.eq("id", teamId)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

        List teamTypeList = criteriaTeamType.list();

        log.info("teamTypeList in getUserteams method of UserTeamDAO class .... {}", teamTypeList.toString());

        Iterator iterator1 = teamTypeList.iterator();

        while(iterator1.hasNext() == true)
        {
            UserTeam teamType = new UserTeam();

            teamType = (UserTeam)iterator1.next();

            teamTypeValue = teamType.getTeam_type();

        }

        log.info("teamTypeValue in getUserteams method of UserTeamDAO class .... {}", teamTypeValue);

        if(teamTypeValue == 2)
        {
            matcheduserteamslist.clear();

            log.info("controller comes to teamTypeValue is 2 condition {}",teamTypeValue);

            Criteria criteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

            criteria.add(Restrictions.eq("tlThId", teamId));

            criteria.add(Restrictions.eq("raSearchCase",rasearchcase)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamUserList = criteria.list();

            log.info("TeamUserList .... {}", teamUserList.toString());

            Iterator iterator2 = teamUserList.iterator();

            while(iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();

                relTeamUserDetails1 = (RelTeamUserDetails)iterator2.next();

                int teamLeadId = relTeamUserDetails1.getTeam_Id();

                Criteria criteriateamname = getSession().createCriteria(UserTeam.class);

                criteriateamname.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", teamLeadId)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                List userteamnameslist = criteriateamname.list();

                Iterator itr = userteamnameslist.iterator();

                while(itr.hasNext() == true)
                {
                    UserTeam userTeam = new UserTeam();

                    ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                    userTeam = (UserTeam)itr.next();

                    String userteamname = userTeam.getTeam_name();

                    int userteamnameid =  userTeam.getId();

                    reassignTeamNameId.setTeamid(userteamnameid);

                    reassignTeamNameId.setTeamname(userteamname);

                    matcheduserteamslist.add(reassignTeamNameId);

                    reassignTeamNameId = null;
                }

            }
        }
       if(teamTypeValue == 3)
        {
            matcheduserteamslist.clear();

            log.info("controller comes to teamTypeValue is 3 condition {}",teamTypeValue);

            Criteria criteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

            criteria.add(Restrictions.eq("tlThId", teamId)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamheaduserlist = criteria.list();

            Iterator headlistiterator = teamheaduserlist.iterator();

            while(headlistiterator.hasNext() == true)
            {
                log.info("controller in first while loop condition 3");

                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();

                relTeamUserDetails = (RelTeamUserDetails)headlistiterator.next();

                int teamheadid = relTeamUserDetails.getTeam_Id();

                Criteria headbasedleadcriteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

                headbasedleadcriteria.add(Restrictions.eq("tlThId", teamheadid));

                headbasedleadcriteria.add(Restrictions.eq("raSearchCase",rasearchcase)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List headbasedleadlist = headbasedleadcriteria.list();

                log.info("headbasedleadlist :::::::::::::::: {}",headbasedleadlist.size());

                Iterator teamleadlistiterator = headbasedleadlist.iterator();

                while(teamleadlistiterator.hasNext() == true)
                {
                    log.info("controller in second while loop condition 3");

                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();

                    relTeamUserDetails1 = (RelTeamUserDetails)teamleadlistiterator.next();

                    int teamleadid = relTeamUserDetails1.getTeam_Id();

                    Criteria criteriateamname = getSession().createCriteria(UserTeam.class);

                    criteriateamname.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", teamleadid)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                    List userteamnameslist = criteriateamname.list();

                    Iterator itr = userteamnameslist.iterator();

                    while(itr.hasNext() == true)
                    {
                        UserTeam userTeam = new UserTeam();

                        ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                        userTeam = (UserTeam)itr.next();

                        String userteamname = userTeam.getTeam_name();

                        int userteamnameid = userTeam.getId();

                        reassignTeamNameId.setTeamid(userteamnameid);

                        reassignTeamNameId.setTeamname(userteamname);

                        matcheduserteamslist.add(reassignTeamNameId);

                        reassignTeamNameId = null;
                    }

                }

            }

        }
        if(teamTypeValue == 4)
        {
            matcheduserteamslist.clear();

            log.info("controller entered in to if condition teamTypevalue is 4 ",teamTypeValue);

            Criteria criteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

            criteria.add(Restrictions.eq("tlThId", teamId)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamheaduserlist = criteria.list();

            Iterator headlistiterator = teamheaduserlist.iterator();

            while(headlistiterator.hasNext() == true)
            {
                log.info("controller in first while loop condition 4");

                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();

                relTeamUserDetails = (RelTeamUserDetails)headlistiterator.next();

                int teamheadid = relTeamUserDetails.getTeam_Id();

                Criteria criteriateamname = getSession().createCriteria(UserTeam.class);

                criteriateamname.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", teamheadid)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                List userteamnameslist = criteriateamname.list();

                Iterator itr = userteamnameslist.iterator();

                while(itr.hasNext() == true)
                {
                    UserTeam userTeam = new UserTeam();

                    ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                    userTeam = (UserTeam)itr.next();

                    String userteamname = userTeam.getTeam_name();

                    int userteamnameid = userTeam.getId();

                    reassignTeamNameId.setTeamname(userteamname);

                    reassignTeamNameId.setTeamid(userteamnameid);

                    matcheduserteamslist.add(reassignTeamNameId);
                }

                Criteria headbasedleadcriteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

                headbasedleadcriteria.add(Restrictions.eq("tlThId", teamheadid)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List headbasedleadlistin4codn = headbasedleadcriteria.list();

                log.info("headbasedleadlistin4codn is :::::::::::  {}",headbasedleadlistin4codn.size());

                Iterator headbasedleadlistiterator4codn = headbasedleadlistin4codn.iterator();

                while(headbasedleadlistiterator4codn.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();

                    relTeamUserDetails1 = (RelTeamUserDetails)headbasedleadlistiterator4codn.next();

                    int teamleadsin4codn = relTeamUserDetails1.getTeam_Id();

                    Criteria leadbaseduser4codncriteria =  getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"),"team_Id"));

                    leadbaseduser4codncriteria.add(Restrictions.eq("tlThId", teamleadsin4codn));

                    leadbaseduser4codncriteria.add(Restrictions.eq("raSearchCase",rasearchcase)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                    List leadbaseduserslist4codn = leadbaseduser4codncriteria.list();

                    Iterator leadbasedusersiterator4codn = leadbaseduserslist4codn.iterator();

                    while(leadbasedusersiterator4codn.hasNext() == true)
                    {
                        RelTeamUserDetails relTeamUserDetails2 = new RelTeamUserDetails();

                        relTeamUserDetails2 = (RelTeamUserDetails)leadbasedusersiterator4codn.next();

                        int leadbaseduserteamid4codn = relTeamUserDetails2.getTeam_Id();

                        Criteria leadbaseduserteamname4codncriteria = getSession().createCriteria(UserTeam.class);

                        leadbaseduserteamname4codncriteria.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", leadbaseduserteamid4codn)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                        List userteamnames4codn = leadbaseduserteamname4codncriteria.list();

                        Iterator userteamnames4codniterator = userteamnames4codn.iterator();

                        while(userteamnames4codniterator.hasNext() == true)
                        {
                            UserTeam userTeam1 = new UserTeam();

                            ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                            userTeam1 = (UserTeam)userteamnames4codniterator.next();

                            String userteamnamess = userTeam1.getTeam_name();

                            int userteamnamessid = userTeam1.getId();

                            reassignTeamNameId.setTeamid(userteamnamessid);

                            reassignTeamNameId.setTeamname(userteamnamess);

                            matcheduserteamslist.add(reassignTeamNameId);

                            reassignTeamNameId = null;
                        }
                    }

                }

            }

        }

        return matcheduserteamslist;

    }

    public String getUserIdByName(String userName)
    {

        Criteria criteria1 = getSession().createCriteria(User.class);
        criteria1.setProjection(Projections.projectionList().add(Projections.property("id"), "id"));
        criteria1.add(Restrictions.eq("userName",userName).ignoreCase()).setResultTransformer(Transformers.aliasToBean(User.class));
        List usernamebasedteamid = criteria1.list();
        Iterator iterator1 = usernamebasedteamid.iterator();
        String userId = null;
        while(iterator1.hasNext() == true)
        {
            User user = new User();
            user = (User)iterator1.next();
            userId = user.getId();
            user = null;
        }

        //userName = userId+" - "+userName;

        return userId;

    }

    public List<String> getUsers(int teamid)
    {
        matchedusernames = new ArrayList<String>();

        log.info("controller entered in to getUsers method of UserTeamDAO class : {}",teamid);

            Criteria criteria1 = getSession().createCriteria(User.class);

            criteria1.setProjection(Projections.projectionList().add(Projections.property("userName"), "userName"));

            criteria1.add(Restrictions.eq("team.id", teamid)).setResultTransformer(Transformers.aliasToBean(User.class));

            List usernamebasedteamid = criteria1.list();

            Iterator iterator1 = usernamebasedteamid.iterator();

            while(iterator1.hasNext() == true)
            {
                User user = new User();

                user = (User)iterator1.next();

                String username = user.getUserName();

                log.info("username is :::: {}",username);

                matchedusernames.add(username);
            }

            Criteria criteria2 = getSession().createCriteria(RelTeamUserDetails.class);

            criteria2.setProjection(Projections.projectionList().add(Projections.property("tlThId"),"tlThId"));

            criteria2.add(Restrictions.eq("team_Id",teamid)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamleadusernamesidlist = criteria2.list();

            Iterator iterator2 = teamleadusernamesidlist.iterator();

            while(iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();

                relTeamUserDetails = (RelTeamUserDetails)iterator2.next();

                int teamleadid = relTeamUserDetails.getTlThId();

                Criteria criteria3 = getSession().createCriteria(User.class);

                criteria3.setProjection(Projections.projectionList().add(Projections.property("userName"),"userName")) ;

                criteria3.add(Restrictions.eq("team.id",teamleadid)).setResultTransformer(Transformers.aliasToBean(User.class));

                List teamleadusernameslist = criteria3.list();

                log.info("teamleadusernameslist in commented place is ::::::: {}",teamleadusernameslist.size());

                Iterator iterator3 = teamleadusernameslist.iterator();

                while(iterator3.hasNext() == true)
                {
                    User user1 = new User();

                    user1 = (User)iterator3.next();

                    String teamleadname = user1.getUserName();

                    log.info("teamlead name is commented place is ::::::{}",teamleadname);

                    matchedusernames.add(teamleadname);
                }
            }

        String all = "ALL";

        if(matchedusernames.size() > 0)
        {
            matchedusernames.add(all);
        }


        return matchedusernames;
    }


    public List<ReassignTeamNameId> popUpUserTeamNames(int teamId,String rasearchuser)
    {
        popUpUserTeamList = new ArrayList<ReassignTeamNameId>();

        log.info("controller comes to popUpUserTeamNames method of UserTeamDAO class");

        Criteria criteriaTeamType = getSession().createCriteria(UserTeam.class).setProjection(Projections.projectionList().add(Projections.property("team_type"),"team_type"));

        criteriaTeamType.add(Restrictions.eq("id", teamId)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

        List teamTypeList = criteriaTeamType.list();

        log.info("teamTypeList in popUpUserTeamNames method of UserTeamDAO class .... {}", teamTypeList.toString());

        Iterator iterator1 = teamTypeList.iterator();

        while(iterator1.hasNext() == true)
        {
            UserTeam teamType = new UserTeam();

            teamType = (UserTeam)iterator1.next();

            teamTypeValue = teamType.getTeam_type();

        }

        log.info("teamTypeValue in popUpUserTeamNames method of UserTeamDAO class .... {}", teamTypeValue);

        if(teamTypeValue == 2)
        {
            popUpUserTeamList.clear();

            log.info("controller comes to teamTypeValue is 2 condition {}",teamTypeValue);

            Criteria criteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

            criteria.add(Restrictions.eq("tlThId", teamId));

            criteria.add(Restrictions.eq("raSearchUser",rasearchuser)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamUserList = criteria.list();

            log.info("TeamUserList .... {}", teamUserList.toString());

            Iterator iterator2 = teamUserList.iterator();

            while(iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();

                relTeamUserDetails1 = (RelTeamUserDetails)iterator2.next();

                int teamLeadId = relTeamUserDetails1.getTeam_Id();

                Criteria criteriateamname = getSession().createCriteria(UserTeam.class);

                criteriateamname.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", teamLeadId)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                List userteamnameslist = criteriateamname.list();

                Iterator itr = userteamnameslist.iterator();

                while(itr.hasNext() == true)
                {
                    UserTeam userTeam = new UserTeam();

                    ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                    userTeam = (UserTeam)itr.next();

                    String userteamname = userTeam.getTeam_name();

                    int userteamanmeid = userTeam.getId();

                    reassignTeamNameId.setTeamname(userteamname);

                    reassignTeamNameId.setTeamid(userteamanmeid);

                    popUpUserTeamList.add(reassignTeamNameId);

                    reassignTeamNameId = null;
                }

            }
        }
        if(teamTypeValue == 3)
        {
            popUpUserTeamList.clear();

            log.info("controller comes to teamTypeValue is 3 condition {}",teamTypeValue);

            Criteria criteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

            criteria.add(Restrictions.eq("tlThId", teamId)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamheaduserlist = criteria.list();

            Iterator headlistiterator = teamheaduserlist.iterator();

            while(headlistiterator.hasNext() == true)
            {
                log.info("controller in first while loop condition 3");

                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();

                relTeamUserDetails = (RelTeamUserDetails)headlistiterator.next();

                int teamheadid = relTeamUserDetails.getTeam_Id();

                Criteria headbasedleadcriteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

                headbasedleadcriteria.add(Restrictions.eq("tlThId", teamheadid));

                headbasedleadcriteria.add(Restrictions.eq("raSearchUser",rasearchuser)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List headbasedleadlist = headbasedleadcriteria.list();

                log.info("headbasedleadlist :::::::::::::::: {}",headbasedleadlist.size());

                Iterator teamleadlistiterator = headbasedleadlist.iterator();

                while(teamleadlistiterator.hasNext() == true)
                {
                    log.info("controller in second while loop condition 3");

                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();

                    relTeamUserDetails1 = (RelTeamUserDetails)teamleadlistiterator.next();

                    int teamleadid = relTeamUserDetails1.getTeam_Id();

                    Criteria criteriateamname = getSession().createCriteria(UserTeam.class);

                    criteriateamname.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", teamleadid)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                    List userteamnameslist = criteriateamname.list();

                    log.info("userteamnameslist size is ::: {}",userteamnameslist.size());

                    Iterator itr = userteamnameslist.iterator();

                    while(itr.hasNext() == true)
                    {
                        UserTeam userTeam = new UserTeam();

                        ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                        userTeam = (UserTeam)itr.next();

                        String userteamname = userTeam.getTeam_name();

                        int userteamnameid = userTeam.getId();

                        reassignTeamNameId.setTeamid(userteamnameid);

                        reassignTeamNameId.setTeamname(userteamname);

                        popUpUserTeamList.add(reassignTeamNameId);

                        reassignTeamNameId = null;
                    }

                }

            }

        }
        if(teamTypeValue == 4)
        {
            popUpUserTeamList.clear();

            log.info("controller entered in to if condition teamTypevalue is 4 ",teamTypeValue);

            Criteria criteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

            criteria.add(Restrictions.eq("tlThId", teamId)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamheaduserlist = criteria.list();

            Iterator headlistiterator = teamheaduserlist.iterator();

            while(headlistiterator.hasNext() == true)
            {
                log.info("controller in first while loop condition 4");

                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();

                relTeamUserDetails = (RelTeamUserDetails)headlistiterator.next();

                int teamheadid = relTeamUserDetails.getTeam_Id();

                Criteria criteriateamname = getSession().createCriteria(UserTeam.class);

                criteriateamname.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", teamheadid)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                List userteamnameslist = criteriateamname.list();

                Iterator itr = userteamnameslist.iterator();

                while(itr.hasNext() == true)
                {
                    UserTeam userTeam = new UserTeam();

                    ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                    userTeam = (UserTeam)itr.next();

                    String userteamname = userTeam.getTeam_name();

                    int userteamnameid = userTeam.getId();

                    reassignTeamNameId.setTeamid(userteamnameid);

                    reassignTeamNameId.setTeamname(userteamname);

                    popUpUserTeamList.add(reassignTeamNameId);
                }

                //--------------

                Criteria headbasedleadcriteria = getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"), "team_Id"));

                headbasedleadcriteria.add(Restrictions.eq("tlThId", teamheadid)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                List headbasedleadlistin4codn = headbasedleadcriteria.list();

                Iterator headbasedleadlistiterator4codn = headbasedleadlistin4codn.iterator();

                while(headbasedleadlistiterator4codn.hasNext() == true)
                {
                    RelTeamUserDetails relTeamUserDetails1 = new RelTeamUserDetails();

                    relTeamUserDetails1 = (RelTeamUserDetails)headbasedleadlistiterator4codn.next();

                    int teamleadsin4codn = relTeamUserDetails1.getTeam_Id();

                    Criteria leadbaseduser4codncriteria =  getSession().createCriteria(RelTeamUserDetails.class).setProjection(Projections.projectionList().add(Projections.property("team_Id"),"team_Id"));

                    leadbaseduser4codncriteria.add(Restrictions.eq("tlThId", teamleadsin4codn));

                    leadbaseduser4codncriteria.add(Restrictions.eq("raSearchUser",rasearchuser)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

                    List leadbaseduserslist4codn = leadbaseduser4codncriteria.list();

                    Iterator leadbasedusersiterator4codn = leadbaseduserslist4codn.iterator();

                    while(leadbasedusersiterator4codn.hasNext() == true)
                    {
                        RelTeamUserDetails relTeamUserDetails2 = new RelTeamUserDetails();

                        relTeamUserDetails2 = (RelTeamUserDetails)leadbasedusersiterator4codn.next();

                        int leadbaseduserteamid4codn = relTeamUserDetails2.getTeam_Id();

                        Criteria leadbaseduserteamname4codncriteria = getSession().createCriteria(UserTeam.class);

                        leadbaseduserteamname4codncriteria.setProjection(Projections.projectionList().add(Projections.property("team_name"), "team_name").add(Projections.property("id"), "id")).add(Restrictions.eq("id", leadbaseduserteamid4codn)).setResultTransformer(Transformers.aliasToBean(UserTeam.class));

                        List userteamnames4codn = leadbaseduserteamname4codncriteria.list();

                        Iterator userteamnames4codniterator = userteamnames4codn.iterator();

                        while(userteamnames4codniterator.hasNext() == true)
                        {
                            UserTeam userTeam1 = new UserTeam();

                            ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

                            userTeam1 = (UserTeam)userteamnames4codniterator.next();

                            String userteamnamess = userTeam1.getTeam_name();

                            int userteamnamessid = userTeam1.getId();

                            reassignTeamNameId.setTeamid(userteamnamessid);

                            reassignTeamNameId.setTeamname(userteamnamess);

                            popUpUserTeamList.add(reassignTeamNameId);

                            reassignTeamNameId = null;
                        }
                    }

                }

            }

        }

        return popUpUserTeamList;

    }

    public List<String> getPopUsers(int teamnameid)
    {
        popUpMatchUserNames = new ArrayList<String>();

        log.info("controller comes to getPopUsers method of UserTeamDAO class {}",teamnameid);

            Criteria criteria1 = getSession().createCriteria(User.class);

            criteria1.setProjection(Projections.projectionList().add(Projections.property("userName"), "userName"));

            criteria1.add(Restrictions.eq("team.id", teamnameid)).setResultTransformer(Transformers.aliasToBean(User.class));

            List usernamebasedteamid = criteria1.list();

            Iterator iterator1 = usernamebasedteamid.iterator();

            while(iterator1.hasNext() == true)
            {
                User user = new User();

                user = (User)iterator1.next();

                String username = user.getUserName();

                popUpMatchUserNames.add(username);
            }

            Criteria criteria2 = getSession().createCriteria(RelTeamUserDetails.class);

            criteria2.setProjection(Projections.projectionList().add(Projections.property("tlThId"),"tlThId"));

            criteria2.add(Restrictions.eq("team_Id",teamnameid)).setResultTransformer(Transformers.aliasToBean(RelTeamUserDetails.class));

            List teamleadusernamesidlist = criteria2.list();

            Iterator iterator2 = teamleadusernamesidlist.iterator();

            while(iterator2.hasNext() == true)
            {
                RelTeamUserDetails relTeamUserDetails = new RelTeamUserDetails();

                relTeamUserDetails = (RelTeamUserDetails)iterator2.next();

                int teamleadid = relTeamUserDetails.getTlThId();

                Criteria criteria3 = getSession().createCriteria(User.class);

                criteria3.setProjection(Projections.projectionList().add(Projections.property("userName"),"userName")) ;

                criteria3.add(Restrictions.eq("team.id",teamleadid)).setResultTransformer(Transformers.aliasToBean(User.class));

                List teamleadusernameslist = criteria3.list();

                Iterator iterator3 = teamleadusernameslist.iterator();

                while(iterator3.hasNext() == true)
                {
                    User user1 = new User();

                    user1 = (User)iterator3.next();

                    String teamleadname = user1.getUserName();

                    popUpMatchUserNames.add(teamleadname);
                }
            }

        return popUpMatchUserNames;

    }
}
